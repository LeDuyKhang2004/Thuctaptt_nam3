package com.javaweb.security;
import jakarta.servlet.*;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/** Session-bound synchronizer token for ALL state-changing POST requests. */
public class CsrfFilter implements Filter {
 private final SecureRandom random=new SecureRandom();
 public void doFilter(ServletRequest rq,ServletResponse rs,FilterChain chain)throws IOException,ServletException{
  HttpServletRequest request=(HttpServletRequest)rq;HttpServletResponse response=(HttpServletResponse)rs;
  HttpSession session=request.getSession(true);
  String expected=(String)session.getAttribute("csrfToken");
  if(expected==null){byte[] bytes=new byte[32];random.nextBytes(bytes);expected=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);session.setAttribute("csrfToken",expected);}
  if("POST".equalsIgnoreCase(request.getMethod())){
   String actual=request.getParameter("_csrf");if(actual==null)actual=request.getHeader("X-CSRF-Token");
   if(actual==null||!java.security.MessageDigest.isEqual(expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),actual.getBytes(java.nio.charset.StandardCharsets.UTF_8))){response.sendError(403,"CSRF token không hợp lệ. Hãy tải lại trang.");return;}
  }
  chain.doFilter(rq,rs);
 }
}
