package appli.model.domaine.personnel.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.personnel.bean.SocieteLivrBean;
import appli.model.domaine.personnel.persistant.SocieteLivrPersistant;
import appli.model.domaine.personnel.service.ISocieteLivrService;
import appli.model.domaine.personnel.validator.SocieteLivrValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=SocieteLivrValidator.class)
@Named
public class SocieteLivrService extends GenericJpaService<SocieteLivrBean, Long> implements ISocieteLivrService{
	@Override
	public String generateNumero() {
		int maxNum = 0;
		Query query = getNativeQuery("select max(CAST(numero AS UNSIGNED)) from societe_livraison");
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
	
	@Override
	public List<SocieteLivrPersistant> getSocieteLivrsActifs() {
		List<SocieteLivrPersistant> listSocieteLivr = getQuery("from SocieteLivrPersistant where is_disable is null or is_disable=0 "
				+ "order by nom")
					.getResultList();
		
		return listSocieteLivr;
	}

	@Override
	public void addPortefeuille(Long societeLivrId, boolean isSoldeNeg, BigDecimal mttPalafond, BigDecimal taux) {
		SocieteLivrBean clientB = findById(societeLivrId);
		clientB.setIs_solde_neg(isSoldeNeg);
		clientB.setTaux_portefeuille(taux);
		clientB.setPlafond_dette(mttPalafond);
		clientB.setSolde_portefeuille(BigDecimalUtil.ZERO);
		
		getEntityManager().merge(clientB);
	}
}
