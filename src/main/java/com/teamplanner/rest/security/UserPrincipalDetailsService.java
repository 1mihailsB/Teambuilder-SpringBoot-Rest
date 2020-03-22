package com.teamplanner.rest.security;

import com.teamplanner.rest.dao.UserRepository;
import com.teamplanner.rest.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param googlesub  contained in JWT token, stored in client's HttpOnly cookie.
     *                  We use Google ID as username in this application.
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String googlesub) throws UsernameNotFoundException {
        User user = this.userRepository.findByGooglesub(googlesub);
        UserPrincipal userPrincipal = null;
        if(user != null) {
            userPrincipal = new UserPrincipal(user);
        }

        if(userPrincipal == null){
            throw new UsernameNotFoundException("No such user");
        }else if(userPrincipal.getAuthorities().size()==0){
            throw new UsernameNotFoundException("User has no authorities");
        }

        return userPrincipal;
    }
}
