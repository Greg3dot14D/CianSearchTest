var tabs = document.getElementsByClassName('siebui-nav-tabs');
//var el = document.getElementsByClassName('ui-tabs-anchor');

for(var t=0; t < tabs.length; t ++){
  var els = tabs.item(t).getElementsByClassName('ui-tabs-anchor');
  for(var i = 0; i < els.length; i ++){
    console.log("// " + tabs.item(t).getAttribute('title') + ' - [' + els.item(i).innerText + ']');
    console.log('@AppletCorrector(appletName = const_levelName, controlName = "'  + els.item(i).innerText + '")');
    console.log("public SiebelTab tab_" + i + ";");
    //    console.log(tabs.item(t).getAttribute('title') + ' || ' + els.item(i).innerText);
  }
}

//////////////////////////////////////////////  Вариант с названиями типа tab_1 ////////////////////////////////////////
var tabs = document.getElementsByClassName('siebui-nav-tabs');
var tabLevel = 0;

for(var t=0; t < tabs.length; t ++){
  var els = tabs.item(t).getElementsByClassName('ui-tabs-anchor');
  var titleLevelName = tabs.item(t).getAttribute('title');
  var titleLevelValue = t;

  if(titleLevelName.contains('первого уровня'))
    titleLevelValue = 1;
  if(titleLevelName.contains('второго уровня'))
    titleLevelValue = 2;
  if(titleLevelName.contains('третьего уровня'))
    titleLevelValue = 3;
  if(titleLevelName.contains('четвертого уровня'))
    titleLevelValue = 4;


  console.log('@Block(appletName="'+ tabs.item(t).getAttribute('title') +'")');
  console.log('public TabsLevel' + titleLevelValue + ' level' + titleLevelValue + ';');
  console.log('public static class TabsLevel' + titleLevelValue + '{');
  for(var i = 0; i < els.length; i ++){
    console.log("// " + tabs.item(t).getAttribute('title') + ' - [' + els.item(i).innerText + ']');
    //console.log('@AppletCorrector(appletName = const_levelName, controlName = "'  + els.item(i).innerText + '")');
    console.log('@AppletCorrector(controlName = "'  + els.item(i).innerText + '")');
    console.log("public SiebelTab tab_" + i + ";");
    //console.log("public SiebelTab " + els.item(i).innerText.replace(' ','_').replace(' ','_').replace(' ','_') + ";");
    //    console.log(tabs.item(t).getAttribute('title') + ' || ' + els.item(i).innerText);
  }
  console.log('}');
}


//////////////////////////////////////////////  Вариант с русскими названиями  /////////////////////////////////////////
var tabs = document.getElementsByClassName('siebui-nav-tabs');
//var el = document.getElementsByClassName('ui-tabs-anchor');
var tabLevel = 0;


// console.log('//////////////////////////////////////////////////    ЗАКЛАДКИ    //////////////////////////////////////////////////');
// console.log('@Block');
// console.log('public LocalTabs tabs;');
// console.log('public static class LocalTabs {');

for(var t=0; t < tabs.length; t ++){
  var els = tabs.item(t).getElementsByClassName('ui-tabs-anchor');
  var titleLevelName = tabs.item(t).getAttribute('title');
  var titleLevelValue = t;

  if(titleLevelName.contains('первого уровня'))
    titleLevelValue = 1;
  if(titleLevelName.contains('второго уровня'))
    titleLevelValue = 2;
  if(titleLevelName.contains('третьего уровня'))
    titleLevelValue = 3;
  if(titleLevelName.contains('четвертого уровня'))
    titleLevelValue = 4;
  
  
  console.log('@Block(appletName="'+ tabs.item(t).getAttribute('title') +'")');
  console.log('public TabsLevel' + titleLevelValue + ' level' + titleLevelValue + ';');
  console.log('public static class TabsLevel' + titleLevelValue + '{');
  for(var i = 0; i < els.length; i ++){
    console.log("// " + tabs.item(t).getAttribute('title') + ' - [' + els.item(i).innerText + ']');
    //console.log('@AppletCorrector(appletName = const_levelName, controlName = "'  + els.item(i).innerText + '")');
    console.log('@AppletCorrector(controlName = "'  + els.item(i).innerText + '")');
    //console.log("public SiebelTab tab_" + i + ";");
    console.log("public SiebelTab " + els.item(i).innerText.replace(' ','_').replace(' ','_').replace(' ','_') + ";");
    //    console.log(tabs.item(t).getAttribute('title') + ' || ' + els.item(i).innerText);
  }
  console.log('}');
}