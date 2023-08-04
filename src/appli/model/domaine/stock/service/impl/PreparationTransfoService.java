package appli.model.domaine.stock.service.impl;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.PreparationTransfoBean;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IPreparationTransfoService;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@Named
public class PreparationTransfoService extends GenericJpaService<PreparationTransfoBean, Long> implements IPreparationTransfoService{
	@Inject
	private IArticleService articleService;
	
	@Override
	public String genererCode() { 
		int maxNum = 0;
	Query query = getNativeQuery("select max(CAST(code AS UNSIGNED)) from preparation_transfo");
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
	@Transactional
	public void merge(PreparationTransfoBean prepTrB, boolean isNewComposant) {
		if(isNewComposant){
			ArticlePersistant artP = prepTrB.getOpc_composant_target().getOpc_composant();
			
			String code = articleService.generateCode(artP.getOpc_famille_stock().getId(), "COMP");
			artP.setCode(code+"_FC");
			
			artP = getEntityManager().merge(artP);
			
			prepTrB.getOpc_composant_target().setOpc_composant(artP);
		}
		
		super.merge(prepTrB);
	}
}
