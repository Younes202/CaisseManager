package appli.model.domaine.administration.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.administration.persistant.EtatFinanceDetailPersistant;
import appli.model.domaine.administration.persistant.EtatFinancePaiementPersistant;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.stock.dao.IArticleDao;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@Named
@WorkModelClassValidator(validator=EtatFinanceValidator.class)
public class EtatFinanceService extends GenericJpaService<EtatFinanceBean, Long> implements IEtatFinanceService{
	
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private ICompteBancaireService banqueService;
	@Inject
	private IArticleDao articleDao;
	@Inject
	private ICompteBancaireService compteBancaireService;
	
	@Override
	public EtatFinanceBean getEtatByDate(Date dateDebut) {
		EtatFinancePersistant etatPers = (EtatFinancePersistant)mouvementService.getSingleResult(mouvementService.getQuery("from EtatFinancePersistant where date_etat=:dateDebut")
			.setParameter("dateDebut", dateDebut));
		
		if(etatPers != null) {
			return (EtatFinanceBean) ServiceUtil.persistantToBean(EtatFinanceBean.class, etatPers);
		}
		return null;
	}
	
	
	
	@Override
	public EtatFinanceBean getEtatFinanceBean(Date dateDebut, Date dateFin){
		List<EtatFinancePaiementPersistant> listP = new ArrayList<EtatFinancePaiementPersistant>();
		List<Object[]> result = null;
		EtatFinanceBean etatFinanceBean = new EtatFinanceBean();
		etatFinanceBean.setList_detail(new ArrayList<>());
		etatFinanceBean.setList_paiement(listP);
		
		/*********************************************** VENTE CAISSE ****************************************************/
		if(!"erp".equals(StrimUtil.getGlobalConfigPropertie("context.soft"))){ 
			// Total net et clôture des ventes caisse --------------------------------------------------------
			Object[] mtt_vente_caisse = (Object[]) mouvementService.getSingleResult(mouvementService.getNativeQuery(
					"select * from ( "
					+ "(select sum(mtt_total_net) as mtt_net, "
						+ " sum(mtt_cloture_caissier-mtt_ouverture) as mtt_cloture "
					+ "from journee where date_journee>=:dateDebut and date_journee<=:dateFin "+getEtsCondition("", true)+") jr1, "
					+ "(select count(0) as nbrLiv, "
						+ "sum(nbr_livraison*tarif_livraison) as mttLiv,"
					+ "sum(nbr_livraison*tarif_livraison_part) as mttLivSoc "
					+ "from journee where date_journee>=:dateDebut and date_journee<=:dateFin and nbr_livraison is not null and nbr_livraison!=0"+getEtsCondition("", true)+") jr2"
					+ ")")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
			
			etatFinanceBean.setMtt_vente_caisse((BigDecimal) mtt_vente_caisse[0]);
			etatFinanceBean.setMtt_vente_caisse_cloture((BigDecimal) mtt_vente_caisse[1]);
			etatFinanceBean.setNbr_livraison(((BigInteger) mtt_vente_caisse[2]).intValue());
			etatFinanceBean.setMtt_livraison((BigDecimal) mtt_vente_caisse[3]);
			etatFinanceBean.setMtt_livraison_part((BigDecimal) mtt_vente_caisse[4]);
			
			
			// Detail des paiements
			Object[] detailPaie = (Object[]) mouvementService.getSingleResult(mouvementService.getQuery("SELECT " +
					"sum(mtt_cloture_caissier_espece-mtt_ouverture), " +
					"sum(mtt_espece), "+
					"sum(mtt_cloture_caissier_cb), "+
					"sum(mtt_cb), "+
					"sum(mtt_cloture_caissier_cheque), sum(mtt_cheque), " +
					"sum(mtt_cloture_caissier_dej), sum(mtt_dej) "+
					"FROM JourneePersistant "+
					"where date_journee>=:dateDebut and date_journee<=:dateFin"+getEtsCondition("", false))
					.setParameter("dateDebut", dateDebut)
					.setParameter("dateFin", dateFin)
					);
			
			if(detailPaie != null) {
				EtatFinancePaiementPersistant efpP = new EtatFinancePaiementPersistant();
				efpP.setType("vc");
				efpP.setMtt_espece((BigDecimal)detailPaie[0]);
				efpP.setMtt_carte((BigDecimal)detailPaie[2]);
				efpP.setMtt_cheque((BigDecimal)detailPaie[4]);
				efpP.setMtt_dej((BigDecimal)detailPaie[6]);
				efpP.setMtt_cheque_f(BigDecimalUtil.ZERO);
				listP.add(efpP);
			}
		} else{
			// Detail des paiements
			Object[] detailPaie = (Object[]) mouvementService.getSingleResult(mouvementService.getNativeQuery("SELECT " +
					"sum(mtt_donne), "+
					"sum(mtt_donne_cb), "+
					"sum(mtt_donne_cheque), " +
					"sum(mtt_commande_net) " +
					"FROM caisse_mouvement "+
					"where date_vente>=:dateDebut and date_vente<=:dateFin"+getEtsCondition("", true))
					.setParameter("dateDebut", dateDebut)
					.setParameter("dateFin", dateFin)
					);
			if(detailPaie != null) {
				EtatFinancePaiementPersistant efpP = new EtatFinancePaiementPersistant();
				efpP.setType("v");
				efpP.setMtt_espece((BigDecimal)detailPaie[0]);
				efpP.setMtt_carte((BigDecimal)detailPaie[1]);
				efpP.setMtt_cheque((BigDecimal)detailPaie[2]);
				efpP.setMtt_cheque_f(BigDecimalUtil.ZERO);
				listP.add(efpP);
			}
		}
		
//		/*********************************************** TVA ****************************************************/
//		boolean isTVA =  StringUtil.isTrue(ContextAppli.getGlobalConfig("CALCUL_TVA_ETAT_FIN"));
//		if(isTVA){
//			BigDecimal ecartTva = getMttEtatTva(dateDebut, dateFin);
//			etatFinanceBean.setMtt_ecart_tva(ecartTva);
//		}
		
		/*********************************************** DEPENSE ****************************************************/
		String typeOpr = "'ASSURANCE','INCIDENT','VIDANGE','VIGNETTE','VISITE','DEPENSE','CARBURANT'";
		// Dépenses, CHÈQUES depenses de ce mois non encaissé, encaissé ce mois --------------------------------------------------------------
		BigDecimal totalDep = null;
		
		Object[] mtt_depense = (Object[]) mouvementService.getSingleResult(mouvementService.getNativeQuery(
				"select * from ( "
				+ "(select sum(montant) as mt1 from assurance mvm where mvm.date_paiement>=:dateDebut and mvm.date_paiement<=:dateFin"+getEtsCondition("mvm", true)+") mv1, "
				+ "(select sum(montant) as mt2 from incident mvm where mvm.date_incident>=:dateDebut and mvm.date_incident<=:dateFin"+getEtsCondition("mvm", true)+") mv2, "
				+ "(select sum(montant_total) as mt3 from vidange mvm where mvm.date_passage>=:dateDebut and mvm.date_passage<=:dateFin"+getEtsCondition("mvm", true)+") mv3, "
				+ "(select sum(montant) as mt4 from vignette mvm where mvm.date_paiement>=:dateDebut and mvm.date_paiement<=:dateFin"+getEtsCondition("mvm", true)+") mv4, "
				+ "(select sum(montant) as mt5 from visite_technique mvm where mvm.date_paiement>=:dateDebut and mvm.date_paiement<=:dateFin"+getEtsCondition("mvm", true)+") mv5, "
				+ "(select sum(mtt_valide) as mt6 from carburant mvm where mvm.date_passage>=:dateDebut and mvm.date_passage<=:dateFin"+getEtsCondition("mvm", true)+") mv6) "
			).setParameter("dateDebut", dateDebut)
			.setParameter("dateFin", dateFin));
		
		totalDep = BigDecimalUtil.add(totalDep, (BigDecimal)mtt_depense[0], (BigDecimal)mtt_depense[1], (BigDecimal)mtt_depense[2], 
				(BigDecimal)mtt_depense[3], (BigDecimal)mtt_depense[4], (BigDecimal)mtt_depense[5]);
		
		/*********************************************** RECETTE ****************************************************/
		// Recettes, encaissé ce mois, non encaissé --------------------------------------------------------
		Object[] mtt_recette = (Object[]) mouvementService.getSingleResult(mouvementService.getNativeQuery(
				"select * from ( "
				+ "(select sum(montant) as mta1 from charge_divers mvm where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='C'"+getEtsCondition("mvm", true)+") mv1, "
				+ "(select sum(montant) as mta2 from charge_divers mvm where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='D'"+getEtsCondition("mvm", true)+") mvD1, "
				// Chèque non encaissés 
				// RECETTES ***********
				+ "(select sum(montant) as mtb2 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('RECETTE') "
				+ "and date_encaissement is null and mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='C'"+getEtsCondition("mvm", true)+") mv2, "
				// DEPENSES ***********
				+ "(select sum(montant) as mtc2 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') "
				+ "and sens='D' "
				+ "and source in ("+typeOpr+") "
				+ "and date_encaissement is null and mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='D'"+getEtsCondition("mvm", true)+") mvD2, "

				// Chèque encaissé
				// RECETTES ***********
				+ "(select sum(montant) as mtd3 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('RECETTE') "
				+ "and date_encaissement is not null and mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='C'"+getEtsCondition("mvm", true)+") mv3, "
				// DEPENSES ***********
				+ "(select sum(montant) as mte3 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and sens='D' "
				+ "and source in ("+typeOpr+") "
				+ "and date_encaissement is not null and mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='D'"+getEtsCondition("mvm", true)+") mvD3 "
				+ ")")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
		
		etatFinanceBean.setMtt_recette_divers((BigDecimal) mtt_recette[0]);
		etatFinanceBean.setMtt_recette_cheque_non_encais((BigDecimal)mtt_recette[2]);
		etatFinanceBean.setMtt_recette_cheque_encais((BigDecimal)mtt_recette[4]);
		
		etatFinanceBean.setMtt_depense_divers(BigDecimalUtil.add(totalDep, (BigDecimal)mtt_recette[1]));// Ajout des autres depenses
		etatFinanceBean.setMtt_depense_cheque_non_encais((BigDecimal)mtt_recette[3]);
		etatFinanceBean.setMtt_depense_cheque_encais((BigDecimal)mtt_recette[5]);
		
		// Répartition des dépenses
		result = mouvementService.getNativeQuery("SELECT sum(montant), val.code "
				+ "from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and sens='D' "
				+ "and source in ("+typeOpr+") "+getEtsCondition("mvm", true)
				+ "group by mvm.financement_enum")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin)
				.getResultList();
			
		EtatFinancePaiementPersistant dataP = setDataPaiement("d", result);
		if(dataP != null){
			listP.add(dataP);
		}
				
		// Répartition des recette
		result = mouvementService.getNativeQuery("SELECT sum(montant), val.code "
				+ "from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and sens='C' "
				+ "and source in ('RECETTE')"+getEtsCondition("mvm", true)
				+ "group by mvm.financement_enum")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin)
				.getResultList();
		
		dataP = setDataPaiement("r", result);
		if(dataP != null){
			listP.add(dataP);
		}
		
		/*********************************************** VENTE HORS CAISSE + AVOIR ****************************************************/
		// Vente hors caisse, Les chèques vente de ce mois non encaissé,  Les chèques encaissé ce mois des ventes --------------------------------------------------------
		mtt_recette= (Object[]) mouvementService.getSingleResult(mouvementService.getNativeQuery(
				"select * from ("
				+ "(select sum(montant_ttc) as mta1 from mouvement mvm where date_mouvement>=:dateDebut and date_mouvement<=:dateFin and mvm.type_mvmnt='v'"+getEtsCondition("mvm", true)+") mv1,"
				+ "(select sum(montant_ttc) as mta2 from mouvement mvm where date_mouvement>=:dateDebut and date_mouvement<=:dateFin and mvm.type_mvmnt='av'"+getEtsCondition("mvm", true)+") mvAV1,"
				+ "(select sum(montant_ttc-IFNULL(montant_ttc_rem,0)) as mt1 from mouvement mvm where date_mouvement>=:dateDebut and date_mouvement<=:dateFin and mvm.type_mvmnt='a'"+getEtsCondition("mvm", true)+") mvA1, "
				// Chèque non encaissé	
				// VENTES ***********
				+ "(select sum(montant) as mt2 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('VENTE')  "
				+ "and date_encaissement is null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mv2,"
				// AVOIRS ***********
				+ "(select sum(montant) as mtb2 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('AVOIR')  "
				+ "and date_encaissement is null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mvAV2,"
				// ACHATS ***********
				+ "(select sum(montant) as mtb3 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('ACHAT') "
				+ "and date_encaissement is null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mvA2, "
				
				// Chèque encaissés
				// VENTES ***********
				+ "(select sum(montant) as mtb4 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('VENTE') "
				+ "and date_encaissement is not null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mv3, "
				// AVOIRS ***********
				+ "(select sum(montant) as mtb5 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('AVOIR') "
				+ "and date_encaissement is not null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mvAV3, "
				// ACHATS ***********
				+ "(select sum(montant) as mtb6 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('ACHAT') "
				+ "and date_encaissement is not null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mvA3"
				+ ")")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
		
		etatFinanceBean.setMtt_vente_hors_caisse((BigDecimal)mtt_recette[0]);
		etatFinanceBean.setMtt_vente_cheque_non_encais((BigDecimal)mtt_recette[3]);
		etatFinanceBean.setMtt_vente_cheque_encais((BigDecimal)mtt_recette[6]);
		
		etatFinanceBean.setMtt_avoir((BigDecimal)mtt_recette[1]);
		etatFinanceBean.setMtt_avoir_cheque_non_encais((BigDecimal)mtt_recette[4]);
		etatFinanceBean.setMtt_avoir_cheque_encais((BigDecimal)mtt_recette[7]);
		
		etatFinanceBean.setMtt_achat((BigDecimal)mtt_recette[2]);
		etatFinanceBean.setMtt_achat_cheque_non_encais((BigDecimal)mtt_recette[5]);
		etatFinanceBean.setMtt_achat_cheque_encais((BigDecimal)mtt_recette[8]);
		
		// Répartition des ventes hors caisse
		result = mouvementService.getNativeQuery(
				"SELECT sum(montant), val.code from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where mvm.date_mouvement>=:dateDebut " +
				 "and mvm.date_mouvement<=:dateFin " +
				 "and source in ('VENTE')"+getEtsCondition("mvm", true)
				 +"group by mvm.financement_enum"
				)
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin)
				.getResultList();
		
		dataP = setDataPaiement("v", result);
		if(dataP != null){
			listP.add(dataP);
		}
		// Répartition des avoirs
		result = mouvementService.getNativeQuery(
				"SELECT sum(montant), val.code from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
				+ "where mvm.date_mouvement>=:dateDebut " +
				 "and mvm.date_mouvement<=:dateFin " +
				 "and source in ('AVOIR')"+getEtsCondition("mvm", true)
				 +"group by mvm.financement_enum"
				)
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin)
				.getResultList();
		
