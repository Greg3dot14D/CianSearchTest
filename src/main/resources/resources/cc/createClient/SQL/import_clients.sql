TRUNCATE TABLE AAA_TRSF_CLIENTS_IDS;



INSERT INTO AAA_TRSF_CLIENTS_IDS (LAST_NAME, FIRST_NAME, MID_NAME, 
                                  BIRTHDAY, 
                                  DOC_SERIES, DOC_NUMBER,
                                  PARTY_ID, CON_BU_ID, POSTN_CON_ID, PS_CREDEN_ID,
                                  CONTACT_MDMID, DUL_MDMID, EXPER_STAB_ID
                                  )
       (
           SELECT 
				SUBSTR(FULLNAME,1,INSTR(FULLNAME, ' ')-1),
				SUBSTR(FULLNAME, INSTR(FULLNAME, ' ')+1, INSTR(FULLNAME, ' ',1,2)-INSTR(FULLNAME, ' ')-1),
				SUBSTR(FULLNAME, INSTR(FULLNAME, ' ',1,2)+1, LENGTH(FULLNAME)-INSTR(FULLNAME, ' ',1,2)),
                  TO_DATE(BIRTHDAY, 'DD.MM.YYYY'), 
                  DOC_SERIES,
				  DOC_NUMBER,
                  TRIM(s_sequence_pkg.get_next_sbllike_id),
				  TRIM(s_sequence_pkg.get_next_sbllike_id),
				  TRIM(s_sequence_pkg.get_next_sbllike_id),
				  TRIM(s_sequence_pkg.get_next_sbllike_id),	
				  TRIM(s_sequence_pkg.get_next_rowid),
				  TRIM(s_sequence_pkg.get_next_rowid),
				  EXPER_STAB
           FROM AAA_TRSF_CLIENTS
       );
       
COMMIT;


INSERT INTO SIEBEL.S_PARTY (ROW_ID, CREATED, CREATED_BY, LAST_UPD, LAST_UPD_BY, MODIFICATION_NUM, CONFLICT_ID, ADMIN_ADJ_FLG, PARTY_TYPE_CD, PARTY_UID, ROOT_PARTY_FLG, DB_LAST_UPD, DB_LAST_UPD_SRC)
       (
            SELECT PARTY_ID, sysdate, '0-1', sysdate, '0-1', 0, '0', 'N', 'Person', CONTACT_MDMID, 'N', sysdate, 'PLSQL'
            FROM AAA_TRSF_CLIENTS_IDS
       );
       
INSERT INTO SIEBEL.S_CONTACT (ROW_ID, CREATED, CREATED_BY, LAST_UPD, LAST_UPD_BY, DCKING_NUM, MODIFICATION_NUM, CONFLICT_ID, PAR_ROW_ID, ACTIVE_FLG, 
                              BU_ID, COURT_PAY_FLG, DISA_CLEANSE_FLG, DISP_IMG_AUTH_FLG, EMAIL_SR_UPD_FLG, EMP_FLG, FST_NAME, INVSTGTR_FLG, LAST_NAME,
                              PERSON_UID, PO_PAY_FLG, PRIV_FLG, PROSPECT_FLG, PTSHP_CONTACT_FLG, PTSHP_KEY_CON_FLG, SEND_SURVEY_FLG, SPEAKER_FLG, SUPPRESS_EMAIL_FLG,
                              SUPPRESS_FAX_FLG, SUSPECT_FLG, SUSP_WTCH_FLG, AGENT_FLG, ENTERPRISE_FLAG, MEMBER_FLG, OK_TO_SAMPLE_FLG, PROVIDER_FLG, PR_REP_DNRM_FLG,
                              PR_REP_MANL_FLG, PR_REP_SYS_FLG, SEND_FIN_FLG, SEND_NEWS_FLG, SEND_PROMOTES_FLG, SUPPRESS_CALL_FLG, SUPPRESS_MAIL_FLG, BIRTH_DT, DB_LAST_UPD,
                              CUST_STAT_CD, CUST_VALUE_CD, DB_LAST_UPD_SRC, MID_NAME, NATIONALITY, NICK_NAME, PRIVACY_CD, PR_POSTN_ID, SEX_MF, X_SUPP_SMS_FLG, X_ACT_FLG,
                              ATTRIB_10, SBRF_LITERACY, SBRF_RESIDENT, X_SBRF_PAYROLL_CLIENT, X_PB_FLG, X_SBRF_CONTACT_WEIGHT,X_STAB_ID)
       (
           SELECT PARTY_ID, sysdate, '0-1', sysdate, '0-1', 0, 1, '0', PARTY_ID, 'Y', 
                  '0-R9NH', 'N', 'N', 'N', 'N', 'N', UPPER(FIRST_NAME), 'N', UPPER(LAST_NAME), 
                  CONTACT_MDMID, 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N',
                  'N', 'N', 'N', 'N', 'N', 'N', 'Y', 'N', 'N',
                  'N', 'N', 'N', 'N', 'N', 'N', 'N', BIRTHDAY, sysdate,
                  'клиент', 'Массовый', 'PLSQL', UPPER(MID_NAME), 'РОССИЯ', UPPER(LAST_NAME||SUBSTR(FIRST_NAME, 1, 1)||SUBSTR(MID_NAME, 1, 1)), 'Opt-Out: All Parties', '0-5220', 'мужской', 'N', 'Y',
                  'Y', 'Y', 'Y', 'N', 'N', 0 , EXPER_STAB_ID
           FROM AAA_TRSF_CLIENTS_IDS
       );   
       
