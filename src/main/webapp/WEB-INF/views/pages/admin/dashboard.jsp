<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Dashboard admin nhận totalLeaves/pendingLeaves/approvedLeaves/rejectedLeaves từ AdminController. --%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<%-- Nhúng header và sidebar dùng chung cho khu vực admin. --%>
<jsp:include page="../../components/layout/header.jsp" />

<div class="wrapper">

    <jsp:include page="../../components/layout/sidebar-admin.jsp" />

    <main class="content">
        <div class="page-heading">
            <div>
                <span class="eyebrow">Tổng quan hệ thống</span>
                <h1>Dashboard quản trị</h1>
                <p>Chào ${sessionScope.currentUser.fullName}, đây là tình hình nghỉ phép hiện tại.</p>
            </div>
            <a class="btn" href="${pageContext.request.contextPath}/admin/leaves?status=PENDING">Xử lý đơn chờ duyệt</a>
        </div>

        <%-- Bốn ô thống kê lấy dữ liệu từ model.addAttribute trong dashboard(). --%>
        <div class="dashboard-grid">
            <div class="stat-card stat-total">
                <span class="stat-icon">∑</span>
                <div><h3>Tổng đơn</h3><p>${totalLeaves}</p><small>Toàn hệ thống</small></div>
            </div>

            <div class="stat-card stat-pending">
                <span class="stat-icon">◷</span>
                <div><h3>Chờ duyệt</h3><p>${pendingLeaves}</p><small>Cần xử lý</small></div>
            </div>

            <div class="stat-card stat-approved">
                <span class="stat-icon">✓</span>
                <div><h3>Đã duyệt</h3><p>${approvedLeaves}</p><small>Đã chấp nhận</small></div>
            </div>

            <div class="stat-card stat-rejected">
                <span class="stat-icon">×</span>
                <div><h3>Từ chối</h3><p>${rejectedLeaves}</p><small>Không chấp nhận</small></div>
            </div>
        </div>

        <div class="card" style="margin-top:18px;padding:20px">
            <h2>Trạng thái duyệt đơn</h2><canvas id="statusGraph" height="85"></canvas>
            <p class="small-note">Biểu đồ cần tải được Chart.js. Các số liệu ở phía trên vẫn hiển thị khi offline.</p>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.8/dist/chart.umd.min.js"></script>
        <script>if(window.Chart)new Chart(document.getElementById('statusGraph'),{type:'doughnut',data:{labels:['Chờ duyệt','Đã duyệt','Từ chối'],datasets:[{data:[Number('${pendingLeaves}'),Number('${approvedLeaves}'),Number('${rejectedLeaves}')]}]},options:{responsive:true}});</script>
        <div class="quick-action-grid">
            <a class="quick-action-card" href="${pageContext.request.contextPath}/admin/announcements"><span class="quick-action-icon">✉</span><span><strong>Gửi thông báo</strong><small>Gửi một người hoặc toàn bộ nhân viên</small></span><b>→</b></a>
            <a class="quick-action-card" href="${pageContext.request.contextPath}/admin/calendar"><span class="quick-action-icon">▦</span><span><strong>Lịch nghỉ công ty</strong><small>Thêm ngày nghỉ và thông báo tự động</small></span><b>→</b></a>
            <a class="quick-action-card" href="${pageContext.request.contextPath}/admin/reports"><span class="quick-action-icon">▥</span><span><strong>Báo cáo biểu đồ và Excel</strong><small>Xem xu hướng nghỉ theo tháng và xuất CSV</small></span><b>→</b></a>
            <a class="quick-action-card" href="${pageContext.request.contextPath}/admin/leaves">
                <span class="quick-action-icon">▤</span>
                <span><strong>Quản lý đơn nghỉ phép</strong><small>Xem, lọc và xử lý toàn bộ đơn</small></span>
                <b>→</b>
            </a>
            <a class="quick-action-card" href="${pageContext.request.contextPath}/admin/employees">
                <span class="quick-action-icon">◎</span>
                <span><strong>Danh sách nhân viên</strong><small>Tìm kiếm và cập nhật quyền tài khoản</small></span>
                <b>→</b>
            </a>
        </div>
    </main>

</div>

</body>
</html>
