package ru.codepinkglitch.test.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.codepinkglitch.test.dtos.in.UserDetailsIn;
import ru.codepinkglitch.test.dtos.out.UserDetailsOut;
import ru.codepinkglitch.test.entities.MyAuthority;
import ru.codepinkglitch.test.entities.MyUserDetails;
import ru.codepinkglitch.test.enums.Role;
import ru.codepinkglitch.test.repositories.UserDetailsRepository;

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
