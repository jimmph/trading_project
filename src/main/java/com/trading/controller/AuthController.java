package com.trading.controller;

import com.trading.config.JwtProvider;
import com.trading.model.TwoFactorOTP;
import com.trading.model.User;
import com.trading.repository.UserRepository;
import com.trading.response.AuthResponse;
import com.trading.service.CustomerUserDetailsService;
import com.trading.service.EmailService;
import com.trading.service.TwoFactorOtpService;
import com.trading.utils.OtpUtils;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/auth")
public class AuthController {

    
    private  final UserRepository userRepository;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final TwoFactorOtpService twoFactorOtpService;
    private final EmailService emailService;

    public AuthController(final UserRepository userRepository, final CustomerUserDetailsService customerUserDetailsService, final TwoFactorOtpService twoFactorOtpService, final EmailService emailService){
        this.userRepository= userRepository;
        this.customerUserDetailsService = customerUserDetailsService;
        this.twoFactorOtpService = twoFactorOtpService;
        this.emailService = emailService;
    }

    @PostMapping("/signup") 
    public ResponseEntity<AuthResponse> register(@RequestBody User user){ //

        User isEmailExist = userRepository.findByEmail(user.getEmail());

        if(isEmailExist!=null){
            throw new IllegalArgumentException("email is already used with another acccount");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());
        
        userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new  AuthResponse();
        this.response(res, jwt, true, "You have been registered!");
        
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    @PostMapping("/signin") 
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{
        String username = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(username);
        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            this.response(res, jwt, true, "Two factor authentication is enabled");
            String otp = OtpUtils.generateOtp();
            
            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(authUser.getId());
            if(oldTwoFactorOTP!=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }

            TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOTP(authUser, otp, jwt);

            emailService.sendVerificationOtpEmail(username, otp);

            res.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(res, HttpStatus
            .ACCEPTED);


        }
        AuthResponse res = new AuthResponse();

        this.response(res, jwt, true, "You are connected");
        

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }




    private Authentication authenticate(String username, String password){
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("invalid username");
        }
        if (!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp, @RequestParam String id) throws Exception{

        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);

        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, id)){
            AuthResponse res = new AuthResponse();
            this.response(res, twoFactorOTP.getJwt(), true, "Two factor authentication is verified");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("invalid otp");
    }

    private void response(final AuthResponse res, final String jwt, final boolean status, final String message){
        res.setJwt(jwt);
        res.setMessage(message);
        res.setStatus(status);

    }
}
