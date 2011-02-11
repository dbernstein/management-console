/*
 * Copyright (c) 2009-2010 DuraSpace. All rights reserved.
 */
package org.duracloud.account.security.domain;

import org.duracloud.common.model.Credential;

/**
 * @author Andrew Woods
 *         Date: Jan 31, 2011
 */
public class InitUserCredential extends Credential {

    private static final String defaultUsername = "init";
    private static final String defaultPassword = "ipw";

    public InitUserCredential() {
        super(getInitUsername(), getInitPassword());
    }

    private static String getInitUsername() {
        String username = System.getProperty("init.username");
        if (null == username) {
            username = defaultUsername;
        }
        return username;
    }

    private static String getInitPassword() {
        String password = System.getProperty("init.password");
        if (null == password) {
            password = defaultPassword;
        }
        return password;
    }

}