package framework.model.common.util.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import framework.controller.bean.NodeBean;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

public class TreeService {
	private List<ElementBase> listElement = null;
	
	/**
	 * @param listObject
	 * @param id
	 * @param label
	 * @param tooltip
	 */
	public TreeService(List listObject, NodeBean nodeBean){
		if(nodeBean.getBleft() == null){
			nodeBean.setBleft("b_left");
		}
		if(nodeBean.getBright() == null){
			nodeBean.setBright("b_right");
		}
		if(nodeBean.getLevel() == null){
			nodeBean.setBright("level");
		}
		// Convert
		convertListObjectToListElement(listObject, nodeBean);
		// Sort tree by left borne
		sortTreeByLeft();
		// Add levels to tree
	//	setTreeLevels();
		
		// Sort by sort
//		if(StringUtil.isNotEmpty(nodeBean.getSort())){
			//sortTreeByLabel();
//		}
	}
	
	/**
	 * @param listObject
	 * @param id
	 * @param label
	 * @param tooltip
	 * @param bleft
	 * @param bright
	 */
/*	public TreeService(List listObject, String id, String label, String tooltip, String bleft, String bright){
		convertListObjectToListElement(listObject, id, label, tooltip, bleft, bright);
		// Sort tree by left borne
		sortTreeByLeft();
		// Add levels to tree
		setTreeLevels();
	}*/

	/**
	 * @param listElement
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	/*public TreeService(List listObject, String id){
		convertListObjectToListElement(listObject, id);
		// Sort tree by left borne
		sortTreeByLeft();
		// Add levels to tree
		setTreeLevels();
	}*/

	/*@SuppressWarnings("unchecked")
	public TreeService(List listObject){
		convertListObjectToListElement(listObject, "id");
		// Sort tree by left borne
		sortTreeByLeft();
		// Add levels to tree
		setTreeLevels();
	}

	/*public TreeService(List<ElementBase> listElement){
		this.listElement = listElement;
	}*/

	/**
	 * @param listObject
	 * @param id
	 * @param label
	 * @param tooltip
	 * @throws Exception
	 */
	private void convertListObjectToListElement(List listObject, NodeBean nodeBean){//String id, String label, String tooltip, String bleft, String bright){
		this.listElement = new ArrayList<ElementBase>(listObject.size());
		for(Object bean : listObject){
			ElementBase element = new ElementBase();
			
			// Origine object
			element.setE_origineObject(bean);
			// Id
			String id = nodeBean.getId();
			if(StringUtil.isNotEmpty(id)){
				element.setE_id(ReflectUtil.getObjectPropertieValue(bean, id));
			}
			// Label
			String label = nodeBean.getLabel();
			if(StringUtil.isNotEmpty(label)){
				element.setE_label(StringUtil.getValueOrEmpty(ReflectUtil.getStringPropertieValue(bean, label)));
			}
			// Sort
			String sort = nodeBean.getSort();
			if(StringUtil.isNotEmpty(sort)){
				String val = StringUtil.getValueOrEmpty(ReflectUtil.getStringPropertieValue(bean, sort));
				if(StringUtil.isNotEmpty(val)){
					element.setE_sort(Integer.valueOf(val));
				}
			}
			// Tooltip
			String tooltip = nodeBean.getTooltip();
			if(StringUtil.isNotEmpty(tooltip)){
				element.setE_tooltip(StringUtil.getValueOrEmpty(ReflectUtil.getStringPropertieValue(bean, tooltip)));
			}
			// Tooltip
			String href = nodeBean.getHref();
			if(StringUtil.isNotEmpty(href)){
				element.setE_href(StringUtil.getValueOrEmpty(ReflectUtil.getStringPropertieValue(bean, href)));
			}
			// Icon
			String icon = nodeBean.getIcon();
			if(StringUtil.isNotEmpty(icon)){
				element.setE_icon(StringUtil.getValueOrEmpty(ReflectUtil.getStringPropertieValue(bean, icon)));
			}
			// Left and Right 
			element.setE_left(NumericUtil.toInteger(ReflectUtil.getStringPropertieValue(bean, nodeBean.getBleft())));
			element.setE_right(NumericUtil.toInteger(ReflectUtil.getStringPropertieValue(bean, nodeBean.getBright())));
			element.setE_level(NumericUtil.toInteger(ReflectUtil.getStringPropertieValue(bean, nodeBean.getLevel())));
			//
			listElement.add(element);
		}
	}

