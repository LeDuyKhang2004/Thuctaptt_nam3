package com.javaweb.service;

import com.javaweb.entity.LeaveRequestEntity;
import com.javaweb.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatbotService {

    private static final String PENDING = "PENDING";
    private static final String APPROVED = "APPROVED";
    private static final String REJECTED = "REJECTED";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Pattern LEAVE_ID_PATTERN = Pattern.compile(
            "(?:ma\\s+don|don)(?:\\s+so)?\\s+(\\d+)|id\\s+(\\d+)"
    );

    private final LeaveRequestService leaveRequestService;
    private final LeavePolicyService policy;

    @Autowired
    public ChatbotService(LeaveRequestService leaveRequestService, LeavePolicyService policy) {
        this.leaveRequestService = leaveRequestService;this.policy=policy;
    }
    // Preserve constructor used by existing unit tests.
    public ChatbotService(LeaveRequestService leaveRequestService){this(leaveRequestService,null);}


    public String reply(String message, UserEntity currentUser) {
        if (currentUser == null) {
            return "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại để sử dụng trợ lý AI.";
        }

        String text = normalize(message);
        if (text.isBlank()) {
            return "Bạn hãy nhập câu hỏi. Ví dụ: \"Đơn gần nhất của tôi thế nào?\" hoặc \"Kiểm tra đơn số 12\".";
        }

        if(containsAny(text,"phep con lai","con bao nhieu ngay phep","so du phep","han muc phep","con lai may ngay")){
            if(policy==null)return "Bạn có thể xem phép còn lại tại mục Số ngày phép của tôi.";
            int year=LocalDate.now().getYear();
            return "Năm "+year+": bạn còn "+policy.remaining(currentUser,year)+" ngày phép năm khả dụng (đã trừ đơn chờ duyệt).";
        }
        if(containsAny(text,"ngay le","nghi le","lich nghi")){
            if(policy==null)return "Xem Lịch nghỉ để biết các đơn đã duyệt.";
            StringBuilder b=new StringBuilder("Ngày nghỉ lễ đã cấu hình:\n");
            policy.holidays().stream().limit(20).forEach(h->b.append("- ").append(h.getDate()).append(": ").append(h.getName()).append("\n"));
            return b.toString();
        }
        Long leaveId = extractLeaveId(text);
        if (leaveId != null && containsAny(text, "don", "trang thai", "chi tiet", "kiem tra", "xem", "bao nhieu ngay")) {
            return answerLeaveById(leaveId, currentUser);
        }

        if (containsAny(text, "don gan nhat", "don moi nhat", "don cuoi cung", "yeu cau gan nhat")) {
            return answerLatestLeave(currentUser);
        }

        if (containsAny(text, "bao nhieu ngay", "tong ngay nghi", "so ngay nghi", "ngay phep da dung",
                "da nghi bao nhieu", "con bao nhieu ngay phep")) {
            return answerApprovedDays(text, currentUser);
        }

        if (containsAny(text, "bao nhieu don", "so luong don", "thong ke", "co may don", "dem don", "tong don")) {
            return answerLeaveCounts(text, currentUser);
        }

        if (containsAny(text, "xin chao", "chao ban", "hello", "hi chatbot", "hi ai")) {
            return "Xin chào " + displayName(currentUser) + "! Tôi là trợ lý AI của LeaveFlow.\n"
                    + "Tôi có thể tra cứu đơn theo mã, xem đơn gần nhất, tính ngày nghỉ, thống kê trạng thái và gợi ý bước tiếp theo.";
        }

        if (containsAny(text, "toi la ai", "tai khoan cua toi", "vai tro cua toi", "quyen cua toi")) {
            return "Bạn đang đăng nhập với tên " + displayName(currentUser) + " và vai trò "
                    + (isAdmin(currentUser) ? "Quản trị viên (ADMIN)." : "Nhân viên (EMPLOYEE).")
                    + (isAdmin(currentUser)
                    ? " Bạn có thể xem thống kê toàn hệ thống, quản lý nhân viên và xử lý đơn."
                    : " Dữ liệu tôi tra cứu sẽ chỉ giới hạn trong các đơn của bạn.");
        }

        if (containsAny(text, "tao don", "gui don", "dang ky nghi", "xin nghi phep", "nop don")) {
            return answerCreateLeave(currentUser);
        }

        if (containsAny(text, "huy don", "xoa don", "rut don")) {
            return answerCancelLeave(currentUser);
        }

        if (containsAny(text, "cach duyet", "duyet don", "cach tu choi", "xu ly don", "phe duyet don")) {
            return answerApprovalWorkflow(currentUser);
        }

        if (containsAny(text, "quy trinh", "don di dau", "sau khi gui", "bao lau duyet", "cac buoc xu ly")) {
            return answerWorkflow();
        }

        if (containsAny(text, "trang thai", "pending", "approved", "rejected", "cho duyet", "da duyet",
                "duoc duyet", "tu choi", "bi tu choi", "khong duoc duyet")) {
            return answerStatusMeaning(text);
        }

        if (containsAny(text, "goi y", "nen lam gi", "thao tac", "tiep theo", "uu tien")) {
            return answerSuggestedAction(currentUser);
        }

        if (containsAny(text, "don cua toi", "xem don", "chi tiet don", "ghi chu admin", "ly do tu choi")) {
            return "Bạn chọn \"Đơn của tôi\" để xem danh sách, sau đó nhấn \"Xem\" tại đơn cần kiểm tra.\n"
                    + "Bạn cũng có thể hỏi trực tiếp: \"Kiểm tra đơn số 12\" hoặc \"Đơn gần nhất của tôi\".";
        }

        if (containsAny(text, "chinh sach", "quy dinh nghi", "phep nam", "han muc", "so ngay phep con lai")) {
            return "Xem mục Số ngày phép của tôi để biết hạn mức và số dư hiện tại. Cuối tuần và ngày lễ không tính là ngày làm việc.";
        }

        if (containsAny(text, "giup do", "tro giup", "huong dan", "ban lam duoc gi", "chatbot", "tro ly ai")) {
            return helpMessage(currentUser);
        }

        return "Tôi chưa xác định được yêu cầu. Bạn có thể thử một trong các câu sau:\n"
                + "- \"Đơn gần nhất của tôi thế nào?\"\n"
                + "- \"Kiểm tra đơn số 12\"\n"
                + "- \"Tôi đã nghỉ bao nhiêu ngày?\"\n"
                + "- \"Tôi nên làm gì tiếp theo?\"";
    }

    private String answerLeaveById(Long leaveId, UserEntity user) {
        LeaveRequestEntity leave = leaveRequestService.findById(leaveId);
        if (leave == null) {
            return "Không tìm thấy đơn #" + leaveId + ". Bạn hãy kiểm tra lại mã đơn trong danh sách nghỉ phép.";
        }

        if (!isAdmin(user) && !belongsTo(leave, user)) {
            return "Tôi không thể hiển thị đơn #" + leaveId + " vì đơn này không thuộc tài khoản của bạn.";
        }

        return formatLeaveSummary(leave, isAdmin(user));
    }

    private String answerLatestLeave(UserEntity user) {
        List<LeaveRequestEntity> leaves = scopedLeaves(user);
        LeaveRequestEntity latest = leaves.stream()
                .max(Comparator
                        .comparing(LeaveRequestEntity::getCreatedAt,
                                Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparing(LeaveRequestEntity::getId,
                                Comparator.nullsFirst(Comparator.naturalOrder())))
                .orElse(null);

        if (latest == null) {
            return isAdmin(user)
                    ? "Hệ thống chưa có đơn nghỉ phép nào."
                    : "Bạn chưa có đơn nghỉ phép. Chọn \"Tạo đơn nghỉ phép\" nếu muốn gửi yêu cầu mới.";
        }

        return "Đơn gần nhất " + (isAdmin(user) ? "trên hệ thống" : "của bạn") + ":\n"
                + formatLeaveSummary(latest, isAdmin(user));
    }

    private String answerApprovedDays(String text, UserEntity user) {
        boolean currentYearOnly = containsAny(
                text,
                "nam nay",
                "trong nam",
                String.valueOf(LocalDate.now().getYear())
        );
        List<LeaveRequestEntity> approvedLeaves = scopedLeaves(user).stream()
                .filter(leave -> APPROVED.equalsIgnoreCase(safe(leave.getStatus())))
                .filter(leave -> !currentYearOnly
                        || (leave.getStartDate() != null && leave.getStartDate().getYear() == LocalDate.now().getYear()))
                .toList();

        long days = approvedLeaves.stream().mapToLong(this::leaveDuration).sum();
        String scope = isAdmin(user) ? "toàn hệ thống" : "của bạn";
        String period = currentYearOnly ? " trong năm nay" : "";
        String answer = "Các đơn đã duyệt " + scope + period + " gồm " + approvedLeaves.size()
                + " đơn, tương ứng " + days + " ngày nghỉ.";

        if (containsAny(text, "con bao nhieu", "con lai", "han muc")) {
            answer += " Hệ thống chưa cấu hình hạn mức phép năm nên tôi chưa thể tính số ngày còn lại.";
        }

        return answer;
    }

    private String answerLeaveCounts(String text, UserEntity user) {
        LeaveCounts counts = getCounts(user);
        String scope = isAdmin(user) ? "trên toàn hệ thống" : "của bạn";

        if (containsAny(text, "pending", "cho duyet", "dang cho")) {
            return "Hiện có " + counts.pending() + " đơn " + scope + " đang chờ duyệt (PENDING).";
        }
        if (containsAny(text, "rejected", "tu choi", "bi tu choi", "khong duoc duyet")) {
            return "Hiện có " + counts.rejected() + " đơn " + scope + " đã bị từ chối (REJECTED).";
        }
        if (containsAny(text, "approved", "da duyet", "duoc duyet")) {
            return "Hiện có " + counts.approved() + " đơn " + scope + " đã được duyệt (APPROVED).";
        }

        return "Thống kê " + scope + ":\n"
                + "- Tổng cộng: " + counts.total() + " đơn\n"
                + "- Chờ duyệt: " + counts.pending() + "\n"
                + "- Đã duyệt: " + counts.approved() + "\n"
                + "- Từ chối: " + counts.rejected();
    }

    private String answerCreateLeave(UserEntity user) {
        if (isAdmin(user)) {
            return "Tài khoản Admin tập trung vào quản lý và xử lý đơn. Để tạo đơn nghỉ phép, hãy dùng tài khoản nhân viên.";
        }

        return "Cách tạo đơn nghỉ phép:\n"
                + "1. Chọn \"Tạo đơn nghỉ phép\" ở thanh bên.\n"
                + "2. Nhập lý do ít nhất 5 ký tự.\n"
                + "3. Chọn ngày bắt đầu và ngày kết thúc.\n"
                + "4. Nhấn \"Gửi đơn nghỉ phép\".\n"
                + "Đơn mới sẽ ở trạng thái PENDING để chờ Admin xử lý.";
    }

    private String answerCancelLeave(UserEntity user) {
        if (isAdmin(user)) {
            return "Admin không hủy đơn thay nhân viên. Bạn có thể duyệt hoặc từ chối đơn đang PENDING trong mục \"Quản lý đơn nghỉ phép\".";
        }

        return "Bạn mở \"Đơn của tôi\" và chọn \"Hủy đơn\". Chỉ đơn PENDING của chính bạn mới có thể hủy.\n"
                + "Đơn APPROVED hoặc REJECTED đã được xử lý nên không thể hủy.";
    }

    private String answerApprovalWorkflow(UserEntity user) {
        if (!isAdmin(user)) {
            return "Chỉ Admin có quyền duyệt hoặc từ chối đơn. Bạn có thể theo dõi kết quả trong \"Đơn của tôi\".";
        }

        return "Cách xử lý đơn:\n"
                + "1. Mở \"Quản lý đơn nghỉ phép\".\n"
                + "2. Lọc trạng thái PENDING và xem chi tiết đơn.\n"
                + "3. Chọn \"Duyệt\" hoặc nhập lý do rồi chọn \"Từ chối\".\n"
                + "Chỉ đơn PENDING mới có thể được xử lý.";
    }

    private String answerWorkflow() {
        return "Quy trình xử lý đơn:\n"
                + "1. Nhân viên tạo đơn → PENDING.\n"
                + "2. Admin xem thông tin và quyết định.\n"
                + "3. Đơn chuyển thành APPROVED hoặc REJECTED.\n"
                + "4. Nhân viên xem kết quả và ghi chú trong trang chi tiết.\n"
                + "Thời gian duyệt phụ thuộc quy trình nội bộ của doanh nghiệp.";
    }

    private String answerStatusMeaning(String text) {
        if (containsAny(text, "pending", "cho duyet", "dang cho")) {
            return "PENDING (Chờ duyệt): đơn đã được gửi và đang chờ Admin xử lý. Nhân viên vẫn có thể hủy đơn ở trạng thái này.";
        }
        if (containsAny(text, "rejected", "tu choi", "bi tu choi", "khong duoc duyet")) {
            return "REJECTED (Từ chối): Admin không chấp nhận đơn. Hãy mở chi tiết đơn để xem ghi chú hoặc lý do từ chối.";
        }
        if (containsAny(text, "approved", "da duyet", "duoc duyet")) {
            return "APPROVED (Đã duyệt): Admin đã chấp nhận đơn nghỉ phép. Đơn không còn có thể hủy.";
        }

        return "Hệ thống có 3 trạng thái:\n"
                + "- PENDING: Chờ duyệt, nhân viên còn có thể hủy.\n"
                + "- APPROVED: Đã được Admin chấp nhận.\n"
                + "- REJECTED: Bị từ chối, nên xem ghi chú của Admin.";
    }

    private String answerSuggestedAction(UserEntity user) {
        LeaveCounts counts = getCounts(user);

        if (isAdmin(user)) {
            if (counts.pending() > 0) {
                return "Ưu tiên hiện tại: xử lý " + counts.pending() + " đơn đang chờ duyệt.\n"
                        + "Mở \"Quản lý đơn nghỉ phép\", lọc PENDING và kiểm tra các đơn cũ trước.";
            }
            return "Hiện không có đơn chờ duyệt. Bạn có thể xem Dashboard hoặc kiểm tra \"Danh sách nhân viên\" để quản lý quyền tài khoản.";
        }

        if (counts.total() == 0) {
            return "Bạn chưa có đơn nghỉ phép. Nếu cần nghỉ, hãy chọn \"Tạo đơn nghỉ phép\" để gửi yêu cầu mới.";
        }
        if (counts.pending() > 0) {
            return "Bạn có " + counts.pending() + " đơn đang chờ duyệt. Hãy theo dõi trong \"Đơn của tôi\"; nếu kế hoạch thay đổi, bạn vẫn có thể hủy đơn PENDING.";
        }
        if (counts.rejected() > 0) {
            return "Bạn có đơn bị từ chối. Hãy xem ghi chú của Admin; nếu vẫn cần nghỉ, bạn có thể tạo đơn mới với thông tin phù hợp hơn.";
        }
        return "Các đơn hiện tại của bạn đều đã được duyệt. Bạn có thể hỏi \"Tôi đã nghỉ bao nhiêu ngày?\" để xem tổng thời gian.";
    }

    private String formatLeaveSummary(LeaveRequestEntity leave, boolean includeEmployee) {
        StringBuilder answer = new StringBuilder();
        answer.append("Đơn #").append(leave.getId()).append(" - ").append(statusLabel(leave.getStatus()));

        if (includeEmployee && leave.getUser() != null) {
            answer.append("\nNhân viên: ").append(displayName(leave.getUser()));
        }

        answer.append("\nThời gian: ")
                .append(formatDate(leave.getStartDate()))
                .append(" → ")
                .append(formatDate(leave.getEndDate()))
                .append(" (").append(leaveDuration(leave)).append(" ngày)");

        if (!safe(leave.getReason()).isBlank()) {
            answer.append("\nLý do: ").append(shorten(leave.getReason(), 120));
        }
        if (!safe(leave.getAdminNote()).isBlank()) {
            answer.append("\nGhi chú Admin: ").append(shorten(leave.getAdminNote(), 120));
        }

        return answer.toString();
    }

    private List<LeaveRequestEntity> scopedLeaves(UserEntity user) {
        return isAdmin(user) ? leaveRequestService.findAll() : leaveRequestService.findByUser(user);
    }

    private LeaveCounts getCounts(UserEntity user) {
        if (isAdmin(user)) {
            return new LeaveCounts(
                    leaveRequestService.countAll(),
                    leaveRequestService.countPending(),
                    leaveRequestService.countApproved(),
                    leaveRequestService.countRejected()
            );
        }

        return new LeaveCounts(
                leaveRequestService.countByUser(user),
                leaveRequestService.countByUserAndStatus(user, PENDING),
                leaveRequestService.countByUserAndStatus(user, APPROVED),
                leaveRequestService.countByUserAndStatus(user, REJECTED)
        );
    }

    private boolean belongsTo(LeaveRequestEntity leave, UserEntity user) {
        return leave.getUser() != null
                && leave.getUser().getId() != null
                && leave.getUser().getId().equals(user.getId());
    }

    private long leaveDuration(LeaveRequestEntity leave) {
        if (leave.getStartDate() == null || leave.getEndDate() == null || leave.getEndDate().isBefore(leave.getStartDate())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
    }

    private Long extractLeaveId(String text) {
        Matcher matcher = LEAVE_ID_PATTERN.matcher(text);
        if (!matcher.find()) {
            return null;
        }

        String value = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String statusLabel(String status) {
        return switch (safe(status).toUpperCase(Locale.ROOT)) {
            case PENDING -> "PENDING (Chờ duyệt)";
            case APPROVED -> "APPROVED (Đã duyệt)";
            case REJECTED -> "REJECTED (Từ chối)";
            case "MANAGER_PENDING" -> "MANAGER_PENDING (Chờ trưởng phòng)";
            case "CANCELLED" -> "CANCELLED (Đã hủy)";
            default -> "Chưa xác định";
        };
    }

    private String formatDate(LocalDate date) {
        return date == null ? "Chưa có" : date.format(DATE_FORMAT);
    }

    private boolean isAdmin(UserEntity user) {
        return "ADMIN".equalsIgnoreCase(user.getRole() == null ? "" : user.getRole().trim());
    }

    private String displayName(UserEntity user) {
        String fullName = user.getFullName();
        return fullName == null || fullName.isBlank() ? safe(user.getUsername()) : fullName.trim();
    }

    private String helpMessage(UserEntity user) {
        return "Tôi có thể hỗ trợ:\n"
                + "- Tra cứu chi tiết theo mã đơn.\n"
                + "- Xem đơn gần nhất và ghi chú Admin.\n"
                + "- Thống kê đơn theo trạng thái.\n"
                + "- Tính tổng ngày nghỉ đã được duyệt.\n"
                + "- Hướng dẫn tạo, hủy và xử lý đơn.\n"
                + "- Giải thích quy trình và gợi ý bước tiếp theo.\n"
                + (isAdmin(user)
                ? "Dữ liệu của bạn bao gồm toàn hệ thống theo quyền Admin."
                : "Tôi chỉ hiển thị dữ liệu thuộc tài khoản của bạn.");
    }

    private String shorten(String value, int maxLength) {
        String cleaned = safe(value).trim().replaceAll("\\s+", " ");
        return cleaned.length() <= maxLength ? cleaned : cleaned.substring(0, maxLength - 1) + "…";
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replace("đ", "d")
                .replace("Đ", "D")
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private record LeaveCounts(long total, long pending, long approved, long rejected) {
    }
}
