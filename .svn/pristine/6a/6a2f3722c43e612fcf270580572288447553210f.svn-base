package appli.model.domaine.stock.persistant.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;




@Entity
@Table(name = "mouvement_stock_view")
public class MouvementStockView implements Serializable {
	@Id
	private Long mouvement_id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_mouvement;
	@Column
	private String num_bl;
	@Column
	private String num_facture;
	@Column
	private Long mouvement_group_id;// Pour regrouper les achats
	@Column
	private Boolean is_groupant;// Si c'est un pere
	@Column
	private String type_mvmnt;
	@Column
	private String fourn_lib;
	@Column
	private Long fourn_id;
	@Column
	private String destination; 
	@Column
	private String emplacement;
	@Column
	private BigDecimal montant_ht;
	@Column
	private BigDecimal montant_ttc;
	@Column
	private BigDecimal montant_tva;
	@Column
	private Long abonne_id;
	@Column
	private Long societe_id;
	@Column
	private Long etablissement_id;
	@Transient
	private List<MouvementStockView> list_groupe;
	
	public Long getMouvement_id() {
		return mouvement_id;
	}
	public void setMouvement_id(Long mouvement_id) {
		this.mouvement_id = mouvement_id;
	}
	public Date getDate_mouvement() {
		return date_mouvement;
	}
	public void setDate_mouvement(Date date_mouvement) {
		this.date_mouvement = date_mouvement;
	}
//	public Date getDate_encaissement() {
//		return date_encaissement;
//	}
//	public void setDate_encaissement(Date date_encaissement) {
//		this.date_encaissement = date_encaissement;
//	}
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
	public String getFourn_lib() {
		return fourn_lib;
	}
	public void setFourn_lib(String fourn_lib) {
		this.fourn_lib = fourn_lib;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getEmplacement() {
		return emplacement;
	}
	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
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
	public Long getFourn_id() {
		return fourn_id;
	}
	public void setFourn_id(Long fourn_id) {
		this.fourn_id = fourn_id;
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
	public List<MouvementStockView> getList_groupe() {
		return list_groupe;
	}
	public void setList_groupe(List<MouvementStockView> list_groupe) {
		this.list_groupe = list_groupe;
	}
	public Long getEtablissement_id() {
		return etablissement_id;
	}
	public void setEtablissement_id(Long etablissement_id) {
		this.etablissement_id = etablissement_id;
	}
	public Long getAbonne_id() {
		return abonne_id;
	}
	public void setAbonne_id(Long abonne_id) {
		this.abonne_id = abonne_id;
	}
	public Long getSociete_id() {
		return societe_id;
	}
	public void setSociete_id(Long societe_id) {
		this.societe_id = societe_id;
	}
}
