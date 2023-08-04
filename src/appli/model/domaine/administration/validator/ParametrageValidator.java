package appli.model.domaine.administration.validator;

import javax.inject.Named;

import framework.model.common.service.MessageService;

@Named
public class ParametrageValidator {
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		MessageService.addBannerMessage("La suppression est impossible dans ce module.");
	}
	
}
