package appli.model.domaine.personnel.persistant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@SuppressWarnings("serial")
@Entity
@Table(name = "client_groupe", indexes={
		@Index(name="IDX_CLI_FUNC", columnList="code_func")
	})
@NamedQuery(name="clientGroupe_find", query="from ClientGroupePersistant clientGroupe" )
public class ClientGroupePersistant extends BasePersistant implements Serializable {
	@Column(length=20)
	private Long client_ids;
	
	@Column
	@Lob
	private String libelle;

	public Long getClient_ids() {
		return client_ids;
	}

	public void setClient_ids(Long client_ids) {
		this.client_ids = client_ids;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	 
	
	
	

}
