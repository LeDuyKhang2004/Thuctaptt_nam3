<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="isAdmin" value="${sessionScope.currentUser.role == 'ADMIN'}" />
<div id="chatbotWidget"
     class="chatbot-widget"
     data-endpoint="${pageContext.request.contextPath}/chatbot/message"
     data-user-id="${sessionScope.currentUser.id}"
     data-user-role="${sessionScope.currentUser.role}"
     data-welcome="Xin chào ${sessionScope.currentUser.fullName}! Tôi là trợ lý AI của LeaveFlow. Bạn có thể hỏi tôi về đơn gần nhất, mã đơn, số ngày nghỉ, trạng thái hoặc bước nên làm tiếp theo.">
    <button id="chatbotToggle"
            class="chatbot-toggle"
            type="button"
            aria-controls="chatbotPanel"
            aria-expanded="false">
        Trợ lý AI
    </button>

    <section id="chatbotPanel" class="chatbot-panel" aria-label="Trợ lý AI quản lý nghỉ phép" hidden>
        <div class="chatbot-panel-header">
            <div class="chatbot-header-main">
                <span class="chatbot-ai-avatar" aria-hidden="true">AI</span>
                <div class="chatbot-title">
                    <strong>Trợ lý LeaveFlow</strong>
                    <span class="chatbot-online">Sẵn sàng hỗ trợ</span>
                </div>
            </div>
            <div class="chatbot-header-actions">
                <button id="chatbotClear"
                        class="chatbot-clear"
                        type="button"
                        title="Xóa hội thoại"
                        aria-label="Xóa hội thoại">↻</button>
                <button id="chatbotClose" class="chatbot-close" type="button" aria-label="Đóng trợ lý">×</button>
            </div>
        </div>

        <div id="chatbotMessages" class="chatbot-messages" aria-live="polite"></div>

        <div class="chatbot-quick-actions" aria-label="Câu hỏi gợi ý">
            <c:choose>
                <c:when test="${isAdmin}">
                    <button type="button" data-chat-message="Có bao nhiêu đơn đang chờ duyệt?">Đơn chờ duyệt</button>
                    <button type="button" data-chat-message="Đơn gần nhất trên hệ thống">Đơn mới nhất</button>
                    <button type="button" data-chat-message="Tổng ngày nghỉ đã duyệt trong năm nay">Ngày nghỉ đã duyệt</button>
                    <button type="button" data-chat-message="Tôi nên ưu tiên việc gì?">Gợi ý ưu tiên</button>
                </c:when>
                <c:otherwise>
                    <button type="button" data-chat-message="Đơn gần nhất của tôi thế nào?">Đơn gần nhất</button>
                    <button type="button" data-chat-message="Tôi đã nghỉ bao nhiêu ngày trong năm nay?">Ngày nghỉ đã dùng</button>
                    <button type="button" data-chat-message="Thống kê đơn của tôi">Thống kê đơn</button>
                    <button type="button" data-chat-message="Tôi nên làm gì tiếp theo?">Gợi ý tiếp theo</button>
                </c:otherwise>
            </c:choose>
        </div>

        <form id="chatbotForm" class="chatbot-form">
            <label class="sr-only" for="chatbotInput">Nhập câu hỏi</label>
            <input id="chatbotInput"
                   type="text"
                   name="message"
                   maxlength="500"
                   autocomplete="off"
                   placeholder="Hỏi về đơn #12, trạng thái, số ngày..."
                   required>
            <button type="submit" aria-label="Gửi câu hỏi">Gửi</button>
        </form>
    </section>
</div>

<script src="${pageContext.request.contextPath}/assets/js/chatbot.js" defer></script>
