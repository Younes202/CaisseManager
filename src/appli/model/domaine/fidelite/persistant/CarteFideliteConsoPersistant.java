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
@Table(name = "carte_fidelite_conso", indexes={
		@Index(name="IDX_CB_FID_CONSO_FUNC", columnList="code_func")
	})
@NamedQuery(name="clientCarteConso_find", query="from CarteFideliteConsoPersistant carteFideliteConso"
		+ " where carteFideliteConso.opc_carte_client.id='{carteClientId}' "
		+ "  order by carteFideliteConso.date_conso desc" )
public class CarteFideliteConsoPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_conso;
	@Column(length = 10)
	private Integer nbr_point_conso;
	
/************************* Generic attributes **********************/
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date date_conso;
	
	@ManyToOne
    @JoinColumn(name = "carte_client_id", referencedColumnName = "id")
	private CarteFideliteClientPersistant opc_carte_client;
	
	@ManyToOne
    @JoinColumn(name = "caisse_mouvement_id", referencedColumnName = "id")
	private CaisseMouvementPersistant opc_mvn_caisse;

	public BigDecimal getMtt_conso() {
		return mtt_conso;
	}

	public void setMtt_conso(BigDecimal mtt_conso) {
		this.mtt_conso = mtt_conso;
	}

	public Date getDate_conso() {
		return date_conso;
	}

	public void setDate_conso(Date date_conso) {
		this.date_conso = date_conso;
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
	public Integer getNbr_point_conso() {
		return nbr_point_conso;
	}

	public void setNbr_point_conso(Integer nbr_point_conso) {
		this.nbr_point_conso = nbr_point_conso;
	}
}
