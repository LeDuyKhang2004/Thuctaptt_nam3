import sys
from docx import Document
from docx.oxml.ns import qn

sys.stdout.reconfigure(encoding="utf-8")
doc = Document(r"D:\BAOCAODACS3_LEDUYKHANG_24IT105_HOANTHIEN.docx")

def attrs(p):
    ppr = p._p.pPr
    if ppr is None:
        return ""
    out = []
    for child in ppr:
        tag = child.tag.rsplit("}", 1)[-1]
        vals = {k.rsplit("}", 1)[-1]: v for k, v in child.attrib.items()}
        out.append(f"{tag}{vals}")
    return "; ".join(out)

for i in range(222, 245):
    p = doc.paragraphs[i]
    print(f"{i}: {p.style.name!r} {p.text!r} | {attrs(p)}")
