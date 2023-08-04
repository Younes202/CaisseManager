package appli.model.domaine.administration.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import framework.model.beanContext.BasePersistant;

@SuppressWarnings("serial")
@Entity
@Table(name = "val_type_enumere", 
		uniqueConstraints={
		   @UniqueConstraint(columnNames={"code", "type_enum_id", "etablissement_id"}),
		   @UniqueConstraint(columnNames={"libelle", "type_enum_id", "etablissement_id"})
		}, indexes={
				@Index(name="IDX_ENUM_VAL_FUNC", columnList="code_func"),
				@Index(name="IDX_ENUM_VAL_CODE", columnList="code")
			}
)
@NamedQuery(name="valTypeEnum_find", query="from ValTypeEnumPersistant valTypeEnum " +
		"where valTypeEnum.opc_typenum.id = '{typenumId}' order by valTypeEnum.ordre")
public class ValTypeEnumPersistant extends BasePersistant  {
	@Column(length = 40, nullable = false)
	private String code;

	@Column(length = 150, nullable = false)
	private String libelle;

	@Column(length = 2)
	private Integer ordre;

	@Column
	private Boolean is_disable;
	
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "type_enum_id", nullable=false, referencedColumnName="id")
	private TypeEnumPersistant opc_typenum;

	@Column(insertable=false, updatable=false)
	private Long type_enum_id;

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

	public Integer getOrdre() {
		return ordre;
	}

	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}

	public TypeEnumPersistant getOpc_typenum() {
		return opc_typenum;
	}

	public void setOpc_typenum(TypeEnumPersistant opc_typenum) {
		this.opc_typenum = opc_typenum;
	}

	public Long getType_enum_id() {
		return type_enum_id;
	}

	public void setType_enum_id(Long type_enum_id) {
		this.type_enum_id = type_enum_id;
	}

	public Boolean getIs_disable() {
		return is_disable;
	}

	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}
	
}
