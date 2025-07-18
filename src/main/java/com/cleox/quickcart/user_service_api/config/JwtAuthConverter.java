package com.cleox.quickcart.user_service_api.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthConverter implements Converter<Jwt,AbstractAuthenticationToken> {


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> roles = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, roles);

    }
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        if(jwt.getClaim("realm_access")!=null){
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            ObjectMapper mapper = new ObjectMapper();

            //er
            List<String> keycloakRoles=mapper.convertValue(realmAccess.get("roles"), new TypeReference<List<String>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
            List<GrantedAuthority> roles=new ArrayList<>();

            for(String keycloakRole:keycloakRoles){
                roles.add(new SimpleGrantedAuthority(keycloakRole));
            }

            return roles;
        }
        return new ArrayList<>();
    }
}
