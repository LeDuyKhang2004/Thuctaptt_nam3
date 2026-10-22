<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- File index.jsp ở root chỉ chuyển hướng về /home khi user mở context path. --%>
<%
    response.sendRedirect(request.getContextPath() + "/home");
%>
