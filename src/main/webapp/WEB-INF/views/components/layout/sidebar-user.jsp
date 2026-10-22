<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- Sidebar dành riêng cho nhân viên; các link trỏ tới UserHomeController và LeaveRequestController. --%>
<div class="sidebar">
    <div class="sidebar-heading">
        <span>Khu vực làm việc</span>
        <strong>Nhân viên</strong>
    </div>

    <c:set var="currentPath" value="${pageContext.request.requestURI}" />

    <nav class="sidebar-nav" aria-label="Menu nhân viên">
        <a class="${fn:contains(currentPath, '/user/home') ? 'active' : ''}"
           href="${pageContext.request.contextPath}/user/home">Tổng quan</a>
        <a class="${fn:contains(currentPath, '/leave/create') ? 'active' : ''}"
           href="${pageContext.request.contextPath}/leave/create">Tạo đơn nghỉ phép</a>
        <a class="${fn:contains(currentPath, '/leave/my') || fn:contains(currentPath, '/leave/detail') ? 'active' : ''}"
           href="${pageContext.request.contextPath}/leave/my">Đơn của tôi</a>
    </nav>

    <div class="sidebar-footer">
        <a class="logout-link" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </div>
</div>
