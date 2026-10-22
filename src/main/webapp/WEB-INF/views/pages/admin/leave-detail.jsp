<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Trang chi tiết đơn nghỉ phép cho admin; biến leave được gửi từ AdminController. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chi tiết đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<jsp:include page="../../components/layout/header.jsp" />

<div class="wrapper">

    <jsp:include page="../../components/layout/sidebar-admin.jsp" />

    <div class="content">
        <div class="page-heading">
            <div>
                <span class="eyebrow">Mã đơn #${leave.id}</span>
                <h1>Chi tiết đơn nghỉ phép</h1>
                <p>Xem thông tin nhân viên và xử lý yêu cầu đang chờ duyệt.</p>
            </div>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/leaves">← Quay lại danh sách</a>
        </div>

        <div class="card detail-card">

            <%-- Bảng này hiển thị đầy đủ thông tin đơn và thông tin nhân viên tạo đơn. --%>
            <div class="table-responsive detail-table-wrap">
            <table class="detail-table">
                <tr>
                    <th>ID đơn</th>
                    <td>${leave.id}</td>
                </tr>

                <tr>
                    <th>Nhân viên</th>
                    <td>${leave.user.fullName}</td>
                </tr>

                <tr>
                    <th>Username</th>
                    <td>${leave.user.username}</td>
                </tr>

                <tr>
                    <th>Email</th>
                    <td>${leave.user.email}</td>
                </tr>

                <tr>
                    <th>Lý do nghỉ</th>
                    <td>${leave.reason}</td>
                </tr>

                <tr>
                    <th>Ngày bắt đầu</th>
                    <td>${leave.startDate}</td>
                </tr>

                <tr>
                    <th>Ngày kết thúc</th>
                    <td>${leave.endDate}</td>
                </tr>

                <tr>
                    <th>Trạng thái</th>
                    <td>
                        <%-- Đổi màu nhãn trạng thái dựa theo leave.status. --%>
                        <c:choose>
                            <c:when test="${leave.status == 'PENDING'}">
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
                </tr>

                <tr>
                    <th>Ghi chú admin</th>
                    <td>${leave.adminNote}</td>
                </tr>

                <tr>
                    <th>Ngày tạo</th>
                    <td>${leave.createdAt}</td>
                </tr>
            </table>
            </div>

            <%-- Nếu đơn còn PENDING thì admin có thể duyệt ngay từ trang chi tiết. --%>
            <div class="detail-actions">
                <c:if test="${leave.status == 'PENDING'}">
                    <a class="btn btn-success"
                       href="${pageContext.request.contextPath}/admin/leaves/approve/${leave.id}"
                       onclick="return confirm('Bạn có chắc muốn duyệt đơn này không?')">
                        ✓ Duyệt đơn
                    </a>
                </c:if>
            </div>
        </div>
    </div>

</div>

</body>
</html>
