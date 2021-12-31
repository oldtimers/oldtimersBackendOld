package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.web.UserWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Controller
@SessionAttributes("user")
public class LoginController {
    private final UserRepository userRepo;
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;

    public LoginController(UserRepository userRepo, LanguageService languageService, LanguageRepository languageRepository) {
        this.userRepo = userRepo;
        this.languageService = languageService;
        this.languageRepository = languageRepository;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserWriteModel(languageService.generateDefaultLanguageSelector()));
        return "signup_form";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") @Valid UserWriteModel user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getNewPassword());
        user.setNewPassword(encodedPassword);
        User savedUser = userRepo.save(new User(user));
        UserDetails userDetails = new UserDetailsImpl(savedUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        model.addAttribute("user", null);
        return "index";
    }

    @ModelAttribute("languages")
    List<Language> languages() {
        return languageRepository.findAll();
    }
}
