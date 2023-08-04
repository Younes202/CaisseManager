package appli.model.domaine.vente.persistant;

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

import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

/**
 *
 * @author 
 */
@Entity
@Table(name = "ticketCaisse_conf", indexes={
		@Index(name="IDX_TICK_CAI_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name="ticketCaisseConf_find", query="from TicketCaisseConfPersistant ticketCaisseConf "
			+ " order by ticketCaisseConf.libelle")
})
public class TicketCaisseConfPersistant extends BasePersistant { 
    @Column(nullable=false, length=150)
    private String libelle;
    @Column(length=2)
    private Integer font_size;
    @Column(length=1)
    private String font_weight;
    @Column(length=4)
    private Integer back_pos;
    @Column(length=4)
    private Integer vertical_space;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "enum_type_id", referencedColumnName="id")
//    private ValTypeEnumPersistant opc_enum_type;
    @GsonExclude
	@OrderBy("idxIhm")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "attest_conf_id", referencedColumnName = "id")
	List<TicketCaisseConfDetailPersistant> list_detail;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<TicketCaisseConfDetailPersistant> getList_detail() {
		return list_detail;
	}

	public void setList_detail(List<TicketCaisseConfDetailPersistant> list_detail) {
		this.list_detail = list_detail;
	}

//	public ValTypeEnumPersistant getOpc_enum_type() {
//		return opc_enum_type;
//	}
//
//	public void setOpc_enum_type(ValTypeEnumPersistant opc_enum_type) {
//		this.opc_enum_type = opc_enum_type;
//	}

	public Integer getFont_size() {
		return font_size;
	}

	public void setFont_size(Integer font_size) {
		this.font_size = font_size;
	}

	public String getFont_weight() {
		return font_weight;
	}

	public void setFont_weight(String font_weight) {
		this.font_weight = font_weight;
	}

	public Integer getBack_pos() {
		return back_pos;
	}

	public void setBack_pos(Integer back_pos) {
		this.back_pos = back_pos;
	}
	
	public Integer getVertical_space() {
		return vertical_space;
	}

	public void setVertical_space(Integer vertical_space) {
		this.vertical_space = vertical_space;
	}
}
