package org.duracloud.security.xml;

import org.duracloud.SecurityUserType;
import org.duracloud.SecurityUsersDocument;
import org.duracloud.SecurityUsersType;
import org.duracloud.common.error.DuraCloudRuntimeException;
import org.duracloud.security.SecurityUserBean;
import org.duracloud.security.error.EmptyUserListException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for binding SecurityUsers xml documents to
 * SecurityUserBean lists.
 *
 * @author Andrew Woods
 *         Date: Apr 15, 2010
 */
public class SecurityUserElementReader {

    /**
     * This method binds a SecurityUsers xml document to a SecurityUsers list.
     *
     * @param doc SecurityUsers xml document
     * @return SecurityUserBean list
     */
    public static List<SecurityUserBean> createSecurityUsersFrom(
        SecurityUsersDocument doc) {
        SecurityUsersType usersType = doc.getSecurityUsers();
        if (usersType.sizeOfSecurityUserArray() == 0) {
            throw new EmptyUserListException();
        }

        checkSchemaVersion(usersType.getSchemaVersion());

        List<SecurityUserBean> beans = new ArrayList<SecurityUserBean>();
        for (SecurityUserType userType : usersType.getSecurityUserArray()) {
            beans.add(createSecurityUser(userType));
        }

        return beans;
    }

    private static SecurityUserBean createSecurityUser(SecurityUserType userType) {
        String username = userType.getUsername();
        String password = userType.getPassword();
        boolean enabled = userType.getEnabled();
        boolean credentialsNonExpired = userType.getCredentialsNonExpired();
        boolean accountNonExpired = userType.getAccountNonExpired();
        boolean accountNonLocked = userType.getAccountNonLocked();
        List<String> authorities = userType.getGrantedAuthorities();

        return new SecurityUserBean(username,
                                    password,
                                    enabled,
                                    credentialsNonExpired,
                                    accountNonExpired,
                                    accountNonLocked,
                                    authorities);
    }

    private static void checkSchemaVersion(String schemaVersion) {
        if (!schemaVersion.equals(SecurityUserBean.SCHEMA_VERSION)) {
            throw new DuraCloudRuntimeException(
                "Unsupported schema version: " + schemaVersion);
        }
    }

}