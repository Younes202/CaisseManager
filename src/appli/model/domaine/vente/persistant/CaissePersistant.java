/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.vente.persistant;

import java.math.BigDecimal;
import java.util.List;

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

import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

/**
 *
 * @author 
 */
@Entity
@Table(name = "caisse", indexes={
		@Index(name="IDX_CAIS_FUNC", columnList="code_func"),
		@Index(name="IDX_CAIS_REF", columnList="reference")
	})
@NamedQueries({
	@NamedQuery(name="caisse_find", query="from CaissePersistant caisse order by caisse.type_ecran, caisse.reference desc"),
	@NamedQuery(name="caisseMouvementCuisine_find", query="from CaisseMouvementPersistant caisseMouvement "
		+ "where caisseMouvement.caisse_cuisine like '{caisseId}' "
		+ "and caisseMouvement.date_vente>='[dateDebut]' and caisseMouvement.date_vente<='[dateFin]' "
		+ "order by caisseMouvement.id desc")
})
public class CaissePersistant extends BasePersistant {
    @Column(length = 50, nullable = false)
    private String adresse_mac;
    
    @Column(length = 50, nullable=false)
    private String reference;
    @Column(length = 20)
	private Integer max_direct_bal;// Code direct balance max
    @Column(length = 200)
    private String imprimente_special; // Impriment de ticket personnalisee X,Y

    @Column
    @Lob
    private String imprimantes;// Imprimantes sélectionnées pour l'impression
    @Column
    @Lob
    private String famille_balance;// Famille avec lesqueles la synchrinisation seront fait
    
    @Column(length = 50)
    private String marque;
//    @Column(length = 10)// PRINT, ECRAN
//    private String mode_travail;//Ecran, Imprimante, Aucun
    
    @Column(length = 4)
    private Integer annee;
    @Column(length = 2)
    private Integer nbr_ticket = 1;
    @Column
    private Boolean is_notprint_cuis;// Ne pas imprimer en cuisine
    @Column
    private Boolean is_desactive;
    @Column
    private Boolean is_livraison;
    @Column
    private Boolean is_specifique;// Si affichage familles et menus dédiés uniquement
    @Column
    private Boolean is_ticketPersonnalise;// Si associé avec ticket de caisse personnalisé
    
    @Column(length = 50, nullable=false)
    private String type_ecran;// AFFICHEUR, AFFICLIENT, CAISSE, PRESENTOIRE, CAISSE_CLIENT, CUISINE, PILOTAGE, PRESENTOIRE
    
    @Column
    private Boolean is_local_print;
    
    // -------------- Gestion auto des commandes -------------------
    @Column
    private Boolean is_auto_cmd;
    @Column(length = 3)
    private Integer nbr_max_cmd;
    @Lob
    private String menus_cmd;
    @Lob
    private String articles_cmd;
    @Lob
    private String familles_cmd;
    
    @Column(length = 20)
	private BigDecimal height;
    @Column(length = 20)
	private BigDecimal width;
	@Column(length = 2)
	private Integer pos_x;
	@Column(length = 2)
	private Integer pos_y;
	@Column(length = 20)
	private String orientation;
    //--------------------------------------------------------------
    
	@Column(insertable=false, updatable=false)
	private Long caisse_id;

    @Column
    private Boolean is_aff_integre;// Si ecran afficheur integré
	@GsonExclude
    @ManyToOne
    @JoinColumn(name = "caisse_id", referencedColumnName="id")
    private CaissePersistant opc_caisse;// Caisse liée si afficheur
    
    @ManyToOne
    @JoinColumn(name = "emplacement_id", referencedColumnName="id")
    private EmplacementPersistant opc_stock_cible;

