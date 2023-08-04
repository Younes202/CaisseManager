package appli.model.domaine.vente.persistant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.SocieteLivrPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.GsonExclude;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "caisse_mouvement", indexes = {
		@Index(name="IDX_CM_BARRE", columnList="code_barre"),
		@Index(name="IDX_CM_REF", columnList="ref_commande"),
		@Index(name="IDX_CM_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name = "caisseMouvement_find", query = "from CaisseMouvementPersistant caisseMouvement "
			+ "where caisseMouvement.opc_caisse_journee.opc_caisse.id = '[caisseId]' "
			+ "and caisseMouvement.opc_caisse_journee.opc_journee.id = '[journeeId]' "
			+ "and caisseMouvement.opc_user.id = '[caissierId]' "
			+ "and caisseMouvement.date_vente>='[dateDebut]' and caisseMouvement.date_vente<='[dateFin]' "
			+ "order by caisseMouvement.opc_caisse_journee.opc_journee.id asc, "
			+ "caisseMouvement.opc_caisse_journee.opc_caisse.id asc, "
			+ "caisseMouvement.opc_caisse_journee.id desc, "
			+ "caisseMouvement.id desc"),
	// Employés et clients
	@NamedQuery(name = "caisseMouvement_tiers_find", query = "from CaisseMouvementPersistant caisseMouvement "
			+ "where caisseMouvement.opc_client.id = '[clientId]' "
			+ "and caisseMouvement.opc_employe.id = '[employeId]' "
			+ "order by caisseMouvement.id desc"),
	// Employés et clients réductions
	@NamedQuery(name = "caisseMouvement_reduc_find", query = "from CaisseMouvementPersistant caisseMouvement "
			+ "where caisseMouvement.opc_client.id = '[clientId]' "
			+ "and caisseMouvement.opc_employe.id = '[employeId]' "
			+ "and (caisseMouvement.mtt_reduction is not null and caisseMouvement.mtt_reduction > 0) "
			+ "order by caisseMouvement.opc_caisse_journee.opc_journee.id desc, caisseMouvement.opc_caisse_journee.opc_caisse.id, caisseMouvement.opc_caisse_journee.id"),
	// Mouvements avec réduction
	@NamedQuery(name = "mouvementReduction_find", query = "from CaisseMouvementPersistant caisseMouvement " + 
			"where ((caisseMouvement.mtt_reduction is not null and caisseMouvement.mtt_reduction > 0) " + 
			"or (caisseMouvement.mtt_art_offert is not null and caisseMouvement.mtt_art_offert > 0)) " + 
			"and caisseMouvement.opc_caisse_journee.opc_journee.id>='[dateDebut]' and caisseMouvement.opc_caisse_journee.opc_journee.id<='[dateFin]' " +
			"order by caisseMouvement.opc_caisse_journee.opc_journee.id desc, caisseMouvement.opc_caisse_journee.opc_caisse.id, "
			+ "caisseMouvement.opc_caisse_journee.id, caisseMouvement.id desc"),
	// Changement de statut des commandes
//	@NamedQuery(name = "suiviStatutCmd_find", query = "from CaisseMouvementPersistant caisseMouvement "
//			+ "where caisseMouvement.date_vente>='[dateDebut]' and caisseMouvement.date_vente<='[dateFin]' "
//			+ "order by caisseMouvement.opc_caisse_journee.opc_caisse.reference, caisseMouvement.date_vente desc"),
	//Movements des annulations
	@NamedQuery(name = "annulationMouvement_find", query = "from CaisseMouvementPersistant caisseMouvement "
			+ "where caisseMouvement.opc_caisse_journee.opc_journee.id>='[dateDebut]' "
			+ "and caisseMouvement.opc_caisse_journee.opc_journee.id<='[dateFin]' "
			+ "and caisseMouvement.is_annule is not null and caisseMouvement.is_annule=1 "
			+ "order by caisseMouvement.opc_caisse_journee.opc_journee.id desc, "
			+ "caisseMouvement.opc_caisse_journee.opc_caisse.id, "
			+ "caisseMouvement.opc_caisse_journee.id, caisseMouvement.id desc"),
	
	@NamedQuery(name = "annulationMouvementDet_find", query = "from CaisseMouvementPersistant caisseMouvement "
			+ "where caisseMouvement.opc_caisse_journee.opc_journee.id>='[dateDebut]' "
			+ "and caisseMouvement.opc_caisse_journee.opc_journee.id<='[dateFin]' "
			+ "and (caisseMouvement.is_annule is null or caisseMouvement.is_annule=0) "
			+ "and mtt_annul_ligne > 0 "
			+ "order by caisseMouvement.opc_caisse_journee.opc_journee.id desc, "
			+ "caisseMouvement.opc_caisse_journee.opc_caisse.id, "
			+ "caisseMouvement.opc_caisse_journee.id, caisseMouvement.id desc"),
	//Movements des changement de quantuté
	@NamedQuery(name = "changeQteMouvement_find", query = "select distinct caisseMouvementArticle.opc_mouvement_caisse from CaisseMouvementArticlePersistant caisseMouvementArticle "
			+ "where caisseMouvementArticle.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id>='[dateDebut]' "
			+ "and caisseMouvementArticle.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id<='[dateFin]' "
			+ "and (caisseMouvementArticle.opc_mouvement_caisse.is_annule is null or caisseMouvementArticle.opc_mouvement_caisse.is_annule=0) "
			+ "and (caisseMouvementArticle.is_annule is null or caisseMouvementArticle.is_annule is null or caisseMouvementArticle.is_annule=0) "
			+ "and caisseMouvementArticle.old_qte_line is not null and caisseMouvementArticle.old_qte_line!='' "
			+ "and (caisseMouvementArticle.quantite - caisseMouvementArticle.old_qte_line) < 0 "
			+ "order by caisseMouvementArticle.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id desc, "
			+ "caisseMouvementArticle.opc_mouvement_caisse.opc_caisse_journee.opc_caisse.id desc, "
			+ "caisseMouvementArticle.opc_mouvement_caisse.id desc")
})
public class CaisseMouvementPersistant extends BasePersistant {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(length = 50)
	private String code_barre;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_vente;
    @Column
    private Boolean is_devise;// En devise
	@Column
	private Date date_encais;// Date encaissement
	@Column(length = 30, nullable = false)
	private String ref_commande;
    @Column(length = 30)
    private String num_token_cmd;
	@Column(length = 1)
	private String type_commande;// L=livraison, P=Sur place, E=A emporter
//	@Column(length = 20)
//	private String ref_table;
	
