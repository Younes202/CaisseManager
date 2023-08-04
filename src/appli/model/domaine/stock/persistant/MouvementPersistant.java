package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Where;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.GsonExclude;


@Entity
@Table(name = "mouvement", indexes={
		@Index(name="IDX_MVM_FUNC", columnList="code_func"),
		@Index(name="IDX_MVM_NUM_BL", columnList="num_bl"),
		@Index(name="IDX_MVM_NUM_FAC", columnList="num_facture"),
		@Index(name="IDX_MVM_NUM_REC", columnList="num_recu")
	})
@NamedQueries({
	@NamedQuery(name="mouvement_detail_find", query="from MouvementArticlePersistant detail where detail.opc_article.id='{article_id}'" +
			" order by detail.opc_mouvement.date_mouvement desc"),
	
	@NamedQuery(name="mouvement_find", query="from MouvementPersistant mouvement where type_mvmnt='[type]'"
			+ " and (mouvement.is_groupant is null or mouvement.is_groupant=0) "
			+ "and mouvement.date_mouvement>='[dateDebut]' "
			+ "and mouvement.date_mouvement<='[dateFin]' "
			+ "and mouvement.opc_travaux.id='[travauxId]' "
			+ " order by mouvement.date_mouvement desc"),
	
	@NamedQuery(name="mouvement_groupe_find", query="from MouvementPersistant mouvement where type_mvmnt='[type]'" +
			" and mouvement.is_groupant is not null and mouvement.is_groupant=1 and mouvement.date_mouvement>='[dateDebut]' and mouvement.date_mouvement<='[dateFin]' " +
			" order by mouvement.date_mouvement desc"),
	
	@NamedQuery(name="mouvement_find2", query="from MouvementPersistant mouvement where type_mvmnt='[type]' order by mouvement.date_mouvement desc"),
	@NamedQuery(name="mouvement_tva_find", query="from MouvementPersistant mvm "
		+ "where (mvm.type_mvmnt='a' or mvm.type_mvmnt='v' or mvm.type_mvmnt='vc') and mvm.date_mouvement>='[dateDebut]' and mvm.date_mouvement<='[dateFin]' "
		+ "order by mvm.date_mouvement desc"),
	@NamedQuery(name="fournisseur_mvm_find", query="from MouvementPersistant mvm "
			+ "where (mvm.type_mvmnt='a' or mvm.type_mvmnt='av') and opc_fournisseur.id='{fournisseurId}' "
			+ "and mvm.montant_ttc>0 "
			+ "order by mvm.date_mouvement desc"),
	@NamedQuery(name="mouvement_transfo_find", query="from MouvementPersistant mouvement "
			+ "where mouvement.type_mvmnt='tr' and mouvement.opc_emplacement.id is not null "
			+ "order by mouvement.date_mouvement desc"),
	@NamedQuery(name="histo_prix_mvm_find", query="from MouvementArticlePersistant mouvementArticle "
			+ "where mouvementArticle.opc_article.id='{articleId}' "
			+ "and mouvementArticle.opc_mouvement.opc_fournisseur.id='[founrisseurId]' "
			+ "and mouvementArticle.opc_mouvement.opc_client.id='[clientId]' "
			+ "and mouvementArticle.opc_mouvement.type_mvmnt='{typeMvmnt}' "
			+ "group by mouvementArticle.opc_article.id, mouvementArticle.prix_ttc "
			+ "order by mouvementArticle.opc_mouvement.date_mouvement desc, mouvementArticle.id desc")
	})
public class MouvementPersistant extends BasePersistant  {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_mouvement;
	@Column(length = 50)
	private String code_barre;
	@Column(length = 50)
	private String num_bl;
	@Column
	private Boolean is_annule;

	@Column(length = 50)
	private String num_recu;
	
	@Column(length = 50)
	private String num_facture;

	@Column(length = 255)
	private String transfert_mvm_ids;// Avec des séparateur ;
	
	@Column(length = 50)
	private String type_commande;
	
	@Column(length = 2, nullable = false)
	private String type_mvmnt;
	
	@Column(length = 2)
	private String type_transfert;
	
	@Column(length = 255)
	private String commentaire;

