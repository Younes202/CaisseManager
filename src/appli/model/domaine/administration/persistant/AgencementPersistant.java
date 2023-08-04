package appli.model.domaine.administration.persistant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import framework.model.beanContext.BasePersistant;

@Entity
@Table(name = "agencement", indexes={
		@Index(name="IDX_AGN_FUNC", columnList="code_func")
	})
@NamedQuery(name="agencement_find", query="from AgencementPersistant agencement order by agencement.emplacement")
public class AgencementPersistant extends BasePersistant {
	@Column(length = 120, nullable=false)
	private String emplacement;
	
	@Column
	@Lob
	private String table_coords;
	@Column
	private Boolean is_calendrier;
	@Column
	@Lob
	private String limite_coords;

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	public String getTable_coords() {
		return table_coords;
	}

	public void setTable_coords(String table_coords) {
		this.table_coords = table_coords;
	}

	public String getLimite_coords() {
		return limite_coords;
	}

	public void setLimite_coords(String limite_coords) {
		this.limite_coords = limite_coords;
	}

	public Boolean getIs_calendrier() {
		return is_calendrier;
	}

	public void setIs_calendrier(Boolean is_calendrier) {
		this.is_calendrier = is_calendrier;
	}
}
