package appli.model.domaine.stock.persistant;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "travaux", indexes={
		@Index(name="IDX_FOURN_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name = "travaux_find", query="from TravauxPersistant travaux "
			+ "order by travaux.opc_chantier.libelle, travaux.libelle"),
})
public class TravauxPersistant extends BasePersistant {
	@Column(length = 120, nullable = false)
	private String libelle;
	
	@Column(length = 120)
	private String lieu;
	
	@Column(length = 120)
	private String employes;// Employés participants : Séparés par des ;
	
	@Column
	@Lob
	private String description;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal budget_prevu;
	
	@Transient
	private  BigDecimal budget_consomme;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_debut;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_fin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chantier_id", referencedColumnName="id")
	private TravauxChantierPersistant opc_chantier;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsable_id", referencedColumnName="id")
	private EmployePersistant opc_responsable;

	@GsonExclude
	@OneToMany
	@JoinColumn(name = "travaux_id", referencedColumnName="id")
	private List<ChargeDiversPersistant> list_depence;
	
    @GsonExclude
    @OneToMany
	@JoinColumn(name="travaux_id", referencedColumnName="id")
	private List<MouvementPersistant> list_achat;

    @GsonExclude
    @OneToMany
	@JoinColumn(name="travaux_id", referencedColumnName="id")
	private List<SalairePersistant> list_salaire;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public BigDecimal getBudget_prevu() {
		return budget_prevu;
	}

	public void setBudget_prevu(BigDecimal budget_prevu) {
		this.budget_prevu = budget_prevu;
	}

	public BigDecimal getBudget_consomme() {
		BigDecimal ttl = null;
		if(this.list_achat != null) {
			for (MouvementPersistant mouvementP : list_achat) {
				ttl = BigDecimalUtil.add(ttl, mouvementP.getMontant_ttc());				
			}
		}
		if(this.list_depence != null) {
			for (ChargeDiversPersistant mouvementP : list_depence) {
				ttl = BigDecimalUtil.add(ttl, mouvementP.getMontant());				
			}			
		}
		if(this.list_salaire != null) {
			for (SalairePersistant mouvementP : list_salaire) {
				ttl = BigDecimalUtil.add(ttl, mouvementP.getMontant_brut());				
			}
		}
		return ttl;
	}

	public void setBudget_consomme(BigDecimal budget_consomme) {
		this.budget_consomme = budget_consomme;
	}

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

	public List<ChargeDiversPersistant> getList_depence() {
		return list_depence;
	}

	public void setList_depence(List<ChargeDiversPersistant> list_depence) {
		this.list_depence = list_depence;
	}

	public List<MouvementPersistant> getList_achat() {
		return list_achat;
	}

	public void setList_achat(List<MouvementPersistant> list_achat) {
		this.list_achat = list_achat;
	}

	public List<SalairePersistant> getList_salaire() {
		return list_salaire;
	}

	public void setList_salaire(List<SalairePersistant> list_salaire) {
		this.list_salaire = list_salaire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TravauxChantierPersistant getOpc_chantier() {
		return opc_chantier;
	}

	public void setOpc_chantier(TravauxChantierPersistant opc_chantier) {
		this.opc_chantier = opc_chantier;
	}

	public String getEmployes() {
		return employes;
	}

	public void setEmployes(String employes) {
		this.employes = employes;
	}

	public EmployePersistant getOpc_responsable() {
		return opc_responsable;
	}

	public void setOpc_responsable(EmployePersistant opc_responsable) {
		this.opc_responsable = opc_responsable;
	}

	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
}
