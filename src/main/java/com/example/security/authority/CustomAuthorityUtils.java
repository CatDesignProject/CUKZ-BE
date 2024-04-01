package com.example.security.authority;

import com.example.member.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomAuthorityUtils {

    private static List<String> roles = List.of("ROLE_MANAGER", "ROLE_USER");

    public static List<GrantedAuthority> getAuthorities(MemberRole memberRole) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String roleName = "ROLE_" + memberRole.name().toUpperCase();

        int startIndex = IntStream.range(0, roles.size())
                .filter(i -> roles.get(i).equals(roleName))
                .findFirst()
                .orElse(-1);

        if(startIndex == -1) {
            return authorities;
        }

        return roles.subList(startIndex, roles.size()).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
