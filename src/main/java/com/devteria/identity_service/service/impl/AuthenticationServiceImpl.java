package com.devteria.identity_service.service.impl;

import com.devteria.identity_service.dto.request.*;
import com.devteria.identity_service.dto.reponse.AuthenticationResponse;
import com.devteria.identity_service.dto.reponse.IntrospectResponse;
import com.devteria.identity_service.entity.InvalidatedToken;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.TokenValidationException;
import com.devteria.identity_service.exceptions.UnAuthenticationException;
import com.devteria.identity_service.exceptions.UserNotExistedException;
import com.devteria.identity_service.repository.InvalidatedRepository;
import com.devteria.identity_service.repository.UserRepository;
import com.devteria.identity_service.service.AuthenticateService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticateService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvalidatedRepository invalidatedRepository;

    @NonFinal
    @Value("${SIGNER_KEY}")
    protected String signerKey;

    @NonFinal
    @Value("${VALID_DURATION}")
    protected Long validDuration;

    @NonFinal
    @Value("${REFRESHABLE_DURATION}")
    protected Long refreshableDuration;

    @Override
    public AuthenticationResponse authenticated(AuthenticationRequest authenticationRequest) {
        User user = getUser(authenticationRequest.getUsername());
        validatePassword(authenticationRequest.getPassword(), user.getPassword());
        return AuthenticationResponse.builder()
                .logIN("Log in success")
                .token(generateToken(user))
                .refreshToken(generateRefreshToken(user))
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        validateToken(request.getToken());
        return IntrospectResponse.builder().valid(true).build();
    }

    @Override
    public void logout(LogoutRequest request) {
        String tokenId = validateToken(request.getToken());
        Date expiryTime = getTokenExpiryTime(request.getToken());
        saveToken(tokenId,expiryTime);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) {
        String tokenId = validateToken(request.getRefreshToken());
        Date expiryTime = getTokenExpiryTime(request.getRefreshToken());
        saveToken(tokenId,expiryTime);
        User user = getUser(getUsernameFromToken(request.getRefreshToken()));
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .refreshToken(generateRefreshToken(user))
                .authenticated(true)
                .build();
    }



    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistedException("User not existed"));
    }


    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new UnAuthenticationException("Authenticated not success");
        }
    }



    private Date getTokenExpiryTime(String token){
        try{
            return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
        }catch(ParseException ex){
            throw new TokenValidationException(ErrorCode.TOKEN_INVALID.getMessage());
        }
    }

    private String validateToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)) {
                throw new AuthenticationException("",new UnAuthenticationException(ErrorCode.UNAUTHENTICATED.getMessage())) {};
            }
            if (invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new AuthenticationException("",new UnAuthenticationException(ErrorCode.UNAUTHENTICATED.getMessage())) {};
            }
            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new AuthenticationException("",new UnAuthenticationException(ErrorCode.UNAUTHENTICATED.getMessage())) {};
            }
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (JOSEException | ParseException ex) {
            throw new TokenValidationException("Invalid Token");
        }
    }

    private void saveToken(String tokenId,Date expiryTime) {
        invalidatedRepository.save(InvalidatedToken.builder()
                .id(tokenId)
                .expiryTime(expiryTime)
                .build());
    }

    private String getUsernameFromToken(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        } catch (ParseException ex) {
            throw new TokenValidationException(ErrorCode.TOKEN_INVALID.getMessage());
        }
    }

    private String generateToken(User user) {
        return generateJWT(user, validDuration);
    }

    private String generateRefreshToken(User user) {
        return generateJWT(user, refreshableDuration);
    }

    private String generateJWT(User user, Long duration) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("Thanh")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()))
                    .claim("scope", buildScope(user))
                    .jwtID(UUID.randomUUID().toString())
                    .build();
            JWSObject jwsObject = new JWSObject(header, new Payload(claims.toJSONObject()));
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException ex) {
            throw new TokenValidationException("Invalid Token");
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getName()));
        }
        return stringJoiner.toString();
    }
}
