package appli.model.domaine.stock.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.beanContext.SocietePersistant;

public class FactureBean {
	private String numero_facture;
	private Date date_facture;
	private String mode_paiement;
	
	private BigDecimal taux_tva;
	private BigDecimal mtt_tva;
	private BigDecimal mtt_net_ht;
	private BigDecimal mtt_net_ttc;
	
	private ClientPersistant opc_client;
	private SocietePersistant opc_societe;
	private List<FactureDetailBean> listDetail;
	public String getNumero_facture() {
		return numero_facture;
	}
	public void setNumero_facture(String numero_facture) {
		this.numero_facture = numero_facture;
	}
	public Date getDate_facture() {
		return date_facture;
	}
	public void setDate_facture(Date date_facture) {
		this.date_facture = date_facture;
	}
	public ClientPersistant getOpc_client() {
		return opc_client;
	}
	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}
	public SocietePersistant getOpc_societe() {
		return opc_societe;
	}
	public void setOpc_societe(SocietePersistant opc_societe) {
		this.opc_societe = opc_societe;
	}
	public List<FactureDetailBean> getListDetail() {
		return listDetail;
	}
	public void setListDetail(List<FactureDetailBean> listDetail) {
		this.listDetail = listDetail;
	}
	public String getMode_paiement() {
		return mode_paiement;
	}
	public void setMode_paiement(String mode_paiement) {
		this.mode_paiement = mode_paiement;
	}
	public BigDecimal getTaux_tva() {
		return taux_tva;
	}
	public void setTaux_tva(BigDecimal taux_tva) {
		this.taux_tva = taux_tva;
	}
	public BigDecimal getMtt_tva() {
		return mtt_tva;
	}
	public void setMtt_tva(BigDecimal mtt_tva) {
		this.mtt_tva = mtt_tva;
	}
	public BigDecimal getMtt_net_ht() {
		return mtt_net_ht;
	}
	public void setMtt_net_ht(BigDecimal mtt_net_ht) {
		this.mtt_net_ht = mtt_net_ht;
	}
	public BigDecimal getMtt_net_ttc() {
		return mtt_net_ttc;
	}
	public void setMtt_net_ttc(BigDecimal mtt_net_ttc) {
		this.mtt_net_ttc = mtt_net_ttc;
	}
}


class FactureDetailBean{
	private String libelle;
	private BigDecimal mtt_ht_unit;
	private int quantite;
	private BigDecimal mtt_ttc_unit;
	private BigDecimal tva;
	private BigDecimal mtt_total_ht;
	private BigDecimal mtt_total_ttc;
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public BigDecimal getMtt_ht_unit() {
		return mtt_ht_unit;
	}
	public void setMtt_ht_unit(BigDecimal mtt_ht_unit) {
		this.mtt_ht_unit = mtt_ht_unit;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	public BigDecimal getMtt_ttc_unit() {
		return mtt_ttc_unit;
	}
	public void setMtt_ttc_unit(BigDecimal mtt_ttc_unit) {
		this.mtt_ttc_unit = mtt_ttc_unit;
	}
	public BigDecimal getTva() {
		return tva;
	}
	public void setTva(BigDecimal tva) {
		this.tva = tva;
	}
	public BigDecimal getMtt_total_ht() {
		return mtt_total_ht;
	}
	public void setMtt_total_ht(BigDecimal mtt_total_ht) {
		this.mtt_total_ht = mtt_total_ht;
	}
	public BigDecimal getMtt_total_ttc() {
		return mtt_total_ttc;
	}
	public void setMtt_total_ttc(BigDecimal mtt_total_ttc) {
		this.mtt_total_ttc = mtt_total_ttc;
	}
}
