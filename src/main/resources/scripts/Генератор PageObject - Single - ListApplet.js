// �������� �������
var appletName = 'SBRF VSP Action Mass List Applet';
// ���� var_appletName = '', �� ������������ �� appletName
var var_appletName = '';


//###########################################################################################

var appletCaption = SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetLabel();

var arr = theApplication().FindApplet(appletName)._applet.GetControls();

var arrCells = "";

if(isListApplet())
  arrCells = SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetListOfColumns();

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

function isListApplet(){
  //if(appletName.contains('List') || appletName.contains('Pick Applet') || appletName.toUpperCase().contains('MVG') || appletName.contains('Popup Applet'))
    return true;
  //return false;
}


if(var_appletName == ''){
    var_appletName = getRemovePrefix(appletName).replace(/ /g,"").replace('#','').replace('-','');
    var_appletName = var_appletName.substr(0,1).toLocaleLowerCase() + var_appletName.substr(1);
}


// �������� ���������, ������������ ��� �������

var const_appletName = 'const_' + var_appletName;



var appletType = appletName.replace(appletName.substring(appletName.length, appletName.lastIndexOf(' Applet')), '');
appletType = appletType.substring(appletType.length, appletType.lastIndexOf(' '))

function generateApplet(appletName, appletCaption){
  console.log('// ' + appletType + ' Applet "' + appletCaption + '"');
  if(isListApplet()){
      console.log('@AppletCorrector');
      console.log('public SiebelTable table;');
    }
    else{
      console.log('@AppletCorrector');
      console.log('public SiebelApplet applet;');
    
    }
    console.log('\n');
}


//var var_appletClassName = var_appletName.substr(0,1).toUpperCase() + var_appletName.substr(1);
var var_appletClassName = appletName.replace(/ /g,"").replace('#','').replace('-','');

console.log('/////////////////////////////    ' + appletType + ' Applet "' + appletCaption + '"    /////////////////////////////');
console.log('@Block(appletName="' + appletName + '")');
console.log('public ' + var_appletClassName + ' ' + var_appletName + ';');

console.log('/////////////////////////////    ' + appletType + ' Applet "' + appletCaption + '"    /////////////////////////////');
console.log('public class ' + var_appletClassName + '{');


function isCell(controlName){
	for(var i in arr)
      if(arr[i] == arrCells[controlName])
        return true;
  return false;
	
}

function generateObject( controlTypeInComment, siebelControlType, controlPostfix){
    //console.log('// '+ controlTypeInComment +' "' + arr[i].GetDisplayName() +'"   [' + appletType + ' Applet "' + appletCaption + '"]');
    var name = arr[i].GetDisplayName();
    console.log('// '+ controlTypeInComment +' "' + name +'"');
    if(name != '')
      console.log('@Name("'+ name +'")');
    console.log('@AppletCorrector(controlName = "' + i +'")');
    //console.log('public ' + siebelControlType + ' ' + i.replace(/ /g,"").substr(0,1).toLowerCase() + i.replace(/ /g,"").substr(1)+ controlPostfix + ";");
    //str = i.replace(/ /g,"").replace('#','');
    //while(removePrefix());
    str = getRemovePrefix(i).replace(/ /g,"").replace('#','').replace('-','');
    if(isCell(i))
        //console.log('public SiebelCell ' + str + controlPostfix + ";");
      console.log('public SiebelCell ' + str + "_Cell;");
    else
        console.log('public ' + siebelControlType + ' ' + str + controlPostfix + ";");
}
console.log('////////////////////////////////////////   '  + appletCaption + '   ////////////////////////////////////////');
console.log('\n');

generateApplet(appletName, appletCaption);

var showMore = document.getElementById(SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetFullId()).getElementsByClassName('siebui-link-icon-e');
if(showMore.length >0){
  console.log('// ������ "�������� ���"');
  console.log('@AppletCorrector');
  console.log('public ShowMoreButton ShowMore_Button;');
  console.log('\n');
}

  console.log('// ������������ ScrollBar"');
  console.log('public ScrollBar scrollBar;');
  console.log('\n');

for(var i in arr){
  var UIType = arr[i].GetUIType();
  
  if(UIType == 'Button')
    generateObject('������', 'SiebelButton', '_Button');
  else if(UIType == 'JText' || UIType == 'Mailto')
    generateObject('TextInput', 'SiebelTextInput', '_TextInput');
  else if(UIType == 'JTextArea')
    generateObject('TextBox', 'SiebelTextBox', '_TextBox');
  else if(UIType == 'JComboBox')
    generateObject('Select', 'SiebelSelect', '_Select');
  else if(UIType == 'JDatePick' || UIType == 'JDateTimePick' || UIType == 'JDateTimeZonePick')
    generateObject('DateTimePick', 'SiebelDatePicker', '_DatePicker');
  else if(UIType == 'JCheckBox')
    generateObject('CheckBox', 'SiebelCheckBox', '_CheckBox');
  else if(UIType == 'JCalculator')
    generateObject('Calculator', 'SiebelTextInput', '_Calculator');
  else if(UIType == 'Pick' || UIType == 'Mvg')
    generateObject('Pick', 'SiebelPick', '_Pick');
  else if(UIType == 'JText')
    generateObject('TextInput', 'SiebelTextInput', '_TextInput');
  else
    console.log(arr[i].GetUIType() + ' -> [' + i + '] -> [' + arr[i].GetDisplayName() + ']');
  console.log('\n');
}

console.log('// ���������� ��������� ����� (���������� ����� ��������)');
console.log('public boolean ensureFormOpened(){');
console.log('return WaitManager.waitApplet(this.table);');
console.log(' }');

console.log('}');