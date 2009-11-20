package org.duracloud.duraservice.error;

import org.duracloud.common.util.error.DuraCloudException;

/**
 * @author: Bill Branan
 * Date: Nov 13, 2009
 */
public class NoSuchServiceException extends DuraCloudException {

    private static final String messageKey =
        "duracloud.error.duraservice.nosuchservice";

    public NoSuchServiceException(int serviceId) {
        super("There is no service with service ID  " +  serviceId, messageKey);
        setArgs(new Integer(serviceId).toString());
    }
}