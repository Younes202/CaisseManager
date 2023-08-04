package appli.controller.domaine.stock.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.controller.bean.action.IViewBean;

public class MouvementBean extends MouvementPersistant implements IViewBean {
	private Long[] mouvementIds;
	private Long[] avoirIds;
	
	private Map<Long, BigDecimal> mapOldArticleInfo; // les articles à mettre à jour dans le stock et leurs quantités
	private String fourn_lib;
	private String destination;
	private String emplacement;
	private String financement;
	private String num_cheque_f;
	private List<MouvementArticlePersistant> listArtTarget;

	public Long[] getMouvementIds() {
		return mouvementIds;
	}

	public void setMouvementIds(Long[] mouvementIds) {
		this.mouvementIds = mouvementIds;
	}

	public Long[] getAvoirIds() {
		return avoirIds;
	}

	public void setAvoirIds(Long[] avoirIds) {
		this.avoirIds = avoirIds;
	}

	public String getFourn_lib() {
		return fourn_lib;
	}

	public void setFourn_lib(String fourn_lib) {
		this.fourn_lib = fourn_lib;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	public String getFinancement() {
		return financement;
	}

	public void setFinancement(String financement) {
		this.financement = financement;
	}

	public String getNum_cheque_f() {
		return num_cheque_f;
	}

	public void setNum_cheque_f(String num_cheque_f) {
		this.num_cheque_f = num_cheque_f;
	}

	public List<MouvementArticlePersistant> getListArtTarget() {
		return listArtTarget;
	}

	public void setListArtTarget(List<MouvementArticlePersistant> listArtTarget) {
		this.listArtTarget = listArtTarget;
	}

	public Map<Long, BigDecimal> getMapOldArticleInfo() {
		return mapOldArticleInfo;
	}

	public void setMapOldArticleInfo(Map<Long, BigDecimal> mapOldArticleInfo) {
		this.mapOldArticleInfo = mapOldArticleInfo;
	}
}
