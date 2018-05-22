// Название апплета
var appletName = 'ИМЯ_АПЛЕТА';
// название константы, определяющей имя апплета
var var_appletName = '';

// true - печатаем в отдельном классе(файле)
// false - вставляем в тело страницы
var isSingleApplet = true;

// true - выводим SiebelCell для объекта типа Cell
// false - создаем контейнер Grid и в него выводим ячейки типа SiebelElement (предпочтительно)
var printCell = false;

//###########################################################################################
var appletCaption = SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetLabel();

// список контролов
var arr = theApplication().FindApplet(appletName)._applet.GetControls();


/////////////////////  Определяем - является ли апплет листапплетом или это формапплет --- начало --- //////////////////
var appletType = 'List Applet';
//var colArray = null;
try{
// список колонок
    var colArray = SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetListOfColumns();

    // чистим список контролов от списка колонок
    for(var j in arr)
        for(var i in colArray)
            if(colArray[i] == arr[j])
                arr[j] = null;
}catch(e){
    appletType = 'Form Applet';
}
/////////////////////  Определяем - является ли апплет листапплетом или это формапплет --- конец --- //////////////////


///////// генерим имя класса и имя объекта апплета -- начало -- ///////////////
var var_appletClassName = '';

if(var_appletName == ''){
    var_appletName = getRemovePrefix(appletName);
    var_appletName = correctAppletName(var_appletName);
    var_appletClassName = correctAppletName(appletName).substr(0,1).toUpperCase() + correctAppletName(appletName).substr(1);
    var_appletName = var_appletName.substr(0,1).toLocaleLowerCase() + var_appletName.substr(1);
}
else
    var_appletClassName = var_appletName.substr(0,1).toUpperCase() + var_appletName.substr(1);
///////// генерим имя класса и имя объекта апплета  -- конец -- ///////////////


function isCell(control){
    //for(var i in arr)
    //    if(arr[i] == colArray[controlName])
    //      return true;
    //return false;
    if(colArray == null)
        return false;

    for(var i in colArray){
        if(colArray[i] == control)
            return true;
    }
    return false;
}

function correctAppletName(varName){
    return varName.replace(/ /g,"").replace('#','').replace('-','').replace('(','').replace(')','');
}

function removePrefix(s){
    if(s.indexOf(' ')!== -1 && s.substr(0,2).indexOf(s.substr(0,2).toUpperCase())!==-1){
        s = s.substr(1);
    }
    return s;}

function getRemovePrefix(s){
    while(s != removePrefix(s))
        s = removePrefix(s);
    return s;
}

function isListApplet(){
    return appletType.indexOf('List Applet');
}

function printApplet(appletName, appletCaption){
    console.log('// ' + appletType + ' "' + appletCaption + '"');
    //if(appletName.contains('List Applet') || appletName.contains('Pick Applet') || appletName.contains('MVG')){
    if(isListApplet()){
        console.log('@AppletCorrector()');
        console.log('public SiebelTable table;');
    }
    else{
        console.log('@AppletCorrector()');
        console.log('public SiebelApplet applet;');

    }
}

// выводим на печать описание контрола
function generateObject(controlTypeInComment, siebelControlType, controlPostfix, control){
    //console.log('// '+ controlTypeInComment +' "' + arr[i].GetDisplayName() +'"   [' + appletType + ' Applet "' + appletCaption + '"]');
    //console.log('// '+ controlTypeInComment +' "' + control.GetDisplayName() +'"');
    console.log('@Name("'+ control.GetDisplayName() +'")');
    console.log('@AppletCorrector(controlName = "' + control.GetName() +'")');
    var str = getRemovePrefix(i).replace(/ /g,"").replace('#','');
    if(isCell(control)){
        if(printCell)
            console.log('public SiebelCell ' + str + "_Cell;");
        else
            console.log('public ' + siebelControlType + ' ' + str + controlPostfix + ";");
    }else
        console.log('public ' + siebelControlType + ' ' + str + controlPostfix + ";");
}

function getAppletIdByAppletName(appletName){
    //let activeView = SiebelApp.S_App.GetActiveView();
    //let appletMap = activeView.GetAppletMap();
    //let applet = appletMap[appletName];
    //activeView.SetActiveApplet(applet);
    //return applet.GetFullId();
    return SiebelApp.S_App.GetActiveView().GetAppletMap()[appletName].GetFullId();
}