	/**
	 * @param listObject
	 * @param id
	 * @throws Exception
	 */
/*	@SuppressWarnings("unchecked")
	private void convertListObjectToListElement(List listObject, String id){
		this.listElement = new ArrayList<ElementBase>(listObject.size());
		for(Object bean : listObject){
			ElementBase element = new ElementBase();
			element.setE_id(ReflectUtil.getObjectPropertieValue(bean, id));
			element.setE_left(NumericUtil.toInteger(ReflectUtil.getStringPropertieValue(bean, "b_left")));
			element.setE_right(NumericUtil.toInteger(ReflectUtil.getStringPropertieValue(bean, "b_right")));
			//
			listElement.add(element);
		}
	}*/

	/**
	 * @param id
	 * @return
	 */
	public ElementBase getElementBase(Object id){
		return getElementBase(listElement, id);
	}

	/**
	 * @param listElm
	 * @param id
	 * @return
	 */
	public static ElementBase getElementBase(List<ElementBase> listElm, Object id){
		if(id != null){
			for(ElementBase element : listElm){
				if(String.valueOf(element.getE_id()).equals(""+id)){
					return element;
				}
			}
		}

		return null;
	}

	/**
	 * @return
	 */
	public List<ElementBase> getListElement(){
		return this.listElement;
	}

	/**
	 * Delete element (noeud or sheet) by id
	 * @param id
	 * @return
	 * @throws Exception : If element dooes not exist
	 */
	public List<ElementBase> deleteElement(Object id) throws RuntimeException {
		ElementBase delElement = getElementBase(id);
		//
		if(delElement == null){
			throw new RuntimeException("The element with id -" + id + "- does n't exist !");
		} else{
			if(isSheet(delElement)){
				ElementBase elementdel = deleteSheet(delElement);
				if(elementdel != null){
					List<ElementBase> listDelete = new ArrayList<ElementBase>();
					listDelete.add(elementdel);
					return listDelete;
				}
			} else{
				return deleteNoeud(delElement);
			}
		}

		return null;
	}

	/**
	 * True if the element is a sheet
	 * @param element
	 * @return
	 */
	public static boolean isSheet(ElementBase element){
		return (element.getE_right() - element.getE_left() == 1);
	}

	/**
	 * Delete noeud
	 * @param ElementBase
	 * @param left
	 */
	public List<ElementBase> deleteNoeud(ElementBase delElement){
		int bright = 0;
		int bleft = 0;
		List<ElementBase> listeRemove = new ArrayList<ElementBase>();

		// recuperer Borne gauche et Borne droite
		 bleft = delElement.getE_left();
		 bright = delElement.getE_right();

		// Delete
		for(ElementBase element : listElement){
			if(element.getE_left() >= bleft && element.getE_right()<= bright){
				listeRemove.add(element);
			}
		}
		//
		listElement.removeAll(listeRemove);

		// Update
		int decal = bright - bleft + 1;
		if(listeRemove.size() > 0){
			for(ElementBase element : listElement){
				if(element.getE_right() > bright){
					element.setE_right(element.getE_right() - decal);
				}
			}

			for(ElementBase element : listElement){
				if(element.getE_left() > bright){
					element.setE_left(element.getE_left() - decal);
				}
			}
		}
		// Sort tree by left borne
		//sortTreeByLeft();

		return listeRemove;
	}

	/**
	 * @param delElement : Element to delete
	 * @return
	 * @throws Exception
	 */
	public ElementBase deleteSheet(ElementBase delElement){
		int left = 0;
		// Delete
		left = delElement.getE_left();
		boolean delete = listElement.remove(delElement);

		// Mise a jour de Borne_G et Borne_D
		if(delete){
			for(ElementBase element : listElement){
				if(element.getE_left() > left){
					element.setE_left(element.getE_left() - 2);
				}
			}
			//
			for(ElementBase element : listElement){
				if(element.getE_right() > left){
					element.setE_right(element.getE_right() - 2);
				}
			}
		}
		//
//		sortTreeByLeft();

		return delElement;
	}

	/**
	 * Delete all sheet
	 * @throws Exception
	 */
	public void deleteAllSheets(){
		List<Integer> tempList = new ArrayList<Integer>();
		//
		for(ElementBase element : listElement){
			if(element.getE_right() - element.getE_left() == 1){
				tempList.add(element.getE_left());
			}
		}

		// Delete sheets
		for(int left : tempList){
			for(ElementBase tree : listElement){
				if(tree.getE_left() == left){
					deleteSheet(tree);
				}
			}
		}

		//
		tempList = null;
		// Sort by left
//		sortTreeByLeft();
	}

