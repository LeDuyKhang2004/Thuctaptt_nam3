import fs from "node:fs/promises";
import { FileBlob, PresentationFile } from "@oai/artifact-tool";

const buildDir = "D:/Study/VKU/Nam2HK2/JavaFramework/Ontap/leave_management/.codex_tmp/slide_english_report";
const starterPptx = `${buildDir}/template-starter.pptx`;
const finalPptx = "D:/Slide_DACS3_LeDuyKhang_English.pptx";
const previewDir = `${buildDir}/final-preview`;
const layoutDir = `${buildDir}/final-layout/final`;
const pencilAsset = `${buildDir}/template-inspect/assets/ppt/media/image15.png`;

async function writeBlob(path, blob) {
    await fs.writeFile(path, new Uint8Array(await blob.arrayBuffer()));
}

const presentation = await PresentationFile.importPptx(await FileBlob.load(starterPptx));
const beforeInspect = await presentation.inspect({
    kind: "slide,textbox,shape,image,table",
    include: "id,slide,name,text",
    maxChars: 250000
});
const beforeRecords = beforeInspect.ndjson
    .split(/\r?\n/)
    .filter((line) => line.trim())
    .map((line) => JSON.parse(line));

function findTarget(kind, slideNumber, name) {
    const record = beforeRecords.find((item) => item.kind === kind
        && item.slide === slideNumber
        && item.name === name);
    if (!record) {
        throw new Error(`Cannot find ${kind} "${name}" on slide ${slideNumber}.`);
    }
    return presentation.resolve(record.id);
}

function rewrite(slideNumber, name, oldText, newText) {
    const target = findTarget("textbox", slideNumber, name);
    target.text.replace(oldText, newText);
}

function setText(slideNumber, name, newText) {
    const target = findTarget("textbox", slideNumber, name);
    target.text = newText;
}

function setTechnologyTable(name, heading, layer, stack, responsibilities) {
    const table = findTarget("table", 5, name);
    const values = [
        [heading, heading, heading],
        [layer, layer, layer],
        ["", stack, ""],
        ["", responsibilities, ""],
        ["", "", ""],
        ["", "", ""],
        ["", "", ""]
    ];

    for (let row = 0; row < values.length; row += 1) {
        for (let column = 0; column < values[row].length; column += 1) {
            table.cells.set(row, column, values[row][column]);
        }
    }
}

// Slide 1 — title
rewrite(1, "TextBox 9", "Students: \nLe Duy Khang - 24IT105", "Student:\nLe Duy Khang - 24IT105");
rewrite(1, "TextBox 15", "MiniNote\nAndroid Note Management Application for Students", "LeaveFlow\nWeb-Based Leave Management System with an AI Assistant");
rewrite(1, "TextBox 17", "Supervisor: MSc. Nguyen Dang Y \nFaculty: Computer Science", "Supervisor: TS. Nguyễn Văn Bình\nFaculty: Computer Science");
setText(1, "TextBox 9", "Student:\nLe Duy Khang - 24IT105");
setText(1, "TextBox 17", "Supervisor: TS. Nguyễn Văn Bình\nFaculty: Computer Science");

// Slide 2 — outline
rewrite(2, "TextBox 8", "Contents", "Presentation Outline");
rewrite(2, "TextBox 17", "Motivation & Objectives", "Motivation & Objectives");
rewrite(2, "TextBox 18", "Technologies Used", "Technology & Architecture");
rewrite(2, "TextBox 19", "System Analysis -Use Case", "Roles & Core Workflow");
rewrite(2, "TextBox 24", "Database Design", "Data Model & Security");
rewrite(2, "TextBox 25", "Key Features & Demo", "Key Features & Demo");
rewrite(2, "TextBox 37", "Conclusion & Future Work", "Results & Future Work");

// Slide 3 — motivation
rewrite(3, "TextBox 8", "Motivation", "Why LeaveFlow Matters");
rewrite(3, "TextBox 24", "Difficulty in managing study notes.", "Manual leave tracking creates delays.");
rewrite(3, "TextBox 28", "Need for better focus", "Employees need clear status updates.");
rewrite(3, "TextBox 32", "Apply acquired knowledge.", "Admins need one approval workspace.");
rewrite(3, "TextBox 36", "Need for effective revision", "Users expect instant, reliable guidance.");
setText(3, "TextBox 36", "Role-aware AI guidance.");

