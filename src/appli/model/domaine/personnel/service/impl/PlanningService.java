package appli.model.domaine.personnel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.personnel.bean.PlanningBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.MailQueuePersistant;
import appli.model.domaine.administration.service.IMailUtilService;
import appli.model.domaine.administration.service.impl.MailUtilService;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.PlanningPersistant;
import appli.model.domaine.personnel.service.IPlanningService;
import framework.model.common.util.DateUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@Named
public class PlanningService extends GenericJpaService<PlanningBean, Long> implements IPlanningService{
	@Inject
	private IMailUtilService mailUtilService;
	
	@Override
	public void addMail(PlanningBean planningBean) {
		if(StringUtil.isEmpty(planningBean.getRepetition()) && StringUtil.isNotEmpty(planningBean.getRappel())) {
			String destinataires = "";
			String[] employeIds_str = StringUtil.getArrayFromStringDelim(planningBean.getEmployes_str(), ";");
			for (String employeId_str : employeIds_str) {
				if(!NumericUtil.isNum(employeIds_str)) {
					continue;
				}
				EmployePersistant employeP = (EmployePersistant) findById(EmployePersistant.class, Long.valueOf(employeId_str));
				if(employeP.getMail() != null) {
					destinataires = destinataires + employeP.getMail() + ";";
				}
			}
			
			String type = planningBean.getRappel().substring(0, 1);
			TIME_ENUM type_enum = null;
			if(type.equals("M")) {
				type_enum = TIME_ENUM.MINUTE;
			} else if(type.equals("J")) {
				type_enum = TIME_ENUM.DAY;
			} else if(type.equals("H")) {
				type_enum = TIME_ENUM.HOUR;
			} else if(type.equals("S")){
				type_enum = TIME_ENUM.WEEK;
			}
			
			int duree_rappel = Integer.valueOf(planningBean.getRappel().substring(2));
			
			Date dateMail = DateUtil.addSubstractDate(planningBean.getDate_debut(), type_enum, duree_rappel);
					
			MailQueuePersistant mail = new MailQueuePersistant();
            mail.setExpediteur_nom(ContextAppli.getUserBean().getLogin());
			mail.setMail_signature("ADMIN");
            mail.setDestinataires(destinataires);
            mail.setExpediteur_mail(StrimUtil.getGlobalConfigPropertie("mail.from.noreply"));
            mail.setOrigine_id(planningBean.getId());
            mail.setSource("AGENDA");
            mail.setDate_mail(dateMail);
            
			mail.setDate_creation(new Date());
            
            Map<String, String> params = new HashMap<>();
            params.put("1", planningBean.getDescription());
            
            String msg = MailUtilService.getMailContent(params, "AGENDA");
            
            mail.setSujet(planningBean.getTitre());
            mail.setMessage(msg);
            
            mailUtilService.addMailToQueue(mail);
		}
	}
	
	@Override
	public List<String> checkTableReserved( Date date_db ,Date date_fn) {
		
		String querya = "from PlanningPersistant where ((date_debut>=:date_db and (date_debut<=:date_fn and date_fin>=:date_fn)) "
				+ "or ((date_debut<=:date_db and date_fin>=:date_db) and date_fin<=:date_fn) "
				+ "or (date_debut<=:date_db and date_fin>=:date_fn)  "
				+ "or (date_debut>=:date_db and date_fin<=:date_fn))  ";
	        //	+ "and date_debut>=:currDate";
		Query query =  getQuery(querya)
		.setParameter("date_db", date_db)
		.setParameter("date_fn", date_fn);
	// 	.setParameter("currDate", new Date());
		List<PlanningPersistant> list = query.getResultList();
		List<String> listTable = new ArrayList<>();
			for (PlanningPersistant det : list) {
				String[] data = StringUtil.getArrayFromStringDelim(det.getLieu_str(), ";");
				for(String dt : data) {
					if(!listTable.contains(dt) && dt!=null){
						listTable.add(dt);
					}
				}
			}
		return listTable;
	}
	
	

	@Override
	public List<PlanningPersistant> getResaPostDate() {
		return getQuery("from PlanningPersistant where date_fin>=:currDt order by date_debut asc")
				.setParameter("currDt", new Date())
				.getResultList();
	}
	
}
