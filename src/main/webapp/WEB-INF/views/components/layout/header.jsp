<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%-- Header dùng chung cho các trang sau đăng nhập. --%>
<div class="header">
    <a class="header-brand"
       href="${pageContext.request.contextPath}${sessionScope.currentUser.role == 'ADMIN' ? '/admin/dashboard' : '/user/home'}"
       aria-label="LeaveFlow - Trang chủ">
        <span class="brand-mark" aria-hidden="true">LF</span>
        <span class="brand-copy">
            <strong>LeaveFlow</strong>
            <small>Quản lý nghỉ phép</small>
        </span>
    </a>

    <%-- currentUser được lưu trong session khi đăng nhập thành công. --%>
    <div class="header-user">
        <span class="user-avatar" aria-hidden="true">${sessionScope.currentUser.role == 'ADMIN' ? 'AD' : 'NV'}</span>
        <span class="user-copy">
            <small>Xin chào • <a href="${pageContext.request.contextPath}${sessionScope.currentUser.role == 'ADMIN' ? '/admin/notifications' : '/user/notifications'}" style="color:inherit">Thông báo</a></small>
            <strong><c:out value="${sessionScope.currentUser.fullName}"/></strong>
        </span>
    </div>
</div>

<input type="hidden" id="csrfValue" value="${sessionScope.csrfToken}">
<%-- Chatbot dùng chung cho cả nhân viên và admin sau khi đăng nhập. --%>
<jsp:include page="chatbot.jsp" />
