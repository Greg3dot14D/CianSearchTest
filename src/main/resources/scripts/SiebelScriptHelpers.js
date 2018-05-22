// Вспомогательные команды для консоли

// Выводит названия апплетов
for(var i in SiebelApp.S_App.GetActiveView().GetAppletMap())
  console.log(i);

// Выводит названия апплетов и их имена
for(var i in SiebelApp.S_App.GetActiveView().GetAppletMap()){
  console.log('[' + SiebelApp.S_App.GetActiveView().GetAppletMap()[i].GetLabel() +']');
  console.log(i);  console.log(SiebelApp.S_App.GetActiveView().GetAppletMap()[i].GetFullId());
  console.log(i);  console.log('\n'); }

// Проваливаемся во вьюху
SiebelApp.S_App.GotoView('###');

// Название активной вьюхи
console.log(SiebelApp.S_App.GetActiveView().GetName());

// Выводит список контроллов для апплета
theApplication().FindApplet("### Applet Name ###")._applet.GetControls();

// Выводит найденный контрол в DOM-е
theApplication().FindApplet("### Applet Name ###").FindActiveXControl("### Controll Name ###");


theApplication().FindApplet("### Applet Name ###")._applet.GetControls();
theApplication().FindApplet("### Applet Name ###").FindActiveXControl("### Controll Name ###");

// Для именования контролов (без кнопок), смотрим в доме
var appletName = 'SBRF Current Activity Form Applet Has Record';
SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetRawRecordSet()

// Контролы, смотрим в доме
SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetControls();

theApplication().FindApplet('### Applet Name ###')._applet.GetControls();
.FindActiveXControl('### Controll Name ###');


// Список контроллов на форме с описанием
var appletName = 'SBRF Current Activity Form Applet Has Record';
var arr = theApplication().FindApplet(appletName)._applet.GetControls();
for(var i in arr)
  console.log(arr[i].GetUIType() + ' -> [' + i + '] -> [' + arr[i].GetDisplayName() + ']');