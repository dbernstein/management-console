package org.duracloud.account.common.domain;

import java.util.Set;

public class DuracloudAccount implements Comparable<DuracloudAccount> {
    private AccountInfo accountInfo;

    private DuracloudInstance instance;

    private String instanceStatus;

    private String instanceVersion;

    private InstanceType instanceType;
    
    private Set<String> versions;

    private Role userRole;

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public DuracloudInstance getInstance() {
        return instance;
    }

    public void setInstance(DuracloudInstance instance) {
        this.instance = instance;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public String getInstanceVersion() {
        return instanceVersion;
    }

    public void setInstanceVersion(String instanceVersion) {
        this.instanceVersion = instanceVersion;
    }

    public Set<String> getVersions() {
        return versions;
    }

    public void setVersions(Set<String> versions) {
        this.versions = versions;
    }

    @Override
    public int compareTo(DuracloudAccount o) {
        return this.accountInfo.compareTo(o.accountInfo);
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
    }
}
