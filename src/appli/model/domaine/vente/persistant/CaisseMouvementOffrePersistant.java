/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.vente.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import appli.model.domaine.personnel.persistant.OffrePersistant;
import framework.model.beanContext.BasePersistant;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "caisse_mouvement_offre", indexes={
		@Index(name="IDX_CM_OFFR_FUNC", columnList="code_func")
	})
public class CaisseMouvementOffrePersistant extends BasePersistant {
    @Column(length = 15, scale = 6, precision = 15)
    private BigDecimal mtt_reduction;
    
    @ManyToOne
    @JoinColumn(name = "mvm_caisse_id", referencedColumnName="id")
    private CaisseMouvementPersistant opc_mouvement_caisse;
    
    @ManyToOne
    @JoinColumn(name = "offre_id", referencedColumnName="id")
    private OffrePersistant opc_offre;

     @Column
    private Boolean is_annule;

    public CaisseMouvementPersistant getOpc_mouvement_caisse() {
        return opc_mouvement_caisse;
    }

    public void setOpc_mouvement_caisse(CaisseMouvementPersistant opc_mouvement_caisse) {
        this.opc_mouvement_caisse = opc_mouvement_caisse;
    }

    public OffrePersistant getOpc_offre() {
        return opc_offre;
    }

    public void setOpc_offre(OffrePersistant opc_offre) {
        this.opc_offre = opc_offre;
    }

    public Boolean getIs_annule() {
        return is_annule;
    }

    public void setIs_annule(Boolean is_annule) {
        this.is_annule = is_annule;
    }

	public BigDecimal getMtt_reduction() {
		return mtt_reduction;
	}

	public void setMtt_reduction(BigDecimal mtt_reduction) {
		this.mtt_reduction = mtt_reduction;
	}
    
}