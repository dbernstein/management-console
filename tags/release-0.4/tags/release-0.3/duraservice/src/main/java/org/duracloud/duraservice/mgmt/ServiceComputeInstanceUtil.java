package org.duracloud.duraservice.mgmt;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.duracloud.common.web.RestHttpHelper;
import org.duracloud.duraservice.domain.ServiceComputeInstance;
import org.duracloud.duraservice.error.ServiceException;
import org.duracloud.servicesadminclient.ServicesAdminClient;

/**
 * This class encapsulates the logic for a ServiceComputeInstance object.
 *
 * @author Andrew Woods
 *         Date: Apr 1, 2010
 */
public class ServiceComputeInstanceUtil {

    private final Logger log = Logger.getLogger(ServiceComputeInstanceUtil.class);

    /**
     * @param hostName    of instance
     * @param port        of instance
     * @param context     of instance
     * @param displayName of instance
     * @return ServiceComputeInstance
     */
    public ServiceComputeInstance createComputeInstance(String hostName,
                                                        String port,
                                                        String context,
                                                        String displayName) {
        checkArgs(hostName, port, context, displayName);

        ServicesAdminClient servicesAdmin = new ServicesAdminClient();
        String baseUrl = "http://" + hostName + ":" + port + "/" + context;
        servicesAdmin.setBaseURL(baseUrl);
        servicesAdmin.setRester(new RestHttpHelper());

        StringBuilder msg = new StringBuilder("creating instance: ");
        msg.append(displayName);
        msg.append(" [" + hostName + ":" + port + "/" + context + "]");
        log.debug(msg);

        return new ServiceComputeInstance(hostName, displayName, servicesAdmin);
    }

    private void checkArgs(String... args) {
        for (String arg : args) {
            if (StringUtils.isBlank(arg)) {
                String error = "Could not create compute instance, " +
                    "values for serviceInstance must be not be empty.";
                throw new ServiceException(error);
            }
        }
    }
}
