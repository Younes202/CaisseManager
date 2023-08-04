package appli.model.domaine.administration.persistant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.beanContext.BasePersistant;

@SuppressWarnings("serial")
@Entity
@Table(name = "message_detail", indexes={
		@Index(name="IDX_MSG_DET_FUNC", columnList="code_func")
	})
public class MessageDetailPersistant extends BasePersistant implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id", nullable=false)
    private MessagePersistant opc_message;
    
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable=false)
    private ClientPersistant opc_destinataire;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MessagePersistant getOpc_message() {
		return opc_message;
	}

	public void setOpc_message(MessagePersistant opc_message) {
		this.opc_message = opc_message;
	}

	public ClientPersistant getOpc_destinataire() {
		return opc_destinataire;
	}

	public void setOpc_destinataire(ClientPersistant opc_destinataire) {
		this.opc_destinataire = opc_destinataire;
	}
}
