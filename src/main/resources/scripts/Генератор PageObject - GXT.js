var controls = new Map();

// в set добавляем ("ТипКонтрола", "xpath для поиска контролов", "xpath для поиска текста внутри контрола")
controls.set("InlineButton", [".//div[contains(@class,'-ButtonStyle-text')]/ancestor::div[contains(@class,'-inlineBlock')]", ".//div[contains(@class,'-ButtonStyle-text')]"]);
controls.set("Radio", [".//label[contains(@class,'RadioStyle')]/ancestor::div[contains(@class,'RadioStyle-wrap')]", ".//label"]);
controls.set("Grid", [".//div[contains(@class,'GridStyle-grid')]",".//div[contains(@class,'Styles-headInner')]/span"]);



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



function getFrame(){
  return document
      .getElementsByTagName('iframe').item(0).getSVGDocument()
      .getElementsByTagName('iframe').item(0).getSVGDocument();
}

var root = getFrame();

function getElementsByXpath(o, xpath){
  let cols = root.evaluate(xpath, o, null, XPathResult.ANY_TYPE, null);
  let col;
  let retvalue = new Array();
  let i=0;
  do{
    col = cols.iterateNext();
    if(col!=null)
      retvalue[i++] = col;

  }while(col);
  return retvalue;
}




function printControls(el){
  for(let key of controls.keys()){
    let elements = getElementsByXpath(el, controls.get(key)[0]);

    for(let i=0; i < elements.length; i ++){
      if(key == 'Grid'){
        console.log('@FindBy(xpath="' + controls.get(key)[0] + '")');
        console.log('public Grid table;');
        console.log('public static class Grid extends GridContainer {');
        printGrid(elements[i], controls.get(key)[1]);
        console.log('\n}');
      }
      else{
        let text = getElementsByXpath(elements[i], controls.get(key)[1])[0].innerText;
        console.log('@Name("' + text + '")');
        console.log('@FindByParametrised(param1 = "' + text + '")');
        console.log('public ' + key + ' ' + getTrimedText(text) + '_' + key + ';');
        console.log('\n');
      }
    }

  }
}

function printGrid(el, xpath){
  let headers = getElementsByXpath(el, xpath);
  for(let i=0; i < headers.length; i++){
    let text = headers[i].innerText;
    if(text == ''){
      console.log('@Name("")');
      console.log('public CheckBoxCell checkbox;');
    }
    else{
      console.log('@Name("' + text + '")');
      console.log('public Cell ' + getTrimedText(text) + ';');
      console.log('\n');
    }
  }
}


function printBlock(el){
  let label = getElementsByXpath(el, ".//div[contains(@class,'Style-inlineLabel')]")[0].innerText;
  console.log('@Name("Блок \'' + label + '\'")');
  console.log('@FindByParametrised(param1 = "' + label + '")');
  console.log('public Block_1 block_1;');
  console.log('public static class Block_1 extends GroupBlockElement {');
  printControls(el);
  console.log('}');
}

function printBlock(el, index){
  let blockClass = "Block_" + index;
  let blockName = "block_" + index;
  let label = getElementsByXpath(el, ".//div[contains(@class,'Style-inlineLabel')]")[0].innerText;
  console.log('@Name("Блок \'' + label + '\'")');
  console.log('@FindByParametrised(param1 = "' + label + '")');
  console.log('public ' + blockClass + ' ' + blockName + ';');
  console.log('public static class ' + blockClass + ' extends GroupBlockElement {');
  printControls(el);
  console.log('}');
}

var blocksXPath = ".//div[contains(@class,'Style-inlineLabel')]//ancestor::div[contains(@class,'Style-fieldLabel')]";
var blocksArray = getElementsByXpath(root, blocksXPath);

for(let i=0; i < blocksArray.length; i++){
  printBlock(blocksArray[i], i + 1);
}