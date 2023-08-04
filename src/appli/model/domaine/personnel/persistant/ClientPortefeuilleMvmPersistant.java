package appli.model.domaine.personnel.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "client_portefeuille_mvm", indexes={
		@Index(name="IDX_CLI_PRTF_MVM_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name="portefeuille_mvm_cli_find", query="from ClientPortefeuilleMvmPersistant clientPortefeuilleMvm "
			+ "where clientPortefeuilleMvm.sens='[sens]' and clientPortefeuilleMvm.opc_client.id='{clientId}' "
			+ "order by clientPortefeuilleMvm.date_mvm desc"),
	@NamedQuery(name="portefeuille_mvm_soc_find", query="from ClientPortefeuilleMvmPersistant clientPortefeuilleMvm "
			+ "where clientPortefeuilleMvm.sens='[sens]' and clientPortefeuilleMvm.opc_societeLivr.id='{socLivrId}' "
			+ "order by clientPortefeuilleMvm.date_mvm desc")
})
public class ClientPortefeuilleMvmPersistant extends BasePersistant  {
	 @Column(length = 1, nullable=false)
	 private String sens;
	 
	@Column(length = 20, nullable=false)
	private String mode_paie;
	 
	@Column(length = 15, scale = 6, precision = 15, nullable=false)
	private BigDecimal montant;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date date_mvm;
	@Column
	private Boolean is_annule;
	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName="id")
	private ClientPersistant opc_client;
	@ManyToOne
	@JoinColumn(name = "societeLivr_id", referencedColumnName="id")
	private SocieteLivrPersistant opc_societeLivr;
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private UserPersistant opc_user;// User qui a effectuer la commande + encaissement
	@ManyToOne
    @JoinColumn(name = "mouvement_id", referencedColumnName = "id")
	private MouvementPersistant opc_mvn;// Si vente BO
	@ManyToOne
    @JoinColumn(name = "caisse_mouvement_id", referencedColumnName = "id")
	private CaisseMouvementPersistant opc_mvn_caisse;//Si vente caisse

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public Date getDate_mvm() {
		return date_mvm;
	}

	public void setDate_mvm(Date date_mvm) {
		this.date_mvm = date_mvm;
	}

//	public ClientPortefeuillePersistant getOpc_portefeuille() {
//		return opc_portefeuille;
//	}
//
//	public void setOpc_portefeuille(ClientPortefeuillePersistant opc_portefeuille) {
//		this.opc_portefeuille = opc_portefeuille;
//	}

//	public CaisseMouvementPersistant getOpc_mvn_caisse() {
//		return opc_mvn_caisse;
//	}
//
//	public void setOpc_mvn_caisse(CaisseMouvementPersistant opc_mvn_caisse) {
//		this.opc_mvn_caisse = opc_mvn_caisse;
//	}

	public String getMode_paie() {
		return mode_paie;
	}

	public void setMode_paie(String mode_paie) {
		this.mode_paie = mode_paie;
	}

	public ClientPersistant getOpc_client() {
		return opc_client;
	}

	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}

	public SocieteLivrPersistant getOpc_societeLivr() {
		return opc_societeLivr;
	}

	public void setOpc_societeLivr(SocieteLivrPersistant opc_societeLivr) {
		this.opc_societeLivr = opc_societeLivr;
	}

	public UserPersistant getOpc_user() {
		return opc_user;
	}

	public void setOpc_user(UserPersistant opc_user) {
		this.opc_user = opc_user;
	}

	public MouvementPersistant getOpc_mvn() {
		return opc_mvn;
	}

	public void setOpc_mvn(MouvementPersistant opc_mvn) {
		this.opc_mvn = opc_mvn;
	}

	public CaisseMouvementPersistant getOpc_mvn_caisse() {
		return opc_mvn_caisse;
	}

	public void setOpc_mvn_caisse(CaisseMouvementPersistant opc_mvn_caisse) {
		this.opc_mvn_caisse = opc_mvn_caisse;
	}

	public Boolean getIs_annule() {
		return is_annule;
	}

	public void setIs_annule(Boolean is_annule) {
		this.is_annule = is_annule;
	}
}