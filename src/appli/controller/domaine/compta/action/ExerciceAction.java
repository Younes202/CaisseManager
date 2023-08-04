package appli.controller.domaine.compta.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.Order;

import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.compta.dao.IEcritureDao;
import appli.model.domaine.compta.dao.IExerciceDao;
import appli.model.domaine.compta.service.IExerciceClotureService;
import appli.model.domaine.compta.service.IExerciceService;
import framework.component.complex.table.RequestTableBean;
import framework.component.facade.text.UI_Text;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ContextGloabalAppli.STATUT_EXERCICE;
import framework.controller.annotation.WorkController;
import framework.controller.annotation.WorkForward;
import framework.model.beanContext.ExerciceBean;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.constante.ActionConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;

/**
  *
 */
@WorkController(nameSpace="compta", bean=ExerciceBean.class, jspRootPath="/domaine/compta/")
public class ExerciceAction extends ActionBase {
	@Inject
	private IExerciceService exerciceService;
	@Inject
	private IExerciceClotureService exerciceClotureService;
	@Inject
	private IExerciceDao exerciceDao;
	@Inject
	private IEcritureDao ecritureDao;
	
	//
	private static final String TABLE1_ID = "list_exercice";

	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil){
		List<ExerciceBean> listExercice = exerciceService.findAll();
		boolean isPremierExercice = listExercice.size() == 0;
		// Ajouter l'identifiant
		Long workId = httpUtil.getWorkIdLong();
		if(workId != null){
			httpUtil.setMenuAttribute("exerciceId", workId);
			// Tous les onglet en lecture seule si exercice en vours non ouvert
			ExerciceBean exeBean = exerciceService.findById(workId);
			if(!ContextGloabalAppli.isExerciceOuvert(exeBean)){
				httpUtil.setMenuAttribute("is_read_only", true);
			} else{
				httpUtil.removeMenuAttribute("is_read_only");
			}
		} else{
			if(!httpUtil.isCreateAction()){
				Long exerciceID = (Long) httpUtil.getMenuAttribute("exerciceId");
				// Gérer le retour
				if(exerciceID != null){
					httpUtil.setViewBean(exerciceService.findById(exerciceID));
				}
			}
		}
		
		if(httpUtil.isCreateAction()){
		   	// Purge de la session
		   	httpUtil.removeMenuAttribute("exerciceId");
		}
		
		// Ajouter le retour à la page précédente
		boolean isEditionPage = httpUtil.isEditionPage();
		if(isEditionPage){
			UI_Text textDateDebut = httpUtil.getText("exercice.date_debut");
			UI_Text textDateFin = httpUtil.getText("exercice.date_fin");
			
			if(httpUtil.getAction().equals(ActionConstante.INIT_CREATE)
						|| httpUtil.getAction().equals(ActionConstante.INIT_DUPLIC)
						|| httpUtil.getAction().equals(ActionConstante.INIT_UPDATE)	
					){
				if(isPremierExercice){
					textDateDebut.setReadOnly("false");
					textDateFin.setReadOnly("false");
					
					if(!httpUtil.getAction().equals(ActionConstante.INIT_UPDATE)){
						Date dateDebut = exerciceService.getMaxDateFinPlus1();
						// Date de début initialisée à la plus grande date des anciens exercice
						Calendar calendar = Calendar.getInstance();
						if(dateDebut == null){
							dateDebut = new Date();	
						}
						// Ajouter un an pour avoir la date de fin
						calendar.setTime(dateDebut);
						calendar.add(Calendar.YEAR, 1);
						Date dateFin = calendar.getTime();
						//
						textDateDebut.setValue(dateDebut);
						textDateFin.setValue(dateFin);
					}
				} else{
					textDateDebut.setReadOnly("true");
					textDateFin.setReadOnly("true");
				}
				//
			}
		}
	}

	/**
	 * @param httpUtil
	 */
	public void work_find(ActionUtil httpUtil) {
		// Exercice table
		RequestTableBean cplxTable = httpUtil.getTableBean(TABLE1_ID);
		//
		List<ExerciceBean> listExercice = (List<ExerciceBean>) exerciceService.findByCriteriaByQueryId(cplxTable, "exercice_find");
		Map<Long, Boolean> mapEtatExercice = new HashMap<Long, Boolean>();
		//
		for (ExercicePersistant exerciceBean : listExercice) {
			List<EcriturePersistant> listEcriture = ecritureDao.getListEcritureByDates(exerciceBean.getDate_debut(), exerciceBean.getDate_fin());
			boolean isEcritureNonOuverture = false;
			for (EcriturePersistant ecriturePersistant : listEcriture) {
				if(!ecriturePersistant.getSource().equals(TYPE_ECRITURE.OUVERTURE.toString())){
					isEcritureNonOuverture = true;
					break;
				}
			}
			if(isEcritureNonOuverture){
				mapEtatExercice.put(exerciceBean.getId(), true);
			}
		}
		httpUtil.setRequestAttribute("mapEtatExercice", mapEtatExercice);
	   	httpUtil.setRequestAttribute("listExercice", listExercice);
	   	
	   	httpUtil.setDynamicUrl("/domaine/compta/exercice_list.jsp");
	}

	@Override
	public void work_create(ActionUtil httpUtil) {
		ExerciceBean exerciceBean = (ExerciceBean) httpUtil.getViewBean();
		exerciceService.create(exerciceBean);
		MessageService.getGlobalMap().put("CURRENT_EXERCICE", exerciceBean);
		//
		work_find(httpUtil);	
	}
	
	/**
	 * @param httpUtil
	 */
	public void work_post(ActionUtil httpUtil){
		ExerciceBean exerciceBean = (ExerciceBean)httpUtil.getViewBean();
		if(exerciceBean != null) {
			ExercicePersistant exercicePrecedent = exerciceDao.getExercicePrecedent(exerciceBean);
			httpUtil.setRequestAttribute("exercicePrecedent", exercicePrecedent);
		}
	   	// On ne peut créer un exercice que si il est le premier dans l'aapli car les suivant sont crées par la clôture
	   	List<ExerciceBean> lisExercice = exerciceService.findAll();
	   	httpUtil.setRequestAttribute("listExercice", lisExercice);
	   	
		// On ne peut plus supprimer un exercice ayant des écritures ni modifier ses dates
		if((exerciceBean != null) && (exerciceBean.getId() != null)){
			List<EcriturePersistant> listEcriture = ecritureDao.getListEcritureByDates(exerciceBean.getDate_debut(), exerciceBean.getDate_fin());
			httpUtil.setRequestAttribute("listEcriture", listEcriture);
			// Si exercice cloturé ou en cours de clôture alors lecture seule 
			boolean isCloture = STATUT_EXERCICE.CLOTURE.getStatut().equals(exerciceBean.getStatut_cloture());//2
			boolean isClotureDefinitif =STATUT_EXERCICE.CLOTURE_DEFINITIF.getStatut().equals( exerciceBean.getStatut_cloture());//3
			
			if(isCloture || isClotureDefinitif){
				httpUtil.setFormReadOnly();
			}
		}
		
		if(httpUtil.isCrudOperationOK()) {
			httpUtil.setUserAttribute("LISTE_EXERCICE", exerciceService.findAll(Order.desc("date_debut")));
		}
	}
	
	/**
	 * @param httpUtil
	 */
	@WorkForward(useBean=false, useFormValidator=false)
	public void cloturer(ActionUtil httpUtil){
		Long exerciceId = httpUtil.getLongParameter("exercice.id");
		ExercicePersistant exercicePersistant =	exerciceDao.findById(exerciceId);
		ExercicePersistant exerciceSuivant = exerciceDao.getExerciceSuivant(exercicePersistant);
		
		String msg = "Cette opération va déclencher la clôture de l'exercice en cours ("+exercicePersistant.getLibelle()+") et en ouvrir un nouveau.";
		
		if(exerciceSuivant != null){
			msg = "Cette opération va déclencher la clôture de l'exercice en cours et le recalcul des écritures de report à nouveau de l'exercice suivant ("+exerciceSuivant.getLibelle()+").";
		}

		boolean isRepondu = MessageService.addDialogConfirmMessage("cloture_dialog", "reg.exercice.cloturer", "Confirmation clôture de l'exercice", msg+".<br>"
				+ "Voulez-vous continuer ?");
		if(isRepondu){
			exerciceClotureService.cloturerExercice(exerciceId);
		}
		
		work_edit(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	@WorkForward(useBean=false, useFormValidator=false)
	public void cloturer_definitif(ActionUtil httpUtil){
		boolean isRepondu = MessageService.addDialogConfirmMessage("cloture_dialog", "reg.exercice.cloturer_definitif", "Confirmation clôture définitive de l'exercice", "Cette opération va déclencher la clôture définitive de l'exercice.<br>"
				+ "Aucun retour en arrière ne sera possible.<br>"
				+ "Voulez-vous continuer ?");
		if(isRepondu){
			Long exerciceId = httpUtil.getLongParameter("exercice.id");
			Long assembleeId = httpUtil.getLongParameter("assemblee.id");

			exerciceClotureService.cloturerDefinitif(exerciceId);
		}
		
		work_edit(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	@WorkForward(useBean=false, useFormValidator=false)
	public void annuler_cloture(ActionUtil httpUtil){
		boolean isRepondu = MessageService.addDialogConfirmMessage("ouvrir_dialog", "reg.exercice.annuler_cloture", "Confirmation de l'annulation", "La clôture sera annulée et ainsi toutes ses opérations.<br>"
				+ "Voulez-vous continuer ?");
		if(isRepondu){
			Long exerciceId = httpUtil.getLongParameter("exercice.id");
			exerciceClotureService.annulerCloture(exerciceId);
			
			if(ContextGloabalAppli.getExerciceBean().getId().equals(exerciceId)){
				ExercicePersistant exercice = exerciceService.findById(exerciceId);
				MessageService.getGlobalMap().put("CURRENT_EXERCICE", exercice);
				
				httpUtil.addJavaScript("window.location.reload();");
			}
		}
		
		work_edit(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	@WorkForward(useBean=false, useFormValidator=false)
	public void ouvrir(ActionUtil httpUtil){
		Long exerciceId = httpUtil.getLongParameter("exercice.id");
		if(exerciceId == null){
			ExercicePersistant exerciceBean = ContextGloabalAppli.getExerciceBean();
			if(exerciceBean != null){
				exerciceId = exerciceBean.getId();
			}
		}
		String msg = "Cette opération va déclencher l'ouverture de l'exercice suivant.";
		boolean isRepondu = MessageService.addDialogConfirmMessage("ouvrir_dialog", "compta.exercice.ouvrir", "Confirmation de l'ouverture", msg+"<br>"
				+ "Voulez-vous continuer ?");
		if(isRepondu){
			exerciceClotureService.ouvrirExercice(exerciceId, true); 
		}
		
		work_find(httpUtil);
	}
	
	/**
	 * @param httpUtil
	 */
	public void exportPDF(ActionUtil httpUtil){
		String tp = httpUtil.getParameter("tp");
		
		Long exerciceId = httpUtil.getWorkIdLong();
		if(exerciceId == null){
			exerciceId = ContextGloabalAppli.getExerciceBean().getId();
		}
	}

	public void getChiffresExercice(ActionUtil httpUtil) {
		Long workId = httpUtil.getWorkIdLong();
		ExercicePersistant exerciceP = exerciceService.getChiffresExercice(workId);
		ExerciceBean exeBean = (ExerciceBean) ServiceUtil.persistantToBean(ExerciceBean.class, exerciceP);
		
		httpUtil.setViewBean(exeBean);
		httpUtil.setDynamicUrl("/domaine/administration/exercice_chiffres_modal.jsp");
	}
	
	/**
	 * @param httpUtil
	 */
	public void switchExercice(ActionUtil httpUtil) {
		Long exeId = Long.valueOf(EncryptionUtil.decrypt(httpUtil.getParameter("exe")));
		MessageService.getGlobalMap().put("CURRENT_EXERCICE", exerciceService.findById(exeId));
		MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Changement exercice", "L'exercice en cours est changé.");
		
		httpUtil.writeResponse("REDIRECT");
	}
	
	/**
	 * @param httpUtil
	 */
	@WorkForward(useBean=false, useFormValidator=false)
	public void filtre_exercice(ActionUtil httpUtil){
		String type = httpUtil.getParameter("type");
		String mode = httpUtil.getParameter("mode");

		if(StringUtil.isTrue(mode)){
			httpUtil.setUserAttribute(type, true);
		} else{
			httpUtil.removeUserAttribute(type);
		}
		if(type.equals("ENCAISS_EXE")){
			httpUtil.setDynamicUrl("reg.encaissement.work_init");
		} else if(type.equals("ECRITURE_EXE")){
			httpUtil.setDynamicUrl("cmp.ecriture.work_find");
		}
	}
}