	@Column(length = 15, scale = 6, precision = 15) 
	private BigDecimal mtt_donne;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_donne_cheque;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_donne_cb;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_donne_dej;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_portefeuille;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_donne_point;
	
	@Column(length = 10)
	private Integer nbr_donne_point;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_reduction;
	 @Column(length = 15, scale = 6, precision = 15)
		private BigDecimal mtt_art_reduction;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_art_offert;	// Total des articles offerts
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_marge_caissier;	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_livraison_ttl;// Total frais de livraison	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_livraison_livr;// Part livreur
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_annul_ligne;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_commande;// Montant de la commande hors réduction
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_commande_net;// Montant de la commande avec prise en compte de la réduction
	
	@Transient
	private BigDecimal mtt_a_rendre;
	@Transient
	private BigDecimal mtt_donne_all;
	
    @Column(length = 5)  
    private Integer max_idx_client; 
    
	@Column(length = 30)
	private String mode_paiement;//cb, es, de, ch, pt, pf
	@Column
	private Boolean is_annule;
	@Column
	private Boolean is_retour;
	@Column(length = 10, nullable = false)
	private String last_statut;// Dernier statut
    @Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_debut_stat;//Date début statut
    @Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_fin_stat;//Date fin statut
    @Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_debut_stat2;//Date début statut
    @Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_fin_stat2;//Date fin statut
	@Column
	@Lob
	private String caisse_cuisine;// Elément à destination de cuisine format "caisseId:elementId;caisseId:elementId;"
	@Column
	private Boolean is_imprime;
	@Column(length = 20)
	private String statut_livreur;
	
