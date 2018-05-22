#include <Constants.au3>

Local $cmdName = $CmdLine[1];
Local $className = $CmdLine[2] 
Local $controlName = $CmdLine[3] 

;активация окна
WinActivate($className);

;ожидание пока окно станет активным
Local $handle = WinWaitActive($className, "");

;мигание окна, просто чтобы убедиться в правильности его выбора
;можно удалить - занимает лишнее время
;WinFlash($handle);

If $cmdName = "SET_TEXT" Then
	ControlSetText($handle, "", $controlName, $CmdLine[4]);
	Exit
ElseIf $cmdName = "CLICK" Then
	MsgBox($MB_SYSTEMMODAL, "AutoIt Example", "OK.  Bye!")
	ControlClick($handle, "", $controlName);
	Exit
EndIf