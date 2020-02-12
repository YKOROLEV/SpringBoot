package ru.korolev.springboot.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import ru.korolev.springboot.model.dto.UserDTO;
import ru.korolev.springboot.service.RestService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final RestService restService;

    public AuthenticationSuccessHandlerImpl(RestService restService) {
        this.restService = restService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
            Optional<UserDTO> userDTO = restService.authorization(true, user.getEmail(), user.getFullName());

            if (userDTO.isPresent()) {
                restService.setBasicAuthentication(userDTO.get().getLogin(), userDTO.get().getPassword());

                Collection<GrantedAuthority> roles = userDTO.get()
                        .getRoles()
                        .stream()
                        .map(role -> (GrantedAuthority) () -> "ROLE_".concat(role.getName()))
                        .collect(Collectors.toList());

                authentication = new UsernamePasswordAuthenticationToken(
                        userDTO.get().getLogin(),
                        userDTO.get().getPassword(),
                        roles
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/user");
        }
    }
}