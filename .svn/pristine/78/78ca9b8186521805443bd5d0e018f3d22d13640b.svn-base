package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.stock.service.IArticleService;
import framework.model.beanContext.BasePersistant;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "article", indexes={
		@Index(name="IDX_ART_FUNC", columnList="code_func"),
		@Index(name="IDX_ART_CODE", columnList="code")
	})
public class ArticlePersistant extends BasePersistant{
	@Column(length = 80, nullable = false) 
	private String code;

	@Column(length = 120, nullable = false)
	private String libelle;
	@Transient
	private String libelle_compl;	
	@Column(length = 50)
	private String code_barre;
	@Column(length = 10)
	private Integer code_direct_bal;// Code direct balance
	@Column(length = 150)
	private String fournisseurs;// Fournisseurs exclusifs
	@Column
	private Boolean is_stock;
	@Column(length = 4)
	private Integer ordre;
	@Column
	private Boolean is_fiche;

	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_ref;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_vente_gros;

	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal marge;
	
	@Column(length = 2)
	private Integer temps_cuis_ref;// Temps référence cuisine
	
	@Transient
	private  BigDecimal marge_calcule;
	
	@Column(length = 10)
	private  Integer qte_seuil_gros;	
	
	@Column(length = 2)
	private String nature;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_ref_prix;//Date référence pour le prix
	
	@Column(length = 20)
	private  Integer unite_vente_quantite;
	
	@Column(length = 4)
	private  Integer freinte;// Perte au momement de la transformation
	@Column(length = 4)
	private  Integer freinte_achat;// Perte au momement de la transformation
	@Column(length = 3, scale = 6, precision = 15)
	private BigDecimal taux_tolerance_trans; // TODO : -- Averifier la stategie du controle des transformation
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_achat_ht;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_achat_lot;// Si achat par boite ou carton
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_achat_ttc;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal prix_achat_moyen_ht;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal prix_achat_moyen_ttc;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_vente;
	@Transient
	private  BigDecimal prix_vente_tmp;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_vente_ht;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_tva;
	@Column(length = 3, scale = 6, precision = 15)
	private BigDecimal taux_marge_caissier;
	@Column(length = 3, scale = 6, precision = 15)
	private BigDecimal min_marge;
	@Column(length = 3, scale = 6, precision = 15)
	private BigDecimal mtt_garantie;
	
	@Column(length = 255)
	private String description;
	
	@Column
	private Boolean is_disable;
	@Column(length=20)
	private Long origine_id;
	@Transient
	private String operation;
	@Column(length = 150)
	private String prix_vente_ets;
	
	// Data except -------------------------------------
	@Column(length = 120)
	private String composition;
	@Column(length = 120)
	private String conditionnement;
	@Column(length = 120)
	private String laboratoire;
	@Column(length = 3)
	private  Integer taux_remboursement;
	@Column(length = 3) 
	private String statut;//
	@Column(length = 50)
	private String code_barre_ori;
	@Column(length = 3) 
	private String type_art;
	// ---------------------------------------------------
	
	@GsonExclude
	@Transient
	private List<FamillePersistant> familleStr;// Arborescence de la famille 

	/*********************** Liens ************************************/
	@GsonExclude
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id") 
	private ArticleClientPrixPersistant opc_article_client;
	
	@GsonExclude
	@ManyToOne
	@JoinColumn(name = "marque_id", referencedColumnName="id") 
	private MarquePersistant opc_marque;
	
	@ManyToOne
	@JoinColumn(name = "famille_stock_id", referencedColumnName="id")
	private FamilleStockPersistant opc_famille_stock;
	
	@ManyToOne
	@JoinColumn(name = "unite_achat_enum_id", referencedColumnName="id")
	private ValTypeEnumPersistant opc_unite_achat_enum;
	
	@ManyToOne
	@JoinColumn(name = "unite_vente_enum_id", referencedColumnName="id")
	private ValTypeEnumPersistant opc_unite_vente_enum;
	
	@ManyToOne
	@JoinColumn(name = "tva_enum_id", referencedColumnName="id")
	private ValTypeEnumPersistant opc_tva_enum;// Tva achat
	@ManyToOne
	@JoinColumn(name = "tva_enum_vente_id", referencedColumnName="id")
	private ValTypeEnumPersistant opc_tva_enum_vente;// Tva vente
	
