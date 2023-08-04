package appli.model.domaine.administration.persistant;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@SuppressWarnings("serial")
@Entity 
@Table(name = "type_enumere", uniqueConstraints={ 
		@UniqueConstraint(columnNames={"code", "etablissement_id"}),
		@UniqueConstraint(columnNames={"libelle", "etablissement_id"})}, indexes={
								@Index(name="IDX_ENUM_FUNC", columnList="code_func"),
								@Index(name="IDX_ENUM_CODE", columnList="code")
							})
@NamedQuery(name="typeEnum_find", query="from TypeEnumPersistant typeEnum order by typeEnum.libelle")
public class TypeEnumPersistant extends BasePersistant  {
	@Column(length = 30, nullable = false)
	private String code;

	@Column(length = 50, nullable = false)
	private String libelle;
	
	@Column(length = 1)
	private Integer is_supprimable;
	@Column(length = 1)
	private Integer is_ajoutable;
	@Column(length = 1)
	private Integer is_modifiable;
	
	@GsonExclude
	@OrderBy("ordre, libelle")
	@OneToMany
	@JoinColumn(name="type_enum_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<ValTypeEnumPersistant> list_valeur;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<ValTypeEnumPersistant> getList_valeur() {
		return list_valeur;
	}

	public void setList_valeur(List<ValTypeEnumPersistant> list_valeur) {
		this.list_valeur = list_valeur;
	}

	public Integer getIs_supprimable() {
		return is_supprimable;
	}

	public void setIs_supprimable(Integer is_supprimable) {
		this.is_supprimable = is_supprimable;
	}

	public Integer getIs_ajoutable() {
		return is_ajoutable;
	}

	public void setIs_ajoutable(Integer is_ajoutable) {
		this.is_ajoutable = is_ajoutable;
	}

	public Integer getIs_modifiable() {
		return is_modifiable;
	}

	public void setIs_modifiable(Integer is_modifiable) {
		this.is_modifiable = is_modifiable;
	}
}
