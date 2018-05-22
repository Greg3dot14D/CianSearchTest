--VSPUSER637
select
--distinct

  f.*
  , thm.name
  , thm.type
from  (--Поиск тем из кампаний
select
distinct
c.row_id
, c.nick_name
--, c.birth_dt
--, r.x_sbrf_num
--, count(t.name)
from s_contact c
     ,s_ps_credential r
     , cx_sbrf_theme t
where 1=1
      and r.contact_id = c.row_id
      and r.x_sbrf_doc_status = 'AA'
      and r.x_sbrf_doc_name = '21'
--      and c.row_id = '1-2L4Y-5090'


--and c.last_name = 'БРОНЕВАЯ'
and t.contact_id = c.row_id
and c.row_id in (select contact_id
                      from cx_sbrf_theme t
                      where 1=1
                            and t.init_ent_id is not null
                            --and t.suite_item_id is not null -- КК
                            -- статусы--
                             and t.status = 'AN' -- новая
                            --and t.status = 'AW' -- в работе
                            --and t.status <> 'X' -- не закрытая
                            --источники--
                            --and t.source = 'TM'
                            --тип темы
                            --and t.type = 'SAL' - Продажа
                            --and t.type = 'SEG' and t.name = 'Приглашение в Премьер'
                            --and t.type = 'SEG' and t.name = 'Понижение сегмента'
                            and t.position_id is null -- Не распределенная
                        )


) f, cx_sbrf_theme thm

where
f.row_id = thm.contact_id
and thm.status = 'AN'-- не закрытая
and thm.position_id is null -- Не распределенная
and thm.div_id = '%s'
--and thm.init_ent_id is not null
--and thm.suite_item_id is not null -- КК
--group by c.row_id, c.nick_name
--group by c.row_id
--order by c.last_name


--AN новая
--X закрыта
--AW в работе
--для гомера 1-SECI5 подразделение