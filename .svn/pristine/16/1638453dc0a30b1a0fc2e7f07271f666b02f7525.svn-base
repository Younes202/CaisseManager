package framework.model.beanContext;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import framework.model.util.GsonExclude;

@Entity
@Table(name = "region", indexes={
		@Index(name="IDX_REGION_FUNC", columnList="code_func")
	})
public class RegionPersistant {
	@Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 120)
	private String code_func;
	@Transient
	private String sync_key;
	@Transient
	private String sync_opr_id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_creation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_maj;
    @Column(length = 50)
    private String signature;
    
	@Column(length = 120, nullable = false)
	private String libelle;

	@GsonExclude
	@OrderBy(value="libelle")
	@OneToMany
	@JoinColumn(name="region_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<VillePersistant> list_ville;
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<VillePersistant> getList_ville() {
		return list_ville;
	}

	public void setList_ville(List<VillePersistant> list_ville) {
		this.list_ville = list_ville;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode_func() {
		return code_func;
	}

	public void setCode_func(String code_func) {
		this.code_func = code_func;
	}

	public String getSync_key() {
		return sync_key;
	}

	public void setSync_key(String sync_key) {
		this.sync_key = sync_key;
	}

	public String getSync_opr_id() {
		return sync_opr_id;
	}

	public void setSync_opr_id(String sync_opr_id) {
		this.sync_opr_id = sync_opr_id;
	}

	public Date getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}

	public Date getDate_maj() {
		return date_maj;
	}

	public void setDate_maj(Date date_maj) {
		this.date_maj = date_maj;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