	@GsonExclude
	@OneToMany
	@JoinColumn(name="article_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<MouvementArticlePersistant> list_mouvement_article;
	
	@GsonExclude
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("emplacement_id")
	@JoinColumn(name="composant_id", referencedColumnName="id")
	private List<EmplacementSeuilPersistant> list_empl_seuil;
	
	@GsonExclude
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "article_id", referencedColumnName = "id")
	private List<ArticleDetailPersistant> list_article;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("article_id")
	@JoinColumn(name="article_id", referencedColumnName="id")
	private List<ArticleGeneriquePersistant> list_generic;
	
	@Transient
	private String familleParent_code;// Utilisé pour impression en local des boissons via swing
	
	@ManyToOne
	@JoinColumn(name = "unite_dosage_enum_id", referencedColumnName="id")
	private ValTypeEnumPersistant opc_unite_dosage_enum;
	
	//-------------------------------- 
	@Column
	private Boolean is_balance;
	@Column
	private Boolean is_fav_balance;
	//-------------------------------------------
	@Column
	private Boolean is_noncaisse;// ne pas afficher dans la caisse
	@Column
	private Boolean is_nonmobile;// ne pas afficher dans la caisse mobile
	@Column
	private Boolean is_fav_caisse;
	
	@Column(length = 1)
	private String destination;//C=Cuisine, P=Présentoire
	
	@ManyToOne
	@JoinColumn(name = "famille_cuisine_id", referencedColumnName="id")
	private FamilleCuisinePersistant opc_famille_cuisine;
		