		dataP = setDataPaiement("av", result);
		if(dataP != null){
			listP.add(dataP);
		}
		// Répartition des achats
		result = mouvementService.getNativeQuery("SELECT sum(montant), val.code "
				+ "from paiement mvm left join val_type_enumere val on mvm.financement_enum=val.id "
				+ "where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin " +
				"and source in ('ACHAT')"+getEtsCondition("mvm", true)
				+"group by mvm.financement_enum")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin)
				.getResultList();
			
		dataP = setDataPaiement("a", result);
		if(dataP != null){
			listP.add(dataP);
		}	
		
		/*********************************************** SALAIRE ****************************************************/
		// Achats hors cheque, Les chèques de ce mois non encaissé, Les chèques encaissé ce mois  des achats --------------------------------------------------------
		Object[] mtt_salaire = (Object[]) mouvementService.getSingleResult(mouvementService.getNativeQuery(
				"select * from ("
				+ "(select sum(montant_net) as mt1 from salaire sal where "
				+ "date_paiement>=:dateDebut and date_paiement<=:dateFin"+getEtsCondition("sal", true)+") sal1, "
				// Chèque non encaissé
				+ "(select sum(montant) as mt2 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
						+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('PAIE_SAL') "
						+ "and date_encaissement is null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mv2, "
				// Chèque encaissé
						+ "(select sum(montant) as mt3 from paiement mvm left join val_type_enumere val on val.id=mvm.financement_enum "
						+ "where (val.code='CHEQUE' or val.code='CHEQUE_F') and source in ('PAIE_SAL') "
						+ "and date_encaissement is not null and date_mouvement>=:dateDebut and date_mouvement<=:dateFin"+getEtsCondition("mvm", true)+") mv3"
				+ ")")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
		
		etatFinanceBean.setMtt_salaire((BigDecimal)mtt_salaire[0]);
		etatFinanceBean.setMtt_salaire_cheque_non_encais((BigDecimal)mtt_salaire[1]);
		etatFinanceBean.setMtt_salaire_cheque_encais((BigDecimal)mtt_salaire[2]);

		// Répartition des salaires
		result = mouvementService.getNativeQuery("SELECT sum(montant), val.code "
				+ "from paiement mvm left join val_type_enumere val on mvm.financement_enum=val.id "
				+ "where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin " +
				"and source in ('PAIE_SAL')"+getEtsCondition("mvm", true)
				+"group by mvm.financement_enum")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin)
				.getResultList();
			
		dataP = setDataPaiement("s", result);
		if(dataP != null){
			listP.add(dataP);
		}

		/*********************************************** STOCK ****************************************************/
		Date finMoisPrecedent = DateUtil.addSubstractDate(dateFin, TIME_ENUM.MONTH, -1);
		Date finMoisEnCours = dateFin;
		//
		List<Object[]> listMttEmplacementPrecedent = articleDao.getMontantStock(finMoisPrecedent);
		List<Object[]> listMttEmplacementEnCours = articleDao.getMontantStock(finMoisEnCours);
		
		for (Object[] objectsP : listMttEmplacementPrecedent) {
			if(objectsP[0] == null){
				continue;
			}
			EtatFinanceDetailPersistant etatP = new EtatFinanceDetailPersistant();
			etatP.setLibelle((String) objectsP[1]);
			etatP.setMtt_etat_prev(BigDecimalUtil.get(""+objectsP[2]));
			etatP.setType("EMPL");
			
			for (Object[] objectsEnC : listMttEmplacementEnCours) {
				if(objectsEnC[0] != null && objectsEnC[0].equals(objectsP[0])){
					etatP.setMtt_etat_actuel(BigDecimalUtil.get(""+objectsEnC[2]));		
				}
			}
			
			etatFinanceBean.getList_detail().add(etatP);
		}

		/*********************************************** COMPTES BANCAIRES ****************************************************/
		List<CompteBancaireBean> listCompteBancaire = banqueService.findAll(Order.asc("libelle"));
		
		for (CompteBancairePersistant compteBanque: listCompteBancaire) {
			EtatFinanceDetailPersistant etatP = new EtatFinanceDetailPersistant();
			etatP.setType("BANQ");
			etatP.setLibelle(compteBanque.getLibelle());
			etatP.setMtt_etat_prev(banqueService.getSoldeCompte(compteBanque.getId(), finMoisPrecedent));
			etatP.setMtt_etat_actuel(banqueService.getSoldeCompte(compteBanque.getId(), finMoisEnCours));
			//
			etatFinanceBean.getList_detail().add(etatP);
		}
		
		BigDecimal soldeStock = null, soldeBanque = null;
		for(EtatFinanceDetailPersistant det : etatFinanceBean.getList_detail()){
			if(det.getType().equals("EMPL")){
				soldeStock = BigDecimalUtil.add(soldeStock, BigDecimalUtil.substract(det.getMtt_etat_actuel(), det.getMtt_etat_prev()));
			}
			if(det.getType().equals("BANQ")){
				soldeBanque = BigDecimalUtil.add(soldeBanque, BigDecimalUtil.substract(det.getMtt_etat_actuel(), det.getMtt_etat_prev()));
			}
		}
		
		BigDecimal soldeChequeRecette = BigDecimalUtil.add(
					BigDecimalUtil.substract(etatFinanceBean.getMtt_recette_cheque_encais(), etatFinanceBean.getMtt_recette_cheque_non_encais()),
					BigDecimalUtil.substract(etatFinanceBean.getMtt_vente_cheque_encais(), etatFinanceBean.getMtt_vente_cheque_non_encais())
				);
		BigDecimal soldeChequeDepense = BigDecimalUtil.add(
					BigDecimalUtil.substract(etatFinanceBean.getMtt_depense_cheque_encais(), etatFinanceBean.getMtt_depense_cheque_non_encais()),
					BigDecimalUtil.substract(etatFinanceBean.getMtt_achat_cheque_encais(), etatFinanceBean.getMtt_achat_cheque_non_encais())
				);
		
		BigDecimal totalRecetteMttCloture = BigDecimalUtil.add(etatFinanceBean.getMtt_vente_caisse_cloture(), etatFinanceBean.getMtt_vente_hors_caisse(), etatFinanceBean.getMtt_recette_divers(), soldeChequeRecette);
		BigDecimal totalDepense = BigDecimalUtil.add(etatFinanceBean.getMtt_achat(), etatFinanceBean.getMtt_salaire(), etatFinanceBean.getMtt_depense_divers(), soldeChequeDepense);
		BigDecimal rsNetCloture = BigDecimalUtil.add(BigDecimalUtil.substract(totalRecetteMttCloture, totalDepense), soldeStock, soldeBanque);
		
		etatFinanceBean.setMtt_resultat_net(rsNetCloture);
		
		return etatFinanceBean;
	}
	
	/**
	 * @param type
	 * @param result
	 * @return
	 */
	@Override
	public EtatFinancePaiementPersistant setDataPaiement(String type, List<Object[]> result){
		if(result.size() == 0){
			return null;
		}
		EtatFinancePaiementPersistant efpP = new EtatFinancePaiementPersistant();
		efpP.setType(type);
		
		for(Object[] detailPaie : result){
			String modePaiement = ""+detailPaie[1];
			BigDecimal montant = (BigDecimal)detailPaie[0];
			//
			if(ContextAppli.MODE_PAIEMENT.CARTE.toString().equals(modePaiement)){
				efpP.setMtt_carte(montant);
			} else if(ContextAppli.MODE_PAIEMENT.CHEQUE.toString().equals(modePaiement)){
				efpP.setMtt_cheque(montant);
			} else if(ContextAppli.MODE_PAIEMENT.DEJ.toString().equals(modePaiement)){
				efpP.setMtt_dej(montant);
			} else if(ContextAppli.MODE_PAIEMENT.ESPECES.toString().equals(modePaiement)){
				efpP.setMtt_espece(montant);
			} else if(ContextAppli.MODE_PAIEMENT.CHEQUE_F.toString().equals(modePaiement)){
				efpP.setMtt_cheque_f(montant);
			} else if(ContextAppli.MODE_PAIEMENT.VIREMENT.toString().equals(modePaiement)){
				efpP.setMtt_virement(montant);
			} else{
				efpP.setMtt_espece(BigDecimalUtil.add(efpP.getMtt_espece(), montant));
			}
		}
		
		return efpP;
	}
	
	@Override
	@WorkModelMethodValidator
	@Transactional
	public void cloreMois(Date dateDebut, Date dateFin) {
		EtatFinanceBean etatBean = getEtatFinanceBean(dateDebut, dateFin);
		etatBean.setDate_etat(dateDebut);
		//
		EtatFinancePersistant efp = new EtatFinancePersistant();
		try {
			PropertyUtils.copyProperties(efp, etatBean);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		efp = mouvementService.getEntityManager().merge(efp);
		
		// Report de solde
		Date dateCloture = DateUtil.addSubstractDate(dateFin, TIME_ENUM.DAY, 1);
		List<CompteBancaireBean> listBanque = compteBancaireService.findAll();
		
		// Report de solde
		for (CompteBancairePersistant compteBancairePersistant : listBanque) {
			BigDecimal soldeCompte = banqueService.getSoldeCompte(compteBancairePersistant.getId());
			soldeCompte = (soldeCompte == null) ? BigDecimalUtil.ZERO : soldeCompte;
			
			EcriturePersistant cbP = new EcriturePersistant();
			cbP.setDate_mouvement(dateCloture);
			cbP.setElementId(efp.getId());
			cbP.setLibelle("Report de solde clôture état");
			cbP.setOpc_banque(compteBancairePersistant);
			cbP.setSens(soldeCompte.compareTo(BigDecimalUtil.ZERO)>=0 ? "C":"D");
			cbP.setSource(TYPE_ECRITURE.CLOTURE_ETAT.toString());
			cbP.setMontant(soldeCompte.abs());
			//
			mouvementService.getEntityManager().merge(cbP);
		}
	}
	
    @Override
	public BigDecimal getMttEtatTva(Date dateDebut, Date dateFin){
		List<MouvementPersistant> listMvmTva = mouvementService.getQuery("from MouvementPersistant mvm "
				+ "where (mvm.type_mvmnt='a' and (mvm.is_facture_comptable is not null and mvm.is_facture_comptable=1)) "
				+ "or mvm.type_mvmnt='v' or mvm.type_mvmnt='vc') "
				+ "and mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin"+getEtsCondition("mvm", false)
				+ "order by mvm.date_mouvement desc")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin)
				.getResultList();
		
			BigDecimal tauxVente = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TVA_VENTE.toString()));
			BigDecimal mttVenteARecupererHT = BigDecimalUtil.ZERO;
			BigDecimal mttVenteADonnerHT = BigDecimalUtil.ZERO;
			BigDecimal mttVenteARecupererTTC = BigDecimalUtil.ZERO;
			BigDecimal mttVenteADonnerTTC = BigDecimalUtil.ZERO;
			
			List<CaisseMouvementPersistant> listCaisseMvm = getQuery("from CaisseMouvementPersistant where date_vente>=:dateDebut and date_vente<=:dateFin order by date_vente desc")
					.setParameter("dateDebut", dateDebut)
					.setParameter("dateFin", dateFin)
					.getResultList();
			Map<String, BigDecimal> mapCaisseMvm = new HashMap<>();
			for (CaisseMouvementPersistant caisseMvmP : listCaisseMvm) {
				if(StringUtil.isNotEmpty(caisseMvmP.getMvm_stock_ids())){
					mapCaisseMvm.put(caisseMvmP.getMvm_stock_ids(), caisseMvmP.getMtt_commande_net());
				}
			}
			
			//
			for (MouvementPersistant mvmP : listMvmTva) {
				if(mvmP.getType_mvmnt().equals(TYPE_MOUVEMENT_ENUM.a.toString())){
					mttVenteARecupererTTC = BigDecimalUtil.add(mttVenteARecupererTTC, mvmP.getMontant_ttc());
					mttVenteARecupererHT = BigDecimalUtil.add(mttVenteARecupererHT, mvmP.getMontant_ht());
				} else if(mvmP.getType_mvmnt().equals(TYPE_MOUVEMENT_ENUM.v.toString())){
					mttVenteADonnerTTC = BigDecimalUtil.add(mttVenteADonnerTTC, mvmP.getMontant_ttc());
					mttVenteADonnerHT = BigDecimalUtil.add(mttVenteADonnerHT, mvmP.getMontant_ht());
				} else if(mvmP.getType_mvmnt().equals(TYPE_MOUVEMENT_ENUM.vc.toString())){
					BigDecimal mttCaisse = null;
					for(String mvmIds : mapCaisseMvm.keySet()) {
						if(mvmIds.indexOf(";"+mvmP.getId()+";") != -1) {
							mttCaisse = mapCaisseMvm.get(mvmIds);
							break;
						}
					}
					BigDecimal mttTva = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttCaisse, tauxVente), BigDecimalUtil.get(100));
					BigDecimal mttHt = BigDecimalUtil.substract(mttCaisse, mttTva);
					
					mttVenteADonnerTTC = BigDecimalUtil.add(mttVenteADonnerTTC, mttCaisse);
					mttVenteADonnerHT = BigDecimalUtil.add(mttVenteADonnerHT, mttHt);
				}
			}
		//
		BigDecimal ecartTva = BigDecimalUtil.substract(BigDecimalUtil.substract(mttVenteADonnerTTC, mttVenteADonnerHT), BigDecimalUtil.substract(mttVenteARecupererTTC, mttVenteARecupererHT));
		
		return ecartTva;
	}
	
	@Override
	@WorkModelMethodValidator
	@Transactional
	public void annulerClotureMois(Long etatId) {
		EtatFinancePersistant efp = (EtatFinancePersistant)mouvementService.findById(EtatFinancePersistant.class, etatId);
		mouvementService.getEntityManager().remove(efp);
		
		// Suppression écriture clôture
		mouvementService.getQuery("delete from EcriturePersistant where elementId=:elementId and source=:source")
			.setParameter("elementId", etatId)
			.setParameter("source", "CLOTURE_ETAT")
			.executeUpdate();
	}
	
	@Override
	public EtatFinancePersistant getMoisClosNonPurge() {
		List<EtatFinancePersistant> listData = mouvementService.getQuery("from EtatFinancePersistant where (is_purge is null or is_purge=0) order by date_etat asc")
			.getResultList();
		
		return (listData.size()>0) ? listData.get(0) : null;
	}
	
	@Override
	public boolean isMoisClos(Date dateRef) {
		List<EtatFinancePersistant> listData = mouvementService.getQuery("from EtatFinancePersistant where date_etat>=:dateDebutRef and date_etat<=:dateFinRef order by date_etat desc")
				.setParameter("dateDebutRef", dateRef)
				.setParameter("dateFinRef", DateUtil.addSubstractDate(dateRef, TIME_ENUM.MONTH, 1))
				.getResultList();
		
		return listData!=null && listData.size()>0;
	}
}

