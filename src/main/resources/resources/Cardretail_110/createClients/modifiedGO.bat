rem **************************
rem Настройка подключения
rem **************************
set siebel_login=%2
set siebel_password=%3
set siebel_odbc=%4

rem **************************

cd %1

@echo off
cls

SET NLS_LANG=AMERICAN_AMERICA.CL8MSWIN1251

if exist LOGS rd /s /q LOGS
md LOGS

if exist BADS rd /s /q BADS
md BADS

echo Logs: > LOGS\log_step1.txt

for %%p IN (test_connect create_tables) DO (
	if not exist SQL\%%p.sql (
		echo %time% ERROR! File %%p.sql doesn't exist.
		goto finish
	)
)

echo %time% Testing connecting...
sqlplus -S -L %siebel_login%/%siebel_password%@%siebel_odbc% @SQL\test_connect.sql >> LOGS\log_step1.txt
IF ERRORLEVEL 1 (
	echo %time% ERROR! Connecting failed.
	goto finish
)
echo %time% Success!

echo %time% Creating tables...
sqlplus %siebel_login%/%siebel_password%@%siebel_odbc% @SQL\create_tables.sql >> LOGS\log_step1.txt
echo %time% Tables created!

echo %time% Loading in database...
for %%p IN (AAA_TRSF_CLIENTS) DO (
	if not exist %%p.ctl (
		echo %time% ERROR! File %%p.ctl doesn't exist.
		goto finish
	)

	if not exist %%p.csv (
		echo %time% ERROR! File %%p.csv doesn't exist.
		goto finish
	)

	SQLLDR userid=%siebel_login%/%siebel_password%@%siebel_odbc% control=%%p.ctl  log=LOGS\%%p.log data=%%p.csv bad=BADS\%%p.bad direct=true skip=1 >> LOGS\log_step1.txt
	IF ERRORLEVEL 1 (
		echo %time% WARNING! Not all rows loaded from %%p.csv. Check bad rows in BADS\%%p.bad
	)
)
echo %time% Load is completed!

echo %time% Import Clients...
sqlplus %siebel_login%/%siebel_password%@%siebel_odbc% @SQL\import_clients.sql >> LOGS\log_step1.txt
echo %time% Clients Imported!

:finish
exit 0