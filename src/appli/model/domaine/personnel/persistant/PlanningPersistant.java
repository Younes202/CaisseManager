package appli.model.domaine.personnel.persistant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "planning", indexes={
		@Index(name="IDX_PLN_FUNC", columnList="code_func")
	})
@NamedQuery(name="planning_find", query="from PlanningPersistant planning order by planning.date_debut desc, id desc")
public class PlanningPersistant extends BasePersistant implements Serializable {
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_debut;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_fin;
	
	@Column
	private Boolean is_all_day;
	@Column
	private Boolean is_mail;
	@Column
	private Boolean is_to_all;// Anvoi à tous

	@Column(length = 5)
	private String rappel;
	@Column(length = 6)
	private String repetition;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dt_fin_repet; //date fin de répétition
	
	@Column(length = 255, nullable=false)
	private String titre;
	@Column(length = 255)
	private String description;
	@Column(length = 255)
	private String employes_str;
	@Column(length = 255)
	private String clients_str;
	@Column(length = 255)
	private String typePlanning_str;
	@Column(length = 255)
	private String lieu_str;
	@Column(length = 255)
	private String allTypePlanning_str;
	@Column(length = 40)
	private String couleur;
	
	@ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable=false)
    private TypePlanningPersistant opc_type_planning;

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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIs_all_day() {
		return is_all_day;
	}
	public void setIs_all_day(Boolean is_all_day) {
		this.is_all_day = is_all_day;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public TypePlanningPersistant getOpc_type_planning() {
		return opc_type_planning;
	}
	public void setOpc_type_planning(TypePlanningPersistant opc_type_planning) {
		this.opc_type_planning = opc_type_planning;
	}
	public String getEmployes_str() {
		return employes_str;
	}
	public void setEmployes_str(String employes_str) {
		this.employes_str = employes_str;
	}
	public String getRappel() {
		return rappel;
	}
	public void setRappel(String rappel) {
		this.rappel = rappel;
	}
	public Date getDt_fin_repet() {
		return dt_fin_repet;
	}
	public void setDt_fin_repet(Date dt_fin_repet) {
		this.dt_fin_repet = dt_fin_repet;
	}
	public String getRepetition() {
		return repetition;
	}
	public void setRepetition(String repetition) {
		this.repetition = repetition;
	}
	public String getLieu_str() {
		return lieu_str;
	}
	public void setLieu_str(String lieu_str) {
		this.lieu_str = lieu_str;
	}
	public String getClients_str() {
		return clients_str;
	}
	public void setClients_str(String clients_str) {
		this.clients_str = clients_str;
	}
	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	public Boolean getIs_mail() {
		return is_mail;
	}
	public void setIs_mail(Boolean is_mail) {
		this.is_mail = is_mail;
	}
	public Boolean getIs_to_all() {
		return is_to_all;
	}
	public void setIs_to_all(Boolean is_to_all) {
		this.is_to_all = is_to_all;
	}
	public String getTypePlanning_str() {
		return typePlanning_str;
	}
	public void setTypePlanning_str(String typePlanning_str) {
		this.typePlanning_str = typePlanning_str;
	}
	public String getAllTypePlanning_str() {
		return allTypePlanning_str;
	}
	public void setAllTypePlanning_str(String allTypePlanning_str) {
		this.allTypePlanning_str = allTypePlanning_str;
	}
}
