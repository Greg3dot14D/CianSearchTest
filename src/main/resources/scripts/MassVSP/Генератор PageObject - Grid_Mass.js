//ГЕНЕРАТОР ОБЪЕКТОВ ТАБЛИЦЫ

//искомый css
var css = 'GMOMJOBBMWD-ru-sbrf-theme-client-base-grid-Css3ColumnHeaderAppearance-Styles-headInner';

var doc = document.getElementById('symbUrlIFrame0').contentDocument.getElementById('wzFrame').contentDocument; 

var el = doc.getElementsByClassName(css);


function removeFakes(varName){
  return varName.replace(/ /g,"").replace('#','').replace('-','').replace('(','').replace(')','').replace('.','').replace(',','');
}

function setFakesToSpace(varName){
  return varName.replace('#g',' ').replace('-',' ').replace('(',' ').replace(')',' ').replace('.g',' ').replace(',g',' ');
}


function removePrefix(s){
  if(s.contains(' ') && s.substr(0,2).contains(s.substr(0,2).toUpperCase()))
    s = s.substr(1);
  return s;
}

function stickText(text){
  a= text.split(' ');
  let result = '';
  for(let i=0; i < a.length; i ++){
    result += a[i].substring(0,1).toUpperCase() + (a[i].length > 0 ? a[i].substring(1): '');
  }
  return result;
}

function getTrimedText(text){
  return removeFakes(stickText(setFakesToSpace(text)));
}

console.log('@FindBy(css = ".'+ css +'")');
console.log('public Grid clientTable;\npublic static class Grid extends GridContainer {');
for(let i =0; i < el.length; i ++){
  console.log('// ячейка "' +el[i].innerText +'"');
  console.log('@Name("' +el[i].innerText +'")');
  console.log('public Cell ' + getTrimedText(el[i].innerText) +';');
  console.log('\n');
}

console.log('}');
