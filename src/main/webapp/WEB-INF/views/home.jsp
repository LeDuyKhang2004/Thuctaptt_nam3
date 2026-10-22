<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Trang public giới thiệu hệ thống, không yêu cầu đăng nhập. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LeaveFlow - Quản lý nghỉ phép thông minh</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="landing-page">

<header class="landing-nav">
    <a class="header-brand" href="${pageContext.request.contextPath}/home">
        <span class="brand-mark" aria-hidden="true">LF</span>
        <span class="brand-copy">
            <strong>LeaveFlow</strong>
            <small>Quản lý nghỉ phép</small>
        </span>
    </a>
    <div class="landing-nav-actions">
        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        <a class="btn" href="${pageContext.request.contextPath}/register">Tạo tài khoản</a>
    </div>
</header>

<main>
    <section class="hero-section">
        <div class="hero-copy">
            <span class="eyebrow">Vận hành nhân sự nhẹ nhàng hơn</span>
            <h1>Ngày nghỉ rõ ràng.<br><span>Đội ngũ an tâm.</span></h1>
            <p>Một không gian chung để nhân viên gửi yêu cầu, quản trị viên phê duyệt và mọi người luôn biết đơn của mình đang ở đâu.</p>
            <div class="hero-actions">
                <a class="btn btn-lg" href="${pageContext.request.contextPath}/register">Bắt đầu sử dụng</a>
                <a class="btn btn-lg btn-ghost" href="${pageContext.request.contextPath}/login">Tôi đã có tài khoản</a>
            </div>
            <div class="hero-trust">
                <span>✓ Trạng thái minh bạch</span>
                <span>✓ Dữ liệu đúng vai trò</span>
                <span>✓ Hỗ trợ ngay khi cần</span>
            </div>
        </div>

        <div class="hero-visual" aria-label="Minh họa bảng điều khiển nghỉ phép">
            <div class="mock-window">
                <div class="mock-window-bar"><i></i><i></i><i></i></div>
                <div class="mock-dashboard">
                    <div class="mock-sidebar"></div>
                    <div class="mock-content">
                        <div class="mock-title"></div>
                        <div class="mock-stats">
                            <div><strong>24</strong><span>Tổng đơn</span></div>
                            <div><strong>05</strong><span>Chờ duyệt</span></div>
                            <div><strong>16</strong><span>Đã duyệt</span></div>
                        </div>
                        <div class="mock-table">
                            <span></span><span></span><span></span><span></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="floating-note note-approved"><b>✓</b><span>Đơn đã được duyệt</span></div>
            <div class="floating-note note-chat"><b>✦</b><span>Chatbot luôn sẵn sàng</span></div>
        </div>
    </section>

    <section class="feature-section">
        <div class="section-heading">
            <span class="eyebrow">Một quy trình, không còn rối</span>
            <h2>Thiết kế để ai cũng dùng được ngay</h2>
        </div>
        <div class="feature-grid">
            <article class="feature-card">
                <span class="feature-number">01</span>
                <h3>Tạo đơn dễ dàng</h3>
                <p>Điền lý do và thời gian nghỉ trên biểu mẫu rõ ràng, kiểm tra dữ liệu ngay khi gửi.</p>
            </article>
            <article class="feature-card">
                <span class="feature-number">02</span>
                <h3>Xử lý minh bạch</h3>
                <p>Theo dõi trạng thái PENDING, APPROVED, REJECTED cùng ghi chú từ quản trị viên.</p>
            </article>
            <article class="feature-card">
                <span class="feature-number">03</span>
                <h3>Hỗ trợ tức thời</h3>
                <p>Chatbot hướng dẫn thao tác và tra cứu thống kê phù hợp với quyền của từng người dùng.</p>
            </article>
        </div>
    </section>
</main>

<footer class="landing-footer">
    <span>© 2026 LeaveFlow</span>
    <span>Hệ thống quản lý nghỉ phép doanh nghiệp</span>
</footer>

</body>
</html>