	/*\--------------------------------------------- ADD --------------------------------------------------\*/

	/**
	 * @param idTarget
	 * @param idCurrentElement
	 */
	public void addSheet(Object idTarget, Object idCurrentElement){
		ElementBase elementBase = new ElementBase(idCurrentElement);
		addSheet(idTarget, elementBase);
	}

	/**
	 * Add sheet
	 * @param idTarget
	 * @param newElement
	 */
	public void addSheet(Object idTarget, ElementBase newElement){
		if(idTarget == null){
			return;	
		}
		
		// Get element
		ElementBase targetElement = getElementBase(idTarget);
		if(targetElement == null){
			return;	
		}
		
		//
		int right = targetElement.getE_right();
		int left = targetElement.getE_left();
		int level = targetElement.getE_level();

		// Update right
		for(ElementBase element : listElement){
			if(element.getE_right() >= right){
				element.setE_right(element.getE_right() + 2);
			}
		}
		// Update left
		for(ElementBase element : listElement){
			if(element.getE_left() > right){
				element.setE_left(element.getE_left() + 2);
			}
		}

		// Insert new sheet
		int newLeft = right;
		int newRight = (right + 1);
		newElement.setE_left(newLeft);
		newElement.setE_right(newRight);
		newElement.setE_level(level + 1);
		//
		listElement.add(newElement);

		// Sort by left
		sortTreeByLeft();
	}

	/**
	 * Move element
	 * @param id
	 * @param idTarget 
	 * @throws Exception
	 */
	public void moveElement(Object elementId, Object idTarget){
		ElementBase delElement = getElementBase(elementId);
		if(isSheet(delElement)){
			moveSheet(delElement, idTarget);
		} else{
			moveNoeud(delElement, idTarget);
		}
	}

	/**
	 * @param delElement
	 * @param idTarget
	 * @throws Exception
	 */
	public void moveSheet(ElementBase element, Object idTarget){
		ElementBase backElement = deleteSheet(element);
		addSheet(idTarget, backElement);
	}

	/**
	 * @param delElement
	 * @param idTarget
	 * @throws Exception
	 */
	public void moveNoeud(ElementBase element, Object idTarget){
		int bright = 0;
		int level = 0;

		// liste des noeuds et feuiilles supprim�e
		List<ElementBase> listeMove = (List<ElementBase>)deleteNoeud(element);

		// recuperer Borne gauche et Borne droite de l'id destination
		ElementBase targetElement = getElementBase(idTarget);

		if(targetElement == null){
			if(listElement.size() > 0){
				int maxRight = 0, minLevel = 10;
				// Get max left, max right and min level
				for(ElementBase elementBase : listElement){
					if(elementBase.getE_right() > maxRight){
						maxRight = elementBase.getE_right();
					}
					if(elementBase.getE_level() < minLevel){
						minLevel = elementBase.getE_level();
					}
				}
				//
				//targetElement.setE_left(maxLeft + 1);
				bright = maxRight + 1;
				level = minLevel;
			}
		} else{
			 bright = targetElement.getE_right();
			 level = targetElement.getE_level();
		}

		// Calcul decalage
		//sortTreeByLeft();
		//
		int decal = bright - listeMove.get(0).getE_left();
		int decalLevel = level - listeMove.get(0).getE_level();
		// Mise a jour ds Borne_G et Borne_D de la liste des noeud a inserer
		for(ElementBase tree : listeMove){
			tree.setE_left(tree.getE_left() + decal);
			tree.setE_right(tree.getE_right() + decal);
			tree.setE_level(tree.getE_level() + decalLevel + 1);
		}

		// Mise a jour du rest de l'arbre
		int bdm = listeMove.get(0).getE_right();
		int bgm = listeMove.get(0).getE_left();
		int bdMove = bdm - bgm + 1;

		// Update right
		for(ElementBase tree : listElement){
			if(tree.getE_right() >= bright){
				tree.setE_right(tree.getE_right() + bdMove);
			}
		}
		// Update left
		for(ElementBase tree : listElement){
			if(tree.getE_left() > bright){
				tree.setE_left(tree.getE_left() + bdMove);
			}
		}
		// Ajout de la liste deplacee a la liste globale
		listElement.addAll(listeMove);

		// Sort by left
		sortTreeByLeft();
	}

