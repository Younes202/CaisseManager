package framework.model.beanContext;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import framework.model.util.GsonExclude;

@Entity
@Table(name = "ville", indexes={
		@Index(name="IDX_VILLE_FUNC", columnList="code_func"),
		@Index(name="IDX_VILLE_CP", columnList="code_postal")
	})
public class VillePersistant /*extends BasePersistant*/ {
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
    @Column(length = 120)
	private String code_postal;
	@ManyToOne
	@JoinColumn(name = "region_id", nullable=false, referencedColumnName="id")
	private RegionPersistant opc_region;
	
	@GsonExclude
	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ville_id", referencedColumnName = "id")
	private List<VilleQuartierPersistant> list_quartier;
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public RegionPersistant getOpc_region() {
		return opc_region;
	}

	public void setOpc_region(RegionPersistant opc_region) {
		this.opc_region = opc_region;
	}

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
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

	public List<VilleQuartierPersistant> getList_quartier() {
		return list_quartier;
	}

	public void setList_quartier(List<VilleQuartierPersistant> list_quartier) {
		this.list_quartier = list_quartier;
	}
}
