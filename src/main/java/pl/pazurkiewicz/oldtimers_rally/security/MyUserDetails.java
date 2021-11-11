package pl.pazurkiewicz.oldtimers_rally.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = user.getUserGroups().stream().filter(userGroup -> userGroup.getEvent() == null)
                .map(group -> new SimpleGrantedAuthority(group.getSelectedGroup().toString())).collect(Collectors.toCollection(ArrayList::new));
        for (UserGroup userGroup : user.getUserGroups()) {
            if (userGroup.getEvent() == null) {
                authorities.add(new SimpleGrantedAuthority(userGroup.getSelectedGroup().toString()));
            } else {
                authorities.add(new SimpleGrantedAuthority(userGroup.getSelectedGroup().toString() + "__" + userGroup.getEvent().getId()));
            }
        }
        return authorities;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
