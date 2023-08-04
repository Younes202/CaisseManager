package framework.model.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import framework.component.complex.table.RequestTableBean;
import framework.controller.bean.RequestFormBean;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.constante.ProjectConstante.QUERY_CONDITIONS;
import framework.model.common.constante.ProjectConstante.TYPE_DATA_ENUM;
import framework.model.common.service.MessageService;
import framework.model.common.service.TypeValidatorService;


public class SqlParser {
	
	private static final Class<?> TYPE_NUMBER_ENTITY = BigDecimal.class;//--> Pour le moment decimal et bigdecimal sont supportés
	//private static final Class<?> TYPE_DATE_ENTITY = Date.class;
	
	private static final String COND_FROM = "from";
	private static final String COND_SELECT = "select";
	private static final String COND_GROUP = "group by";
	private static final String COND_ORDER = "order by";
	private static final String COND_WHERE = "where";
	private String query;
	private String queryLower;
	private static String nextOperateur = "";
	private String tableOrderBloc;
	private String tableConditionBloc;
	//
	private RequestTableBean queryTableBean;
	private RequestFormBean queryFormBean;

	/**
	 * @param query
	 * @param dataMap
	 */
	public SqlParser(String query, RequestFormBean queryBean) {
		this.query = query;
		this.queryLower = query.toLowerCase();
		this.queryFormBean = queryBean;
	}

	/**
	 * @param query
	 * @param queryBean
	 */
	public SqlParser(String query, RequestTableBean queryBean) {
		this.query = query;
		this.queryLower = query.toLowerCase();
		this.queryFormBean = queryBean.getFormBean();
		this.queryTableBean = queryBean;
		// Add table criteria
		addTableFilterCriters();
	}


	//----------------------------------------------------------------------------------------------

	private void addTableFilterCriters(){
		Map<String, Object> filterCriterionMap = this.queryTableBean.getFilterCriteria();

		// Build new request
		if(!filterCriterionMap.isEmpty()){
			StringBuilder sbTemp = new StringBuilder();
			boolean isAdded = false;
			//
			for(String key : filterCriterionMap.keySet()){
				if(key.endsWith("_to")){// If from to value
					continue;
				}
				if(isAdded){// If not error
					sbTemp.append("and ");
				}
				//
				isAdded = addTableDynConditionBlock(sbTemp, key);
			}
			// Delete last and
			while(sbTemp.toString().endsWith("and ")){
				sbTemp = new StringBuilder(sbTemp.substring(0, (sbTemp.length()-4)) + " ");
			}
			this.tableConditionBloc	= sbTemp.toString();
		}
		//
		this.tableOrderBloc = getOrder();
	}

	/**
	 * @return
	 */
	private String getOrder(){
		Map<String, String> orderSortMap = this.queryTableBean.getOrderSortMap();
		Map<String, String> filterJoinMap = this.queryTableBean.getFieldJoin();

		String order = "";
		if(!orderSortMap.isEmpty()){
			for(String key : orderSortMap.keySet()){
				String fieldKey = key;
				// Manage join key
				String fieldJoinValues = filterJoinMap.get(key);
				if(fieldJoinValues != null){
					String fieldLabel = StringUtil.getArrayFromStringDelim(fieldJoinValues)[2];
					if(StringUtil.isNotEmpty(fieldLabel)){
						fieldKey = (fieldKey.endsWith(".id") ? fieldKey.substring(0,  fieldKey.lastIndexOf(".")) : fieldKey) + "." + fieldLabel;
					}
				}
				//-------
				order += (fieldKey + " " + orderSortMap.get(key) + ",");
			}
			// Delete last ","
			order = order.substring(0, (order.length()-1));
		}
		return order;
	}
	//-------------------------------------------------------------------------------------

