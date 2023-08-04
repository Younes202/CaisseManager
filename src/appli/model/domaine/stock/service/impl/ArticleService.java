package appli.model.domaine.stock.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.hibernate.criterion.Order;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.stock.bean.ArticleBean;
import appli.controller.domaine.stock.bean.FamilleBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.persistant.NotificationPersistant;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.caisse.persistant.ArticleBalancePersistant;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import appli.model.domaine.stock.persistant.ArticleDetailPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.DemandeTransfertArticlePersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.InventaireDetailPersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.LotArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.PreparationArticlePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.impl.FamilleService.FAMILLE_TYPE_ENUM;
import appli.model.domaine.stock.validator.ArticleValidator;
import appli.model.domaine.util_srv.printCom.ticket.PrintCodeBarreBalanceUtil;
import appli.model.domaine.util_srv.printCom.ticket.PrintCodeBarreUtil;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.ListChoixDetailPersistant;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.DataFormPersistant;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.exception.ActionValidationException;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.util.ModelConstante;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@WorkModelClassValidator(validator=ArticleValidator.class)
@Named
@SuppressWarnings("unchecked") 
public class ArticleService extends GenericJpaService<ArticleBean, Long> implements IArticleService {
	@Inject
	private IFamilleService familleService;
	@Inject
	private IParametrageService parametrageService;
	@Inject
	private IValTypeEnumService valEnumService;
	
	@Override
	public List<ArticlePersistant> getListArticleStock(boolean isActifOnly) {
		String req = "from ArticlePersistant art "
				+ "where art.is_stock=true and code != 'GEN' ";
		if(isActifOnly){
			req = req + " and (is_disable is null or is_disable=0) ";
		}
		req = req + "order by opc_famille_stock.b_left, art.ordre, art.code, art.libelle ";
		
		return getQuery(req)
				.getResultList();
	}
	
	@Override
	public List<ArticlePersistant> getListFicheComposant(boolean isActifOnly) {
		String req = "from ArticlePersistant art "
				+ "where is_stock=true and is_fiche is not null and is_fiche = 1 and code != 'GEN' ";
		if(isActifOnly){
			req = req + " and (is_disable is null or is_disable=0) ";
		}
		req = req + "order by opc_famille_stock.b_left, art.ordre, art.code, art.libelle ";
		
		return getQuery(req)
				.getResultList();
	}
	
	@Override
	public List<ArticlePersistant> getListArticleNetAndStock(boolean isActifOnly) {
		String req = "from ArticlePersistant "
				+ "where is_stock=true and (nature='N' or nature is null) and code != 'GEN' ";
		if(isActifOnly){
			req = req + " and (is_disable is null or is_disable=0) ";
		}
		req = req + "order by opc_famille_stock.b_left, ordre, code, libelle ";
		
		return getQuery(req)
				.getResultList();
	}
	
	@Override
	public List<ArticlePersistant> getListArticleNonStock(boolean isActifOnly) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		String req = "select art from ArticlePersistant art "
				+ "where (art.is_stock is null or art.is_stock=false) and code != 'GEN' ";
		if(isActifOnly){
			req = req + " and (art.is_disable is null or art.is_disable=0) ";
		}
		if(isRestau){
			req = req + "order by opc_famille_cuisine.b_left, art.ordre, art.code, art.libelle ";			
		} else{
			req = req + "order by opc_famille_stock.b_left, art.ordre, art.code, art.libelle ";
		}
		
