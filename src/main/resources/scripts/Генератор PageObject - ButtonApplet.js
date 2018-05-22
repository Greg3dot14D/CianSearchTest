// Название апплета
var appletName = '%Имя_Апплета';
// Название константы, определяющей имя апплета
var var_appletName = '';

//###########################################################################################

var appletCaption = SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetLabel();

var arr = theApplication().FindApplet(appletName)
._applet.GetControls();


var str = "";

function removePrefix(s){
  if(s.contains(' ') && s.substr(0,2).contains(s.substr(0,2).toUpperCase())){
    s = s.substr(1);
    } 
  return s;}

function getRemovePrefix(s){
  while(s != removePrefix(s))
    s = removePrefix(s);
  return s;
}

if(var_appletName == ''){
  var_appletName = getRemovePrefix(appletName).replace(/ /g,"").replace('#','');
    var_appletName = var_appletName.substr(0,1).toLocaleLowerCase() + var_appletName.substr(1);
}



var appletType = appletName.replace(appletName.substring(appletName.length, appletName.lastIndexOf(' Applet')), '');
appletType = appletType.substring(appletType.length, appletType.lastIndexOf(' '))

function generateApplet(appletName, appletCaption){
  console.log('// ' + appletType + ' Applet "' + appletCaption + '"');
  if(appletName.contains('List Applet') || appletName.contains('Pick Applet') || appletName.contains('MVG')){
      console.log('@AppletCorrector()');
      console.log('public SiebelTable table;');
    }
    else{
      console.log('@AppletCorrector()');
      console.log('public SiebelApplet applet;');
    
    }
    console.log('\n');
}

//Название константы, определяющей имя апплета
var const_appletName = 'const_' + var_appletName;
var var_appletClassName = var_appletName.substr(0,1).toUpperCase() + var_appletName.substr(1);



console.log('/////////////////////////////    ' + appletType + ' Applet "' + appletCaption + '"    /////////////////////////////');
console.log('@Block(appletName="' + appletName + '")');
console.log('public ' + var_appletClassName + ' ' + var_appletName + ';');
console.log('\n');
console.log('public static class ' + var_appletClassName + '{');


function generateObject( controlTypeInComment, siebelControlType, controlPostfix){
    //console.log('// '+ controlTypeInComment +' "' + arr[i].GetDisplayName() +'"   [' + appletType + ' Applet "' + appletCaption + '"]');
    //console.log('// '+ controlTypeInComment +' "' + arr[i].GetDisplayName() +'"');
  console.log('// '+ controlTypeInComment +' "' + theApplication().FindApplet(appletName).FindActiveXControl(arr[i].GetName()).innerText +'"');
  //console.log('// '+ controlTypeInComment +' "' + appletName +'"');
    // theApplication().FindApplet("SBRF Request Top Buttons Applet").FindActiveXControl("FastBtn1").innerText;
  
  console.log('@AppletCorrector(controlName = "' + i +'")');
    //console.log('public ' + siebelControlType + ' ' + i.replace(/ /g,"").substr(0,1).toLowerCase() + i.replace(/ /g,"").substr(1)+ controlPostfix + ";");
    str = getRemovePrefix(i).replace(/ /g,"").replace('#','');
    console.log('public ' + siebelControlType + ' ' + str + controlPostfix + ";");
}
//console.log('////////////////////////////////////////   '  + appletCaption + '   ////////////////////////////////////////');
//console.log('//Константа определяющая ' + appletType + ' Applet "' + appletCaption + '"');
//console.log('private final String ' + const_appletName + ' = "' + appletName + '";');
//console.log('\n');

generateApplet(appletName, appletCaption);

var showMore = document.getElementById(SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetFullId()).getElementsByClassName('siebui-link-icon-e');
if(showMore.length >0){
  console.log('//Кнопка "Показать еще"');
  console.log('@AppletCorrector()');
  console.log('public ShowMoreButton ShowMore_Button;');
  console.log('\n');
}

for(var i in arr){
  var UIType = arr[i].GetUIType();
  
  if(UIType == 'PlainText')
    generateObject('Кнопка', 'SiebelButton', '_Button');
  else
    console.log(arr[i].GetUIType() + ' -> [' + i + '] -> [' + arr[i].GetDisplayName() + ']');
  console.log('\n');
}

console.log('//Дожидаемся появления формы (используем явное ожидание)');
console.log('public boolean ensureFormOpened(){');
console.log('return WaitManager.waitApplet(this.applet);');
console.log(' }');

console.log('}');