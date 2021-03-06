/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package org.duracloud.account.util;

import org.duracloud.storage.domain.StorageProviderType;

import java.util.ArrayList;
import java.util.List;

public class StorageProviderTypeUtil {
    private static final List<StorageProviderType> SECONDARY_PROVIDER_TYPES;
    
    static {
        SECONDARY_PROVIDER_TYPES = new ArrayList<>();
        SECONDARY_PROVIDER_TYPES.add(StorageProviderType.AMAZON_GLACIER);
        SECONDARY_PROVIDER_TYPES.add(StorageProviderType.DPN);
        SECONDARY_PROVIDER_TYPES.add(StorageProviderType.CHRONOPOLIS);
        SECONDARY_PROVIDER_TYPES.add(StorageProviderType.SDSC);
        SECONDARY_PROVIDER_TYPES.add(StorageProviderType.RACKSPACE);
    }
    
    public static List<StorageProviderType> getAvailableSecondaryTypes() {
        return new ArrayList<>(SECONDARY_PROVIDER_TYPES);
    }

    public static List<StorageProviderType> getAvailableTypes() {
        List<StorageProviderType> types = new ArrayList<>(SECONDARY_PROVIDER_TYPES);
        types.add(0,StorageProviderType.AMAZON_S3);
        return types;
    }

}
