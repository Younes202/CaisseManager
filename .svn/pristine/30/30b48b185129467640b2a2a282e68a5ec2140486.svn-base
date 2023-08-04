package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;


@Entity
@Table(name = "preparation_transfo", indexes={
		@Index(name="IDX_PREP_TRANS_FUNC", columnList="code_func"),
		@Index(name="IDX_PREP_TRANS_CODE", columnList="code")
	})
@NamedQuery(name="preparationTransfo_find", query="from PreparationTransfoPersistant preparationTransfo" +
		" order by preparationTransfo.code, preparationTransfo.libelle")
public class PreparationTransfoPersistant extends BasePersistant {
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
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "composant_target_id", referencedColumnName="id")
	private PreparationTransfoArticlePersistant opc_composant_target;
	
	/*********************** Liens ************************************/
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "transfo_id", referencedColumnName = "id")
	List<PreparationTransfoArticlePersistant> list_composant;
	
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
	public PreparationTransfoArticlePersistant getOpc_composant_target() {
		return opc_composant_target;
	}
	public void setOpc_composant_target(PreparationTransfoArticlePersistant opc_composant_target) {
		this.opc_composant_target = opc_composant_target;
	}
	public List<PreparationTransfoArticlePersistant> getList_composant() {
		return list_composant;
	}
	public void setList_composant(List<PreparationTransfoArticlePersistant> list_composant) {
		this.list_composant = list_composant;
	}
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