		return getQuery(req)
				.getResultList();
	}
	
	@Override
	public List<ArticlePersistant> getListArticleNonStock() {
		return getQuery("from ArticlePersistant art "
				+ "where (art.is_stock is null or art.is_stock=0) and code != 'GEN' "
				+ "order by opc_famille_stock.b_left, art.ordre, art.code, art.libelle ")
				.getResultList();
	}
	
	@Override
	public List<CaissePersistant> getListCaisseActive(String typeCaisse){
		String request = "from CaissePersistant where type_ecran=:type ";
		request = request + "and (is_desactive is null or is_desactive=false)";
		request = request + " order by reference";
		
		return getQuery(request).setParameter("type", typeCaisse).getResultList();
	}
	
	public String refreshCodeBarre(String codeBarre) {
		BarcodeGenerator gen = null;
		String checkSum = codeBarre.substring(codeBarre.length()-1);
		BarcodeUtil util = BarcodeUtil.getInstance();
		try {
			gen = util.createBarcodeGenerator(PrintCodeBarreUtil.buildCfg("ean-13"));
			gen.generateBarcode(null, codeBarre);
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().indexOf(":") != -1){
				checkSum = e.getMessage().substring(e.getMessage().indexOf(":")+1).trim();
				codeBarre = codeBarre.substring(0, codeBarre.length()-1)+checkSum;
			}
		}
		
		return codeBarre;
	}
	
	private String generateCodeBarreNonBalance(Long famId){
		String startCode = "399";// Code manuel
		
		BigDecimal max_num = BigDecimalUtil.get(""+getQuery("select count(0) from ArticlePersistant where opc_famille_stock.id=:famId")
			.setParameter("famId", famId)
			.getSingleResult());
		
		
//		String req = "select max(CAST(SUBSTR(code_barre, 3, 9) AS UNSIGNED)) "
//				+ "from article "
//				+ "where LENGTH(code_barre)>5 and code_barre like '"+startCode+"%'";
		
//		Query query = getNativeQuery(req);
//		BigDecimal max_num = null;//BigDecimalUtil.get(""+query.getSingleResult());
		
		int nextCode = 1;
		if(max_num != null){
			nextCode = (max_num.intValue() + 1);
		}
		
		int numRand = new Random().nextInt(99 - 10 + 1) + 10;
		String finalCode = startCode + "" + famId;
		
		int length = (""+nextCode).length();
		while(finalCode.length()+length<10){
			finalCode = finalCode + "0";
		}
		finalCode = finalCode + numRand + "" + nextCode;
		int codeCtrl = PrintCodeBarreUtil.CHECK_SUM(finalCode);
		finalCode = finalCode + codeCtrl;
		
		return refreshCodeBarre(finalCode);
	}
	
	@Override
	public String generateCodeBarre(Long famId) {
		boolean isBalance = false;
		List<CaissePersistant> listCaisse = getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString());
		for (CaissePersistant caisseP : listCaisse) {
			
			if(StringUtil.isEmpty(caisseP.getFamille_balance())){
				continue;
			}
			if(caisseP.getFamille_balance().indexOf("|"+famId+"|") != -1){
				isBalance = true;
			}
		}
		if(isBalance){
			return generateCodeBarreBalance(famId);
		} else{
			return generateCodeBarreNonBalance(famId);
		}
	}
		
	private String generateCodeBarreBalance(Long famId) {	
		String startCode = "22";
		List<CaissePersistant> listCaisse = getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString());
		String numeroBalance = "";
		int barreLength = 11;
		for (CaissePersistant caisseP : listCaisse) {
			if(StringUtil.isEmpty(caisseP.getFamille_balance())){
				continue;
			}
			if(caisseP.getFamille_balance().indexOf("|"+famId+"|") != -1){
				ParametragePersistant paramBarStartP = parametrageService.getParameterByCode("CODE_BARRE_BALANCE", caisseP.getId());
				ParametragePersistant paramBarCompoP = parametrageService.getParameterByCode("CODE_BARRE_BALANCE_COMPO", caisseP.getId());
				ParametragePersistant paramNumBalanceP = parametrageService.getParameterByCode("BALANCE_NUM", caisseP.getId());
				
				if(paramNumBalanceP != null && StringUtil.isNotEmpty(paramNumBalanceP.getValeur())){
					numeroBalance = paramNumBalanceP.getValeur().trim();
				}
				
				//
				if(paramBarCompoP != null && StringUtil.isNotEmpty(paramBarCompoP.getValeur())){
					String[] composition = PrintCodeBarreBalanceUtil.getInfosCdeBarreBalance(paramBarCompoP.getValeur(), "1234567891011");
			    	String barre = composition[1];
			    	barreLength = barre.length();
				}
				if(paramBarStartP != null && StringUtil.isNotEmpty(paramBarStartP.getValeur())){
					startCode = paramBarStartP.getValeur();
					break;
				}
			}
		}
		
		String req = "select max(CAST(SUBSTR(code_barre, 3, "+barreLength+") AS UNSIGNED)) "
				+ "from article "
				+ "where LENGTH(code_barre)>5 and code_barre like '"+startCode+"%'";
		
		Query query = getNativeQuery(req);
				
		BigDecimal max_num = BigDecimalUtil.get(""+query.getSingleResult());
		
		String nextCode = "1";
		if(max_num != null){
			nextCode = ""+(max_num.intValue() + 1);
		}
		
		if(StringUtil.isNotEmpty(numeroBalance) && !nextCode.startsWith(numeroBalance) && nextCode.length()<=5){
			nextCode = numeroBalance+nextCode;
		}
		
		nextCode = startCode+nextCode;
		while(nextCode.length()<(barreLength+2)){
			nextCode = nextCode + "0";
		}
		
		return nextCode;
	}
	@Override
	public String generateCleBalance(Long famId) {
		List<CaissePersistant> listCaisse = getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString());
		Long caisseId = null;
		for (CaissePersistant caisseP : listCaisse) {
			if(StringUtil.isEmpty(caisseP.getFamille_balance())){
				continue;
			}
			if(caisseP.getFamille_balance().indexOf("|"+famId+"|") != -1){
				caisseId = caisseP.getId();
				break;
			}
		}
		
		if(caisseId == null){
			return "";
		}
		String req = "select max(max_direct_bal) from caisse where id=:caisseId";
		
		Query query = getNativeQuery(req)
				.setParameter("caisseId", caisseId);
				
		BigDecimal max_num = (BigDecimal)query.getSingleResult();
		
		String nextCode = "1";
		if(max_num != null){
			nextCode = ""+(max_num.intValue() + 1);
		}
		
		return nextCode;
	}
	
	@Override
	public String generateCode(Long famId, String type) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		FamillePersistant parentFam = findById(FamillePersistant.class, famId);
		String code = parentFam.getCode();
		
		if(code == null) {
			code = (famId+"-"+type+"-"+new Random(10).nextInt());
		}
		
		String req = "select  "
				+ "max("
				+ "	case when LENGTH(code) > "+code.length()+" then CAST(SUBSTR(code, "+(code.length()+1)+") AS UNSIGNED) "
				+ " else 0 end "
				+ ") from article "
				+ "where ";
		if(type.equals("ART")) {
			req = req + (isRestau ? "famille_cuisine_id=:famId " : "famille_stock_id=:famId ");
		} else {
			req = req + "famille_stock_id=:famId ";
		}
		
		Query query = getNativeQuery(req).setParameter("famId", parentFam.getId());
				
		BigDecimal max_num = BigDecimalUtil.get(""+query.getSingleResult());
		
		String nextCode = "1";
		if(max_num != null){
			nextCode = ""+(max_num.intValue() + 1);
		}
		
		return code+nextCode;
	}
	
	@Override
	public List<ArticleStockInfoPersistant> getListArticleView(Long articleId){
		return getQuery("from ArticleStockInfoPersistant art "
				+ "where art.opc_article.id=:artId "
				+ "order by emplacement_id, opc_article.id")
				.setParameter("artId", articleId)
				.getResultList();                                                           
	}


	@Override
	public ArticlePersistant getArticleByCodeBarre(String codeBarre, boolean isArticle) {
		String req = "from ArticlePersistant art where art.code_barre=:codeBarre";
		
		if(isArticle){
			req += " and (is_stock is null or is_stock=0) ";
		}
		
		List<ArticlePersistant> listData = getQuery(req)
				.setParameter("codeBarre", codeBarre)
				.getResultList();
		
		return (listData.size() > 0 ? listData.get(0) : null);
	}
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long articleId) {
		ArticlePersistant articlePersistant = findById(ArticlePersistant.class, articleId);
		articlePersistant.setIs_disable(BooleanUtil.isTrue(articlePersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(articlePersistant); 
	}

	@Override
	public List<ArticlePersistant> getListComposantsActifs(Long familleId) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		String req = "from ArticlePersistant where opc_famille_stock.id=:famId ";
		if(isRestau){
			req += "and opc_famille_stock is not null ";
		} else{
			
		}
		req += "and (is_fiche is null or is_fiche=0) "
				+ "and (is_disable is null or is_disable=0) and code != 'GEN' "
				+ "order by opc_famille_stock.b_left, ordre, libelle, code";
		
		List<ArticlePersistant> data = getQuery(req)
				.setParameter("famId", familleId)
				.getResultList();
		
		return data;
	}
	
	@Override
	public List<ArticlePersistant> getListArticleActifs(Long familleId, boolean isMobile) {
		return getListArticleActifs(familleId, null, isMobile);
	}
	@Override
	public List<ArticlePersistant> getListArticleActifs(Long familleId, 
			PagerBean pagerBean, boolean isMobile) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		String req = "from ArticlePersistant where "+(isRestau ? "opc_famille_cuisine.id": "opc_famille_stock.id") +"=:famId "
				+ (isRestau ? "and (is_stock is null or is_stock=0) " : " ")
				+ "and (is_disable is null or is_disable=0) "
				+ "and (is_noncaisse is null or is_noncaisse = 0) "
				+ (isMobile ? "and (is_nonmobile is null or is_nonmobile = 0) " : " ")
				+ "and code != 'GEN' ";
		
		// Count
		if(pagerBean != null){
			Long count = (Long) getQuery("select count(0) "+req).setParameter("famId", familleId).getSingleResult(); 
			pagerBean.setNbrLigne(count.intValue());
		}
		
		Query query = getQuery(req
				+ " order by ordre, libelle")
				.setParameter("famId", familleId);
		
		if(pagerBean != null){
			query.setFirstResult(pagerBean.getStartIdx());
			query.setMaxResults(pagerBean.getElementParPage());
		}
		return query.getResultList();
	}
	
	@Override
	public ArticlePersistant getArticleByCode(String code) {
		return (ArticlePersistant) getSingleResult(getQuery("from ArticlePersistant where code=:code").setParameter("code", code));
	}
	
	@Override
	public List<ArticlePersistant> getListArticleStockActif(Long famId) {
		return getQuery("from ArticlePersistant where "
				+ "(is_disable is null or is_disable=0) and code != 'GEN' "
				+ "and opc_famille_stock.id=:famId "
				+ "order by ordre, libelle")
			.setParameter("famId", famId)
			.getResultList();
	}

	@Override
	public List<ArticlePersistant> getListArticleNonStockActif(Long famId) {
		return getQuery("from ArticlePersistant where "
				+ "(is_disable is null or is_disable=0) and code != 'GEN' "
				+ "and (is_stock is null or is_stock=0) "
				+ "and opc_famille_stock.id=:famId "
				+ "order by ordre, libelle")
			.setParameter("famId", famId)
			.getResultList();
	}
	@Override
	public List<ArticlePersistant> getListFamilleArticle(Long famId, PagerBean pagerBean) {
		return getQuery("from ArticlePersistant where opc_famille_stock.id=:famId "
				+ "and (is_disable is null or is_disable=0) "
				+ "and (is_stock is null or is_stock=0) "
				+ " order by ordre, libelle")
					.setParameter("famId", famId)
					.setMaxResults(pagerBean.getElementParPage())
					.setFirstResult(pagerBean.getStartIdx())
					.getResultList();
	}
	
	
	@Override
	public List<ArticlePersistant> getListArticleCuisineActif(Long famId) {
		return getQuery("from ArticlePersistant where (is_disable is null or is_disable=0) and code != 'GEN' "
				+ "and famille_cuisine_id=:famId "
				+ "and (is_noncaisse is null or is_noncaisse = 0) "
				+ "order by ordre, libelle")
			.setParameter("famId", famId)
			.getResultList();
	}
	
	@Override
	public Map<ArticlePersistant, BigDecimal[]> calculMargeArticles(List<ArticlePersistant> listArticles){
		Map<ArticlePersistant, BigDecimal[]> mapArticles = new LinkedHashMap<>();
		for (ArticlePersistant articleP : listArticles) {
			BigDecimal mttAchat = BigDecimalUtil.ZERO, marge = BigDecimalUtil.ZERO;
			// Calcul prix achat article
			mttAchat = getComposantsOfArticle(articleP.getList_article(), mttAchat, BigDecimalUtil.get(1));
			
			BigDecimal margePourcent = BigDecimalUtil.ZERO;
			if(!BigDecimalUtil.isZero(articleP.getPrix_vente()) && !BigDecimalUtil.isZero(mttAchat)){
				marge = BigDecimalUtil.substract(articleP.getPrix_vente(), mttAchat);
				margePourcent = BigDecimalUtil.divide(BigDecimalUtil.multiply(marge, BigDecimalUtil.get(100)), articleP.getPrix_vente());
			}
			mapArticles.put(articleP, new BigDecimal[]{mttAchat, articleP.getPrix_vente(), marge, margePourcent});
		}
		
		return mapArticles;
	}
	
	@Override
	public List<ArticlePersistant> getListArticleChecked(Long[] checkedArticles){
		Set<Long> listIds = new HashSet();
		for (Long id : checkedArticles) {
			listIds.add(id);
		}
		
		return getQuery("from ArticlePersistant where id in (:ids)")
				.setParameter("ids", listIds)
				.getResultList();
	}
	
	private BigDecimal getComposantsOfArticle(List<ArticleDetailPersistant> listDetail, BigDecimal mttAchat, BigDecimal quantiteParent) {
		for (ArticleDetailPersistant articleDetP : listDetail) {
			BigDecimal prix_achat = articleDetP.getOpc_article_composant().getPrix_achat_ttc();
			if(BigDecimalUtil.isZero(prix_achat)){
				prix_achat = articleDetP.getOpc_article_composant().getPrix_achat_ht();
			}
			
			if(!BigDecimalUtil.isZero(prix_achat)){
				BigDecimal mttAchatQte = BigDecimalUtil.multiply(quantiteParent, BigDecimalUtil.multiply(prix_achat, articleDetP.getQuantite()));// Unité achat / vente
				mttAchat = BigDecimalUtil.add(mttAchat, mttAchatQte);
			}
			// Ajouter les composants du composant$
			if(articleDetP.getOpc_article_composant().getList_article().size() > 0) {
			BigDecimal mttAchatEnfants = getComposantsOfArticle(articleDetP.getOpc_article_composant().getList_article(), mttAchat, articleDetP.getQuantite());
			mttAchat = BigDecimalUtil.add(mttAchat, mttAchatEnfants);
			}
		}        
        return mttAchat;
	}
	
	@Override
	@Transactional
	public void dupliquerEnFicheArticle(Long[] listComposantsIds, Long familleId, String dest, BigDecimal prixVente) {
		for (Long composantId : listComposantsIds) {
			ArticlePersistant composantP = findById(composantId);
			ArticlePersistant ficheP = null;
			List<ArticleDetailPersistant> listDetail = null;
			// Vérification existance article
			List<ArticlePersistant> listData = getQuery("from ArticlePersistant where (is_stock is null or is_stock=0) and code=:code")
				.setParameter("code", composantP.getCode()+"_C")
				.getResultList();
			if(listData.size() > 0){
				ficheP = listData.get(0);
				listDetail = ficheP.getList_article();
				listDetail.clear();
			} else{
				ficheP = new ArticlePersistant();
				listDetail = new ArrayList<>();
			}
			
			ficheP.setCode(composantP.getCode()+"_C");
			ficheP.setLibelle(composantP.getLibelle());
			ficheP.setCode_barre(composantP.getCode_barre());
			ficheP.setIs_stock(false);
			ficheP.setDescription(dest);
			ficheP.setOpc_famille_cuisine(findById(FamilleCuisinePersistant.class, familleId));
			// Infos vente
			ficheP.setOpc_unite_vente_enum(composantP.getOpc_unite_vente_enum());
			ficheP.setPrix_vente(prixVente);
//			ficheP.setUnite_vente_quantite(composantP.getveUnite_vente_quantite());
//			ficheP.setPrix_vente_gros(composantP.getPrix_vente_gros());
//			ficheP.setQte_seuil_gros(composantP.getQte_seuil_gros());
			
			ArticleDetailPersistant artDet = new ArticleDetailPersistant();
			artDet.setIdxIhm(1);
			artDet.setOpc_article(ficheP);
			artDet.setOpc_article_composant(composantP);
			artDet.setQuantite(BigDecimalUtil.get(1));
			
			listDetail.add(artDet);
			ficheP.setList_article(listDetail);
			
			ficheP = getEntityManager().merge(ficheP);
			
			// Copier les images
			String imgPath = StrimUtil._GET_PATH("article")+"/"+composantId;
			File img = new File(imgPath);
			
			if(img.exists()){
				String destF = StrimUtil._GET_PATH("article")+"/"+ficheP.getId();
				try {
					FileUtils.moveDirectory(img, new File(destF));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public List<ArticlePersistant> getArticlesAutocomplete(String value, boolean isActifOnly, boolean isStock) {
		boolean isRestau = ContextAppli.IS_RESTAU_ENV();
		String req = "from ArticlePersistant art where code != 'GEN' ";
		if(StringUtil.isNotEmpty(value)) {
			req = req + "and (UPPER(art.code_barre) like :code "
					+ "or UPPER(art.composition) like :code "
					+ "or UPPER(art.libelle) like :code "
					+ "or UPPER(art.code) like :code) ";
			
			if(isRestau) {
				if(isStock) {
					req = req +"and art.is_stock is not null and art.is_stock=1 ";				
				} else {
					req = req + "and (art.is_stock is null or art.is_stock=0) ";
				}
			}
			if(isActifOnly){
				req = req + " and (is_disable is null or is_disable=0) ";
			}
		}
		Query query = getQuery(req);
		if(StringUtil.isNotEmpty(value)) {
			query.setParameter("code", "%"+value.toUpperCase()+"%");
		}
		return query.setMaxResults(50).getResultList();
	}

	@Override
	public FamillePersistant getGenericFamille() {
		FamillePersistant genFamP = (FamillePersistant) getSingleResult(getQuery("from FamillePersistant art where code=:code ")
			.setParameter("code", "GEN"));
		
		return genFamP;
	}
	
	@Override
	@Transactional
	public void addGenericFamille() {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		FamillePersistant genFamP = (FamillePersistant) getSingleResult(getQuery("from FamillePersistant art where code=:code ")
				.setParameter("code", "GEN"));
		FamilleBean genFam = null;
		if(genFamP == null){
			Long parentId = familleService.getFamilleRoot(isRestau ? "CU" : "ST").getId();
			
			genFam = new FamilleBean();
			genFam.setType(isRestau ? "CU" : "ST");
			genFam.setCode("GEN");
			genFam.setLibelle("Autre");
			genFam.setParent_id(parentId);
			genFam.setElement_id(parentId);
			//
			familleService.createFamille(genFam);
			getEntityManager().flush();
		}
	}
	
	@Override
	@Transactional
	public ArticlePersistant getGenericArticle(FamillePersistant genFamP) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		ArticlePersistant genArt = (ArticlePersistant) getSingleResult(getQuery("from ArticlePersistant art where code=:code ")
						.setParameter("code", "GEN"));
		
		if(genArt == null){
			genArt = new ArticlePersistant();
			genArt.setCode("GEN");
			genArt.setLibelle("Générique");
			
			if(isRestau){
				genArt.setOpc_famille_cuisine((FamilleCuisinePersistant) genFamP);	
			} else{
				genArt.setOpc_famille_stock((FamilleStockPersistant) genFamP);	
			}
			genArt = getEntityManager().merge(genArt);
		}
		
		return genArt;
	}
	
	@Override
	@Transactional
	public void synchroniserArticleBalance(CaissePersistant balanceCaisse){
		String dbName = parametrageService.getParameterByCode("BALANCE_DB_NAME", balanceCaisse.getId()).getValeur();
		String dbPw= parametrageService.getParameterByCode("BALANCE_DB_PW", balanceCaisse.getId()).getValeur();
		String dbUser = parametrageService.getParameterByCode("BALANCE_DB_USER", balanceCaisse.getId()).getValeur();
		String dbPort = parametrageService.getParameterByCode("BALANCE_DB_PORT", balanceCaisse.getId()).getValeur();
		String dbHost = parametrageService.getParameterByCode("BALANCE_DB_PATH", balanceCaisse.getId()).getValeur();
		String dbType = parametrageService.getParameterByCode("BALANCE_DB_TYPE", balanceCaisse.getId()).getValeur();
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		if(StringUtil.isEmpty(dbHost) 
				|| StringUtil.isEmpty(dbType) 
				|| StringUtil.isEmpty(dbUser) 
				|| StringUtil.isEmpty(dbPort)
				|| StringUtil.isEmpty(dbName)){
			MessageService.addGrowlMessage("", "La base de données de la balance n'est pas configurée");
			return;
		}
		
		String IdEmpresa = parametrageService.getParameterByCode("BALANCE_EMPRESA", balanceCaisse.getId()).getValeur();
		if(StringUtil.isEmpty(IdEmpresa)){
			MessageService.addGrowlMessage("", "L'identifiant Epmresa est obligatoire dans la conf de la balance.");
			return;
		}
		
		ParametragePersistant paramBarCompoP = parametrageService.getParameterByCode("CODE_BARRE_BALANCE_COMPO", balanceCaisse.getId());
		
		int barreLength = 5;
		if(paramBarCompoP != null && StringUtil.isNotEmpty(paramBarCompoP.getValeur())){
			String[] composition = PrintCodeBarreBalanceUtil.getInfosCdeBarreBalance(paramBarCompoP.getValeur(), "1234567891011");
	    	String barre = composition[1];
	    	barreLength = barre.length();
		}
		
		List<ArticlePersistant> listArt = null;
		String[] famillesbalance = StringUtil.getArrayFromStringDelim(balanceCaisse.getFamille_balance(), "|");
		if(famillesbalance != null){
			Set<Long> setIds = new HashSet<>();
			for (String famId : famillesbalance) {
				if(StringUtil.isEmpty(famId)) {
					continue;
				}
				setIds.add(Long.valueOf(famId));
			}
			
			if(setIds.size() > 0) {
			listArt = getQuery("from ArticlePersistant where "+(isRestau ? "opc_famille_cuisine" : "opc_famille_stock")+".id in (:famIds)")
				.setParameter("famIds", setIds)
				.getResultList();
			}
		}
		
		if(listArt == null || listArt.size() == 0){
			MessageService.addGrowlMessage("", "Aucun article balance n'a été trouvé.");
			return;
		}
		
		Connection conAdministrator = null;
		try{
			// Register driver
			if("mysql".equals(dbType)){
				File jarFile = new File(getClass().getResource("/appli/conf/mysql-connector-java-5.1.35.jar").getFile());
				URLClassLoader cl = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, ClassLoader.getSystemClassLoader());
				
				Driver driver = (Driver) cl.loadClass("com.mysql.jdbc.Driver").newInstance();
				
				Properties prop = new Properties();
			    prop.put("user", dbUser);
			    prop.put("password", dbPw);
				
				conAdministrator = driver.connect("jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName, prop);
						
//				Class.forName( "com.mysql.jdbc.Driver");
//				conAdministrator = DriverManager.getConnection("jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName, dbUser, dbPw);
			} else{
				Class.forName( "net.ucanaccess.jdbc.UcanaccessDriver" );
				conAdministrator = DriverManager.getConnection("jdbc:ucanaccess://"+dbHost+":"+dbPort+"/"+dbName, dbUser, dbPw);
			}
			
			// Open a connection to the database
		      Statement stmt = conAdministrator.createStatement();
		      EntityManager em = getEntityManager();
		      String reqMax = "select max(max_direct_bal) from caisse where id=:caisseId";
		      Query query = getNativeQuery(reqMax)
						.setParameter("caisseId", balanceCaisse.getId());
						
				BigDecimal max_num = BigDecimalUtil.get(""+query.getSingleResult());
				int max = (max_num == null ? 0 : max_num.intValue());
				
			   List<Integer> codeBarreTraite = new ArrayList<>();	
		      for (ArticlePersistant artP : listArt) {
		    	String codeBarre = artP.getCode_barre();
				if(StringUtil.isEmpty(codeBarre)){
		    		  continue;
		    	  }
				  codeBarre = codeBarre.trim();
		    	  
		    	  int unit = 1;
		    	  
		    	  String uniteVente = "KG";// Par defaut
		    	  ValTypeEnumPersistant uniteVenteP = artP.getOpc_unite_vente_enum();
		    	  if(uniteVenteP != null){
		    		  uniteVente = artP.getOpc_unite_vente_enum().getCode();
		    	  }
					
		    	  if(uniteVente.equalsIgnoreCase("P")){
		    		  unit = 2;
		    	  }
		    	  
		    	int indexEnd = codeBarre.length() < barreLength+2 ? codeBarre.length() : 2+barreLength;
				String code_barre = codeBarre.substring(2, indexEnd);
			    int codeBarInt = Integer.valueOf(code_barre);
			    
			    if(codeBarreTraite.contains(codeBarInt)){
			    	continue;
			    }
			    
			    codeBarreTraite.add(codeBarInt);
			    
				int direcTkeyInt = (artP.getCode_direct_bal()==null ? 0 : artP.getCode_direct_bal());
			    
				if(direcTkeyInt == 0){
					max = max + 1;
			    	artP.setCode_direct_bal(max);
			    	direcTkeyInt = max;
			    	
					em.merge(artP);
			    }
			   
			      ResultSet rs = null;
			      // Balance
//			      String sql = "SELECT IdEmpresa FROM dat_empresa where IdEmpresa="+balanceCaisse.getId();
//			      rs = stmt.executeQuery(sql);
//			      
//			      int IdEmpresa = -1;
//			      while(rs.next()){
//			    	  IdEmpresa = rs.getInt("IdEmpresa");
//			      }
//			      if(IdEmpresa < 0){
//			    	  sql = "INSERT INTO dat_empresa (IdEmpresa, NombreEmpresa) values ("+balanceCaisse.getId()+", '"+balanceCaisse.getReference().replaceAll("'", "''")+"')";
//			    	  IdEmpresa = balanceCaisse.getId().intValue();
//			      } else{
//			    	  sql = "UPDATE dat_empresa set NombreEmpresa='"+balanceCaisse.getReference().replaceAll("'", "''")+"' where IdEmpresa="+balanceCaisse.getId();
//			      }
//			      rs.close();
//			      stmt.executeUpdate(sql);
			      
			      // Etablissement
//			      sql = "SELECT IdTienda FROM dat_tienda where IdTienda="+etsP.getId();
//			      rs = stmt.executeQuery(sql);
//			      int IdTienda = -1;
//			      while(rs.next()){
//			    	  IdTienda = rs.getInt("IdTienda");
//			      }
//			      if(IdTienda < 0){
//			    	  sql = "INSERT INTO dat_tienda (IdTienda, IdEmpresa, Nombre) values ("+etsP.getId()+", "+IdEmpresa+", '"+etsP.getNom().replaceAll("'", "''")+"')";
//			    	  IdTienda = etsP.getId().intValue();
//			      } else{
//			    	  sql = "UPDATE dat_tienda set Nombre='"+etsP.getNom().replaceAll("'", "''")+"' where IdTienda="+etsP.getId();
//			      }
//			      rs.close();
//			      stmt.executeUpdate(sql);
			      
			      // Balance
//			      sql = "SELECT IdBalanza FROM dat_balanza where IdBalanza="+balanceCaisse.getId();
//			      rs = stmt.executeQuery(sql);
//			      int IdBalanza = -1;
//			      while(rs.next()){
//			    	  IdBalanza = rs.getInt("IdBalanza");
//			      }
//			      if(IdBalanza < 0){
//			    	  sql = "INSERT INTO dat_balanza (IdBalanza, IdTienda, IdEmpresa, Nombre) values ("+balanceCaisse.getId()+", "+IdTienda+", "+IdEmpresa+", '"+balanceCaisse.getReference().replaceAll("'", "''")+"')";
//			    	  IdBalanza = balanceCaisse.getId().intValue();
//			      } else{
//			    	  sql = "UPDATE dat_balanza set Nombre='"+balanceCaisse.getReference().replaceAll("'", "''")+"' where IdBalanza="+balanceCaisse.getId();
//			      }
//			      rs.close();
//			      stmt.executeUpdate(sql);
			      
			      // famille
			      int IdSeccion = -1;
			      
			      Long famId = null;
			      String lib = "";
			      if(!isRestau) {
			    	  famId = artP.getOpc_famille_stock().getId();
			    	  lib = artP.getOpc_famille_stock().getLibelle();
			      } else {
			    	  famId = artP.getOpc_famille_cuisine().getId();
			    	  lib = artP.getOpc_famille_cuisine().getLibelle();
			      }
			      
			      String sql = "SELECT IdSeccion FROM dat_seccion where IdSeccion="+famId;
			      rs = stmt.executeQuery(sql);
			      while(rs.next()){
			    	  IdSeccion = rs.getInt("IdSeccion");
			      }
			      if(IdSeccion < 0){
			    	  sql = "INSERT INTO dat_seccion(IdSeccion, NombreSeccion, IdEmpresa, Operacion, Usuario) values ("+famId+", '"+lib.replaceAll("'", "''")+"', "+IdEmpresa+", 'A', 'admindfs')";
			    	  IdSeccion = famId.intValue();
			      } else{
			    	  sql = "UPDATE dat_seccion set NombreSeccion='"+lib.replaceAll("'", "''")+"' where IdSeccion="+famId;
			      }
			      rs.close();
			      stmt.executeUpdate(sql);
			      
			      sql = "SELECT IdArticulo FROM dat_articulo where IdArticulo="+codeBarInt;
			      rs = stmt.executeQuery(sql);
			      int IdArticulo = -1;
			      while(rs.next()){
			    	  IdArticulo = rs.getInt("IdArticulo");
			      }
			      if(IdArticulo > 0){
			    	  
			    	  if(BooleanUtil.isTrue(artP.getIs_disable())){
			    		  sql = "DELETE from dat_articulo where IdArticulo="+codeBarInt;
			    	  } else{
			    		  sql = "UPDATE dat_articulo set " 
					    	  		+ " TeclaDirecta="+direcTkeyInt+","
						      		+ " Descripcion='"+artP.getLibelle().replaceAll("'", "''")+"', "
						      		+ " IdTipo="+unit+", "	// 1=weight, 2=Unit',
						      		+ " IdSeccion="+IdSeccion+", "
						      		+ " IdEmpresa="+IdEmpresa+", "
						      		+ " PrecioConIVA="+artP.getPrix_vente()
						      		+ " where IdArticulo="+codeBarInt;
			    	  }
			      } else{
			    	  sql = "INSERT INTO dat_articulo (IdArticulo, Descripcion, TeclaDirecta, IdTipo, PrecioConIVA, IdSeccion, IdEmpresa) "
				      			+ "values("+codeBarInt+", '"+artP.getLibelle().replaceAll("'", "''")+"', "+direcTkeyInt+", "+unit+", "+artP.getPrix_vente()+", "+IdSeccion+", "+IdEmpresa+")";
			      }
			      rs.close();
			      stmt.executeUpdate(sql);
		      }
		      stmt.close();
		      
		      em.flush();
		      
		      balanceCaisse.setMax_direct_bal(max);
		      em.merge(balanceCaisse);
		      
		} catch(Exception e){
			MessageService.addGrowlMessage("", "Un problème est survenu lors de la connexion à la base de données ["+e.getMessage()+"].");
			e.printStackTrace();
		} finally {
			try {
				if(conAdministrator != null){
					conAdministrator.close();
				} else{
					MessageService.addGrowlMessage("", "Un problème est survenu lors de la connexion à la base de données [Connexion non établie].");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	@Transactional
	public void mergeFastUpdate(List<ArticlePersistant> listArticle) {
		for (ArticlePersistant articleP : listArticle) {
			ArticleBean artBean = ServiceUtil.persistantToBean(ArticleBean.class, articleP);
//			artBean.setIs_stock();
			super.merge(artBean);
		}
	}
	
	@Override
	@Transactional
	public void updateRowsOrder(String[] orderArray){

		int orderIdx = 1;
		//
		for(String order : orderArray){
			String ctrlAct = EncryptionUtil.decrypt(order);

			ArticlePersistant ordrePersistant = findById(ArticlePersistant.class, NumericUtil.toLong(ctrlAct));
			ordrePersistant.setOrdre(orderIdx);
			getEntityManager().merge(ordrePersistant);
			orderIdx++;
		}
	}

	private Map<String, Integer> getIdxCol(Sheet sheet) {
		Map<String, Integer> mapColPos = new HashMap<>();
		//
		Cell[] cells = sheet.getRow(0);
		for(int i=0; i<cells.length; i++) {
			String sheetCol = sheet.getCell(i, 0).getContents().trim().toUpperCase();
			if(StringUtil.isEmpty(sheetCol)) {
				continue;
			}
			mapColPos.put(sheetCol, i);
		}
		return mapColPos;
	}
	
	@Override
	@Transactional
	public InventairePersistant importerComposants(String fileName, 
			boolean generateCB,
			boolean isDisComposant,
			Date dateInventaire,
			Long emplcementId) {
		InventairePersistant invP = null;
		String targetPath = StrimUtil._GET_PATH("article_import");
		File folder = new File(targetPath);
		
		List<InventaireDetailPersistant> listDetInv = new ArrayList<>();
		File inputWorkbook = null;
		// Purger les anciens fichiers s'ils existent
		try{
			for(File file : folder.listFiles()){
				if(file.isDirectory()) {
					for(File fileSub : file.listFiles()){
						if(!fileSub.getName().equals(fileName)){
							fileSub.delete();
						} else {
							inputWorkbook = fileSub;
						}
					}
				} else {
					if(!file.getName().equals(fileName)){
						file.delete();
					} else {
						inputWorkbook = file;
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if(folder.listFiles().length == 0){
			MessageService.addBannerMessage("Aucun fichier d'importation des données n'a été trouvé");
			throw new ActionValidationException("Bean Error");
		} else if(folder.listFiles().length > 1){
			MessageService.addBannerMessage("Il existe plus d'un fichier dans le répertoire d'importation");
			throw new ActionValidationException("Bean Error");
		} else if(!inputWorkbook.getName().toLowerCase().endsWith("xls")){
			MessageService.addBannerMessage("Ce format n'est pas supporté (uniquement .xls)");
			throw new ActionValidationException("Bean Error");
		}
		
		List<DataValuesPersistant> listDataValue = loadDataForm(null, "COMPOSANT");
		List<DataFormPersistant> listDataForm = getQuery("from DataFormPersistant where data_group=:groupe")
					.setParameter("groupe", "COMPOSANT")
					.getResultList();
		//------------------------------------------------------------------
		
		EntityManager em = getEntityManager();
		
		Date date_maj = new Date();
		Workbook w;
	    try {
	    	  WorkbookSettings ws = new WorkbookSettings();
	    	  ws.setEncoding("Cp1252");
			  w = Workbook.getWorkbook(inputWorkbook, ws);
			
		      // Get the first sheet
		      Sheet[] sheets = w.getSheets();

		      /*
		      ('KG', 'Kilogramme', 1),
		      ('G', 'Gramme', 1),
		      ('L', 'Litre', 1),
		      ('ML', 'Millilitre', 1),
		      ('B', 'Boite', 1),
		      ('P', 'Piéce', 1),*/
		      
	  			ValTypeEnumPersistant opc_unite_achat = (ValTypeEnumPersistant) getSingleResult(getQuery("from ValTypeEnumPersistant where code=:valEnum")
		  				.setParameter("valEnum", "P"));
	  			ValTypeEnumPersistant opc_unite_vente = (ValTypeEnumPersistant) getSingleResult(getQuery("from ValTypeEnumPersistant where code=:valEnum")
		  				.setParameter("valEnum", "P"));
	  			List<ValTypeEnumBean> listTva = valEnumService.getListValeursByType(ModelConstante.ENUM_TVA);
	  			
	  			Map<String, ValTypeEnumPersistant> mapTva = new HashMap<>();
	  			for (ValTypeEnumBean valTva : listTva) {
					mapTva.put(valTva.getCode(), valTva);
					mapTva.put(valTva.getLibelle(), valTva);
				}
	  			
	  			List<ValTypeEnumBean> listUnite = valEnumService.getListValeursByType(ModelConstante.ENUM_UNITE);
	  			Map<String, ValTypeEnumPersistant> mapUnite = new HashMap<>();
	  			for (ValTypeEnumBean valUnite : listUnite) {
	  				mapUnite.put(valUnite.getCode(), valUnite);
	  				mapUnite.put(valUnite.getLibelle(), valUnite);
				}
	  			
	    		 // Désacctiver tous les articles en premier
	    		 if(isDisComposant) {
	    			 getQuery("update ArticlePersistant set is_disable=1")
	    			 	.executeUpdate();
	    			 em.flush();
	    		 }
	    		 
	  			FamillePersistant rootFam = familleService.getFamilleRoot("ST");
	  			
	  			List<String> listCode = new ArrayList<>();
	  			for (Sheet sheet : sheets) {
	  				if(StringUtil.isEmpty(sheet.getName())){
	  					continue;
	  				}
	  				
	  				String sheetInfos = sheet.getName().trim().toUpperCase();
					if(sheet.getName().indexOf("|") != -1){
	  					listCode.add(StringUtil.getArrayFromStringDelim(sheetInfos, "|")[0]);	
	  				} else{
	  					listCode.add(sheetInfos);
	  				}
	  			}
	  			
	  			Map<String, FamilleStockPersistant> mapFamCode = new HashMap<>();
	  			Map<String, FamilleStockPersistant> mapFamLib = new HashMap<>();
	  			List<FamilleStockPersistant> listFamille = getQuery("from FamilleStockPersistant where upper(code) in (:fam) or upper(libelle) in (:fam)")
	  						.setParameter("fam", listCode)
	  						.getResultList();
	  			for (FamilleStockPersistant familleP : listFamille) {
	  				if(StringUtil.isNotEmpty(familleP.getCode())){
	  					String code = familleP.getCode().toUpperCase().trim();
	  					String normCode = Normalizer.normalize(code, Normalizer.Form.NFD);
	  					normCode = normCode.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	  					//
	  					mapFamCode.put(normCode, familleP);
	  				}
	  				mapFamLib.put(familleP.getLibelle().toUpperCase().trim(), familleP);
				}
	  			
	  			EtablissementPersistant etsB = ContextAppli.getEtablissementBean();
	  			
	  			Map<String, Integer> mapCols = getIdxCol(sheets[0]);
	  			
	  		  //
		      for (Sheet sheet : sheets) {
		    	int rows = sheet.getRows();
		    	
		    	if(rows == 0 || StringUtil.isEmpty(sheet.getName())){
		    		continue;
		    	}
		    	String sheetLib = null;
		    	String sheetCode = null;
	    		String sheetName = sheet.getName().toUpperCase().trim();
	    		
	    		 if(sheet.getName().indexOf("|") != -1) {
	    			 sheetCode = sheetName.substring(0, sheetName.indexOf("|"));
	    			 sheetLib = sheetName.substring(sheetName.indexOf("|")+1);
	    		 } else {
	    			 sheetCode = sheetName;
	    			 sheetLib = sheetName;
	    		 }
	    		 String sheetCodeShort = Normalizer.normalize((sheetCode.length() > 20 ? sheetCode.substring(0, 20) : sheetCode), Normalizer.Form.NFKD);
	    		 sheetCodeShort = sheetCodeShort.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    		
	    		 FamilleStockPersistant familleP = mapFamCode.get(sheetCodeShort);
				if(familleP == null) {
					familleP = mapFamCode.get(sheetLib);
				}
				if(familleP == null) {
					familleP = mapFamLib.get(sheetCodeShort);
				}
				if(familleP == null) {
					familleP = mapFamLib.get(sheetLib);
				}
				
	  			if(familleP == null){
	  				FamilleBean familleB = new FamilleBean();
	  				familleB.setCode(sheetCodeShort);
	  				familleB.setLibelle(sheetLib);
	  				familleB.setDate_creation(date_maj);
	  				familleB.setSignature("ADMIN");
	  				familleB.setParent_id(rootFam.getId());
	  				familleB.setElement_id(rootFam.getId());
	  				familleB.setType("ST");
	  				
	  				familleService.createFamille(familleB);
	  				em.flush();
	  				
	  				familleP = new FamilleStockPersistant();
	  				
	  				ReflectUtil.copyProperties(familleP, familleB); 
	  				
	  				mapFamCode.put(sheetCodeShort, familleP);
	  				mapFamLib.put(sheetLib, familleP);
	  			} else if(familleP.getId() != null && BooleanUtil.isTrue(familleP.getIs_disable())) {
	  				familleP.setIs_disable(null);
	  				familleP = em.merge(familleP);
	  				em.flush();
	  			}
		  			
	    		 System.out.println("==========>"+sheetLib);
	    		 /*  "CODE",
		  			"CODE_BARRE",
		  			"LIBELLE",
		  			"DESCRIPTION",
		  			"PRIX_ACHAT_TTC",
		  			"TVA_ACHAT",
		  			"UNITE_ACHAT",
		  			"PRIX_VENTE_TTC",
		  			"TVA_VENTE",
		  			"UNITE_VENTE",
		  			"FOURNISSEUR",
		  			"QUANTITE"	*/		    	
		    	
				int idxCode = mapCols.get("CODE");
				int idxLib = mapCols.get("LIBELLE");
				int idxBarre = mapCols.get("CODE_BARRE");
				int idxUniteAchat = mapCols.get("UNITE_ACHAT");
				int idxUniteVente = mapCols.get("UNITE_VENTE");
				int idxTvaAchat = mapCols.get("TVA_ACHAT");
				int idxTvaVente = mapCols.get("TVA_VENTE");
				int idxDesc = mapCols.get("DESCRIPTION");
				int idxQte = mapCols.get("QUANTITE");
				int idxPrixVenteTtc = mapCols.get("PRIX_VENTE_TTC");
				int idxPrixAchatTtc = mapCols.get("PRIX_ACHAT_TTC");
				int idxFourn = mapCols.get("FOURNISSEUR");
	    		 //
		  		for (int i = 1; i < rows; i++) {
		  			ArticlePersistant artP = null; 
		  			
					String code = sheet.getCell(idxCode, i).getContents();
					String codeBarre = sheet.getCell(idxBarre, i).getContents();
					String libArt = sheet.getCell(idxLib, i).getContents();
					String prixAchatTtc = sheet.getCell(idxPrixAchatTtc, i).getContents();
					String prixVenteTTc = sheet.getCell(idxPrixVenteTtc, i).getContents();
					String qteExcel = sheet.getCell(idxQte, i).getContents();
					String description = sheet.getCell(idxDesc, i).getContents();
					String tvaAchat = sheet.getCell(idxTvaAchat, i).getContents();
					String tvaVente = sheet.getCell(idxTvaVente, i).getContents();
					String uniteVente = sheet.getCell(idxUniteVente, i).getContents();
					String uniteAchat = sheet.getCell(idxUniteAchat, i).getContents();
					String codeFourn = sheet.getCell(idxFourn, i).getContents();
					
		  			if(StringUtil.isEmpty(libArt)){
		  				continue;
		  			}
		  			libArt = libArt.trim();
		  		
		  			System.out.println("- "+libArt);
		  			
		  			List<ArticlePersistant> listArt = new ArrayList<>();
		  			
		  			if(StringUtil.isNotEmpty(code)){
		  				code = code.trim();
		  				listArt = getQuery("from ArticlePersistant where upper(code)=:code")
		  						.setParameter("code", code.toUpperCase())
		  						.getResultList();
		  			} else if(StringUtil.isNotEmpty(codeBarre)){
		  				codeBarre = codeBarre.trim();
		  				listArt = getQuery("from ArticlePersistant where upper(code_barre)=:code")
		  						.setParameter("code", codeBarre.toUpperCase())
		  						.getResultList();
		  			} else{
		  				listArt = getQuery("from ArticlePersistant where upper(libelle)=:libelle")
		  						.setParameter("libelle", libArt.toUpperCase())
		  						.getResultList();
		  			}

		  			if(StringUtil.isEmpty(codeBarre) && generateCB){
			  			codeBarre = generateCodeBarre(familleP.getId());
			  		}
		  			
		  			if(listArt.size() == 0){
		  				artP =  new ArticlePersistant();
		  				if(StringUtil.isEmpty(code)){
		  					code = generateCode(familleP.getId(), "ST");
		  				}
		  			} else{
		  				artP = listArt.get(0);
		  			}
		  			
		  			if(StringUtil.isNotEmpty(codeFourn)){
		  				codeFourn = codeFourn.trim();
		  				String[] fournArray = StringUtil.getArrayFromStringDelim(codeFourn, "|");
		  				String codes = "";
		  				for(String codeF : fournArray) {
			  				List<FournisseurPersistant> listFourn = getQuery("from FournisseurPersistant where code=:code")
			  						.setParameter("code", codeF)
			  						.getResultList();
			  				if(listFourn.size() > 0) {
			  					codes += listFourn.get(0).getId()+"|";
			  				}
		  				}
		  				artP.setFournisseurs(codes);
		  			}
		  			
		  			if(StringUtil.isEmpty(code)) {
		  				if(StringUtil.isNotEmpty(codeBarre)) {
		  					code = codeBarre;
		  				} else {
		  					code = libArt;
		  				}
		  			}
		  			
		  			artP.setCode(code);
					artP.setCode_barre(codeBarre);
					artP.setLibelle(libArt);
					artP.setDescription(description);
					artP.setIs_stock(true);
					
					// TTC ---------------------------------------------------
					BigDecimal prix_achat_ttc = BigDecimalUtil.get(prixAchatTtc);
					BigDecimal prix_vente_ttc = BigDecimalUtil.get(prixVenteTTc);
					artP.setPrix_vente(prix_vente_ttc);
					artP.setPrix_achat_ttc(prix_achat_ttc);

					// TVA
					if(StringUtil.isNotEmpty(tvaAchat)) {
						artP.setOpc_tva_enum(mapTva.get(tvaAchat));
						BigDecimal prix_achat_ht = BigDecimalUtil.substract(prix_achat_ttc, BigDecimalUtil.multiply(prix_achat_ttc, BigDecimalUtil.divide(BigDecimalUtil.get(tvaAchat), BigDecimalUtil.get(100))));
						artP.setPrix_achat_ht(prix_achat_ht);
						artP.setMtt_tva(BigDecimalUtil.substract(prix_achat_ttc, prix_achat_ht));
					}
					if(StringUtil.isNotEmpty(tvaVente)) {
						artP.setOpc_tva_enum_vente(mapTva.get(tvaVente));
						BigDecimal prix_vente_ht = BigDecimalUtil.substract(prix_vente_ttc, BigDecimalUtil.multiply(prix_vente_ttc, BigDecimalUtil.divide(BigDecimalUtil.get(tvaVente), BigDecimalUtil.get(100))));
						artP.setPrix_vente_ht(prix_vente_ht);
						//artP.setMtt_tva_vente(BigDecimalUtil.substract(prix_vente_ttc, prix_vente_ht));
					}
					// Unité
					if(StringUtil.isNotEmpty(uniteAchat)) {
						artP.setOpc_unite_achat_enum(mapUnite.get(uniteAchat));
					} else {
						artP.setOpc_unite_achat_enum(opc_unite_achat);
					}
					if(StringUtil.isNotEmpty(uniteVente)) {
						artP.setOpc_unite_vente_enum(mapUnite.get(uniteVente));
					} else {
						artP.setOpc_unite_vente_enum(opc_unite_vente);
					}
					
					if(artP.getId() == null){
						artP.setPrix_ref(prix_achat_ttc);
						artP.setPrix_achat_moyen_ht(prix_achat_ttc);
						artP.setPrix_achat_moyen_ttc(prix_achat_ttc);
			  			artP.setDate_creation(date_maj);
					}// else {
						/*if(artP.getOpc_tva_enum() != null) {
							BigDecimal tauxTva = BigDecimalUtil.get(artP.getOpc_tva_enum().getCode());
							BigDecimal mttHt = getMttHt(prix_achat_ttc, tauxTva);
							
							artP.setMtt_tva(BigDecimalUtil.substract(prix_achat_ttc, mttHt));
							artP.setPrix_achat_ht(mttHt);
						} else {
							artP.setMtt_tva(BigDecimalUtil.get(0));
							artP.setPrix_achat_ht(prix_achat_ttc);							
						}*/
					//}
					
					// ---------------------------------------------------------
		  			artP.setOpc_etablissement(etsB);
		  			artP.setOpc_famille_stock(familleP);
		  			artP.setIs_disable(null);
		  			
		  			artP.setDate_maj(date_maj);
		  			artP.setSignature("IMPORT");

		  			artP = em.merge(artP);
			  		em.flush();

		  			// Ajouter données form -------------------------------------------
		  			if(listDataValue != null) {
		  				for (DataValuesPersistant dataV : listDataValue) {
		  					for(String col : mapCols.keySet()) {
								if(dataV.getOpc_data_form().getData_label().toUpperCase().equals(col)){
									
									getQuery("delete from DataValuesPersistant where data_code=:code "
											+ "and element_id=:id "
											+ "and data_group=:group")
										.setParameter("code", col)
										.setParameter("id", artP.getId())
										.setParameter("group", "COMPOSANT")
										.executeUpdate();
									
									String value = sheet.getCell(mapCols.get(col), i).getContents();
									if(StringUtil.isNotEmpty(value)) {
										DataValuesPersistant dataValP = new DataValuesPersistant();
										dataValP.setData_group("COMPOSANT");
										dataValP.setElement_id(artP.getId());
										
										DataFormPersistant dataForm = (listDataForm.size()>0 ? listDataForm.get(0) : null);
										for(DataFormPersistant form : listDataForm) {
											if(form.getData_code().toUpperCase().equals(col.toUpperCase())
													|| form.getData_label().toUpperCase().equals(col.toUpperCase())) {
												dataForm = form;
												break;
											}
										}
										dataValP.setOpc_data_form(dataForm);
										dataValP.setData_value(value);
										dataValP.setData_code(col);
										//
										em.merge(dataValP);
									}
									
									break;
								}
							}
			  			}
		  			}
		  			// ----------------------------------------------------------------
		  			
			  		
			  		//
					if(dateInventaire != null && StringUtil.isNotEmpty(qteExcel)){
				  		BigDecimal qteEntree = BigDecimalUtil.get(qteExcel);
				  		
				  		ArticleStockInfoPersistant artInfoP = (ArticleStockInfoPersistant) getSingleResult(getQuery("from ArticleStockInfoPersistant "
				  				+ "where opc_article.id=:idArt and opc_emplacement.id=:emplId")
				  				.setParameter("idArt", artP.getId())
				  				.setParameter("emplId", emplcementId)
				  				);
				  		BigDecimal qteTheorique = (artInfoP != null) ? artInfoP.getQte_reel() : qteEntree;
				  		
				  		InventaireDetailPersistant invDet = new InventaireDetailPersistant();
				  		//invDet.setQte_reel_0(qteEntree);
				  		invDet.setQte_theorique(qteTheorique);
				  		invDet.setQte_reel(qteEntree);
				  		invDet.setQte_ecart(BigDecimalUtil.substract(qteTheorique, qteEntree));
				  		invDet.setOpc_article(artP);
				  		
				  		listDetInv.add(invDet);
			  		}
		  		}
		    }
		      
		      
		      System.out.println("FIN IMPORT");
		      
		     //
		     if(dateInventaire != null && listDetInv.size() > 0){
		    	invP = new InventairePersistant();
			    invP.setDate_realisation(dateInventaire);
				invP.setOpc_emplacement(findById(EmplacementPersistant.class, emplcementId));
				invP.setOpc_responsable(ContextAppli.getUserBean().getOpc_employe());
				invP.setOpc_saisisseur(ContextAppli.getUserBean().getOpc_employe());
				invP.setOpc_type_enum( valEnumService.getValeurByCode(ModelConstante.ENUM_TYPE_INVENTAIRE.toString(), "JOURNALIER"));
				//
				//invP.setList_detail(listDetInv);	
				
				invP.setSignature(ContextAppli.getUserBean().getLogin());
				invP.setDate_creation(date_maj);
				invP.setDate_maj(date_maj);
				
				invP = em.merge(invP);
				em.flush();
				
				for(InventaireDetailPersistant detP : listDetInv) {
					detP.setOpc_inventaire(invP);
					em.merge(detP);
				}
				em.flush();
		     }
		     
		     System.out.println("FIN INVENTAIRE");
		     
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	if(e instanceof ActionValidationException){
	    		throw new ActionValidationException(e);
	    	} else{
	    		throw new RuntimeException(e);
	    	}
	    }
	    
	    return invP;
	}
	
	private static BigDecimal getMttHt(BigDecimal mttTtc, BigDecimal tauxTva) {
		String taux = ""+BigDecimalUtil.divide(tauxTva, BigDecimalUtil.get("10"));
		
		taux = taux.replace('.', ' ').replace(',', ' ');
		taux = "1,"+taux;
		tauxTva = BigDecimalUtil.get(taux);
		
		BigDecimal mttHt = BigDecimalUtil.divide(mttTtc, tauxTva);
		
		return mttHt;
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(Long compId) {
		getQuery("delete from ArticleStockInfoPersistant where opc_article.id=:artId")
			.setParameter("artId", compId)
			.executeUpdate();
		
		getQuery("delete from EmplacementSeuilPersistant where opc_composant.id=:artId")
		.setParameter("artId", compId)
		.executeUpdate();
		
		getEntityManager().flush();
		
		super.delete(compId);
	}

	@Override
	public File exporteComposants(String[] familles) {
		String path = StrimUtil._GET_PATH("importArt");
		if(!new File(path).exists()){
			new File(path).mkdirs();
		}
		
		File file = new File(path+"/export_article_"+DateUtil.dateToString(new Date(), "dd_MM_yyyy_HH_mm_ss")+".xls");
		List<DataValuesPersistant> listDataValueCols = loadDataForm(null, "COMPOSANT");
		
		try {
			file.createNewFile();
			WritableWorkbook book = Workbook.createWorkbook(file);

			List<FamillePersistant> listAllFam = new ArrayList<>();
			for(String famIdStr : familles){
				if(StringUtil.isEmpty(famIdStr)){
					continue;
				}
				
				Long familleId = Long.valueOf(famIdStr);
				FamillePersistant familleP = findById(FamillePersistant.class, familleId);
				
				List<FamillePersistant> listFam = familleService.getFamilleEnfants(FAMILLE_TYPE_ENUM.ST.toString(), familleId, true);
				listAllFam.add(familleP);
				
				listAllFam.addAll(listFam);
			}
			
			WritableCellFormat cellFormatR = new WritableCellFormat();
			cellFormatR.setAlignment(Alignment.RIGHT);
			
			int sheetIdx = 0;
			for(FamillePersistant familleP : listAllFam){
				WritableSheet sheet = book.createSheet(familleP.getCode()+"|"+familleP.getLibelle(), sheetIdx);
				sheet.setColumnView(0, 10);
				sheet.setColumnView(1, 10);
				sheet.setColumnView(2, 20);
				sheet.setColumnView(3, 10);
				sheet.setColumnView(4, 10);
				sheet.setColumnView(5, 5);
				sheet.setColumnView(6, 7);
				sheet.setColumnView(7, 7);
				sheet.setColumnView(8, 7);
				sheet.setColumnView(9, 7);
				sheet.setColumnView(10, 7);
				sheet.setColumnView(11, 7);
				int idx = 1;
				for (DataValuesPersistant dataV : listDataValueCols) {
					sheet.setColumnView(11+idx, 7);
					idx++;
				}
				
				//CODE	CODE_BARRE	LIBELLE	DESCRIPTION	PRIX_ACHAT_TTC	TVA_ACHAT	UNITE_ACHAT	PRIX_VENTE_TTC	TVA_VENTE	UNITE_VENTE	QUANTITE
				sheet.addCell(new Label(0, 0, "CODE"));
				sheet.addCell(new Label(1, 0, "CODE_BARRE"));
				sheet.addCell(new Label(2, 0, "LIBELLE"));
				sheet.addCell(new Label(3, 0, "DESCRIPTION"));
				sheet.addCell(new Label(4, 0, "PRIX_ACHAT_TTC"));
				sheet.addCell(new Label(5, 0, "TVA_ACHAT"));
				sheet.addCell(new Label(6, 0, "UNITE_ACHAT"));
				sheet.addCell(new Label(7, 0, "PRIX_VENTE_TTC"));
				sheet.addCell(new Label(8, 0, "TVA_VENTE"));
				sheet.addCell(new Label(9, 0, "UNITE_VENTE"));
				sheet.addCell(new Label(10, 0, "FOURNISSEUR"));
				sheet.addCell(new Label(11, 0, "QUANTITE"));
				//
				idx = 1;
				for (DataValuesPersistant dataV : listDataValueCols) {
					sheet.addCell(new Label(11+idx, 0, dataV.getOpc_data_form().getData_label().toUpperCase()));
					idx++;
				}
				
				int rowIdx = 1;
				List<ArticlePersistant> arts = getListComposantsActifs(familleP.getId());
				for(ArticlePersistant artP : arts){
					sheet.addCell(new Label(0, rowIdx, artP.getCode()));
					sheet.addCell(new Label(1, rowIdx, artP.getCode_barre()));
					sheet.addCell(new Label(2, rowIdx, artP.getLibelle()));
					sheet.addCell(new Label(3, rowIdx, artP.getDescription()));
					
					sheet.addCell(new Label(4, rowIdx, BigDecimalUtil.formatNumber(artP.getPrix_achat_ttc()), cellFormatR));
					sheet.addCell(new Label(5, rowIdx, (artP.getOpc_tva_enum()!=null?artP.getOpc_tva_enum().getCode():"")));
					sheet.addCell(new Label(6, rowIdx, (artP.getOpc_unite_achat_enum()!=null?artP.getOpc_unite_achat_enum().getCode():"")));
					
					sheet.addCell(new Label(7, rowIdx, BigDecimalUtil.formatNumber(artP.getPrix_vente()), cellFormatR));
					sheet.addCell(new Label(8, rowIdx, (artP.getOpc_tva_enum_vente()!=null?artP.getOpc_tva_enum_vente().getCode():"")));
					sheet.addCell(new Label(9, rowIdx, (artP.getOpc_unite_vente_enum()!=null?artP.getOpc_unite_vente_enum().getCode():"")));
					
					String[] fournArray = StringUtil.getArrayFromStringDelim(artP.getFournisseurs(), "|");
					String fourn = "";
					if(fournArray != null && fournArray.length > 0) {
						FournisseurPersistant fournP = findById(FournisseurPersistant.class, Long.valueOf(fournArray[0]));
						fourn += fournP.getCode()+"|";
					}
					sheet.addCell(new Label(10, rowIdx, fourn));
					
					sheet.addCell(new Label(11, rowIdx, ""));
					
					// Ajouter data form ---------------------------------------------------------------
					//List<DataValuesPersistant> listDataVal = loadDataForm(artP.getId(), "COMPOSANT");
					
					List<DataValuesPersistant> listDataVal = getQuery("from DataValuesPersistant "
							+ "where element_id=:elmntId and data_group=:groupe ")
							.setParameter("elmntId", artP.getId())
							.setParameter("groupe", "COMPOSANT")
							.getResultList();
					
					if(listDataVal.size() > 0) {
						idx = 1;
						for (DataValuesPersistant dataV : listDataValueCols) {
							for (DataValuesPersistant data : listDataVal) {
								if(dataV.getOpc_data_form().getId() == data.getOpc_data_form().getId()){
									sheet.addCell(new Label(11+idx, rowIdx, data.getData_value()));
									break;
								}
							}
							idx++;
						}
					} else {
						idx = 1;
						for (DataValuesPersistant dataV : listDataValueCols) {
							sheet.addCell(new Label(11+idx, rowIdx, ""));
							idx++;
						}
					}
					//-----------------------------------------------------------------
					
					rowIdx++;
				}
				sheetIdx++;
			}
			book.write();
			book.close();  
		} catch (Exception e) {   
            e.printStackTrace();   
        }   			
		return file;
	}
	
	@Override
	public List<DataValuesPersistant> getListDataValues(String groupe, Long elementId){
		List<DataValuesPersistant> listDataVal = getQuery("from DataValuesPersistant "
				+ "where data_value is not null "
				+ "and data_value != '' "
				+ "and data_group=:groupe "
				+ "and element_id=:elementId")
				.setParameter("groupe", "COMPOSANT")
				.setParameter("elementId", elementId)
				.getResultList();
		
		return listDataVal;
	}
	
	@Override
	@Transactional
	public int fusionnerArticleDoublon(){
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		 
		List<Object[]> listDoublon = getNativeQuery("SELECT COUNT(*) AS nbr_doublon, id, libelle "
				+ "FROM article "+(isRestau ? "where (is_stock is not null and is_stock=1)":"")+" GROUP BY libelle HAVING COUNT(*) > 1")
			.getResultList();
		
		if(listDoublon == null || listDoublon.size() == 0){
			return 0;
		}
		for (Object[] objects : listDoublon) {
			List<ArticlePersistant> listDoublonLib = getQuery("from ArticlePersistant where libelle=:libelle "
					+(isRestau ? "and (is_stock is not null and is_stock=1)":""))
				.setParameter("libelle", objects[2])
				.getResultList();
			
			Long masterId = listDoublonLib.get(0).getId(); 
			for(ArticlePersistant det : listDoublonLib){
				if(StringUtil.isNotEmpty(det.getCode_barre())){
					masterId = det.getId();
				}
			}
		
			String others = "";
			for(ArticlePersistant det : listDoublonLib){
				if(det.getId() != masterId){
					others += det.getId() + ",";
				}
			}
			others = others.substring(0, others.length()-1);
		
			getNativeQuery("update inventaire_detail set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update caisse_mouvement_article set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update article_stock_info set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update mouvement_article set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update article_client_prix set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update demande_transfert_article set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update preparation_article set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update preparation_article set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update article_detail set article_id="+masterId+" where article_id in ("+others+")").executeUpdate();
			getNativeQuery("update article_detail set article_composant_id="+masterId+" where article_composant_id in ("+others+")").executeUpdate();
			getEntityManager().flush();
			getNativeQuery("delete from article where id in ("+others+")").executeUpdate();
		}
		
		return listDoublon.size();
	}

	@Override
	@Transactional
	public void changerFamille(Long familleId, Long[] artIds) {
		EntityManager em = getEntityManager();
		FamilleStockPersistant famP = findById(FamilleStockPersistant.class, familleId);
		for(Long artId : artIds){
			ArticlePersistant artP = findById(ArticlePersistant.class, artId);
			artP.setOpc_famille_stock(famP);
			//
			em.merge(artP);
		}
	}

	@Override
	public List<ArticlePersistant> getListPharmaPrinceps() {
		return getQuery("from ArticlePersistant art "
				+ "where art.type_art='P' "
				+ "order by art.opc_marque.libelle, art.libelle")
				.getResultList();
	}

	@Override
	@Transactional
	public void purgerDoublonStockInfo() {
		getNativeQuery("delete t1 FROM article_stock_info t1 "+
					"INNER JOIN article_stock_info t2 " +
					"WHERE t1.id < t2.id AND " +
					"t1.emplacement_id = t2.emplacement_id AND " +
					"t1.article_id = t2.article_id")
			.executeUpdate();	
	}

	@Override
	public Map<String, BigDecimal[]> controleMarge() {
		List<ArticlePersistant> listArticle = findAll(ArticlePersistant.class, Order.asc("libelle"));
		Map<String, BigDecimal[]> mapData = new LinkedHashMap<>();
		
		for (ArticlePersistant artP : listArticle) {
			if(BigDecimalUtil.isZero(artP.getPrix_vente())){
				continue;
			}
			BigDecimal prixAchat = (BigDecimalUtil.isZero(artP.getPrix_achat_ttc()) ? artP.getPrix_achat_ht() : artP.getPrix_achat_ttc());
			BigDecimal margeCalc = BigDecimalUtil.substract(artP.getPrix_vente(), prixAchat);
			BigDecimal margePourcent = BigDecimalUtil.divide(
										BigDecimalUtil.multiply(margeCalc, BigDecimalUtil.get(100)), artP.getPrix_vente());
		
			if(margePourcent.compareTo(BigDecimalUtil.get(10)) < 0) {
				mapData.put(artP.getCode()+"-"+artP.getLibelle(), new BigDecimal[]{artP.getPrix_vente(), prixAchat, margeCalc, margePourcent});
			}
		}
		
		return mapData;
	}

	@Override
	@Transactional
	public void fusionnerArticle(Long source, Long dest) {
		String[] entites = {
				MouvementArticlePersistant.class.getSimpleName(),
				CaisseMouvementArticlePersistant.class.getSimpleName(),
				NotificationPersistant.class.getSimpleName(),
				ArticleBalancePersistant.class.getSimpleName(),
				//ArticleBalisagePersistant.class.getSimpleName(),
				ArticleClientPrixPersistant.class.getSimpleName(),
				DemandeTransfertArticlePersistant.class.getSimpleName(),
				LotArticlePersistant.class.getSimpleName(),
				PreparationArticlePersistant.class.getSimpleName(),
				ListChoixDetailPersistant.class.getSimpleName(),
				InventaireDetailPersistant.class.getSimpleName(),
			};
		
		for(String ent : entites) {
			getQuery("update "+ent+" det set det.opc_article.id=:dest "
					+ " where det.opc_article.id=:src")
					.setParameter("dest", dest)
					.setParameter("src", source)
					.executeUpdate();
		}
		getQuery("update ArticleDetailPersistant det set det.opc_article_composant.id=:dest "
				+ "where det.opc_article_composant.id=:src ")
				.setParameter("dest", dest)
				.setParameter("src", source)
				.executeUpdate();
	}
}

class DelegatingDriver implements Driver
{
	private Driver driver;
	DelegatingDriver(Driver d) {
		this.driver = d;
	}
	public boolean acceptsURL(String u) throws SQLException {
		return this.driver.acceptsURL(u);
	}
	public Connection connect(String u, Properties p) throws SQLException {
		return this.driver.connect(u, p);
	}
	public int getMajorVersion() {
		return this.driver.getMajorVersion();
	}
	public int getMinorVersion() {
		return this.driver.getMinorVersion();
	}
	public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
		return this.driver.getPropertyInfo(u, p);
	}
	public boolean jdbcCompliant() {
		return this.driver.jdbcCompliant();
	}
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
