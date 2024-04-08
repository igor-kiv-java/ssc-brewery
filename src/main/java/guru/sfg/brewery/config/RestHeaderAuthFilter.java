package guru.sfg.brewery.config;

import org.springframework.security.web.util.matcher.RequestMatcher;


import javax.servlet.http.HttpServletRequest;


public class RestHeaderAuthFilter extends AbstractRestHeaderAuthFilter {


    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getUserPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Api-Secret");
    }

    protected String getUserName(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Api-Key");
    }
}
