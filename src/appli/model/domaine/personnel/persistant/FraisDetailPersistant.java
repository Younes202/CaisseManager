package appli.model.domaine.personnel.persistant;

import java.io.Serializable;
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
@Table(name = "frais_detail", indexes={
		@Index(name="IDX_FRAIS_DET_FUNC", columnList="code_func")
	})
@NamedQuery(name="fraisDetail_find", query="from FraisDetailPersistant fraisDetail order by fraisDetail.date_depense desc")
public class FraisDetailPersistant extends BasePersistant  implements Serializable {
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_depense;
	@Column(length = 15, scale = 6, precision = 15, nullable=false)
	private BigDecimal montant;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_rembours;
	//Ajout diagramme répartition des notes de frais par type;;; (camembert) select SUM(montant),
	//,opc_frais.libelle from FraisDetailPersistant group by opc_type_frais.id 
	//select SUM(montant) ,opc_frais.libelle.opc_employe.nom from FraisDetailPersistant group by opc_frais.libelle.opc_employe.id
	@ManyToOne
	@JoinColumn(name = "type_frais_id", referencedColumnName="id", nullable=false)
	private TypeFraisPersistant opc_type_frais;

	@ManyToOne
	@JoinColumn(name = "frais_id", referencedColumnName="id")
	private FraisPersistant opc_frais;
	
	public Date getDate_depense() {
		return date_depense;
	}
	public void setDate_depense(Date date_depense) {
		this.date_depense = date_depense;
	}
	public BigDecimal getMontant() {
		return montant;
	}
	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}
	public BigDecimal getMtt_rembours() {
		return mtt_rembours;
	}
	public void setMtt_rembours(BigDecimal mtt_rembours) {
		this.mtt_rembours = mtt_rembours;
	}
	public TypeFraisPersistant getOpc_type_frais() {
		return opc_type_frais;
	}
	public void setOpc_type_frais(TypeFraisPersistant opc_type_frais) {
		this.opc_type_frais = opc_type_frais;
	}
	public FraisPersistant getOpc_frais() {
		return opc_frais;
	}
	public void setOpc_frais(FraisPersistant opc_frais) {
		this.opc_frais = opc_frais;
	}
		
}
