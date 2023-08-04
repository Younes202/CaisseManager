package framework.component.complex.table;

import java.util.List;

import framework.controller.bean.action.IViewBean;

public class RowBean {
	private String id;
	private List<CellBean> listCells;
	private IViewBean bean;
	
	/**
	 * @return the bean
	 */
	public IViewBean getBean() {
		return bean;
	}
	/**
	 * @param bean the bean to set
	 */
	public void setBean(IViewBean bean) {
		this.bean = bean;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the listCells
	 */
	public List<CellBean> getListCells() {
		return listCells;
	}
	/**
	 * @param listCells the listCells to set
	 */
	public void setListCells(List<CellBean> listCells) {
		this.listCells = listCells;
	}
	
	public CellBean getCell(String id){
		if(this.listCells != null){
			for(CellBean cellBean : listCells){
				if(cellBean.getColumnId().equals(id)){
					return cellBean;
				}
			}
		}
		
		return null;
	}
}
