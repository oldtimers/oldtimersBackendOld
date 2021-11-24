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
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.web.UserWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.UserRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.validation.Valid;


@Controller
@SessionAttributes("user")
public class LoginController {
    private final UserRepository userRepo;
    private final LanguageService languageService;

    public LoginController(UserRepository userRepo, LanguageService languageService) {
        this.userRepo = userRepo;
        this.languageService = languageService;
    }

    @GetMapping("/login")
    public String login() {
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
        UserDetails userDetails = new MyUserDetails(savedUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        model.addAttribute("user", null);
        return "index";
    }
}
