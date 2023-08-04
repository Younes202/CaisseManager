package appli.model.domaine.habilitation.persistant;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import appli.model.domaine.administration.persistant.UserPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@SuppressWarnings( { "serial" })
@Entity
@Table(name = "profile", indexes={
		@Index(name="IDX_PROF_FUNC", columnList="code_func"),
		@Index(name="IDX_PROF_CODE", columnList="code")
	}) 
@NamedQuery(name="profile_find", query="from ProfilePersistant profile order by profile.libelle")
public class ProfilePersistant extends BasePersistant  {
	@Column(length = 30)// Unique dans validateur
	private String code;//MANAGER, ADMIN, SUPERVISEUR, GESTIONNAIRE, LIVREUR, CAISSIER, SERVEUR
	
	@Column(length = 50, nullable = false)// Unique dans validateur
	private String libelle;
	
	@Column(length = 150)
	private String envs;// Environnements autorisés 

	@Column
    private Boolean is_desactive;
	@Column
    private Boolean is_multi_ets;// Peut accèser au multi établissement (Cloud)

	@Column
    private Boolean is_backoffice; 
	@Column
    private Boolean is_caisse;
	
	@GsonExclude
	@OneToMany
	@JoinColumn(name="profile_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<ProfileMenuPersistant> profile_menus;
	
	@GsonExclude
	@OneToMany
	@JoinColumn(name="profile_id", referencedColumnName="id", insertable=false, updatable=false)
	Set<UserPersistant> list_user;
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ProfileMenuPersistant> getProfile_menus() {
		return profile_menus;
	}

	public void setProfile_menus(List<ProfileMenuPersistant> profile_menus) {
		this.profile_menus = profile_menus;
	}

	public Set<UserPersistant> getList_user() {
		return list_user;
	}

	public void setList_user(Set<UserPersistant> list_user) {
		this.list_user = list_user;
	}

	public Boolean getIs_desactive() {
		return is_desactive;
	}

	public void setIs_desactive(Boolean is_desactive) {
		this.is_desactive = is_desactive;
	}

	public Boolean getIs_backoffice() {
		return is_backoffice;
	}

	public void setIs_backoffice(Boolean is_backoffice) {
		this.is_backoffice = is_backoffice;
	}

	public Boolean getIs_caisse() {
		return is_caisse;
	}

	public void setIs_caisse(Boolean is_caisse) {
		this.is_caisse = is_caisse;
	}

	public Boolean getIs_multi_ets() {
		return is_multi_ets;
	}

	public void setIs_multi_ets(Boolean is_multi_ets) {
		this.is_multi_ets = is_multi_ets;
	}

	public String getEnvs() {
		return (envs != null ? envs : "");
	}

	public void setEnvs(String envs) {
		this.envs = envs;
	}
	
}
