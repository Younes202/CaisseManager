package appli.model.domaine.personnel.persistant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "type_planning", indexes={
		@Index(name="IDX_TYPE_PL_FUNC", columnList="code_func")
	})
@NamedQuery(name="typePlanning_find", query="from TypePlanningPersistant typePlanning order by typePlanning.libelle")
public class TypePlanningPersistant extends BasePersistant implements Serializable {
	@Column(length = 50, nullable = false)
	private String libelle;
	@Column(length = 10)
	private Integer duree;// Durée
	@Column
	private Boolean is_situation_cli;// Affichage situation client	
	@Column(length = 7, nullable = false)
	private String color;

	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getDuree() {
		return duree;
	}
	public void setDuree(Integer duree) {
		this.duree = duree;
	}
	public Boolean getIs_situation_cli() {
		return is_situation_cli;
	}
	public void setIs_situation_cli(Boolean is_situation_cli) {
		this.is_situation_cli = is_situation_cli;
	}

}
