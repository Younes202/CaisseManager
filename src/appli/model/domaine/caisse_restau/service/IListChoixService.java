/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appli.model.domaine.caisse_restau.service;

import appli.controller.domaine.caisse_restau.bean.ListChoixBean;
import framework.model.service.IGenericJpaService;

/**
 *
 * @author hp
 */
public interface IListChoixService extends IGenericJpaService<ListChoixBean, Long> {
	void activerDesactiverElement(Long istChoixId);
	String genererCode();
}
