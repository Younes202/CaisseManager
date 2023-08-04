package appli.model.domaine.caisse.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.habilitation.bean.ProfileBean;
import appli.model.domaine.caisse.persistant.JourneeVenteErpView;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import framework.model.service.IGenericJpaService;

public interface IDashBoard2Service extends IGenericJpaService<ProfileBean, Long> {

	JourneePersistant getLastJourneCaisse();

	JourneePersistant getJourneeDetail(Long journeeId);

	List<Object[]> getMttResultatNetParMois();

	Date getLastDateVente();

	List<Object[]> getVentesMois(); 

	List<JourneePersistant> getVentesJours();

	Map getRepartitionVenteArticle(Long journeeId, Date dateDebut, Date dateFin, Long familleIncludeId);

	JourneeVenteErpView getJourneeDetail(Date lastDate);

	Date[] getMinMaxDate();

	Map<ArticlePersistant, BigDecimal[]> calculMargeArticles();

	List<Object[]> getVentesJoursErp();

	List<Object[]> getVentesMoisErp();

	List<Object[]> getEvolutionRecettes();

	List<Object[]> getEvolutionVentes();

	List<Object[]> getEvolutionAchats();

	List<Object[]> getEvolutionDepences();

	List<Object[]> getRepartitionRecettesPie(Date dtDebut, Date dtFin);

	List<Object[]> getRepartitionDepencesPie(Date dtDebut, Date dtFin);

	List<Object[]> getRepartitionAchatArticlePie(Date dtDebut, Date dtFin, Long familleIncludeId);
 }
