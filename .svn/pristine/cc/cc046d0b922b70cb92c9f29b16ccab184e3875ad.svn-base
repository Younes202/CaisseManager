package appli.model.domaine.administration.persistant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.beanContext.BasePersistant;
import framework.model.util.GsonExclude;

@Entity
@Table(name = "message", indexes={
		@Index(name="IDX_MSG_FUNC", columnList="code_func")
	})	
@NamedQuery(name="message_find", query="from MessagePersistant message order by message.date_envoi desc")
public class MessagePersistant extends BasePersistant implements Serializable {
	@Column(length = 120, nullable = false)
	private String sujet;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date date_envoi;

	@Column
	@Lob
	private String text;
	
	@Column(length = 1)
	private Integer is_piece_jointe;

	@GsonExclude
    @OrderBy(value="date_envoi desc")
    @OneToMany
	@JoinColumn(name="message_id", referencedColumnName="id", insertable=false, updatable=false)
	private List<MessageDetailPersistant> list_detail;
    
	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<MessageDetailPersistant> getList_detail() {
		return list_detail;
	}

	public void setList_detail(List<MessageDetailPersistant> list_detail) {
		this.list_detail = list_detail;
	}
	

	public Date getDate_envoi() {
		return date_envoi;
	}

	public void setDate_envoi(Date date_envoi) {
		this.date_envoi = date_envoi;
	}
	
	public Integer getIs_piece_jointe() {
		return is_piece_jointe;
	}

	public void setIs_piece_jointe(Integer is_piece_jointe) {
		this.is_piece_jointe = is_piece_jointe;
	}

	public String getDestinataires(){
		String dest = "";
		
		if(this.list_detail != null){
			for (MessageDetailPersistant messageDetailPersistant : list_detail) {
				ClientPersistant destP = messageDetailPersistant.getOpc_destinataire();
				dest = dest + destP.getMail()+"["+destP.getNom()+" "+destP.getPrenom()+"]<br>";
			}
		}
		return dest;
	}
}
