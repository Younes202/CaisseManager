package appli.model.domaine.caisse_restau.service.impl;

import javax.inject.Named;

import appli.controller.domaine.caisse_restau.bean.TokenBean;
import appli.model.domaine.caisse_restau.service.ITokenService;
import appli.model.domaine.caisse_restau.validator.TokenValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=TokenValidator.class)
@Named
public class TokenService extends GenericJpaService<TokenBean, Long> implements ITokenService {

	@Override
	public void activerDesactiver(Long tokenId) {
		TokenBean tokenBean = findById(tokenId);
		tokenBean.setIs_actif(tokenBean.getIs_actif() ? false : true);
		//
		update(tokenBean);
	}
}
