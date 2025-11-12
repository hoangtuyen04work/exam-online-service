//package edu.exam_online.exam_online_system.config;
//
//import edu.exam_online.exam_online_system.service.auth.impl.CustomUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//
//@RequiredArgsConstructor
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private final CustomUserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        CustomUsernamePasswordAuthenticationToken authenticationToken = (CustomUsernamePasswordAuthenticationToken) authentication;
//
//        String username = authenticationToken.getName();
//        Long roleId = authenticationToken.getRoleId();
//        String password = authenticationToken.getCredentials().toString();
//
//        UserDetails userDetails = userDetailsService.loadUserByEmailAndRoleId(username, roleId);
//
//        if(passwordEncoder.matches(password, userDetails.getPassword())){
//            throw new BadCredentialsException("Invalid username or password");
//        }
//
//        return new CustomUsernamePasswordAuthenticationToken(userDetails, password, roleId);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return CustomUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
