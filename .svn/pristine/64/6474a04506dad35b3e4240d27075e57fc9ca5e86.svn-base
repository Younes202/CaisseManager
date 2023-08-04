/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.vente.persistant;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.personnel.persistant.EmployePersistant;
import framework.model.beanContext.BasePersistant;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "caisse_mouvement_statut", indexes={
		@Index(name="IDX_CAI_MVM_STAT_FUNC", columnList="code_func")
	})
public class CaisseMouvementStatutPersistant extends BasePersistant {
	@Column(length = 10, nullable = false)
	private String statut_cmd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date_statut;

	@ManyToOne
	@JoinColumn(name = "caisse_mvm_id", referencedColumnName = "id", nullable = false)
	private CaisseMouvementPersistant opc_caisse_mouvement;
	@ManyToOne
	@JoinColumn(name = "employe_id", referencedColumnName = "id")
	private EmployePersistant opc_employe;

	public String getStatut_cmd() {
		return statut_cmd;
	}

	public void setStatut_cmd(String statut_cmd) {
		this.statut_cmd = statut_cmd;
	}

	public Date getDate_statut() {
		return date_statut;
	}

	public void setDate_statut(Date date_statut) {
		this.date_statut = date_statut;
	}

	public CaisseMouvementPersistant getOpc_caisse_mouvement() {
		return opc_caisse_mouvement;
	}

	public void setOpc_caisse_mouvement(
			CaisseMouvementPersistant opc_caisse_mouvement) {
		this.opc_caisse_mouvement = opc_caisse_mouvement;
	}

	public EmployePersistant getOpc_employe() {
		return opc_employe;
	}

	public void setOpc_employe(EmployePersistant opc_employe) {
		this.opc_employe = opc_employe;
	}
}
