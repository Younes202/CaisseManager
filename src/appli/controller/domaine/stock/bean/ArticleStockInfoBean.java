package appli.controller.domaine.stock.bean;

import java.math.BigDecimal;

public class ArticleStockInfoBean {
	private Long emplId;
	private Long articleId;
	private BigDecimal qte;
	private String sens;
	
	public ArticleStockInfoBean(Long emplId, Long articleId, BigDecimal qte, String sens){
		this.emplId = emplId;
		this.articleId = articleId;
		this.qte = qte;
		this.sens = sens;
	}

	public Long getEmplId() {
		return emplId;
	}

	public void setEmplId(Long emplId) {
		this.emplId = emplId;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public BigDecimal getQte() {
		return qte;
	}

	public void setQte(BigDecimal qte) {
		this.qte = qte;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}
}