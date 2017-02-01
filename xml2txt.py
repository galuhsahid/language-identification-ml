import os
import xml.etree.ElementTree as ET
from lxml import etree
import nltk.data
import sys

#README
#python xml2txt.py <input-file>.xml <output-file>.txt

fin_name = sys.argv[1]
fout_name = sys.argv[2]

# Append root to file
with open(fin_name, 'rb') as f, open('CombinationNew.xml', 'wb') as g:
    g.write('<ROOT>{}</ROOT>'.format(f.read()).replace('\n', '').replace('\r', ''))

parser = etree.XMLParser(recover=True, encoding="utf-8", remove_blank_text=True)
tree = ET.parse('CombinationNew.xml', parser=parser)
root = tree.getroot()

# Strip unnecessary whitespaces
for elem in root.iter('*'):
    if elem.text is not None:
        elem.text = elem.text.strip()
    if elem.tail is not None:
        elem.tail = elem.tail.strip()

tree.write('CombinationNew.xml', encoding="utf-8")
sentence = ""

number = 1
for DOC in list(root):
    bahasa = DOC.find("BAHASA").text
    content = DOC.find("CONTENT").text

    # Separate content into sentences
    if bahasa == "Spanyol":
        tokenizer = nltk.data.load('tokenizers/punkt/spanish.pickle')
    elif bahasa == "Perancis":
        tokenizer = nltk.data.load('tokenizers/punkt/french.pickle')
    else:
        tokenizer = nltk.data.load('tokenizers/punkt/english.pickle')

    sentences = tokenizer.tokenize(content)

    length = len(sentences)
    for x in range (0, length):
        sentence += str(number) + ";" + sentences[x] + ";" + bahasa + "\n"
        number = number + 1

# Write output to Sentences.txt
out = open(fout_name, "w")  
out.write(sentence.encode('utf-8').strip())
out.close()
