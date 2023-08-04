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
@Table(name = "list_choix_detail", indexes={
		@Index(name="IDX_LC_DET_FUNC", columnList="code_func")
	})
public class ListChoixDetailPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 13, nullable = false)
	private  BigDecimal quantite;
    
	@Column(length = 6)
	private Integer nombre;
	
    @Transient
	private int idxIhm;
    
    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName="id")
    private ArticlePersistant opc_article;
	
	@ManyToOne
	@JoinColumn(name = "list_choix_id", referencedColumnName="id")
	private ListChoixPersistant opc_list_choix;// Peut se composer d'une autres liste de choix
	
	@ManyToOne
	@JoinColumn(name = "famille_id", referencedColumnName="id")
	private FamilleCuisinePersistant opc_famille;
	
    @ManyToOne
	@JoinColumn(name = "choix_id", referencedColumnName="id")
	ListChoixPersistant opc_choix_parent;// Liste de choix parent

	public BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}

	public ArticlePersistant getOpc_article() {
		return opc_article;
	}

	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}

	public ListChoixPersistant getOpc_list_choix() {
		return opc_list_choix;
	}

	public void setOpc_list_choix(ListChoixPersistant opc_list_choix) {
		this.opc_list_choix = opc_list_choix;
	}

	public FamilleCuisinePersistant getOpc_famille() {
		return opc_famille;
	}

	public void setOpc_famille(FamilleCuisinePersistant opc_famille) {
		this.opc_famille = opc_famille;
	}

	public ListChoixPersistant getOpc_choix_parent() {
		return opc_choix_parent;
	}

	public void setOpc_choix_parent(ListChoixPersistant opc_choix_parent) {
		this.opc_choix_parent = opc_choix_parent;
	}

	public int getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(int idxIhm) {
		this.idxIhm = idxIhm;
	}

	public Integer getNombre() {
		return nombre;
	}

	public void setNombre(Integer nombre) {
		this.nombre = nombre;
	}
}

