<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><meta name="viewport" content="width=device-width, initial-scale=1"><title>Duyệt đơn cấp trưởng phòng - LeaveFlow</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head>
<body><jsp:include page="../../components/layout/header.jsp"/><div class="wrapper"><jsp:include page="../../components/layout/sidebar-user.jsp"/>
<main class="content"><div class="page-heading"><div><span class="eyebrow">LeaveFlow • Enhanced</span><h1>Duyệt đơn cấp trưởng phòng</h1></div></div><div class="card"><p>Chỉ hiển thị các đơn đang chờ duyệt thuộc phòng ban bạn quản lý. Sau khi trưởng phòng đồng ý, đơn chuyển tới Admin.</p>
<c:if test="${not empty param.error}"><div class="alert-error">Không thể xử lý đơn; kiểm tra quyền hoặc trạng thái.</div></c:if>
<div class="table-responsive"><table><thead><tr><th>Đơn</th><th>Nhân viên</th><th>Từ–đến</th><th>Lý do</th><th>Xử lý</th></tr></thead><tbody>
<c:forEach var="l" items="${leaves}"><tr><td>#${l.id}</td><td><c:out value="${l.user.fullName}"/></td><td>${l.startDate} → ${l.endDate}</td><td><c:out value="${l.reason}"/></td><td>
<form method="post" action="${pageContext.request.contextPath}/manager/decision/${l.id}"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><input type="hidden" name="decision" value="approve"><button class="btn btn-success">Chuyển Admin</button></form>
<form method="post" action="${pageContext.request.contextPath}/manager/decision/${l.id}"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><input type="hidden" name="decision" value="reject"><input name="note" placeholder="Lý do từ chối"><button class="btn btn-danger">Từ chối</button></form></td></tr></c:forEach>
</tbody></table></div><c:if test="${empty leaves}"><p>Không có đơn chờ duyệt.</p></c:if></div></main></div></body></html>