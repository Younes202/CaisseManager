package framework.model.beanContext;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "ville_quartier", indexes={
		@Index(name="IDX_VILLE_QUAR_FUNC", columnList="code_func")
	})
public class VilleQuartierPersistant {
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
    
    @Column(length = 25, scale = 20, precision = 25)
	private BigDecimal position_lat;
    @Column(length = 25, scale = 20, precision = 25)
	private BigDecimal position_lng;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date date_creation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_maj;
    @Column(length = 50)
    private String signature;
	
	@Column(length = 120, nullable = false)
	private String libelle;
	
	@ManyToOne
	@JoinColumn(name = "ville_id", nullable=false, referencedColumnName="id")
	private VillePersistant opc_ville;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
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

	public BigDecimal getPosition_lat() {
		return position_lat;
	}

	public void setPosition_lat(BigDecimal position_lat) {
		this.position_lat = position_lat;
	}

	public BigDecimal getPosition_lng() {
		return position_lng;
	}

	public void setPosition_lng(BigDecimal position_lng) {
		this.position_lng = position_lng;
	}

	public VillePersistant getOpc_ville() {
		return opc_ville;
	}

	public void setOpc_ville(VillePersistant opc_ville) {
		this.opc_ville = opc_ville;
	}
}
