package appli.model.domaine.personnel.persistant.paie;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "pointage", indexes={
		@Index(name="IDX_PNT_FUNC", columnList="code_func")
	})
@NamedQuery(name="pointage_find", query="from PointagePersistant pointage where pointage.date_event>='[dateDebut]' "
		+ "and pointage.date_event<='[dateFin]' order by pointage.date_event")
public class PointagePersistant extends BasePersistant  {
	@Column(length = 50)
	private String libelle;

	@Column(length = 50)
	private String text_1;
	
	@Column(length = 15, scale = 2, precision = 4)
	private BigDecimal duree;
	
	@Column(length = 10, nullable = false)
	private String type;// Travail heure/jour: tr, Congé:cgP, cgNp, avance:av, prime:pri, pret:pre, retenue:rt, ind:Indemnité...
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_event;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal tarif_ref;// tarif de référence (salaire, heur supp, ...)

	@Column(length = 255)
	private String description;
	
	@Column(length = 255)
	private String commentaire;

	/*********************** Liens ************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employe_id", referencedColumnName="id", nullable=false)
	private EmployePersistant opc_employe;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getText_1() {
		return text_1;
	}

	public void setText_1(String text_1) {
		this.text_1 = text_1;
	}

	public BigDecimal getDuree() {
		return duree;
	}
	public String getDureeStr() {
		if(("cg".equals(this.type) || "tr".equals(this.type)) && this.duree != null) {
			return this.duree+"j";	
		} else {
			if(this.duree != null) {
				long h = (this.duree.longValue()/60);
				long min = h > 0 ? (this.duree.longValue()%60) : this.duree.longValue();
				
				return (h>0 ? h+"h ":"")+(min>0?min+"min":"");		
			}
		}
		return "";
	}

	public void setDuree(BigDecimal duree) {
		this.duree = duree;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate_event() {
		return date_event;
	}

	public void setDate_event(Date date_event) {
		this.date_event = date_event;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public BigDecimal getTarif_ref() {
		return tarif_ref;
	}

	public void setTarif_ref(BigDecimal tarif_ref) {
		this.tarif_ref = tarif_ref;
	}
}
