package appli.model.domaine.stock.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.FournisseurBean;
import appli.model.domaine.stock.dao.IFournisseurDao;
import appli.model.domaine.stock.persistant.FamilleFournisseurPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IFournisseurService;
import appli.model.domaine.stock.validator.FournisseurValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=FournisseurValidator.class)
@Named
public class FournisseurService extends GenericJpaService<FournisseurBean, Long> implements IFournisseurService{
	@Inject
	private IFournisseurDao fournisseurDao;
	@Inject
	private IFamilleService familleService;
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long fournisseurId) {
		FournisseurPersistant fournisseurPersistant = fournisseurDao.findById(fournisseurId);
		fournisseurPersistant.setIs_disable(BooleanUtil.isTrue(fournisseurPersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(fournisseurPersistant);
	}
	
	@Override
	public List<FournisseurPersistant> getListFournisseur(boolean ignoreRoot, boolean onlyActif){
		String req = "from FournisseurPersistant where 1=1 " ;
		if(ignoreRoot){
			req = req + "and opc_famille.code!='ROOT' ";
		}
		if(onlyActif){
			req = req + "and (is_disable is null  or is_disable=0) ";
		}
		req = req + " order by opc_famille.b_left";
		
		return getQuery(req).getResultList();
	}
	
	@Override
	public String genererCode() {
		int maxNum = 0;
		Query query = getNativeQuery("select max(CAST(code AS UNSIGNED)) from fournisseur");
		BigInteger max_num = (BigInteger)query.getSingleResult();
		if(max_num != null){
			maxNum = max_num.intValue();
		}
		String max = "000001";
		
		if(StringUtil.isNotEmpty(maxNum)){
			max = maxNum+1+"";
		}
		while(max.length() != 6){
			max = "0"+max;
		}
		
		return max;
	}

//	@Override
//	public Object[] detailEtatFournisseur(Long fournId) {
//		List<MouvementPersistant> listFourn = getQuery("from MouvementPersistant "
//				+ "where opc_fournisseur.id=:fournId and (type_mvmnt='a' or type_mvmnt='av') "
//				+ "order by date_mouvement desc")
//				.setParameter("fournId", fournId)
//				.getResultList();
//		
//		List<PaiementPersistant> listPaiement = getQuery("from PaiementPersistant "
//				+ "where opc_fournisseur.id=:fournId and (date_encaissement is not null or date_echeance is null) "
//				+ "and source='ACHAT' "
//				+ "order by date_mouvement desc")
//				.setParameter("fournId", fournId)
//				.getResultList();
//		
//		return new Object[]{listFourn, listPaiement};
//	}
	
	@Override
	public void affecterEtatFournisseur(FournisseurPersistant fournisseurP) {
		String req = "select "
				+ "fourn.id, "
				+ "nonpaye.mtt_nonpaye, "
				+ "paye.mtt_paye, "
				+ "avoir.mtt_avoir, "
				+ "m.mtt_mvm "
				
				+ "from fournisseur fourn "
				
				+ "left join ("
				+ "		select sum(pai.montant) as mtt_nonpaye, pai.fournisseur_id as fournisseur_id "
				+ "		from paiement pai "
				+ "		where pai.date_encaissement is null and pai.source='ACHAT' "
				+ "		group by pai.fournisseur_id"
				+ "		) nonpaye on nonpaye.fournisseur_id=fourn.id "
				
				+ "left join ("
				+ "		select sum(pai.montant) as mtt_paye, pai.fournisseur_id as fournisseur_id "
				+ "		from paiement pai "
				+ "		where pai.date_encaissement is not null and pai.source='ACHAT' "
				+ "		group by pai.fournisseur_id"
				+ "		) paye on paye.fournisseur_id=fourn.id "
				
				+ "left join ("
				+ "		select sum(pai.montant) as mtt_avoir, pai.fournisseur_id as fournisseur_id "
				+ "		from paiement pai "
				+ "		where pai.date_encaissement is not null and pai.source='AVOIR' "
				+ "		group by pai.fournisseur_id"
				+ "		) avoir on paye.fournisseur_id=fourn.id "
				
				+ "left join ("
				+ "		select sum(mvm.montant_ttc) as mtt_mvm, mvm.fournisseur_id as fournisseur_id "
				+ "		from mouvement mvm "
				+ "		where mvm.type_mvmnt='a' group by mvm.fournisseur_id "
				+ "		) m on m.fournisseur_id=fourn.id where fourn.id=:fournId";
		
		List<Object[]> list_result = getNativeQuery(req)
				.setParameter("fournId", fournisseurP.getId())
				.getResultList();
		
		for (Object[] object : list_result) {
			fournisseurP.setMtt_non_paye((BigDecimal) object[1]);
			fournisseurP.setMtt_paye((BigDecimal) object[2]);
			fournisseurP.setMtt_avoir((BigDecimal) object[3]);
			fournisseurP.setMtt_total((BigDecimal) object[4]);
		}
	}
	
	@Override
	public List<FournisseurPersistant> getListFournisseurByFamilleCode(String code){
		Set<Long> ids = new HashSet<>();
		
		FamilleFournisseurPersistant famFourn = (FamilleFournisseurPersistant) getSingleResult(getQuery("from FamilleFournisseurPersistant where code=:code")
			.setParameter("code", code));
		
		if(famFourn == null) {
			return null;
		}
		
		List<FamillePersistant> listEnfants = familleService.getFamilleEnfants("FO", famFourn.getId(), false);
		//
		for (FamillePersistant familleP : listEnfants) {
			ids.add(familleP.getId());
		}
		ids.add(famFourn.getId());
		
		String req = "from FournisseurPersistant where opc_famille.id IN (:ids)"
					+ " order by opc_famille.b_left";
		
		return getQuery(req).setParameter("ids", ids).getResultList();
	}
	
	/*
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void create(FournisseurBean e) {
		CompteBean compteFournisseur = compteService.getCompteByCode("4411");
		String codeMaxCompte = compteService.calculateNewCodeCompte(compteFournisseur, 7);
		
		// Creation du compte
		CompteBean compteB = new CompteBean();
		compteB.setCode(codeMaxCompte);
		compteB.setLibelle(e.getLibelle() + StringUtil.getValueOrEmpty(e.getMarque()));
		compteB.setParent_id(compteFournisseur.getId());
		// Creation du compte
		compteService.create(compteB);
		
		e.setOpc_compte(compteB);
		
		// Creation
		super.create(e);
	}*/
}