	/**
	 * @param listElement
	 */
//	public void setTreeLevels(){
//		Integer level = 0;
//		ElementBase pElement = null;;
//		for(ElementBase treeElement : listElement){
//	    	Integer left = treeElement.getE_left();
//			Integer right = treeElement.getE_right();
//			//
//			if(pElement != null){
//		    	Integer pLeft = pElement.getE_left();
//				Integer pRight = pElement.getE_right(); 
//				
//				if(left > pLeft && right < pRight && (right-left) > 1){// Noeud
//					level++;
//				} else if(left > pLeft && right > pRight && (right-left) > 1){// Noeud
//					level = level - (left - pRight) + 1;
//				} else if(left > pLeft && right < pRight){// Feuille
//					level++; 
//				} else if(left > pLeft && right > pRight && (left-pRight>1)){// Feuille avec niveau monté
//					level = level - ((left - pRight) - 1);
//				} else if(left > pLeft && right > pRight ){// Feuille
//					// On ne fait rien
//				}
//			}
//			// Update level
//			treeElement.setE_level(level);
//			//
//			pElement = treeElement;
//		}
//	}

	/**
	 * @param listElement
	 */
	public void show(){
		for(ElementBase tree : listElement){
			String espace = "";
			int niveau = tree.getE_level();

			while(niveau != 0){
	    		espace = "---" + espace;
	    		niveau--;
	    	}
			//
	    	Integer left = tree.getE_left();
			Integer right = tree.getE_right();
			int level = tree.getE_level();
			//
			String img = (right - left > 1) ? "+" : "-";
			
			System.out.println(img + espace + tree.getE_label()+" || "+tree.getE_left()+"---"+tree.getE_right());
		}
	}

	/**
	 * Sort by left
	 */
	public void sortTreeByLeft(){
		Collections.sort(listElement, new SortByLeft());
	}

	/**
	 * @throws Exception
	 */
	public void sortTreeByOrderIdx(){
		List<ElementBase> listTemp = new ArrayList<ElementBase>(listElement);

		int maxSort = 0;
		for(ElementBase tree : listTemp){
			if(tree.getE_sort() == null){
				continue;
			}
			if(tree.getE_sort()>maxSort){
				maxSort = tree.getE_sort();
			}
		}
		
		
		// On tri par ordre alphabetique
//		Collections.sort(listTemp, new SortByIdx());

		for(int i=1; i<maxSort+1; i++){
			for(ElementBase tree : listTemp){
				if(tree.getE_sort() == null || tree.getE_sort() != i){
					continue;
				}
				
				Object parentId = getParentId(tree.getE_id());
				if(isSheet(tree)){
					moveSheet(tree, parentId);
				} else{
					moveNoeud(tree, parentId);
				}
			}
		}
		
		// Tri par borne gauche
		sortTreeByLeft();
		//
		listTemp = null;
	}

	/**
	 * @param level
	 * @param treeBean
	 * @return
	 */
	public Object getParentId(int level, ElementBase treeBean){
		Object parentId = null;

		for(ElementBase bean : listElement){
			if((bean.getE_level() == level) && ((bean.getE_left() < treeBean.getE_right()) && (bean.getE_right() > treeBean.getE_right()))){
				parentId = bean.getE_id();
				break;
			}
		}
		return parentId;
	}

	/**
	 * @param id
	 * @return
	 */
	public ElementBase getParentElement(Object id){
		ElementBase elementBase = getElementBase(id);
		int level = elementBase.getE_level();
		//
		for(ElementBase bean : listElement){
			if(((level-bean.getE_level())==1) && ((bean.getE_left() < elementBase.getE_right()) && (bean.getE_right() > elementBase.getE_right()))){
				return bean;
			}
		}

		return null;
	}

	/**
	 * @param id
	 * @return
	 */
	public Object getParentId(Object id){
		Object parentId = null;
		ElementBase elementBase = getElementBase(id);
		int level = elementBase.getE_level();
		//
		for(ElementBase bean : listElement){
			if(((level-bean.getE_level())==1) && ((bean.getE_left() < elementBase.getE_right()) && (bean.getE_right() > elementBase.getE_right()))){
				parentId = bean.getE_id();
				break;
			}
		}

		return parentId;
	}
}

/**
 * @author t.
 *
 */
class SortByLeft implements Comparator<ElementBase>{
	public int compare(ElementBase o1, ElementBase o2) {
		return new Integer(o1.getE_left()).compareTo(new Integer(o2.getE_left())) ;
	}
}

/**
 * @author t.
 *
 */
class SortByIdx implements Comparator<ElementBase>{
	public int compare(ElementBase o1, ElementBase o2) {
		if(o1.getE_sort()==null || o2.getE_sort()==null) {
			return 0;
		}
		return o1.getE_sort().compareTo(o2.getE_sort()) ;
	}
}