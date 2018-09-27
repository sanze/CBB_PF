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
'函数：ProcessSequences
'功能：递归遍历所有的序列
'*****************************************************************************
sub ProcessSequences(folder)
'处理模型中的序列：小写改大写
dim sequence
for each sequence in folder.sequences
sequence.name = UCase(sequence.name)
sequence.code = UCase(sequence.code)
next
end sub

'*****************************************************************************
'函数：ProcessTables
'功能：递归遍历所有的表
'*****************************************************************************
sub ProcessTables(folder)
'处理模型中的表
dim table
for each table in folder.tables
if not table.IsShortCut then 
ProcessTable table
end if
next
'对子目录进行递归
dim subFolder
for each subFolder in folder.Packages
ProcessTables subFolder
next 
end sub

'*****************************************************************************
'函数：ProcessTable
'功能：遍历指定table的所有字段，将字段名由小写改成大写，
' 字段代码由小写改成大写
' 表名由小写改成大写 
'*****************************************************************************
sub ProcessTable(table)
dim col
for each col in table.Columns
'将字段名由小写改成大写
col.code = UCase(col.code)
col.name = UCase(col.name)
next 
table.name = UCase(table.name)
table.code = UCase(table.code)
end sub
