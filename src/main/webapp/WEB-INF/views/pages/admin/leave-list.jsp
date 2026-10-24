<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Trang admin xem toàn bộ đơn nghỉ phép và duyệt/từ chối đơn đang PENDING. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<jsp:include page="../../components/layout/header.jsp" />

<div class="wrapper">

    <jsp:include page="../../components/layout/sidebar-admin.jsp" />

    <div class="content">
        <div class="page-heading">
            <div>
                <span class="eyebrow">Quy trình phê duyệt</span>
                <h1>Quản lý đơn nghỉ phép</h1>
                <p>Lọc, xem chi tiết và xử lý yêu cầu nghỉ phép của nhân viên.</p>
            </div>
        </div>

        <div class="card table-card">
            <%-- Thông báo sau khi redirect từ action duyệt đơn. --%>
            <c:if test="${param.success == 'approve'}">
                <div class="alert-success">
                    Duyệt đơn thành công.
                </div>
            </c:if>

            <c:if test="${param.success == 'reject'}">
                <div class="alert-success">
                    Từ chối đơn thành công.
                </div>
            </c:if>
            <c:if test="${not empty param.error}"><div class="alert-error">Đơn không hợp lệ hoặc đã được xử lý.</div></c:if>
            <%-- Form GET lọc theo status; controller trả lại selectedStatus để giữ lựa chọn. --%>
            <form class="filter-bar compact-filter" action="${pageContext.request.contextPath}/admin/leaves" method="get">
                <div class="form-group">
                    <label>Lọc theo trạng thái</label>

                    <select name="status" onchange="this.form.submit()">
                        <option value="ALL"
                            <c:if test="${selectedStatus == 'ALL'}">selected</c:if>>
                            Tất cả
                        </option>

                        <option value="PENDING"
                            <c:if test="${selectedStatus == 'PENDING'}">selected</c:if>>
                            Chờ duyệt
                        </option>

                        <option value="APPROVED"
                            <c:if test="${selectedStatus == 'APPROVED'}">selected</c:if>>
                            Đã duyệt
                        </option>

                        <option value="MANAGER_PENDING" <c:if test="${selectedStatus == 'MANAGER_PENDING'}">selected</c:if>>Chờ trưởng phòng</option>
                        <option value="CANCELLED" <c:if test="${selectedStatus == 'CANCELLED'}">selected</c:if>>Đã hủy</option>
                        <option value="REJECTED"
                            <c:if test="${selectedStatus == 'REJECTED'}">selected</c:if>>
                            Từ chối
                        </option>
                    </select>
                </div>
            </form>

            <%-- leaves là danh sách đơn nghỉ phép được AdminController đưa vào Model. --%>
            <div class="table-responsive">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Nhân viên</th>
                    <th>Loại</th><th>Lý do</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Ghi chú</th>
                    <th>Chi tiết</th>
                    <th>Thao tác</th>
                </tr>

                <%-- Lặp từng đơn nghỉ phép để render một dòng. --%>
                <c:forEach var="leave" items="${leaves}">
                    <tr>
                        <td>${leave.id}</td>
                        <td><c:out value="${leave.user.fullName}"/></td>
                        <td><c:out value="${leave.leaveType.name}"/></td><td><c:out value="${leave.reason}"/></td>
                        <td>${leave.startDate}</td>
                        <td>${leave.endDate}</td>

                        <td>
                            <%-- Hiển thị màu trạng thái theo status của đơn. --%>
                            <c:choose>
                                <c:when test="${leave.status == 'MANAGER_PENDING'}">
                                    <span class="status-pending">Chờ trưởng phòng</span>
                                </c:when>
                                <c:when test="${leave.status == 'PENDING'}">
                                    <span class="status-pending">Chờ duyệt</span>
                                </c:when>
                                <c:when test="${leave.status == 'APPROVED'}">
                                    <span class="status-approved">Đã duyệt</span>
                                </c:when>
                                <c:when test="${leave.status == 'CANCELLED'}">
                                    <span>Đã hủy</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-rejected">Từ chối</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${leave.adminNote}"/></td>

                        <td>
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/admin/leaves/detail/${leave.id}">
                                Xem
                            </a>
                        </td>

                       <td>
                           <%-- Chỉ đơn PENDING mới có nút duyệt/từ chối. --%>
                           <c:choose>
                               <c:when test="${leave.status == 'PENDING'}">
                                   <form method="post" style="display:inline" action="${pageContext.request.contextPath}/admin/leaves/approve/${leave.id}" onsubmit="return confirm('Duyệt đơn này?')"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><button class="btn btn-success">Duyệt</button></form>

                                   <form action="${pageContext.request.contextPath}/admin/leaves/reject/${leave.id}"
                                         method="post"
                                         style="display:inline-block; margin-top:6px;"
                                         onsubmit="return confirm('Bạn có chắc muốn từ chối đơn này không?')">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">

                                       <input type="text"
                                              name="adminNote"
                                              placeholder="Lý do từ chối"
                                              required>

                                       <button class="btn-danger" type="submit">Từ chối</button>
                                   </form>
                               </c:when>

                               <c:otherwise>
                                   <span>Đã xử lý</span>
                               </c:otherwise>
                           </c:choose>
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
