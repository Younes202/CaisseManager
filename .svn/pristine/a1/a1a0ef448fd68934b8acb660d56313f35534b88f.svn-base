/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import appli.controller.domaine.stock.bean.FamilleBean;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.caisse.service.IFamille2Service;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.bean.PagerBean;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StrimUtil;
import framework.model.service.GenericJpaService;

@Named
public class Famille2Service extends GenericJpaService<FamilleBean, Long> implements IFamille2Service{
	@Inject
	private IFamilleService familleService;
	
	@Override
    public List<FamillePersistant> getFamilleEnfants(Long famId, Long caisseId) {
		return getFamilleEnfants(famId, caisseId, null);
	}
	@Override
    public List<FamillePersistant> getFamilleEnfants(Long famId, Long caisseId, PagerBean pagerBean) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		FamillePersistant famille = (FamillePersistant) familleService.getQuery("from "+(isRestau ? "FamilleCuisinePersistant":"FamilleStockPersistant")+" where id=:id")
				.setParameter("id", famId).getSingleResult();
		
		CaissePersistant currCaisse = (CaissePersistant)familleService.findById(CaissePersistant.class, caisseId);
		boolean isCaisseSpecifique = BooleanUtil.isTrue(currCaisse.getIs_specifique());
		
        String req = "from "+(isRestau ? "FamilleCuisinePersistant":"FamilleStockPersistant")+" where b_left>:bLeft and b_right<:bRight and code!='ROOT' "
        		+ " and (is_disable is null or is_disable=0) ";
        if(isCaisseSpecifique){
        	req = req + " and caisse_target is not null and caisse_target like :currCaisse ";
        } else{
        	req = req + " and (caisse_target is null or caisse_target like :currCaisse) ";
        }
    	req = req + "and (is_noncaisse is null or is_noncaisse = 0) ";
        req = req + " and level=:level order by b_left asc";

        Query query = familleService.getQuery(req)
                .setParameter("bLeft", famille.getB_left())
                .setParameter("bRight", famille.getB_right())
                .setParameter("level", famille.getLevel()+1)
                .setParameter("currCaisse", "%|"+caisseId+"|%");
        
        if(pagerBean != null){
        	query.setFirstResult(pagerBean.getStartIdx());
        	query.setMaxResults(pagerBean.getElementParPage());
        }
        
        return query.getResultList();
    }

}

