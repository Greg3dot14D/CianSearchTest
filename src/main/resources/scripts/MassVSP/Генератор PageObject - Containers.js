var cont = document.getElementsByClassName('GMOMJOBBJT-com-sencha-gxt-theme-base-client-field-FieldLabelDefaultAppearance-Style-fieldItem');


// #НАЧАЛО#   Корректор текста
function removeFakes(varName){
  return varName.replace(/ /g,"").replace('#','').replace('-','').replace('(','').replace(')','').replace('\n','').replace('/','').replace('.','').replace(',','').replace('?','');
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
  let result = text;
  for(let i=0; i < 5; i ++)
    result = removeFakes(stickText(setFakesToSpace(result)));
  return result;
}

function toSmall(text){
  return text.substring(0,1).toLowerCase() + text.substring(1);
}

// #КОНЕЦ#   Корректор текста



function getSelector(el){
  let id = el.getAttribute('id');
  if(id != null)
    return 'id ="' + id + '"';
  return '';
}

function getLabel(el){
    let label = el.getElementsByTagName('label').item(0);
    if(label != null)
      return label.innerText;
    return '';
}


function getButtons(el){
  let buttons = el.getElementsByClassName('GMOMJOBBPJC-ru-sbrf-appearance-client-button-AbstractButtonAppearance-ButtonStyle-button');
  return buttons;
}


function getButtonText(el){
  return el.getElementsByClassName('GMOMJOBBOKC-ru-sbrf-appearance-client-button-AbstractButtonAppearance-ButtonStyle-text').item(0).innerText;
}


function getButtonSelector(el){
  return 'xpath = ".//div[text()=\'' + getButtonText(el) + '\']"';
}


function printButtons(el){
  let buttons = getButtons(el);
    for(let b=0; b<buttons.length; b++ ){
      let buttonText = getButtonText(buttons[b]);

      console.log('// кнопарь [' + buttonText + ']');
      console.log('@Name("' + buttonText + '")');
      console.log('@FindBy(' + getButtonSelector(buttons[b]) + ')');
      console.log('public Button ' + getTrimedText(buttonText) + '_Button;');
    }
}


function isRadioGroup(el){
  return el.getElementsByClassName('GMOMJOBBKSC-ru-sbrf-appearance-client-form-RadioAppearance-RadioStyle-wrap').length > 0;
}


function printRadioGroup(el){
  let className = 'MassVspRadioGroup_' + index;
  console.log('// Радиогруппа ' + getLabel(el));
  console.log('public ' + className + ' ' + toSmall(getTrimedText(getLabel(el))) + '_RadioGroup;');
  console.log('public class ' + className + ' extends MassVspRadioGroup {');


  let radio = el.getElementsByClassName('GMOMJOBBKSC-ru-sbrf-appearance-client-form-RadioAppearance-RadioStyle-wrap');

  for(let i =0; i < radio.length; i ++){

    let radioName = radio[i].innerText;
    console.log('// Радиокнопарь [' + radioName + ']');
    console.log('@Name("' + radioName + '")');
    console.log('@FindBy(xpath=".//label[text()=\'' + radioName +'\']//ancestor::div[2]//input")');

    console.log('public RadioButton ' + toSmall(getTrimedText(radioName)) + '_RadioButton;');
    //.//label[text()='Для ремонта, отдыха, на покупки (в том числе автомобиля), просто нужны деньги и т.д.']//ancestor::div[2]//input
  }

      //console.log('public static class ' + containerName + '{');
}

var index = 0;
for(let i = 0; i < cont.length; i ++){
  index ++;
  let el = cont[i];
  let selector = getSelector(el);

  let containerName = '';


  if(selector != ''){
    let label = getLabel(el);
    console.log('// контейнер ' + label);
    console.log('@Name("' + label + '")');
    console.log('@FindBy(' + selector + ')');

    containerName = getTrimedText(label);

    if(!isRadioGroup(el)){
      console.log('public ' + containerName + ' ' + toSmall(containerName) + ';');
      console.log('public static class ' + containerName + '{');
    }
    else
      printRadioGroup(el);

    // печатаем кнопари
    printButtons(el);


  }


    if(containerName != '')
      console.log('}');

//  }
}