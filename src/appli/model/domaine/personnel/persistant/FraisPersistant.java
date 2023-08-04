package appli.model.domaine.personnel.persistant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import appli.model.domaine.compta.persistant.PaiementPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "frais", indexes={
		@Index(name="IDX_FRAIS_FUNC", columnList="code_func")
	})
@NamedQuery(name="frais_find", query="from FraisPersistant frais order by frais.opc_employe.nom, frais.date_creation desc")
public class FraisPersistant extends BasePersistant implements Serializable {
	@Column(length = 120)
	private String libelle;
	@Column(length = 10)
	private String statut;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_total;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_rembours;
	@Column(length = 255)
	private String commentaire;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employe_id", referencedColumnName="id")
	private EmployePersistant opc_employe;
	
	@GsonExclude
	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "frais_id", referencedColumnName = "id")
	private List<FraisDetailPersistant> list_detail;
	
	@OneToOne
	@JoinColumn(name = "paiementId", referencedColumnName = "id")
	@Where(clause = "source='REMBOURS'")
	private PaiementPersistant opc_paiement;

	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public List<FraisDetailPersistant> getList_detail() {
		return list_detail;
	}
	public void setList_detail(List<FraisDetailPersistant> list_detail) {
		this.list_detail = list_detail;
	}
	public BigDecimal getMtt_total() {
		return mtt_total;
	}
	public void setMtt_total(BigDecimal mtt_total) {
		this.mtt_total = mtt_total;
	}
	public EmployePersistant getOpc_employe() {
		return opc_employe;
	}
	public void setOpc_employe(EmployePersistant opc_employe) {
		this.opc_employe = opc_employe;
	}
	public PaiementPersistant getOpc_paiement() {
		return opc_paiement;
	}
	public void setOpc_paiement(PaiementPersistant opc_paiement) {
		this.opc_paiement = opc_paiement;
	}
	public BigDecimal getMtt_rembours() {
		return mtt_rembours;
	}
	public void setMtt_rembours(BigDecimal mtt_rembours) {
		this.mtt_rembours = mtt_rembours;
	}
	
}
