package appli.model.domaine.compta.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.FournisseurChequePersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.CompteBancairePersistant;

@Entity
@Table(name = "paiement", indexes={
		@Index(name="IDX_PAIE_FUNC", columnList="code_func")
	})

@NamedQueries({
	@NamedQuery(name="list_paiement_echeance", query="from PaiementPersistant paiement "
			+ "where paiement.date_echeance is not null and paiement.date_encaissement is null "
		+ "order by paiement.date_echeance"),
	@NamedQuery(name="cheques_pointes_find", query="from PaiementPersistant paiement "
		+ "where (paiement.opc_financement_enum.code='CHEQUE' or paiement.opc_financement_enum.code='CHEQUE_F') "
		+ "and paiement.date_encaissement is not null and paiement.opc_fournisseur.id='[fournId]' "
		+ "order by paiement.opc_fournisseur.libelle, paiement.date_mouvement desc"),
	@NamedQuery(name="cheques_non_pointes_find", query="from PaiementPersistant paiement "
		+ "where (paiement.opc_financement_enum.code='CHEQUE' or paiement.opc_financement_enum.code='CHEQUE_F') "
		+ "and paiement.date_encaissement is null "
		+ "order by paiement.opc_fournisseur.libelle, paiement.date_mouvement desc")
})
public class PaiementPersistant extends BasePersistant  {
	@Column(length = 200, nullable = false) 
	private String libelle;
	
	@Column(length = 30)
	private String num_cheque;
	
	@Column(length = 1, nullable=false)
	private String sens;
	
	@Column(length = 30)
	private String num_virement;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_echeance;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_encaissement;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_mouvement;
	
	@Column(length = 15, scale = 6, precision = 15, nullable=false)
	private  BigDecimal montant;
	
	@Column(length = 20, nullable=false)
	private Long elementId;
	@Column(length = 30, nullable=false)
	private String source;
	
	@Column(length = 5)
	private Integer groupe_encaiss;// Si les paiement sont effectués par groupe ou lot
	
	
	/*********************** Liens ************************************/
	@ManyToOne
	@JoinColumn(name = "fournisseur_id", referencedColumnName="id")
	private FournisseurPersistant opc_fournisseur;
	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName="id")
	private ClientPersistant opc_client;

	@ManyToOne
	@JoinColumn(name = "banque_id", referencedColumnName="id", nullable=false)
	private CompteBancairePersistant opc_compte_bancaire;
	
	@ManyToOne
	@JoinColumn(name = "financement_enum",   referencedColumnName="id", nullable=false)
	private ValTypeEnumPersistant opc_financement_enum;

	@ManyToOne
	@JoinColumn(name = "cheque_fourn_id",   referencedColumnName="id")
	private FournisseurChequePersistant opc_fournisseurCheque;

	public String getNum_cheque() {
		return num_cheque;
	}

	public void setNum_cheque(String num_cheque) {
		this.num_cheque = num_cheque;
	}

	public Date getDate_echeance() {
		return date_echeance;
	}

	public void setDate_echeance(Date date_echeance) {
		this.date_echeance = date_echeance;
	}

	public Date getDate_encaissement() {
		return date_encaissement;
	}

	public void setDate_encaissement(Date date_encaissement) {
		this.date_encaissement = date_encaissement;
	}

	public Date getDate_mouvement() {
		return date_mouvement;
	}

	public void setDate_mouvement(Date date_mouvement) {
		this.date_mouvement = date_mouvement;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public FournisseurPersistant getOpc_fournisseur() {
		return opc_fournisseur;
	}

	public void setOpc_fournisseur(FournisseurPersistant opc_fournisseur) {
		this.opc_fournisseur = opc_fournisseur;
	}

	public CompteBancairePersistant getOpc_compte_bancaire() {
		return opc_compte_bancaire;
	}

	public void setOpc_compte_bancaire(CompteBancairePersistant opc_compte_bancaire) {
		this.opc_compte_bancaire = opc_compte_bancaire;
	}

	public ValTypeEnumPersistant getOpc_financement_enum() {
		return opc_financement_enum;
	}

	public void setOpc_financement_enum(ValTypeEnumPersistant opc_financement_enum) {
		this.opc_financement_enum = opc_financement_enum;
	}

	public FournisseurChequePersistant getOpc_fournisseurCheque() {
		return opc_fournisseurCheque;
	}

	public void setOpc_fournisseurCheque(FournisseurChequePersistant opc_fournisseurCheque) {
		this.opc_fournisseurCheque = opc_fournisseurCheque;
	}

	public String getNum_virement() {
		return num_virement;
	}

	public void setNum_virement(String num_virement) {
		this.num_virement = num_virement;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

	public ClientPersistant getOpc_client() {
		return opc_client;
	}

	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}

	public Integer getGroupe_encaiss() {
		return groupe_encaiss;
	}

	public void setGroupe_encaiss(Integer groupe_encaiss) {
		this.groupe_encaiss = groupe_encaiss;
	}
}
