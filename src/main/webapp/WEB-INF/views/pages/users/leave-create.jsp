<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Trang tạo đơn nghỉ phép; form POST về LeaveRequestController.createLeaveRequest(). --%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tạo đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<jsp:include page="../../components/layout/header.jsp" />

<div class="wrapper">

    <jsp:include page="../../components/layout/sidebar-user.jsp" />

    <div class="content">
        <div class="page-heading">
            <div>
                <span class="eyebrow">Yêu cầu nghỉ mới</span>
                <h1>Tạo đơn xin nghỉ phép</h1>
                <p>Điền đầy đủ thông tin; đơn sẽ được gửi đến quản trị viên để xét duyệt.</p>
            </div>
        </div>

        <div class="card form-card">
            <%-- Nếu validate lỗi trong controller, error được gửi về lại trang này. --%>
            <c:if test="${not empty error}">
                <div class="alert-error">
                    ${error}
                </div>
            </c:if>

            <%-- name của input phải khớp field reason/startDate/endDate trong LeaveRequestEntity. --%>
            <form action="${pageContext.request.contextPath}/leave/create" method="post">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">

                <p class="small-note">Phép năm còn khả dụng (đã tính đơn chờ duyệt): <strong>${balance} ngày</strong>.</p>
                <div class="form-group"><label>Loại nghỉ phép</label><select name="typeId" required>
                  <c:forEach var="t" items="${types}"><option value="${t.id}"><c:out value="${t.name}"/></option></c:forEach>
                </select></div>
                <div class="form-group">
                    <label>Lý do nghỉ <span class="required-mark">*</span></label>
                    <textarea name="reason" rows="5" placeholder="Nhập lý do xin nghỉ..." required>${leaveRequest.reason}</textarea>
                </div>

                <div class="form-group">
                    <label>Ngày bắt đầu <span class="required-mark">*</span></label>
                    <input type="date" name="startDate" value="${leaveRequest.startDate}" required>
                </div>

                <div class="form-group">
                    <label>Ngày kết thúc <span class="required-mark">*</span></label>
                    <input type="date" name="endDate" value="${leaveRequest.endDate}" required>
                </div>

                <div class="form-actions">
                    <button type="submit">Gửi đơn nghỉ phép</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/leave/my">Hủy và quay lại</a>
                </div>

            </form>
        </div>
    </div>

</div>

</body>
</html>