	@Column(length = 1)
	private Boolean is_automatise;
	@Column(length = 1)
	private Boolean is_valide;// Pour le bon de commande et avoir pour validation
	private Boolean is_chiffre;// Pour le bon de commande et afficher les montants ou non
	@Column
	private Boolean is_facture_comptable;
	@Column(length = 120)
	private String retour_ref_cmd; //en cas de retour
		/*********************** interaction avec la centrale ************************************/
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_reception;
	@ManyToOne
	@JoinColumn(name = "demande_id", referencedColumnName="id")
	private DemandeTransfertPersistant opc_demande;
	@ManyToOne
	@JoinColumn(name = "travaux_id", referencedColumnName="id")
	private TravauxPersistant opc_travaux;
	@Column(length = 20)
	private Long origine_id;
	@Column(length = 20)
	private Long tache_id;
	
	@Column(length = 20)
	private Long mouvement_group_id;// Pour regrouper les achats
	@Column
	private Boolean is_groupant;// Si c'est un pere
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_ht;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_ttc;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_ht_rem;//Montant remise
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_ttc_rem;//Montant remise
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_tva;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal montant_annul;
	
	//---------------------- INFOS FACTURE ---------------------------
	@Transient
	private String nom_facture;
	@Transient
	private String adresse_facture;
	@Transient
	private Date date_facture;
	//----------------------------------------------------------------
	// Immobilisation -------------------
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_debut_amo;// Début amortissement
	@Column(length = 3)
	private Integer nbr_annee_amo;// Nombre d'année amortissement
	// ----------------------------------	
	
	@Transient
	@GsonExclude
	private List<MouvementPersistant> list_groupe;
	
	/*********************** Liens ************************************/
	@ManyToOne
	@JoinColumn(name = "type_perte_enum", referencedColumnName="id")
	private ValTypeEnumPersistant opc_type_perte_enum;
	
	@ManyToOne
	@JoinColumn(name = "famille_conso_id", referencedColumnName="id")
	private FamilleConsommationPersistant opc_famille_consommation;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "preparation_id", referencedColumnName="id")
