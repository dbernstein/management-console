/*
 * Copyright (c) 2009-2010 DuraSpace. All rights reserved.
 */
package org.duracloud.account.common.domain;

/**
 * @author Bill Branan
 *         Date: Dec 17, 2010
 */
public class DuracloudInstance extends BaseDomainData {
    public static String PLACEHOLDER_PROVIDER_ID = "TBD";

    /**
     * The ID of the Image on which this instance is based.
     */
    private int imageId;

    /**
     * The ID of the Account for which this instance was created
     */
    private int accountId;

    /**
     * The host name at which this instance is available.
     */
    private String hostName;

    /**
     * The identifier value assigned to this machine instance by the compute
     * provider. This ID is used when starting, stopping, or restarting the
     * server on which the DuraCloud software is running.
     */
    private String providerInstanceId;

    /**
     * Indicates if the instance is available.
     */
    private boolean initialized;

    public DuracloudInstance(int id,
                             int imageId,
                             int accountId,
                             String hostName,
                             String providerInstanceId,
                             boolean initialized) {
        this(id,
             imageId,
             accountId,
             hostName,
             providerInstanceId,
             initialized,
             0);
    }

    public DuracloudInstance(int id,
                             int imageId,
                             int accountId,
                             String hostName,
                             String providerInstanceId,
                             boolean initialized,
                             int counter) {
        this.id = id;
        this.imageId = imageId;
        this.accountId = accountId;
        this.hostName = hostName;
        this.providerInstanceId = providerInstanceId;
        this.initialized = initialized;
        this.counter = counter;
    }

    public int getImageId() {
        return imageId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getHostName() {
        return hostName;
    }

    public String getProviderInstanceId() {
        return providerInstanceId;
    }

    public void setProviderInstanceId(String providerInstanceId) {
        this.providerInstanceId = providerInstanceId;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DuracloudInstance)) {
            return false;
        }

        DuracloudInstance that = (DuracloudInstance) o;

        if (accountId != that.accountId) {
            return false;
        }
        if (imageId != that.imageId) {
            return false;
        }
        if (initialized != that.initialized) {
            return false;
        }
        if (hostName != null ? !hostName.equals(that.hostName) :
            that.hostName != null) {
            return false;
        }
        if (providerInstanceId != null ? !providerInstanceId
            .equals(that.providerInstanceId) :
            that.providerInstanceId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageId;
        result = 31 * result + accountId;
        result = 31 * result + (hostName != null ? hostName.hashCode() : 0);
        result = 31 * result +
            (providerInstanceId != null ? providerInstanceId.hashCode() : 0);
        result = 31 * result + (initialized ? 1 : 0);
        return result;
    }

}
