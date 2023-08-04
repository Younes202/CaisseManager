package appli.model.domaine.dashboard.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.habilitation.bean.ProfileBean;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import framework.model.service.IGenericJpaService;

public interface IDashBoardService extends IGenericJpaService<ProfileBean, Long> {
	public List<Object[]> getVentesMois();
	List<Object[]> getMttResultatNetParMois();
	public List<EcriturePersistant> getBanqueEvolution(Long compteId);
	public List<Object[]> getEcartEmploye(Date dtDebut, Date dtFin);
	public List<Object[]> getLivraisonParutilisateur(Date dtDebut, Date dtFin);
	Map<String, Object> getDataInfos(Date dateFin);
	public Map<String, List<BigDecimal>> getDataChiffresEmploye(Date dtDebut, Date dtFin);
	List<Object[]> getCongeNonPayeByDate(Date dateDebut, Date dateFin);
	List<Object[]> getPointageRH(Date dateDebut, Date dateFin);
	public Map<String, Integer> getMapNbrAlerte();
	List<Object[]> getTempsTravailEmploye(Date dtDebut, Date dtFin, Long employe, Long poste);
	public List<Object[]> getRepVenteCaisseParCaissier(Date dtDebut, Date dtFin, Long user);
	List<Object[]> getChiffreVenteBOParCaissier(Date dtDebut, Date dtFin);
	List<Object[]> getChiffreVenteCaisseParCaissier(Date dtDebut, Date dtFin);
	public List<Object[]> getRepartitionVentesParArticle(Date dtDebut, Date dtFin, Long familleId);
	public List<Object[]> getRepartitionVenteBOParPaiement(Date dtDebut, Date dtFin);
	int getCurrentMonth();
	public Map<String, List<String>> getRepartiotionPaiementParModePaiement(Long banqueId);
	public List<Object[]> getRepartitionFraisParType(Date dtDebut, Date dtFin, Long typeFrais, Long employe);
	public List<Object[]> getRepartitionFraisParEmploye(Date dtDebut, Date dtFin, Long typeFrais, Long employe);
 }
