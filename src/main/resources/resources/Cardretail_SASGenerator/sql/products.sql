select p_as.row_id as product_as_id, -- Идентификатор продукта АП
       p_pr.row_id as product_id, -- Идентификатор продаваемого продукта
       p_pr.detail_type_cd as product_type, -- Тип продаваемого продукта
       p_prx.attrib_01 as procuct_kind, -- Вид продаваемого продукта
       p_pr.name as product_name, -- Название продаваемого продукта
       p_pr.eff_start_dt as procuct_start_dt, --Начало срока действия продаваемого продукта
       p_pr.eff_end_dt as procuct_end_dt, --Окончание срока действия продаваемого продукта
       p_pr.desc_text as product_desc, -- Описание продаваемого продукта
       p_pr.x_source, -- Система источник продаваемого продукта
       p_pr.cary_cost_curcy_cd, -- Валюта продаваемого продукта
       p_pr.x_prod_type_tsm_cd, -- Код типа продукта TSM продаваемого продукта
       p_pr.x_prod_tsm_cd, -- Код продукта TSM продаваемого продукта
       p_pr.x_subprod_tsm_cd, -- Код субпродукта TSM продаваемого продукта
       p_pr.x_subprod_tsm_ver, -- Версия субпродукта TSM продаваемого продукта
       p_as.x_topup_flg as x_topup_as_flg -- Флаг Top-up для АП продукта
  from s_prod_int p_as, -- продукт АП
       s_vod p_asv, -- версионность
       s_iss_sub_obj p_asb, -- запись bundle для продукта АП
       s_prod_int p_pr, -- продаваемый продукт в составе продукта АП
       s_prod_int_x p_prx, -- продаваемый продукт в сотаве продукта АП (таблица расширения)
       s_vod p_prv -- версионность
where p_as.cfg_model_id = p_asv.object_num
   and p_asv.row_id = p_asb.vod_id
   and p_asb.last_vers = 0
   and p_asv.locked_flg <> 'Y'
   and (p_as.x_source != 'SAS' or p_as.x_source is null)
   and p_as.prod_type_cd = 'Bundle'
   and p_pr.row_id = p_asb.sub_obj_id
   and p_prx.par_row_id = p_pr.row_id
   and p_prx.attrib_08 = 'Y'
   and p_pr.cfg_model_id = p_prv.object_num
   and p_prv.locked_flg <> 'Y'
   and p_pr.detail_type_cd  = 'Банковская карта'
   and p_prx.attrib_01 = 'Кредитная карта'
   
   and  sysdate + 10 < p_pr.eff_end_dt
order by p_as.created desc, product_as_id