	//---------------------- INFOS FACTURE ---------------------------
	@Transient
	private String num_facture;// Pour archivage des factures
	@Transient
	private String nom_facture;
	@Transient
	private String adresse_facture;
	@Transient
	private Date date_facture;
	//----------------------------------------------------------------
	
	//---------------------- Commande en ligne -----------------------
	/** 
	 Source de la commande : 
	 	MOB_ENV : Depuis mobile app, 
	 	WEB_CLI : commande en ligne web widget
	 	LOCAL_CLI : local client
	 	LOCAL : Local établissement
	 	CLOUD : Depuis le cloud
	 */
	@Column(length = 30)
    private String src_cmd;
	@Column(length = 20)
	private String adresse_ip;
	@Column(length = 25, scale = 20, precision = 25)
	private BigDecimal pos_lat;
	@Column(length = 25, scale = 20, precision = 25)
	private BigDecimal pos_lng;
    @Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_souhaite;//Date commande souhaitée
    @Column
    private Boolean is_tovalidate;// Si la commande est à valider ou non
	//----------------------------------------------------------------
	
	@ManyToOne
	@JoinColumn(name = "caisse_journee_id", nullable = false, referencedColumnName = "id")
	private CaisseJourneePersistant opc_caisse_journee;
	
	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName = "id")
	private ClientPersistant opc_client;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private UserPersistant opc_user;// User qui a effectuer la commande + encaissement
    @ManyToOne
    @JoinColumn(name = "user_confirm_id", referencedColumnName="id")
    private UserPersistant opc_user_confirm;// User qui a confirmé
    @ManyToOne
    @JoinColumn(name = "user_annul_id", referencedColumnName="id")
    private UserPersistant opc_user_annul;// User qui a annulé
    @ManyToOne
    @JoinColumn(name = "serveur_id", referencedColumnName="id")
    private UserPersistant opc_serveur;// Serveur
	@ManyToOne
	@JoinColumn(name = "livreurU_id", referencedColumnName = "id")
	private UserPersistant opc_livreurU;
	
    @ManyToOne
    @JoinColumn(name = "user_lock_id", referencedColumnName="id")
    private UserPersistant opc_user_lock;// User qui a la commande dans le chario
	
	@ManyToOne
	@JoinColumn(name = "societe_livr_id", referencedColumnName = "id")
	private SocieteLivrPersistant opc_societe_livr; 
	@ManyToOne
	@JoinColumn(name = "employe_id", referencedColumnName = "id")
	private EmployePersistant opc_employe;// Si employe ayant commande
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_annul;//Date d'annulation
    
	@Column(length = 80)
	private String mvm_retour_ids;
	@Column(length = 80)
	private String mvm_stock_ids;// Mouvements de stock liés à ce mouvement caisse
	
	@GsonExclude
    @OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mvm_caisse_id", referencedColumnName = "id")
	private List<CaisseMouvementOffrePersistant> list_offre;

	@GsonExclude
	@OrderBy("idx_element, id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mvm_caisse_id", referencedColumnName = "id")
	private List<CaisseMouvementArticlePersistant> list_article;

