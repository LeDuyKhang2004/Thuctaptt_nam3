<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="currentPath" value="${pageContext.request.requestURI}"/>
<div class="sidebar">
 <div class="sidebar-heading"><span>Khu vực làm việc</span><strong>${sessionScope.currentUser.role == 'MANAGER' ? 'Trưởng phòng' : 'Nhân viên'} • User</strong></div>
 <nav class="sidebar-nav" aria-label="Menu nhân viên">
  <a class="${fn:contains(currentPath,'/user/home') ? 'active' : ''}" href="${pageContext.request.contextPath}/user/home">Tổng quan</a>
  <a class="${fn:contains(currentPath,'/leave/create') ? 'active' : ''}" href="${pageContext.request.contextPath}/leave/create">Tạo đơn nghỉ phép</a>
  <a class="${fn:contains(currentPath,'/leave/my') || fn:contains(currentPath,'/leave/detail') ? 'active' : ''}" href="${pageContext.request.contextPath}/leave/my">Đơn của tôi</a>
  <a class="${fn:contains(currentPath,'/leave/balance') ? 'active' : ''}" href="${pageContext.request.contextPath}/leave/balance">Số ngày phép của tôi</a>
  <a class="${fn:contains(currentPath,'/user/calendar') ? 'active' : ''}" href="${pageContext.request.contextPath}/user/calendar">Lịch nghỉ công ty</a>
  <a class="${fn:contains(currentPath,'/user/notifications') ? 'active' : ''}" href="${pageContext.request.contextPath}/user/notifications">Thông báo từ Admin</a>
  <c:if test="${sessionScope.currentUser.role == 'MANAGER'}"><a class="${fn:contains(currentPath,'/manager/leaves') ? 'active' : ''}" href="${pageContext.request.contextPath}/manager/leaves">Duyệt đơn của phòng</a></c:if>
 </nav>
 <div class="sidebar-footer"><form method="post" action="${pageContext.request.contextPath}/logout"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button class="logout-link" type="submit">Đăng xuất</button></form></div>
</div>
