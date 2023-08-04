package appli.model.domaine.vente.persistant;

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

import appli.model.domaine.administration.persistant.UserPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.GsonExclude;


/**
 *
 * @author hp
 */
@Entity
@Table(name = "caisse_journee", indexes={
		@Index(name="IDX_CAIS_JRN_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name = "caisseJournee_find", query="from CaisseJourneePersistant caisseJournee "
		+ "where caisseJournee.opc_caisse.id = '{caisse_id}' "
		+ "order by caisseJournee.opc_journee.date_journee desc"),
	@NamedQuery(name="caisseview_find", query="from CaisseJourneePersistant caisseV order by caisseV.opc_journee.date_journee desc")
})
public class CaisseJourneePersistant extends BasePersistant {
   

	@Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date_ouverture;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date_cloture;
    
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_ouverture;
    
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier;// Montant total de clôture net hors ouverture
    
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_espece;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_cb;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_cheque;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_dej;
    
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_old_espece;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_old_cb;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_old_cheque;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_old_dej;
    
	@Column
	private Integer nbr_vente;
    @Column(length = 1, nullable=false)
    private String statut_caisse;
    @Column(length = 3)
	private Integer nbr_livraison;
     @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_espece;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_cheque;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_dej;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_cb;
	 
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_portefeuille;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_donne_point;
	 
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_total;// Hors réduction
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_total_net;// Avec prise en compte des réduction
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_annule;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_annule_ligne;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_reduction;
	 @Column(length = 15, scale = 6, precision = 15)
		private BigDecimal mtt_art_reduction;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_art_offert;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_marge_caissier;
     @Column(length = 5)
     private Integer nbr_dash_open;
     
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private UserPersistant opc_user;// User qui a ouvert le shift
    
	@ManyToOne
	@JoinColumn(name = "user_caisse_id", referencedColumnName = "id")
	private UserPersistant opc_user_cloture;// Utilisateur qui a clôturé la caisse
    
    @ManyToOne
    @JoinColumn(name = "caisse_id", nullable=false, referencedColumnName="id")
    private CaissePersistant opc_caisse;

    @ManyToOne
    @JoinColumn(name = "journee_id", nullable=false, referencedColumnName="id")
    private JourneePersistant opc_journee;
    
    @GsonExclude
    @OneToMany
	@JoinColumn(name = "caisse_journee_id", referencedColumnName = "id", insertable=false, updatable=false)
    private List<CaisseMouvementPersistant> list_caisse_mouvement;

    public UserPersistant getOpc_user() {
		return opc_user;
	}

	public void setOpc_user(UserPersistant opc_user) {
		this.opc_user = opc_user;
	}

	public BigDecimal getMtt_ouverture() {
        return mtt_ouverture;
    }

    public void setMtt_ouverture(BigDecimal mtt_ouverture) {
        this.mtt_ouverture = mtt_ouverture;
    }

    public CaissePersistant getOpc_caisse() {
        return opc_caisse;
    }

    public void setOpc_caisse(CaissePersistant opc_caisse) {
        this.opc_caisse = opc_caisse;
    }

    public BigDecimal getMtt_cloture_caissier() {
        return mtt_cloture_caissier;
    }

    public void setMtt_cloture_caissier(BigDecimal mtt_cloture_caissier) {
        this.mtt_cloture_caissier = mtt_cloture_caissier;
    }

	public String getStatut_caisse() {
		return statut_caisse;
	}

	public void setStatut_caisse(String statut_caisse) {
		this.statut_caisse = statut_caisse;
	}

	public JourneePersistant getOpc_journee() {
		return opc_journee;
	}

	public void setOpc_journee(JourneePersistant opc_journee) {
		this.opc_journee = opc_journee;
	}

	public UserPersistant getOpc_user_cloture() {
		return opc_user_cloture;
	}

	public void setOpc_user_cloture(UserPersistant opc_user_cloture) {
		this.opc_user_cloture = opc_user_cloture;
	}

	public List<CaisseMouvementPersistant> getList_caisse_mouvement() {
		return list_caisse_mouvement;
	}

	public void setList_caisse_mouvement(List<CaisseMouvementPersistant> list_caisse_mouvement) {
		this.list_caisse_mouvement = list_caisse_mouvement;
	}

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

	public BigDecimal getMtt_cloture_caissier_espece() {
		return mtt_cloture_caissier_espece;
	}

	public void setMtt_cloture_caissier_espece(BigDecimal mtt_cloture_caissier_espece) {
		this.mtt_cloture_caissier_espece = mtt_cloture_caissier_espece;
	}

	public BigDecimal getMtt_cloture_caissier_cb() {
		return mtt_cloture_caissier_cb;
	}

	public void setMtt_cloture_caissier_cb(BigDecimal mtt_cloture_caissier_cb) {
		this.mtt_cloture_caissier_cb = mtt_cloture_caissier_cb;
	}

	public BigDecimal getMtt_cloture_caissier_cheque() {
		return mtt_cloture_caissier_cheque;
	}

	public void setMtt_cloture_caissier_cheque(BigDecimal mtt_cloture_caissier_cheque) {
		this.mtt_cloture_caissier_cheque = mtt_cloture_caissier_cheque;
	}

	public BigDecimal getMtt_cloture_caissier_dej() {
		return mtt_cloture_caissier_dej;
	}