	public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}

	public String getConditionnement() {
		return conditionnement;
	}

	public void setConditionnement(String conditionnement) {
		this.conditionnement = conditionnement;
	}

	public String getLaboratoire() {
		return laboratoire;
	}

	public void setLaboratoire(String laboratoire) {
		this.laboratoire = laboratoire;
	}

	public Integer getTaux_remboursement() {
		return taux_remboursement;
	}

	public void setTaux_remboursement(Integer taux_remboursement) {
		this.taux_remboursement = taux_remboursement;
	}

	public ValTypeEnumPersistant getOpc_unite_dosage_enum() {
		return opc_unite_dosage_enum;
	}

	public void setOpc_unite_dosage_enum(ValTypeEnumPersistant opc_unite_dosage_enum) {
		this.opc_unite_dosage_enum = opc_unite_dosage_enum;
	}

	public Long getOrigine_id() {
		return origine_id;
	}

	public void setOrigine_id(Long origine_id) {
		this.origine_id = origine_id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getCode_barre_ori() {
		return code_barre_ori;
	}

	public void setCode_barre_ori(String code_barre_ori) {
		this.code_barre_ori = code_barre_ori;
	}

	public Boolean getIs_balance() {
		return is_balance;
	}

	public void setIs_balance(Boolean is_balance) {
		this.is_balance = is_balance;
	}

	public Boolean getIs_fav_balance() {
		return is_fav_balance;
	}

	public void setIs_fav_balance(Boolean is_fav_balance) {
		this.is_fav_balance = is_fav_balance;
	}
	public Boolean getIs_noncaisse() {
		return is_noncaisse;
	}

	public void setIs_noncaisse(Boolean is_noncaisse) {
		this.is_noncaisse = is_noncaisse;
	}

	public Boolean getIs_fav_caisse() {
		return is_fav_caisse;
	}

	public void setIs_fav_caisse(Boolean is_fav_caisse) {
		this.is_fav_caisse = is_fav_caisse;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public FamilleCuisinePersistant getOpc_famille_cuisine() {
		return opc_famille_cuisine;
	}

	public void setOpc_famille_cuisine(FamilleCuisinePersistant opc_famille_cuisine) {
		this.opc_famille_cuisine = opc_famille_cuisine;
	}
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getCode_barre() {
		return code_barre;
	}

	public void setCode_barre(String code_barre) {
		this.code_barre = code_barre;
	}

	public BigDecimal getPrix_achat_ht() {
		return prix_achat_ht;
	}

	public void setPrix_achat_ht(BigDecimal prix_achat_ht) {
		this.prix_achat_ht = prix_achat_ht;
	}

	public BigDecimal getPrix_achat_ttc() {
		if(this.prix_achat_ttc == null){
			return this.prix_achat_ht;
		}
		return prix_achat_ttc;
	}

	public void setPrix_achat_ttc(BigDecimal prix_achat_ttc) {
		this.prix_achat_ttc = prix_achat_ttc;
	}


	public BigDecimal getPrix_vente() {
		return prix_vente;
	}

	public void setPrix_vente(BigDecimal prix_vente) {
		this.prix_vente = prix_vente;
	}

	public ValTypeEnumPersistant getOpc_tva_enum() {
		return opc_tva_enum;
	}

	public void setOpc_tva_enum(ValTypeEnumPersistant opc_tva_enum) {
		this.opc_tva_enum = opc_tva_enum;
	}

	public String getLibelle() {
		return libelle;
	}
	public String getLibelleDataVal() {
		String libDataVal = this.getLibelle();
		// Ajouter le complément
		if(this.getId() != null) {
			IArticleService articleService = ServiceUtil.getBusinessBean(IArticleService.class);
			List<DataValuesPersistant> listDataVal = articleService.getListDataValues("COMPOSANT", this.getId());	
			for(DataValuesPersistant dataVal : listDataVal) {
				libDataVal += " ["+dataVal.getOpc_data_form().getData_label()+" : "+dataVal.getData_value()+"]"; 
			}
		}
		return libDataVal;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FamilleStockPersistant getOpc_famille_stock() {
		return opc_famille_stock;
	}

	public void setOpc_famille_stock(FamilleStockPersistant opc_famille_stock) {
		this.opc_famille_stock = opc_famille_stock;
	}

	public List<ArticleDetailPersistant> getList_article() {
		return list_article;
	}

	public void setList_article(List<ArticleDetailPersistant> list_article) {
		this.list_article = list_article;
	}

	public List<MouvementArticlePersistant> getList_mouvement_article() {
		return list_mouvement_article;
	}

	public void setList_mouvement_article(
			List<MouvementArticlePersistant> list_mouvement_article) {
		this.list_mouvement_article = list_mouvement_article;
	}

	public Integer getUnite_vente_quantite() {
		return unite_vente_quantite;
	}

	public void setUnite_vente_quantite(Integer unite_vente_quantite) {
		this.unite_vente_quantite = unite_vente_quantite;
	}

	public ValTypeEnumPersistant getOpc_unite_achat_enum() {
		return opc_unite_achat_enum;
	}

	public void setOpc_unite_achat_enum(ValTypeEnumPersistant opc_unite_achat_enum) {
		this.opc_unite_achat_enum = opc_unite_achat_enum;
	}

	public ValTypeEnumPersistant getOpc_unite_vente_enum() {
		return opc_unite_vente_enum;
	}

	public void setOpc_unite_vente_enum(ValTypeEnumPersistant opc_unite_vente_enum) {
		this.opc_unite_vente_enum = opc_unite_vente_enum;
	}

	public Boolean getIs_stock() {
		return is_stock;
	}

	public void setIs_stock(Boolean is_stock) {
		this.is_stock = is_stock;
	}

	public List<FamillePersistant> getFamilleStr() {
		return familleStr;
	}

	public void setFamilleStr(List<FamillePersistant> familleStr) {
		this.familleStr = familleStr;
	}

	public Boolean getIs_disable() {
		return is_disable;
	}


	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}


	public BigDecimal getPrix_achat_moyen_ht() { 
		if(this.prix_achat_moyen_ht == null){
			return getPrix_achat_ht();
		}
		return prix_achat_moyen_ht;
	}


	public void setPrix_achat_moyen_ht(BigDecimal prix_achat_moyen_ht) {
		this.prix_achat_moyen_ht = prix_achat_moyen_ht;
	}


	public BigDecimal getMtt_tva() {
		return mtt_tva;
	}

	public void setMtt_tva(BigDecimal mtt_tva) {
		this.mtt_tva = mtt_tva;
	}

	public List<EmplacementSeuilPersistant> getList_empl_seuil() {
		return list_empl_seuil;
	}


	public void setList_empl_seuil(List<EmplacementSeuilPersistant> list_empl_seuil) {
		this.list_empl_seuil = list_empl_seuil;
	}


	public ArticleClientPrixPersistant getOpc_article_client() {
		return opc_article_client;
	}


	public void setOpc_article_client(ArticleClientPrixPersistant opc_article_client) {
		this.opc_article_client = opc_article_client;
	}

	public String getFamilleParent_code() {
		return familleParent_code;
	}


	public void setFamilleParent_code(String familleParent_code) {
		this.familleParent_code = familleParent_code;
	}

	public Boolean getIs_fiche() {
		return is_fiche;
	}

	public void setIs_fiche(Boolean is_fiche) {
		this.is_fiche = is_fiche;
	}
	
	// Pour calculer la marf=ge unitaire d'article
	public BigDecimal getPrixAchatUnitaireHT(){
//			if(BooleanUtil.isTrue(this.is_stock) && this.unite_vente_quantite != null && this.prix_achat_ht != null){
//				BigDecimal divider = BigDecimalUtil.isZero(BigDecimalUtil.get(this.unite_vente_quantite)) ? BigDecimalUtil.get(1) : BigDecimalUtil.get(this.unite_vente_quantite);
//				return BigDecimalUtil.divide(this.prix_achat_ht, divider);
//			}
		return this.prix_achat_ht;
	}
	// Pour calculer la marf=ge unitaire d'article
	public BigDecimal getPrixAchatUnitaireTTC(){
//			if(BooleanUtil.isTrue(this.is_stock) && this.unite_vente_quantite != null && this.prix_achat_ttc != null){
//				BigDecimal divider = BigDecimalUtil.isZero(BigDecimalUtil.get(this.unite_vente_quantite)) ? BigDecimalUtil.get(1) : BigDecimalUtil.get(this.unite_vente_quantite);
//				return BigDecimalUtil.divide(this.prix_achat_ttc, divider);
//			}
		return this.prix_achat_ttc;
	}


	public BigDecimal getTaux_marge_caissier() {
		return taux_marge_caissier;
	}


	public void setTaux_marge_caissier(BigDecimal taux_marge_caissier) {
		this.taux_marge_caissier = taux_marge_caissier;
	}


	public String getNature() {
		return nature;
	}


	public void setNature(String nature) {
		this.nature = nature;
	}


	public BigDecimal getTaux_tolerance_trans() {
		return taux_tolerance_trans;
	}


	public void setTaux_tolerance_trans(BigDecimal taux_tolerance_trans) {
		this.taux_tolerance_trans = taux_tolerance_trans;
	}


	public BigDecimal getPrix_achat_moyen_ttc() {
		if(this.prix_achat_moyen_ttc == null){
			return getPrix_achat_ttc();
		}
		return prix_achat_moyen_ttc;
	}


	public void setPrix_achat_moyen_ttc(BigDecimal prix_achat_moyen_ttc) {
		this.prix_achat_moyen_ttc = prix_achat_moyen_ttc;
	}


	public Integer getFreinte() {
		return freinte;
	}

	public void setFreinte(Integer freinte) {
		this.freinte = freinte;
	}

	public Integer getFreinte_achat() {
		return freinte_achat;
	}

	public void setFreinte_achat(Integer freinte_achat) {
		this.freinte_achat = freinte_achat;
	}

	public BigDecimal getPrix_vente_gros() {
		return prix_vente_gros;
	}

	public void setPrix_vente_gros(BigDecimal prix_vente_gros) {
		this.prix_vente_gros = prix_vente_gros;
	}

	public BigDecimal getMarge() {
		return marge;
	}

	public void setMarge(BigDecimal marge) {
		this.marge = marge;
	}

	public Integer getQte_seuil_gros() {
		return qte_seuil_gros;
	}

	public void setQte_seuil_gros(Integer qte_seuil_gros) {
		this.qte_seuil_gros = qte_seuil_gros;
	}

	public String getPrix_vente_ets() {
		return prix_vente_ets;
	}

	public void setPrix_vente_ets(String prix_vente_ets) {
		this.prix_vente_ets = prix_vente_ets;
	}
	
	public BigDecimal getPrix_vente_ht() {
		return prix_vente_ht;
	}

	public void setPrix_vente_ht(BigDecimal prix_vente_ht) {
		this.prix_vente_ht = prix_vente_ht;
	}

	public MarquePersistant getOpc_marque() {
		return opc_marque;
	}

	public void setOpc_marque(MarquePersistant opc_marque) {
		this.opc_marque = opc_marque;
	}

	public String getFournisseurs() {
		return fournisseurs;
	}

	public void setFournisseurs(String fournisseurs) {
		this.fournisseurs = fournisseurs;
	}

	public BigDecimal getPrix_ref() {
		return prix_ref;
	}

	public void setPrix_ref(BigDecimal prix_ref) {
		this.prix_ref = prix_ref;
	}

	public BigDecimal getMin_marge() {
		return min_marge;
	}

	public void setMin_marge(BigDecimal min_marge) {
		this.min_marge = min_marge;
	}

	public Date getDate_ref_prix() {
		return date_ref_prix;
	}

	public void setDate_ref_prix(Date date_ref_prix) {
		this.date_ref_prix = date_ref_prix;
	}

	public String getLibelle_compl() {
		return libelle_compl;
	}

	public void setLibelle_compl(String libelle_compl) {
		this.libelle_compl = libelle_compl;
	}

	public BigDecimal getPrix_vente_tmp() {
		return prix_vente_tmp;
	}

	public void setPrix_vente_tmp(BigDecimal prix_vente_tmp) {
		this.prix_vente_tmp = prix_vente_tmp;
	}

	public Integer getCode_direct_bal() {
		return code_direct_bal;
	}

	public void setCode_direct_bal(Integer code_direct_bal) {
		this.code_direct_bal = code_direct_bal;
	}

	public BigDecimal getPrix_achat_lot() {
		return prix_achat_lot;
	}

	public void setPrix_achat_lot(BigDecimal prix_achat_lot) {
		this.prix_achat_lot = prix_achat_lot;
	}

	public BigDecimal getMtt_garantie() {
		return mtt_garantie;
	}

	public void setMtt_garantie(BigDecimal mtt_garantie) {
		this.mtt_garantie = mtt_garantie;
	}

	public Integer getTemps_cuis_ref() {
		return temps_cuis_ref;
	}

	public void setTemps_cuis_ref(Integer temps_cuis_ref) {
		this.temps_cuis_ref = temps_cuis_ref;
	}
	
	public Integer getOrdre() {
		return ordre;
	}

	public Boolean getIs_nonmobile() {
		return is_nonmobile;
	}

	public void setIs_nonmobile(Boolean is_nonmobile) {
		this.is_nonmobile = is_nonmobile;
	}

	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}

	public List<ArticleGeneriquePersistant> getList_generic() {
		return list_generic;
	}

	public void setList_generic(List<ArticleGeneriquePersistant> list_generic) {
		this.list_generic = list_generic;
	}

	public String getType_art() {
		return type_art;
	}

	public void setType_art(String type_art) {
		this.type_art = type_art;
	}

	public ValTypeEnumPersistant getOpc_tva_enum_vente() {
		return opc_tva_enum_vente;
	}

	public void setOpc_tva_enum_vente(ValTypeEnumPersistant opc_tva_enum_vente) {
		this.opc_tva_enum_vente = opc_tva_enum_vente;
	}

	public BigDecimal getSeuilEmpl(Long emplId) {
		if(this.list_empl_seuil != null) {
			for(EmplacementSeuilPersistant emplSeuil : this.list_empl_seuil) {
				if(emplSeuil.getOpc_emplacement().getId().equals(emplId)) {
					return emplSeuil.getQte_seuil();
				}
			}
		}
		return BigDecimalUtil.ZERO;
	}

	public String getMarge_calcule() {
		if(!BigDecimalUtil.isZero(this.prix_vente)){
			BigDecimal prixAchat = (BigDecimalUtil.isZero(this.prix_achat_ttc) ? this.prix_achat_ht : this.prix_achat_ttc);
			
			BigDecimal margeCalc = BigDecimalUtil.substract(this.prix_vente, prixAchat);
			BigDecimal margePourcent = BigDecimalUtil.divide(
								BigDecimalUtil.multiply(margeCalc, BigDecimalUtil.get(100)), this.prix_vente);
			
			return BigDecimalUtil.formatNumber(margeCalc)+" ("+BigDecimalUtil.formatNumber(margePourcent)+"%)"; 
		}
		return "";
	}

	public Map<Long, BigDecimal> getMapPrix(){
		Map<Long, BigDecimal> mapInfos = new HashMap<>();
		if(this.getPrix_vente_ets() != null){
			String[] prixArray = StringUtil.getArrayFromStringDelim(this.getPrix_vente_ets(), ";");
			for (String data : prixArray) {
				String[] infosArray = StringUtil.getArrayFromStringDelim(data, ":");
				
				Long ets = Long.valueOf(infosArray[0]);
				BigDecimal eprix = BigDecimalUtil.get(infosArray[1]);
				
				mapInfos.put(ets, eprix);
			}
		}
		return mapInfos;
	}
}
