package pl.pazurkiewicz.oldtimers_rally.login;

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
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;
import pl.pazurkiewicz.oldtimers_rally.user.User;
import pl.pazurkiewicz.oldtimers_rally.user.UserRepository;

import javax.validation.Valid;


@Controller
public class LoginController {
    private final UserRepository userRepo;

    public LoginController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserWriteModel());
        return "login/signup_form";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") @Valid UserWriteModel user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login/signup_form";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = userRepo.save(new User(user));
        UserDetails userDetails = new MyUserDetails(savedUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return "index";
    }
}
