package com.muggle.poseidon.filter;

import com.muggle.poseidon.base.exception.SimplePoseidonCheckException;
import com.muggle.poseidon.service.TokenService;
import com.muggle.poseidon.store.SecurityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: poseidon-cloud-starter
 * @description: 登陆过滤器
 * @author: muggle
 * @create: 2019-11-07
 **/

public class SecurityLoginFilter extends UsernamePasswordAuthenticationFilter {
    private TokenService tokenService;

    private SecurityStore securityStore;


    static Logger logger= LoggerFactory.getLogger(SecurityLoginFilter.class);
    public SecurityLoginFilter(TokenService tokenService, SecurityStore securityStore) {
        this.tokenService = tokenService;
        this.securityStore = securityStore;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            UserDetails login = tokenService.login(request, response);
            String token = securityStore.signUserMessage(login);
            return  new UsernamePasswordAuthenticationToken(token, "");
        }catch (SimplePoseidonCheckException e){
            logger.error("登录校验异常：",e);
            throw new AuthenticationServiceException(e.getMessage());
        }catch (Exception e){
            logger.error("系统异常：",e);
            throw new AuthenticationServiceException(e.getMessage());
        }

    }
}
