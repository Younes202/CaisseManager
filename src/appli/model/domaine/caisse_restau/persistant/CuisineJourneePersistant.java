package appli.model.domaine.caisse_restau.persistant;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;
 
@Entity
@Table(name = "cuisine_journee", indexes={
		@Index(name="IDX_CUIS_JRN_FUNC", columnList="code_func"),
	})
public class CuisineJourneePersistant extends BasePersistant {
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date_ouverture;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date_cloture;
    
    @Column
    private Boolean is_Manuel;
    
	/*********************** Liens ************************************/
    
    @ManyToOne
    @JoinColumn(name = "user_ouverture_id", referencedColumnName="id")
    private UserPersistant opc_user_ouverture;// User qui a ouvert le shift
     
	@ManyToOne
	@JoinColumn(name = "user_cloture_id", referencedColumnName = "id")
	private UserPersistant opc_user_cloture;// Utilisateur qui a clôturé la cuisine
    
    @ManyToOne
    @JoinColumn(name = "cuisine_id", nullable=false, referencedColumnName="id")
    private CaissePersistant opc_cuisine;
    
    @OneToOne
    @JoinColumn(name = "inventaire_ouverture_id", referencedColumnName="id")
    private InventairePersistant opc_inventaire_ouverture;
    
    @OneToOne
    @JoinColumn(name = "inventaire_cloture_id", referencedColumnName="id")
    private InventairePersistant opc_inventaire_cloture;
    
    @GsonExclude
    @ManyToOne
    @JoinColumn(name = "journee_id", nullable=false, referencedColumnName="id")
    private JourneePersistant opc_journee;
    
	public Date getDate_ouverture() {
		return date_ouverture;
	}

	public void setDate_ouverture(Date date_ouverture) {
		this.date_ouverture = date_ouverture;
	}

	public Date getDate_cloture() {
		return date_cloture;
	}

	public void setDate_cloture(Date date_cloture) {
		this.date_cloture = date_cloture;
	}

	public UserPersistant getOpc_user_ouverture() {
		return opc_user_ouverture;
	}

	public void setOpc_user_ouverture(UserPersistant opc_user_ouverture) {
		this.opc_user_ouverture = opc_user_ouverture;
	}

	public UserPersistant getOpc_user_cloture() {
		return opc_user_cloture;
	}

	public void setOpc_user_cloture(UserPersistant opc_user_cloture) {
		this.opc_user_cloture = opc_user_cloture;
	}

	public CaissePersistant getOpc_cuisine() {
		return opc_cuisine;
	}

	public void setOpc_cuisine(CaissePersistant opc_cuisine) {
		this.opc_cuisine = opc_cuisine;
	}

	public InventairePersistant getOpc_inventaire_ouverture() {
		return opc_inventaire_ouverture;
	}

	public void setOpc_inventaire_ouverture(InventairePersistant opc_inventaire_ouverture) {
		this.opc_inventaire_ouverture = opc_inventaire_ouverture;
	}

	public InventairePersistant getOpc_inventaire_cloture() {
		return opc_inventaire_cloture;
	}

	public void setOpc_inventaire_cloture(InventairePersistant opc_inventaire_cloture) {
		this.opc_inventaire_cloture = opc_inventaire_cloture;
	}

	public JourneePersistant getOpc_journee() {
		return opc_journee;
	}

	public void setOpc_journee(JourneePersistant opc_journee) {
		this.opc_journee = opc_journee;
	}

	public Boolean getIs_Manuel() {
		return is_Manuel;
	}

	public void setIs_Manuel(Boolean is_Manuel) {
		this.is_Manuel = is_Manuel;
	}
    
}