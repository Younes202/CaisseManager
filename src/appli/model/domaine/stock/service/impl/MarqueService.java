package appli.model.domaine.stock.service.impl;

import java.math.BigInteger;

import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.MarqueBean;
import appli.model.domaine.stock.persistant.MarquePersistant;
import appli.model.domaine.stock.service.IMarqueService;
import appli.model.domaine.stock.validator.MarqueValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=MarqueValidator.class)
@Named
public class MarqueService extends GenericJpaService<MarqueBean, Long> implements IMarqueService{

	@Override
	public String genererCode() {
		int maxNum = 0;
		Query query = getNativeQuery("select max(CAST(code AS UNSIGNED)) from marque");
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
	public void activerDesactiverElement(Long marqueId) {
		MarquePersistant marquePersistant = findById(MarquePersistant.class, marqueId);
		marquePersistant.setIs_disable(BooleanUtil.isTrue(marquePersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(marquePersistant);
	}
}
