/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.administration.persistant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;
import framework.model.common.util.BigDecimalUtil;

/**
 *
 * @author 
 */
@Entity
@Table(name = "etat_finance", indexes={
		@Index(name="IDX_ETFIN_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name="etat_finance_find", query="from EtatFinancePersistant etatFinance order by etatFinance.date_etat desc"),
})
public class EtatFinancePersistant extends BasePersistant  {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date date_etat;// Date début de l'état
	@Column
	private Boolean is_purge;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_vente_caisse;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_vente_caisse_cloture;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_vente_hors_caisse;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_salaire;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_salaire_cheque_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_salaire_cheque_non_encais;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_achat;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_depense_divers;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_recette_divers;
	// Avoir
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_avoir;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_avoir_cheque_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_avoir_cheque_non_encais;
	
	@Column(length = 10)
	private Integer nbr_livraison;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_livraison;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_livraison_part;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_tva_achat;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal mtt_tva_vente;
	// Infos raz
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_raz_declare;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_depense_cheque_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_depense_cheque_non_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_recette_cheque_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_recette_cheque_non_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_vente_cheque_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_vente_cheque_non_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_achat_cheque_encais;
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_achat_cheque_non_encais;
	
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_resultat_net;
	@Column(length = 15, scale = 6, precision = 15)
	BigDecimal mtt_ecart_tva;
	
	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "etat_id", referencedColumnName = "id")
	List<EtatFinanceDetailPersistant> list_detail;
	
	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "etat_id", referencedColumnName = "id")
	List<EtatFinancePaiementPersistant> list_paiement;

	public Date getDate_etat() {
		return date_etat;
	}

	public void setDate_etat(Date date_etat) {
		this.date_etat = date_etat;
	}

	public BigDecimal getMtt_vente_caisse() {
		return mtt_vente_caisse;
	}

	public void setMtt_vente_caisse(BigDecimal mtt_vente_caisse) {
		this.mtt_vente_caisse = mtt_vente_caisse;
	}

	public BigDecimal getMtt_vente_caisse_cloture() {
		return mtt_vente_caisse_cloture;
	}

	public void setMtt_vente_caisse_cloture(BigDecimal mtt_vente_caisse_cloture) {
		this.mtt_vente_caisse_cloture = mtt_vente_caisse_cloture;
	}

	public BigDecimal getMtt_vente_hors_caisse() {
		return mtt_vente_hors_caisse;
	}

	public void setMtt_vente_hors_caisse(BigDecimal mtt_vente_hors_caisse) {
		this.mtt_vente_hors_caisse = mtt_vente_hors_caisse;
	}

	public BigDecimal getMtt_salaire() {
		return mtt_salaire;
	}

	public void setMtt_salaire(BigDecimal mtt_salaire) {
		this.mtt_salaire = mtt_salaire;
	}

	public BigDecimal getMtt_achat() {
		return mtt_achat;
	}

	public void setMtt_achat(BigDecimal mtt_achat) {
		this.mtt_achat = mtt_achat;
	}

	public BigDecimal getMtt_depense_divers() {
		return mtt_depense_divers;
	}

	public void setMtt_depense_divers(BigDecimal mtt_depense_divers) {
		this.mtt_depense_divers = mtt_depense_divers;
	}

	public BigDecimal getMtt_recette_divers() {
		return mtt_recette_divers;
	}

	public void setMtt_recette_divers(BigDecimal mtt_recette_divers) {
		this.mtt_recette_divers = mtt_recette_divers;
	}

	public List<EtatFinanceDetailPersistant> getList_detail() {
		return list_detail;
	}

	public void setList_detail(List<EtatFinanceDetailPersistant> list_detail) {
		this.list_detail = list_detail;
	}

	public Integer getNbr_livraison() {
		return nbr_livraison;
	}

	public void setNbr_livraison(Integer nbr_livraison) {
		this.nbr_livraison = nbr_livraison;
	}

	public BigDecimal getMtt_livraison() {
		return mtt_livraison;
	}

	public void setMtt_livraison(BigDecimal mtt_livraison) {
		this.mtt_livraison = mtt_livraison;
	}

	public BigDecimal getMtt_tva_achat() {
		return mtt_tva_achat;
	}

	public void setMtt_tva_achat(BigDecimal mtt_tva_achat) {
		this.mtt_tva_achat = mtt_tva_achat;
	}

	public BigDecimal getMtt_tva_vente() {
		return mtt_tva_vente;
	}

	public void setMtt_tva_vente(BigDecimal mtt_tva_vente) {
		this.mtt_tva_vente = mtt_tva_vente;
	}

	public BigDecimal getMtt_raz_declare() {
		return mtt_raz_declare;
	}

	public void setMtt_raz_declare(BigDecimal mtt_raz_declare) {
		this.mtt_raz_declare = mtt_raz_declare;
	}

	public BigDecimal getMtt_depense_cheque_encais() {
		return mtt_depense_cheque_encais;
	}

	public void setMtt_depense_cheque_encais(BigDecimal mtt_depense_cheque_encais) {
		this.mtt_depense_cheque_encais = mtt_depense_cheque_encais;
	}

	public BigDecimal getMtt_depense_cheque_non_encais() {
		return mtt_depense_cheque_non_encais;
	}

	public void setMtt_depense_cheque_non_encais(BigDecimal mtt_depense_cheque_non_encais) {
		this.mtt_depense_cheque_non_encais = mtt_depense_cheque_non_encais;
	}

	public BigDecimal getMtt_recette_cheque_encais() {
		return mtt_recette_cheque_encais;
	}

	public void setMtt_recette_cheque_encais(BigDecimal mtt_recette_cheque_encais) {
		this.mtt_recette_cheque_encais = mtt_recette_cheque_encais;
	}

	public BigDecimal getMtt_recette_cheque_non_encais() {
		return mtt_recette_cheque_non_encais;
	}

	public void setMtt_recette_cheque_non_encais(BigDecimal mtt_recette_cheque_non_encais) {
		this.mtt_recette_cheque_non_encais = mtt_recette_cheque_non_encais;
	}

	public BigDecimal getMtt_vente_cheque_encais() {
		return mtt_vente_cheque_encais;
	}

	public void setMtt_vente_cheque_encais(BigDecimal mtt_vente_cheque_encais) {
		this.mtt_vente_cheque_encais = mtt_vente_cheque_encais;
	}

	public BigDecimal getMtt_vente_cheque_non_encais() {
		return mtt_vente_cheque_non_encais;
	}

	public void setMtt_vente_cheque_non_encais(BigDecimal mtt_vente_cheque_non_encais) {
		this.mtt_vente_cheque_non_encais = mtt_vente_cheque_non_encais;
	}

	public BigDecimal getMtt_achat_cheque_encais() {
		return mtt_achat_cheque_encais;
	}

	public void setMtt_achat_cheque_encais(BigDecimal mtt_achat_cheque_encais) {
		this.mtt_achat_cheque_encais = mtt_achat_cheque_encais;
	}

	public BigDecimal getMtt_achat_cheque_non_encais() {
		return mtt_achat_cheque_non_encais;
	}

	public void setMtt_achat_cheque_non_encais(BigDecimal mtt_achat_cheque_non_encais) {
		this.mtt_achat_cheque_non_encais = mtt_achat_cheque_non_encais;
	}

	public BigDecimal getMtt_resultat_net() {
		return mtt_resultat_net;
	}

	public void setMtt_resultat_net(BigDecimal mtt_resultat_net) {
		this.mtt_resultat_net = mtt_resultat_net;
	}

	public Boolean getIs_purge() {
		return is_purge;
	}

	public void setIs_purge(Boolean is_purge) {
		this.is_purge = is_purge;
	}

	public BigDecimal getMtt_ecart_tva() {
		return mtt_ecart_tva;
	}

	public void setMtt_ecart_tva(BigDecimal mtt_ecart_tva) {
		this.mtt_ecart_tva = mtt_ecart_tva;
	}

	public List<EtatFinancePaiementPersistant> getList_paiement() {
		return list_paiement;
	}

	public void setList_paiement(List<EtatFinancePaiementPersistant> list_paiement) {
		this.list_paiement = list_paiement;
	}

	public BigDecimal getMtt_livraison_part() {
		return mtt_livraison_part;
	}

	public void setMtt_livraison_part(BigDecimal mtt_livraison_part) {
		this.mtt_livraison_part = mtt_livraison_part;
	}
	public BigDecimal getMttLivraisonPartLivreur() {
		return BigDecimalUtil.substract(mtt_livraison, mtt_livraison_part); 
	}

	public BigDecimal getMtt_salaire_cheque_encais() {
		return mtt_salaire_cheque_encais;
	}

	public void setMtt_salaire_cheque_encais(BigDecimal mtt_salaire_cheque_encais) {
		this.mtt_salaire_cheque_encais = mtt_salaire_cheque_encais;
	}

	public BigDecimal getMtt_salaire_cheque_non_encais() {
		return mtt_salaire_cheque_non_encais;
	}

	public void setMtt_salaire_cheque_non_encais(BigDecimal mtt_salaire_cheque_non_encais) {
		this.mtt_salaire_cheque_non_encais = mtt_salaire_cheque_non_encais;
	}

	public BigDecimal getMtt_avoir() {
		return mtt_avoir;
	}

	public void setMtt_avoir(BigDecimal mtt_avoir) {
		this.mtt_avoir = mtt_avoir;
	}

	public BigDecimal getMtt_avoir_cheque_encais() {
		return mtt_avoir_cheque_encais;
	}

	public void setMtt_avoir_cheque_encais(BigDecimal mtt_avoir_cheque_encais) {
		this.mtt_avoir_cheque_encais = mtt_avoir_cheque_encais;
	}

	public BigDecimal getMtt_avoir_cheque_non_encais() {
		return mtt_avoir_cheque_non_encais;
	}

	public void setMtt_avoir_cheque_non_encais(BigDecimal mtt_avoir_cheque_non_encais) {
		this.mtt_avoir_cheque_non_encais = mtt_avoir_cheque_non_encais;
	}

	// Calculs
	public BigDecimal[] calculTotalRecetteDepense(){
		BigDecimal recette = BigDecimalUtil.add(this.mtt_recette_divers, this.mtt_vente_caisse_cloture, this.mtt_vente_hors_caisse, this.mtt_avoir);
		BigDecimal depense = BigDecimalUtil.add(this.mtt_achat, this.mtt_depense_divers, getMttLivraisonPartLivreur(), this.mtt_salaire);
		
		return new BigDecimal[]{recette, depense, BigDecimalUtil.substract(recette, depense)};
	}
	public BigDecimal[] calculTotalEtat(){
		BigDecimal totalEtatCheque = null, totalEtatBanque = null, totalEtatStock = null;
		if(this.list_detail != null){
			for (EtatFinanceDetailPersistant etatFinanceDetailP : list_detail) {
				if(etatFinanceDetailP.getType().equals("BANQ")){
					totalEtatBanque = BigDecimalUtil.add(totalEtatBanque, BigDecimalUtil.substract(etatFinanceDetailP.getMtt_etat_actuel(), etatFinanceDetailP.getMtt_etat_prev()));
				} else if(etatFinanceDetailP.getType().equals("EMPL")){
					totalEtatStock = BigDecimalUtil.add(totalEtatStock, BigDecimalUtil.substract(etatFinanceDetailP.getMtt_etat_actuel(), etatFinanceDetailP.getMtt_etat_prev()));
				}
			}
		}
		
		BigDecimal soldeChequeRecette = BigDecimalUtil.add(
				BigDecimalUtil.substract(this.mtt_recette_cheque_encais, this.mtt_recette_cheque_non_encais),
				BigDecimalUtil.substract(this.mtt_vente_cheque_encais, this.mtt_vente_cheque_non_encais),
				BigDecimalUtil.substract(this.mtt_avoir_cheque_encais, this.mtt_avoir_cheque_non_encais)
			);
		BigDecimal soldeChequeDepense = BigDecimalUtil.add(
				BigDecimalUtil.substract(this.mtt_depense_cheque_encais, this.mtt_depense_cheque_non_encais),
				BigDecimalUtil.substract(this.mtt_achat_cheque_encais, this.mtt_achat_cheque_non_encais),
				BigDecimalUtil.substract(this.mtt_salaire_cheque_encais, this.mtt_salaire_cheque_non_encais)
			);
	
		totalEtatCheque = BigDecimalUtil.substract(soldeChequeRecette, soldeChequeDepense);
			
		return new BigDecimal[]{totalEtatStock, totalEtatCheque, totalEtatBanque};
	}
}
