package appli.model.domaine.administration.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;

@SuppressWarnings("serial")
@Entity
@Table(name = "user", 
	uniqueConstraints={
        @UniqueConstraint(columnNames = {"login", "etablissement_id"})
	},
	indexes={
		@Index(name="IDX_USER_FUNC", columnList="code_func")
	})
public class UserPersistant extends UserBasePersistant {
	
    @Column
	@Lob
	private String device_token;
    @Column
    private Boolean is_device_notif;
   
    @Column(length = 255)
    private String clientEts_ids;// String séparé par des ; des établissements séléctionnés par le client dans l'app mobile
    @Column(length = 255)
    private String apps_types;// String séparé par des ; des types d'appliations mobiles 

	/**
     * ********************* Liens ***********************************
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employe_id", referencedColumnName = "id")
    private EmployePersistant opc_employe;
    
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientPersistant opc_client;

	public EmployePersistant getOpc_employe() {
		return opc_employe;
	}

	public void setOpc_employe(EmployePersistant opc_employe) {
		this.opc_employe = opc_employe;
	}

	public ClientPersistant getOpc_client() {
		return opc_client;
	}

	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public Boolean getIs_device_notif() {
		return is_device_notif;
	}

	public void setIs_device_notif(Boolean is_device_notif) {
		this.is_device_notif = is_device_notif;
	}
	
	public String getClientEts_ids() {
			return clientEts_ids;
	}

	public void setClientEts_ids(String clientEts_ids) {
			this.clientEts_ids = clientEts_ids;
	}

	public String getApps_types() {
		return apps_types;
	}

	public void setApps_types(String apps_types) {
		this.apps_types = apps_types;
	}
}
