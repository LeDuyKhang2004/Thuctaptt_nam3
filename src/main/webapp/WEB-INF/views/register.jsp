<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Trang đăng ký tài khoản nhân viên mới. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-page">

<main class="auth-shell">
    <section class="auth-brand-panel auth-brand-register">
        <a class="header-brand header-brand-light" href="${pageContext.request.contextPath}/home">
            <span class="brand-mark" aria-hidden="true">LF</span>
            <span class="brand-copy"><strong>LeaveFlow</strong><small>Quản lý nghỉ phép</small></span>
        </a>
        <div class="auth-brand-copy">
            <span class="eyebrow eyebrow-light">Bắt đầu chỉ trong một phút</span>
            <h2>Một tài khoản.<br>Mọi đơn nghỉ phép.</h2>
            <p>Đăng ký tài khoản nhân viên để tạo đơn và theo dõi quá trình xử lý ở bất cứ đâu.</p>
        </div>
        <ul class="auth-benefits">
            <li><b>✓</b> Mật khẩu được bảo vệ bằng PBKDF2</li>
            <li><b>✓</b> Theo dõi trạng thái đơn tập trung</li>
            <li><b>✓</b> Chatbot hỗ trợ ngay trong hệ thống</li>
        </ul>
    </section>

    <section class="auth-form-panel">
        <div class="auth-container auth-container-register">
            <a class="back-link" href="${pageContext.request.contextPath}/home">← Về trang chủ</a>
            <div class="auth-heading">
                <span class="eyebrow">Tạo tài khoản mới</span>
                <h1>Đăng ký nhân viên</h1>
                <p>Hoàn thành thông tin bên dưới để bắt đầu.</p>
            </div>
    <%-- Nếu username bị trùng, AuthController gửi request attribute "error" về lại trang này. --%>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <div class="alert-error">
            <%= error %>
        </div>
    <%
        }
    %>

    <%-- name của input phải khớp field trong UserEntity để @ModelAttribute bind dữ liệu. --%>
    <form action="${pageContext.request.contextPath}/register" method="post">

        <div class="form-group">
            <label>Họ tên</label>
            <input type="text" name="fullName" value="${user.fullName}" maxlength="100" required>
        </div>

        <div class="form-group">
            <label>Tên đăng nhập</label>
            <input type="text" name="username" value="${user.username}" minlength="3" maxlength="50" required>
        </div>

        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" value="${user.email}">
        </div>

        <div class="form-group">
            <label>Mật khẩu</label>
            <input type="password" name="password" minlength="8" maxlength="100" required>
        </div>

        <button class="btn-block" type="submit">Tạo tài khoản</button>

    </form>

    <div class="auth-link">
        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
    </div>
        </div>
    </section>
</main>

</body>
</html>
