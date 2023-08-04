package framework.model.beanContext;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "exercice", indexes={
		@Index(name="IDX_EXE_FUNC", columnList="code_func")
	})
@NamedQuery(name="exercice_find", query="from ExercicePersistant exercice order by exercice.date_debut desc")
public class ExercicePersistant extends BasePersistant {
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_debut;

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_fin;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_approbation;// Cas approbation des comptes
	
	@Column
	private Boolean is_cloture;
	
	@Column(length = 50, nullable = false)
	private String libelle;

	@Column(length = 1)
	private String etat;

	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_salaire;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_vente_hors_caisse;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_achat;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_vente_caisse;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_recette_divers;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_depense_divers;
	
	@Column(length = 255)
	private String commentaire;
	
	// Traitement inital pour les montants d'initialisation
	@Column(length = 255)
	private String initial_dest;
	
	@Column(length = 3)
	private String statut_cloture;

	@Column(length = 12, scale = 2, precision = 13) 
	private BigDecimal montant_avance;	
	
	public Date getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}

	public Date getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
	
	public boolean isExerciceOuvert(){
		return "O".equals(etat);
	}

	public BigDecimal getMtt_salaire() {
		return mtt_salaire;
	}

	public void setMtt_salaire(BigDecimal mtt_salaire) {
		this.mtt_salaire = mtt_salaire;
	}

	public BigDecimal getMtt_vente_hors_caisse() {
		return mtt_vente_hors_caisse;
	}

	public void setMtt_vente_hors_caisse(BigDecimal mtt_vente_hors_caisse) {
		this.mtt_vente_hors_caisse = mtt_vente_hors_caisse;
	}

	public BigDecimal getMtt_achat() {
		return mtt_achat;
	}

	public void setMtt_achat(BigDecimal mtt_achat) {
		this.mtt_achat = mtt_achat;
	}

	public BigDecimal getMtt_vente_caisse() {
		return mtt_vente_caisse;
	}

	public void setMtt_vente_caisse(BigDecimal mtt_vente_caisse) {
		this.mtt_vente_caisse = mtt_vente_caisse;
	}

	public BigDecimal getMtt_recette_divers() {
		return mtt_recette_divers;
	}

	public void setMtt_recette_divers(BigDecimal mtt_recette_divers) {
		this.mtt_recette_divers = mtt_recette_divers;
	}

	public BigDecimal getMtt_depense_divers() {
		return mtt_depense_divers;
	}

	public void setMtt_depense_divers(BigDecimal mtt_depense_divers) {
		this.mtt_depense_divers = mtt_depense_divers;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getInitial_dest() {
		return initial_dest;
	}

	public void setInitial_dest(String initial_dest) {
		this.initial_dest = initial_dest;
	}

	public String getStatut_cloture() {
		return statut_cloture;
	}

	public void setStatut_cloture(String statut_cloture) {
		this.statut_cloture = statut_cloture;
	}

	public BigDecimal getMontant_avance() {
		return montant_avance;
	}

	public void setMontant_avance(BigDecimal montant_avance) {
		this.montant_avance = montant_avance;
	}

	public Boolean getIs_cloture() {
		return is_cloture;
	}

	public void setIs_cloture(Boolean is_cloture) {
		this.is_cloture = is_cloture;
	}

	public Date getDate_approbation() {
		return date_approbation;
	}

	public void setDate_approbation(Date date_approbation) {
		this.date_approbation = date_approbation;
	}
}
