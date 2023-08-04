package appli.model.domaine.stock.persistant;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.DateUtil;


@Entity
@Table(name = "inventaire", indexes={
		@Index(name="IDX_INV_FUNC", columnList="code_func")
	})
@NamedQuery(name="inventaire_find", query="from InventairePersistant inventaire" +
		" order by inventaire.date_realisation desc, id desc")
public class InventairePersistant extends BasePersistant  {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_realisation;
	
	@Column(length = 255)
	private String commentaire;
	
	@Column
	private Boolean is_valid;
	
	@Transient
	private String date_realisation_toString;

	/*********************** Liens ************************************/
	
	@ManyToOne
	@JoinColumn(name = "emplacement_id", referencedColumnName="id", nullable=false)
	private EmplacementPersistant opc_emplacement;
	
	@ManyToOne
	@JoinColumn(name = "responsable_id", referencedColumnName="id")
	private EmployePersistant opc_responsable;
	
	@ManyToOne
	@JoinColumn(name = "saisisseur_id", referencedColumnName="id")
	private EmployePersistant opc_saisisseur;
	@ManyToOne
	@JoinColumn(name = "saisisseur2_id", referencedColumnName="id")
	private EmployePersistant opc_saisisseur2;
	
	@ManyToOne
	@JoinColumn(name = "type_enum", referencedColumnName="id")
	private ValTypeEnumPersistant opc_type_enum;
	
	@OrderBy("famille_bleft asc")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "inventaire_id", referencedColumnName = "id")
	private List<InventaireDetailPersistant> list_detail;
	
	public Date getDate_realisation() {
		return date_realisation;
	}

	public void setDate_realisation(Date date_realisation) {
		this.date_realisation = date_realisation;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Boolean getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Boolean is_valid) {
		this.is_valid = is_valid;
	}

	public EmplacementPersistant getOpc_emplacement() {
		return opc_emplacement;
	}

	public void setOpc_emplacement(EmplacementPersistant opc_emplacement) {
		this.opc_emplacement = opc_emplacement;
	}

	public EmployePersistant getOpc_responsable() {
		return opc_responsable;
	}

	public void setOpc_responsable(EmployePersistant opc_responsable) {
		this.opc_responsable = opc_responsable;
	}

	public EmployePersistant getOpc_saisisseur() {
		return opc_saisisseur;
	}

	public void setOpc_saisisseur(EmployePersistant opc_saisisseur) {
		this.opc_saisisseur = opc_saisisseur;
	}

	public List<InventaireDetailPersistant> getList_detail() {
		return list_detail;
	}

	public void setList_detail(List<InventaireDetailPersistant> list_detail) {
		this.list_detail = list_detail;
	}

	public ValTypeEnumPersistant getOpc_type_enum() {
		return opc_type_enum;
	}

	public void setOpc_type_enum(ValTypeEnumPersistant opc_type_enum) {
		this.opc_type_enum = opc_type_enum;
	}

	public EmployePersistant getOpc_saisisseur2() {
		return opc_saisisseur2;
	}

	public void setOpc_saisisseur2(EmployePersistant opc_saisisseur2) {
		this.opc_saisisseur2 = opc_saisisseur2;
	}
	
	public String getDate_realisation_toString() {
		return DateUtil.dateToString(date_realisation);
	}

}
