package appli.model.domaine.compta.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.compta.bean.CompteBean;
import appli.controller.domaine.compta.bean.EcritureBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.EcriturePersistant;
import appli.model.domaine.compta.dao.ICompteDao;
import appli.model.domaine.compta.service.ICompteService;
import appli.model.domaine.compta.service.IEcritureService;
import appli.model.domaine.compta.validator.CompteValidator;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.CompteInfosPersistant;
import framework.model.beanContext.ComptePersistant;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

@WorkModelClassValidator(validator=CompteValidator.class)
@Named
public class CompteService extends GenericJpaService<CompteBean, Long> implements ICompteService{
	@Inject
	private ICompteDao compteDao;
	@Inject
	private IEcritureService ecritureService;
	
	@Override
	public ComptePersistant getCompteRoot() {
		return (ComptePersistant) getQuery("from ComptePersistant where code=:code")
			.setParameter("code", "X")
			.getSingleResult();
	}
	
	/* (non-Javadoc)
	 * @see monprojet.compte.model.service.ICompteService#create(monprojet.compte.ihm.CompteBean)
	 */
	@WorkModelMethodValidator 
	@Transactional
	@Override
	public void create(CompteBean compte){
		compte.setId(null);
		compte.setIs_ajoute(true);
		compte.setIs_modifiable(true);
		//
		compteDao.createCompte(compte); 
	}

	/* (non-Javadoc)
	 * @see monprojet.compte.model.service.ICompteService#update(monprojet.compte.ihm.CompteBean)
	 */
	@Transactional
	@Override
	@WorkModelMethodValidator
	public CompteBean update(CompteBean compte){
		ExercicePersistant currentExercice = ContextGloabalAppli.getExerciceBean();
		Query query = compteDao.getQuery("from CompteInfosPersistant where opc_compte.id=:compteId and opc_exercice.id=:exerciceId")
			.setParameter("compteId", compte.getId())
			.setParameter("exerciceId", currentExercice.getId());
		//
		CompteInfosPersistant compteInfosPersistant = (CompteInfosPersistant) compteDao.getSingleResult(query);
		// On ne met à jour que la clé et le commentaire
		ComptePersistant comptePersistant = compteDao.findById(compte.getId());
				
		// Si ce n'est pas le même exercice
		if(compteInfosPersistant == null){
			compteInfosPersistant = new CompteInfosPersistant();
			compteInfosPersistant.setOpc_exercice(ContextGloabalAppli.getExerciceBean());
			compteInfosPersistant.setOpc_compte(comptePersistant);
		}
		
		// Compte infos
		if(compte.getTaux_recuperation() != null){
			compteInfosPersistant.setTaux_recuperation(compte.getTaux_recuperation());
		}
		compteDao.getEntityManager().merge(compteInfosPersistant);
		// Compte
		comptePersistant.setLibelle(compte.getLibelle());
		comptePersistant.setCommentaire(compte.getCommentaire());
		//
		compteDao.update(comptePersistant);
		
		return compte;
	}

	/* (non-Javadoc)
	 * @see monprojet.compte.model.service.ICompteService#delete(java.lang.Long)
	 */
	@Override
	@WorkModelMethodValidator
	@Transactional
	public void delete(Long id){
		compteDao.deleteCompte(id);
	}
	
	public List<CompteBean> getListCompteByCode(String code) {
		return persistantToBean(compteDao.getListCompteByCode(code));		
	}
	public CompteBean getCompteByCode(String code) {
		return (CompteBean) persistantToBean(CompteBean.class, compteDao.getCompteByCode(code));		
	}
	
	/* (non-Javadoc)
	 * @see org.metier.domaine.par.service.ICompteService#calculateNewCodeCompte(java.lang.String, boolean)
	 */
	@Override
	public String calculateNewCodeCompte(ComptePersistant parentCompte, int taille){
		String codeMaxCompte = compteDao.getMaxCodeCompte(parentCompte, taille);
		
		// Pas d'enfants
		if(codeMaxCompte.equals(parentCompte.getCode())){
			if(codeMaxCompte.length() < taille){
				codeMaxCompte = parentCompte.getCode();
				while (codeMaxCompte.length() < taille -1) {
					codeMaxCompte = codeMaxCompte + "0";
				}
				codeMaxCompte = codeMaxCompte + "1";
			} 
		} // Avec enfants
		else {
			codeMaxCompte = ""+(NumericUtil.toInteger(codeMaxCompte)+1);
		}
		
		return codeMaxCompte;
	}

