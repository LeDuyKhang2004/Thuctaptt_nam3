<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="currentPath" value="${pageContext.request.requestURI}"/>
<div class="sidebar">
 <div class="sidebar-heading"><span>Khu vực làm việc</span><strong>Quản trị viên • Admin</strong></div>
 <nav class="sidebar-nav" aria-label="Menu quản trị">
  <a class="${fn:contains(currentPath,'/admin/dashboard') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/dashboard">Tổng quan</a>
  <a class="${fn:contains(currentPath,'/admin/leaves') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/leaves">Quản lý đơn nghỉ phép</a>
  <a class="${fn:contains(currentPath,'/admin/employees') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/employees">Nhân viên và phân quyền</a>
  <a class="${fn:contains(currentPath,'/admin/announcements') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/announcements">Gửi thông báo cho User</a>
  <a class="${fn:contains(currentPath,'/admin/calendar') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/calendar">Lịch nghỉ của công ty</a>
  <a class="${fn:contains(currentPath,'/admin/settings') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/settings">Cài đặt / phép năm / phòng ban</a>
  <a class="${fn:contains(currentPath,'/admin/reports') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/reports">Báo cáo và xuất CSV</a>
  <a class="${fn:contains(currentPath,'/admin/audit') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/audit">Nhật ký hoạt động</a>
  <a class="${fn:contains(currentPath,'/admin/notifications') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/notifications">Thông báo nhận được</a>
 </nav>
 <div class="sidebar-footer"><form method="post" action="${pageContext.request.contextPath}/logout"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button class="logout-link" type="submit">Đăng xuất</button></form></div>
</div>
