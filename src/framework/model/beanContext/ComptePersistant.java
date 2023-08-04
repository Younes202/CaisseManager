package framework.model.beanContext;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.google.gson.annotations.Expose;

import framework.model.common.util.ServiceUtil;

@Entity
@Table(name = "compte", uniqueConstraints={ 
		@UniqueConstraint(columnNames={"code", "etablissement_id"})}, indexes={
				@Index(name="IDX_COMPTE_FUNC", columnList="code_func"),
				@Index(name="IDX_COMPTE_CODE", columnList="code")
	})
public class ComptePersistant extends BasePersistant {
	@Column(length = 20, nullable = false)
	@Expose
	private String code;

	@Column(length = 150, nullable = false)
	@Expose
	private String libelle;
	@Transient
	private Integer idx_order;
	@Column(length = 150, nullable = false)
	private String classe;
	@Column(length = 150, nullable = false)
	private String rubrique;
	@Column(length = 10)
	private BigDecimal taux_recuperation;
	@Column
	private Boolean is_modifiable;
	@Column
	private Boolean is_subcompte;
	@Column
	private Boolean is_ajoute;
	@Column(length = 255)
	private String commentaire;

	@Column(nullable = false)
	private Integer b_left;
	@Column(nullable = false)
	private Integer b_right;
	@Column(length = 5)
	private Integer level;
	
	@Expose
	@Transient
	private String codeLibelle;

	//-----Pour initialisation des compte
	@Column(length = 12, scale = 2, precision = 13) 
	private BigDecimal montant_init;

	@Column(length = 1)
	private String sens;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exercice_id", referencedColumnName = "id")
	private ExercicePersistant opc_exercice;
	
	// Jointure
	@OrderBy("exercice_id DESC")
	@OneToMany()
	@JoinColumn(name="compte_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<CompteInfosPersistant> listCompteInfos;
	
	/************************* Generic attributes **********************/
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

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Boolean getIs_modifiable() {
		return is_modifiable;
	}

	public void setIs_modifiable(Boolean is_modifiable) {
		this.is_modifiable = is_modifiable;
	}

	public Boolean getIs_subcompte() {
		return is_subcompte;
	}

	public void setIs_subcompte(Boolean is_subcompte) {
		this.is_subcompte = is_subcompte;
	}

	public Boolean getIs_ajoute() {
		return is_ajoute;
	}

	public void setIs_ajoute(Boolean is_ajoute) {
		this.is_ajoute = is_ajoute;
	}

	public Integer getB_left() {
		return b_left;
	}

	public void setB_left(Integer b_left) {
		this.b_left = b_left;
	}

	public Integer getB_right() {
		return b_right;
	}

	public void setB_right(Integer b_right) {
		this.b_right = b_right;
	}

	public String getCodeLibelle() {
		return codeLibelle;
	}

	public void setCodeLibelle(String codeLibelle) {
		this.codeLibelle = codeLibelle;
	}

	public BigDecimal getMontant_init() {
		return montant_init;
	}

	public void setMontant_init(BigDecimal montant_init) {
		this.montant_init = montant_init;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}
	
	public BigDecimal getTaux_recuperation() {
		return taux_recuperation;
	}

	public void setTaux_recuperation(BigDecimal taux_recuperation) {
		this.taux_recuperation = taux_recuperation;
	}
	
	public BigDecimal getTauxRecuperation(Long exerciceId) {
		BigDecimal taux_recuperation = this.getTaux_recuperation();
		CompteInfosPersistant compteInbfo = getCompteInfos(exerciceId);
		if(compteInbfo != null){
			taux_recuperation = compteInbfo.getTaux_recuperation();
		}

		return taux_recuperation;
	}
	
	/**
	 * @param exercice
	 * @return
	 */
	private CompteInfosPersistant getCompteInfos(Long exerciceId){
		if(ServiceUtil.isNotEmpty(this.listCompteInfos)){
			// On cherche sur le même si non le drnier avant cet exercice
			for(CompteInfosPersistant compteInfo : this.listCompteInfos){
				if(exerciceId >= compteInfo.getOpc_exercice().getId()){
					return compteInfo;
				}
			}
		}
		return null;
	}
	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getRubrique() {
		return rubrique;
	}

	public void setRubrique(String rubrique) {
		this.rubrique = rubrique;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIdx_order() {
		return idx_order;
	}

	public void setIdx_order(Integer idx_order) { 
		this.idx_order = idx_order;
	}

	public ExercicePersistant getOpc_exercice() {
		return opc_exercice;
	}

	public void setOpc_exercice(ExercicePersistant opc_exercice) {
		this.opc_exercice = opc_exercice;
	}

	public List<CompteInfosPersistant> getListCompteInfos() {
		return listCompteInfos;
	}

	public void setListCompteInfos(List<CompteInfosPersistant> listCompteInfos) {
		this.listCompteInfos = listCompteInfos;
	}
}
