package appli.model.domaine.administration.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "parametrage", 
uniqueConstraints={
		   @UniqueConstraint(columnNames={"code",  "terminal_id", "etablissement_id"})
		}, indexes={
				@Index(name="IDX_PARAM_FUNC", columnList="code_func"),
				@Index(name="IDX_PARAM_CODE", columnList="code")
			} )
public class ParametragePersistant extends BasePersistant  {
	
	public ParametragePersistant(){
		
	}
	public ParametragePersistant(
			String groupe,
			String groupe_sub,
			String code, 
			String libelle, 
			String help, 
			String type, 
			String valeur){
		this.groupe=groupe;
		this.groupe_sub=groupe_sub;
		this.code=code;
		this.libelle=libelle; 
		this.help=help;
		this.type=type;
		this.valeur=valeur;
	}
	
	@Column(length = 30, nullable=false)
	private String type;
	
	@Column(length = 30)
	private String abonnement;
	
	@Column(length = 120, nullable=false)
	private String code;

	@Column(length = 255)
	private String help;

	@Column(length = 120, nullable=false)
	private String libelle;
	
	@Column(length = 120)
	private String valeur;
	
	@Column(length = 50, nullable=false)
	private String groupe;
	@Column(length = 50)
	private String groupe_sub;
	
	@Column(length = 3)
	private Integer ordre;
	
    @ManyToOne
    @JoinColumn(name = "terminal_id", referencedColumnName="id")           
    private CaissePersistant opc_terminal;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getAbonnement() {
		return abonnement;
	}

	public void setAbonnement(String abonnement) {
		this.abonnement = abonnement;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getGroupe() {
		return groupe;
	}

	public Integer getOrdre() {
		return ordre;
	}

	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}

	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}
	public CaissePersistant getOpc_terminal() {
		return opc_terminal;
	}
	public void setOpc_terminal(CaissePersistant opc_terminal) {
		this.opc_terminal = opc_terminal;
	}
	public String getGroupe_sub() {
		return groupe_sub;
	}
	public void setGroupe_sub(String groupe_sub) {
		this.groupe_sub = groupe_sub;
	}
}
