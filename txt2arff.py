import sys
import re

# README
# python txt2arff.py <input-file>.txt <output-file>.arff

name = ''

# input 
fin_name = sys.argv[1]
fout_name = sys.argv[2]

with open(fin_name, 'rb') as fin:
	pattern = re.compile(r',\s')
	attributes = re.sub(pattern, ',', fin.readline().strip())
	attributes_list = attributes.split(',')

# Begin exporting to ARFF
fout = open(fout_name, 'w')

# @RELATION
# @RELATION <relation-name>
name = fin_name.replace('.txt','')
fout.write('@RELATION ' + str(name)+ '\n\n')

# @ATTRIBUTE
# @ATTRIBUTE <attribute-name> <data-type>
attributes_size = len(attributes_list)
for i in range(0, attributes_size-1):
	fout.write('@ATTRIBUTE \'' + attributes_list[i] + '\' ' + 'NUMERIC' + '\n')

fout.write('@ATTRIBUTE ' + 'language_class' + ' ' + '{Inggris,Indonesia,Perancis,Spanyol}' + '\n')

# @DATA
fout.write('\n@DATA \n')
with open(fin_name, 'rb') as fin:
    next(fin)
    for line in fin:
        fout.write(line)

fout.close()
