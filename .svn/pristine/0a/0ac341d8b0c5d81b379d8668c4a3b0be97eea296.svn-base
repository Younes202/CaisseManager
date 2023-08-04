package appli.model.domaine.vente.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import framework.model.beanContext.BasePersistant;

/**
 *
 * @author 
 */
@Entity
@Table(name = "ticketCaisse_conf_detail", indexes={
		@Index(name="IDX_TICK_CAI_CONF_FUNC", columnList="code_func")
	})
public class TicketCaisseConfDetailPersistant extends BasePersistant { 
	@Column(length=3)
	private Integer idxIhm;
	
    @Transient
    private String value;
    @Column
    private Long famille;
    @Column(length=255)
    private String valeur_defaut;
    @Column(length=80)
    private String correspondance;
    @Column(nullable=false, length=5)
    private BigDecimal posX;
    @Column(nullable=false, length=5)
    private BigDecimal posY;
    
    
	@ManyToOne
	@JoinColumn(name = "attest_conf_id", referencedColumnName="id", updatable=false, insertable=false)
	private TicketCaisseConfPersistant opc_menu;
    
	public String getCorrespondance() {
		return correspondance;
	}

	public void setCorrespondance(String correspondance) {
		this.correspondance = correspondance;
	}

	public BigDecimal getPosX() {
		return posX;
	}

	public void setPosX(BigDecimal posX) {
		this.posX = posX;
	}

	public BigDecimal getPosY() {
		return posY;
	}

	public void setPosY(BigDecimal posY) {
		this.posY = posY;
	}
	
	public Integer getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(Integer idxIhm) {
		this.idxIhm = idxIhm;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValeur_defaut() {
		return valeur_defaut;
	}

	public void setValeur_defaut(String valeur_defaut) {
		this.valeur_defaut = valeur_defaut;
	}

	public Long getFamille() {
		return famille;
	}

	public void setFamille(Long famille) {
		this.famille = famille;
	}

	public TicketCaisseConfPersistant getOpc_menu() {
		return opc_menu;
	}

	public void setOpc_menu(TicketCaisseConfPersistant opc_menu) {
		this.opc_menu = opc_menu;
	}
}
