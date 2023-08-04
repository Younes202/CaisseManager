/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse_restau.service.impl;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.caisse_restau.bean.ListChoixBean;
import appli.model.domaine.caisse_restau.dao.IListChoixDao;
import appli.model.domaine.caisse_restau.service.IListChoixService;
import appli.model.domaine.caisse_restau.validator.ListChoixValidator;
import appli.model.domaine.vente.persistant.ListChoixPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@Named
@WorkModelClassValidator(validator=ListChoixValidator.class)
public class ListChoixService extends GenericJpaService<ListChoixBean, Long> implements IListChoixService{
	@Inject
	IListChoixDao listChoixDao;
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long istChoixId) {
		ListChoixPersistant istChoixPersistant = listChoixDao.findById(istChoixId);
		istChoixPersistant.setIs_disable(BooleanUtil.isTrue(istChoixPersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(istChoixPersistant);
	}

	@Override
	public String genererCode() {
		int maxNum = 0;
		Query query = getNativeQuery("select max(CAST(code AS UNSIGNED)) from list_choix");
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

