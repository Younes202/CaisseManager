package appli.model.domaine.administration.persistant;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import framework.model.beanContext.BasePersistant;

@SuppressWarnings("serial")
@Entity
@Table(name = "job", indexes={
		@Index(name="IDX_JOB_FUNC", columnList="code_func")
	})
@NamedQuery(name="job_find", query="from JobPersistant job order by job.start_date desc")
public class JobPersistant extends BasePersistant  {
	@Column(length = 80)
	private String job_label;
	@Column
	@Lob
	private String job_log;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date start_date;
	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	private Date end_date;
	@Column(length = 1, nullable = false)
	private String statut;

	/**
	 * @return the start_date
	 */
	public Date getStart_date() {
		return start_date;
	}
	/**
	 * @param start_date the start_date to set
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	/**
	 * @return the end_date
	 */
	public Date getEnd_date() {
		return end_date;
	}
	/**
	 * @param end_date the end_date to set
	 */
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	/**
	 * @return the statut
	 */
	public String getStatut() {
		return statut;
	}
	/**
	 * @param statut the statut to set
	 */
	public void setStatut(String statut) {
		this.statut = statut;
	}
	/**
	 * @return the job_label
	 */
	public String getJob_label() {
		return job_label;
	}
	/**
	 * @param job_label the job_label to set
	 */
	public void setJob_label(String job_label) {
		this.job_label = job_label;
	}
	public String getJob_log() {
		return job_log;
	}
	public void setJob_log(String job_log) {
		this.job_log = job_log;
	}
	
}