	/**
	 * @return
	 */
	public String getFinalQuery(){
		StringBuilder finalQuery = new StringBuilder();

		String selectBloc = this.getSelectBloc();
		if(StringUtil.isNotEmpty(selectBloc)){
			finalQuery.append("select " + selectBloc);
		}

		String fromBloc = this.getFromBloc();
		if(StringUtil.isNotEmpty(fromBloc)){
			finalQuery.append("from " + this.getFromBloc());
		}

		String conditionBloc = this.getConditionBloc();
		if(StringUtil.isNotEmpty(conditionBloc)){
			finalQuery.append(" " + COND_WHERE + " " + conditionBloc);
		}
		// Add table filter condition
		if(StringUtil.isNotEmpty(tableConditionBloc)){
			if(StringUtil.isNotEmpty(conditionBloc)){
				finalQuery.append(" and ");
			}
			if((finalQuery.toString().toLowerCase().indexOf(COND_WHERE) == -1)){
				finalQuery.append(" " + COND_WHERE + " ");
			}
			finalQuery.append(tableConditionBloc);
		}

		String groupBloc = this.getGroupBloc();
		if(StringUtil.isNotEmpty(groupBloc)){
			finalQuery.append(" " + COND_GROUP + " " + groupBloc);
		}
		// If is table filter
		if(StringUtil.isNotEmpty(tableOrderBloc)){
			finalQuery.append(" " + COND_ORDER + " " + tableOrderBloc);
		} else{
			String orderBloc = this.getOrderBloc();
			if(StringUtil.isNotEmpty(orderBloc)){
				finalQuery.append(" " + COND_ORDER + " " + orderBloc);
			}
		}

		return finalQuery.toString().replaceAll("  ", " ");
	}

	/**
	 * @return
	 */
	public String getSelectBloc(){
		int idx_select = this.queryLower.indexOf(COND_SELECT);
		int idx_from = this.queryLower.indexOf(COND_FROM);
		//
		if(idx_select != -1){
			return this.query.substring(idx_select+COND_SELECT.length(), idx_from);
		}

		return null;
	}

	/**
	 * @return
	 */
	public String getFromBloc(){
		int idx_from = this.queryLower.indexOf(COND_FROM);
		int idx_end = this.queryLower.indexOf(COND_WHERE);
		//
		if(idx_end == -1){
			idx_end = this.queryLower.indexOf(COND_GROUP);
			if(idx_end == -1){
				idx_end = this.queryLower.indexOf(COND_ORDER);
			} else{
				idx_end = this.queryLower.length();
			}
		}
		String fromBloc = this.query.substring(idx_from+COND_FROM.length(), idx_end);

		return fromBloc;
	}

	/**
	 * @return
	 */
	public String getConditionBloc(){
		Map<String, Object> queryData = new HashMap<String, Object>();
		String condtionBloc = this.getWhereBloc();
		String finalCondition = "";

		//
		if(StringUtil.isNotEmpty(condtionBloc)){
			Map<String, Object> dataMap = queryFormBean.getFormCriterion();

			// Patterns
			String splitBracketPattern = "\\][^\\[\\]]*\\[|[^\\[\\]]*\\[|\\][^\\[\\]]*";
			String splitParentePattern = "\\}[^\\{\\}]*\\{|[^\\{\\}]*\\{|\\}[^\\{\\}]*";
			// Compile
			Pattern bPattern = Pattern.compile(splitBracketPattern);
			Pattern pPattern = Pattern.compile(splitParentePattern);
			// Liste cles
	        List<String> bClesList = getListCles(bPattern.split(condtionBloc), condtionBloc);
	        List<String> pClesList = getListCles(pPattern.split(condtionBloc), condtionBloc);
	        List<String> tempArray = new ArrayList<String>();

	        // Build bracket parameters map
			for(String st : bClesList){
				Object value = dataMap.get(st.replaceAll("%", ""));
				if(value != null){
					queryData.put(st.replaceAll("%", ""), addPourcent(st, value));
				}
			}
			// Build parentheses parameters map
			for(String st : pClesList){
				String key = st.replaceAll("%", "");
				Object value = dataMap.get(key);
				//
				queryData.put(key, addPourcent(st, value));
			}

			boolean isDataComplete = (bClesList.size() + pClesList.size()) == queryData.size();
			// Reorganize query
			if(isDataComplete){
				for(String key : queryData.keySet()){
					// Replace "." by "_" --> Bug hibernate
					tempArray.add(key);
					String newKey = key.replace('.', '_');
					//
					finalCondition = finalCondition.replaceAll(key, newKey);
				}
				finalCondition = replaceParams(condtionBloc);
			} else{
				int idxOpr = 0;
				//
				while(idxOpr != -1){
					int idxOprS = getIdxOperateur(condtionBloc, idxOpr);
					idxOprS = (idxOprS == -1) ? condtionBloc.length() : idxOprS;

					if(idxOprS == idxOpr){
						break;
					}

					String temp = condtionBloc.substring(idxOpr, idxOprS);
					idxOpr = idxOprS+nextOperateur.length();
					// Delete not used blocs
					//
					boolean isPassed = false;
					for(String key : queryData.keySet()){
						if(temp.indexOf(key) != -1){
							// Replace "." by "_" --> Bug hibernate
							tempArray.add(key);
							String newKey = key.replace('.', '_');
							
							int lastIdx = temp.lastIndexOf(key);
							temp = temp.substring(0, lastIdx) + temp.substring(lastIdx).replaceAll(key, newKey); 
							//
							//temp = temp.replaceAll(key, newKey);

							finalCondition += temp + nextOperateur;
							isPassed = true;
							
							break;
						}
					}
					if(!isPassed && temp.indexOf("]") == -1 && temp.indexOf("}") == -1){
						finalCondition = finalCondition + temp + nextOperateur; 
					}
					// Final request
					finalCondition = replaceParams(finalCondition);
				}
				// Remove last operator
				boolean endWithAnd = finalCondition.toLowerCase().trim().endsWith("and");
				boolean endWithOr = finalCondition.toLowerCase().trim().endsWith("or");
				if(endWithAnd || endWithOr){
					int queryLength = finalCondition.trim().length();
					int idxEnd = endWithAnd ? (queryLength-3) : (queryLength-2);
					finalCondition = finalCondition.trim().substring(0, idxEnd);
				}
				// Add parentheses
				finalCondition = getParenthesesToAdd(finalCondition);
			}

			// Add keys
			for(String key : tempArray){
				if(key.indexOf(".") != -1){
					String newKey = key.replace('.', '_');
					Object value = queryData.get(key);
					//
					queryData.put(newKey, value);
				}
			}
			// Remove old keys
			for(String key : tempArray){
				if(key.indexOf(".") != -1){
					queryData.remove(key);
				}
			}

			// Add form query criteria
			this.queryFormBean.setQueryCriterion(queryData);
			// Add table query criteria
			if(this.queryTableBean != null){
				this.queryFormBean.getQueryCriterion().putAll(this.queryTableBean.getQueryCriteria());
			}
		}

		return finalCondition;
	}

