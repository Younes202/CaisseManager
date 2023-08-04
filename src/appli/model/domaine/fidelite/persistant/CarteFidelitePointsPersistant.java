package appli.model.domaine.fidelite.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.beanContext.BasePersistant;


@Entity
@Table(name = "carte_fidelite_points", indexes={
		@Index(name="IDX_CB_FID_PT_FUNC", columnList="code_func")
	})
@NamedQuery(name="clientCartePoint_find", query="from CarteFidelitePointsPersistant carteFidelitePoints"
		+ " where carteFidelitePoints.opc_carte_client.id='{carteClientId}' "
		+ "  order by carteFidelitePoints.date_gain desc" )
public class CarteFidelitePointsPersistant extends BasePersistant  {
	@Column(length = 10)
	private Integer nbr_point_gain;
	
	@Column(length = 15, scale = 6, precision=15)
	private BigDecimal mtt_gain;
	
	@Column(length = 10, nullable = false)
	private String source;//CMD=Commande caisse, CDE=Cadeau
	
	/************************* Generic attributes **********************/
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date date_gain;
	
	@ManyToOne
    @JoinColumn(name = "carte_client_id", referencedColumnName = "id", nullable=false)
	private CarteFideliteClientPersistant opc_carte_client;
//	@Column
//	private Long caisse_mouvement_id;
	
	@ManyToOne
    @JoinColumn(name = "caisse_mouvement_id", referencedColumnName = "id")
	private CaisseMouvementPersistant opc_mvn_caisse; 

	public Integer getNbr_point_gain() {
		return nbr_point_gain;
	}

	public void setNbr_point_gain(Integer nbr_point_gain) {
		this.nbr_point_gain = nbr_point_gain;
	}

	public BigDecimal getMtt_gain() {
		return mtt_gain;
	}

	public void setMtt_gain(BigDecimal mtt_gain) {
		this.mtt_gain = mtt_gain;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getDate_gain() {
		return date_gain;
	}

	public void setDate_gain(Date date_gain) {
		this.date_gain = date_gain;
	}

	public CarteFideliteClientPersistant getOpc_carte_client() {
		return opc_carte_client;
	}

	public void setOpc_carte_client(CarteFideliteClientPersistant opc_carte_client) {
		this.opc_carte_client = opc_carte_client;
	}
	public CaisseMouvementPersistant getOpc_mvn_caisse() {
		return opc_mvn_caisse;
	}
	public void setOpc_mvn_caisse(CaisseMouvementPersistant opc_mvn_caisse) {
		this.opc_mvn_caisse = opc_mvn_caisse;
	}
}
