package ru.codepinkglitch.jwt_auth_demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.codepinkglitch.jwt_auth_demo.dtos.in.UserDetailsIn;
import ru.codepinkglitch.jwt_auth_demo.dtos.out.UserDetailsOut;
import ru.codepinkglitch.jwt_auth_demo.entities.MyAuthority;
import ru.codepinkglitch.jwt_auth_demo.entities.MyUserDetails;
import ru.codepinkglitch.jwt_auth_demo.enums.Role;
import ru.codepinkglitch.jwt_auth_demo.repositories.UserDetailsRepository;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userDetailsRepository.existsMyUserDetailsByUsername(username).equals(false)) {
            throw new UsernameNotFoundException("No such user.");
        } else {
            return userDetailsRepository.findMyUserDetailsByUsername(username);
        }
    }


    public UserDetailsOut createNew(UserDetailsIn userDetailsIn) {
        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setUsername(userDetailsIn.getUsername());
        myUserDetails.setPassword(bCryptPasswordEncoder.encode(userDetailsIn.getPassword()));
        MyAuthority myAuthority = new MyAuthority();
        myAuthority.setAuthority(Role.USER);
        myAuthority.setUserDetails(myUserDetails);
        myUserDetails.setAuthorities(Collections.singletonList(myAuthority));
        UserDetailsOut userDetailsOut = new UserDetailsOut();
        MyUserDetails saved = userDetailsRepository.save(myUserDetails);
        userDetailsOut.setUsername(saved.getUsername());
        userDetailsOut.setId(saved.getId());
        return userDetailsOut;
    }
}
