package appli.model.domaine.administration.service.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.internal.SessionImpl;

import appli.controller.domaine.stock.bean.ArticleBean;
import appli.model.domaine.administration.dao.IJobDao;
import appli.model.domaine.administration.service.IRequeteurService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@Named
public class RequeteurService extends GenericJpaService<ArticleBean, Long> implements IRequeteurService{

	@Inject
	private IJobDao jobDao; 
	
	@Override
	public List<String> getListTables() {
		List<String> listTables = new ArrayList<>();
		SessionImpl session = (SessionImpl) jobDao.getEntityManager().getDelegate();
		Connection conn = session.connection();
		
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(StrimUtil.getGlobalConfigPropertie("db.name"), null, "%", null);
			while (rs.next()) {
				listTables.add(rs.getString(3));
			}
			Collections.sort(listTables);
		} catch(Exception e){
			e.printStackTrace();
		}
		return listTables;
	}

	@Override
	public Map<String, Object> executeSql(String requete, Integer start) throws SQLException {
		SessionImpl session = (SessionImpl) jobDao.getEntityManager().getDelegate();
		Connection conn = session.connection();
		Statement stmt = conn.createStatement();
		
		boolean isCrud = false;
		if(requete.toLowerCase().indexOf("update") != -1 
				|| requete.toLowerCase().indexOf("drop") != -1
				|| requete.toLowerCase().indexOf("insert") != -1
				|| requete.toLowerCase().indexOf("alter") != -1
				|| requete.toLowerCase().indexOf("delete") != -1){
			isCrud = true;
		}
		
		if(isCrud){
			if(requete.indexOf(";") != -1) {
				String[] requetes = StringUtil.getArrayFromStringDelim(requete, ";");
				for (String req : requetes) {
					if(StringUtil.isEmpty(req)) {
						continue;
					}
					stmt.executeUpdate(req);	
				}
			} else {
				stmt.executeUpdate(requete);	
			}
		} else{
			
			requete = requete.replace(';', ' ');
			
			if(start < 0) {
				start = 0;
			}
			
			requete = requete + " LIMIT 100 OFFSET "+start;
			
			ResultSet rs = stmt.executeQuery(requete);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			StringBuilder sb = new StringBuilder( 1024 );
			Map<String, String> mapCols = new LinkedHashMap<>();
			//
			for ( int i = 1; i <= columnCount; i ++ ) {
			    if ( i > 1 ) sb.append( ", " );
			    String columnName = rsmd.getColumnLabel( i );
			    String columnType = rsmd.getColumnTypeName( i );
	
			    mapCols.put(columnName, columnType);
	
	//		    int precision = rsmd.getPrecision( i );
	//		    if ( precision != 0 ) {
	//		        sb.append( "( " ).append( precision ).append( " )" );
	//		    }
			} // for columns
			
			List<Object[]> listData = new ArrayList<>();
			
			while(rs.next()){
				int idx = 0;
				Object[] dataArray = new Object[mapCols.size()];
				for(String colName : mapCols.keySet()){
					dataArray[idx] = rs.getObject(colName);
					idx++;
				}
				listData.add(dataArray);
			}
			
			Map<String , Object> mapReturn = new HashMap<>();
			mapReturn.put("cols", mapCols);
			mapReturn.put("data", listData);
			
			return mapReturn;
		}
		
		return null;
	}

	@Override
	public File[] getListLogFiles(String tomcatPath) {
		tomcatPath = tomcatPath+"/logs"; 
		
		return new File(tomcatPath).listFiles();
	}
	
}
