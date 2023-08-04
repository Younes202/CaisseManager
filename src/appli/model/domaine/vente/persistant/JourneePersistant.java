/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author 
 */
@Entity
@Table(name = "journee", indexes={
		@Index(name="IDX_JRN_DATE", columnList="date_journee"),
		@Index(name="IDX_JRN_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name="journee_find", query="from JourneePersistant journee "
			+ "where journee.date_journee>='[dateDebut]' and journee.date_journee<='[dateFin]' "
			+ "order by journee.date_journee desc"),
	@NamedQuery(name="depense_journee_find", query="from CaisseMouvementArticlePersistant caisseMouvementArticle " +
			" where caisseMouvementArticle.code like 'GEN_%' and caisseMouvementArticle.mtt_total < 0 "
			+ "and caisseMouvementArticle.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id>='[journeeId]'"
			+ "order by caisseMouvementArticle.id desc"),
	@NamedQuery(name="recette_journee_find", query="from CaisseMouvementArticlePersistant caisseMouvementArticle " +
			" where caisseMouvementArticle.code like 'GEN_%' and caisseMouvementArticle.mtt_total > 0 "
			+ "and caisseMouvementArticle.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id>='[journeeId]'"
			+ "order by caisseMouvementArticle.id desc"),
	@NamedQuery(name="garantie_journee_find", query="from CaisseMouvementArticlePersistant caisseMouvementArticle " +
			" where caisseMouvementArticle.code like 'GAR%' and caisseMouvementArticle.mtt_total > 0 "
			+ "and caisseMouvementArticle.opc_mouvement_caisse.opc_caisse_journee.opc_journee.id>='[journeeId]'"
			+ "order by caisseMouvementArticle.id desc")
})
public class JourneePersistant  extends BasePersistant {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date_journee;// Unique mais dans validateur (synchro)

    @Column(length = 1, nullable=false)
    private String statut_journee;
    
    @Column(length = 120)// custom call indisponible
    private String customcall_out;
    
    @Column(length = 5)
	private Integer nbr_vente;
    @Column(length = 3)
	private Integer nbr_livraison;
    
    @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal tarif_livraison;
    @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal tarif_livraison_part;

    @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal solde_coffre;
    
     @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_espece;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_cheque;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_dej;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_cb;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_total;// Hors réduction
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_total_net;// Avec prise en compte des réduction et des livraisons après clôture journée
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_annule;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_annule_ligne;// Les lignes de commande annullées dans une commande non annulée
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_reduction;
	 @Column(length = 15, scale = 6, precision = 15)
		private BigDecimal mtt_art_reduction;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_donne_point;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_portefeuille;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_livraison;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_art_offert;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_ouverture;
	 @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_cloture_caissier;
     @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_espece;
     @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_cb;
     @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_cheque;
     @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_cloture_caissier_dej; 
	
     @Column(length = 15, scale = 6, precision = 15)
     private BigDecimal mtt_total_achat;// Total valeur d'achat des articles vendus
     
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private UserPersistant opc_user;
    
    @GsonExclude
    @OneToMany
	@JoinColumn(name="journee_id", referencedColumnName="id", insertable=false, updatable=false)
	List<CaisseJourneePersistant> list_caisse_journee;

	public Date getDate_journee() {
		return date_journee;
	}

	public void setDate_journee(Date date_journee) {
		this.date_journee = date_journee;
	}

	public String getStatut_journee() {
		return statut_journee;
	}

	public void setStatut_journee(String statut_journee) {
		this.statut_journee = statut_journee;
	}

	public List<CaisseJourneePersistant> getList_caisse_journee() {
		return list_caisse_journee;
	}

	public UserPersistant getOpc_user() {
		return opc_user;
	}

	public void setOpc_user(UserPersistant opc_user) {
		this.opc_user = opc_user;
	}

	public void setList_caisse_journee(
			List<CaisseJourneePersistant> list_caisse_journee) {
		this.list_caisse_journee = list_caisse_journee;
	}

	public String getCustomcall_out() {
		return customcall_out;
	}

	public void setCustomcall_out(String customcall_out) {
		this.customcall_out = customcall_out;
	}

	public Integer getNbr_vente() {
		return nbr_vente;
	}

	public void setNbr_vente(Integer nbr_vente) {
		this.nbr_vente = nbr_vente;
	}

	public Integer getNbr_livraison() {
		return nbr_livraison;
	}