INSERT INTO SIEBEL.S_CONTACT_BU (ROW_ID, CREATED, CREATED_BY, LAST_UPD, LAST_UPD_BY, MODIFICATION_NUM, CONFLICT_ID, BU_ID, CONTACT_ID, CON_EMP_FLG, CON_FST_NAME, 
                                 CON_LAST_NAME, AGENT_FLG, MEMBER_FLG, PROVIDER_FLG, DB_LAST_UPD, DB_LAST_UPD_SRC)
       (
           SELECT CON_BU_ID, sysdate, '0-1', sysdate, '0-1', 0, '0', '0-R9NH', PARTY_ID, 'N', 'x', 
                  'x', 'N', 'N', 'N', sysdate, 'PLSQL'
           FROM AAA_TRSF_CLIENTS_IDS
       ); 
       
INSERT INTO SIEBEL.S_POSTN_CON (ROW_ID, CREATED, CREATED_BY, LAST_UPD, LAST_UPD_BY, MODIFICATION_NUM, CONFLICT_ID, CON_FST_NAME, CON_ID, CON_LAST_NAME, POSTN_ID,
                                ROW_STATUS, AGENT_FLG, ASGN_DNRM_FLG, ASGN_MANL_FLG, ASGN_SYS_FLG, DOCK_FLG, MEMBER_FLG, PRIORITY_FLG, PROVIDER_FLG, DB_LAST_UPD,
                                DB_LAST_UPD_SRC, X_POSTN_STATUS)
       (
           SELECT POSTN_CON_ID, sysdate, '0-1', sysdate, '0-1', 0, '0', 'x', PARTY_ID, 'x', '0-5220', 
                  'N', 'N', 'N', 'Y', 'N', 'N', 'N', 'N', 'N', sysdate, 
                  'PLSQL', 'Активный'
           FROM AAA_TRSF_CLIENTS_IDS
       );
       
INSERT INTO SIEBEL.S_PS_CREDENTIAL (ROW_ID, CREATED, CREATED_BY, LAST_UPD, LAST_UPD_BY, MODIFICATION_NUM, CONFLICT_ID, CATEGORY_CD, CONTACT_ID, CREDENTIAL_NUM, 
                                    ISSUING_COUNTRY, STATUS_CD, DB_LAST_UPD, ISSUE_TS, DB_LAST_UPD_SRC, X_MDM, X_SBRF_DOC_FIRST_NAME, X_SBRF_DOC_LAST_NAME,
                                    X_SBRF_DOC_MIDDLE_NAME, X_SBRF_DOC_NAME, X_SBRF_DOC_STATUS, X_SBRF_DPT_CD, X_SBRF_ISSUE_PLACE, X_SBRF_NUM, X_SBRF_NUM_DIRTY) 
       (
           SELECT PS_CREDEN_ID, sysdate, '0-1', sysdate, '0-1', 0, '0', 'Passport', PARTY_ID, DUL_MDMID, 
                  'Российская Федерация', 'Active', sysdate, BIRTHDAY + 365 * 20 + 5, 'PLSQL', 'Y', UPPER(FIRST_NAME), UPPER(LAST_NAME), 
                  UPPER(MID_NAME), '21', 'AA', SUBSTR(DUL_MDMID, 3, 3)||'-'||SUBSTR(DUL_MDMID, 6, 3), 'ФМС России', REPLACE(DOC_SERIES, ' ', '')||REPLACE(DOC_NUMBER, ' ', ''), DOC_SERIES||' '||DOC_NUMBER
           FROM AAA_TRSF_CLIENTS_IDS
       );
       
       
INSERT INTO SIEBEL.CX_CONT_INFO (ROW_ID, PERSON_UID, X_STAB_ID, LAST_NAME, FST_NAME, MID_NAME, PASSPORT_SERIES, PASSPORT_NUMBER)
       (
           SELECT PARTY_ID, CONTACT_MDMID, EXPER_STAB_ID ,  LAST_NAME, FIRST_NAME, MID_NAME, REPLACE(DOC_SERIES, ' ', ''), REPLACE(DOC_NUMBER, ' ', '')
           FROM AAA_TRSF_CLIENTS_IDS                                      
       );

COMMIT;

DROP TABLE AAA_13061991_CLIENTS_MDM;
CREATE TABLE AAA_13061991_CLIENTS_MDM AS
       (
           SELECT PARTY_ID, CONTACT_MDMID,EXPER_STAB_ID, LAST_NAME, FIRST_NAME, MID_NAME
           FROM AAA_TRSF_CLIENTS_IDS
       );
       
COMMIT;          

--DROP TABLE AAA_TRSF_CLIENTS_IDS;
--DROP TABLE AAA_TRSF_CLIENTS;     
   
quit;