	/**
	 * @param condition
	 * @return
	 */
	private String getParenthesesToAdd(String condition){
        int nbrOpenParenthese = condition.replaceAll("[^(]", "").length();
        int nbrCloseParenthese = condition.replaceAll("[^)]", "").length();
        int ecart = nbrOpenParenthese - nbrCloseParenthese;
        boolean isMoreOpen = (ecart > 0);
        boolean isMoreClose = (ecart < 0);
        //
        if (isMoreOpen) {
        	condition = condition + repeat(")", Math.abs(ecart));
        } else if (isMoreClose) {
        	condition = repeat("(", Math.abs(ecart)) + condition;
        }

        return condition;
	}

	/**
	 * @param carac
	 * @param count
	 * @return
	 */
	private String repeat(String carac, int count) {
		String value = "";
		for(int i=0; i<count; i++){
			value += carac;
		}
		return value;
	}

	/**
	 * @return
	 */
	public String getWhereBloc(){
		int idx_where = this.queryLower.indexOf(COND_WHERE);
		String whereBloc = null;
		//
		if(idx_where != -1){
			int idx_order = this.queryLower.indexOf(COND_ORDER);
			int idx_group = this.queryLower.indexOf(COND_GROUP);
			//
			if(idx_group != -1){
				whereBloc = this.query.substring(idx_where+COND_WHERE.length(), idx_group);
			} else if(idx_order != -1){
				whereBloc = this.query.substring(idx_where+COND_WHERE.length(), idx_order);
			}  else{
				whereBloc = this.query.substring(idx_where+COND_WHERE.length());
			}
		}

		return whereBloc;
	}

	/**
	 * @return
	 */
	public String getOrderBloc(){
		int idx_order = this.queryLower.indexOf(COND_ORDER);
		//
		String orderBloc = this.query.substring(idx_order+COND_ORDER.length());

		return orderBloc;
	}

