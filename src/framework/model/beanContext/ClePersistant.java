package framework.model.beanContext;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(name = "cle", uniqueConstraints={ @UniqueConstraint(columnNames={"code", "etablissement_id"}),
			@UniqueConstraint(columnNames={"libelle", "etablissement_id"})})
@NamedQueries({
		@NamedQuery(name="cle_find", query="from ClePersistant cle where cle.opc_etablissement.id='{etablissement_id}' order by cle.libelle")
})
public class ClePersistant extends BasePersistant implements Serializable {
	@Column(length = 30, nullable = false)
	private String code;

	
	@Column(length = 50, nullable = false)
	private String libelle;

	@Column(length = 255)
	private String commentaire;

	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean equals(Object o) {
	    if(o == null)
	        return false;

	   ClePersistant cle = (ClePersistant) o;
	   if(!(getId().equals(cle.getId())))
	       return false;

	   return true;
	}
}
