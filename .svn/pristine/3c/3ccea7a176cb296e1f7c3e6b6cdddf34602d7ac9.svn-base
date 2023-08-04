package appli.model.domaine.vente.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "menu_composition_detail", indexes={
		@Index(name="IDX_MNU_COMP_DET_FUNC", columnList="code_func")
	})
public class MenuCompositionDetailPersistant extends BasePersistant{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "article_destock_id", referencedColumnName="id")
	private ArticlePersistant opc_article_destock;// Articles à destocker sans proposer dans la caisse
	
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id")
	private ArticlePersistant opc_article;
	
	@ManyToOne
	@JoinColumn(name = "article_inc_id", referencedColumnName="id")
	private ArticlePersistant opc_article_inc;
	
	@ManyToOne
	@JoinColumn(name = "choix_id", referencedColumnName="id")
	private ListChoixPersistant opc_list_choix;
	
	@ManyToOne
	@JoinColumn(name = "famille_id", referencedColumnName="id")
	private FamilleCuisinePersistant opc_famille;
	
	@ManyToOne
	@JoinColumn(name = "menu_id", referencedColumnName="id")
	private MenuCompositionPersistant opc_menu;
	
	@Column(length = 15, scale = 6, precision = 13)
	private  BigDecimal prix;

	@Column(length = 15, scale = 6, precision = 13, nullable = false)
	private  BigDecimal quantite;

	@Column(length = 6)
	private Integer nombre;
	  
	@Transient
	private int idxIhm;
	
	public ArticlePersistant getOpc_article() {
		return opc_article;
	}

	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}

	public MenuCompositionPersistant getOpc_menu() {
		return opc_menu;
	}

	public void setOpc_menu(MenuCompositionPersistant opc_menu) {
		this.opc_menu = opc_menu;
	}

	public BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}

	public int getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(int idxIhm) {
		this.idxIhm = idxIhm;
	}

	public ListChoixPersistant getOpc_list_choix() {
		return opc_list_choix;
	}

	public void setOpc_list_choix(ListChoixPersistant opc_list_choix) {
		this.opc_list_choix = opc_list_choix;
	}

	public BigDecimal getPrix() {
		return prix;
	}

	public void setPrix(BigDecimal prix) {
		this.prix = prix;
	}

	public FamilleCuisinePersistant getOpc_famille() {
		return opc_famille;
	}

	public void setOpc_famille(FamilleCuisinePersistant opc_famille) {
		this.opc_famille = opc_famille;
	}

	public Integer getNombre() {
		return nombre;
	}

	public void setNombre(Integer nombre) {
		this.nombre = nombre;
	}

	public ArticlePersistant getOpc_article_destock() {
		return opc_article_destock;
	}

	public void setOpc_article_destock(ArticlePersistant opc_article_destock) {
		this.opc_article_destock = opc_article_destock;
	}

	public ArticlePersistant getOpc_article_inc() {
		return opc_article_inc;
	}

	public void setOpc_article_inc(ArticlePersistant opc_article_inc) {
		this.opc_article_inc = opc_article_inc;
	}
	
}
