package appli.model.domaine.stock.persistant.centrale;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;


@NamedQuery(name="centraleSynchro_find", query="from CentraleSynchroPersistant centraleSynchro " + 
		" order by centraleSynchro.date_creation desc")
@Entity
@Table(name = "centrale_synchro")
public class CentraleSynchroPersistant extends BasePersistant {
	@Column(length = 20, nullable = false) 
	private String type_opr;// ART, FAM, FOURN, LCHOIX, MNU, TRANSF, DEMTRANSF
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_synchro;

	@Column
	private Boolean is_to_disable;
	
	@Column(length = 20) 
	private Long element_id;// Id du mouvement à synchroniser ou autre avec exactement l'élément
	
	@Column(length = 120, nullable = false) 
	private String code_auth;
	
	// Champs instance centrale
	@ManyToOne
	@JoinColumn(name = "centrale_ets_id", referencedColumnName="id")
	private CentraleEtsPersistant opc_centrale_ets;

	public String getType_opr() {
		return type_opr;
	}

	public void setType_opr(String type_opr) {
		this.type_opr = type_opr;
	}

	public Date getDate_synchro() {
		return date_synchro;
	}

	public void setDate_synchro(Date date_synchro) {
		this.date_synchro = date_synchro;
	}

	public CentraleEtsPersistant getOpc_centrale_ets() {
		return opc_centrale_ets;
	}

	public void setOpc_centrale_ets(CentraleEtsPersistant opc_centrale_ets) {
		this.opc_centrale_ets = opc_centrale_ets;
	}

	public Boolean getIs_to_disable() {
		return is_to_disable;
	}

	public void setIs_to_disable(Boolean is_to_disable) {
		this.is_to_disable = is_to_disable;
	}

	public String getCode_auth() {
		return code_auth;
	}

	public void setCode_auth(String code_auth) {
		this.code_auth = code_auth;
	}
	public Long getElement_id() {
		return element_id;
	}

	public void setElement_id(Long element_id) {
		this.element_id = element_id;
	}
}
