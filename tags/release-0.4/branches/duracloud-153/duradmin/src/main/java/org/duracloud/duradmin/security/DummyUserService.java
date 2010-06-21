package org.duracloud.duradmin.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * Spring security configuration seems to require a user detail service.  I played with it for a while
 * and couldn't see how to get around this requirement.  This class doesn't seem to be called 
 * by the security framework at all. 
 * @author Daniel Bernstein
 * @version $Id$
 */
public class DummyUserService implements UserDetailsService {

    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException, DataAccessException {
        
        throw new UsernameNotFoundException(username);
    }

}
