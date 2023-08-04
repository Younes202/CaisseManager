package appli.model.domaine.caisse.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.beanContext.BasePersistant;

@Entity 
@Table(name = "article_balance", indexes={
		@Index(name="IDX_ART_BAL_FUNC", columnList="code_func"),
		@Index(name="IDX_ART_BAL_CODE", columnList="code")
	})
public class ArticleBalancePersistant extends BasePersistant {
	@Column(length = 50, nullable = false)
	private String code;
	@Column
	private Boolean is_annule;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal poids;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal nbr_piece;
	
	@ManyToOne
	@JoinColumn(name = "famille_stock_id", referencedColumnName="id")
	private ArticlePersistant opc_article;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private UserPersistant opc_user;
    @ManyToOne
    @JoinColumn(name = "caisse_id", referencedColumnName="id")
    private CaissePersistant opc_caisse;
    @ManyToOne
    @JoinColumn(name = "journee_id", referencedColumnName="id")
    private JourneePersistant opc_journee;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ArticlePersistant getOpc_article() {
		return opc_article;
	}
	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}
	public BigDecimal getPoids() {
		return poids;
	}
	public void setPoids(BigDecimal poids) {
		this.poids = poids;
	}
	public BigDecimal getNbr_piece() {
		return nbr_piece;
	}
	public void setNbr_piece(BigDecimal nbr_piece) {
		this.nbr_piece = nbr_piece;
	}
	public UserPersistant getOpc_user() {
		return opc_user;
	}
	public void setOpc_user(UserPersistant opc_user) {
		this.opc_user = opc_user;
	}
	public Boolean getIs_annule() {
		return is_annule;
	}
	public void setIs_annule(Boolean is_annule) {
		this.is_annule = is_annule;
	}
	public CaissePersistant getOpc_caisse() {
		return opc_caisse;
	}
	public void setOpc_caisse(CaissePersistant opc_caisse) {
		this.opc_caisse = opc_caisse;
	}
	public JourneePersistant getOpc_journee() {
		return opc_journee;
	}
	public void setOpc_journee(JourneePersistant opc_journee) {
		this.opc_journee = opc_journee;
	}
}
