package framework.model.beanContext;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "restaurant_ouverture", indexes={
		@Index(name="IDX_ETS_OUV_FUNC", columnList="code_func")
	})
public class EtablissementOuverturePersistant extends BasePersistant {
    @Column(length = 250)
    private String jour_ouverture;
    
    @Column(length = 5)
    private String heure_debut_matin;

    @Column(length = 5)
    private String heure_fin_matin;

    @Column(length = 5)
    private String heure_debut_midi;

    @Column(length = 5)
    private String heure_fin_midi;
    
    public String getJour_ouverture() {
		return jour_ouverture;
	}
	public void setJour_ouverture(String jour_ouverture) {
		this.jour_ouverture = jour_ouverture;
	}

	public String getHeure_debut_matin() {
		return heure_debut_matin;
	}

	public void setHeure_debut_matin(String heure_debut_matin) {
		this.heure_debut_matin = heure_debut_matin;
	}

	public String getHeure_fin_matin() {
		return heure_fin_matin;
	}

	public void setHeure_fin_matin(String heure_fin_matin) {
		this.heure_fin_matin = heure_fin_matin;
	}

	public String getHeure_debut_midi() {
		return heure_debut_midi;
	}

	public void setHeure_debut_midi(String heure_debut_midi) {
		this.heure_debut_midi = heure_debut_midi;
	}

	public String getHeure_fin_midi() {
		return heure_fin_midi;
	}

	public void setHeure_fin_midi(String heure_fin_midi) {
		this.heure_fin_midi = heure_fin_midi;
	}
}
