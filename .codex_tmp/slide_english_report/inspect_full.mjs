import fs from "node:fs/promises";
import { FileBlob, PresentationFile } from "@oai/artifact-tool";

const sourcePptx = "D:/Slide_DACS3_LeDuyKhang.pptx";
const outputDir = "D:/Study/VKU/Nam2HK2/JavaFramework/Ontap/leave_management/.codex_tmp/slide_english_report";

const presentation = await PresentationFile.importPptx(await FileBlob.load(sourcePptx));
const snapshot = await presentation.inspect({
    kind: "deck,slide,textbox,shape,image,table,chart,notes,layout",
    include: "id,slide,name,title,text,textPreview,textChars,textLines,bbox,bboxUnit,rows,cols,preview,isPlaceholder,placeholders",
    maxChars: 250000
});

await fs.writeFile(`${outputDir}/full-inspect.ndjson`, snapshot.ndjson, "utf8");

const structure = {
    slideCount: presentation.slides.items.length,
    masters: presentation.masters.items.map((master) => ({
        id: master.id,
        name: master.name,
        placeholderSummary: master.placeholders?.summary?.() ?? null,
        elementCount: master.elements?.length ?? null,
        proto: master.toProto?.() ?? null
    })),
    layouts: presentation.layouts.items.map((layout) => ({
        id: layout.id,
        name: layout.name,
        parentLayoutId: layout.parentLayoutId ?? null,
        placeholderSummary: layout.placeholders?.summary?.() ?? null,
        proto: layout.toProto?.() ?? null
    }))
};

await fs.writeFile(`${outputDir}/source-structure.json`, JSON.stringify(structure, null, 2), "utf8");
console.log(JSON.stringify({ slideCount: structure.slideCount, masterCount: structure.masters.length, layoutCount: structure.layouts.length }));
