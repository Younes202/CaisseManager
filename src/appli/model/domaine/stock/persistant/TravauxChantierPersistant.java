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
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "travaux_chantier", indexes={
		@Index(name="IDX_FOURN_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name = "travauxChantier_find", query="from TravauxChantierPersistant travauxChantier "
			+ "order by travauxChantier.libelle"),
})
public class TravauxChantierPersistant extends BasePersistant {
	@Column(length = 120, nullable = false)
	private String libelle;
	
	@Column(length = 120)
	private String lieu;
	
	@Column
	@Lob
	private String description;
	
	@Transient
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
	@JoinColumn(name = "responsable_id", referencedColumnName="id")
	private EmployePersistant opc_responsable;
	
	@GsonExclude
	@OneToMany
	@JoinColumn(name = "chantier_id", referencedColumnName="id")
	private List<TravauxPersistant> list_travaux;
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public BigDecimal getBudget_prevu() {
		BigDecimal ttl = null;
		if(this.list_travaux != null) {
			for (TravauxPersistant travauxP : list_travaux) {
				ttl = BigDecimalUtil.add(ttl, travauxP.getBudget_prevu());
			}
		}
		return ttl;
	}

	public void setBudget_prevu(BigDecimal budget_prevu) {
		this.budget_prevu = budget_prevu;
	}

	public BigDecimal getBudget_consomme() {
		BigDecimal ttl = null;
		if(this.list_travaux != null) {
			for (TravauxPersistant travauxP : list_travaux) {
				ttl = BigDecimalUtil.add(ttl, travauxP.getBudget_consomme());
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

	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TravauxPersistant> getList_travaux() {
		return list_travaux;
	}

	public void setList_travaux(List<TravauxPersistant> list_travaux) {
		this.list_travaux = list_travaux;
	}

	public EmployePersistant getOpc_responsable() {
		return opc_responsable;
	}

	public void setOpc_responsable(EmployePersistant opc_responsable) {
		this.opc_responsable = opc_responsable;
	}
}
