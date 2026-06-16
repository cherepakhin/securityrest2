package ru.perm.v.securityrest2.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.perm.v.securityrest2.model.ERole;
import ru.perm.v.securityrest2.model.Role;
import ru.perm.v.securityrest2.model.User;
import ru.perm.v.securityrest2.payload.request.LoginRequest;
import ru.perm.v.securityrest2.payload.request.SignupRequest;
import ru.perm.v.securityrest2.payload.responce.JwtResponse;
import ru.perm.v.securityrest2.payload.responce.MessageResponse;
import ru.perm.v.securityrest2.repository.RoleRepository;
import ru.perm.v.securityrest2.repository.UserRepository;
import ru.perm.v.securityrest2.security.jwt.JwtUtils;
import ru.perm.v.securityrest2.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final static String ERROR_ROLE_NOT_FOUND = "Error: Role is not "
			+ "found.";
	private static final Logger logger = LoggerFactory
			.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity authenticateUser(
			@Valid @RequestBody LoginRequest loginRequest) {

		logger.warn(loginRequest.toString());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword()));

		logger.warn(authentication.toString());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication
				.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
	}

	@PostMapping("/signup")
	public ResponseEntity registerUser(
			@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(
							"Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(
							"Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		logger.warn(String.format("ROLES: %s", strRoles));
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException(
							ERROR_ROLE_NOT_FOUND));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				logger.warn(String.format("ROLE: %s", role));
				switch (role) {
					case "admin":
						Role adminRole = roleRepository
								.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException(
										ERROR_ROLE_NOT_FOUND));
						roles.add(adminRole);

						break;
					case "mod":
						Role modRole = roleRepository
								.findByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException(
										ERROR_ROLE_NOT_FOUND));
						roles.add(modRole);

						break;
					default:
						Role userRole = roleRepository
								.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException(
										ERROR_ROLE_NOT_FOUND));
						roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity
				.ok(new MessageResponse("User registered successfully!"));
	}
}