//	@GsonExclude
//	@OrderBy(value = "id asc")
//	@OneToMany
//	@JoinColumn(name = "caisse_mvm_id", referencedColumnName = "id")
//	private List<MouvementPersistant> list_mvm_stock;// Car une commande peut générer polusieurs commandes stock 
	 
	 @GsonExclude
	@OrderBy(value = "date_statut desc")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "caisse_mvm_id", referencedColumnName = "id")
	 private List<CaisseMouvementStatutPersistant> list_statut;
	 
	 @GsonExclude
	@Transient
	private List<CaisseMouvementArticlePersistant> listEncaisse;
	
	public CaisseJourneePersistant getOpc_caisse_journee() {
		return opc_caisse_journee;
	}

	public void setOpc_caisse_journee(CaisseJourneePersistant opc_caisse_journee) {
		this.opc_caisse_journee = opc_caisse_journee;
	}

	public ClientPersistant getOpc_client() {
		return opc_client;
	}

	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}

	public Date getDate_vente() {
		return date_vente;
	}

	public void setDate_vente(Date date_vente) {
		this.date_vente = date_vente;
	}

	public EmployePersistant getOpc_employe() {
		return opc_employe;
	}

	public void setOpc_employe(EmployePersistant opc_employe) {
		this.opc_employe = opc_employe;
	}
	public String getRef_commande() {
		return ref_commande;
	}

	public void setRef_commande(String ref_commande) {
		this.ref_commande = ref_commande;
	}

	public String getType_commande() {
		return type_commande;
	}

	public void setType_commande(String type_commande) {
		this.type_commande = type_commande;
	}

	public BigDecimal getMtt_commande() {
		return mtt_commande;
	}

	public void setMtt_commande(BigDecimal mtt_commande) {
		this.mtt_commande = mtt_commande;
	}

	public BigDecimal getMtt_donne() {
		return mtt_donne;
	}

	public void setMtt_donne(BigDecimal mtt_donne) {
		this.mtt_donne = mtt_donne;
	}

	public BigDecimal getMtt_reduction() {
		return mtt_reduction;
	}

	public void setMtt_reduction(BigDecimal mtt_reduction) {
		this.mtt_reduction = mtt_reduction;
	}

	public BigDecimal getMtt_commande_net() {
		return mtt_commande_net;
	}

	public void setMtt_commande_net(BigDecimal mtt_commande_net) {
		this.mtt_commande_net = mtt_commande_net;
	}

	public String getMode_paiement() {
		return mode_paiement;
	}

	public void setMode_paiement(String mode_paiement) {
		this.mode_paiement = mode_paiement;
	}
	
	public Boolean getIs_annule() {
		return is_annule;
	}

	public void setIs_annule(Boolean is_annule) {
		this.is_annule = is_annule;
	}

	public List<CaisseMouvementOffrePersistant> getList_offre() {
		return list_offre;
	}

	public void setList_offre(List<CaisseMouvementOffrePersistant> list_offre) {
		this.list_offre = list_offre;
	}

	public List<CaisseMouvementArticlePersistant> getList_article() {
		return list_article;
	}

	public void setList_article(List<CaisseMouvementArticlePersistant> list_article) {
		this.list_article = list_article;
	}

	public List<CaisseMouvementStatutPersistant> getList_statut() {
		return list_statut;
	}

	public void setList_statut(List<CaisseMouvementStatutPersistant> list_statut) {
		this.list_statut = list_statut;
	}

	public String getLast_statut() {
		return last_statut;
	}

	public void setLast_statut(String last_statut) {
		if(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(last_statut)) {
			if(this.getDate_debut_stat() == null) {
				this.setDate_debut_stat(new Date());
			}
		} else if(STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(last_statut)) {
			if(this.getDate_debut_stat() == null) {
				this.setDate_debut_stat(new Date());
			}
		} else if(STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(last_statut)) {
			if(this.getDate_fin_stat() == null) {
				this.setDate_fin_stat(new Date());
			}
			if(this.getDate_debut_stat2() == null) {
				this.setDate_debut_stat2(new Date());
			}
		} else if(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(last_statut)) {
			if(this.getDate_fin_stat2() == null) {
				this.setDate_fin_stat2(new Date());
			}
		}
		this.last_statut = last_statut;
	}

	public String getNum_token_cmd() {
		return num_token_cmd;
	}

	public void setNum_token_cmd(String num_token_cmd) {
		this.num_token_cmd = num_token_cmd;
	}

	public BigDecimal getMtt_donne_cheque() {
		return mtt_donne_cheque;
	}

	public void setMtt_donne_cheque(BigDecimal mtt_donne_cheque) {
		this.mtt_donne_cheque = mtt_donne_cheque;
	}

	public BigDecimal getMtt_donne_cb() {
		return mtt_donne_cb;
	}

	public void setMtt_donne_cb(BigDecimal mtt_donne_cb) {
		this.mtt_donne_cb = mtt_donne_cb;
	}

	public BigDecimal getMtt_donne_dej() {
		return mtt_donne_dej;
	}

	public void setMtt_donne_dej(BigDecimal mtt_donne_dej) {
		this.mtt_donne_dej = mtt_donne_dej;
	}
