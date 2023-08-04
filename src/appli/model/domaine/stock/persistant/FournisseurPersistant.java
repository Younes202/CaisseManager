package appli.model.domaine.stock.persistant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import framework.model.beanContext.ComptePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "fournisseur", indexes={
		@Index(name="IDX_FOURN_CODE", columnList="code"),
		@Index(name="IDX_FOURN_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name = "fournisseur_find", query="from FournisseurPersistant fournisseur "
			+ "where fournisseur.id='[fournisseurId]' "
			+ "order by fournisseur.opc_famille.code, fournisseur.opc_famille.libelle, fournisseur.code, fournisseur.libelle"),
	
	@NamedQuery(name = "fournisseur_article_find", query="from MouvementArticlePersistant mouvementArticle "
			+ "where mouvementArticle.opc_mouvement.opc_fournisseur is not null and mouvementArticle.opc_article.id='{articleId}' "
			+ "order by mouvementArticle.opc_mouvement.opc_fournisseur.code")
})
public class FournisseurPersistant extends PersonneBasePersistant {
	@Column(length = 20, nullable = false)
	private String code;

	@Column(length = 80, nullable = false)
	private String libelle;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", referencedColumnName="id")
	private ComptePersistant opc_compte;
	
	@GsonExclude
	@Transient
	private List<FamillePersistant> familleStr;// Arborescence de la famille
	
	/*********************** Liens ************************************/
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", referencedColumnName="id")
	private ComptePersistant opc_compte;*/
	@ManyToOne
	@JoinColumn(name = "famille_id", referencedColumnName="id", nullable=false)
	private FamilleFournisseurPersistant opc_famille;
	
	@GsonExclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fournisseur_id", referencedColumnName = "id")
    List<FournisseurContactPersistant> list_contact;
    
    @GsonExclude
    @OneToMany
	@JoinColumn(name="fournisseur_id", referencedColumnName="id", insertable=false, updatable=false)
	List<MouvementPersistant> list_mouvement;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<FamillePersistant> getFamilleStr() {
		return familleStr;
	}

	public void setFamilleStr(List<FamillePersistant> familleStr) {
		this.familleStr = familleStr;
	}

	/*public ComptePersistant getOpc_compte() {
		return opc_compte;
	}

	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}*/

	public FamilleFournisseurPersistant getOpc_famille() {
		return opc_famille;
	}

	public void setOpc_famille(FamilleFournisseurPersistant opc_famille) {
		this.opc_famille = opc_famille;
	}

	public List<FournisseurContactPersistant> getList_contact() {
		return list_contact;
	}

	public void setList_contact(List<FournisseurContactPersistant> list_contact) {
		this.list_contact = list_contact;
	}

	public List<MouvementPersistant> getList_mouvement() {
		return list_mouvement;
	}

	public void setList_mouvement(List<MouvementPersistant> list_mouvement) {
		this.list_mouvement = list_mouvement;
	}

	public ComptePersistant getOpc_compte() {
		return opc_compte;
	}

	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}
}
