package appli.model.domaine.stock.service;


import java.math.BigDecimal;

import appli.controller.domaine.stock.bean.LotArticleBean;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.service.IGenericJpaService;

public interface ILotArticleService extends IGenericJpaService<LotArticleBean, Long>{

	void manageLotAchat(MouvementPersistant mvmPer);
//	void manageArticleLot(MouvementArticlePersistant mvmArtPer, Long articleId, Long emplacement_id, BigDecimal quantite, String sens);
//	void deleteLot(Long lotId);

	void manageArticleLot(Long articleId, Long emplacement_id, BigDecimal quantite, String sens);

	void deleteLot(Long lotId);
}
