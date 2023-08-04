package appli.model.domaine.personnel.persistant;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "type_frais", indexes={
		@Index(name="IDX_TYPE_FR_FUNC", columnList="code_func")
	})
@NamedQuery(name="typeFrais_find", query="from TypeFraisPersistant typeFrais order by typeFrais.libelle")
public class TypeFraisPersistant extends BasePersistant implements Serializable {
	@Column(length = 50, nullable = false)
	private String libelle;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_max;

	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public BigDecimal getMontant_max() {
		return montant_max;
	}
	public void setMontant_max(BigDecimal montant_max) {
		this.montant_max = montant_max;
	}
	
}
