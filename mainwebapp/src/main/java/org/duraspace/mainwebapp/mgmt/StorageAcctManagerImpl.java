
package org.duraspace.mainwebapp.mgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.duraspace.common.model.Credential;
import org.duraspace.mainwebapp.domain.model.StorageAcct;
import org.duraspace.mainwebapp.domain.model.StorageProvider;
import org.duraspace.mainwebapp.domain.repo.StorageAcctRepository;
import org.duraspace.mainwebapp.domain.repo.StorageProviderRepository;
import org.duraspace.storage.domain.StorageProviderType;

public class StorageAcctManagerImpl
        implements StorageAcctManager {

    protected final Logger log = Logger.getLogger(getClass());

    private StorageAcctRepository storageAcctRepository;

    private StorageProviderRepository storageProviderRepository;

    private CredentialManager credentialManager;

    /**
     * {@inheritDoc}
     */
    public List<StorageAcct> getStorageProviderAccountsByDuraAcctId(int duraAcctId) {
        List<StorageAcct> accts = new ArrayList<StorageAcct>();
        try {
            accts =
                    getStorageAcctRepository()
                            .findStorageAcctsByDuraAcctId(duraAcctId);
            for (StorageAcct acct : accts) {
                int storageCredId = acct.getStorageCredentialId();
                int storageProviderId = acct.getStorageProviderId();
                acct.setStorageProviderCredential(getCredentialManager()
                        .findCredentialById(storageCredId));
                acct.setStorageProvider(getStorageProviderRepository()
                        .findStorageProviderById(storageProviderId));
            }
        } catch (Exception e) {
            log.error(e);
        }
        return accts;
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    public StorageAcct getStorageProviderAccount(int storageAcctId)
            throws Exception {
        return getStorageAcctRepository().findStorageAcctById(storageAcctId);
    }

    public StorageAcct findStorageAccountAndLoadCredential(int storageAcctId)
            throws Exception {
        StorageAcct acct =
                getStorageAcctRepository().findStorageAcctById(storageAcctId);

        Credential storageCred =
                getCredentialManager().findCredentialById(acct
                        .getStorageCredentialId());

        acct.setStorageProviderCredential(storageCred);
        return acct;
    }

    public StorageProvider findStorageProviderForStorageAcct(int storageAcctId)
            throws Exception {
        StorageAcct acct =
                getStorageAcctRepository().findStorageAcctById(storageAcctId);

        return getStorageProviderRepository().findStorageProviderById(acct
                .getStorageProviderId());
    }

    public int findStorageProviderIdByProviderType(StorageProviderType providerType)
            throws Exception {
        return getStorageProviderRepository()
                .findStorageProviderIdByProviderType(providerType);
    }

    public int saveStorageAcct(StorageAcct storageAcct) throws Exception {
        return getStorageAcctRepository().saveStorageAcct(storageAcct);
    }

    public int saveCredentialForStorageAcct(Credential storageAcctCred,
                                            int storageAcctId) throws Exception {
        int credId = getCredentialManager().saveCredential(storageAcctCred);
        StorageAcct storageAcct =
                getStorageAcctRepository().findStorageAcctById(storageAcctId);
        storageAcct.setStorageCredentialId(credId);
        saveStorageAcct(storageAcct);
        return credId;
    }

    public boolean isStorageNamespaceTaken(String storageAcctNamespace) {
        return getStorageAcctRepository()
                .isStorageNamespaceTaken(storageAcctNamespace);
    }

    public StorageAcctRepository getStorageAcctRepository() {
        return storageAcctRepository;
    }

    public void setStorageAcctRepository(StorageAcctRepository storageAcctRepository) {
        this.storageAcctRepository = storageAcctRepository;
    }

    public StorageProviderRepository getStorageProviderRepository() {
        return storageProviderRepository;
    }

    public void setStorageProviderRepository(StorageProviderRepository storageProviderRepository) {
        this.storageProviderRepository = storageProviderRepository;
    }

    public CredentialManager getCredentialManager() {
        return credentialManager;
    }

    public void setCredentialManager(CredentialManager credentialManager) {
        this.credentialManager = credentialManager;
    }

}