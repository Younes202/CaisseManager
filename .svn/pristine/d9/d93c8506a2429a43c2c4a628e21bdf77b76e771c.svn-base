package appli.model.domaine.administration.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.habilitation.persistant.ProfilePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.StringUtil;

@MappedSuperclass
public abstract class UserBasePersistant extends BasePersistant {
    @Column(length = 30, nullable = false)
    private String password;
    @Column(length = 50, nullable = false)
    private String login;
	@Column(length = 30)
	private String badge;
	@Column(length = 150)
	private String image;
	@Column(length = 80)
	private String salt;
	@Column(length = 200)
	private String favoris_nav;
	
	//------------------ Identite ------------------------//
	@Column(length = 50)
	private String nom;
	@Column(length = 50)
	private String prenom;
		
    @Column
    private Boolean is_desactive;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_connexion;
    
    @Column(length = 25, scale = 20, precision = 25)
	private BigDecimal position_lat;
    @Column(length = 25, scale = 20, precision = 25)
	private BigDecimal position_lng;
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column
    private Date date_position;
    @Column
    private Boolean is_tovalidate;
    
    /**
     * ********************* Liens ***********************************
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", nullable=false)
    private ProfilePersistant opc_profile;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile2_id", referencedColumnName = "id")
    private ProfilePersistant opc_profile2;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile3_id", referencedColumnName = "id")
    private ProfilePersistant opc_profile3;
    
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

    public Date getDate_connexion() {
        return date_connexion;
    }

    public void setDate_connexion(Date date_connexion) {
        this.date_connexion = date_connexion;
    }

    public ProfilePersistant getOpc_profile() {
        return opc_profile;
    }

    public void setOpc_profile(ProfilePersistant opc_profile) {
        this.opc_profile = opc_profile;
    }

	public Boolean getIs_desactive() {
		return is_desactive;
	}

	public void setIs_desactive(Boolean is_desactive) {
		this.is_desactive = is_desactive;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getFavoris_nav() {
		if(StringUtil.isEmpty(this.favoris_nav)){
			if(ContextGloabalAppli.getAbonementBean() == null){
				return "";
			}
			boolean isCompta = ContextGloabalAppli.getAbonementBean().isOptCompta(); 
			boolean isPromo = ContextGloabalAppli.getAbonementBean().isOptCommercial();
//			boolean isRh = ContextGloabalAppli.getAbonementBean().isOptRh();
//			boolean isAuto = ContextGloabalAppli.getAbonementBean().isOptAuto();
			boolean isStock = ContextGloabalAppli.getAbonementBean().isOptStock();
			
			return "cai-journee;"
					+ "cai-article;"
					+ (isStock ? "stock-etat_stock;" : "")
					+ (isStock ? "stock-inventaire;" : "")
					+ (isStock ? "stock-vnt-achat;" : "")
					+ (isStock ? "stock-charges-depense;" : "")
					
					+ (isCompta ? "compta-bilan;" : "")
					+ (isCompta ? "compta-facture;" : "")
					
					+ "utilisateurs;"
					+ (isPromo ? "cai-client;" : "")
					
					+ "cai-gestion;"
					;
		}
		
		return favoris_nav;
	}

	public void setFavoris_nav(String favoris_nav) {
		this.favoris_nav = favoris_nav;
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

	public Date getDate_position() {
		return date_position;
	}

	public void setDate_position(Date date_position) {
		this.date_position = date_position;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getIs_tovalidate() {
		return is_tovalidate;
	}

	public void setIs_tovalidate(Boolean is_tovalidate) {
		this.is_tovalidate = is_tovalidate;
	}

	public ProfilePersistant getOpc_profile2() {
		return opc_profile2;
	}

	public void setOpc_profile2(ProfilePersistant opc_profile2) {
		this.opc_profile2 = opc_profile2;
	}

	public ProfilePersistant getOpc_profile3() {
		return opc_profile3;
	}

	public void setOpc_profile3(ProfilePersistant opc_profile3) {
		this.opc_profile3 = opc_profile3;
	}
	
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getNomPrenom() {
		if(this.nom != null) {
			return this.nom+(StringUtil.isNotEmpty(this.prenom) ? " "+this.prenom : "");	
		}
		return null;
	}
	
	//-------------------------------------------------------//
	public String getAllEnvs() {
		String envs = "";
		if(this.opc_profile != null) {
			envs += opc_profile.getEnvs();
		}
		if(this.opc_profile2 != null) {
			envs += opc_profile2.getEnvs();
		}
		if(this.opc_profile3 != null) {
			envs += opc_profile3.getEnvs();
		}
		return envs;
	}
	public boolean getIsMultiMobileEnv() {
		String[] envs = StringUtil.getArrayFromStringDelim(getAllEnvs(), ";");
		
		return (envs != null && envs.length > 1);
	}
	public boolean isEnvGranted(String env, UserPersistant userP) {
		String[] envs = StringUtil.getArrayFromStringDelim(userP.getAllEnvs(), ";");
		
		if(!"mob-client".equals(env)) {
			if("ADMIN".equals(userP.getOpc_profile().getCode())) {
				return true;
			}
		} else {
			if("CLIENT".equals(userP.getOpc_profile().getCode())) {
				return true;
			}
		}
		
		if(envs == null) {
			return false;
		}
		for (String e : envs) {
			if(e.equals(env)) {
				return true;
			}
		}
		return false;
	}
	public boolean isInProfile(String code) {
		if(this.opc_profile == null) {
			return false;
		}
		if(code.equals(this.opc_profile.getCode())){
			return true;
		}
		if(this.opc_profile2 != null && code.equals(this.opc_profile2.getCode())){
			return true;
		}
		if(this.opc_profile3 != null && code.equals(this.opc_profile3.getCode())){
			return true;
		}
		return false;
	}
}
