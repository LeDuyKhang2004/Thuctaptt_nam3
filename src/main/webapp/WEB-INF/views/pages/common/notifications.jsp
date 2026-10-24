<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Thông báo - LeaveFlow</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head>
<body><jsp:include page="../../components/layout/header.jsp"/><div class="wrapper"><c:choose><c:when test="${sessionScope.currentUser.role == 'ADMIN'}"><jsp:include page="../../components/layout/sidebar-admin.jsp"/></c:when><c:otherwise><jsp:include page="../../components/layout/sidebar-user.jsp"/></c:otherwise></c:choose>
<main class="content"><div class="page-heading"><div><span class="eyebrow">LeaveFlow • Thông báo</span><h1>${sessionScope.currentUser.role == 'ADMIN' ? 'Thông báo nhận được' : 'Thông báo từ công ty và Admin'}</h1><p>Bạn có <b>${unread}</b> thông báo chưa đọc.</p></div>
<form method="post" action="${pageContext.request.contextPath}/notifications/delete-read" onsubmit="return confirm('Xóa tất cả thông báo đã đọc?')"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button class="btn btn-secondary" type="submit">Xóa tất cả đã đọc</button></form></div>
<c:if test="${not empty param.success}"><div class="alert-success">Đã xóa thông báo đã đọc.</div></c:if>
<c:if test="${not empty param.error}"><div class="alert-error">Chỉ được xóa thông báo của chính bạn sau khi đánh dấu đã đọc.</div></c:if>
<div class="card"><div class="table-responsive"><table><thead><tr><th>Nội dung</th><th>Thời gian</th><th>Trạng thái</th><th>Thao tác</th></tr></thead><tbody>
<c:forEach var="n" items="${notices}"><tr><td><c:out value="${n.message}"/></td><td><c:out value="${n.createdAt}"/></td><td>${n.read ? 'Đã đọc' : 'Chưa đọc'}</td><td class="notice-actions">
<c:if test="${not n.read}"><form method="post" action="${pageContext.request.contextPath}/notifications/read/${n.id}"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button type="submit" class="btn btn-secondary">Đánh dấu đã đọc</button></form></c:if>
<c:if test="${n.read}"><form method="post" action="${pageContext.request.contextPath}/notifications/delete/${n.id}" onsubmit="return confirm('Xóa thông báo đã đọc này?')"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button type="submit" class="btn btn-danger">Xóa</button></form></c:if>
<c:if test="${not empty n.targetUrl}"><a class="btn btn-secondary" href="${pageContext.request.contextPath}${n.targetUrl}">Xem liên quan</a></c:if>
</td></tr></c:forEach><c:if test="${empty notices}"><tr><td colspan="4">Bạn chưa có thông báo nào.</td></tr></c:if>
</tbody></table></div></div></main></div></body></html>