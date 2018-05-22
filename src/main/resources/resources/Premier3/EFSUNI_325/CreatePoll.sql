declare
  --Row_id Шаблона опронсика
  v_row_id varchar2(15) := '';
  divn_id varchar2(15) := '';
begin
  v_row_id     := s_sequence_pkg.get_next_rowid;
  divn_id      := s_sequence_pkg.get_next_rowid;
insert into cx_poll_tmpl
(
  ROW_ID,-- row_id
  CREATED_BY, --created_by
  LAST_UPD_BY, -- last_upd_by
  NUMBER_OF_PRODUCTS, --Number_of_products
  REV_NUM, -- rev_num
  NAME,
  STATUS,
  HEAD_ID,
  EXP_PERIOD,
  MSG_TEXT,
  PRIORITY
)
values
(
 v_row_id, -- row_id
  'AutoScript', -- created_by
  'AutoScript', -- last_upd_by
  0, --Number_of_products
  1, --rev_num
  'EFSUNI_325', --name
  'Активный', --status
  '1-AX6WCP', --head_id
  null, --exp_period
  'EFSUNI_325 сообщение',--msg_text
  -10-- priority
);

insert into cx_poll_divn
(
  ROW_ID,-- row_id
  CREATED_BY, --created_by
  LAST_UPD_BY, -- last_upd_by
  DIVN_ID, -- Подразделение
  POLL_TMPL_ID-- Id шаблона
)
values
(
  divn_id, -- row_id
  'AutoScript', -- created_by
  'AutoScript', -- last_upd_by
  '%s',
  v_row_id -- Id шаблона
);

commit;
end;