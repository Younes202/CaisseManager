package appli.model.domaine.administration.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "ged_fichier", indexes={
		@Index(name="IDX_GED_FIC_FUNC", columnList="code_func")
	})
public class GedFichierPersistant extends BasePersistant  {
	@Column(length = 80, nullable = false)
	private String libelle;

	@Column(length = 120, nullable = false)
	private String file_name;

	@Column(length = 10, nullable = false)
	private String extention;
	
	@Column(length = 255)
	private String commentaire;
	@Column(length = 255)
	private String path;

	@Column
	private Boolean is_not_sup;// Si on peut supprimer la pièce jointe

	@ManyToOne
	@JoinColumn(name = "ged_id", referencedColumnName="id", nullable=false)
	private GedPersistant opc_ged;
	
	public GedPersistant getOpc_ged() {
		return opc_ged;
	}
	public void setOpc_ged(GedPersistant opc_ged) {
		this.opc_ged = opc_ged;
	}
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
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getExtention() {
		return extention;
	}
	public void setExtention(String extention) {
		this.extention = extention;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Boolean getIs_not_sup() {
		return is_not_sup;
	}
	public void setIs_not_sup(Boolean is_not_sup) {
		this.is_not_sup = is_not_sup;
	}
	
}
