package appli.model.domaine.stock.validator;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.stock.bean.ChargeDiversBean;
import appli.model.domaine.stock.dao.IChargeDiversDao;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import framework.model.common.service.MessageService;

@Named
public class ChargeDiversValidator {
	@Inject
	private IChargeDiversDao chargeDiversDao;

	/*
	 * 
	 */
	public void updateCreateValidator(ChargeDiversBean chargeDiversBean) {

	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(ChargeDiversBean chargeDiversBean) {
		updateCreateValidator(chargeDiversBean);
	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(ChargeDiversBean chargeDiversBean) {
		updateCreateValidator(chargeDiversBean);
	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id) {
		ChargeDiversPersistant chargeDiversPersistant = chargeDiversDao
				.findById(id);

	}

	public void update_automate(ChargeDiversPersistant cdp, Date dateDebut,
			Date dateFin, Integer frequence) {
		// La date de début doit être inférieure ou égale à la date de fin
		// s’elle est renseignée
		if (dateFin != null) {
			if ( dateDebut.after(dateFin)) {
				MessageService.addFieldMessageKey(
						"chargeDivers.date_debut_auto", "chargeDivers.dates");
			}

		}
		// La fréquence doit être supérieure à 0 et inférieure à 365
		if (frequence < 0
				|| frequence > 365) {
			MessageService.addFieldMessageKey("chargeDivers.frequence",
					"chargeDivers.frequenceMsg");
		}
	}

}