	/**
	 * @return
	 */
	public Map<String, String> getOrderMap(){
		String orderBloc = getOrderBloc();
		if(orderBloc != null){
			Map<String, String> orderMap = new LinkedHashMap<String, String>();
			String[] orderArray = StringUtil.getArrayFromStringDelim(orderBloc, ",");
			//
			for(String val : orderArray){
				String[] colArray = StringUtil.getArrayFromStringDelim(val, " ");
				orderMap.put(colArray[0], ((colArray.length==2) ? colArray[1] : null));
			}
			return orderMap;
		}

		return null;
	}

	/**
	 * @return
	 */
	public String getGroupBloc(){
		int idx_group = this.queryLower.indexOf(COND_GROUP);
		int idx_order = this.queryLower.indexOf(COND_ORDER);
		//
		if(idx_group != -1){
			int idx_end = (idx_order == -1)?this.query.length():idx_order;
			String groupBloc = this.query.substring((idx_group+COND_GROUP.length()), idx_end);
			return groupBloc;
		}

		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	private String replaceParams(String value){
		value = value.replaceAll("'\\[", ":")
					 .replaceAll("'\\{", ":")
 					 .replaceAll("]'", " ")
					 .replaceAll("}'", " " );
					 //.replaceAll("%", "");
		return value;
	}

    /**
     * @param donne
     * @param offset
     * @return
     */
    private int getIdxOperateur(String donne, int offset) {
        int idxAnd = donne.toLowerCase().indexOf(" and ", offset);
        int idxOr = donne.toLowerCase().indexOf(" or ", offset);
        //
        if(idxAnd > idxOr){
        	if(idxOr > 0) {
        		nextOperateur = " or ";
        		return idxOr;
        	} else{
        		nextOperateur = " and ";
        		return idxAnd;
        	}
        } else if (idxOr > idxAnd){
        	if(idxAnd > 0) {
        		nextOperateur = " and ";
        		return idxAnd;
        	} else{
        		nextOperateur = " or ";
        		return idxOr;
        	}
        }
        nextOperateur = "";

        return -1;
    }

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	private Object addPourcent(String key, Object value){
		if(value != null){
			if(key.startsWith("%")){
				value = "%" + value;
			}
			if(key.endsWith("%")){
				value = value + "%";
			}
		}

		return value;
	}

	/**
	 * @param cles
	 * @param condition
	 * @return
	 */
	private List<String> getListCles(String[] cles, String condition){
        List<String> bClesList = new ArrayList<String>();
        for (int i = 0; i < cles.length; i++) {
            if (cles[i] != null && cles[i].trim().length() > 0 && !cles[i].equals(condition)) {
            	bClesList.add(cles[i]);
            }
        }

        return bClesList;
	}

	/**
	 * @param sbTemp
	 * @param key
	 * @return
	 */
	private boolean addTableDynConditionBlock(StringBuilder sbTemp, String key) {
		Map<String, Object> filterCriterionMap = this.queryTableBean.getFilterCriteria();
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		Map<String, String> conditionCriterionMap = this.queryTableBean.getConditionCriteria();
		Map<String, String> conditionTypeMap = this.queryTableBean.getConditionType();
		Map<String, String> fieldJoinMap = this.queryTableBean.getFieldJoin();
		//
		String newKey = key.replace('.', '_');
		String condition = conditionCriterionMap.get(key);
		boolean isAdded = true;
		boolean isJoin = false;
		// Like condition = cast to string
		Object filterValue = filterCriterionMap.get(key);
		String fieldJoinValues = fieldJoinMap.get(key);
		String conditionType = conditionTypeMap.get(key);
		
		if(fieldJoinValues != null){
			isJoin = StringUtil.isTrue(StringUtil.getArrayFromStringDelim(fieldJoinValues)[0]);
			if(isJoin){
				sbTemp.append("(");
			}
			String groupLabel = StringUtil.getArrayFromStringDelim(fieldJoinValues)[1];
			if(StringUtil.isNotEmpty(groupLabel)){
				//key += "." + groupLabel; 
			}
		}
		
		if(condition.indexOf("%") != -1){
			sbTemp.append("CAST(" + key + " as string) like :" + "filter_" + newKey+ " ");
			if(QUERY_CONDITIONS.STRING_CONDITION_LIKE.getCode().equals(condition)){
				queryCriterion.put("filter_" + newKey, "%"+filterValue+"%");
			} else if(QUERY_CONDITIONS.STRING_CONDITION_END.getCode().equals(condition)){
				queryCriterion.put("filter_" + newKey, "%"+filterValue);
			} else if(QUERY_CONDITIONS.STRING_CONDITION_START.getCode().equals(condition)){
				queryCriterion.put("filter_" + newKey, filterValue+"%");
			}
		} else{
			// Convert element to real type
			if(conditionType != null){
				if(ProjectConstante.TYPE_DATA_ENUM.DATE.getType().equals(conditionType) || ProjectConstante.TYPE_DATA_ENUM.DATE_TIME.getType().equals(conditionType)){
					isAdded = getDateCondition(sbTemp, filterValue, condition, key);
				} else if(ProjectConstante.TYPE_DATA_ENUM.BOOLEAN.getType().equals(conditionType)){
					isAdded = getBooleanCondition(sbTemp, filterValue, condition, key);
				} else if(ProjectConstante.TYPE_DATA_ENUM.LONG.getType().equals(conditionType)){
					isAdded = getLongCondition(sbTemp, filterValue, condition, key);
				} else if(ProjectConstante.TYPE_DATA_ENUM.LONG_ARRAY.getType().equals(conditionType)){ 
					isAdded = getLongArrayCondition(sbTemp, filterValue, condition, key);
				} 
				 else if(ProjectConstante.TYPE_DATA_ENUM.STRING_ARRAY.getType().equals(conditionType)){ 
						isAdded = getStringArrayCondition(sbTemp, filterValue, condition, key);
					} 
				else if(ProjectConstante.TYPE_DATA_ENUM.INTEGER.getType().equals(conditionType)){
					isAdded = getIntegerCondition(sbTemp, filterValue, condition, key);
				} else if(ProjectConstante.TYPE_DATA_ENUM.DECIMAL.getType().equals(conditionType)){
					isAdded = getDecimalCondition(sbTemp, filterValue, condition, key);
				} else{
					queryCriterion.put("filter_" + newKey, filterValue);
					sbTemp.append(key + " " + condition + ":filter_" + newKey+ " ");
				}
			}
		}
		
		// Add left join
		if(isJoin){
			sbTemp.append(" or " + key + " IS NULL)");
		}

		return isAdded;
	}
	
	/*-------------------------------------------------- Condition by field type -------------------------------------------------------*/
	/**
	 * @param sbTemp
	 * @param filterValue
	 * @param condition
	 * @param key
	 * @return
	 */
	private boolean getDecimalCondition(StringBuilder sbTemp, Object filterValue, String condition, String key){
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		String newKey = key.replace('.', '_');
		
		if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition) || QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN_EQUALS.getCode().equals(condition)){
			if((""+filterValue).indexOf("|") == -1){
				MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.between.error");
				return false;
			} else{
				Object filterValue2 = (""+filterValue).substring((""+filterValue).indexOf("|")+1);
				filterValue = (""+filterValue).substring(0, (""+filterValue).indexOf("|"));
				
				if(!NumericUtil.isNum(filterValue)){
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue);
					return false;
				} else if(!NumericUtil.isNum(filterValue2)){
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue2);
					return false;
				}
				
