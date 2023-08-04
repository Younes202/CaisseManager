package appli.model.domaine.administration.dao.impl;

import java.math.BigDecimal;

import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.Table;

import appli.model.domaine.administration.dao.ITreeDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import framework.model.common.util.ReflectUtil;
import framework.model.util.GenericJpaDao;

@Named
public class TreeDao extends GenericJpaDao<ArticlePersistant, Long> implements ITreeDao{
	/**
	 * 
	 */
	@Override
	public String getNextCodeTree(Class treeClass, Long parentId) {
		Table table = (Table) treeClass.getAnnotation(Table.class);
		String tableName = table.name();
		
		Object parentFam = findById(treeClass, parentId);
		String parentCode = ""+ReflectUtil.getObjectPropertieValue(parentFam, "code");
		boolean isPrentRoot = parentCode.equals("ROOT");
		int parentLength = (isPrentRoot ? 0 : parentCode.length());
		
		Query query = getNativeQuery("select  "
				+ "max("
				+ "	case when LENGTH(code) > "+parentLength+" then CAST(SUBSTR(code, "+(parentLength+1)+") AS UNSIGNED) "
				+ " else 0 end "
				+ ") from "+tableName
				+ " where b_left>:left and b_right<:right and level=:level") 
						.setParameter("left",ReflectUtil.getObjectPropertieValue(parentFam, "b_left"))
						.setParameter("right", ReflectUtil.getObjectPropertieValue(parentFam, "b_right"))
						.setParameter("level", Integer.valueOf(""+ReflectUtil.getObjectPropertieValue(parentFam, "level"))+1);
				
		BigDecimal max_num = (BigDecimal)query.getSingleResult();
		
		String nextCode = "1";
		if(max_num != null){
			nextCode = ""+(max_num.intValue() + 1);
		}
		return (isPrentRoot ? ""+nextCode : ReflectUtil.getObjectPropertieValue(parentFam, "code")+nextCode);
	}
}
