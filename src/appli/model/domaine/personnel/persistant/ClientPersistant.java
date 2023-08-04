package appli.model.domaine.personnel.persistant;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.stock.persistant.PersonneBasePersistant;
import framework.model.beanContext.ComptePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "client", 
uniqueConstraints={
        @UniqueConstraint(columnNames = {"login", "etablissement_id"})
	},
indexes={
		@Index(name="IDX_CLI_FUNC", columnList="code_func"),
		@Index(name="IDX_CLI_CIN", columnList="cin"),
		@Index(name="IDX_CLI_NUM", columnList="numero")
	})
@NamedQueries({
	@NamedQuery(name = "client_find", query = "from ClientPersistant client order by client.nom"),
	@NamedQuery(name="portefeuille_find", query="from ClientPersistant client "
			+ "where client.is_portefeuille is not null and client.is_portefeuille=1 "
			+ "order by client.nom")
})
public class ClientPersistant extends PersonneBasePersistant  {
	@Column(length = 30)
    private String type_client;// PM=personne morale, PP=personne physique
	
	//---- compte client ------------
	@Column
	private Boolean is_visible;
	@Column
	private Boolean is_reduc_art;// Réduction article par article
	@Column
	private Boolean is_solde_neg;// Autoriser solde négatif
    @Column(length = 150)
    private String ets_fav;
	
    //-------------------------- Synchro centrale --------------------------//
  	@Column(length = 80)
  	private String origine_auth;
  	//-----------------------------------------------------------------------
  	
	@Column(length = 10)
	private String statut;
	@Column(length = 15, scale = 6, precision = 15)
	private  BigDecimal plafond_dette;
	@Column
    private Boolean is_portefeuille;
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal taux_portefeuille;
    @Column(length = 15, scale = 6, precision = 15)
	private BigDecimal solde_portefeuille = BigDecimalUtil.ZERO;// Champs calculé par les rechargements carte
    
    // Commande en ligne -----------------
	@Column(length = 30)
    private String password;
    @Column(length = 50)
    private String login;
    @Column
    private Boolean is_statut_cmd;
    @Column
    private Boolean is_offre_cmd;
    // -----------------------------------
    @Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date date_naissance;
	@Column(length=255)
	private String commentaire;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_connexion;
    
    @Column(length = 25, scale = 20, precision = 25)
    private BigDecimal position_lat;
    @Column(length = 25, scale = 20, precision = 25)
    private BigDecimal position_lng;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compte_id", referencedColumnName="id")
	private ComptePersistant opc_compte;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupe_id", referencedColumnName="id")
	private ClientGroupePersistant opc_groupe; 
	
    @GsonExclude
    @OneToMany
   	@JoinColumn(name="client_id", referencedColumnName="id", insertable=false, updatable=false)
    private List<CarteFideliteClientPersistant> list_cartes;
    
    @GsonExclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<ClientContactPersistant> list_contact;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getIs_visible() {
		return is_visible;
	}

	public void setIs_visible(Boolean is_visible) {
		this.is_visible = is_visible;
	}

	public Boolean getIs_reduc_art() {
		return is_reduc_art;
	}

	public void setIs_reduc_art(Boolean is_reduc_art) {
		this.is_reduc_art = is_reduc_art;
	}

	public Boolean getIs_solde_neg() {
		return is_solde_neg;
	}

	public void setIs_solde_neg(Boolean is_solde_neg) {
		this.is_solde_neg = is_solde_neg;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public BigDecimal getPlafond_dette() {
		return plafond_dette;
	}

	public void setPlafond_dette(BigDecimal plafond_dette) {
		this.plafond_dette = plafond_dette;
	}

	public Date getDate_connexion() {
		return date_connexion;
	}

	public void setDate_connexion(Date date_connexion) {
		this.date_connexion = date_connexion;
	}

	public List<CarteFideliteClientPersistant> getList_cartes() {
		return list_cartes;
	}

	public void setList_cartes(List<CarteFideliteClientPersistant> list_cartes) {
		this.list_cartes = list_cartes;
	}

	public List<ClientContactPersistant> getList_contact() {
		return list_contact;
	}

	public void setList_contact(List<ClientContactPersistant> list_contact) {
		this.list_contact = list_contact;
	}

	public String getType_client() {
		return type_client;
	}

	public void setType_client(String type_client) {
		this.type_client = type_client;
	}

	public Boolean getIs_portefeuille() {
		return is_portefeuille;
	}

	public void setIs_portefeuille(Boolean is_portefeuille) {
		this.is_portefeuille = is_portefeuille;
	}

	public BigDecimal getTaux_portefeuille() {
		return taux_portefeuille;
	}

	public void setTaux_portefeuille(BigDecimal taux_portefeuille) {
		this.taux_portefeuille = taux_portefeuille;
	}

	public BigDecimal getSolde_portefeuille() {
		return solde_portefeuille;
	}

	public void setSolde_portefeuille(BigDecimal solde_portefeuille) {
		this.solde_portefeuille = solde_portefeuille;
	}
	
	public String getEts_fav() {
		return ets_fav;
	}

	public void setEts_fav(String ets_fav) {
		this.ets_fav = ets_fav;
	}

	public String getOrigine_auth() {
		return origine_auth;
	}

	public void setOrigine_auth(String origine_auth) {
		this.origine_auth = origine_auth;
	}

	public BigDecimal getPosition_lat() {
		return position_lat;
	}

	public void setPosition_lat(BigDecimal position_lat) {
		this.position_lat = position_lat;
	}

	public BigDecimal getPosition_lng() {
		return position_lng;
	}

	public void setPosition_lng(BigDecimal position_lng) {
		this.position_lng = position_lng;
	}
	public Boolean getIs_statut_cmd() {
		return is_statut_cmd;
	}

	public void setIs_statut_cmd(Boolean is_statut_cmd) {
		this.is_statut_cmd = is_statut_cmd;
	}

	public Boolean getIs_offre_cmd() {
		return is_offre_cmd;
	}

	public void setIs_offre_cmd(Boolean is_offre_cmd) {
		this.is_offre_cmd = is_offre_cmd;
	}

	public ComptePersistant getOpc_compte() {
		return opc_compte;
	}

	public void setOpc_compte(ComptePersistant opc_compte) {
		this.opc_compte = opc_compte;
	}

	public Date getDate_naissance() {
		return date_naissance;
	}

	public void setDate_naissance(Date date_naissance) {
		this.date_naissance = date_naissance;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public ClientGroupePersistant getOpc_groupe() {
		return opc_groupe;
	}

	public void setOpc_groupe(ClientGroupePersistant opc_groupe) {
		this.opc_groupe = opc_groupe;
	}
}