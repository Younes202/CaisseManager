package appli.model.domaine.fidelite.persistant;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.GsonExclude;


@Entity
@Table(name = "carte_fidelite", indexes={
		@Index(name="IDX_CB_FID_FUNC", columnList="code_func")
	})
@NamedQuery(name="carteFidelite_find", query="from CarteFidelitePersistant carteFidelite "
		+ "where is_default is null "
		+ "order by carteFidelite.libelle")
public class CarteFidelitePersistant extends BasePersistant  {
	@Column(length = 255)
	private String description;
	
	@Column(length = 80, nullable = false) 
	private String libelle;
	
	@Column
	private Boolean is_active;
	@Column
	private Boolean is_default;
	
//    @Column(length = 15, scale = 6, precision = 15)
//    private BigDecimal taux_portefeuille;
    
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_seuil;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_palier;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_pf_palier;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_plafond;
	
	@Column(length = 10)
	private BigDecimal mtt_seuil_util;// Seuil à partir duquel on peut utiliser les points
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_bloc_util;// On utilise les points par bloc de ... Ex: 10poins pour chaque utilisation minimum
	
	@Column(length = 10)
	private Integer nbr_point_palier;
	
	@GsonExclude
	@OneToMany
	@JoinColumn(name="carte_id", referencedColumnName="id", insertable=false, updatable=false)
	List<CarteFideliteClientPersistant> list_cartes_client;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public Boolean getIs_active() {
		return is_active;
	}
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}
	public BigDecimal getMtt_seuil() {
		return mtt_seuil;
	}
	public void setMtt_seuil(BigDecimal mtt_seuil) {
		this.mtt_seuil = mtt_seuil;
	}
	public BigDecimal getMtt_palier() {
		return mtt_palier;
	}
	public void setMtt_palier(BigDecimal mtt_palier) {
		this.mtt_palier = mtt_palier;
	}
	public BigDecimal getMtt_plafond() {
		return mtt_plafond;
	}
	public void setMtt_plafond(BigDecimal mtt_plafond) {
		this.mtt_plafond = mtt_plafond;
	}
	public List<CarteFideliteClientPersistant> getList_cartes_client() {
		return list_cartes_client;
	}
	public void setList_cartes_client(List<CarteFideliteClientPersistant> list_cartes_client) {
		this.list_cartes_client = list_cartes_client;
	}
	
	public BigDecimal getMtt_pf_palier() {
		return mtt_pf_palier;
	}
	public void setMtt_pf_palier(BigDecimal mtt_pf_palier) {
		this.mtt_pf_palier = mtt_pf_palier;
	}
	public BigDecimal getMtt_seuil_util() {
		return mtt_seuil_util;
	}
	public void setMtt_seuil_util(BigDecimal mtt_seuil_util) {
		this.mtt_seuil_util = mtt_seuil_util;
	}
	public BigDecimal getMtt_bloc_util() {
		return mtt_bloc_util;
	}
	public void setMtt_bloc_util(BigDecimal mtt_bloc_util) {
		this.mtt_bloc_util = mtt_bloc_util;
	}
//	public BigDecimal getTaux_portefeuille() {
//		return taux_portefeuille;
//	}
//	public void setTaux_portefeuille(BigDecimal taux_portefeuille) {
//		this.taux_portefeuille = taux_portefeuille;
//	}
	public Boolean getIs_default() {
		return is_default;
	}
	public void setIs_default(Boolean is_default) {
		this.is_default = is_default;
	}
	public BigDecimal getSoldeActuel(){
//		int totalPöints = 0;
		BigDecimal total = null;
		if(this.list_cartes_client != null){
			for (CarteFideliteClientPersistant carteFideliteClientPersistant : list_cartes_client) {
//				totalPöints = totalPöints + carteFideliteClientPersistant.getTotal_point();
				total = BigDecimalUtil.add(total, carteFideliteClientPersistant.getMtt_total());
			}
		}
		return total;
	}
	public Integer getNbr_point_palier() {
		return nbr_point_palier;
	}
	public void setNbr_point_palier(Integer nbr_point_palier) {
		this.nbr_point_palier = nbr_point_palier;
	}
	public BigDecimal getSoldeGagnes(){
//		BigDecimal totalPöints = null; 
		BigDecimal totalMtt = null;
		if(this.list_cartes_client != null){
			for (CarteFideliteClientPersistant carteFideliteClientPersistant : list_cartes_client) {
				for(CarteFidelitePointsPersistant pointP : carteFideliteClientPersistant.getList_point_gagne()){
					//totalPöints = BigDecimalUtil.add(BigDecimalUtil.get(""+totalPöints), BigDecimalUtil.get(""+pointP.getNbr_point_gain()));
					totalMtt = BigDecimalUtil.add(totalMtt, pointP.getMtt_gain());
				}
			}
		}
		return totalMtt;
	}
	public Integer getNbrCarteActives(){
		Integer total = 0;
		if(this.list_cartes_client != null){
			for (CarteFideliteClientPersistant carteFideliteClientPersistant : list_cartes_client) {
				total = total + (carteFideliteClientPersistant.getIs_active() == null || carteFideliteClientPersistant.getIs_active() ? 1 : 0);
			}
		}
		return total;
	}
}
