from docx import Document
from docx.shared import Cm

source = r"D:\BAOCAODACS3_LEDUYKHANG_24IT105_LEAVEFLOW.docx"
output = r"D:\BAOCAODACS3_LEDUYKHANG_24IT105_LEAVEFLOW_CAN_GIUA.docx"

doc = Document(source)

# Keep the cover section unchanged. The report sections originally use
# 3 cm left and 2 cm right margins; changing both to 2.5 cm preserves the
# usable width while centering the complete text block on the A4 page.
for section in doc.sections[1:]:
    section.left_margin = Cm(2.5)
    section.right_margin = Cm(2.5)

doc.save(output)
print(output)
