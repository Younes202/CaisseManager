package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
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
@Table(name = "fournisseur_cheque", indexes={
		@Index(name="IDX_FCHQ_NUM", columnList="num_cheque"),
		@Index(name="IDX_FCHQ_FUNC", columnList="code_func")
	})
@NamedQuery(name = "fournisseurCheque_find", query="from FournisseurChequePersistant fournisseurCheque "
		+ "order by opc_fournisseur.libelle, fournisseurCheque.id desc")
public class FournisseurChequePersistant extends BasePersistant  {
	@Column(length = 30)
	private String num_cheque;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_encaissement;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_annulation;

	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal montant;
	
	@Column(length = 20)
	private Long elementId;
	@Column(length = 30)
	private String source;
	
	/*********************** Liens ************************************/
	@ManyToOne
	@JoinColumn(name = "fournisseur_id", referencedColumnName="id", nullable=false)
	private FournisseurPersistant opc_fournisseur;

	public String getNum_cheque() {
		return num_cheque;
	}

	public void setNum_cheque(String num_cheque) {
		this.num_cheque = num_cheque;
	}

	public Date getDate_encaissement() {
		return date_encaissement;
	}

	public void setDate_encaissement(Date date_encaissement) {
		this.date_encaissement = date_encaissement;
	}

	public Date getDate_annulation() {
		return date_annulation;
	}

	public void setDate_annulation(Date date_annulation) {
		this.date_annulation = date_annulation;
	}

	public FournisseurPersistant getOpc_fournisseur() {
		return opc_fournisseur;
	}

	public void setOpc_fournisseur(FournisseurPersistant opc_fournisseur) {
		this.opc_fournisseur = opc_fournisseur;
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
}
