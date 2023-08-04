package appli.model.domaine.habilitation.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@SuppressWarnings("serial")
@Entity
@Table(name = "profile_menu", indexes={
		@Index(name="IDX_PROF_MNU_FUNC", columnList="code_func"),
		@Index(name="IDX_PROF_MNU_ID", columnList="menu_id")
	})
public class ProfileMenuPersistant extends BasePersistant  {
	
	@ManyToOne
	@JoinColumn(name = "profile_id", nullable=false, referencedColumnName="id")
	private ProfilePersistant opc_profile;

	@Column(length = 50, nullable = false)
	private String menu_id;

	@Column(length = 100, nullable = false)
	private String rights;

	/**
	 * @return
	 */
	public ProfilePersistant getOpc_profile() {
		return opc_profile;
	}

	public void setOpc_profile(ProfilePersistant opc_profile) {
		this.opc_profile = opc_profile;
	}
	
	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}
}
