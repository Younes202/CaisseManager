package framework.component.facade;

import javax.servlet.http.HttpServletRequest;

public interface UI_IComponent {
	public void setUIName(String name);
	public void setRequestGui(HttpServletRequest requestGui);
}
