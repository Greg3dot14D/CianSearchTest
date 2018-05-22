// #########################    ПРАМЕТРЫ    ##########################

// имя формируемого класса
var pageClassName = 'PfmEdit';

// постфикс к имени класса ( обычно Form / ( List || Table ) ... кому как удобнее различать)
var postfix = 'Form';



// #########################    РЕАЛИЗАЦИЯ    ##########################

// список типов объектов, по которым умеем формировать код
var controlTypes = ['select', 'input', 'textbox', 'hasDatepicker'];

// словарь ссылок к пакетам типовых элементов
var set = new Set();
var map = new Map();

map.set('Cell', 'import azimuth.oui.elements.typifiedelements.Cell;');
map.set('Grid', 'import azimuth.oui.elements.typifiedelements.grid.GridContainer;');
map.set('DatePicker', 'import azimuth.oui.elements.typifiedelements.DatePicker;');
map.set('TextInput', 'import azimuth.oui.elements.typifiedelements.TextInput;');
map.set('TextBox', 'import azimuth.oui.elements.typifiedelements.TextBox;');
map.set('Select', 'import azimuth.oui.elements.typifiedelements.Select;');
map.set('Button', 'import ru.yandex.qatools.htmlelements.element.Button;');
map.set('CheckBox', 'import azimuth.oui.elements.typifiedelements;');



var pageTitle = document.getElementsByTagName('h1').item(0).innerText;
var tables = $('.table-wrapper .table');
var o = document.getElementsByClassName('input-item');

function getDate(){
  return new Date().toLocaleFormat('%d.%m.%Y');
}

function getClassName(postfix){
  return pageClassName + postfix;
}


function removeFakes(varName){
  return varName.replace(/[\.,#()-/' /'\[\]]/g,'');
}

function setFakesToSpace(varName){
  return varName.replace(/[\.,#()-/' /'\[\]]/g,' ');
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

// Фрагмент кода, для генерации данных для таблицы
  function printTableCode(index){

    let indexPostfix = '';
    if(tables.length > 1)
      indexPostfix = '' + index;

    let o = $('.table__cell_header>.table__cell-content');
    let id = tables[index].getAttribute('id');

    console.log('@FindBy(id="' + id + '")');
    console.log('public Grid' + indexPostfix + ' tableBox' + indexPostfix + ';');
    console.log('public static class Grid' + indexPostfix + ' extends GridContainer{');

    for(let i=0; i < o.length; i ++){
        console.log('// ячейко "' + o[i].innerHTML + '"');
        console.log('@Name("' + o[i].innerHTML + '")');
        console.log('public Cell ' + getTrimedText(o[i].innerHTML) + ';');
        console.log('\n');
    }
    console.log('}');
  }


function isTextInput(el){
  if(el.getElementsByClassName('textbox').length > 0)
    return false;
  return true;
}

function getFormBoxTitle(){
  let formHeaders = document.getElementsByClassName('collapsible-box__header');

  if(formHeaders.length > 0)
    return formHeaders[0].innerText;

  formHeaders = document.getElementsByTagName('h1');

  if(formHeaders.length > 0)
    return formHeaders[0].innerText;
  return 'unnamed';
}

function getFormBoxFindBy(){
  let formTable = document.getElementsByClassName('collapsible-box__header');

  if(formTable.length > 0)
    return 'css=".collapsible-box"';

  formTable = document.getElementsByClassName('table_form_layout');

  if(formTable.length == 1)
    return 'css=".table_form_layout"';
  return 'css="azimuth-workspace"';
}

//console.log(getFormBoxName());
//console.log(getFormBoxFindBy());

// printBlock

console.log('import azimuth.oui.base.HtmlPage;');
console.log('import org.openqa.selenium.WebDriver;');
console.log('import org.openqa.selenium.support.FindBy;');
console.log('import ru.yandex.qatools.htmlelements.annotations.Name;');
console.log('import azimuth.oui.elements.typifiedelements.*;');
if(tables.length > 0)
  console.log('import azimuth.oui.elements.typifiedelements.grid.GridContainer;');

console.log('\n');
console.log('/**');
console.log(' * Created by js on ' + getDate() +'. ');
console.log('**/');


className = getTrimedText(getClassName(postfix)) + 'Page';
console.log('// ' + pageTitle);
console.log('public class ' + className + ' extends HtmlPage{');

if(o.length > 0 && tables.length > 0){
    console.log('// ' + getFormBoxTitle());
    console.log('@Name("' + getFormBoxTitle() + '")');
    console.log('@FindBy(' + getFormBoxFindBy() + ')');
    console.log('public FormBox formBox;');
    console.log('public static class FormBox extends HtmlElementExtended {');
}

// генерация кода для контроллов россыпью
for(let i=0; i < o.length; i ++){
  let textMapIndex = 0;
  let el = o[i];

  for(let j=0; j < controlTypes.length; j++ ){
    let skipMe = false;
    let controlType = controlTypes[j];
    if(el.getElementsByClassName(controlType).length > 0){
      let type      = '';
      let typeLable = '';

      switch(controlType){
        case 'hasDatepicker':
          type      = 'DatePicker';
          typeLable = 'ДатаПикер';
          break;
        case 'textbox':
          type      = 'TextBox';
          typeLable = 'ТекстБокс';
          break;
        case 'select':
          type      = 'Select';
          typeLable = 'Селект';
          break;

        case 'input':
          if(!isTextInput(el))
            skipMe = true;
          else{
            type      = 'TextInput';
            typeLable = 'ТекстИнпут';
          }
          break;

        case 'fine-button':
          type      = 'Button';
          typeLable = 'Кнопарь';
          break;

      }
      if(!skipMe){

        let label = el.getElementsByClassName('label').item(0).textContent;
        let id = el.parentElement.getAttribute('id');

        if(controlType == 'input'){
        try{
          if(el.getElementsByTagName('input').item(0).getAttribute('type') == 'checkbox'){
            type      = 'CheckBox';
            typeLable = 'Чекбокс';
            id = el.getElementsByClassName('item').item(0).getAttribute('id');
          }
        }catch(e){}

        // просаживаем ключ типа элемента в сет
        //set.add(type);
      }

        if(id == null)
          id = el.getAttribute('id');

        if(type != ''){
          console.log('// ' + typeLable + ' "' + label + '"');
          console.log('@Name("' + label + '")');
          console.log('@FindBy(id="' + id + '")');
          console.log('public ' + type + ' ' + getTrimedText(label) + '_' + type + ';');
          console.log('\n');
        }
      }
    }
  }
}

o = document.getElementsByClassName('fine-button');
controlType = 'Button';

for(let i=0; i < o.length; i ++){
  let b = o[i];
  console.log('// кнопарь "' + b.textContent + '"');
  console.log('@Name("' + b.textContent + '")');
  console.log('@FindBy(id="' + b.parentElement.getAttribute('id') + '")');
  console.log('public ' + controlType + ' ' + getTrimedText(b.textContent) + '_' + controlType + ';');
  console.log('\n');
}

if(o.length > 0 && tables.length > 0){
    console.log('}');
    console.log('\n');
}

if(tables.length > 0)
for(let j=0; j < tables.length; j ++)
  printTableCode(j);

console.log('public ' + className + '(WebDriver driver) {super(driver);}');
console.log('}');