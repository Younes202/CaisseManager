package framework.model.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.tree.ElementBase;

public class TreeFolderService {
	private int idxDir = 1;
	private int idxLevel = 1;
	List<ElementBase> listPaths = new ArrayList<ElementBase>();
	List<ElementBase> listFolders = new ArrayList<ElementBase>();
	private String basePath;
	
	public TreeFolderService(String basePath){
		this.basePath = StrimUtil._GET_PATH("temp")+ "/" + basePath;
	}
	
	/**
	 * @param currPath
	 * @param elementDir
	 * @return
	 */
	public List<ElementBase> getDirContentPaths(String currPath, ElementBase elementDir){
		currPath = this.basePath + currPath; 
        File root = new File(currPath);
        File[] list = root.listFiles();

        if (list == null || list.length == 0) {
            if(elementDir != null){
            	elementDir.setE_right(idxDir++);
            	idxLevel--;
            }

        	return listPaths;
        }

        for ( File f : list ) {
            String replacePath = f.getAbsolutePath().replace("\\", "/").replace(this.basePath, "");
			String pathEncrypted = EncryptionUtil.encrypt(replacePath);
             
			if (f.isDirectory()){
                ElementBase element = new ElementBase();
                element.setE_left(idxDir++);
                element.setE_label(f.getName());
                element.setE_level(idxLevel++);
                element.setE_id(pathEncrypted);
                
                listPaths.add(element);
                listFolders.add(element);
                
            	getDirContentPaths(replacePath, element);
            }
            else {
                ElementBase element = new ElementBase();
                element.setE_left(idxDir++);
                element.setE_right(idxDir++);
                element.setE_label(f.getName());
                element.setE_level(idxLevel);
                element.setE_id(pathEncrypted);
                
                listPaths.add(element);
            }
        }
        
        if(elementDir != null){
        	elementDir.setE_right(idxDir++);
        	idxLevel--;
        }
               
        return this.listPaths;
	}
	
	public List<ElementBase> getListRepetoires(){
		return this.listFolders;
	}

	/**
	 * @param listElement
	 * @param id
	 * @return
	 */
	public static ElementBase getElementById(List<ElementBase> listElement, String id) {
		for (ElementBase elementBase : listElement) {
			if(elementBase.getE_id().equals(id)){
				return elementBase;
			}
		}
		return null;
	}
	
	/**
	 * @param listElement
	 * @param element
	 * @return
	 */
	public static ElementBase getParent(List<ElementBase> listElement, ElementBase element) {
		int level = element.getE_level();
		
		for (ElementBase elementBase : listElement) {
			if(((level-elementBase.getE_level())==1) && ((elementBase.getE_left() < element.getE_right()) && (elementBase.getE_right() > element.getE_right()))){
					return elementBase;
			}
		}

		return null;
	}
}
