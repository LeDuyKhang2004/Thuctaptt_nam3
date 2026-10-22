<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- Sidebar dành riêng cho ADMIN; các link trỏ tới AdminController. --%>
<div class="sidebar">
    <div class="sidebar-heading">
        <span>Khu vực làm việc</span>
        <strong>Quản trị viên</strong>
    </div>

    <c:set var="currentPath" value="${pageContext.request.requestURI}" />

    <nav class="sidebar-nav" aria-label="Menu quản trị">
        <a class="${fn:contains(currentPath, '/admin/dashboard') ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a class="${fn:contains(currentPath, '/admin/leaves') ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/leaves">Quản lý đơn nghỉ phép</a>
        <a class="${fn:contains(currentPath, '/admin/employees') ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/employees">Danh sách nhân viên</a>
    </nav>

    <div class="sidebar-footer">
        <a class="logout-link" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </div>
</div>
