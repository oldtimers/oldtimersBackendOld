package pl.pazurkiewicz.oldtimers_rally.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.exception.TokenRefreshException;
import pl.pazurkiewicz.oldtimers_rally.model.RefreshToken;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.LogOutRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.LoginRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.TokenRefreshRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.JwtResponse;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.MessageResponse;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.TokenRefreshResponse;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserRepository;
import pl.pazurkiewicz.oldtimers_rally.security.jwt.JwtUtils;
import pl.pazurkiewicz.oldtimers_rally.security.service.RefreshTokenService;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUser().getId(),
                userDetails.getUsername(), userDetails.getUser().getEmail(), roles));
    }

    @PostMapping("/refresh")
    @Transactional
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshTokenService::increaseExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromLogin(user.getLogin());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser(@RequestBody @Valid LogOutRequest logOutRequest, @AuthenticationPrincipal UserDetailsImpl principal) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

}