//	private PreparationPersistant opc_preparation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emplacement_id", referencedColumnName="id")
	private EmplacementPersistant opc_emplacement;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_id", referencedColumnName="id")
	private EmplacementPersistant opc_destination;

	@GsonExclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mouvement_src_id", referencedColumnName="id")
	private MouvementPersistant opc_mouvement;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fournisseur_id", referencedColumnName="id")
	private FournisseurPersistant opc_fournisseur;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", referencedColumnName="id")
	private ClientPersistant opc_client;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventaire_id", referencedColumnName="id")
	private InventairePersistant opc_inventaire;

	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private UserPersistant opc_user;// User qui a effectuer la commande + encaissement

	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mouvement_id", referencedColumnName = "id")
	private List<MouvementArticlePersistant> list_article;
	
	@OrderBy("financement_enum")
	@OneToMany
	@JoinColumn(name = "elementId", referencedColumnName = "id", insertable=false, updatable=false)
	@ForeignKey(name = "none")
	@Where(clause = "source='ACHAT' or source='VENTE' or source='AVOIR' or source='RETOUR'")
	private List<PaiementPersistant> list_paiement;
	
	public Date getDate_mouvement() {
		return date_mouvement;
	}
	public void setDate_mouvement(Date date_mouvement) {
		this.date_mouvement = date_mouvement;
	}
	public String getNum_bl() {
		return num_bl;
	}
	public void setNum_bl(String num_bl) {
		this.num_bl = num_bl;
	}
	public String getNum_facture() {
		return num_facture;
	}
	public void setNum_facture(String num_facture) {
		this.num_facture = num_facture;
	}
	public String getType_mvmnt() {
		return type_mvmnt;
	}
	public void setType_mvmnt(String type_mvmnt) {
		this.type_mvmnt = type_mvmnt;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public ValTypeEnumPersistant getOpc_type_perte_enum() {
		return opc_type_perte_enum;
	}
	public void setOpc_type_perte_enum(ValTypeEnumPersistant opc_type_perte_enum) {
		this.opc_type_perte_enum = opc_type_perte_enum;
	}

	public FamilleConsommationPersistant getOpc_famille_consommation() {
		return opc_famille_consommation;
	}
	public void setOpc_famille_consommation(
			FamilleConsommationPersistant opc_famille_consommation) {
		this.opc_famille_consommation = opc_famille_consommation;
	}
//	public PreparationPersistant getOpc_preparation() {
//		return opc_preparation;
//	}
//	public void setOpc_preparation(PreparationPersistant opc_preparation) {
//		this.opc_preparation = opc_preparation;
//	}
	public EmplacementPersistant getOpc_emplacement() {
		return opc_emplacement;
	}
	public void setOpc_emplacement(EmplacementPersistant opc_emplacement) {
		this.opc_emplacement = opc_emplacement;
	}
	public FournisseurPersistant getOpc_fournisseur() {
		return opc_fournisseur;
	}
	public void setOpc_fournisseur(FournisseurPersistant opc_fournisseur) {
		this.opc_fournisseur = opc_fournisseur;
	}
	public EmplacementPersistant getOpc_destination() {
		return opc_destination;
	}
	public void setOpc_destination(EmplacementPersistant opc_destination) {
		this.opc_destination = opc_destination;
	}
	public List<MouvementArticlePersistant> getList_article() {
		return list_article;
	}
	public void setList_article(List<MouvementArticlePersistant> list_article) {
		this.list_article = list_article;
	}
	public MouvementPersistant getOpc_mouvement() {
		return opc_mouvement;
	}
	public void setOpc_mouvement(MouvementPersistant opc_mouvement) {
		this.opc_mouvement = opc_mouvement;
	}
	public Boolean getIs_automatise() {
		return is_automatise;
	}
	public void setIs_automatise(Boolean is_automatise) {
		this.is_automatise = is_automatise;
	}
//	public String getNum_bl_source() {
//		return num_bl_source;
//	}
//	public void setNum_bl_source(String num_bl_source) {
//		this.num_bl_source = num_bl_source;
//	}
//	public String getNum_facture_source() {
//		return num_facture_source;
//	}
//	public void setNum_facture_source(String num_facture_source) {
//		this.num_facture_source = num_facture_source;
//	}
	public InventairePersistant getOpc_inventaire() {
		return opc_inventaire;
	}
	public void setOpc_inventaire(InventairePersistant opc_inventaire) {
		this.opc_inventaire = opc_inventaire;
	}
//	public Date getDate_encaissement() {
//		return date_encaissement;
//	}
//	public void setDate_encaissement(Date date_encaissement) {
//		this.date_encaissement = date_encaissement;
//	}
	public Boolean getIs_facture_comptable() {
		return is_facture_comptable;
	}
	public void setIs_facture_comptable(Boolean is_facture_comptable) {
		this.is_facture_comptable = is_facture_comptable;
	}
	public Long getMouvement_group_id() {
		return mouvement_group_id;
	}
	public void setMouvement_group_id(Long mouvement_group_id) {
		this.mouvement_group_id = mouvement_group_id;
	}
	public Boolean getIs_groupant() {
		return is_groupant;
	}
	public void setIs_groupant(Boolean is_groupant) {
		this.is_groupant = is_groupant;
	}
	public BigDecimal getMontant_ht() {
		return montant_ht;
	}
	public void setMontant_ht(BigDecimal montant_ht) {
		this.montant_ht = montant_ht;
	}
	public BigDecimal getMontant_ttc() {
		return montant_ttc;
	}
	public void setMontant_ttc(BigDecimal montant_ttc) {
		this.montant_ttc = montant_ttc;
	}
	public BigDecimal getMontant_tva() {
		return montant_tva;
	}
	public void setMontant_tva(BigDecimal montant_tva) {
		this.montant_tva = montant_tva;
	}
	public List<MouvementPersistant> getList_groupe() {
		return list_groupe;
	}
	public void setList_groupe(List<MouvementPersistant> list_groupe) {
		this.list_groupe = list_groupe;
	}
	public String getNum_recu() {
		return num_recu;
	}
	public void setNum_recu(String num_recu) {
		this.num_recu = num_recu;
	}
//	public String getNum_recu_source() {
//		return num_recu_source;
//	}
//	public void setNum_recu_source(String num_recu_source) {
//		this.num_recu_source = num_recu_source;
//	}
	public BigDecimal getMontant_ttc_rem() {
		return montant_ttc_rem;
	}
	public void setMontant_ttc_rem(BigDecimal montant_ttc_rem) {
		this.montant_ttc_rem = montant_ttc_rem;
	}
	public void setList_paiement(List<PaiementPersistant> list_paiement) {
		this.list_paiement = list_paiement;
	}
	public List<PaiementPersistant> getList_paiement(){
		return this.list_paiement;
	}
	public Date getDate_reception() {
		return date_reception;
	}
	public void setDate_reception(Date date_reception) {
		this.date_reception = date_reception;
	}
	
	public DemandeTransfertPersistant getOpc_demande() {
		return opc_demande;
	}
	public void setOpc_demande(DemandeTransfertPersistant opc_demande) {
		this.opc_demande = opc_demande;
	}
	public Long getOrigine_id() {
		return origine_id;
	}
	public void setOrigine_id(Long origine_id) {
		this.origine_id = origine_id;
	}
	public String getRetour_ref_cmd() {
		return retour_ref_cmd;
	}
	public void setRetour_ref_cmd(String retour_ref_cmd) {
		this.retour_ref_cmd = retour_ref_cmd;
	}
//	public CaisseMouvementPersistant getOpc_caisse_mouvement() {
//		return opc_caisse_mouvement;
//	}
//	public void setOpc_caisse_mouvement(CaisseMouvementPersistant opc_caisse_mouvement) {
//		this.opc_caisse_mouvement = opc_caisse_mouvement;
//	}
	
	public String getType_transfert() {
		return type_transfert;
	}
//	public Long getCaisse_mvm_id() {
//		return caisse_mvm_id;
//	}
//	public void setCaisse_mvm_id(Long caisse_mvm_id) {
//		this.caisse_mvm_id = caisse_mvm_id;
//	}
	public void setType_transfert(String type_transfert) {
		this.type_transfert = type_transfert;
	}
	public Long getTache_id() {
		return tache_id;
	}
	public void setTache_id(Long tache_id) {
		this.tache_id = tache_id;
	}
	public ClientPersistant getOpc_client() {
		return opc_client;
	}
	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}
	public Boolean getIs_valide() {
		return is_valide;
	}
	public void setIs_valide(Boolean is_valide) {
		this.is_valide = is_valide;
	}
	public Boolean getIs_chiffre() {
		return is_chiffre;
	}
	public void setIs_chiffre(Boolean is_chiffre) {
		this.is_chiffre = is_chiffre;
	}
	public String getCode_barre() {
		return code_barre;
	}
	public void setCode_barre(String code_barre) {
		this.code_barre = code_barre;
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
	public BigDecimal getMontant_ht_rem() {
		return montant_ht_rem;
	}
	public void setMontant_ht_rem(BigDecimal montant_ht_rem) {
		this.montant_ht_rem = montant_ht_rem;
	}
	public TravauxPersistant getOpc_travaux() {
		return opc_travaux;
	}
	public void setOpc_travaux(TravauxPersistant opc_travaux) {
		this.opc_travaux = opc_travaux;
	}
	public Date getDate_debut_amo() {
		return date_debut_amo;
	}

	public void setDate_debut_amo(Date date_debut_amo) {
		this.date_debut_amo = date_debut_amo;
	}

	public Integer getNbr_annee_amo() {
		return nbr_annee_amo;
	}

	public void setNbr_annee_amo(Integer nbr_annee_amo) {
		this.nbr_annee_amo = nbr_annee_amo;
	}
	public UserPersistant getOpc_user() {
		return opc_user;
	}
	public void setOpc_user(UserPersistant opc_user) {
		this.opc_user = opc_user;
	}
	public Boolean getIs_annule() {
		return is_annule;
	}
	public void setIs_annule(Boolean is_annule) {
		this.is_annule = is_annule;
	}
	public String getType_commande() {
		return type_commande;
	}
	public void setType_commande(String type_commande) {
		this.type_commande = type_commande;
	}
	public BigDecimal getMontant_annul() {
		return montant_annul;
	}
	public void setMontant_annul(BigDecimal montant_annul) {
		this.montant_annul = montant_annul;
	}
	public String getPaiementsStr(){
		List<PaiementPersistant> listP = getList_paiement();
		String paiementStr = "";
		if(listP != null){
			for(PaiementPersistant paiement : getList_paiement()){
				if(paiement.getOpc_financement_enum() != null){
					paiementStr = paiementStr + paiement.getOpc_financement_enum().getLibelle()+" | ";
				}
			}
		}
		
		return paiementStr;
	}
	public BigDecimal getMttPaye() {
		if(this.list_paiement == null) {
			return BigDecimalUtil.ZERO;
		}
		//-----------------------------------------
		BigDecimal mttPaye = BigDecimalUtil.ZERO;
		for(PaiementPersistant det : this.list_paiement) {
			if(det.getDate_encaissement() != null) {
				mttPaye = BigDecimalUtil.add(mttPaye, det.getMontant());
			}
		}
		return mttPaye;
	}
	
	
	public String getTransfert_mvm_ids() {
		return transfert_mvm_ids;
	}
	public void setTransfert_mvm_ids(String transfert_mvm_ids) {
		this.transfert_mvm_ids = transfert_mvm_ids;
	}
	public String getTypeMvmntLib() {
		if(StringUtil.isNotEmpty(type_mvmnt)) {
			try {
				return ContextAppli.TYPE_MOUVEMENT_ENUM.valueOf(type_mvmnt).getLibelle();
			} catch(Exception e) {
				return "";
			}
		}
		return "";
	}
}
