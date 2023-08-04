package appli.model.domaine.administration.persistant;

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
import framework.model.beanContext.CompteBancairePersistant;

@Entity
@Table(name = "compte_bancaire_fonds", indexes={
		@Index(name="IDX_CPT_BQE_FND_FUNC", columnList="code_func"),
		@Index(name="IDX_CPT_BQE_FND_NUM", columnList="num_virsement"),
	})
@NamedQuery(name="compteBancaireFonds_find", query="from CompteBancaireFondsPersistant compteBancaireFonds order by compteBancaireFonds.date_mouvement desc") 
public class CompteBancaireFondsPersistant extends BasePersistant{ 
	@Column(length = 80, nullable = false)
	private String libelle;
	
	@Column(length = 30)
	private String num_virsement;
	
	@Column(length = 255)
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_mouvement;
	
	@Column(length = 15, scale = 6, precision = 15, nullable=false)
	private  BigDecimal montant;

    @ManyToOne
    @JoinColumn(name = "banque_source_id", referencedColumnName="id")           
    private CompteBancairePersistant opc_banque_source;

    @ManyToOne
    @JoinColumn(name = "banque_dest_id", referencedColumnName="id", nullable=false)           
    private CompteBancairePersistant opc_banque_dest;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Date getDate_mouvement() {
		return date_mouvement;
	}

	public void setDate_mouvement(Date date_mouvement) {
		this.date_mouvement = date_mouvement;
	}

	public CompteBancairePersistant getOpc_banque_source() {
		return opc_banque_source;
	}

	public void setOpc_banque_source(CompteBancairePersistant opc_banque_source) {
		this.opc_banque_source = opc_banque_source;
	}

	public CompteBancairePersistant getOpc_banque_dest() {
		return opc_banque_dest;
	}

	public void setOpc_banque_dest(CompteBancairePersistant opc_banque_dest) {
		this.opc_banque_dest = opc_banque_dest;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public String getNum_virsement() {
		return num_virsement;
	}

	public void setNum_virsement(String num_virsement) {
		this.num_virsement = num_virsement;
	}
}
