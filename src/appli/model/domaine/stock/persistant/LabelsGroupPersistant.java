package appli.model.domaine.stock.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "labels_group")
public class LabelsGroupPersistant extends BasePersistant {
	@Column(length=120, nullable = false)
	private String libelle;
	@Column(length = 1)
	private Boolean is_groupe;
	@Column(length = 5)
	private String source;//REC, DEP : Charges divers, ...
	@Column(length=3) 
	private Integer idxIhm;
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public Boolean getIs_groupe() {
		return is_groupe;
	}
	public void setIs_groupe(Boolean is_groupe) {
		this.is_groupe = is_groupe;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getIdxIhm() {
		return idxIhm;
	}
	public void setIdxIhm(Integer idxIhm) {
		this.idxIhm = idxIhm;
	}
}