	/* (non-Javadoc)
	 * @see org.metier.domaine.par.service.ICompteService#updateSolde(java.lang.Long, java.math.BigDecimal, java.lang.String)
	 */
	@Override
	@Transactional
	public void updateSoldeInitialisation(Long compteId, BigDecimal montant, String sens) {
		List<EcriturePersistant> ecriturePersistant = ecritureService.getListEcritureByOrigineAndType(compteId, ContextAppli.TYPE_ECRITURE.INITEXE);
		ComptePersistant comptePersistant = compteDao.findById(compteId);
		ExercicePersistant currentExercice = ContextGloabalAppli.getExerciceBean();
		
		// Suppression s'elle existe déja
		if(ecriturePersistant.size() > 0){
			ecritureService.delete(ecriturePersistant.get(0).getId());
		}
		
		// Création
		if(montant.compareTo(BigDecimalUtil.ZERO) != 0){
			Integer nextGroupe = ecritureService.getNextGroupParOrigine(ContextAppli.TYPE_ECRITURE.INITEXE, compteId);
			
			
			EcritureBean epC = new EcritureBean();
			epC.setSource(ContextAppli.TYPE_ECRITURE.INITEXE.toString());
			epC.setDate_mouvement(currentExercice.getDate_debut());
			epC.setLibelle("Ecriture d'initialisation de l'exercice : "+currentExercice.getLibelle());
			epC.setMontant(montant);
			epC.setOpc_compte(comptePersistant);
			epC.setGroupe(nextGroupe);
			epC.setSens(sens);
			epC.setElementId(compteId);
			
			ecritureService.create(epC);
		}
		
		//Maj compte
		comptePersistant.setMontant_init(montant);
		comptePersistant.setSens(sens);
		compteDao.update(comptePersistant);
	}

	@Override
	public List<CompteBean> getListCompteChargeRecuperable(boolean isRecuperable) {
		return persistantToBean(compteDao.getListCompteChargeRecuperable(isRecuperable));
	}

	@Override
	public List<ComptePersistant> getPlanComptable(boolean ignoreRoot) {
		List<ComptePersistant> listCompte = compteDao.getPlanComptabe(ignoreRoot);
		if(listCompte.size() == 0) {
			// Plan comptable
			importCompteExcelTemp();
		}
		
		return listCompte;
	}

	@Override
	public void createCompte(CompteBean compteBean) {
		compteDao.createCompte(compteBean);
	}

	@Override
	public void deleteCompte(Long id) {
		compteDao.deleteCompte(id);
	}

	@Override
	public void updateCompte(CompteBean compteBean) {
		compteDao.updateCompte(compteBean);
	}
	
	
	
