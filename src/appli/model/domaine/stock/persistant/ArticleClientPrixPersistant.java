package appli.model.domaine.stock.persistant;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import appli.model.domaine.personnel.persistant.ClientPersistant;
import framework.model.beanContext.BasePersistant;

@Entity 
@NamedQuery(name = "prix_composant_find", query = "from ArticleClientPrixPersistant composantClientPrix "
		+ "where composantClientPrix.opc_client.id = '{clientId}' "
		+ "order by composantClientPrix.opc_article.libelle")
@Table(name = "article_client_prix", indexes={
		@Index(name="IDX_ART_CLI_PR_FUNC", columnList="code_func")
	})
public class ArticleClientPrixPersistant extends BasePersistant {
	@Column(length = 15, scale = 6, precision = 15)
	private BigDecimal mtt_prix;
	@ManyToOne
	@JoinColumn(name = "article_id", referencedColumnName="id")
	private ArticlePersistant opc_article;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName="id")
    private ClientPersistant opc_client;
    
	public BigDecimal getMtt_prix() {
		return mtt_prix;
	}
	public void setMtt_prix(BigDecimal mtt_prix) {
		this.mtt_prix = mtt_prix;
	}
	public ArticlePersistant getOpc_article() {
		return opc_article;
	}
	public void setOpc_article(ArticlePersistant opc_article) {
		this.opc_article = opc_article;
	}
	public ClientPersistant getOpc_client() {
		return opc_client;
	}
	public void setOpc_client(ClientPersistant opc_client) {
		this.opc_client = opc_client;
	}
}
