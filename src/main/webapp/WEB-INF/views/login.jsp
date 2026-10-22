<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Trang login nhận lỗi/thông báo thành công qua query string trên URL. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-page">

<main class="auth-shell">
    <section class="auth-brand-panel">
        <a class="header-brand header-brand-light" href="${pageContext.request.contextPath}/home">
            <span class="brand-mark" aria-hidden="true">LF</span>
            <span class="brand-copy"><strong>LeaveFlow</strong><small>Quản lý nghỉ phép</small></span>
        </a>
        <div class="auth-brand-copy">
            <span class="eyebrow eyebrow-light">Không gian làm việc tập trung</span>
            <h2>Quản lý ngày nghỉ<br>nhẹ nhàng hơn mỗi ngày.</h2>
            <p>Tạo đơn, phê duyệt và theo dõi trạng thái trong một quy trình rõ ràng.</p>
        </div>
        <div class="auth-testimonial">
            <span>✦</span>
            <p>“Ít thao tác hơn, nhiều thời gian hơn cho những công việc thật sự quan trọng.”</p>
        </div>
    </section>

    <section class="auth-form-panel">
        <div class="auth-container">
            <a class="back-link" href="${pageContext.request.contextPath}/home">← Về trang chủ</a>
            <div class="auth-heading">
                <span class="eyebrow">Chào mừng trở lại</span>
                <h1>Đăng nhập tài khoản</h1>
                <p>Nhập thông tin để tiếp tục vào hệ thống.</p>
            </div>

    <%-- Nếu controller redirect về /login?error=true thì hiển thị lỗi đăng nhập. --%>
    <%
        String error = request.getParameter("error");
        if ("true".equals(error)) {
    %>
        <div class="alert-error">Sai tài khoản hoặc mật khẩu!</div>
    <%
        }
    %>
    <%-- Sau khi đăng ký thành công, AuthController redirect về /login?success=register. --%>
    <%
        String success = request.getParameter("success");
        if ("register".equals(success)) {
    %>
        <div class="alert-success">Đăng ký tài khoản thành công. Vui lòng đăng nhập.</div>
    <%
        }
    %>

    <%-- Form POST về /login, AuthController.login() sẽ nhận username và password. --%>
    <form action="${pageContext.request.contextPath}/login" method="post">

        <div class="form-group">
            <label>Tên đăng nhập</label>
            <input type="text" name="username" required>
        </div>

        <div class="form-group">
            <label>Mật khẩu</label>
            <input type="password" name="password" required>
        </div>

        <button class="btn-block" type="submit">Đăng nhập</button>

    </form>

    <div class="auth-link">
        Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
    </div>
        </div>
    </section>
</main>

</body>
</html>
