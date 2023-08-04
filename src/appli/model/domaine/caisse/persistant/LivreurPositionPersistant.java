package appli.model.domaine.caisse.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.BasePersistant;

@Entity 
@Table(name = "livreur_position")
public class LivreurPositionPersistant extends BasePersistant {
    @Column(length = 25, scale = 20, precision = 25, nullable = false)
	private BigDecimal position_lat;
    @Column(length = 25, scale = 20, precision = 25, nullable = false)
	private BigDecimal position_lng;
    @Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_position;

	@ManyToOne
	@JoinColumn(name = "livreur_id", referencedColumnName = "id", nullable = false)
	private UserPersistant opc_livreur;

	public LivreurPositionPersistant() {
		
	}
	
	public LivreurPositionPersistant(Date date_position, BigDecimal position_lat, BigDecimal position_lng, UserPersistant opc_livreur) {
		this.date_position = date_position;
		this.position_lat = position_lat;
		this.position_lng = position_lng;
		this.opc_livreur = opc_livreur;
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

	public Date getDate_position() {
		return date_position;
	}

	public void setDate_position(Date date_position) {
		this.date_position = date_position;
	}

	public UserPersistant getOpc_livreur() {
		return opc_livreur;
	}

	public void setOpc_livreur(UserPersistant opc_livreur) {
		this.opc_livreur = opc_livreur;
	}
	
	
}
