package appli.model.domaine.administration.service;


import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.stock.bean.ArticleBean;
import framework.model.service.IGenericJpaService;

public interface IRequeteurService extends IGenericJpaService<ArticleBean, Long>{
	List<String> getListTables();
	Map<String, Object> executeSql(String requete, Integer start) throws SQLException;
	File[] getListLogFiles(String tomcatPath);
}
