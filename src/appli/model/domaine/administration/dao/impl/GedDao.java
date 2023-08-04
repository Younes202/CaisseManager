package appli.model.domaine.administration.dao.impl;

import javax.inject.Named;

import appli.model.domaine.administration.dao.IGedDao;
import appli.model.domaine.administration.persistant.GedPersistant;
import appli.model.domaine.administration.validator.GedValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.util.GenericJpaDao;

@Named
@WorkModelClassValidator(validator = GedValidator.class)
public class GedDao extends GenericJpaDao<GedPersistant, Long> implements IGedDao {
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public GedPersistant getGedParent(GedPersistant ged) {
//		// On cherche le parent dans l'ancien exercice
//		List<GedPersistant> listGeds = getQuery(
//				"from GedPersistant cpt where "
//						+ "cpt.b_left<:left and cpt.b_right>:right "
//						+ "and type=:type "
//						+ "order by cpt.b_left desc ")
//				.setParameter("left", ged.getB_left())
//				.setParameter("right", ged.getB_right())
//				.setParameter("type", ged.getType())
//				.getResultList();
//
//		for (GedPersistant gedPersistant : listGeds) {
//			return gedPersistant;
//		}
//		return null;
//	}
//
//	@Override
//	@Transactional
//	@WorkModelMethodValidator
//	public void createGed(GedBean ged) {
//		GedPersistant parentCompte = findById(ged.getElement_id());
//
//		int right = parentCompte.getB_right();
//		// Update
//		updateSuccessifElements(2, right, "+", ged.getType());
//
//		GedPersistant gedP = new GedPersistant();
//		
//		// Insert new sheet
//		ReflectUtil.copyProperties(gedP, ged);
//		gedP.setLibelle(ged.getLibelle());
//		gedP.setB_left(right);
//		gedP.setB_right(right + 1);
//		gedP.setType(ged.getType());
//		gedP.setLevel(parentCompte.getLevel() + 1);
//		gedP.setDate_maj(new Date());
//		gedP.setSignature(Context.getUserLogin());
//
//		// Ajouter le nouvel élément � l'arbre
//		gedP = getEntityManager().merge(gedP);
//		
//		// Copier les propriété de l'entité
//		ReflectUtil.copyProperties(ged, gedP);
//	}
//
//	@Override
//	@Transactional
//	@WorkModelMethodValidator
//	public void deleteGed(Long id) {
//		GedPersistant gedPersistant = findById(id);
//		int b_right = gedPersistant.getB_right();
//		int b_left = gedPersistant.getB_left();
//
//		// Update elements-----------------------------------------------------
//		int decal = 2;
//
//		if (b_right - b_left != 1) {
//			decal = b_right - b_left + 1;
//		}
//
//		// Delete elements from DB
//		Query deleteQuery = getQuery(
//				"delete from GedPersistant compte where compte.b_left>=:left and compte.b_right<=:right"
//				+ " and type=:type")
//				.setParameter("left", b_left).setParameter("right", b_right)
//				.setParameter("type", gedPersistant.getType());
//
//		deleteQuery.executeUpdate();
//		getEntityManager().flush();
//
//		// Update
//		updateSuccessifElements(decal, gedPersistant.getB_right(), "-", gedPersistant.getType());
//		// update levels
////		Query query = getQuery(
////				"UPDATE GedPersistant ged SET ged.level = ged.level - 1"
////						+ " WHERE ged.b_right BETWEEN :min AND :max AND ged.b_left BETWEEN :min AND :max "
////						+ "and type=:type")
////				.setParameter("min", b_left).setParameter("max", b_right)
////				.setParameter("type", gedPersistant.getType());
////		query.executeUpdate();
//	}
//
//	/**
//	 * Mise à jour des bornes à partir des éléments impactés
//	 * 
//	 * @param decal
//	 * @param right
//	 */
//	@Transactional
//	private void updateSuccessifElements(int decal, int right, String sens, String type) {
//		// Update right
//		Query updateRightQuery = getQuery(
//				"update GedPersistant set b_right=b_right" + sens
//						+ ":decal " + "where b_right>=:right and type=:type")
//				.setParameter("decal", decal).setParameter("right", right)
//				.setParameter("type", type);
//		updateRightQuery.executeUpdate();
//		// Update left
//		Query updateLeftQuery = getQuery(
//				"update GedPersistant set b_left=b_left" + sens + ":decal "
//						+ "where b_left>=:right and type=:type")
//				.setParameter("decal", decal).setParameter("right", right)
//				.setParameter("type", type);
//		updateLeftQuery.executeUpdate();
//	}
//
//	@Override
//	@Transactional
//	@WorkModelMethodValidator
//	public void updateGed(GedBean gedBean) {
//		GedPersistant gedDb = findById(gedBean.getId());
//		gedDb.setLibelle(gedBean.getLibelle());
//		//GedPersistant gedParentDB = getGedParent(gedBean);
//		
//		// Vérifier si le parent à changé --> déplacement
//		//if (!targetId.equals(gedParentDB.getId())) {
//		//	GedPersistant gedTarget = findById(targetId);
//		//	mouveElement(gedBean, gedTarget);
//		//}
//		getEntityManager().merge(gedDb);
//	}
//
//	/**
//	 * @param gedP
//	 * @param gedTarget
//	 */
//	private void mouveElement(GedPersistant gedP, GedPersistant gedTargetP) {
//		int decal = 2;
//		int b_right = gedP.getB_right();
//		int b_left = gedP.getB_left();
//
//		// Mise à jour come pour une suppression
//		updateSuccessifElements(decal, gedP.getB_right(), "-", gedP.getType());
//		//
//		Query query = getQuery(
//				"UPDATE GedPersistant ged SET ged.level = ged.level - 1"
//						+ " WHERE ged.b_right BETWEEN :min AND :max AND ged.b_left BETWEEN :min AND :max "
//						+ "and type=:type")
//				.setParameter("min", b_left).setParameter("max", b_right)
//				.setParameter("type", gedP.getType());
//		query.executeUpdate();
//
//		// Feuille
//		int right = gedTargetP.getB_right();
//		// Update
//		updateSuccessifElements(2, right, "+", gedP.getType());
//		// Insert new sheet
//		gedP.setB_left(right);
//		gedP.setB_right(right + 1);
//		gedP.setLevel(gedTargetP.getLevel() + 1);
//		//
//		super.update(gedP);
//	}
}
