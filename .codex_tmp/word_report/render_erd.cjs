const sharp = require("sharp");

const source = "D:/Study/VKU/Nam2HK2/JavaFramework/Ontap/leave_management/.codex_tmp/word_report/So_do_quan_he_LeaveFlow.svg";
const output = "D:/SO_DO_QUAN_HE_DATABASE_LEAVEFLOW.png";

sharp(source, { density: 180 })
  .png({ compressionLevel: 9, quality: 100 })
  .resize({ width: 2400 })
  .toFile(output)
  .then(info => console.log(JSON.stringify(info)))
  .catch(error => {
    console.error(error);
    process.exitCode = 1;
  });
