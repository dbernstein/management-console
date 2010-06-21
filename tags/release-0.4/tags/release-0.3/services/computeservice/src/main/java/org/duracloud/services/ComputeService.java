
package org.duracloud.services;

import java.util.Map;

public interface ComputeService {

    public enum ServiceStatus {INSTALLED, STARTING, STARTED, STOPPING, STOPPED};

    public void start() throws Exception;

    public void stop() throws Exception;

    public Map<String, String> getServiceProps();

    public String describe() throws Exception;

    public String getServiceId();

    public ServiceStatus getServiceStatus() throws Exception;

    public String getServiceWorkDir();

    public void setServiceWorkDir(String serviceWorkDir);
}
