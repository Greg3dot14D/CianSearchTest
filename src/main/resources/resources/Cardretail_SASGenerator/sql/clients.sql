select ci.*, c.birth_dt, c.cust_value_cd ,

dbms_obfuscation_toolkit.MD5 (input => utl_raw.cast_to_raw(c.LAST_NAME))LAST_NAME_MD5,

dbms_obfuscation_toolkit.MD5 (input => utl_raw.cast_to_raw(c.FST_NAME)) FST_NAME_MD5,

dbms_obfuscation_toolkit.MD5 (input => utl_raw.cast_to_raw(c.MID_NAME)) MID_NAME_MD5,

dbms_obfuscation_toolkit.MD5 (input => utl_raw.cast_to_raw(ci.PASSPORT_SERIES)) PASSPORT_SERIES_MD5,

dbms_obfuscation_toolkit.MD5 (input => utl_raw.cast_to_raw(ci.PASSPORT_NUMBER)) PASSPORT_NUMBER_MD5,

dbms_obfuscation_toolkit.MD5 (input => utl_raw.cast_to_raw(c.birth_dt)) birth_dt_MD5,

case when exists (select 1 from siebel.cx_agree_con ac where ac.con_id = c.row_id) then 'Y' else 'N' end as ZP

from cx_cont_info ci

, s_contact c

where c.row_id = ci.row_id

and ci.last_name is not null

and ci.fst_name is not null

and ci.mid_name is not null

and ci.passport_series is not null and ci.passport_number is not null

and ci.phone_mobile_number is not null

and c.birth_dt is not null

and c.cust_value_cd is not null

and ci.last_name not like '%юю%'

and c.cust_value_cd='ѕремьер'

and rownum<=1000

and c.row_id in ('1-10UKKB','1-SWFEJ','1-2LLQ-9582','1-21RWCI') Ц-—юда вбиваем наши клиенты