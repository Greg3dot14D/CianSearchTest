// Создаем объект SiebelView, для текущей выбранной вьюхи

var view = SiebelApp.S_App.GetActiveView();
var viewName = view.GetName();

var str = "";

function removePrefix(){
  if(str.substr(0,2).indexOf(str.substr(0,2).toUpperCase()) !== -1){
    str = str.substr(1);
    return true; } 
  return false;}


// ui-tabs-active ui-state-active siebui-active-navtab ui-state-hover
var el = document.getElementsByClassName("ui-tabs-active");
var tabCaption = "";
for(var i = 0; i < el.length; i ++)
  tabCaption += "[" + el.item(i).getAttribute("aria-label").replace(" Выбрано","") + "]/";

str = viewName.replace(/ /g,"").replace('#','');
    while(removePrefix());


tabCaption = tabCaption.substring(0,tabCaption.length-1);

console.log('@Name("' + tabCaption+ '")');
console.log('@AppletCorrector(appletName = "' + viewName + '")');
console.log('public SiebelView ' + str.substr(0,1).toLowerCase() + str.substr(1) + ';');

