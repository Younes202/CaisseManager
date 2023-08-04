package appli.model.domaine.personnel.persistant.paie;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Where;

import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.stock.persistant.TravauxPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.ComptePersistant;

@Entity
@Table(name = "salaire", indexes={
		@Index(name="IDX_SAL_FUNC", columnList="code_func")
	})
@NamedQuery(name="salaire_find", query="from SalairePersistant salaire" +
		" where salaire.opc_employe.id = '[employe_id]' "
		+ "and salaire.opc_travaux.id='[travauxId]' "
		+ "order by salaire.opc_employe.nom, salaire.opc_employe.prenom, salaire.date_debut desc")
public class SalairePersistant extends BasePersistant  {
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_debut;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_fin;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_paiement;

	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal tarif_jour;// Util pour calculer le montant des congés non payé
	@Column(length = 3)
	private Integer nbr_jours;// Nombre jour travaillé

	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal nbr_conge;// Congés payés
	@Column(length = 2)
	private Integer nbr_conge_np;// Nombre congé non payés
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_brut;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_net;

	@Column(length = 120)
	private String indemnite_lib;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mt_indemnite;
	
	@Column(length = 120)
	private String prime_lib;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mt_prime;

	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mt_avance;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mt_reliquat;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mt_pret;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mt_retenue;
	@Lob
	private String descipline;
	
	@Column(length = 255)
	private String commentaire;
	
	@Column(length = 2, nullable = false)
	private Integer mois;
	@Column(length = 4, nullable = false)
	private Integer annee;
	
	/*********************** Liens ************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employe_id", referencedColumnName="id", nullable=false)
	private EmployePersistant opc_employe;
	
	@ManyToOne
	@JoinColumn(name = "travaux_id", referencedColumnName="id")
	private TravauxPersistant opc_travaux;

	@OrderBy("financement_enum")
	@OneToMany
	@JoinColumn(name = "elementId", referencedColumnName = "id", insertable=false, updatable=false)
	@ForeignKey(name = "none")
	@Where(clause = "source='PAIE_SAL'")
	List<PaiementPersistant> list_paiement;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_banq_id", referencedColumnName="id")
	private ComptePersistant opc_compte_banq;
	
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
	public BigDecimal getMontant_brut() {
		return montant_brut;
	}
	public void setMontant_brut(BigDecimal montant_brut) {
		this.montant_brut = montant_brut;
	}
	public BigDecimal getMontant_net() {
		return montant_net;
	}
	public void setMontant_net(BigDecimal montant_net) {
		this.montant_net = montant_net;
	}
	public BigDecimal getNbr_conge() {
		return nbr_conge;
	}
	public void setNbr_conge(BigDecimal nbr_conge) {
		this.nbr_conge = nbr_conge;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public EmployePersistant getOpc_employe() {
		return opc_employe;
	}
	public void setOpc_employe(EmployePersistant opc_employe) {
		this.opc_employe = opc_employe;
	}
	public List<PaiementPersistant> getList_paiement() {
		return list_paiement;
	}
	public void setList_paiement(List<PaiementPersistant> list_paiement) {
		this.list_paiement = list_paiement;
	}
	public Date getDate_paiement() {
		return date_paiement;
	}
	public void setDate_paiement(Date date_paiement) {
		this.date_paiement = date_paiement;
	}
	public Integer getNbr_conge_np() {
		return nbr_conge_np;
	}
	public void setNbr_conge_np(Integer nbr_conge_np) {
		this.nbr_conge_np = nbr_conge_np;
	}
	public BigDecimal getMt_prime() {
		return mt_prime;
	}
	public void setMt_prime(BigDecimal mt_prime) {
		this.mt_prime = mt_prime;
	}
	public BigDecimal getMt_avance() {
		return mt_avance;
	}
	public void setMt_avance(BigDecimal mt_avance) {
		this.mt_avance = mt_avance;
	}
	public BigDecimal getMt_reliquat() {
		return mt_reliquat;
	}
	public void setMt_reliquat(BigDecimal mt_reliquat) {
		this.mt_reliquat = mt_reliquat;
	}
	public BigDecimal getMt_pret() {
		return mt_pret;
	}
	public void setMt_pret(BigDecimal mt_pret) {
		this.mt_pret = mt_pret;
	}
	public Integer getMois() {
		return mois;
	}
	public void setMois(Integer mois) {
		this.mois = mois;
	}
	public Integer getAnnee() {
		return annee;
	}
	public void setAnnee(Integer annee) {
		this.annee = annee;
	}
	public BigDecimal getTarif_jour() {
		return tarif_jour;
	}
	public void setTarif_jour(BigDecimal tarif_jour) {
		this.tarif_jour = tarif_jour;
	}
	public ComptePersistant getOpc_compte_banq() {
		return opc_compte_banq;
	}
	public void setOpc_compte_banq(ComptePersistant opc_compte_banq) {
		this.opc_compte_banq = opc_compte_banq;
	}
	public Integer getNbr_jours() {
		return nbr_jours;
	}
	public void setNbr_jours(Integer nbr_jours) {
		this.nbr_jours = nbr_jours;
	}
	public TravauxPersistant getOpc_travaux() {
		return opc_travaux;
	}
	public void setOpc_travaux(TravauxPersistant opc_travaux) {
		this.opc_travaux = opc_travaux;
	}
	public String getIndemnite_lib() {
		return indemnite_lib;
	}
	public void setIndemnite_lib(String indemnite_lib) {
		this.indemnite_lib = indemnite_lib;
	}
	public BigDecimal getMt_indemnite() {
		return mt_indemnite;
	}
	public void setMt_indemnite(BigDecimal mt_indemnite) {
		this.mt_indemnite = mt_indemnite;
	}
	public String getPrime_lib() {
		return prime_lib;
	}
	public void setPrime_lib(String prime_lib) {
		this.prime_lib = prime_lib;
	}
	public BigDecimal getMt_retenue() {
		return mt_retenue;
	}
	public void setMt_retenue(BigDecimal mt_retenue) {
		this.mt_retenue = mt_retenue;
	}
	public String getDescipline() {
		return descipline;
	}
	public void setDescipline(String descipline) {
		this.descipline = descipline;
	}
}