	public void setNbr_livraison(Integer nbr_livraison) {
		this.nbr_livraison = nbr_livraison;
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

	public BigDecimal getMtt_ouverture() {
		return mtt_ouverture;
	}

	public void setMtt_ouverture(BigDecimal mtt_ouverture) {
		this.mtt_ouverture = mtt_ouverture;
	}

	public BigDecimal getMtt_cloture_caissier() {
		return mtt_cloture_caissier;
	}

	public void setMtt_cloture_caissier(BigDecimal mtt_cloture_caissier) {
		this.mtt_cloture_caissier = mtt_cloture_caissier;
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

	public BigDecimal getTarif_livraison() {
		return tarif_livraison;
	}

	public void setTarif_livraison(BigDecimal tarif_livraison) {
		this.tarif_livraison = tarif_livraison;
	}

	public BigDecimal getMtt_livraison() {
		return mtt_livraison;
	}

	public void setMtt_livraison(BigDecimal mtt_livraison) {
		this.mtt_livraison = mtt_livraison;
	}

	public BigDecimal getMtt_donne_point() {
		return mtt_donne_point;
	}

	public void setMtt_donne_point(BigDecimal mtt_donne_point) {
		this.mtt_donne_point = mtt_donne_point;
	}

	public BigDecimal getMtt_portefeuille() {
		return mtt_portefeuille;
	}

	public void setMtt_portefeuille(BigDecimal mtt_portefeuille) {
		this.mtt_portefeuille = mtt_portefeuille;
	}

	public BigDecimal getTarif_livraison_part() {
		return tarif_livraison_part;
	}

	public void setTarif_livraison_part(BigDecimal tarif_livraison_part) {
		this.tarif_livraison_part = tarif_livraison_part;
	}

	public BigDecimal getMttLivraisonGlobal() {
		BigDecimal nbrLivraison = BigDecimalUtil.get((this.nbr_livraison==null?0:this.nbr_livraison));
		
		return BigDecimalUtil.multiply(this.tarif_livraison, nbrLivraison); 
	}
	public BigDecimal getMttLivraisonPartSociete() {
		BigDecimal nbrLivraison = BigDecimalUtil.get((this.nbr_livraison==null?0:this.nbr_livraison));
		
		return BigDecimalUtil.multiply(this.tarif_livraison_part, nbrLivraison); 
	}
	public BigDecimal getMttLivraisonPartLivreur() {
		BigDecimal nbrLivraison = BigDecimalUtil.get((this.nbr_livraison==null?0:this.nbr_livraison));
		BigDecimal tarifLivreur = BigDecimalUtil.substract(this.tarif_livraison, this.tarif_livraison_part);
		
		return BigDecimalUtil.multiply(tarifLivreur, nbrLivraison); 
	}

	public BigDecimal getMtt_total_achat() {
		return mtt_total_achat;
	}

	public void setMtt_total_achat(BigDecimal mtt_total_achat) {
		this.mtt_total_achat = mtt_total_achat;
	}

	public BigDecimal getSolde_coffre() {
		return solde_coffre;
	}

	public void setSolde_coffre(BigDecimal solde_coffre) {
		this.solde_coffre = solde_coffre;
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

	public BigDecimal getEcartNet(){
		BigDecimal ecart = null;
		BigDecimal netCaisse = BigDecimalUtil.substract(this.mtt_total_net, 
											this.mtt_donne_point, 
											this.mtt_portefeuille
											/*, getMttLivraisonPartLivreur()*/
										);
		//
		if("O".equals(this.getStatut_journee())) {
			BigDecimal netCloture = BigDecimalUtil.substract(this.mtt_cloture_caissier, this.mtt_ouverture);
			ecart = BigDecimalUtil.substract(netCloture, netCaisse);
		} else{
			if(this.list_caisse_journee != null){
				BigDecimal netCloture = null;
				for (CaisseJourneePersistant caisseJourneeP : list_caisse_journee) {
					BigDecimal mttCloture = BigDecimalUtil.add(
							caisseJourneeP.getMtt_cloture_caissier_espece(), 
							caisseJourneeP.getMtt_cloture_caissier_cb(), 
							caisseJourneeP.getMtt_cloture_caissier_cheque(), 
							caisseJourneeP.getMtt_cloture_caissier_dej()
						);
					
					mttCloture = BigDecimalUtil.substract(mttCloture, caisseJourneeP.getMtt_ouverture());
					netCloture = BigDecimalUtil.add(netCloture, mttCloture);
				}
				ecart = BigDecimalUtil.substract(netCloture, netCaisse);
			}
		}
		
		return ecart;
	}
}
