/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.vente.persistant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

/**
 *
 * @author 
 */
@Entity
@Table(name = "list_choix", indexes={
		@Index(name="IDX_LC_CODE", columnList="code"),
		@Index(name="IDX_LC_FUNC", columnList="code_func")
	})
@NamedQuery(name="listChoix_find", query="from ListChoixPersistant listChoix" +
		" order by listChoix.code, listChoix.libelle")
public class ListChoixPersistant extends BasePersistant {
    @Column(length=30, nullable = false)// Unique mais dans validateur
    private String code;

    @Column(length = 120, nullable=false)
    private String libelle;
    
    @Column
    private Boolean is_disable;
    
    @GsonExclude
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="choix_id", referencedColumnName="id")
	List<ListChoixDetailPersistant> list_choix_detail;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Boolean getIs_disable() {
		return is_disable;
	}

	public void setIs_disable(Boolean is_disable) {
		this.is_disable = is_disable;
	}

	public List<ListChoixDetailPersistant> getList_choix_detail() {
		return list_choix_detail;
	}

	public void setList_choix_detail(List<ListChoixDetailPersistant> list_choix_detail) {
		this.list_choix_detail = list_choix_detail;
	}
}
