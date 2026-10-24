<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Gửi thông báo - LeaveFlow</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<jsp:include page="../../components/layout/header.jsp"/><div class="wrapper"><jsp:include page="../../components/layout/sidebar-admin.jsp"/>
<main class="content"><div class="page-heading"><div><span class="eyebrow">Admin • Thông báo</span><h1>Gửi thông báo đến nhân viên</h1><p>Thông báo sẽ xuất hiện trong khu vực User ngay sau khi gửi.</p></div></div>
<c:if test="${not empty param.sent}"><div class="alert-success">Đã gửi thông báo tới <c:out value="${param.sent}"/> tài khoản.</div></c:if>
<c:if test="${not empty param.error}"><div class="alert-error">Không gửi được. Kiểm tra nội dung và người nhận.</div></c:if>
<div class="card"><h2>Soạn thông báo</h2>
<form action="${pageContext.request.contextPath}/admin/announcements/send" method="post" class="lf-form">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
<label>Người nhận<select name="recipientId"><option value="">Tất cả nhân viên và trưởng phòng</option>
<c:forEach var="u" items="${users}"><option value="${u.id}"><c:out value="${u.fullName}"/> (<c:out value="${u.username}"/>)</option></c:forEach></select></label>
<label>Nội dung thông báo<textarea name="message" rows="5" maxlength="500" required placeholder="Ví dụ: Công ty tổ chức họp vào sáng thứ Hai..."></textarea></label>
<button class="btn" type="submit" onclick="return confirm('Gửi thông báo đến các tài khoản được chọn?')">Gửi thông báo</button></form></div>
<div class="card" style="margin-top:18px"><h2>Lịch sử thông báo đã gửi</h2><div class="table-responsive"><table><thead><tr><th>Thời gian</th><th>Nội dung và số người nhận</th></tr></thead><tbody>
<c:forEach var="log" items="${sentLogs}"><tr><td><c:out value="${log.createdAt}"/></td><td><c:out value="${log.detail}"/></td></tr></c:forEach>
<c:if test="${empty sentLogs}"><tr><td colspan="2">Chưa có thông báo được gửi.</td></tr></c:if></tbody></table></div></div>
</main></div></body></html>