/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.personnel.persistant;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "offre", indexes={
		@Index(name="IDX_OFF_FUNC", columnList="code_func"),
		@Index(name="IDX_OFF_CODE", columnList="code"),
	})
@NamedQuery(name="offre_find", query="from OffrePersistant offre "
		+ "where offre.destination!='S' "
		+ "order by offre.destination, offre.date_debut desc, offre.libelle")
public class OffrePersistant extends BasePersistant  {
    @Column(length = 80)
    private String code;
    @Column(length = 120, nullable = false)
    private String libelle;
    @Column
	private Boolean is_disable;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_debut;
	@Column(length = 4)
	private Integer ordre;
    @Column
    private Boolean is_ventil;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_fin;
    
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_seuil;
    
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_plafond;
    
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal taux_reduction;

    @Column(length = 1, nullable=false)
    private String destination;// E=employé, C=client, A=all
    
    @Column(length = 1, nullable=false)
    private String type_offre;//P=>Prix d'achat, R=>Réduction
    
    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public BigDecimal getMtt_seuil() {
        return mtt_seuil;
    }

    public void setMtt_seuil(BigDecimal mtt_seuil) {
        this.mtt_seuil = mtt_seuil;
    }

    public BigDecimal getMtt_plafond() {
        return mtt_plafond;
    }

    public void setMtt_plafond(BigDecimal mtt_plafond) {
        this.mtt_plafond = mtt_plafond;
    }

    public BigDecimal getTaux_reduction() {
        return taux_reduction;
    }

    public void setTaux_reduction(BigDecimal taux_reduction) {
        this.taux_reduction = taux_reduction;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getType_offre() {
		return type_offre;
	}

	public void setType_offre(String type_offre) {
		this.type_offre = type_offre;
	}

	public Boolean getIs_ventil() {
		return is_ventil;
	}

	public void setIs_ventil(Boolean is_ventil) {
		this.is_ventil = is_ventil;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getIs_disable() {
		return is_disable;
	}

	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable; 
	}

	public Integer getOrdre() {
		return ordre;
	}

	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}
}
