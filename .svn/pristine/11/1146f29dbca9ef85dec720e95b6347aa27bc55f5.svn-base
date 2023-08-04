package framework.component.complex.table;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import framework.component.Component;
import framework.component.ComponentUtil;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

@SuppressWarnings("serial")
public class TrTag extends Component {

	private String type;
	private String format;
	private String workId;
	private int idxTd = 0;
	private String align;
	private String checkable;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		writeStartComponent();
		
		return EVAL_BODY_INCLUDE;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		writeEndComponent();
		
		releaseTr();
		
		return EVAL_PAGE;
	}
	
	/**
	 * 
	 */
	private void releaseTr(){
		this.type = null;
		this.format = null;
		this.workId = null;
		this.idxTd = 0;
		this.align = null;
		
		super.release();
	}

	/**
	 * 
	 */
	public void writeStartComponent() throws JspException {
		TableTag parentTable = (TableTag) findAncestorWithClass(this, TableTag.class);
		StringBuilder sb = new StringBuilder();
		idxTd = 0;
		int idx = parentTable.getIdxAlternate();
		parentTable.setIdxAlternate((idx+1));
		//
		if(align != null){
			align = " align='"+align+"'";
		}
		//
		sb.append("<tr " + (StringUtil.isTrue(parentTable.getDragable()) ? " drg=\""+EncryptionUtil.encrypt(workId)+"\" ":"") + getFullComponentAttrubutes(null) + " " +  StringUtil.getValueOrEmpty(align)+ ">\n");
		//
		if(this.idxTd == 0){
			String tableName = parentTable.getName();
			HttpServletRequest request = getGuiOrContextHttpRequest();
			if(StringUtil.isTrueOrNull(parentTable.getCheckable()) && !ComponentUtil.isReadOnlyFormSetted(request)){
				if(StringUtil.isTrueOrNull(this.checkable)){
					String align = " align='center'";
					// Add global checkbox
					sb.append("<td" + getFullComponentAttrubutes(null) + align + ">\n");
					
					// If actions are not conditioned
					String workId = getWorkId();
					sb.append("<input type='checkbox' class='colored-success' value='" + workId + "'");
					//
					Map<String, Object> params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
					String check = (String)params.get(tableName + "_" + ProjectConstante.CHECK_SAVE_STR);
					List<String> listElements = StringUtil.getElementsList(check, "|", false);
					if(listElements != null){
						if(listElements.contains(workId)){
							sb.append(" checked='checked'");
						}
					}
					sb.append("/>\n" +
							"</td>\n");
				} else{
					sb.append("<td" + getFullComponentAttrubutes(null) + align + "></td>\n");
				}
			}
		}
		
		ComponentUtil.writeComponent(pageContext, sb);
	}

	/**
	 * 
	 */
	public void writeEndComponent() throws JspException {
		ComponentUtil.writeComponent(pageContext, "</tr>\n");
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	public int getIdxTd() {
		return idxTd;
	}

	public void setIdxTd(int idxTd) {
		this.idxTd = idxTd;
	}

	/**
	 * @return the workId
	 */
	public String getWorkId() {
		return workId;
	}

	/**
	 * @param workId the workId to set
	 */
	public void setWorkId(String workId) {
		this.workId = workId;
	}

	/**
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}
	public String getCheckable() {
		return checkable;
	}

	public void setCheckable(String checkable) {
		this.checkable = checkable;
	}
}
