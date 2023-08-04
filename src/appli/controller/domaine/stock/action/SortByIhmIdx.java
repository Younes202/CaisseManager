package appli.controller.domaine.stock.action;

import java.util.Comparator;

import appli.model.domaine.stock.persistant.MouvementArticlePersistant;

class SortByIhmIdx implements Comparator<MouvementArticlePersistant>{
	@Override
	public int compare(MouvementArticlePersistant o1, MouvementArticlePersistant o2) {
		int returnVal = 0;

		if(o1 == null || o2 == null){
			return 0;
		}
		
	    if(o1.getIdxIhm() < o2.getIdxIhm()){
	        returnVal =  -1;
	    }else if(o1.getIdxIhm() > o2.getIdxIhm()){
	        returnVal =  1;
	    }
	    return returnVal;
	}
}
