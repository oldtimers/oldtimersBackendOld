package pl.pazurkiewicz.oldtimers_rally.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Locale;

@Component
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        localeResolver.setLocale(request, response, new Locale(((UserDetailsImpl) authentication.getPrincipal()).getUser().getDefaultLanguage().getCode()));
        userRepository.updateLastLogin(Instant.now(), ((UserDetailsImpl) authentication.getPrincipal()).getUsername());
    }
}
