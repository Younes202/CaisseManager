/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.vente.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "caisse_mouvement_article", indexes={
		@Index(name="IDX_CM_ART_FUNC", columnList="code_func")
	})
public class CaisseMouvementArticlePersistant extends BasePersistant {
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal quantite;

    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_total;
    @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_art_reduction;
    @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_reduction;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal taux_marge_cai;// Marge caissier
    @Column
    private Boolean is_devise;// En devise

    @ManyToOne
    @JoinColumn(name = "user_annul_id", referencedColumnName="id")
    private UserPersistant opc_user_annul;// User qui a annulé
    @Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_annul;//Date d'annulation
    @Column
    private Boolean is_annule;
    @Column
    private Boolean is_offert;
    @Column
    private Boolean is_client_pr;

    @Column(length = 50, nullable = false)
    private String code;
    @Column(length = 120, nullable = false)
    private String libelle;
    @Column(length = 20, nullable = false)
    private Long elementId;

    @Column(length = 4)
    private Integer level;
    
    @Column(length = 255)
    private String commentaire;
    
	@Column(length = 10)
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
    @ManyToOne
    @JoinColumn(name = "user_stat_id", referencedColumnName="id")
    private UserPersistant opc_user_stat;// User qui a changé le statut

    @Column(length = 4)
    private String menu_idx;// Pour destinguer les menus car un même menu peut être ajouté plusieurs fois
    @Column(length = 20, nullable = false)
    private String type_ligne;// Type de la ligne MENU, ART, GROUP_MENU, ...
    @Column(length = 250)
    private String parent_code;// Code du group auquel il appartient (famille, liste composition, ...)
    @Transient// Util pour caluculer le nombre max d'articles la composition des menus
    private String composition_detail_id;// Group auquel il appartient dans les menu composition  donc MenCompositionDetail (liste de choix, famille ou article)
   
    @Column(length = 5)  
    private Integer idx_client; 
	@Column(length = 20)
	private String ref_table;
	@Column(length = 2)  
    private Integer nbr_couvert;
	// Ce champs sert à corriger le problème des statut changé puis on change de quantité avec renvoi en cuisine donc afficher le nombre uniquement ajouté
	/*
	 * PRETE:5;LIVRE:2;VALIDE:1
	 */
	@Column(length = 120)  
    private String nbr_statut; 
    @Column(length = 5)  
    private Integer idx_element;// Ce champs permet d'ordonner le detail de la commande
	@Column
	private Boolean is_encaisse;
    @Column
    private Boolean is_menu;
    @Column
    private Boolean is_suite_lock;//suite commande
    @Column
    private Boolean is_suite_end;//suite commande envoyée et terminée
    
    @Column
    private Integer type_opr; // 1=new 		2=updated  3=deleted   pour afficher en gras les nouveaux articles ajoutés à la cmd
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal old_qte_line; // ancienne quantite de l'article apres modification

    @Transient
    private BigDecimal prix_unitaire; // pour le calcul du mtt_commande dans la gestion des retours clients
    @Transient
    private BigDecimal old_qte; // pour le calcul du mtt_commande dans la gestion des retours clients
    
