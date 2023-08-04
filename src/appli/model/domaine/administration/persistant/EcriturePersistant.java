package appli.model.domaine.administration.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.beanContext.ComptePersistant;

@Entity
@Table(name = "compte_bancaire_ecriture", indexes={
		@Index(name="IDX_ECR_FUNC", columnList="code_func")
	})
@NamedQueries({
//	@NamedQuery(name="ecriture_find", query="from EcriturePersistant ecriture where "
//			+ "ecriture.opc_banque.id='[banque_id]' "
//			+ "order by ecriture.date_mouvement desc, ecriture.libelle"),
	@NamedQuery(name="ecriture_find", query="from EcriturePersistant ecriture where "
		+ " ecriture.source='[type_ecriture]' "
		+ " and ecriture.date_mouvement>='[date_debut]' "
		+ " and ecriture.date_mouvement<='[date_fin]' "
		+ " and ecriture.sens='[sens]' "
		+ " and ecriture.opc_banque.id='[banque_id]' "
		+ " group by ecriture.groupe "
		+ " order by ecriture.date_mouvement desc, ecriture.source, ecriture.groupe, ecriture.elementId, ecriture.id, ecriture.opc_compte.code"),
	@NamedQuery(name="ecriture_find_like", query="from EcriturePersistant ecriture where "
			+ " ecriture.opc_compte.code like '[compte_code]' "
			+ " and ecriture.date_mouvement>='[date_debut]' "
			+ " and ecriture.date_mouvement<='[date_fin]' "
			+ " and ecriture.montant != 0"
			+ " order by ecriture.date_mouvement, ecriture.source, ecriture.groupe, ecriture.elementId, ecriture.id, ecriture.opc_compte.code"),
	@NamedQuery(name="ecriture_livre_find_like", query="from EcriturePersistant ecriture where "
			+ " ecriture.opc_compte.code like '[compte_code]' "
			+ " and ecriture.date_mouvement>='[date_debut]' "
			+ " and ecriture.date_mouvement<='[date_fin]' "
			+ " and ecriture.montant != 0"
			+ " order by ecriture.opc_compte.code, ecriture.date_mouvement, ecriture.source, ecriture.groupe, ecriture.elementId, ecriture.id")
	})
public class EcriturePersistant extends BasePersistant {  
	@Column(length = 80, nullable = false) 
	private String libelle;

	@Column(length = 30)
	private String source;
	@Column(length = 40)
	private Integer groupe;
	
	@Column(length = 1, nullable=false)
	private String sens;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_mouvement;
	
	@Column(length = 20)
	private Long elementId;

	@Column(length = 15, scale = 6, precision = 15, nullable=false)
	private BigDecimal montant;
	
    @ManyToOne
    @JoinColumn(name = "banque_id", referencedColumnName="id", nullable=false)           
    private CompteBancairePersistant opc_banque;

	@Column
	private Boolean is_compta;// Si l'écriture est comptabilisée ou non
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_rapprochement;
	/*********************** Liens ************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", referencedColumnName="id")
	private ComptePersistant opc_compte;
	
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public CompteBancairePersistant getOpc_banque() {
		return opc_banque;
	}

	public void setOpc_banque(CompteBancairePersistant opc_banque) {
		this.opc_banque = opc_banque;
	}

	public Date getDate_mouvement() {
		return date_mouvement;
	}

	public void setDate_mouvement(Date date_mouvement) {
		this.date_mouvement = date_mouvement;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

	public Boolean getIs_compta() {
		return is_compta;
	}

	public void setIs_compta(Boolean is_compta) {
		this.is_compta = is_compta;
	}

	public Date getDate_rapprochement() {
		return date_rapprochement;
	}

	public void setDate_rapprochement(Date date_rapprochement) {
		this.date_rapprochement = date_rapprochement;
	}

	public ComptePersistant getOpc_compte() {
		return opc_compte;
	}

	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}

	public Integer getGroupe() {
		return groupe;
	}

	public void setGroupe(Integer groupe) {
		this.groupe = groupe;
	}
	
}
