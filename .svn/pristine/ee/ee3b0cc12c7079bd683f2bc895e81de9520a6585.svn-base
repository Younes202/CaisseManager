package appli.model.domaine.stock.service.impl;

import java.math.BigInteger;

import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.stock.bean.PreparationBean;
import appli.model.domaine.stock.service.IPreparationService;
import appli.model.domaine.stock.validator.PreparationValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=PreparationValidator.class)
@Named
public class PreparationService extends GenericJpaService<PreparationBean, Long> implements IPreparationService{

	@Override
	public String genererCode() { 
		int maxNum = 0;
		Query query = getNativeQuery("select max(CAST(code AS UNSIGNED)) from preparation");
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
}
