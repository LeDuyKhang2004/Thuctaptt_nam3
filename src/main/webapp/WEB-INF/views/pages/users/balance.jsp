<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><meta name="viewport" content="width=device-width, initial-scale=1"><title>Số dư phép năm - LeaveFlow</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head>
<body><jsp:include page="../../components/layout/header.jsp"/><div class="wrapper"><jsp:include page="../../components/layout/sidebar-user.jsp"/>
<main class="content"><div class="page-heading"><div><span class="eyebrow">LeaveFlow • Enhanced</span><h1>Số dư phép năm</h1></div></div><div class="dashboard-grid"><div class="stat-card stat-total"><h3>Năm</h3><p>${year}</p></div><div class="stat-card stat-approved"><h3>Được cấp</h3><p>${balance.allocatedDays}</p></div>
<div class="stat-card stat-pending"><h3>Đã dùng + đang chờ</h3><p>${reserved}</p></div><div class="stat-card stat-approved"><h3>Còn khả dụng</h3><p>${remaining}</p></div></div>
<div class="card"><p>Đơn phép năm ở trạng thái chờ duyệt cũng được giữ chỗ để tránh xin vượt hạn mức. Nếu đơn bị từ chối hoặc hủy, ngày phép sẽ được giải phóng tự động.</p></div></main></div></body></html>