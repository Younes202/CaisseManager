package appli.model.domaine.caisse.service;

import java.math.BigDecimal;
import java.util.List;

import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;

public class MouvementDetailPrintBean {
    private BigDecimal quantite;
    private BigDecimal mtt_total;
    private String code;
    private String libelle;
    private String type;
    private List<FamillePersistant> listfamilleParent;
    
    public List<FamillePersistant> getListfamilleParent() {
		return listfamilleParent;
	}
	public void setListfamilleParent(List<FamillePersistant> listfamilleParent) {
		this.listfamilleParent = listfamilleParent;
	}
	private FamilleCuisinePersistant famille;
    private CaisseMouvementArticlePersistant menu;
    
    public BigDecimal getQuantite() {
        return quantite;
    }
    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public BigDecimal getMtt_total() {
        return mtt_total;
    }
    public void setMtt_total(BigDecimal mtt_total) {
        this.mtt_total = mtt_total;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }   
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
	public FamilleCuisinePersistant getFamille() {
		return famille;
	}
	public void setFamille(FamilleCuisinePersistant famille) {
		this.famille = famille;
	}
	public CaisseMouvementArticlePersistant getMenu() {
		return menu;
	}
	public void setMenu(CaisseMouvementArticlePersistant menu) {
		this.menu = menu;
	}
}