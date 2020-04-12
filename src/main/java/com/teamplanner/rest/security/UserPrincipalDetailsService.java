package com.teamplanner.rest.security;

import com.teamplanner.rest.model.entity.User;
import com.teamplanner.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public UserPrincipalDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param googlesub contained in JWT token, stored in client's HttpOnly cookie.
            *                  We use Google ID as username in this application.
     * @return UserDetails – a fully populated user record (never null)
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String googlesub) throws UsernameNotFoundException {
        User user = this.userService.findById(googlesub);
        MyUserDetails userDetails = null;
        if(user != null) {
            userDetails = new MyUserDetails(user);
        }

        if(userDetails == null){
            throw new UsernameNotFoundException("No such user");
        }else if(userDetails.getAuthorities().size()==0){
            throw new UsernameNotFoundException("User has no authorities");
        }

        return userDetails;
    }
}
