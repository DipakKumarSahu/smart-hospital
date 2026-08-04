package com.hospital.smart_hospital.security.service.impl;



import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.security.principal.CustomUserPrincipal;
import com.hospital.smart_hospital.security.service.CustomUserDetailsService;
import com.hospital.smart_hospital.user.User;
import com.hospital.smart_hospital.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsServiceImpl
        implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        ));

        return new CustomUserPrincipal(user);
    }
}