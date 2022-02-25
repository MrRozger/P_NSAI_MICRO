package com.politechnika.projekt.configuration;

import com.politechnika.projekt.model.Client;
import com.politechnika.projekt.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("ClientDetailsService")
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        Client client = clientRepository.findByUsername(username);
        List<GrantedAuthority> authorities = buildUserAuthority(client.getRole().name());
        return buildUserForAuthentication(client, authorities);
    }

    private User buildUserForAuthentication(Client client, List<GrantedAuthority> authorities) {

        return new User(client.getUsername(), client.getPassword(), true, true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(String clientRole) {

        Set<GrantedAuthority> setAuths = new HashSet<>();
        setAuths.add(new SimpleGrantedAuthority(clientRole));
        List<GrantedAuthority> result = new ArrayList<>(setAuths);

        return result;
    }


}
