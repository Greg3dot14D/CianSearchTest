// #########################    ПРАМЕТРЫ    ##########################

// имя формируемого класса
var pageClassName = 'PfmEdit';

// постфикс к имени класса ( обычно Form / ( List || Table ) ... кому как удобнее различать)
var postfix = '';



// #########################    РЕАЛИЗАЦИЯ    ##########################

// список типов объектов, по которым умеем формировать код
var controlTypes = ['select', 'input', 'textbox', 'hasDatepicker'];

// словарь ссылок к пакетам типовых элементов


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
    if(tables.length == 1)
      indexPostfix = '' + index;

    let o = $('.table__cell_header>.table__cell-content');
    let id = tables[index].getAttribute('id');


    for(let i=0; i < o.length; i ++){
        console.log('@Alias("' + o[i].innerHTML + '")');
        console.log('public String ' + getTrimedText(o[i].innerHTML) + ';');
        console.log('\n');
    }
    console.log('}');
  }


function isTextInput(el){
  if(el.getElementsByClassName('textbox').length > 0)
    return false;
  return true;
}

// printBlock

console.log('import azimuth.oui.annotations.Alias;');
console.log('import azimuth.oui.base.BaseModel;');
console.log('\n');
console.log('/**');
console.log(' * Created by js on ' + getDate() +'. ');
console.log('**/');


className = getTrimedText(getClassName(postfix)) + 'Model';
console.log('public class ' + className + ' extends BaseModel{');

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

        if(type != ''){
          console.log('@Alias("' + label + '")');
          console.log('public String ' + getTrimedText(label) + ';');
          console.log('\n');
        }
      }
    }
  }
}

if(tables.length > 0)
for(let j=0; j < tables.length; j ++)
  printTableCode(j);
console.log('}');