package pl.pazurkiewicz.oldtimers_rally.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (UserGroup userGroup : user.getUserGroups()) {
            if (userGroup.getEvent() == null) {
                authorities.add(new SimpleGrantedAuthority(userGroup.getSelectedGroup().toString()));
            }
//            Else it will be taken from user object
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
