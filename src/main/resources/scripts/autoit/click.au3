#include <Constants.au3>

Local $cmdName = $CmdLine[1];
Local $className = $CmdLine[2] 
Local $controlName = $CmdLine[3] 

;��������� ����
WinActivate($className);

;�������� ���� ���� ������ ��������
Local $handle = WinWaitActive($className, "");

;������� ����, ������ ����� ��������� � ������������ ��� ������
;����� ������� - �������� ������ �����
;WinFlash($handle);

If $cmdName = "SET_TEXT" Then
	ControlSetText($handle, "", $controlName, $CmdLine[4]);
	Exit
ElseIf $cmdName = "CLICK" Then
	MsgBox($MB_SYSTEMMODAL, "AutoIt Example", "OK.  Bye!")
	ControlClick($handle, "", $controlName);
	Exit
EndIf