package appli.model.domaine.caisse.persistant;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.common.util.NumericUtil;

/**
 *
 * @author 
 */
@Entity
@Table(name = "ticketCaisse", indexes={
		@Index(name="IDX_TCK_CA_FUNC", columnList="code_func")
	})
@NamedQueries({
	@NamedQuery(name="ticketCaisse_find", query="from TicketCaissePersistant ticketCaisse "
			+ " order by ticketCaisse.opc_enum_type.libelle, ticketCaisse.date_reception desc")
})
public class TicketCaissePersistant extends BasePersistant { 
    @Column(nullable=false, length=15)
    private Long numero_debut;
    @Column(nullable=false, length=15)
    private Long numero_fin;
	
    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date date_reception;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enum_type_id", referencedColumnName = "id")
	private ValTypeEnumPersistant opc_enum_type;
    
	public Long getNumero_debut() {
		return numero_debut;
	}

	public void setNumero_debut(Long numero_debut) {
		this.numero_debut = numero_debut;
	}

	public Long getNumero_fin() {
		return numero_fin;
	}

	public void setNumero_fin(Long numero_fin) {
		this.numero_fin = numero_fin;
	}

	public Date getDate_reception() {
		return date_reception;
	}

	public void setDate_reception(Date date_reception) {
		this.date_reception = date_reception;
	}

	public Integer getNbrTicketCaisse() {
		return (NumericUtil.getIntegerOrDefault(numero_fin)-NumericUtil.getIntegerOrDefault(numero_debut)+1);
	}

	public ValTypeEnumPersistant getOpc_enum_type() {
		return opc_enum_type;
	}

	public void setOpc_enum_type(ValTypeEnumPersistant opc_enum_type) {
		this.opc_enum_type = opc_enum_type;
	}
}
