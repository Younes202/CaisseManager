package appli.model.domaine.caisse_restau.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import appli.model.domaine.administration.persistant.UserPersistant;
import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "token", 
	uniqueConstraints={
        @UniqueConstraint(columnNames = {"libelle", "etablissement_id"})
	},
	indexes={
		@Index(name="IDX_TKN_FUNC", columnList="code_func"),
		@Index(name="IDX_TKN_REF", columnList="reference")
	})
@NamedQuery(name = "token_find", query="from TokenPersistant token "
		+ "order by token.reference, token.libelle")

public class TokenPersistant extends BasePersistant  {
	@Column(length = 50, nullable = false)
	private String reference;

	@Column(length = 120, nullable = false)
	private String libelle;

	@Column
    private Boolean is_actif;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private UserPersistant opc_user;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	public UserPersistant getOpc_user() {
		return opc_user;
	}

	public void setOpc_user(UserPersistant opc_user) {
		this.opc_user = opc_user;
	}

	public Boolean getIs_actif() {
		return is_actif;
	}

	public void setIs_actif(Boolean is_actif) {
		this.is_actif = is_actif;
	}
	
}
