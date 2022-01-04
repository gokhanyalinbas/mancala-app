package com.bol.mancala.mancalagame.controller;

import com.bol.mancala.mancalagame.config.jwt.JwtTokenProvider;
import com.bol.mancala.mancalagame.config.jwt.JwtTokenResponse;
import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.bol.mancala.mancalagame.dto.WebUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

@RestController
@RequestMapping(MancalaConst.LOGIN)
@CrossOrigin
public class LoginController {
    @Autowired
    private AuthenticationManager authMngr;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody @Valid WebUserDto webUser) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    webUser.getUsername(), webUser.getPassword());
            authMngr.authenticate(authToken);
            return ResponseEntity.ok(new JwtTokenResponse(jwtTokenProvider.createToken(webUser.getUsername())));
        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED " + e.getMessage());
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("INVALID_CREDENTIALS " + e.getMessage());
        }
    }

}