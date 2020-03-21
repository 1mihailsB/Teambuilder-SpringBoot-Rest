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
     * @param googlesub - contained in JWT token, stored in client's HttpOnly cookie
     *                  we use Google Id as username in our application.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String googlesub) throws UsernameNotFoundException {
        User user = this.userRepository.findByGooglesub(googlesub);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        return userPrincipal;
    }
}
