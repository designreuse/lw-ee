package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets.filters;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.AuthorizationUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Nikolay on 24.11.2015.
 */
public class LoginFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResp = (HttpServletResponse) servletResponse;

        boolean loggedIn = AuthorizationUtil.checkIsLoggedIn(httpServletReq);

        if (loggedIn) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            try {
                httpServletResp.sendRedirect(httpServletReq.getContextPath() + "/Login.jsp");
            } catch (Exception e) {
                LOGGER.warn("Exception in LoginFilter", e);
                httpServletResp.sendRedirect(httpServletReq.getContextPath() + "/Login.jsp");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
