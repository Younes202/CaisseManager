package appli.model.domaine.administration.persistant;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "ged", indexes={
		@Index(name="IDX_GED_CODE", columnList="code"),
		@Index(name="IDX_GED_FUNC", columnList="code_func")
	})
@NamedQuery(name="ged_find", query="from GedPersistant ged order by ged.b_left")
public class GedPersistant extends BasePersistant  {
	@Column(length = 80)
	private String code;

	@Column(length = 10)
	private String type_ged;//FO, CL, EM, TRV || RAZ
	@Column(length = 20)
	private Long source_id;

	@Column(length = 80, nullable = false)
	private String libelle;

	@Column(length = 5)
	private Integer b_left;
	
	@Column(length = 5)
	private Integer b_right;

	@Column(length = 5)
	private Integer level;

	@Column
	private Boolean is_not_supp;// Si on peut supprimer la pièce jointe

	@Transient
	private Integer idx_order;
	
	@GsonExclude
	@OneToMany
	@JoinColumn(name = "ged_id", referencedColumnName = "id", updatable=false, insertable=false)
	List<GedFichierPersistant> list_fichier;
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public Integer getB_left() {
		return b_left;
	}
	public void setB_left(Integer b_left) {
		this.b_left = b_left;
	}
	public Integer getB_right() {
		return b_right;
	}
	public void setB_right(Integer b_right) {
		this.b_right = b_right;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public List<GedFichierPersistant> getList_fichier() {
		return list_fichier;
	}
	public void setList_fichier(List<GedFichierPersistant> list_fichier) {
		this.list_fichier = list_fichier;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getIdx_order() {
		return idx_order;
	}
	public void setIdx_order(Integer idx_order) {
		this.idx_order = idx_order;
	}
	public String getType_ged() {
		return type_ged;
	}
	public void setType_ged(String type_ged) {
		this.type_ged = type_ged;
	}			
	public Long getSource_id() {
		return source_id;
	}
	public void setSource_id(Long source_id) {
		this.source_id = source_id;
	}
	public Boolean getIs_not_supp() {
		return is_not_supp;
	}
	public void setIs_not_supp(Boolean is_not_supp) {
		this.is_not_supp = is_not_supp;
	}
}
