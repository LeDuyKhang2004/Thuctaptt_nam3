package com.javaweb.security;

import com.javaweb.entity.UserEntity;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Enforce areas at servlet level, including paths that bypass MVC controller guards. */
public class AreaAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.equals("/admin/login") || path.equals("/user/login") || path.equals("/login") || path.equals("/register") || path.equals("/logout")) {
            chain.doFilter(req, res); return;
        }
        boolean adminOnly = path.startsWith("/admin/");
        boolean userOnly = path.startsWith("/user/") || path.startsWith("/leave/") || path.startsWith("/manager/");
        boolean loginRequired = adminOnly || userOnly || path.equals("/notifications") || path.startsWith("/notifications/") || path.equals("/calendar") || path.startsWith("/chatbot/");
        HttpSession session = request.getSession(false);
        UserEntity user = session == null ? null : (UserEntity) session.getAttribute("currentUser");
        if (loginRequired) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + (adminOnly ? "/admin/login" : "/user/login")); return;
            }
            boolean isAdmin = "ADMIN".equals(user.getRole());
            if (adminOnly && !isAdmin) { response.sendRedirect(request.getContextPath() + "/user/home"); return; }
            if (userOnly && isAdmin) { response.sendRedirect(request.getContextPath() + "/admin/dashboard"); return; }
            if (path.startsWith("/manager/") && !"MANAGER".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/user/home"); return;
            }
        }
        chain.doFilter(req, res);
    }
}
