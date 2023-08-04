package appli.model.domaine.administration.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.personnel.bean.ClientBean;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.validator.ClientValidator;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.dao.IClientDao;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ClientValidator.class)
@Named
public class ClientService extends GenericJpaService<ClientBean, Long> implements IClientService{
	@Inject
	private IClientDao clientDao;
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long clientId) {
		ClientPersistant clientPersistant = clientDao.findById(clientId);
		clientPersistant.setIs_disable(BooleanUtil.isTrue(clientPersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(clientPersistant);
	}
	
	@Override
	public String generateNum() {
		Query query = getNativeQuery("select max(CAST(numero AS UNSIGNED)) from client");
		BigInteger max_num = (BigInteger)query.getSingleResult();
		if(max_num != null){
			return "0000"+(max_num.intValue()+1);
		} else{
			return "00001";
		}
	}

	@Override
	public List<ClientPersistant> getClientsActifs() {
		List<ClientPersistant> listClient = getQuery("from ClientPersistant where is_disable is null or is_disable=0 "
				+ "order by nom")
					.getResultList();
		
		return listClient;
	}
	
	@Override
	public List<ClientPersistant> findByCin(String cin) {
		List<ClientPersistant> listClient = getQuery("from ClientPersistant where cin like :cin")
						.setParameter("cin", "%"+cin+"%")
						.setMaxResults(10)
						.getResultList();
		
		return listClient;
	}
	@Override
	public List<ClientPersistant> findByPhone(String phone) {
		List<ClientPersistant> listClient = getQuery("from ClientPersistant where telephone like :phone")
						.setParameter("phone", "%"+phone+"%")
						.setMaxResults(10)
						.getResultList();
		
		return listClient;
	}

	@Override
	public List<ClientPersistant> findByNom(String nom) {
		List<ClientPersistant> listClient = getQuery("from ClientPersistant where nom like :nom")
				.setParameter("nom", nom+"%")
				.setMaxResults(10)
				.getResultList();

		return listClient;
	}
	
	/* (non-Javadoc)
	 * @see model.hibernate.service.user.IToto#getUserByLogingAndPw(java.lang.String, java.lang.String)
	 */
	@Override
	public ClientBean getClientByLoginAndPw(String login, String pw) { 
		ClientPersistant clientPersistant = clientDao.getClientByLoginAndPw(login, pw);

		return (ClientBean) ServiceUtil.persistantToBean(ClientBean.class, clientPersistant);
	}
	
	@Override
	public ClientPersistant getClientByCodeBarre(String codeBarre) {
		return (ClientPersistant) getSingleResult(getQuery("from ClientPersistant client "
				+ "where client.code_barre=:codeBarre")
				.setParameter("codeBarre", codeBarre.trim())
		);
	}
	
	@Override
	@Transactional
	public void addPortefeuille(Long clientId, boolean isSoldeNeg, BigDecimal mttPalafond, BigDecimal taux) { 
		ClientPersistant clientB = findById(clientId);
		clientB.setIs_solde_neg(isSoldeNeg);
		clientB.setTaux_portefeuille(taux);
		clientB.setPlafond_dette(mttPalafond);
		clientB.setSolde_portefeuille(BigDecimalUtil.ZERO);
		
		getEntityManager().merge(clientB);
	}

	@Override
	public ClientPersistant getClientByNumero(String numero) {
		return (ClientPersistant) getSingleResult(getQuery("from ClientPersistant where numero=:numero").setParameter("numero", numero));
	}
	
	@Override
	public Object[] detailEtatClient(Long clientId) {
		List<MouvementPersistant> listFourn = getQuery("from MouvementPersistant "
				+ "where opc_client.id=:clientId and type_mvmnt='v' "
				+ "order by date_mouvement desc")
				.setParameter("clientId", clientId)
				.setMaxResults(100)
				.getResultList();
		
		List<PaiementPersistant> listPaiement = getQuery("from PaiementPersistant "
				+ "where opc_client.id=:clientId and date_encaissement is not null "
				+ "and source='VENTE' "
				+ "order by date_mouvement desc")
				.setParameter("clientId", clientId)
				.setMaxResults(100)
				.getResultList();
		
		return new Object[]{listFourn, listPaiement};
	}
	
	@Override
	public void affecterEtatClient(ClientPersistant clientP) {
		String req = "select client.id, nonpaye.mtt_nonpaye, paye.mtt_paye, m.mtt_mvm from client client "
				+ "left join ("
				+ "		select sum(pai.montant) as mtt_nonpaye, pai.client_id as client_id "
				+ "from paiement pai "
				+ "		where pai.date_encaissement is null and pai.source='VENTE' "
				+ "		group by pai.client_id"
				+ "		) nonpaye on nonpaye.client_id=client.id "
				+ "left join ("
				+ "		select sum(pai.montant) as mtt_paye, pai.client_id as client_id from paiement pai "
				+ "		where pai.date_encaissement is not null and pai.source='VENTE' "
				+ "		group by pai.client_id"
				+ "		) paye on paye.client_id=client.id "
				+ "left join ("
				+ "		select sum(mvm.montant_ttc) as mtt_mvm, mvm.client_id as client_id from mouvement mvm "
				+ "		where mvm.type_mvmnt='v' group by mvm.client_id "
				+ "		) m on m.client_id=client.id where client.id=:clientId";
		
		List<Object[]> list_result = getNativeQuery(req)
				.setParameter("clientId", clientP.getId())
				.getResultList();
		
		for (Object[] object : list_result) {
			clientP.setMtt_non_paye((BigDecimal) object[1]);
			clientP.setMtt_paye((BigDecimal) object[2]);
			clientP.setMtt_total((BigDecimal) object[3]);
		}
	}

	@Override
	public BigDecimal getSituationDetteClient(Long clientId) {
		BigDecimal mttPaiement = (BigDecimal) getSingleResult(getQuery("select sum(montant) from PaiementPersistant paiement "
				+ "where paiement.date_encaissement is null "
				+ "and opc_client.id is not null and opc_client.id=:clientId")
				.setParameter("clientId", clientId)
				.setParameter("dateRef", new Date())
			);
		
		return mttPaiement;
	}

	@Override
	public List<PaiementPersistant> getEcheancePasseClient(Long clientId) {
		List<PaiementPersistant> listPaiement = getQuery("from PaiementPersistant paiement "
				+ "where paiement.date_echeance is not null "
				+ "and paiement.date_encaissement is null "
				+ "and paiement.date_echeance>:dateRef"
				+ "and opc_client.id is not null and opc_client.id=:clientId")
				.setParameter("clientId", clientId)
				.setParameter("dateRef", new Date())
			.getResultList();
		
		return listPaiement;
	}
}
