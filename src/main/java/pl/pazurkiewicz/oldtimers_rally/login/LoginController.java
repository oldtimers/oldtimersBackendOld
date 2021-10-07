package pl.pazurkiewicz.oldtimers_rally.login;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.pazurkiewicz.oldtimers_rally.user.User;
import pl.pazurkiewicz.oldtimers_rally.user.UserRepository;
import pl.pazurkiewicz.oldtimers_rally.user.UserWriteModel;

import javax.validation.Valid;


@Controller
public class LoginController {
    private final UserRepository userRepo;

    public LoginController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserWriteModel());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(@Valid UserWriteModel user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(new User(user));
        return "index";
    }
}