// Slide 4 — objectives
rewrite(4, "TextBox 8", "Objectives", "Project Objectives");
rewrite(4, "TextBox 27", "Manage study folders\nCreate, edit, delete, and view notes", "Employee workflow\nSubmit, cancel, and track leave requests");
rewrite(4, "TextBox 28", "Create and review flashcards\nSupport focused study sessions with Pomodoro", "Admin control\nReview decisions and manage employee roles");
rewrite(4, "TextBox 29", "Store data using PHP API and MySQL\nBuild a simple, clean, and user-friendly interface", "Secure experience\nProtect accounts, data scope, and AI assistance");
setText(4, "TextBox 27", "EMPLOYEE WORKFLOW\nSubmit, cancel, and track leave requests");
setText(4, "TextBox 28", "ADMIN CONTROL\nReview decisions and manage employee roles");
setText(4, "TextBox 29", "SECURE EXPERIENCE\nProtect accounts, data scope, and AI assistance");

// Slide 5 — technology stack
rewrite(5, "TextBox 8", "Technologies Used", "Technology Stack");
rewrite(5, "TextBox 13", "Easy to change colors, photos and Text. You can simply impress your audience and add a unique zing and appeal to your Presentations. ", "");
setTechnologyTable(
    "Table 9",
    "Presentation",
    "JSP + CSS",
    "JSP\nJSTL\nHTML5 / CSS3\nJavaScript",
    "Responsive pages, role-aware views, and AI chat widget"
);
setTechnologyTable(
    "Table 11",
    "Application",
    "Spring MVC",
    "Java 17\nSpring MVC 6.1\nControllers\nServices",
    "Routing and validation, business rules, and session auth"
);
setTechnologyTable(
    "Table 10",
    "Persistence",
    "JPA + MySQL",
    "Spring Data JPA\nHibernate 6.5\nMySQL 8",
    "Entity mapping, repository queries, and transactional updates"
);
setTechnologyTable(
    "Table 18",
    "Delivery",
    "Maven + Tomcat",
    "Maven WAR\nApache Tomcat\nJUnit 5",
    "Packaging, automated tests, and Tomcat runtime"
);

// Slide 6 — roles and workflow
rewrite(6, "TextBox 8", "System Analysis -Use Case", "Roles & Core Workflow");
rewrite(6, "TextBox 13", "Use Case Diagram", "Role-Based Workflow");
rewrite(6, "TextBox 14", "Main Actor: User / Student", "System Actors: Employee and Administrator");
rewrite(6, "TextBox 14", "The user can perform the following functions:", "The request lifecycle has three controlled stages:");
rewrite(6, "TextBox 14", "Manage study folders.", "Employee submits a leave request.");
rewrite(6, "TextBox 14", "Add, edit, delete, and search folders.", "The system stores it as PENDING.");
rewrite(6, "TextBox 14", "Manage notes inside each folder.", "Admin reviews the reason and leave dates.");
rewrite(6, "TextBox 14", "Add, edit, and delete notes.Manage flashcards by folder.", "Admin selects APPROVED or REJECTED.");
rewrite(6, "TextBox 14", "Flip cards and move between flashcards.", "Employee tracks the result and admin note.");
rewrite(6, "TextBox 14", "Use Pomodoro timer for focused study.", "Each role sees only authorized data.");
rewrite(6, "TextBox 14", "View basic profile and application information", "The AI assistant provides role-aware guidance.");
setText(6, "TextBox 8", "Roles & Core Workflow");
setText(6, "TextBox 13", "Role Workflow");

const roleImage = findTarget("image", 6, "Picture 14");
const roleFrame = roleImage.frame;
const roleCrop = roleImage.crop;
const roleGeometry = roleImage.geometry;
const roleBorderRadius = roleImage.borderRadius;
const roleRotation = roleImage.rotation;
const roleFlipHorizontal = roleImage.flipHorizontal;
const roleFlipVertical = roleImage.flipVertical;
const roleLockAspectRatio = roleImage.lockAspectRatio;
const pencilBytes = await fs.readFile(pencilAsset);
roleImage.replace({
    blob: new Uint8Array(pencilBytes),
    contentType: "image/png",
    alt: "Pencil and lightbulb symbol from the source presentation",
    fit: "contain"
});
roleImage.frame = roleFrame;
roleImage.crop = roleCrop;
roleImage.geometry = roleGeometry;
roleImage.borderRadius = roleBorderRadius;
roleImage.rotation = roleRotation;
roleImage.flipHorizontal = roleFlipHorizontal;
roleImage.flipVertical = roleFlipVertical;
roleImage.lockAspectRatio = roleLockAspectRatio;

