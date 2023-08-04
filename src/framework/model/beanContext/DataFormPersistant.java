package framework.model.beanContext;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "data_form", indexes={
		@Index(name="IDX_DATA_FORM_FUNC", columnList="code_func")
	})
public class DataFormPersistant extends BasePersistant {
	@Column(length = 50, nullable=false)
	private String data_group;// Fonctionnalité : CLIENT, ARTICLE, ACHAT, FOURNISSEUR, ....
	@Column(length = 120, nullable=false)
	private String data_label;
	@Column(length = 120, nullable=false)
	private String data_code;
	@Column(length = 20)
	private Long data_enum;
	@Column(length = 30, nullable=false)
	private String data_type;//Long, Date, Decimal, Enum
	@Column(length = 5)
	private int data_order;
	@Column(length = 5)
	private int max_length;
	@Column(length = 250)
	private String data_style;//Style css
	@Column
	private boolean is_required;
	@Column
	private boolean is_default;//Données non supprimable
	
	public String getData_group() {
		return data_group;
	}

	public void setData_group(String data_group) {
		this.data_group = data_group;
	}

	public String getData_label() {
		return data_label;
	}

	public void setData_label(String data_label) {
		this.data_label = data_label;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public int getData_order() {
		return data_order;
	}

	public void setData_order(int data_order) {
		this.data_order = data_order;
	}

	public int getMax_length() {
		return max_length;
	}

	public void setMax_length(int max_length) {
		this.max_length = max_length;
	}

	public Long getData_enum() {
		return data_enum;
	}

	public void setData_enum(Long data_enum) {
		this.data_enum = data_enum;
	}

	public String getData_style() {
		return data_style;
	}

	public void setData_style(String data_style) {
		this.data_style = data_style;
	}

	public boolean getIs_required() {
		return is_required;
	}

	public void setIs_required(boolean is_required) {
		this.is_required = is_required;
	}

	public boolean getIs_default() {
		return is_default;
	}

	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}

	public String getData_code() {
		return data_code;
	}

	public void setData_code(String data_code) {
		this.data_code = data_code;
	}
}