package pl.pazurkiewicz.oldtimers_rally.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import pl.pazurkiewicz.oldtimers_rally.repository.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.UserGroupRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final EventRepository eventRepository;
    private final UserGroupRepository userGroupRepository;

    public MethodSecurityConfig(EventRepository eventRepository, UserGroupRepository userGroupRepository) {
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(eventRepository, userGroupRepository));
        return expressionHandler;
    }
}
