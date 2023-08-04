package appli.model.domaine.personnel.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "poste", indexes={
		@Index(name="IDX_PST_FUNC", columnList="code_func"),
		@Index(name="IDX_PST_CODE", columnList="code"),
	})
@NamedQuery(name="poste_find", query="from PostePersistant poste" +
		" order by poste.intitule")
public class PostePersistant extends BasePersistant  {
	@Column(length = 20)
	private String code;
	
	@Column(length = 50, nullable = false)
	private String intitule;
	
	@Column(length = 1)
	private String mode_paie; //mode de paiement (par jour ou par heure)
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal tarif;
	@Column(length = 5, scale = 2, precision = 5)
	private  BigDecimal heureParJour;

	@Column(length = 255)
	private String description;
	
	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMode_paie() {
		return mode_paie;
	}

	public void setMode_paie(String mode_paie) {
		this.mode_paie = mode_paie;
	}

	public BigDecimal getTarif() {
		return tarif;
	}

	public void setTarif(BigDecimal tarif) {
		this.tarif = tarif;
	}

	public BigDecimal getHeureParJour() {
		return heureParJour;
	}

	public void setHeureParJour(BigDecimal heureParJour) {
		this.heureParJour = heureParJour;
	}
}
