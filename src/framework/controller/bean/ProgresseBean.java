package framework.controller.bean;

public class ProgresseBean {
	private String innerProgressLabel;
	private String advanceLabel;
	private String logLabel;
	private String statut;
	private int percent;
	
	/**
	 * @return the advanceLabel
	 */
	public String getAdvanceLabel() {
		return advanceLabel;
	}
	
	
	/**
	 * @param advanceLabel the advanceLabel to set
	 */
	public void setAdvanceLabel(String advanceLabel) {
		this.advanceLabel = advanceLabel;
	}
	
	
	/**
	 * @return the logLabel
	 */
	public String getLogLabel() {
		return logLabel;
	}
	
	
	/**
	 * @param logLabel the logLabel to set
	 */
	public void setLogLabel(String logLabel) {
		this.logLabel = logLabel;
	}
	
	/**
	 * @return the percent
	 */
	public int getPercent() {
		return percent;
	}
	
	
	/**
	 * @param percent the percent to set
	 */
	public void setPercent(int percent) {
		this.percent = percent;
	}

	/**
	 * @return the statut
	 */
	public String getStatut() {
		return statut;
	}

	public String getInnerProgressLabel() {
		return innerProgressLabel;
	}


	public void setInnerProgressLabel(String innerProgressLabel) {
		this.innerProgressLabel = innerProgressLabel;
	}


	/**
	 * @param statut the statut to set
	 */
	public void setStatut(String statut) {
		this.statut = statut;
	}
}

