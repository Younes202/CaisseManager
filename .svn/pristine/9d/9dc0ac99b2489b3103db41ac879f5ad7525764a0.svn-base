package appli.model.domaine.personnel.service;

import java.math.BigDecimal;
import java.util.List;

import appli.controller.domaine.personnel.bean.SocieteLivrBean;
import appli.model.domaine.personnel.persistant.SocieteLivrPersistant;
import framework.model.service.IGenericJpaService;

public interface ISocieteLivrService extends IGenericJpaService<SocieteLivrBean, Long> {

	String generateNumero();

	List<SocieteLivrPersistant> getSocieteLivrsActifs();
	void addPortefeuille(Long societeLivrId, boolean isSoldeNeg, BigDecimal mttPalafond, BigDecimal taux);

}
