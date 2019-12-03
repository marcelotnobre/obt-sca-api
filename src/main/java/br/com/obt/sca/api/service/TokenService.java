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

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class TokenService {

    public final String SECRET_KEY = "outerboxtech";

    public String criarToken(String login) {
        String token = null;
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.HOUR, 12);
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            token = JWT.create()
                    .withIssuer(login)
                    .withExpiresAt(expiresAt.getTime())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return token;
    }

    public DecodedJWT verify(String token, String login) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(login)
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        } catch (JWTCreationException exception) {
        }
        return null;
    }

    public boolean isExpirou(String token, String login) {
        try {
            Date dataExpiracao = verify(token, login).getExpiresAt();
            Date dataAtual = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataExpiracao);
            cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 30);
            dataExpiracao = cal.getTime();

            return dataAtual.after(dataExpiracao);
        } catch (Exception e) {
        }
        return true;
    }
}