// Slide 7 — database design
rewrite(7, "TextBox 8", "Database Design", "Database Design");
rewrite(7, "TextBox 12", "The system uses MySQL database with three main tables:", "The data model makes ownership, status, and review history explicit.");
rewrite(7, "TextBox 9", "folders: Stores study folder information", "1. users: Stores account and role data");
rewrite(7, "TextBox 9", "name", "username");
rewrite(7, "TextBox 9", "note_count", "password / email");
rewrite(7, "TextBox 9", "created_at", "full_name / role");
rewrite(7, "TextBox 10", "notes: Stores notes belonging to each folder.", "2. leave_requests: Stores employee requests.");
rewrite(7, "TextBox 10", "folder_id", "user_id");
rewrite(7, "TextBox 10", "title ", "reason");
rewrite(7, "TextBox 10", "content", "start_date / end_date");
rewrite(7, "TextBox 10", "created_date", "status");
rewrite(7, "TextBox 14", "3. flashcard: Stores flashcards belonging to each folder.", "3. review: Records decisions and history.");
rewrite(7, "TextBox 14", "id\nfolder_id\nquestion \nanswer\ncreated_at", "admin_note\ncreated_at\nstatus\nPENDING\nAPPROVED / REJECTED");
setText(7, "TextBox 9", "users: Account and role data\nFields: id; username; password/email; full_name/role");
setText(7, "TextBox 10", "leave_requests: Employee requests\nFields: id; user_id; reason; start_date; end_date; status");
setText(7, "TextBox 14", "3. review: Decision history\nFields: admin_note; created_at; status; PENDING; APPROVED/REJECTED");

// Slide 8 — key features
rewrite(8, "TextBox 10", "Key features", "Key Features");
rewrite(8, "TextBox 17", "Folder Management:", "Employee Workspace:");
rewrite(8, "TextBox 17", "Display study folders.", "Register and sign in.");
rewrite(8, "TextBox 17", "Add new folders.", "Create leave requests.");
rewrite(8, "TextBox 17", "Edit folder names.", "Cancel pending requests.");
rewrite(8, "TextBox 17", "Delete folders.", "Track status and admin notes.");
rewrite(8, "TextBox 17", "Search folders.\n", "");
rewrite(8, "TextBox 18", "Note Management", "Admin Workspace:");
rewrite(8, "TextBox 18", "Display notes by folder.", "View dashboard statistics.");
rewrite(8, "TextBox 18", "Add new notes.", "Filter and inspect requests.");
rewrite(8, "TextBox 18", "Edit note content.", "Approve or reject requests.");
rewrite(8, "TextBox 18", "Delete notes", "Manage employee roles.");
rewrite(8, "TextBox 19", "Flashcard & Pomodoro:", "Role-Aware AI Assistant:");
rewrite(8, "TextBox 19", "Create and review flashcards.", "Query a request by ID.");
rewrite(8, "TextBox 19", "Flip cards to view answers.", "Calculate approved leave days.");
rewrite(8, "TextBox 19", "Choose Pomodoro study sets.", "Suggest the next action.");
rewrite(8, "TextBox 19", "Start, pause, and reset study timer.", "Keep conversations across pages.");
setText(8, "TextBox 17", "Employee Workspace:\nRegister and sign in.\nCreate leave requests.\nCancel pending requests.\nTrack status and admin notes.");
setText(8, "TextBox 18", "Admin Workspace:\nView dashboard statistics.\nFilter and inspect requests.\nApprove or reject requests.\nManage employee roles.");
setText(8, "TextBox 19", "Role-Aware AI Assistant:\nQuery a request by ID.\nCalculate approved leave days.\nSuggest the next action.\nKeep conversations across pages.");

// Slide 9 — demo divider
rewrite(9, "TextBox 14", "Demo", "Live Demonstration");

