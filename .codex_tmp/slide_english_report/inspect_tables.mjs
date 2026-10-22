import fs from "node:fs/promises";
import { FileBlob, PresentationFile } from "@oai/artifact-tool";

const sourcePptx = "D:/Slide_DACS3_LeDuyKhang.pptx";
const outputPath = "D:/Study/VKU/Nam2HK2/JavaFramework/Ontap/leave_management/.codex_tmp/slide_english_report/table-details.json";
const presentation = await PresentationFile.importPptx(await FileBlob.load(sourcePptx));
const tableIds = ["tb/rmxovy5s", "tb/dofqx8ny", "tb/ql47mto7", "tb/sn6po36d"];
const tables = [];

for (const id of tableIds) {
    const table = presentation.resolve(id);
    const rows = [];
    for (let row = 0; row < 7; row += 1) {
        const cells = [];
        for (let column = 0; column < 3; column += 1) {
            const cell = table.getCell(row, column);
            cells.push({
                row,
                column,
                value: cell.value ?? null,
                text: cell.text?.toString?.() ?? null,
                proto: cell.toProto?.() ?? null
            });
        }
        rows.push(cells);
    }
    tables.push({ id, proto: table.toProto?.() ?? null, rows });
}

await fs.writeFile(outputPath, JSON.stringify(tables, null, 2), "utf8");
console.log(outputPath);
