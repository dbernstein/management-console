/*
 * Copyright (c) 2009-2010 DuraSpace. All rights reserved.
 */
package org.duracloud.account.app.controller;

import org.duracloud.account.common.domain.AccountInfo;
import org.duracloud.account.common.domain.DuracloudUser;
import org.duracloud.account.db.IdUtil;
import org.duracloud.account.db.error.DBNotFoundException;
import org.duracloud.account.util.AccountService;
import org.duracloud.account.util.error.AccountNotFoundException;
import org.duracloud.account.util.error.SubdomainAlreadyExistsException;
import org.duracloud.storage.domain.StorageProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @contributor "Daniel Bernstein (dbernstein@duraspace.org)"
 * 
 */
@Controller
@Lazy
public class AccountController extends AbstractAccountController {

    public static final String NEW_ACCOUNT_FORM_KEY = "newAccountForm";

    @Autowired
    private IdUtil idUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = { ACCOUNT_PATH }, method = RequestMethod.GET)
    public String getHome(@PathVariable int accountId, Model model)
        throws AccountNotFoundException {
        loadAccountInfo(accountId, model);
        return ACCOUNT_HOME;
    }

    @RequestMapping(value = { ACCOUNT_PATH + "/statement" }, method = RequestMethod.GET)
    public String getStatement(@PathVariable int accountId, Model model)
        throws AccountNotFoundException {
        loadAccountInfo(accountId, model);
        return "account-statement";
    }

    @RequestMapping(value = { ACCOUNT_PATH + "/instance" }, method = RequestMethod.GET)
    public String getInstance(@PathVariable int accountId, Model model)
        throws AccountNotFoundException {
        loadAccountInfo(accountId, model);
        loadInstanceInfo(accountId, model);
        return "account-instance";
    }

    @RequestMapping(value = { NEW_MAPPING }, method = RequestMethod.GET)
    public String openAddForm(Model model) throws DBNotFoundException {
        log.info("serving up new AccountForm");
        model.addAttribute(NEW_ACCOUNT_FORM_KEY, new NewAccountForm());
        addUserToModel(model);
        return NEW_ACCOUNT_VIEW;
    }

    private void addUserToModel(Model model) throws DBNotFoundException {
        model.addAttribute(UserController.USER_KEY, getUser());
    }

    @RequestMapping(value = { NEW_MAPPING }, method = RequestMethod.POST)
    public String add(
        @ModelAttribute(NEW_ACCOUNT_FORM_KEY) @Valid NewAccountForm newAccountForm,
        BindingResult result, Model model) throws Exception {
        DuracloudUser user = getUser();

        if (!result.hasErrors()) {
            try {

                int paymentInfoId = -1;
                Set<Integer> instanceIds = null;
                AccountInfo accountInfo =
                    new AccountInfo(idUtil.newAccountId(),
                        newAccountForm.getSubdomain(),
                        newAccountForm.getAcctName(),
                        newAccountForm.getOrgName(),
                        newAccountForm.getDepartment(),
                        paymentInfoId,
                        instanceIds,
                        new HashSet<StorageProviderType>(newAccountForm.getStorageProviders()));

                AccountService service =
                    this.accountManagerService.createAccount(accountInfo, user);

                // FIXME seems like there is some latency between successfully
                // creating
                // a new user and the user actually being visible to subsequent
                // calls
                // to the repository (due to amazon async I believe).
                sleepMomentarily();

                int id = service.retrieveAccountInfo().getId();
                String idText = Integer.toString(id);
                user = getUser();

                // reauthenticate
                // FIXME I'm not sure that this is the right way to accomplish
                // updating the account rights within the current security
                // context.
                // The question: will the password be available in this context?
                // Or will it be hashed or null?
                reauthenticate(user, authenticationManager);
                return formatAccountRedirect(idText, "/");
            } catch (SubdomainAlreadyExistsException ex) {
                result.addError(new ObjectError("subdomain",
                    "The subdomain you selected is already in use. Please choose another."));
            } catch (DBNotFoundException e) {
                log.error(e.getMessage(), e);
                throw new Error("This should never happen", e);
            }
        }

        addUserToModel(model);
        return NEW_ACCOUNT_VIEW;

    }

    /**
     * @return
     */
    private DuracloudUser getUser() throws DBNotFoundException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getName();
        return this.userService.loadDuracloudUserByUsername(username);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(
        AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public IdUtil getIdUtil() {
        return idUtil;
    }

    public void setIdUtil(IdUtil idUtil) {
        this.idUtil = idUtil;
    }

}
