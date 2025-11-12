//package edu.exam_online.exam_online_system.service.auth.impl;
//
//import edu.exam_online.exam_online_system.entity.auth.User;
//import edu.exam_online.exam_online_system.repository.auth.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    UserRepository userRepository;
//
//    // load theo email và role
//    public UserDetails loadUserByEmailAndRoleId(String email, Long roleId) {
//        User user = userRepository.findByEmailAndRoleId(email, roleId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))
//        );
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // không dùng ở đây (Spring vẫn yêu cầu implement)
//        throw new UnsupportedOperationException("Use loadUserByUsernameAndRole instead");
//    }
//}
