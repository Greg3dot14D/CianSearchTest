var ref = document.location.href;
var modulName = ref.split('moduleName=')[1].split('&')[0]
var formId = ref.split('formId=')[1].split('&')[0]

console.log('@QueryStringParameters(moduleName = "' + modulName + '", formId = "'+ formId.replace('Form', '') +'Table")');
console.log('public Table '+ formId.replace('Form', '') + 'Table;')


// Команды для консоли

// пример отправленного запроса
Window.v = null;
$.post(
		'https://10.116.170.244:9443/azimuth/getForm',
       {
       formId:'incomeMessageListForm',
       moduleName:'cx-leads',
       locale:'ru'
       }, function(data, status, s){ Window.v = s;});

Window.i = 1;

// получаем список строк в виде объектов
var rows = Window.v.responseJSON[1]
.layout[1]
._components[1][Window.i][1]
.model[1]
.rows[1];

// получаем список колонок в виде объектов
var columns =
Window.v.responseJSON[1]
.layout[1]
._components[1][Window.i][1]
.model[1]
.columns[1]

// выводим на экран список заголовков по ключу ('id', 'name')

for(i=0; i < columns.length; i ++)
  //console.log(columns[i][1]['id']);
  console.log(columns[i][1]['name']);