//
//	public String getRef_table() {
//		return ref_table;
//	}
//
//	public void setRef_table(String ref_table) {
//		this.ref_table = ref_table;
//	}

	public BigDecimal getMtt_art_offert() {
		return mtt_art_offert;
	}

	public void setMtt_art_offert(BigDecimal mtt_art_offert) {
		this.mtt_art_offert = mtt_art_offert;
	}
	
	public BigDecimal getMtt_marge_caissier() {
		return mtt_marge_caissier;
	}

	public void setMtt_marge_caissier(BigDecimal mtt_marge_caissier) {
		this.mtt_marge_caissier = mtt_marge_caissier;
	}

	public Boolean getIs_imprime() {
		return is_imprime;
	}

	public void setIs_imprime(Boolean is_imprime) {
		this.is_imprime = is_imprime;
	}

//	public EmployePersistant getOpc_livreur() {
//		return opc_livreur;
//	}
//
//	public void setOpc_livreur(EmployePersistant opc_livreur) {
//		this.opc_livreur = opc_livreur;
//	}

	public Integer getMax_idx_client() {
		return max_idx_client;
	}

	public void setMax_idx_client(Integer max_idx_client) {
		this.max_idx_client = max_idx_client;
	}

	public UserPersistant getOpc_user() {
		return opc_user;
	}

	public void setOpc_user(UserPersistant opc_user) {
		this.opc_user = opc_user;
	}

	public BigDecimal getMtt_donne_point() {
		return mtt_donne_point;
	}

	public UserPersistant getOpc_livreurU() {
		return opc_livreurU;
	}

	public void setOpc_livreurU(UserPersistant opc_livreurU) {
		this.opc_livreurU = opc_livreurU;
	}

	public void setMtt_donne_point(BigDecimal mtt_donne_point) {
		this.mtt_donne_point = mtt_donne_point;
	}

	public Integer getNbr_donne_point() {
		return nbr_donne_point;
	}

	public void setNbr_donne_point(Integer nbr_donne_point) {
		this.nbr_donne_point = nbr_donne_point;
	}

	public BigDecimal getMtt_portefeuille() {
		return mtt_portefeuille;
	}

	public void setMtt_portefeuille(BigDecimal mtt_portefeuille) {
		this.mtt_portefeuille = mtt_portefeuille;
	}

	public String getCaisse_cuisine() {
		return caisse_cuisine;
	}

	public void setCaisse_cuisine(String caisse_cuisine) {
		this.caisse_cuisine = caisse_cuisine;
	}

	public String getOffreStr() {
		String offre = "";
		if(this.list_offre != null) {
			for (CaisseMouvementOffrePersistant offreP : list_offre) {
				if(!BooleanUtil.isTrue(offreP.getIs_annule())){
					if("P".equals(offreP.getOpc_offre().getType_offre())) {
						offre = "Prix d'achat"+";";
					} else {
						offre = offreP.getOpc_offre().getTaux_reduction().intValue()+"%;";
					}
				}
			}
			if(!"".equals(offre)) {
				offre = offre.substring(0, offre.length()-1);
			}
		}
		return offre;
	}

	public BigDecimal getMtt_a_rendre() {
		return mtt_a_rendre;
	}

	public void setMtt_a_rendre(BigDecimal mtt_a_rendre) {
		this.mtt_a_rendre = mtt_a_rendre;
	}

	public BigDecimal getMtt_donne_all() {
		return mtt_donne_all;
	}

	public void setMtt_donne_all(BigDecimal mtt_donne_all) {
		this.mtt_donne_all = mtt_donne_all;
	}

	public UserPersistant getOpc_serveur() {
		return opc_serveur;
	}

	public void setOpc_serveur(UserPersistant opc_serveur) {
		this.opc_serveur = opc_serveur;
	}

	public String getCode_barre() {
		return code_barre;
	}

	public void setCode_barre(String code_barre) {
		this.code_barre = code_barre;
	}

	public String getRefTablesDetail(){
		String tableSt = ",";
		if(this.list_article != null){
			for (CaisseMouvementArticlePersistant det : list_article) {
				if(BooleanUtil.isTrue(det.getIs_annule())) {
					continue;
				}
				if(StringUtil.isNotEmpty(det.getRef_table()) && tableSt.indexOf(","+det.getRef_table()+",")==-1){
					tableSt = tableSt+det.getRef_table()+",";
				}
			}
			if(tableSt.length()>1  && tableSt.endsWith(",")){
				tableSt = tableSt.substring(1, tableSt.length()-1);
			} else{
				tableSt = "";
			}
		}
		return tableSt;
	}

	public Boolean getIs_retour() {
		return is_retour;
	}

	public void setIs_retour(Boolean is_retour) {
		this.is_retour = is_retour;
	}

	public List<CaisseMouvementArticlePersistant> getListEncaisse() {
		return listEncaisse;
	}

	public void setListEncaisse(List<CaisseMouvementArticlePersistant> listEncaisse) {
		this.listEncaisse = listEncaisse;
	}

