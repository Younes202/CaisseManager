package appli.model.domaine.vente.persistant;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "menu_composition", indexes={
		@Index(name="IDX_MNU_COMP_FUNC", columnList="code_func"),
		@Index(name="IDX_MNU_COMP_CODE", columnList="code")
	})
public class MenuCompositionPersistant extends BasePersistant {
	@Column(length = 20, nullable = false)
	private String code;
	
	@Column(length = 80, nullable = false)
	private String libelle;
	
	@Column(length = 20)
	private String mnu_source;// Point d'entrée Source
	
	@Column(length = 5)
	private Integer b_left;
	
	@Column(length = 5)
	private Integer b_right;

	@Transient
	private Integer idx_order;
	
	@Column(length = 5)
	private Integer level;
	
	@Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_prix;
	
	@Column(length=6)
	private  Integer nombre_max;
	
	@Column
    private Boolean is_desactive;
	
	@Column
    private Boolean is_menu;
	
	@Column(length=50)
	public String caisse_target;// Les caisse ou sera uniquement affiché
	@GsonExclude
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "menu_id", referencedColumnName = "id")
	List<MenuCompositionDetailPersistant> list_composition;

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
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public BigDecimal getMtt_prix() {
		return mtt_prix;
	}
	public void setMtt_prix(BigDecimal mtt_prix) {
		this.mtt_prix = mtt_prix;
	}
	public Boolean getIs_desactive() {
		return is_desactive;
	}
	public void setIs_desactive(Boolean is_desactive) {
		this.is_desactive = is_desactive;
	}
	public List<MenuCompositionDetailPersistant> getList_composition() {
		return list_composition;
	}
	public void setList_composition(
			List<MenuCompositionDetailPersistant> list_composition) {
		this.list_composition = list_composition;
	}
	public Integer getNombre_max() {
		return nombre_max;
	}
	public void setNombre_max(Integer nombre_max) {
		this.nombre_max = nombre_max;
	}
	public Boolean getIs_menu() {
		return is_menu;
	}
	public void setIs_menu(Boolean is_menu) {
		this.is_menu = is_menu;
	}
	public Integer getIdx_order() {
		return idx_order;
	}
	public void setIdx_order(Integer idx_order) {
		this.idx_order = idx_order;
	}
	public String getCaisse_target() {
		return caisse_target;
	}
	public void setCaisse_target(String caisse_target) {
		this.caisse_target = caisse_target;
	}
	public String getMnu_source() {
		return mnu_source;
	}
	public void setMnu_source(String mnu_source) {
		this.mnu_source = mnu_source;
	}
}
