<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Trang hiển thị danh sách đơn nghỉ phép của chính nhân viên đang đăng nhập. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đơn của tôi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<jsp:include page="../../components/layout/header.jsp" />

<div class="wrapper">

    <jsp:include page="../../components/layout/sidebar-user.jsp" />

    <div class="content">
        <div class="page-heading">
            <div>
                <span class="eyebrow">Theo dõi yêu cầu</span>
                <h1>Đơn xin nghỉ phép của tôi</h1>
                <p>Xem trạng thái, ghi chú và thông tin chi tiết của từng đơn.</p>
            </div>
            <a class="btn" href="${pageContext.request.contextPath}/leave/create">＋ Tạo đơn mới</a>
        </div>

        <div class="card table-card">
            <%-- Thông báo sau khi tạo đơn thành công và redirect về /leave/my. --%>
            <c:if test="${param.success == 'create'}">
                <div class="alert-success">
                    Tạo đơn xin nghỉ phép thành công.
                </div>
            </c:if>

            <%-- leaves là danh sách đơn của currentUser được controller đưa vào Model. --%>
            <div class="table-responsive">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Loại</th><th>Lý do</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Ghi chú admin</th>
                    <th>Chi tiết</th>
                    <th>Thao tác</th>

                </tr>

                <%-- Lặp từng đơn nghỉ phép của nhân viên. --%>
                <c:forEach var="leave" items="${leaves}">
                    <tr>
                        <td>${leave.id}</td>
                        <td><c:out value="${leave.leaveType.name}"/></td><td><c:out value="${leave.reason}"/></td>
                        <td>${leave.startDate}</td>
                        <td>${leave.endDate}</td>

                        <td>
                            <%-- Hiển thị trạng thái bằng màu khác nhau. --%>
                            <c:choose>
                                <c:when test="${leave.status == 'PENDING' || leave.status == 'MANAGER_PENDING'}">
                                    <span class="status-pending">Chờ duyệt</span>
                                </c:when>
                                <c:when test="${leave.status == 'APPROVED'}">
                                    <span class="status-approved">Đã duyệt</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-rejected">Từ chối</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${leave.adminNote}"/></td>

                        <td>
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/leave/detail/${leave.id}">
                                Xem
                            </a>
                        </td>
                        <td>
                            <%-- Chỉ đơn PENDING mới được nhân viên hủy. --%>
                            <c:if test="${leave.status == 'PENDING' || leave.status == 'MANAGER_PENDING'}">
                                <form method="post" action="${pageContext.request.contextPath}/leave/cancel/${leave.id}" onsubmit="return confirm('Bạn muốn hủy đơn?')"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button class="btn btn-danger" type="submit">Hủy đơn</button></form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </div>
        </div>
    </div>

</div>

</body>
</html>