    @ManyToOne
    @JoinColumn(name = "mvm_caisse_id", referencedColumnName = "id")
    private CaisseMouvementPersistant opc_mouvement_caisse;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private ArticlePersistant opc_article;
    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName="id")
    private MenuCompositionPersistant opc_menu;
    
    public CaisseMouvementPersistant getOpc_mouvement_caisse() {
        return opc_mouvement_caisse;
    }

    public void setOpc_mouvement_caisse(CaisseMouvementPersistant opc_mouvement_caisse) {
        this.opc_mouvement_caisse = opc_mouvement_caisse;
    }

    public ArticlePersistant getOpc_article() {
        return opc_article;
    }

    public void setOpc_article(ArticlePersistant opc_article) {
        this.opc_article = opc_article;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getMtt_total() {
        return mtt_total;
    }

    public void setMtt_total(BigDecimal mtt_total) {
        this.mtt_total = mtt_total;
    }

    public Boolean getIs_annule() {
        return is_annule;
    }

    public void setIs_annule(Boolean is_annule) {
        this.is_annule = is_annule;
    }

	public String getMenu_idx() {
		return menu_idx;
	}

	public void setMenu_idx(String menu_idx) {
		this.menu_idx = menu_idx;
	}

	public MenuCompositionPersistant getOpc_menu() {
		return opc_menu;
	}

	public void setOpc_menu(MenuCompositionPersistant opc_menu) {
		this.opc_menu = opc_menu;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getType_ligne() {
		return type_ligne;
	}

	public void setType_ligne(String type_ligne) {
		this.type_ligne = type_ligne;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Boolean getIs_offert() {
		return is_offert;
	}

	public void setIs_offert(Boolean is_offert) {
		this.is_offert = is_offert;
	}

	public String getComposition_detail_id() {
		return composition_detail_id;
	}

	public void setComposition_detail_id(String composition_detail_id) {
		this.composition_detail_id = composition_detail_id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIdx_client() {
		return idx_client;
	}

	public void setIdx_client(Integer idx_client) {
		this.idx_client = idx_client;
	}

	public Boolean getIs_menu() {
		return is_menu;
	}

	public void setIs_menu(Boolean is_menu) {
		this.is_menu = is_menu;
	}

	public String getLast_statut() {
		return last_statut;
	}

	public void setLast_statut(String last_statut) {
		
		if(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(last_statut)) {
			if(this.getDate_debut_stat() == null) {
				this.setDate_debut_stat(new Date());
				this.setOpc_user_stat(ContextAppli.getUserBean());
			}
		} else if(STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(last_statut)) {
			if(this.getDate_debut_stat() == null) {
				this.setDate_debut_stat(new Date());
				this.setOpc_user_stat(ContextAppli.getUserBean());
			}
		} else if(STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(last_statut)) {
			if(this.getDate_fin_stat() == null) {
				this.setDate_fin_stat(new Date());
				this.setOpc_user_stat(ContextAppli.getUserBean());
			}
			if(this.getDate_debut_stat2() == null) {
				this.setDate_debut_stat2(new Date());
				this.setOpc_user_stat(ContextAppli.getUserBean());
			}
		} else if(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(last_statut)) {
			if(this.getDate_fin_stat2() == null) {
				this.setDate_fin_stat2(new Date());
				this.setOpc_user_stat(ContextAppli.getUserBean());
			}
		}
		
		this.last_statut = last_statut;
	}

	public String getRef_table() {
		return ref_table;
	}

	public void setRef_table(String ref_table) {
		this.ref_table = ref_table;
	}

	public Boolean getIs_encaisse() {
		return is_encaisse;
	}

	public void setIs_encaisse(Boolean is_encaisse) {
		this.is_encaisse = is_encaisse;
	}

	public Boolean getIs_client_pr() {
		return is_client_pr;
	}

	public void setIs_client_pr(Boolean is_client_pr) {
		this.is_client_pr = is_client_pr;
	}

	public Integer getNbr_couvert() {
		return nbr_couvert;
	}

	public void setNbr_couvert(Integer nbr_couvert) {
		this.nbr_couvert = nbr_couvert;
	}

	public Integer getIdx_element() {
		return idx_element;
	}

	public void setIdx_element(Integer idx_element) {
		this.idx_element = idx_element;
	}

	public BigDecimal getPrix_unitaire() {
		return prix_unitaire;
	}

	public void setPrix_unitaire(BigDecimal prix_unitaire) {
		this.prix_unitaire = prix_unitaire;
	}

	public BigDecimal getOld_qte() {
		return old_qte;
	}

	public void setOld_qte(BigDecimal old_qte) {
		this.old_qte = old_qte;
	}

	public BigDecimal getOld_qte_line() {
		return old_qte_line;
	}

	public void setOld_qte_line(BigDecimal old_qte_line) {
		this.old_qte_line = old_qte_line;
	}

	public Integer getType_opr() {
		return type_opr;
	}

	public void setType_opr(Integer type_opr) {
		this.type_opr = type_opr;
	}

	public String getNbr_statut() {
		return nbr_statut;
	}

	public void setNbr_statut(String nbr_statut) {
		this.nbr_statut = nbr_statut;
	}
	
	public UserPersistant getOpc_user_annul() {
		return opc_user_annul;
	}

	public void setOpc_user_annul(UserPersistant opc_user_annul) {
		this.opc_user_annul = opc_user_annul;
	}

	public Date getDate_annul() {
		return date_annul;
	}

	public void setDate_annul(Date date_annul) {
		this.date_annul = date_annul;
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

	public UserPersistant getOpc_user_stat() {
		return opc_user_stat;
	}

	public void setOpc_user_stat(UserPersistant opc_user_stat) {
		this.opc_user_stat = opc_user_stat;
	}

	public Boolean getIs_suite_lock() {
		return is_suite_lock;
	}

	public void setIs_suite_lock(Boolean is_suite_lock) {
		this.is_suite_lock = is_suite_lock;
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

	public BigDecimal getTaux_marge_cai() {
		return taux_marge_cai;
	}

	public void setTaux_marge_cai(BigDecimal taux_marge_cai) {
		this.taux_marge_cai = taux_marge_cai;
	}

	public Boolean getIs_suite_end() {
		return is_suite_end;
	}

	public void setIs_suite_end(Boolean is_suite_end) {
		this.is_suite_end = is_suite_end;
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

	public BigDecimal getMtt_reduction() {
		return mtt_reduction;
	}

	public void setMtt_reduction(BigDecimal mtt_reduction) {
		this.mtt_reduction = mtt_reduction;
	}

	public int getNbrByStatut(STATUT_CAISSE_MOUVEMENT_ENUM statut){
		if(this.nbr_statut != null){
			String[] vals = StringUtil.getArrayFromStringDelim(this.nbr_statut, ";");
			for (String vs : vals) {
				String[] valsDet = StringUtil.getArrayFromStringDelim(vs, ":");
				if(valsDet[0].equals(statut.toString())){
					return BigDecimalUtil.get(valsDet[1]).intValue();
				}
			}
		}
		return 0;
	}

}
