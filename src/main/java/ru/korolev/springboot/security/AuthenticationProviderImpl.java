package ru.korolev.springboot.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.korolev.springboot.model.dto.RoleDTO;
import ru.korolev.springboot.model.dto.UserDTO;
import ru.korolev.springboot.service.RestService;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final RestService restService;

    public AuthenticationProviderImpl(RestService restService) {
        this.restService = restService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String login = authentication.getName();
        final String password = authentication.getCredentials().toString();

        UserDTO userDTO = restService.authorization(login, password)
                .orElseThrow(() -> new BadCredentialsException("Authentication failed for " + login));

        return new UsernamePasswordAuthenticationToken(
                userDTO.getLogin(),
                userDTO.getPassword(),
                grantedAuthorities(userDTO.getRoles())
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Collection<GrantedAuthority> grantedAuthorities(Collection<RoleDTO> roles) {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_".concat(role.getName()))
                .collect(Collectors.toList());
    }
}