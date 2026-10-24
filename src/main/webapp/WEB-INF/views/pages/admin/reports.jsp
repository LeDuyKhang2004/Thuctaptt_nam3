<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><meta name="viewport" content="width=device-width, initial-scale=1"><title>Báo cáo và thống kê - LeaveFlow</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head>
<body><jsp:include page="../../components/layout/header.jsp"/><div class="wrapper"><jsp:include page="../../components/layout/sidebar-admin.jsp"/>
<main class="content"><div class="page-heading"><div><span class="eyebrow">LeaveFlow • Enhanced</span><h1>Báo cáo và thống kê</h1></div></div><div class="card"><form method="get" class="filter-bar"><label>Năm <input type="number" name="year" min="2000" max="2100" value="${year}"></label><button>Lọc</button>
<a class="btn" href="${pageContext.request.contextPath}/admin/reports/export?year=${year}">Xuất CSV (mở được bằng Excel)</a></form>
<h2>Thống kê theo tháng (không cần kết nối Internet)</h2><div id="monthlyReport" class="month-report" aria-label="Số đơn theo tháng"></div>
<div class="table-responsive"><table id="reportRows"><thead><tr><th>Mã đơn</th><th>Nhân viên</th><th>Tháng</th><th>Loại</th><th>Ngày làm việc</th><th>Trạng thái</th></tr></thead><tbody>
<c:forEach var="l" items="${leaves}"><tr data-month="${l.startDate.monthValue}" data-status="${l.status}"><td>${l.id}</td><td><c:out value="${l.user.fullName}"/></td><td>${l.startDate.monthValue}</td><td><c:out value="${l.leaveType.name}"/></td><td>${l.workDays}</td><td><c:out value="${l.status}"/></td></tr></c:forEach>
</tbody></table></div></div><script>(function(){
 const counts=Array(12).fill(0),accepted=Array(12).fill(0);
 document.querySelectorAll('#reportRows tbody tr').forEach(row=>{const i=Number(row.dataset.month)-1;if(i>=0&&i<12){counts[i]++;if(row.dataset.status==='APPROVED')accepted[i]++;}});
 const max=Math.max(1,...counts),root=document.getElementById('monthlyReport');
 for(let i=0;i<12;i++){const group=document.createElement('div');group.className='month-col';
  const text=document.createElement('span');text.textContent=counts[i]+' đơn / '+accepted[i]+' duyệt';
  const bg=document.createElement('div');bg.className='month-track';
  const bar=document.createElement('div');bar.className='month-bar';bar.style.height=(counts[i]/max*100)+'%';
  const approved=document.createElement('div');approved.className='month-approved';approved.style.height=(accepted[i]/max*100)+'%';
  bg.append(bar,approved); const label=document.createElement('strong');label.textContent='T'+(i+1);group.append(text,bg,label);root.append(group);
 }
})();</script></main></div></body></html>
