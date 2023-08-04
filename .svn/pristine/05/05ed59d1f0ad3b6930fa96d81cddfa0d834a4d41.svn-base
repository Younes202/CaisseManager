package framework.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public class Upload extends ComponentBase{
	  private String extensions;
	  private String maxConnection;
	  private String multipleSelection;
	  private String maxSize;   
	  private String minSize;
	  private String oneFile;
	  private String uniqueFile;
	  private String linkAction;

    /* (non-Javadoc)
     * @see front.component.Component#releaseAll()
     */
    @Override
    public void releaseAll() {
  	  	this.extensions = null;
  	  	this.maxConnection = null;
  		this.multipleSelection = null;
  		this.maxSize = null;
  		this.minSize = null;
  		this.uniqueFile = null;
  		this.oneFile = null;
  		this.linkAction = null;
    }

	/**
	 * Add label if read only
	 * @return
	 */
	private boolean writeReadOnlyComponent(){
//		String defaultClass = "inputROnly";
		boolean isReadOnly = super.isReadOnlyAttributeForm();
		//
		if(isReadOnly){
	//		String label = "<label " + getFullClassStyle(defaultClass) + ">" + getValueSt().replaceAll(";", "<br>") + "</label>\n" + getHiddenValues();
			
			//
			ComponentUtil.writeComponent(getContextOrJspContext(), getHiddenValues(true));
			return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	private String getHiddenValues(boolean isReadOnly) {
		StringBuilder label = new StringBuilder();
		
		if(StringUtil.isNotEmpty(getValueSt())){
			label.append("<ul class=\"qq-upload-list\">\n");
			
			String[] piecesJointes = null;
			if(getValue() instanceof String){
				piecesJointes = new String[1];
				piecesJointes[0] = (String)getValue();	
			} else{
				piecesJointes = (String[])getValue();
			}
			
//			String[] piecesJointes = (String[])getValue(); //StringUtil.getArrayFromStringDelim(getValueSt(), ";");
			for(String val : piecesJointes){
				label.append("<li class=\"qq-upload-success\">\n" +
				"<span class=\"qq-upload-file\"><a href=\"javascript:\" onClick=\"submitWorkForm('"+EncryptionUtil.encrypt(this.linkAction)+"', 'uploaded_name="+val+"');\">"+val+"</a></span>\n" +
				"<input type=\"hidden\" id=\""+getName()+"_"+val+"_lispan\" name=\""+getName()+"[]\" class=\"qq-real-upload\" value=\""+val+"\">\n");
				
				if(!isReadOnly){
					label.append("<a class=\"qq-upload-del\" href=\"#\" style=\"\" onClick=\"deleteFromFolder('"+getName()+"', '"+val+"');\">Supprimer</a>\n");
				}
				label.append("</li>\n"); 
			}
			
			label.append("</ul>\n");
		}
		
		return label.toString();
	}

    /* (non-Javadoc)
     * @see front.component.Component#doBeforStartComponent()
     */
    @Override
    public void doBeforStartComponent() throws JspException {
    	setType("string");
    	setType(getType() + "[]");
    }
    
    /* (non-Javadoc)
	 * @see front.component.Component#writeEndComponent()
	 */
	public void writeEndComponent() throws JspException{
		
	}
	
	/* (non-Javadoc)
	 * @see front.component.ComponentBase#doAfterEndComponent()
	 */
	@Override
	public void doAfterEndComponent() throws JspException {

	}
	
	/* (non-Javadoc)
	 * @see front.component.Component#writeStartComponent()
	 */
	public void writeStartComponent() throws JspException{
		// Write label if is read only
		if(writeReadOnlyComponent()) return;
		
		Form parentForm = (Form) findAncestorWithClass(this, Form.class);
		StringBuilder sb = new StringBuilder();
		HttpServletRequest request = getGuiOrContextHttpRequest();
		int randomId = NumericUtil.getRandomNumber(1,100);
		String uploadName = "file_upload_"+randomId;
		String hiddenUploadName = "hide_upload_"+randomId;
		
		// Init string builder in form tag
		StringBuilder sbScript = parentForm.getSbScript();
		
		// Initialize
		if(StringUtil.isEmpty(maxConnection)){
			maxConnection = "1";
		}
		if(StringUtil.isEmpty(multipleSelection)){
			multipleSelection = "false";
		}
		if(StringUtil.isEmpty(maxSize)){
			maxSize = "0";
		}
		if(StringUtil.isEmpty(minSize)){
			minSize = "0";
		}
		//if(StringUtil.isEmpty(uniqueFile)){
		//	uniqueFile = "true";
		//}
		if(StringUtil.isNotEmpty(extensions)){
			String[] tab = StringUtil.getArrayFromStringDelim(extensions, ",");
			String newExtension = "";
			for(int i=0; i<tab.length; i++){
				newExtension += "'"+tab[i]+"'" + ((i+1<tab.length) ? ",":"");
			}
			extensions = newExtension;
		}
		// Function
		//sbScript.append("function createUploader_"+randomId+"(){\n" +            
		sbScript.append("var uploader_"+randomId+" = new qq.FileUploader({\n" +
	            "element: document.getElementById('"+uploadName+"'),\n" +
	            "action: 'front?"+ProjectConstante.IS_AJAX_BACK_JOB_CALL+"=true&methode=uploadFile&fieldName="+getName()+"',\n");
				if(StringUtil.isNotEmpty(extensions)){
					sbScript.append("allowedExtensions: ["+extensions+"],\n");
				}
				sbScript.append("maxConnections: "+maxConnection+",\n" +
	            "multiple: "+multipleSelection+",\n" +
	            //"params: {\n" +
	            	//param1: 'value1',
	           // "},\n" +
	            "fieldName: '"+getName()+"',\n" +
	            "sizeLimit: "+maxSize+",\n" +   
	            "minSizeLimit: "+minSize+",\n" +
	            "debug: true,\n" +
	            "onSubmit: function(id, fileName){\n");
					if(StringUtil.isTrue(oneFile)){
		          		// Show message if unique
						sbScript.append("if(document.getElementById(\""+hiddenUploadName+"\")){\n" +
							"openUploadDialog(\"Un seule fichier est autorisé pour le téléchargement !\");\n" +
							"return false;\n" +
						"}\n" +
	            		// Add hidden field
	            		"$(\"#body_form\").append(\"<input type='hidden' name='"+hiddenUploadName+"' id='"+hiddenUploadName+"'/>\");\n");
					}
					
					if(StringUtil.isTrue(uniqueFile)){
						sbScript.append("if(isUploadedFileExist(fileName)){\n" +
								"openUploadDialog(\"Ce fichier est dèja dans la liste des fichiers téléchargés !\");\n" +
								"return false;\n" +
								"}\n");
					}
					
				sbScript.append("}\n" +
	        "});\n");
			// Input file
			sb.append("<div id=\""+uploadName+"\" style=\"padding-bottom:5px;\"></div>\n");
			// Test if dialog box exist
			if(request.getAttribute("upload-dialog") == null){
				// Dialog message		
				sb.append("<div id=\"dialog\" title=\"Basic dialog\" style=\"display: none;\">\n"+
					"<span id='upload-msg'></span>\n"+
				"</div>\n");
				//
				request.setAttribute("upload-dialog", "true");
			}	
			// Add old values
			sb.append(getHiddenValues(false));
			
			// Add hidden for convertion to String array
			sb.append("<input type=\"hidden\" name=\""+getName()+"[]\"/>\n");

			
		// Write
		ComponentUtil.writeComponent(pageContext, sb);
    }

	/**
	 * @return the extensions
	 */
	public String getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}

	/**
	 * @return the maxConnections
	 */
	public String getMaxConnection() {
		return maxConnection;
	}

	/**
	 * @param maxConnections the maxConnections to set
	 */
	public void setMaxConnections(String maxConnection) {
		this.maxConnection = maxConnection;
	}

	/**
	 * @return the multipleSelection
	 */
	public String getMultipleSelection() {
		return multipleSelection;
	}

	/**
	 * @param multipleSelection the multipleSelection to set
	 */
	public void setMultipleSelection(String multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	/**
	 * @return the maxSize
	 */
	public String getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * @return the minSize
	 */
	public String getMinSize() {
		return minSize;
	}

	/**
	 * @param minSize the minSize to set
	 */
	public void setMinSize(String minSize) {
		this.minSize = minSize;
	}

	/**
	 * @return the uniqueFile
	 */
	public String getUniqueFile() {
		return uniqueFile;
	}

	/**
	 * @param uniqueFile the uniqueFile to set
	 */
	public void setUniqueFile(String uniqueFile) {
		this.uniqueFile = uniqueFile;
	}

	public String getOneFile() {
		return oneFile;
	}

	public void setOneFile(String oneFile) {
		this.oneFile = oneFile;
	}

	public void setMaxConnection(String maxConnection) {
		this.maxConnection = maxConnection;
	}

	public String getLinkAction() {
		return linkAction;
	}

	public void setLinkAction(String linkAction) {
		this.linkAction = linkAction;
	}
	
	
}
