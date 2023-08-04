package appli.model.domaine.stock.persistant.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.model.common.util.BigDecimalUtil;
 
@Entity
@IdClass(ArticleStockPK.class)
@Table(name = "article_stock_view")
public class ArticleEtatStockView implements Serializable {
	@Id
	@Column
	private Long article_id; 
	@Id
	@Column
    private Long emplacement_id;
	
	@Column
	private String emplacement_titre;
	@Column
	private String article_code;
	@Column
	private String article_lib;
	@Column
	private BigDecimal seuil;
	@Column
	private Integer famille_bleft;
	@Column
	private Long famille_id;
	@Column
	private String famille_code;
	@Column
	private String famille_lib;
	@Column
    private BigDecimal qte_entree;
	@Column
    private BigDecimal qte_sortie;
	@Column
	private BigDecimal prix_achat_ht;
	@Column
	private BigDecimal prix_achat_ttc;
	@Column
	private BigDecimal taux_tva;
	@Column
	private BigDecimal mtt_tva;
	@Column
	private BigDecimal prix_achat_moyen_ht;
	@Column
	private Long abonne_id;
	@Column
	private Long societe_id;
	@Column
	private Long etablissement_id;
	@Transient
	private List<FamillePersistant> familleStr;// Arborescence de la famille 
		
	public Long getArticle_id() {
		return article_id;
	}
	public void setArticle_id(Long article_id) {
		this.article_id = article_id;
	}
	public String getArticle_code() {
		return article_code;
	}
	public void setArticle_code(String article_code) {
		this.article_code = article_code;
	}
	public String getArticle_lib() {
		return article_lib;
	}
	public void setArticle_lib(String article_lib) {
		this.article_lib = article_lib;
	}
	public BigDecimal getSeuil() {
		return seuil;
	}
	public void setSeuil(BigDecimal seuil) {
		this.seuil = seuil;
	}
	public Long getFamille_id() {
		return famille_id;
	}
	public void setFamille_id(Long famille_id) {
		this.famille_id = famille_id;
	}
	public String getFamille_lib() {
		return famille_lib;
	}
	public void setFamille_lib(String famille_lib) {
		this.famille_lib = famille_lib;
	}
	public Long getEmplacement_id() {
		return emplacement_id;
	}
	public void setEmplacement_id(Long emplacement_id) {
		this.emplacement_id = emplacement_id;
	}
	public BigDecimal getQte_entree() {
		return qte_entree;
	}
	public void setQte_entree(BigDecimal qte_entree) {
		this.qte_entree = qte_entree;
	}
	public BigDecimal getQte_sortie() {
		return qte_sortie;
	}
	public void setQte_sortie(BigDecimal qte_sortie) {
		this.qte_sortie = qte_sortie; 
	}
	public String getEmplacement_titre() {
		return emplacement_titre;
	}
	public void setEmplacement_titre(String emplacement_titre) {
		this.emplacement_titre = emplacement_titre;
	}
	public BigDecimal getPrix_achat_ht() {
		return prix_achat_ht;
	}
	public void setPrix_achat_ht(BigDecimal prix_achat_ht) {
		this.prix_achat_ht = prix_achat_ht;
	}
	public BigDecimal getTaux_tva() {
		return taux_tva;
	}
	public void setTaux_tva(BigDecimal taux_tva) {
		this.taux_tva = taux_tva;
	}
	public BigDecimal getMtt_tva() {
		return mtt_tva;
	}
	public void setMtt_tva(BigDecimal mtt_tva) {
		this.mtt_tva = mtt_tva;
	}
	public BigDecimal getPrix_achat_moyen_ht() {
		return prix_achat_moyen_ht;
	}
	public void setPrix_achat_moyen_ht(BigDecimal prix_achat_moyen_ht) {
		this.prix_achat_moyen_ht = prix_achat_moyen_ht;
	}
	public BigDecimal getPrix_achat_ttc() {
		return prix_achat_ttc;
	}
	public void setPrix_achat_ttc(BigDecimal prix_achat_ttc) {
		this.prix_achat_ttc = prix_achat_ttc;
	}
	public BigDecimal getQte_restante() {
		return BigDecimalUtil.substract(qte_entree, qte_sortie);
	}
	public List<FamillePersistant> getFamilleStr() {
		return familleStr;
	}
	public void setFamilleStr(List<FamillePersistant> familleStr) {
		this.familleStr = familleStr;
	}
	public Integer getFamille_bleft() {
		return famille_bleft;
	}
	public void setFamille_bleft(Integer famille_bleft) {
		this.famille_bleft = famille_bleft;
	}
	public String getFamille_code() {
		return famille_code;
	}
	public void setFamille_code(String famille_code) {
		this.famille_code = famille_code;
	}
	public Long getEtablissement_id() {
		return etablissement_id;
	}
	public void setEtablissement_id(Long etablissement_id) {
		this.etablissement_id = etablissement_id;
	}
	public Long getAbonne_id() {
		return abonne_id;
	}
	public void setAbonne_id(Long abonne_id) {
		this.abonne_id = abonne_id;
	}
	public Long getSociete_id() {
		return societe_id;
	}
	public void setSociete_id(Long societe_id) {
		this.societe_id = societe_id;
	}
}

class ArticleStockPK implements Serializable { 
    protected Long article_id;
    protected Long emplacement_id;

    public ArticleStockPK() {}

    public ArticleStockPK(Long article_id, Long emplacement_id) {
        this.article_id = article_id;
        this.emplacement_id = emplacement_id;
    }
}