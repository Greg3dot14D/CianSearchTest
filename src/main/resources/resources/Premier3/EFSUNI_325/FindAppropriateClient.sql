SELECT distinct s1.last_name, s1.fst_name, c.x_sbrf_num


FROM

     SIEBEL.s_ps_credential C,
     SIEBEL.S_CONTACT s1,
     SIEBEL.S_PARTY s2,
     SIEBEL.S_POSTN_CON s3,
     SIEBEL.cx_poll T,
     SIEBEL.cx_poll_tmpl K
WHERE
     c.contact_id = s1.row_id AND
     s2.ROW_ID = s1.PAR_ROW_ID AND
     s3.CON_ID = s2.ROW_ID AND
     T.Con_Id = s3.con_id AND
     K.row_id = T.TMPL_ID AND
     K.PRIORITY IS NOT NULL AND
     s1.cust_value_cd = 'Премьер' AND
     K.Status = 'Активный' AND
     (s2.PARTY_TYPE_CD != 'Suspect' AND s1.X_ACT_FLG = 'Y' AND s1.EMP_FLG = 'N') AND
     (s3.POSTN_ID = '%s') AND
     s1.row_id not in

(
       select s4.con_id from cx_poll s4
       where
       s4.status = 'SP'
       and
        to_char(s4.last_upd,'DD.MM.YYYY') = to_char((sysdate-EXTRACT(timezone_hour FROM systimestamp)/24),'DD.MM.YYYY')
)