//	public List<MouvementPersistant> getList_mvm_stock() {
//		return list_mvm_stock;
//	}
//
//	public void setList_mvm_stock(List<MouvementPersistant> list_mvm_stock) {
//		this.list_mvm_stock = list_mvm_stock;
//	}

//	public MouvementPersistant getOpc_mouvement() {
//		return opc_mouvement;
//	}
//
//	public void setOpc_mouvement(MouvementPersistant opc_mouvement) {
//		this.opc_mouvement = opc_mouvement;
//	}

	public SocieteLivrPersistant getOpc_societe_livr() {
		return opc_societe_livr;
	}

	public void setOpc_societe_livr(SocieteLivrPersistant opc_societe_livr) {
		this.opc_societe_livr = opc_societe_livr;
	}

	public BigDecimal getMtt_livraison_ttl() {
		return mtt_livraison_ttl;
	}

	public void setMtt_livraison_ttl(BigDecimal mtt_livraison_ttl) {
		this.mtt_livraison_ttl = mtt_livraison_ttl;
	}

	public BigDecimal getMtt_livraison_livr() {
		return mtt_livraison_livr;
	}

	public void setMtt_livraison_livr(BigDecimal mtt_livraison_livr) {
		this.mtt_livraison_livr = mtt_livraison_livr;
	}

	public String getNum_facture() {
		return num_facture;
	}

	public void setNum_facture(String num_facture) {
		this.num_facture = num_facture;
	}

	public String getNom_facture() {
		return nom_facture;
	}

	public void setNom_facture(String nom_facture) {
		this.nom_facture = nom_facture;
	}

	public String getAdresse_facture() {
		return adresse_facture;
	}

	public void setAdresse_facture(String adresse_facture) {
		this.adresse_facture = adresse_facture;
	}

	public Date getDate_facture() {
		return date_facture;
	}

	public void setDate_facture(Date date_facture) {
		this.date_facture = date_facture;
	}

	public UserPersistant getOpc_user_annul() {
		return opc_user_annul;
	}

	public void setOpc_user_annul(UserPersistant opc_user_annul) {
		this.opc_user_annul = opc_user_annul;
	}

	public String getMvm_retour_ids() {
		return mvm_retour_ids;
	}

	public void setMvm_retour_ids(String mvm_retour_ids) {
		this.mvm_retour_ids = mvm_retour_ids;
	}
	
	public boolean isMvmRecharge(){
		if(this.list_article != null && this.list_article.size() == 1
				&& ContextAppli.TYPE_LIGNE_COMMANDE.RECHARGE_PF.toString().equals(this.list_article.get(0).getType_ligne())){
			return true;
		}
		return false;
	}
	
	public BigDecimal getMtt_annul_ligne() {
		return mtt_annul_ligne;
	}

	public void setMtt_annul_ligne(BigDecimal mtt_annul_ligne) {
		this.mtt_annul_ligne = mtt_annul_ligne;
	}

	public Date getDate_annul() {
		return date_annul;
	}

	public void setDate_annul(Date date_annul) {
		this.date_annul = date_annul;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMvm_stock_ids() {
		return mvm_stock_ids;
	}

	public void setMvm_stock_ids(String mvm_stock_ids) {
		this.mvm_stock_ids = mvm_stock_ids;
	}

	public String getSrc_cmd() {
		return src_cmd;
	}

	public void setSrc_cmd(String src_cmd) {
		this.src_cmd = src_cmd;
	}

	public String getAdresse_ip() {
		return adresse_ip;
	}

	public void setAdresse_ip(String adresse_ip) {
		this.adresse_ip = adresse_ip;
	}

	public BigDecimal getPos_lat() {
		return pos_lat;
	}

	public void setPos_lat(BigDecimal pos_lat) {
		this.pos_lat = pos_lat;
	}

	public BigDecimal getPos_lng() {
		return pos_lng;
	}

	public void setPos_lng(BigDecimal pos_lng) {
		this.pos_lng = pos_lng;
	}

	public Date getDate_souhaite() {
		return date_souhaite;
	}

	public void setDate_souhaite(Date date_souhaite) {
		this.date_souhaite = date_souhaite;
	}

	public Date getDate_encais() {
		return date_encais;
	}

	public void setDate_encais(Date date_encais) {
		this.date_encais = date_encais;
	}

	public Date getDate_debut_stat() {
		return date_debut_stat;
	}

	public void setDate_debut_stat(Date date_debut_stat) {
		this.date_debut_stat = date_debut_stat;
	}

	public Date getDate_fin_stat() {
		return date_fin_stat;
	}

	public void setDate_fin_stat(Date date_fin_stat) {
		this.date_fin_stat = date_fin_stat;
	}

	public Date getDate_debut_stat2() {
		return date_debut_stat2;
	}

	public void setDate_debut_stat2(Date date_debut_stat2) {
		this.date_debut_stat2 = date_debut_stat2;
	}

	public Date getDate_fin_stat2() {
		return date_fin_stat2;
	}

	public void setDate_fin_stat2(Date date_fin_stat2) {
		this.date_fin_stat2 = date_fin_stat2;
	}

	public Boolean getIs_tovalidate() {
		return is_tovalidate;
	}

	public void setIs_tovalidate(Boolean is_tovalidate) {
		this.is_tovalidate = is_tovalidate;
	}

	public void negateMtt(){
		this.mtt_donne = BigDecimalUtil.negate(this.mtt_donne);
		this.mtt_donne_all = BigDecimalUtil.negate(this.mtt_donne_all);
		this.mtt_donne_cheque = BigDecimalUtil.negate(this.mtt_donne_cheque);
		this.mtt_commande = BigDecimalUtil.negate(this.mtt_commande);
		this.mtt_commande_net = BigDecimalUtil.negate(this.mtt_commande_net);
	}

	public String getStatut_livreur() {
		return statut_livreur;
	}

	public void setStatut_livreur(String statut_livreur) {
		this.statut_livreur = statut_livreur;
	}

	public UserPersistant getOpc_user_confirm() {
		return opc_user_confirm;
	}

	public void setOpc_user_confirm(UserPersistant opc_user_confirm) {
		this.opc_user_confirm = opc_user_confirm;
	}

	public UserPersistant getOpc_user_lock() {
		return opc_user_lock;
	}

	public void setOpc_user_lock(UserPersistant opc_user_lock) {
		this.opc_user_lock = opc_user_lock;
	}

	public Boolean getIs_devise() {
		return is_devise;
	}

	public void setIs_devise(Boolean is_devise) {
		this.is_devise = is_devise;
	}

	public BigDecimal getMtt_art_reduction() {
		return mtt_art_reduction;
	}

	public void setMtt_art_reduction(BigDecimal mtt_art_reduction) {
		this.mtt_art_reduction = mtt_art_reduction;
	}
}
