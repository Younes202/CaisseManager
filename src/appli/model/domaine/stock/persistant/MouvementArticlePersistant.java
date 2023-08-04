package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;
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
import javax.persistence.Transient;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;


@Entity
@Table(name = "mouvement_article", indexes={
		@Index(name="IDX_MVM_ART_FUNC", columnList="code_func")
	})
public class MouvementArticlePersistant extends BasePersistant  {
	@Column(length = 15, scale = 6, precision = 13)
	private  BigDecimal quantite_bal;// Quantité balisage
	
	@Column(length = 15, scale = 6, precision = 13, nullable = false)
	private  BigDecimal quantite;// Quantitité unitaire (stock)
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_ht;// Prix achat HT 

	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_ttc;// Prix achat TTC
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_vente;// Prix vente TTC
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_vente_ht;// Prix vente TTC
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_ht_total;

	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal prix_ttc_total;
	
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal remise;//Taux ou mtt
	@Column
	private  Boolean is_remise_ratio;// Si remise poucent
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_peremption;

/*********************** Liens ************************************/
	@GsonExclude
	@ManyToOne
	@JoinColumn(name = "mouvement_id", referencedColumnName="id")
	private MouvementPersistant opc_mouvement;
	
	@ManyToOne
	@JoinColumn(name = "article_id", nullable=false, referencedColumnName="id")
	private ArticlePersistant opc_article;
	
	@ManyToOne
	@JoinColumn(name = "unite_enum_id", referencedColumnName="id")
	private ValTypeEnumPersistant opc_unite_enum;
	
	@ManyToOne
	@JoinColumn(name = "tva_enum", referencedColumnName="id")
	private ValTypeEnumPersistant opc_tva_enum;
	
	@OneToOne
	@JoinColumn(name = "lot_art_id", referencedColumnName="id")
	private LotArticlePersistant opc_lot_article;

	@Transient
	private int idxIhm;

	public BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}

	public BigDecimal getPrix_ht() {
		return prix_ht;
	}

	public void setPrix_ht(BigDecimal prix_ht) {
		this.prix_ht = prix_ht;
	}

	public BigDecimal getRemise() {
		return remise;
	}

	public void setRemise(BigDecimal remise) {
		this.remise = remise;
	}

	public Date getDate_peremption() {
		return date_peremption;
	}

	public void setDate_peremption(Date date_peremption) {
		this.date_peremption = date_peremption;
	}

	public MouvementPersistant getOpc_mouvement() {
		return opc_mouvement;
	}

	public void setOpc_mouvement(MouvementPersistant opc_mouvement) {
		this.opc_mouvement = opc_mouvement;
	}

	public ArticlePersistant getOpc_article() {
		return opc_article;
	}

	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}

	public ValTypeEnumPersistant getOpc_tva_enum() {
		return opc_tva_enum;
	}

	public void setOpc_tva_enum(ValTypeEnumPersistant opc_tva_enum) {
		this.opc_tva_enum = opc_tva_enum;
	}

	public int getIdxIhm() {
		return idxIhm;
	}

	public void setIdxIhm(int idxIhm) {
		this.idxIhm = idxIhm; 
	}

	public BigDecimal getPrix_vente() {
		return prix_vente;
	}

	public void setPrix_vente(BigDecimal prix_vente) {
		this.prix_vente = prix_vente;
	}
	
	public BigDecimal getPrix_ttc() {
		return prix_ttc;
	}

	public void setPrix_ttc(BigDecimal prix_ttc) {
		this.prix_ttc = prix_ttc;
	}

	public BigDecimal getPrix_ht_total() {
		return prix_ht_total;
	}

	public void setPrix_ht_total(BigDecimal prix_ht_total) {
		this.prix_ht_total = prix_ht_total;
	}

	public BigDecimal getPrix_ttc_total() {
		return prix_ttc_total;
	}

	public void setPrix_ttc_total(BigDecimal prix_ttc_total) {
		this.prix_ttc_total = prix_ttc_total;
	}

	public LotArticlePersistant getOpc_lot_article() {
		return opc_lot_article;
	}

	public void setOpc_lot_article(LotArticlePersistant opc_lot_article) {
		this.opc_lot_article = opc_lot_article;
	}

	public BigDecimal getPrix_vente_ht() {
		return prix_vente_ht;
	}

	public void setPrix_vente_ht(BigDecimal prix_vente_ht) {
		this.prix_vente_ht = prix_vente_ht;
	}

	public Boolean getIs_remise_ratio() {
		return is_remise_ratio;
	}

	public void setIs_remise_ratio(Boolean is_remise_ratio) {
		this.is_remise_ratio = is_remise_ratio;
	}

	public ValTypeEnumPersistant getOpc_unite_enum() {
		return opc_unite_enum;
	}

	public void setOpc_unite_enum(ValTypeEnumPersistant opc_unite_enum) {
		this.opc_unite_enum = opc_unite_enum;
	}

	public BigDecimal getQuantite_bal() {
		return quantite_bal;
	}

	public void setQuantite_bal(BigDecimal quantite_bal) {
		this.quantite_bal = quantite_bal;
	}

//	public BigDecimal getMttTTC(){
//		ValTypeEnumPersistant opc_tva_enumArt = this.opc_article.getOpc_tva_enum();
//		//
//		if(opc_tva_enum != null || opc_tva_enumArt != null){
//			BigDecimal tauxTva = null;
//			
//			if(opc_tva_enum != null){
//				if(NumericUtil.isNum(opc_tva_enum.getLibelle()) || NumericUtil.isDecimal(opc_tva_enum.getLibelle())){
//					tauxTva = BigDecimalUtil.get(opc_tva_enum.getCode());
//				} else if(NumericUtil.isNum(opc_tva_enum.getCode()) || NumericUtil.isDecimal(opc_tva_enum.getCode())){
//					tauxTva = BigDecimalUtil.get(opc_tva_enum.getCode());
//				}
//			}
//			// On récipére de l'article si TVA null
//			if(tauxTva == null && opc_tva_enumArt != null){
//				if(NumericUtil.isNum(opc_tva_enumArt.getLibelle()) || NumericUtil.isDecimal(opc_tva_enumArt.getLibelle())){
//					tauxTva = BigDecimalUtil.get(opc_tva_enumArt.getCode());
//				} else if(NumericUtil.isNum(opc_tva_enumArt.getCode()) || NumericUtil.isDecimal(opc_tva_enumArt.getCode())){
//					tauxTva = BigDecimalUtil.get(opc_tva_enumArt.getCode());
//				}
//			}
//			
//			if(tauxTva == null){
//				return prix_ht;
//			}
//			BigDecimal mttTotalTVA = BigDecimalUtil.divide(BigDecimalUtil.multiply(prix_ht, tauxTva), BigDecimalUtil.get(100));
//			return BigDecimalUtil.add(prix_ht, mttTotalTVA);
//		} else{
//			return prix_ht;
//		}
//	}
}