    @GsonExclude
    @OrderBy(value="id desc")
    @OneToMany
	@JoinColumn(name="caisse_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<CaisseJourneePersistant> list_caisse_journee;
    
    @GsonExclude
    @OrderBy(value="id desc")
    @OneToMany
	@JoinColumn(name="terminal_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<ParametragePersistant> list_params;


	public String getImprimente_special() {
		return imprimente_special;
	}

	public void setImprimente_special(String imprimente_special) {
		this.imprimente_special = imprimente_special;
	}

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }
	public String getAdresse_mac() {
		return adresse_mac;
	}

	public void setAdresse_mac(String adresse_mac) {
		this.adresse_mac = adresse_mac;
	}
    
	public EmplacementPersistant getOpc_stock_cible() {
		return opc_stock_cible;
	}

	public void setOpc_stock_cible(EmplacementPersistant opc_stock_cible) {
		this.opc_stock_cible = opc_stock_cible;
	}
	

	public List<CaisseJourneePersistant> getList_caisse_journee() {
		return list_caisse_journee;
	}

	public void setList_caisse_journee(
			List<CaisseJourneePersistant> list_caisse_journee) {
		this.list_caisse_journee = list_caisse_journee;
	}
	public Boolean getIs_desactive() {
		return is_desactive;
	}

	public void setIs_desactive(Boolean is_desactive) {
		this.is_desactive = is_desactive;
	}

	public String getImprimantes() {
		return imprimantes;
	}

	public void setImprimantes(String imprimantes) {
		this.imprimantes = imprimantes;
	}

	public String getStatutCaisse(){
		if(this.list_caisse_journee != null && this.list_caisse_journee.size() > 0){
			return this.list_caisse_journee.get(0).getStatut_caisse();
		}
		return null;
	}
	
	public String getStatutJournee(){
		if(this.list_caisse_journee != null && this.list_caisse_journee.size() > 0){
			return this.list_caisse_journee.get(0).getOpc_journee().getStatut_journee();
		}
		return null;
	}

	public CaissePersistant getOpc_caisse() {
		return opc_caisse;
	}

	public void setOpc_caisse(CaissePersistant opc_caisse) {
		this.opc_caisse = opc_caisse;
	}

	public String getType_ecran() {
		return type_ecran;
	}

	public void setType_ecran(String type_ecran) {
		this.type_ecran = type_ecran;
	}

	public Integer getNbr_max_cmd() {
		return nbr_max_cmd;
	}

	public void setNbr_max_cmd(Integer nbr_max_cmd) {
		this.nbr_max_cmd = nbr_max_cmd;
	}

	public Boolean getIs_auto_cmd() {
		return is_auto_cmd;
	}

	public void setIs_auto_cmd(Boolean is_auto_cmd) {
		this.is_auto_cmd = is_auto_cmd;
	}

	public String getMenus_cmd() {
		return menus_cmd;
	}

	public void setMenus_cmd(String menus_cmd) {
		this.menus_cmd = menus_cmd;
	}

	public String getArticles_cmd() {
		return articles_cmd;
	}

	public void setArticles_cmd(String articles_cmd) {
		this.articles_cmd = articles_cmd;
	}

	public String getFamilles_cmd() {
		return familles_cmd;
	}

	public void setFamilles_cmd(String familles_cmd) {
		this.familles_cmd = familles_cmd;
	}

	public Boolean getIs_livraison() {
		return is_livraison;
	}

	public void setIs_livraison(Boolean is_livraison) {
		this.is_livraison = is_livraison;
	}

	public Boolean getIs_specifique() {
		return is_specifique;
	}

	public void setIs_specifique(Boolean is_specifique) {
		this.is_specifique = is_specifique;
	}
	
	public Boolean getIs_ticketPersonnalise() {
		return is_ticketPersonnalise;
	}

	public void setIs_ticketPersonnalise(Boolean is_ticketPersonnalise) {
		this.is_ticketPersonnalise = is_ticketPersonnalise;
	}

	public Boolean getIs_local_print() {
		return is_local_print;
	}

	public void setIs_local_print(Boolean is_local_print) {
		this.is_local_print = is_local_print;
	}

	public Integer getNbr_ticket() {
		return nbr_ticket;
	}

	public void setNbr_ticket(Integer nbr_ticket) {
		this.nbr_ticket = nbr_ticket;
	}

	public Integer getPos_x() {
		return pos_x;
	}

	public void setPos_x(Integer pos_x) {
		this.pos_x = pos_x;
	}

	public Integer getPos_y() {
		return pos_y;
	}

	public void setPos_y(Integer pos_y) {
		this.pos_y = pos_y;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

//	public String getMode_travail() {
//		return mode_travail;
//	}
//
//	public void setMode_travail(String mode_travail) {
//		this.mode_travail = mode_travail;
//	}

	public List<ParametragePersistant> getList_params() {
		return list_params;
	}

	public void setList_params(List<ParametragePersistant> list_params) {
		this.list_params = list_params;
	}

	public String getFamille_balance() {
		return famille_balance;
	}

	public void setFamille_balance(String famille_balance) {
		this.famille_balance = famille_balance;
	}

	public Integer getMax_direct_bal() {
		return max_direct_bal;
	}

	public void setMax_direct_bal(Integer max_direct_bal) {
		this.max_direct_bal = max_direct_bal;
	}

	public Long getCaisse_id() {
		return caisse_id;
	}

	public void setCaisse_id(Long caisse_id) {
		this.caisse_id = caisse_id;
	}

	public Boolean getIs_aff_integre() {
		return is_aff_integre;
	}

	public void setIs_aff_integre(Boolean is_aff_integre) {
		this.is_aff_integre = is_aff_integre;
	}

	public Boolean getIs_notprint_cuis() {
		return is_notprint_cuis;
	}

	public void setIs_notprint_cuis(Boolean is_notprint_cuis) {
		this.is_notprint_cuis = is_notprint_cuis;
	}


}
