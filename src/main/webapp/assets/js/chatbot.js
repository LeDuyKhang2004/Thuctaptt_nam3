(function () {
    "use strict";

    document.addEventListener("DOMContentLoaded", function () {
        const widget = document.getElementById("chatbotWidget");
        if (!widget) {
            return;
        }

        const endpoint = widget.dataset.endpoint;
        const userId = widget.dataset.userId || "guest";
        const storageKey = "leaveflow-ai-chat:" + userId;
        const welcomeMessage = widget.dataset.welcome
                || "Xin chào! Tôi là trợ lý AI của LeaveFlow. Tôi có thể giúp bạn tra cứu và quản lý đơn nghỉ phép.";
        const toggle = document.getElementById("chatbotToggle");
        const close = document.getElementById("chatbotClose");
        const clear = document.getElementById("chatbotClear");
        const panel = document.getElementById("chatbotPanel");
        const form = document.getElementById("chatbotForm");
        const input = document.getElementById("chatbotInput");
        const messages = document.getElementById("chatbotMessages");
        const submitButton = form.querySelector("button[type='submit']");
        const quickActionButtons = Array.from(widget.querySelectorAll("[data-chat-message]"));
        let conversation = readConversation();
        let typingIndicator = null;

        function readConversation() {
            try {
                const stored = JSON.parse(sessionStorage.getItem(storageKey) || "[]");
                return Array.isArray(stored)
                        ? stored.filter(function (item) {
                            return item && (item.role === "user" || item.role === "assistant")
                                    && typeof item.content === "string";
                        }).slice(-30)
                        : [];
            } catch (error) {
                return [];
            }
        }

        function saveConversation() {
            try {
                sessionStorage.setItem(storageKey, JSON.stringify(conversation.slice(-30)));
            } catch (error) {
                // Trợ lý vẫn hoạt động nếu trình duyệt không cho phép lưu sessionStorage.
            }
        }

        function scrollToLatest() {
            messages.scrollTop = messages.scrollHeight;
        }

        function setPanelOpen(open) {
            panel.hidden = !open;
            toggle.setAttribute("aria-expanded", String(open));
            if (open) {
                input.focus();
                scrollToLatest();
            }
        }

        function appendMessage(content, fromUser, persist) {
            const message = document.createElement("div");
            message.className = "chatbot-message "
                    + (fromUser ? "chatbot-message-user" : "chatbot-message-bot");
            message.textContent = content;
            messages.appendChild(message);

            if (persist !== false) {
                conversation.push({
                    role: fromUser ? "user" : "assistant",
                    content: content
                });
                conversation = conversation.slice(-30);
                saveConversation();
            }
            scrollToLatest();
        }

        function showTyping() {
            removeTyping();
            typingIndicator = document.createElement("div");
            typingIndicator.className = "chatbot-message chatbot-message-bot chatbot-typing";
            typingIndicator.setAttribute("aria-label", "Trợ lý đang trả lời");
            typingIndicator.innerHTML = "<span></span><span></span><span></span>";
            messages.appendChild(typingIndicator);
            scrollToLatest();
        }

        function removeTyping() {
            if (typingIndicator) {
                typingIndicator.remove();
                typingIndicator = null;
            }
        }

        function setBusy(busy) {
            input.disabled = busy;
            submitButton.disabled = busy;
            quickActionButtons.forEach(function (button) {
                button.disabled = busy;
            });
            submitButton.textContent = busy ? "Đang gửi" : "Gửi";
        }

        function resetConversation() {
            conversation = [];
            try {
                sessionStorage.removeItem(storageKey);
            } catch (error) {
                // Không cần dừng thao tác xóa nếu bộ nhớ trình duyệt bị chặn.
            }
            messages.replaceChildren();
            appendMessage(welcomeMessage, false);
            input.focus();
        }

        async function sendMessage(rawMessage) {
            const message = rawMessage.trim();
            if (!message || submitButton.disabled) {
                return;
            }

            appendMessage(message, true);
            input.value = "";
            setBusy(true);
            showTyping();

            try {
                const response = await fetch(endpoint, {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                    body: new URLSearchParams({message: message})
                });
                const responseText = await response.text();

                if (!response.ok) {
                    throw new Error(responseText || "Không thể nhận phản hồi từ trợ lý.");
                }

                removeTyping();
                appendMessage(responseText, false);
            } catch (error) {
                removeTyping();
                appendMessage(error.message || "Đã có lỗi xảy ra. Vui lòng thử lại.", false);
            } finally {
                setBusy(false);
                input.focus();
            }
        }

        if (conversation.length > 0) {
            conversation.forEach(function (item) {
                appendMessage(item.content, item.role === "user", false);
            });
        } else {
            appendMessage(welcomeMessage, false);
        }

        toggle.addEventListener("click", function () {
            setPanelOpen(panel.hidden);
        });

        close.addEventListener("click", function () {
            setPanelOpen(false);
            toggle.focus();
        });

        clear.addEventListener("click", resetConversation);

        form.addEventListener("submit", function (event) {
            event.preventDefault();
            sendMessage(input.value);
        });

        quickActionButtons.forEach(function (button) {
            button.addEventListener("click", function () {
                sendMessage(button.dataset.chatMessage);
            });
        });

        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape" && !panel.hidden) {
                setPanelOpen(false);
                toggle.focus();
            }
        });
    });
})();
