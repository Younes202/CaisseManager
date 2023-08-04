package framework.component.box;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import framework.component.ComponentUtil;
import framework.controller.bean.message.DialogMessageBean;
import framework.model.common.service.MessageIdsService;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;

@SuppressWarnings("serial")
public class DialogBoxAutoOpen extends TagSupport{
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		List<DialogMessageBean> listMessage = MessageService.getListDialogMessageBean();
		//
		boolean isMsgExist = (listMessage != null) && (listMessage.size() > 0);
		if(isMsgExist){
			ComponentUtil.writeComponent(pageContext, getDialogueBox());
		} else{
			// Clear all ids
			MessageIdsService.clearQuestionAction();
			MessageIdsService.clearConfirmIds();
		}

		return SKIP_BODY;
	}

	/**
	 * @return
	 * @throws JspException
	 */
	public String getDialogueBox() throws JspException {
		List<DialogMessageBean> cloneListMessages = null;
		StringBuilder sb = new StringBuilder();
		String sbMutilple = "";
		String classStyle = "error";
		String title = "Alertes";

		List<DialogMessageBean> listMessage = MessageService.getListDialogMessageBean();
		cloneListMessages = new ArrayList<DialogMessageBean>(listMessage);
		//
		if(cloneListMessages != null){
			for(DialogMessageBean msgBean : cloneListMessages){
				switch(msgBean.getType()){
					case INFO :{
						title = StrimUtil.label("work.info");
						classStyle = "info";
					}; break;
					case SUCCES :{
						title = StrimUtil.label("work.succes");
						classStyle = "succes";
					}; break;
					case WARNING :{
						title = StrimUtil.label("work.warning");
						classStyle = "warning";
					}; break;
					default:{
						title = StrimUtil.label("work.info");
						classStyle = "info";
					}; break;
				}
				sbMutilple = sbMutilple + "<p style='line-height: 20px;'>" +
						"<span class='" + classStyle + "'>" + msgBean.getMessage() + "</span></p>";
			}
		}
		// 
		sb.append(ComponentUtil.getJavascriptBloc(ComponentUtil.getOnReadyScript("buildErrorDialog(\""+sbMutilple+"\", \""+title+"\");"))); 

		// Clear all ids
		MessageIdsService.clearQuestionAction();
		MessageIdsService.clearConfirmIds();

		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		release();

		return EVAL_PAGE;
	}
}
