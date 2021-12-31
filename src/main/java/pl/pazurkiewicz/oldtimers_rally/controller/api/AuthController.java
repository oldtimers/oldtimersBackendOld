package pl.pazurkiewicz.oldtimers_rally.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.exception.TokenRefreshException;
import pl.pazurkiewicz.oldtimers_rally.model.RefreshToken;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.LoginRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.TokenRefreshRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.JwtResponse;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.MessageResponse;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.UserName;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserRepository;
import pl.pazurkiewicz.oldtimers_rally.security.jwt.JwtUtils;
import pl.pazurkiewicz.oldtimers_rally.security.service.RefreshTokenService;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());
        return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUsername());
    }

    @PostMapping("/refresh")
    @Transactional
    JwtResponse refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshTokenService::increaseExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromLogin(user.getLogin());
                    return new JwtResponse(token, requestRefreshToken, user.getLogin());
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @GetMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    UserName getUsername(@AuthenticationPrincipal UserDetailsImpl principal) {
        return new UserName(principal.getUsername());
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    MessageResponse logoutUser(@AuthenticationPrincipal UserDetailsImpl principal) {
        refreshTokenService.deleteByUserId(principal.getUser().getId());
        return new MessageResponse("Log out successful!");
    }

}
