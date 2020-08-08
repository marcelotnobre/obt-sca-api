package br.com.obt.sca.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.obt.sca.api.service.exception.ServiceException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class TokenService {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public final String SECRET_KEY = "outerboxtech";

    public String criarToken(Long idUsuario) {
        String token = null;
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.HOUR, 12);
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            token = JWT.create()
                    .withIssuer("" + idUsuario)
                    .withExpiresAt(expiresAt.getTime())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            logger.warn("-- Não foi possível criar o token!. --");
        }
        return token;
    }

    public DecodedJWT verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        } catch (JWTCreationException exception) {
            logger.warn("-- Não foi possível verificar a validade do token!. --");
        }
        return null;
    }

    public boolean isExpirou(Date dataExpiracao) {
        try {
            Date dataAtual = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataExpiracao);
            cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 30);
            dataExpiracao = cal.getTime();

            return dataAtual.after(dataExpiracao);
        } catch (Exception e) {
            logger.warn("-- Não foi possível verificar se o token está expirado!. --");
        }
        return true;
    }
}
