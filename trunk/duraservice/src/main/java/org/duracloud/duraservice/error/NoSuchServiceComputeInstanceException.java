package org.duracloud.duraservice.error;

import org.duracloud.common.error.DuraCloudCheckedException;

/**
 * @author: Bill Branan
 * Date: Nov 11, 2009
 */
public class NoSuchServiceComputeInstanceException extends DuraCloudCheckedException {

    private static final String messageKey =
        "duracloud.error.duraservice.nosuchservicecomputeinstance";

    public NoSuchServiceComputeInstanceException(String instanceHost) {
        super("No service compute instance is available on host " +  instanceHost,
              messageKey);
        setArgs(instanceHost);
    }
}