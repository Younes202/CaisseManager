package framework.model.util.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="data_synchronise")
public class SynchronisePersistant {
	@Id
	@Column(nullable = false)
	@GeneratedValue
	private Long id;
	@Column(length = 50)
	private String code;
	@Column(length = 2)
	private String action;
	@Column(length = 50)
	private String classType;
	@Column
	private Long element_id;
	@Column(length=80)
	private String ets_code;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_creation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Long getElement_id() {
		return element_id;
	}

	public void setElement_id(Long element_id) {
		this.element_id = element_id;
	}
	public String getEts_code() {
		return ets_code;
	}

	public void setEts_code(String ets_code) {
		this.ets_code = ets_code;
	}

	public Date getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}
}