// Slide 10 — results and future work
rewrite(10, "TextBox 12", "Conclusion & Future Work", "Results & Future Work");
rewrite(10, "TextBox 26", "Results", "Implemented Results");
rewrite(10, "TextBox 30", "Future Improvements", "Future Improvements");
rewrite(10, "TextBox 23", "The application runs successfully on Android Emulator.", "Complete employee and admin workflows.");
rewrite(10, "TextBox 24", "Folder, note, and flashcard management functions work correctly.", "Role-based access and PBKDF2 password protection.");
rewrite(10, "Freeform 15", "The Android app can connect to PHP API and MySQL.", "Context-aware AI assistant with 12 passing tests.");
rewrite(10, "TextBox 21", "Add login and registration.", "Add annual leave quotas and policy rules.");
rewrite(10, "TextBox 22", "Deploy API and database to an online server.", "Deploy with HTTPS, monitoring, and managed MySQL.");
rewrite(10, "TextBox 45", "Add cloud backup and synchronization.", "Integrate an optional LLM with access control.");
rewrite(10, "TextBox 25", "Add Contents Title", "");
setText(10, "TextBox 26", "Delivered");
setText(10, "TextBox 25", "");

// Slide 11 — conclusion
rewrite(
    11,
    "TextBox 6",
    "MiniNote is an Android application designed to support students in managing study notes, reviewing knowledge with flashcards, and improving concentration with the Pomodoro technique.",
    "LeaveFlow centralizes leave requests, approval decisions, and status tracking in one clear web workflow."
);
rewrite(
    11,
    "TextBox 6",
    "Through this project, I learned how to build an Android application using Kotlin and XML, connect it with PHP API through Retrofit, and store data in MySQL.",
    "The project applies Java 17, Spring MVC, JPA, Hibernate, JSP, and MySQL in a layered architecture packaged for Tomcat."
);
rewrite(
    11,
    "TextBox 6",
    "Although the application still has some limitations, it has completed the main goals of the project and can be further improved in the future.",
    "Role-based data access, PBKDF2 passwords, automated tests, and the AI assistant provide a strong foundation for future deployment."
);

// Slide 12 — closing
rewrite(12, "TextBox 8", "Thank you", "Thank You — Questions?");
setText(12, "TextBox 8", "Thank You — Questions?");

const slideNotes = [
    "[Sources]\n- User-provided reference deck and internal LeaveFlow project metadata.",
    "[Sources]\n- Narrative synthesized from the internal LeaveFlow project scope.",
    "[Sources]\n- Internal LeaveFlow controllers, services, and current workflow design.",
    "[Sources]\n- Internal LeaveFlow functional requirements and implemented controllers.",
    "[Sources]\n- Internal pom.xml and Spring MVC configuration.",
    "[Sources]\n- Internal Employee/Admin controllers and leave-request service logic.",
    "[Sources]\n- Internal UserEntity.java and LeaveRequestEntity.java.",
    "[Sources]\n- Internal LeaveFlow controllers, ChatbotService.java, and UI components.",
    "[Sources]\n- Demonstration uses the locally implemented LeaveFlow application.",
    "[Sources]\n- Internal implementation status and Maven test results: 12 tests passing.",
    "[Sources]\n- Synthesis of the internal LeaveFlow architecture and implemented capabilities.",
    "[Sources]\n- User-provided reference deck visual assets."
];

for (let index = 0; index < presentation.slides.items.length; index += 1) {
    const slide = presentation.slides.items[index];
    slide.speakerNotes.textFrame.setText(slideNotes[index]);
    slide.speakerNotes.setVisible(true);
}

await fs.mkdir(previewDir, { recursive: true });
await fs.mkdir(layoutDir, { recursive: true });

for (let index = 0; index < presentation.slides.items.length; index += 1) {
    const slide = presentation.slides.items[index];
    const stem = `slide-${String(index + 1).padStart(2, "0")}`;
    await writeBlob(`${previewDir}/${stem}.png`, await presentation.export({ slide, format: "png", scale: 1 }));
    const layout = await slide.export({ format: "layout" });
    await fs.writeFile(`${layoutDir}/${stem}.layout.json`, await layout.text(), "utf8");
}

await writeBlob(
    `${buildDir}/final-montage.webp`,
    await presentation.export({ format: "webp", montage: true, scale: 1 })
);

const finalInspect = await presentation.inspect({
    kind: "deck,slide,textbox,shape,image,table,chart,notes,layout",
    include: "id,slide,name,title,text,textPreview,textChars,textLines,bbox,bboxUnit,rows,cols,preview,isPlaceholder,placeholders",
    maxChars: 250000
});
await fs.writeFile(`${buildDir}/final-inspect.ndjson`, finalInspect.ndjson, "utf8");

const pptx = await PresentationFile.exportPptx(presentation);
await pptx.save(finalPptx);

console.log(JSON.stringify({ finalPptx, slideCount: presentation.slides.items.length }));
