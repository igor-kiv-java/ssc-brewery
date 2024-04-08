package guru.sfg.brewery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;


@Slf4j
public class RestUrlParameterAuthFilter extends AbstractRestHeaderAuthFilter {


    protected RestUrlParameterAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getUserName(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("login");
    }

    protected String getUserPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("password");
    }
}