function generateRadioGroup(appletName){
    let inputs = document.getElementById(getAppletIdByAppletName(appletName)).getElementsByTagName('input');

    console.log("@Block");
    console.log("public RBGroup rbGroup;");
    console.log("public static class RBGroup{");
    for(let i=0; i < inputs.length; i ++){
        let inputValue = inputs[i].value;
        console.log("@FindBy(css = \"[value='" + inputValue + "']\")");
        console.log("public SiebelRadioButton " + inputValue
                .replace(/ /g,"_")
                .replace(/#/g,"_")
                .replace(/-/g,"_")
            //.replace('#','').replace('-','').replace('(','').replace(')','');
            + ";");
    }
    console.log("}");
}


// определяем тип контрола и выводим на печать
function printControl(control){
    var UIType = control.GetUIType();

    if(UIType == 'Button')
        generateObject('кнопка', 'SiebelButton', '_Button', control);
    else if(UIType == 'JText' || UIType == 'Mailto')
        generateObject('TextInput', 'SiebelTextInput', '_TextInput', control);
    else if(UIType == 'JTextArea')
        generateObject('TextBox', 'SiebelTextBox', '_TextBox', control);
    else if(UIType == 'JComboBox')
        generateObject('Select', 'SiebelSelect', '_Select', control);
    else if(UIType == 'JDatePick' || UIType == 'JDateTimePick' || UIType == 'JDateTimeZonePick')
        generateObject('DateTimePick', 'SiebelDatePicker', '_DatePicker', control);
    else if(UIType == 'JCheckBox')
        generateObject('CheckBox', 'SiebelCheckBox', '_CheckBox', control);
    else if(UIType == 'JCalculator')
        generateObject('Calculator', 'SiebelCalculator', '_Calculator', control);
    else if(UIType == 'Pick' || UIType == 'Mvg')
        generateObject('Pick', 'SiebelPick', '_Pick', control);
    else if(UIType == 'JText')
        generateObject('TextInput', 'SiebelTextInput', '_TextInput', control);
    else if(UIType == 'Link')
        generateObject('Link', 'SiebelLink', '_Link', control);
    else if(UIType == 'PlainText')
        generateObject('PlainText', 'SiebelPlainText', '_PlainText', control);
    else if(UIType == 'JRadioButton')
        generateRadioGroup(appletName);
    else
        console.log(control.GetUIType() + ' -> [' + i + '] -> [' + control.GetDisplayName() + ']');
    console.log('\n');
}




///////////////////////////////////////////////   P R I N T   ///////////////////////////////////////////////

console.log('/////////////////////////////    ' + appletType + ' "' + appletCaption + '"    /////////////////////////////');
console.log('@Block(appletName="' + appletName + '")');
console.log('public ' + var_appletClassName + ' ' + var_appletName + ';');
console.log('\n');
//console.log('public static class ' + var_appletClassName + '{');

if(isSingleApplet){
    console.log('/////////////////////////////    ' + appletType + ' "' + appletCaption + '"    /////////////////////////////');
    console.log('@AppletCorrector(appletName="' + appletName + '")');
    console.log('public class ' + var_appletClassName + '{');
}else
    console.log('public static class ' + var_appletClassName + '{');

printApplet(appletName, appletCaption);

console.log('@Block');
console.log('public SiebelMethods methods;');
console.log('\n');


var showMore = document.getElementById(SiebelApp.S_App.GetActiveView()
    .GetAppletMap()[appletName]
    .GetFullId())
    .getElementsByClassName('siebui-link-icon-e');
if(showMore.length >0){
    console.log('// кнопка "Показать еще"');
    console.log('@AppletCorrector()');
    console.log('public ShowMoreButton ShowMore_Button;');
    console.log('\n');
}

// печатаем список контролов
for(var i in arr){
    if(arr[i] != null )
        printControl(arr[i])
}

// печатаем список ячеек
if(isListApplet()){

    if(!printCell){
        console.log('@Block');
        console.log('public Grid grid;');
        console.log('public static class Grid extends SiebelGrid{');
    }

    console.log('// Вертикальный ScrollBar"');
    console.log('@Block');
    console.log('public ScrollBar scrollBar;');
    console.log('\n');

    for(var i in colArray){
        if(colArray[i] != null )
            printControl(colArray[i])
    }

    if(!printCell)
        console.log(' }');
}

console.log('}');