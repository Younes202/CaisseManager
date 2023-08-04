package appli.model.domaine.fidelite.persistant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name ="carte_fidelite_client", indexes={
		@Index(name="IDX_CB_FID_CLI_FUNC", columnList="code_func"),
		@Index(name="IDX_CB_FID_CLI_BARRE", columnList="code_barre")
	})
@NamedQueries({
	@NamedQuery(name="carteFideliteClient_find", query="from CarteFideliteClientPersistant carteFideliteClient"
			+ " where carteFideliteClient.opc_carte_fidelite.id='[carteId]' "
			+ " and carteFideliteClient.opc_client.id='[clientId]'"
			+ " order by carteFideliteClient.opc_carte_fidelite.libelle, carteFideliteClient.opc_client.nom")
})
public class CarteFideliteClientPersistant extends BasePersistant  {
	@Column(length = 10)
	private Integer total_point;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_total;// Solde carte client
	
	@Column(length = 50, nullable = false)
	private String code_barre;
	
	@Column
	private Boolean is_active;
	 
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_debut;
    
	@Column
	@Temporal(TemporalType.TIMESTAMP)
    private Date date_fin;
    
	@Column
	@Temporal(TemporalType.TIMESTAMP)
    private Date date_suspension;
    
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientPersistant opc_client;
    
    @ManyToOne
    @JoinColumn(name = "carte_id", referencedColumnName = "id")
    private CarteFidelitePersistant opc_carte_fidelite;
    
    @GsonExclude
    @OneToMany
	@JoinColumn(name="carte_client_id", referencedColumnName="id", insertable=false, updatable=false)
    private List<CarteFidelitePointsPersistant> list_point_gagne;
    
    @GsonExclude
    @OneToMany
	@JoinColumn(name="carte_client_id", referencedColumnName="id", insertable=false, updatable=false)
    private List<CarteFideliteConsoPersistant> list_point_conso;
    
//	public int getTotal_point() {
//		return total_point;
//	}
//
//	public void setTotal_point(int total_point) {
//		this.total_point = total_point;
//	}

	public BigDecimal getMtt_total() {
		return mtt_total;
	}

	public void setMtt_total(BigDecimal mtt_total) {
		this.mtt_total = mtt_total;
	}


	public String getCode_barre() {
		return code_barre;
	}

	public void setCode_barre(String code_barre) {
		this.code_barre = code_barre;
	}

	public Date getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}

	public Date getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}

	public Date getDate_suspension() {
		return date_suspension;
	}

	public void setDate_suspension(Date date_suspension) {
		this.date_suspension = date_suspension;
	}

	public ClientPersistant getOpc_client() {
		return opc_client;
	}

	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}

	public CarteFidelitePersistant getOpc_carte_fidelite() {
		return opc_carte_fidelite;
	}

	public void setOpc_carte_fidelite(CarteFidelitePersistant opc_carte_fidelite) {
		this.opc_carte_fidelite = opc_carte_fidelite;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public List<CarteFidelitePointsPersistant> getList_point_gagne() {
		return list_point_gagne;
	}

	public void setList_point_gagne(List<CarteFidelitePointsPersistant> list_point_gagne) {
		this.list_point_gagne = list_point_gagne;
	}

	public List<CarteFideliteConsoPersistant> getList_point_conso() {
		return list_point_conso;
	}

	public void setList_point_conso(List<CarteFideliteConsoPersistant> list_point_conso) {
		this.list_point_conso = list_point_conso;
	}

	public Integer getTotal_point() {
		return total_point;
	}

	public void setTotal_point(Integer total_point) {
		this.total_point = total_point;
	}
	
//	public BigDecimal getMtt_solde(){
////		BigDecimal totalPöints = null; 
//		BigDecimal totalMtt = null;
//		if (list_point_gagne != null) {
//			for(CarteFidelitePointsPersistant pointP : this.list_point_gagne){
//				//totalPöints = BigDecimalUtil.add(BigDecimalUtil.get(""+totalPöints), BigDecimalUtil.get(""+pointP.getNbr_point_gain()));
//				totalMtt = BigDecimalUtil.add(totalMtt, pointP.getMtt_gain());
//			}
//		}
//		return totalMtt;
//	}
}
