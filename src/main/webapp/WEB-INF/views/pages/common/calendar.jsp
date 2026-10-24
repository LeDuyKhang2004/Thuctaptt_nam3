<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Lịch nghỉ công ty - LeaveFlow</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head>
<body><jsp:include page="../../components/layout/header.jsp"/><div class="wrapper">
<c:choose><c:when test="${sessionScope.currentUser.role == 'ADMIN'}"><jsp:include page="../../components/layout/sidebar-admin.jsp"/></c:when><c:otherwise><jsp:include page="../../components/layout/sidebar-user.jsp"/></c:otherwise></c:choose>
<main class="content"><div class="page-heading"><div><span class="eyebrow">LeaveFlow • Lịch chung</span><h1>Lịch nghỉ của công ty</h1><p>Ngày nghỉ do Admin công bố được hiển thị trên lịch của toàn bộ nhân viên.</p></div></div>
<c:if test="${not empty param.success}"><div class="alert-success">Đã cập nhật ngày nghỉ công ty và gửi thông báo cho nhân viên.</div></c:if>
<c:if test="${not empty param.error}"><div class="alert-error">Không thể cập nhật ngày nghỉ. Có thể ngày này đã tồn tại hoặc dữ liệu chưa hợp lệ.</div></c:if>
<c:if test="${sessionScope.currentUser.role == 'ADMIN'}"><div class="card" style="margin-bottom:18px"><h2>Công bố ngày nghỉ công ty</h2>
<form class="lf-form inline-form" method="post" action="${pageContext.request.contextPath}/admin/holidays">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
<label>Ngày nghỉ<input type="date" name="date" required></label><label>Tên sự kiện / ngày nghỉ<input name="name" maxlength="120" required placeholder="Ví dụ: Nghỉ lễ Quốc khánh"></label>
<button class="btn" type="submit">Thêm vào lịch và gửi thông báo</button></form>
<p class="small-note">Khi thêm hoặc xóa ngày nghỉ, hệ thống tự gửi thông báo tới tất cả tài khoản User (nhân viên và trưởng phòng).</p></div></c:if>
<div class="card"><div class="filter-bar lf-cal-toolbar"><button type="button" class="btn btn-secondary" onclick="moveMonth(-1)">← Tháng trước</button><h2 id="calendarLabel" aria-live="polite"></h2><button type="button" class="btn btn-secondary" onclick="moveMonth(1)">Tháng sau →</button><button type="button" class="btn btn-secondary" onclick="goToday()">Hôm nay</button></div>
<div class="calendar-grid" id="calendarGrid"></div>
<p class="lf-legend"><span class="lf-company-dot"></span> Ngày nghỉ công ty &nbsp;&nbsp; <span class="lf-leave-dot"></span> Đơn nghỉ phép đã duyệt</p>
<div id="calendarData" hidden>
<c:forEach var="h" items="${holidays}"><span data-holiday-date="${h.date}" data-holiday-name="${fn:escapeXml(h.name)}"></span></c:forEach>
<c:forEach var="l" items="${leaves}"><span data-start="${l.startDate}" data-end="${l.endDate}" data-person="${fn:escapeXml(l.user.fullName)}" data-id="${l.id}"></span></c:forEach>
</div></div>
<div class="card" style="margin-top:18px"><h2>Danh sách ngày nghỉ công ty</h2><div class="table-responsive"><table><thead><tr><th>Ngày</th><th>Tên ngày nghỉ</th><c:if test="${sessionScope.currentUser.role == 'ADMIN'}"><th>Thao tác</th></c:if></tr></thead><tbody>
<c:forEach var="h" items="${holidays}"><tr><td>${h.date}</td><td><c:out value="${h.name}"/></td><c:if test="${sessionScope.currentUser.role == 'ADMIN'}"><td><form method="post" action="${pageContext.request.contextPath}/admin/holidays/delete/${h.id}" onsubmit="return confirm('Xóa ngày nghỉ và gửi thông báo cập nhật đến mọi nhân viên?')"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button type="submit" class="btn btn-danger">Xóa ngày nghỉ</button></form></td></c:if></tr></c:forEach>
<c:if test="${empty holidays}"><tr><td colspan="3">Chưa có ngày nghỉ công ty được công bố.</td></tr></c:if></tbody></table></div></div>
<script src="${pageContext.request.contextPath}/assets/js/company-calendar.js"></script>
</main></div></body></html>