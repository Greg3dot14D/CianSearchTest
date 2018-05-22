var sections = document.getElementsByClassName('structure').item(0).getElementsByClassName('section');

for(let i = 0; i < sections.length; i ++){
  //let items = sections[i].getElementsByClassName('item folder');
  //let items = sections[i].getElementsByClassName('item doc');
  let items = sections[i].getElementsByClassName('item');

  console.log('// Section ' + i)
  for(let j =0; j < items.length; j ++){
    console.log('' + items[j].innerText + ' ');
  }
  console.log('\n');
}

// Пункты меню выпадающего списка
var li = document
          .getElementById('goalInstAddForm__goalInstTypeSelectBox-menu')
          .getElementsByTagName('li')

for(i=0; i< li.length; i ++)
  console.log(li[i].textContent);