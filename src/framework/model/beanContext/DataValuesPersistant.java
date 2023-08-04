package framework.model.beanContext;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "data_value", indexes={
		@Index(name="IDX_DATA_VAL_FUNC", columnList="code_func")
	})
public class DataValuesPersistant extends BasePersistant {
	@Column(length = 50, nullable=false)
	private String data_group;// Fonctionnalité : CLIENT, ARTICLE, ACHAT, FOURNISSEUR, ....
	@Column(length = 20, nullable=false)
	private Long element_id;
	@Column(length = 255)
	private String data_value;
	@Column(length = 120, nullable=false)
	private String data_code;
	@ManyToOne
    @JoinColumn(name = "form_id", referencedColumnName="id", nullable=false)
    private DataFormPersistant opc_data_form;
	
	public String getData_group() {
		return data_group;
	}
	public void setData_group(String data_group) {
		this.data_group = data_group;
	}
	public Long getElement_id() {
		return element_id;
	}
	public void setElement_id(Long element_id) {
		this.element_id = element_id;
	}
	public String getData_value() {
		return data_value;
	}
	public void setData_value(String data_value) {
		this.data_value = data_value;
	}
	public DataFormPersistant getOpc_data_form() {
		return opc_data_form;
	}
	public void setOpc_data_form(DataFormPersistant opc_data_form) {
		this.opc_data_form = opc_data_form;
	}
	public String getData_code() {
		return data_code;
	}
	public void setData_code(String data_code) {
		this.data_code = data_code;
	}
}