	public void setMtt_cloture_caissier_dej(BigDecimal mtt_cloture_caissier_dej) {
		this.mtt_cloture_caissier_dej = mtt_cloture_caissier_dej;
	}

	public BigDecimal getMtt_cloture_old_espece() {
		return mtt_cloture_old_espece;
	}

	public void setMtt_cloture_old_espece(BigDecimal mtt_cloture_old_espece) {
		this.mtt_cloture_old_espece = mtt_cloture_old_espece;
	}

	public BigDecimal getMtt_cloture_old_cb() {
		return mtt_cloture_old_cb;
	}

	public void setMtt_cloture_old_cb(BigDecimal mtt_cloture_old_cb) {
		this.mtt_cloture_old_cb = mtt_cloture_old_cb;
	}

	public BigDecimal getMtt_cloture_old_cheque() {
		return mtt_cloture_old_cheque;
	}

	public void setMtt_cloture_old_cheque(BigDecimal mtt_cloture_old_cheque) {
		this.mtt_cloture_old_cheque = mtt_cloture_old_cheque;
	}

	public BigDecimal getMtt_cloture_old_dej() {
		return mtt_cloture_old_dej;
	}

	public void setMtt_cloture_old_dej(BigDecimal mtt_cloture_old_dej) {
		this.mtt_cloture_old_dej = mtt_cloture_old_dej;
	}

	public BigDecimal getMtt_espece() {
		return mtt_espece;
	}

	public void setMtt_espece(BigDecimal mtt_espece) {
		this.mtt_espece = mtt_espece;
	}

	public BigDecimal getMtt_cheque() {
		return mtt_cheque;
	}

	public void setMtt_cheque(BigDecimal mtt_cheque) {
		this.mtt_cheque = mtt_cheque;
	}

	public BigDecimal getMtt_dej() {
		return mtt_dej;
	}

	public void setMtt_dej(BigDecimal mtt_dej) {
		this.mtt_dej = mtt_dej;
	}

	public BigDecimal getMtt_cb() {
		return mtt_cb;
	}

	public void setMtt_cb(BigDecimal mtt_cb) {
		this.mtt_cb = mtt_cb;
	}

	public BigDecimal getMtt_total() {
		return mtt_total;
	}

	public void setMtt_total(BigDecimal mtt_total) {
		this.mtt_total = mtt_total;
	}

	public BigDecimal getMtt_total_net() {
		return mtt_total_net;
	}

	public void setMtt_total_net(BigDecimal mtt_total_net) {
		this.mtt_total_net = mtt_total_net;
	}

	public BigDecimal getMtt_annule() {
		return mtt_annule;
	}

	public void setMtt_annule(BigDecimal mtt_annule) {
		this.mtt_annule = mtt_annule;
	}

	public BigDecimal getMtt_reduction() {
		return mtt_reduction;
	}

	public void setMtt_reduction(BigDecimal mtt_reduction) {
		this.mtt_reduction = mtt_reduction;
	}

	public BigDecimal getMtt_art_offert() {
		return mtt_art_offert;
	}

	public void setMtt_art_offert(BigDecimal mtt_art_offert) {
		this.mtt_art_offert = mtt_art_offert;
	}

	public Integer getNbr_livraison() {
		return nbr_livraison;
	}

	public void setNbr_livraison(Integer nbr_livraison) {
		this.nbr_livraison = nbr_livraison;
	}

	public Integer getNbr_vente() {
		return nbr_vente;
	}

	public void setNbr_vente(Integer nbr_vente) {
		this.nbr_vente = nbr_vente;
	}	
	
	public BigDecimal getEcartCalcule() {
		return BigDecimalUtil.substract(this.mtt_cloture_caissier, this.mtt_total_net, this.mtt_ouverture);
	}

	public Integer getNbr_dash_open() {
		return nbr_dash_open==null?0:nbr_dash_open;
	}

	public void setNbr_dash_open(Integer nbr_dash_open) {
		this.nbr_dash_open = nbr_dash_open;
	}

	public BigDecimal getMtt_marge_caissier() {
		return mtt_marge_caissier;
	}

	public void setMtt_marge_caissier(BigDecimal mtt_marge_caissier) {
		this.mtt_marge_caissier = mtt_marge_caissier;
	}

	public BigDecimal getMtt_portefeuille() {
		return mtt_portefeuille;
	}

	public void setMtt_portefeuille(BigDecimal mtt_portefeuille) {
		this.mtt_portefeuille = mtt_portefeuille;
	}

	public BigDecimal getMtt_donne_point() {
		return mtt_donne_point;
	}

	public void setMtt_donne_point(BigDecimal mtt_donne_point) {
		this.mtt_donne_point = mtt_donne_point;
	}

	public BigDecimal getMtt_annule_ligne() {
		return mtt_annule_ligne;
	}

	public void setMtt_annule_ligne(BigDecimal mtt_annule_ligne) {
		this.mtt_annule_ligne = mtt_annule_ligne;
	}
	 public BigDecimal getMtt_art_reduction() {
			return mtt_art_reduction;
		}

		public void setMtt_art_reduction(BigDecimal mtt_art_reduction) {
			this.mtt_art_reduction = mtt_art_reduction;
		}
}
	
	