				if(TYPE_NUMBER_ENTITY.equals(BigDecimal.class)){
					filterValue = new BigDecimal(""+filterValue);
					filterValue2 = new BigDecimal(""+filterValue2);
				} else if(TYPE_NUMBER_ENTITY.equals(Double.class)){
					filterValue = NumericUtil.getDoubleOrDefault(filterValue);
					filterValue2 = NumericUtil.getDoubleOrDefault(filterValue2);
				} 
				
				// Add keys to filter map for request				
				queryCriterion.put("filter_" + newKey+"_1", filterValue);
				queryCriterion.put("filter_" + newKey+"_2", filterValue2);
				
				if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition)){
					sbTemp.append("( " + key + " > :filter_" + newKey+ "_1 and " + key + " < :filter_" + newKey+"_2 )");									
				} else{
					sbTemp.append("( " + key + " >= :filter_" + newKey+ "_1 and " + key + " <= :filter_" + newKey+"_2 )");
				}
			}
		} else if(!NumericUtil.isDecimal(filterValue)){
			MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue);
			return false;
		} else{
			filterValue = NumericUtil.getDoubleOrDefault(filterValue);
			
			if(TYPE_NUMBER_ENTITY.equals(BigDecimal.class)){
				filterValue = new BigDecimal(""+filterValue);
			} else if(TYPE_NUMBER_ENTITY.equals(Double.class)){
				filterValue = NumericUtil.getDoubleOrDefault(filterValue);
			} 
			
			sbTemp.append(key + " " + condition + " :" + "filter_" + newKey+ " ");
			queryCriterion.put("filter_" + newKey, filterValue);
		}
		return true;
	}
	
	/**
	 * @param sbTemp
	 * @param filterValue
	 * @param condition
	 * @param key
	 * @return
	 */
	private boolean getLongCondition(StringBuilder sbTemp, Object filterValue, String condition, String key){
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		String newKey = key.replace('.', '_');
		
		if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition) || QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN_EQUALS.getCode().equals(condition)){
			if((""+filterValue).indexOf("|") == -1){
				MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.between.error");
				return false;
			} else{
				Object filterValue2 = (""+filterValue).substring((""+filterValue).indexOf("|")+1);
				filterValue = (""+filterValue).substring(0, (""+filterValue).indexOf("|"));
				
				if(!NumericUtil.isLong(filterValue)){
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue);
					return false;
				} else if(!NumericUtil.isLong(filterValue2)){
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue2);
					return false;
				}
				
				filterValue2 = NumericUtil.getLongOrDefault(filterValue2);
				filterValue = NumericUtil.getLongOrDefault(filterValue);

				// Add keys to filter map for request				
				queryCriterion.put("filter_" + newKey+"_1", filterValue);
				queryCriterion.put("filter_" + newKey+"_2", filterValue2);
				
				if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition)){
					sbTemp.append("( " + key + " > :filter_" + newKey+ "_1 and " + key + " < :filter_" + newKey+"_2 )");									
				} else{
					sbTemp.append("( " + key + " >= :filter_" + newKey+ "_1 and " + key + " <= :filter_" + newKey+"_2 )");
				}
			}
		} else if(!NumericUtil.isLong(filterValue)){
			MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue);
			return false;
		} else{
			filterValue = NumericUtil.getLongOrDefault(filterValue);
			sbTemp.append(key + " " + condition + " :" + "filter_" + newKey+ " ");
			queryCriterion.put("filter_" + newKey, filterValue);
		}
		
		return true;
	}

	/**
	 * @param sbTemp
	 * @param filterValue
	 * @param condition
	 * @param key
	 * @return
	 */
	private boolean getLongArrayCondition(StringBuilder sbTemp, Object filterValue, String condition, String key){
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		String newKey = key.replace('.', '_');
		
		Set<Long> listIds = new HashSet<>(); 
		if(filterValue != null && ((String[]) filterValue).length > 0){
			for(String id : (String[]) filterValue){
				if(StringUtil.isNotEmpty(id) && NumericUtil.isNum(id)){
					listIds.add(Long.valueOf(id));
				}
			}
		}
		
		sbTemp.append(key + " in :" + "filter_" + newKey+ " ");
		queryCriterion.put("filter_" + newKey, listIds);
		
		return true;
	}
	
	/**
	 * @param sbTemp
	 * @param filterValue
	 * @param condition
	 * @param key
	 * @return
	 */
	private boolean getStringArrayCondition(StringBuilder sbTemp, Object filterValue, String condition, String key){
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		String newKey = key.replace('.', '_');
		
		Set<String> listIds = new HashSet<>(); 
		if(filterValue != null && ((String[]) filterValue).length > 0){
			for(String id : (String[]) filterValue){
				if(StringUtil.isNotEmpty(id)){
					listIds.add(id);
				}
			}
		}
		
		sbTemp.append(key + " in :" + "filter_" + newKey+ " ");
		queryCriterion.put("filter_" + newKey, listIds);
		
		return true;
	}
	
	/**
	 * @param sbTemp
	 * @param filterValue
	 * @param condition
	 * @param key
	 * @return
	 */
	private boolean getIntegerCondition(StringBuilder sbTemp, Object filterValue, String condition, String key){
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		String newKey = key.replace('.', '_');
		
		if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition) || QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN_EQUALS.getCode().equals(condition)){
			if((""+filterValue).indexOf("|") == -1){
				MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.between.error");
				return false;
			} else{
				Object filterValue2 = (""+filterValue).substring((""+filterValue).indexOf("|")+1);
				filterValue = (""+filterValue).substring(0, (""+filterValue).indexOf("|"));
				
				if(!NumericUtil.isInt(filterValue)){
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue);
					return false;
				} else if(!NumericUtil.isInt(filterValue2)){
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue2);
					return false;
				}
				
				filterValue2 = NumericUtil.getIntegerOrDefault(filterValue2);
				filterValue = NumericUtil.getIntegerOrDefault(filterValue);

				// Add keys to filter map for request				
				queryCriterion.put("filter_" + newKey+"_1", filterValue);
				queryCriterion.put("filter_" + newKey+"_2", filterValue2);
				
				if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition)){
					sbTemp.append("( " + key + " > :filter_" + newKey+ "_1 and " + key + " < :filter_" + newKey+"_2 )");									
				} else{
					sbTemp.append("( " + key + " >= :filter_" + newKey+ "_1 and " + key + " <= :filter_" + newKey+"_2 )");
				}
			}
		} else if(!NumericUtil.isInt(filterValue)){
			MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.numeric.error", ""+filterValue);
			return false;
		} else{
			filterValue = NumericUtil.getIntegerOrDefault(filterValue);
			sbTemp.append(key + " " + condition + " :" + "filter_" + newKey+ " ");
			queryCriterion.put("filter_" + newKey, filterValue);
		}
		
		return true;
	}

	/**
	 * @param sbTemp
	 * @param filterValue
	 * @param condition
	 * @param key
	 * @return
	 */
	private boolean getDateCondition(StringBuilder sbTemp, Object filterValue, String condition, String key){
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		Map<String, Object> filterCriterionMap = this.queryTableBean.getFilterCriteria();
		Object filterValue2 = filterCriterionMap.get(key+"_to");
		
		String newKey = key.replace('.', '_');
		
		if("__/__/____".equals(""+filterValue) || "__/__/____ | __/__/____".equals(""+filterValue)){
			return false;
		}
		
		if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition) || QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN_EQUALS.getCode().equals(condition)){
			if(StringUtil.isEmpty(filterValue) && StringUtil.isEmpty(filterValue2)){
				MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.between.error");
				return false;
			} else{
				if (!DateUtil.isDate(""+filterValue)) {
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.date.error");
					return false;
				} else if (!DateUtil.isDate(""+filterValue2)) {
					MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.date.error");
					return false;
				}
				
				filterValue2 = DateUtil.stringToDate(""+filterValue2);
				filterValue = DateUtil.stringToDate(""+filterValue);
	
				// Add keys to filter map for request				
				queryCriterion.put("filter_" + newKey+"_1", filterValue);
				queryCriterion.put("filter_" + newKey+"_2", filterValue2);
				
				if(QUERY_CONDITIONS.NUMERIC_CONDITION_BETWEEN.getCode().equals(condition)){
					sbTemp.append("( " + key + " > :filter_" + newKey+ "_1 and " + key + " < :filter_" + newKey+"_2 )");									
				} else{
					sbTemp.append("( " + key + " >= :filter_" + newKey+ "_1 and " + key + " <= :filter_" + newKey+"_2 )");
				}
			}
		} else if (!DateUtil.isDate(""+filterValue)) {
			MessageService.addDialogMessageKey(MSG_TYPE.ERROR, "work.date.error", ""+filterValue);
			return false;
		} else{
			filterValue = DateUtil.stringToDate(""+filterValue);
			sbTemp.append(key + " " + condition + " :" + "filter_" + newKey+ " ");
			queryCriterion.put("filter_" + newKey, filterValue);
		}
		
		return true;
	}
	
	/**
	 * @param sbTemp
	 * @param filterValue
	 * @param condition
	 * @param key
	 * @return
	 */
	private boolean getBooleanCondition(StringBuilder sbTemp, Object filterValue, String condition, String key){
		String newKey = key.replace('.', '_');
		Map<String, Object> queryCriterion = this.queryTableBean.getQueryCriteria();
		
		filterValue = TypeValidatorService.validateType(TYPE_DATA_ENUM.BOOLEAN, key, ""+filterValue);
		boolean isFalse = StringUtil.isFalseOrNull(""+filterValue);
		
		//
		if(isFalse){
			sbTemp.append("(");
		}
		sbTemp.append(key + " " + condition + ":filter_" + newKey+ " ");
		//
		if(isFalse){
			sbTemp.append(" or " + key + " IS NULL)");
		}
		
		queryCriterion.put("filter_" + newKey, filterValue);
		return true;
	}
}
