<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Trang quản lý nhân viên dùng JSTL để lặp danh sách users và phân nhánh quyền. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Danh sách nhân viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<jsp:include page="../../components/layout/header.jsp" />

<div class="wrapper">

    <jsp:include page="../../components/layout/sidebar-admin.jsp" />

    <div class="content">
        <div class="page-heading">
            <div>
                <span class="eyebrow">Quản lý người dùng</span>
                <h1>Danh sách nhân viên</h1>
                <p>Tìm kiếm tài khoản và quản lý vai trò truy cập hệ thống.</p>
            </div>
        </div>

        <div class="card table-card">
            <c:if test="${param.error == 'self-role'}">
                <div class="alert-error">Bạn không thể tự thay đổi quyền của tài khoản đang đăng nhập.</div>
            </c:if>
            <%-- Form GET gửi keyword lên AdminController.employees() để tìm kiếm user. --%>
            <form class="filter-bar" action="${pageContext.request.contextPath}/admin/employees" method="get">
                <div class="form-group">
                    <label>Tìm kiếm nhân viên</label>
                    <input type="text"
                           name="keyword"
                           value="${keyword}"
                           placeholder="Nhập họ tên, username hoặc email...">
                </div>

                <button type="submit">Tìm kiếm</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/employees">Làm mới</a>
            </form>

            <%-- users là danh sách được controller đưa vào Model. --%>
            <%-- Nếu không có user phù hợp thì hiện thông báo. --%>
            <c:if test="${empty users}">
                <div class="empty-state">Không tìm thấy nhân viên phù hợp.</div>
            </c:if>

            <div class="table-responsive">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Họ tên</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Vai trò</th>
                    <th>Thao tác</th>
                </tr>

                <%-- Lặp từng user để hiển thị một dòng trong bảng. --%>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.id}</td>
                        <td><c:out value="${user.fullName}"/></td>
                        <td><c:out value="${user.username}"/></td>
                        <td><c:out value="${user.email}"/></td>
                        <td><c:out value="${user.role}"/></td>
                        <td>
                            <%-- Admin không được tự đổi quyền của chính tài khoản đang đăng nhập. --%>
                            <c:choose>
                                <c:when test="${sessionScope.currentUser.id == user.id}">
                                    <span>Không thể tự đổi quyền</span>
                                </c:when>

                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/employees/update-role/${user.id}"
                                          method="post"
                                          style="display: flex; gap: 8px; align-items: center;">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">

                                        <select name="role" style="width: 150px;">
                                            <option value="EMPLOYEE"
                                                <c:if test="${user.role == 'EMPLOYEE'}">selected</c:if>>
                                                EMPLOYEE
                                            </option>

                                            <option value="MANAGER" <c:if test="${user.role == 'MANAGER'}">selected</c:if>>MANAGER</option>
                                            <option value="ADMIN"
                                                <c:if test="${user.role == 'ADMIN'}">selected</c:if>>
                                                ADMIN
                                            </option>
                                        </select>

                                        <button type="submit">Cập nhật</button>
                                    </form>
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
