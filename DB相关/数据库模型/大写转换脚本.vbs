dim model 'current model
set model = ActiveModel

If (model Is Nothing) Then
MsgBox "There is no current Model"
ElseIf Not model.IsKindOf(PdPDM.cls_Model) Then
MsgBox "The current model is not an Physical Data model."
Else
ProcessTables model
ProcessSequences model
End If

'*****************************************************************************
'������ProcessSequences
'���ܣ��ݹ�������е�����
'*****************************************************************************
sub ProcessSequences(folder)
'����ģ���е����У�Сд�Ĵ�д
dim sequence
for each sequence in folder.sequences
sequence.name = UCase(sequence.name)
sequence.code = UCase(sequence.code)
next
end sub

'*****************************************************************************
'������ProcessTables
'���ܣ��ݹ�������еı�
'*****************************************************************************
sub ProcessTables(folder)
'����ģ���еı�
dim table
for each table in folder.tables
if not table.IsShortCut then 
ProcessTable table
end if
next
'����Ŀ¼���еݹ�
dim subFolder
for each subFolder in folder.Packages
ProcessTables subFolder
next 
end sub

'*****************************************************************************
'������ProcessTable
'���ܣ�����ָ��table�������ֶΣ����ֶ�����Сд�ĳɴ�д��
' �ֶδ�����Сд�ĳɴ�д
' ������Сд�ĳɴ�д 
'*****************************************************************************
sub ProcessTable(table)
dim col
for each col in table.Columns
'���ֶ�����Сд�ĳɴ�д
col.code = UCase(col.code)
col.name = UCase(col.name)
next 
table.name = UCase(table.name)
table.code = UCase(table.code)
end sub
