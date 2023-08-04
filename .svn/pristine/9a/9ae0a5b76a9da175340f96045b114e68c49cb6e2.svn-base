package framework.component.form.pluging;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.controller.ControllerUtil;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class FormValidator {

	private final static String [] VALIDATORS = {"alpha", "alphanum", "min", "max", "minlength", "maxlength", "email"};
	private final static String [] TYPES_DATA = {
						ProjectConstante.TYPE_DATA_ENUM.LONG.getType(),
						ProjectConstante.TYPE_DATA_ENUM.LONG_ARRAY.getType(),
						ProjectConstante.TYPE_DATA_ENUM.DECIMAL.getType(),
						ProjectConstante.TYPE_DATA_ENUM.DECIMAL_ARRAY.getType(),
						ProjectConstante.TYPE_DATA_ENUM.DATE.getType()
					};

	/**
	 * @param request
	 * @return
	 * @throws JspException
	 */
	public static String addValidatorScript(HttpServletRequest request, String formName) throws JspException{
		StringBuilder block = null;
		Map<String, Map<String, String>> mapValidators = ControllerUtil.getMapValidator(request);
		//------
		if((mapValidators != null ) && (mapValidators.size() > 0)){
			block = new StringBuilder();
			StringBuilder rulesBlock = new StringBuilder();
			StringBuilder messagesBlock = new StringBuilder();
			// Add bloc javascript validator
			block.append(
				"$(\"#"+formName+"\").validate({\n"); 

			rulesBlock.append("rules: {\n");
			messagesBlock.append("messages: {\n");
			// Boucle
			//----------------------------------------------------------------------------
			Map<String, Map<String, String>> mapTemp = new HashMap<String, Map<String, String>>();
			for(String fieldName : mapValidators.keySet()){
				Map<String, String> listValidators = mapValidators.get(fieldName);
				if(listValidators == null) {
					continue;
				}
				String req = listValidators.get(ProjectConstante.REQUIR);
				String min = listValidators.get(ProjectConstante.MIN);
				String max = listValidators.get(ProjectConstante.MAX);
				String minlength = listValidators.get(ProjectConstante.MINLENGTH);
				String maxlength = listValidators.get(ProjectConstante.MAXLENGTH);
				String valid = listValidators.get(ProjectConstante.VALID);
				String type = listValidators.get(ProjectConstante.TYPE);
				//
				if(StringUtil.isTrue(req) || (min != null) || (max != null) || (minlength != null) || (maxlength != null)
							|| (valid != null && StringUtil.contains(valid, VALIDATORS))
							|| (StringUtil.contains(type, TYPES_DATA))){
					mapTemp.put(fieldName, listValidators);
				}
			}
			int i = 0;
			for(String fieldName : mapTemp.keySet()){
				Map<String, String> listValidators = mapTemp.get(fieldName);

				String req = listValidators.get(ProjectConstante.REQUIR);
				String min = listValidators.get(ProjectConstante.MIN);
				String max = listValidators.get(ProjectConstante.MAX);
				String minlength = listValidators.get(ProjectConstante.MINLENGTH);
				String maxlength = listValidators.get(ProjectConstante.MAXLENGTH);
				String valid = listValidators.get(ProjectConstante.VALID);
				String type = listValidators.get(ProjectConstante.TYPE);
				String rulMinMaxBlock = "", rulMinMaxMsg = "";

				// Rules and messages block
				rulesBlock.append("\""+fieldName + "\": {\n");
				messagesBlock.append("\""+fieldName+ "\": {\n");
				// Required
				if(StringUtil.isTrue(req)){
					rulesBlock.append("required: true");
					messagesBlock.append("required: \""+StrimUtil.label("work.required.error")+"\"");
				}
				
				// On s'arrête uniquement au test de l'obligation pour les id
				if(!fieldName.endsWith(".id")){//---> Pour régler le problème des identifiant crypter et qui sont ainsi transformés en string
					// Range
					if(StringUtil.isNotEmpty(min) && StringUtil.isNotEmpty(max)){
						rulMinMaxBlock += "range: [" + min + ", " + max + "]";
						rulMinMaxMsg = "range: \""+StrimUtil.label("work.range.error", min, max)+"\"";
					} else{
						// Minimum
						if(StringUtil.isNotEmpty(min)){
							rulMinMaxBlock += "min: " + min;
							rulMinMaxMsg = "min: \""+StrimUtil.label("work.superieur.error", min)+"\"";
						}
						 // Maximum
						if(StringUtil.isNotEmpty(max)){
							if(min != null){rulMinMaxBlock+=",\n";rulMinMaxMsg+=",\n";};
							rulMinMaxBlock +="max: " + max;
							rulMinMaxMsg += "max: \""+StrimUtil.label("work.inferieur.error", max)+"\"";
						}
					}
					boolean isNumRul = (min != null) || (max != null);
					// Range Lenght
					if(StringUtil.isNotEmpty(minlength) && StringUtil.isNotEmpty(maxlength)){
						if(isNumRul){rulMinMaxBlock+=",\n";rulMinMaxMsg+=",\n";}
						rulMinMaxBlock += "rangelength: [" + minlength + ", " + maxlength + "]";
						rulMinMaxMsg = "rangelength: \""+StrimUtil.label("work.rangelength.error", minlength, maxlength)+"\"";
					} else{
						// Minimum length
						if(StringUtil.isNotEmpty(minlength)){
							if(isNumRul){rulMinMaxBlock+=",\n";rulMinMaxMsg+=",\n";}
							rulMinMaxBlock += "minlength: " + minlength;
							rulMinMaxMsg = "minlength: \""+StrimUtil.label("work.minlength.error", minlength)+"\"";
						}
						 // Maximum length
						if(StringUtil.isNotEmpty(maxlength)){
							if((minlength != null) || isNumRul){rulMinMaxBlock+=",\n";rulMinMaxMsg+=",\n";};
							rulMinMaxBlock +="maxlength: " + maxlength;
							rulMinMaxMsg += "maxlength: \""+StrimUtil.label("work.maxlength.error", maxlength)+"\"";
						}
					}
					// Rules and messages block ------------------------------------------
					isNumRul = StringUtil.isNotEmpty(min) || StringUtil.isNotEmpty(max) || StringUtil.isNotEmpty(minlength)
														  || StringUtil.isNotEmpty(maxlength);
					boolean isReq = StringUtil.isTrue(req);
					//--------------------------------------------Type-------------------------------------------------
					if(ProjectConstante.TYPE_DATA_ENUM.LONG.getType().equals(type) || ProjectConstante.TYPE_DATA_ENUM.LONG_ARRAY.getType().equals(type)){
						if(isReq){rulesBlock.append(",\n");messagesBlock.append(",\n");};
						rulesBlock.append("digits: true");
						messagesBlock.append("digits: \""+StrimUtil.label("work.field.numeric.error")+"\"");
						if(isNumRul){rulesBlock.append(",\n"+rulMinMaxBlock);messagesBlock.append(",\n"+rulMinMaxMsg);};
					}/* else if (ProjectConstante.TYPE_DATA_ENUM.DECIMAL.getType().equals(type) || ProjectConstante.TYPE_DATA_ENUM.DECIMAL_ARRAY.getType().equals(type)){
						if(isReq){rulesBlock.append(",\n");messagesBlock.append(",\n");};
						rulesBlock.append("number: true\n");
						messagesBlock.append("number: \""+StrimUtil.label("work.field.decimal.error")+"\"");
						if(isNumRul){rulesBlock.append(",\n"+rulMinMaxBlock);messagesBlock.append(",\n"+rulMinMaxMsg);};
					}*/ else if (ProjectConstante.TYPE_DATA_ENUM.DATE.getType().equals(type)){
						if(isReq){rulesBlock.append(",\n");messagesBlock.append(",\n");};
						rulesBlock.append("date: true\n");
						messagesBlock.append("date: \""+StrimUtil.label("work.date2.error")+"\"");
					} 
					//-------------------------------------------Validator-------------------------------------------
					else if ("alpha".equals(valid)){
						if(isReq){rulesBlock.append(",\n");messagesBlock.append(",\n");};
						rulesBlock.append("alpha: true\n");
						messagesBlock.append("alpha: \""+StrimUtil.label("work.alpha.error")+"\"");
					} else if ("alphanum".equals(valid)){
						if(isReq){rulesBlock.append(",\n");messagesBlock.append(",\n");};
						rulesBlock.append("alpha2: true\n");
						messagesBlock.append("alpha2: \""+StrimUtil.label("work.alpha.error")+"\"");
					} else if ("email".equals(valid)){
						if(isReq){rulesBlock.append(",\n");messagesBlock.append(",\n");};
						rulesBlock.append("email: true\n");
						messagesBlock.append("email: \""+StrimUtil.label("work.field.email.error")+"\"");
					}
				}
				//
				rulesBlock.append("\n}");
				messagesBlock.append("\n}");
				if(i < (mapTemp.size()-1)){
					rulesBlock.append(",\n");
					messagesBlock.append(",\n");
				}
				i++;
			}

			// End rules block
			rulesBlock.append("},\n");
			messagesBlock.append("}\n");
			// Add block rules and messages
			block.append(rulesBlock);
			block.append(messagesBlock);
			// End validator
			block.append(",errorPlacement : function(error, element) {"+
						"error.insertAfter(element);"+
				"	}	"
				+ "});\n");
		}

		return ((block != null) ? block.toString() : "");
	}
}