	@Transactional
	private void importCompteExcelTemp(){
		File inputWorkbook = new File(this.getClass().getResource("/").getPath()+ "appli/conf/plan-comptable-marocain-excel.xls");
		EntityManager em = getEntityManager();
		
		try{
		 WorkbookSettings ws = new WorkbookSettings();
		 ws.setEncoding("Cp1252");
		  Workbook w = Workbook.getWorkbook(inputWorkbook, ws);
		
	      // Get the first sheet
	      Sheet[] sheets = w.getSheets();
	      // Loop over first 10 column and lines
	      Sheet sheet = sheets[0];
		
	      	List<ComptePersistant> listCompte = new ArrayList<>();
	      	
		   	int rows = sheet.getRows();
			for (int i = 2; i < rows; i++) {
				String code = ""+sheet.getCell(0, i).getContents();
				String libelle = ""+sheet.getCell(1, i).getContents();
				String classe = ""+sheet.getCell(2, i).getContents();
				String rubrique = ""+sheet.getCell(3, i).getContents();
				
				ComptePersistant compteP = new ComptePersistant();
				compteP.setCode(code);
				compteP.setLibelle(libelle);
				compteP.setClasse(classe);
				compteP.setRubrique(rubrique);
				
				int level = code.trim().length();
				if(code.indexOf("-") != -1){// Compte avec - comme 24 - 25
					String[] cdArr = StringUtil.getArrayFromStringDelim(code, "-");
					level = cdArr[0].trim().length();
				}
				compteP.setLevel(level);
				
				listCompte.add(compteP);
			}
			
			int oldLevel = 0;
			String oldCode = null;
			int b_left = 0;
			//
			int cpt = 0;
			for (ComptePersistant comptePersistant : listCompte) {
				
				if(StringUtil.isEmpty(comptePersistant.getCode())){
					cpt++;
					continue;
				}
				
				int currLevel = comptePersistant.getLevel();
				int nextLevel = (cpt <= listCompte.size()-2 ? listCompte.get(cpt+1).getLevel() : currLevel);
				
				if(currLevel > oldLevel){
					b_left++;
				} else if(currLevel == oldLevel){
					b_left++;
				} else if(currLevel < oldLevel){
					for(int i=1; i<(oldLevel-currLevel)+1; i++){
						String cd1 = oldCode.substring(0, (oldCode.length()-i));
						String cd2 = null;
								
						if(oldCode.indexOf("-") != -1){// Compte avec - comme 24 - 25
							String[] cdArr = StringUtil.getArrayFromStringDelim(oldCode, "-");
							cd1 = cdArr[0].trim();
							cd1 = cd1.substring(0, (cd1.length()-i));
							
							cd2 = cdArr[1].trim();
							cd1 = cd2.substring(0, (cd2.length()-i));
						}
						
						for (ComptePersistant comptePersistant2 : listCompte) {
							if(comptePersistant2.getB_right() == null && (comptePersistant2.getCode().startsWith(cd1) || (cd2!=null && comptePersistant2.getCode().startsWith(cd2)))){
								b_left++;
								comptePersistant2.setB_right(b_left);
							}
						}
					}
					
					b_left++;
				}
				comptePersistant.setB_left(b_left);
				
				if(currLevel == nextLevel || currLevel > nextLevel){
					b_left++;
					comptePersistant.setB_right(b_left);
				}
				
				oldLevel = comptePersistant.getLevel();
				oldCode = comptePersistant.getCode();
				cpt++;
			}
			
		/*	for (ComptePersistant comptePersistant : listCompte) {
				if(comptePersistant.getB_left() == null){
					comptePersistant.setB_left(0);
				}
				if(comptePersistant.getB_right() == null){
					comptePersistant.setB_right(0);
				}
//				System.out.println(comptePersistant.getCode()+"---------"+comptePersistant.getB_left()+"---------"+comptePersistant.getB_right()+"---------"+comptePersistant.getLevel());
			}*/
			
//			NodeBean nodeBean = new NodeBean();
//			nodeBean.setBleft("b_left");
//			nodeBean.setBright("b_right");
//			nodeBean.setLabel("code");
//			nodeBean.setLevel("level");
//			
//			new TreeService(listCompte, nodeBean).show();
			
	      	ComptePersistant compteP = new ComptePersistant();
			compteP.setCode("X");
			compteP.setLibelle("ROOT");
			compteP.setClasse("X");
			compteP.setRubrique("X");
			compteP.setLevel(0);
			compteP.setB_left(0);
			compteP.setB_right(b_left++);
			
			listCompte.add(0, compteP);
			
			// Delete last
			listCompte.remove(listCompte.size()-1);
			//
			for (ComptePersistant comptePersistant : listCompte) {
				comptePersistant.setOpc_exercice(ContextAppli.getExerciceBean());
				comptePersistant.setOpc_abonne(ContextAppli.getAbonneBean());
				comptePersistant.setOpc_etablissement(ContextAppli.getEtablissementBean());
				comptePersistant.setOpc_societe(ContextAppli.getSocieteBean());

				em.merge(comptePersistant);
			}
		
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public ComptePersistant getCompteParent(Long compteId) {
		return compteDao.getCompteParent(compteId);
	}
	
}
