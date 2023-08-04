package appli.model.domaine.administration.persistant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.beanContext.ExercicePersistant;

@Entity
@Table(name = "compte_bancaire_bordereau", indexes={
		@Index(name="IDX_CPT_BQE_BRD_FUNC", columnList="code_func")
	})
@NamedQuery(name="compteBordereau_find", query="from CompteBancaireBordereauPersistant cbb where cbb.opc_exercice.id='{exercice_id}'" +
		" order by cbb.date, cbb.libelle")
public class CompteBancaireBordereauPersistant extends BasePersistant {
	@Column(length = 50, nullable = false)
	private String libelle;

	@Column(length = 12, scale = 2, precision = 13, nullable=false)
	private BigDecimal montant;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date date;

	@Column(length = 3, nullable=false)
	private Integer nbr_cheque;

	@Column(length = 255)
	private String commentaire;

	/*********************** Liens ************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_bancaire_id", referencedColumnName="id", nullable=false)
	private CompteBancairePersistant opc_compte_bancaire;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exercice_id", referencedColumnName="id", nullable=false)
	private ExercicePersistant opc_exercice;
	
	@OneToMany
	@JoinColumn(name="compte_bordereau_id", referencedColumnName="id", insertable=false, updatable=false)
	List<EcriturePersistant> list_ecriture;

	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public BigDecimal getMontant() {
		return montant;
	}
	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getNbr_cheque() {
		return nbr_cheque;
	}
	public void setNbr_cheque(Integer nbr_cheque) {
		this.nbr_cheque = nbr_cheque;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public CompteBancairePersistant getOpc_compte_bancaire() {
		return opc_compte_bancaire; 
	}
	public void setOpc_compte_bancaire(CompteBancairePersistant opc_compte_bancaire) {
		this.opc_compte_bancaire = opc_compte_bancaire;
	}
	public ExercicePersistant getOpc_exercice() {
		return opc_exercice;
	}
	public void setOpc_exercice(ExercicePersistant opc_exercice) {
		this.opc_exercice = opc_exercice;
	}
	public List<EcriturePersistant> getList_ecriture() {
		return list_ecriture;
	}
	public void setList_ecriture(List<EcriturePersistant> list_ecriture) {
		this.list_ecriture = list_ecriture;
	}
}
