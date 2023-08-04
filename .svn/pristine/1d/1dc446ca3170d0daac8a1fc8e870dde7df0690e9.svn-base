package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;


@Entity
@Table(name = "preparation", indexes={
		@Index(name="IDX_PREP_FUNC", columnList="code_func"),
		@Index(name="IDX_PREP_CODE", columnList="code")
	})
@NamedQuery(name="preparation_find", query="from PreparationPersistant preparation" +
		" where" + 
		" preparation.code like '[%preparation.code%]' and" + 
		" preparation.libelle like '[%preparation.libelle%]' " + 
		" order by preparation.code, preparation.libelle")
public class PreparationPersistant extends BasePersistant  {
	@Column(length = 80, nullable = false)
	private String libelle;

	@Column(length = 20, nullable = false)
	private String code;
	
	@Column(length = 255)
	private String commentaire;

	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_ht;// Valeur en ht
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_ttc;// Valeur en ttc
	
	/*********************** Liens ************************************/
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "preparation_id", referencedColumnName = "id")
	List<PreparationArticlePersistant> list_article;
	
//	@OneToMany
//	@JoinColumn(name="preparation_id", referencedColumnName="id", insertable=false, updatable=false)
//	List<MouvementPersistant> list_mouvement;
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}	
	public List<PreparationArticlePersistant> getList_article() {
		return list_article;
	}
	public void setList_article(List<PreparationArticlePersistant> list_article) {
		this.list_article = list_article;
	}
//	public List<MouvementPersistant> getList_mouvement() {
//		return list_mouvement;
//	}
//	public void setList_mouvement(List<MouvementPersistant> list_mouvement) {
//		this.list_mouvement = list_mouvement;
//	}
	public BigDecimal getMontant_ht() {
		return montant_ht;
	}
	public void setMontant_ht(BigDecimal montant_ht) {
		this.montant_ht = montant_ht;
	}
	public BigDecimal getMontant_ttc() {
		return montant_ttc;
	}
	public void setMontant_ttc(BigDecimal montant_ttc) {
		this.montant_ttc = montant_ttc;
	}
}
