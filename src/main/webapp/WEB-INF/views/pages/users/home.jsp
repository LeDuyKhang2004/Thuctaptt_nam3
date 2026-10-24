<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Trang home của nhân viên sau khi đăng nhập thành công. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Trang nhân viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<%-- Nhúng header và sidebar dành cho nhân viên. --%>
<jsp:include page="../../components/layout/header.jsp" />

<div class="wrapper">

    <jsp:include page="../../components/layout/sidebar-user.jsp" />

    <main class="content">
        <section class="welcome-banner">
            <div>
                <span class="eyebrow eyebrow-light">Không gian của bạn</span>
                <h1>Xin chào, ${sessionScope.currentUser.fullName}!</h1>
                <p>Tạo đơn nghỉ phép mới hoặc theo dõi quá trình xử lý đơn của bạn.</p>
                <div class="welcome-actions">
                    <a class="btn btn-light" href="${pageContext.request.contextPath}/leave/create">Tạo đơn nghỉ phép</a>
                    <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/leave/my">Xem đơn của tôi</a>
                </div>
            </div>
            <div class="welcome-illustration" aria-hidden="true">
                <a href="${pageContext.request.contextPath}/user/calendar" class="calendar-card"><span></span><strong>📅</strong><small>LỊCH CÔNG TY</small></a>
                <div class="check-orbit">✓</div>
            </div>
        </section>

        <div class="page-heading compact-heading">
            <div>
                <span class="eyebrow">Thao tác nhanh</span>
                <h2>Bạn muốn làm gì hôm nay?</h2>
            </div>
        </div>

        <div class="quick-action-grid employee-actions">
            <a class="quick-action-card" href="${pageContext.request.contextPath}/user/calendar"><span class="quick-action-icon">▦</span><span><strong>Lịch nghỉ của công ty</strong><small>Xem ngày nghỉ do Admin công bố</small></span><b>→</b></a>
            <a class="quick-action-card" href="${pageContext.request.contextPath}/user/notifications"><span class="quick-action-icon">✉</span><span><strong>Thông báo từ Admin</strong><small>Đọc hoặc xóa thông báo đã xem</small></span><b>→</b></a><a class="quick-action-card" href="${pageContext.request.contextPath}/leave/balance"><span class="quick-action-icon">▣</span><span><strong>Ngày phép của tôi</strong><small>Kiểm tra số dư phép năm</small></span><b>→</b></a>
            <a class="quick-action-card" href="${pageContext.request.contextPath}/leave/create">
                <span class="quick-action-icon action-create">＋</span>
                <span><strong>Tạo đơn nghỉ phép</strong><small>Gửi yêu cầu nghỉ mới đến quản trị viên</small></span>
                <b>→</b>
            </a>
            <a class="quick-action-card" href="${pageContext.request.contextPath}/leave/my">
                <span class="quick-action-icon action-list">▤</span>
                <span><strong>Đơn của tôi</strong><small>Theo dõi trạng thái và xem ghi chú</small></span>
                <b>→</b>
            </a>
        </div>
    </main>

</div>

</body>
</html>
