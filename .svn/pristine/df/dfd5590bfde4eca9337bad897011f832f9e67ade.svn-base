package framework.model.beanContext;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "abonne", indexes={
		@Index(name="IDX_ABN_FUNC", columnList="code_func")
	})
public class AbonnePersistant implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 120)
   	private String code_func;
    @Column
   	private Long last_ets;// Dernier etablissement de connexion

	@Transient
	private String sync_key;
	@Transient
	private String sync_opr_id;
    
    @Column(length = 120, nullable = false)
    private String raison_sociale;
    @Column(length = 255)
    private String adresse;
    @Column(length = 50)
    private String mail;
    
    @Column
    private Boolean is_disable;
    @Column
    private Boolean is_valide;
	
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
	public String getRaison_sociale() {
		return raison_sociale;
	}
	public void setRaison_sociale(String raison_sociale) {
		this.raison_sociale = raison_sociale;
	}
	public Boolean getIs_disable() {
		return is_disable;
	}
	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
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
	public Long getLast_ets() {
		return last_ets;
	}
	public void setLast_ets(Long last_ets) {
		this.last_ets = last_ets;
	}
	public Boolean getIs_valide() {
		return is_valide;
	}
	public void setIs_valide(Boolean is_valide) {
		this.is_valide = is_valide;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}   
}
