package com.example.myBackend.Backend.service;

import com.example.myBackend.Backend.dao.UserRepository;
import com.example.myBackend.Backend.model.CustomUserBean;
import com.example.myBackend.Backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with "
                        + "user name " + username + " not found"));
        return CustomUserBean.createInstance(user);
    }
}