package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;


@Entity
@Table(name = "inventaire_detail", indexes={
		@Index(name="IDX_INV_DET_FUNC", columnList="code_func")
	})
public class InventaireDetailPersistant extends BasePersistant  {
	@Column(length = 80)
	private String motif_ecart;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal qte_theorique;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal qte_reel_0;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal qte_reel;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal qte_ecart;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal pourcent_ecart;

	@Column(length = 5)
	private Integer famille_bleft;
	
	/** Données pour l'écran car natif SQl*/
	@Transient
	private Long article_id;
	@Transient
	private  String article_code;
	@Transient
	private String article_lib;
	/*********************** Liens ************************************/
	
	@Transient
	private List<FamillePersistant> familleStr;// Arborescence de la famille 
	
	@GsonExclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventaire_id", referencedColumnName="id")
	private InventairePersistant opc_inventaire;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id", referencedColumnName="id", nullable=false)
	private ArticlePersistant opc_article;

	public String getMotif_ecart() {
		return motif_ecart;
	}

	public void setMotif_ecart(String motif_ecart) {
		this.motif_ecart = motif_ecart;
	}

	public BigDecimal getQte_theorique() {
		return qte_theorique;
	}

	public void setQte_theorique(BigDecimal qte_theorique) {
		this.qte_theorique = qte_theorique;
	}

	public BigDecimal getQte_reel() {
		return qte_reel;
	}

	public void setQte_reel(BigDecimal qte_reel) {
		this.qte_reel = qte_reel;
	}

	public BigDecimal getQte_ecart() {
		return qte_ecart;
	}

	public void setQte_ecart(BigDecimal qte_ecart) {
		this.qte_ecart = qte_ecart;
	}

	public BigDecimal getPourcent_ecart() {
		return pourcent_ecart;
	}

	public void setPourcent_ecart(BigDecimal pourcent_ecart) {
		this.pourcent_ecart = pourcent_ecart;
	}

	public InventairePersistant getOpc_inventaire() {
		return opc_inventaire;
	}

	public void setOpc_inventaire(InventairePersistant opc_inventaire) {
		this.opc_inventaire = opc_inventaire;
	}

	public ArticlePersistant getOpc_article() {
		return opc_article;
	}

	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}

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

	public BigDecimal getQte_reel_0() {
		return qte_reel_0;
	}

	public void setQte_reel_0(BigDecimal qte_reel_0) {
		this.qte_reel_0 = qte_reel_0;
	}
	
}
