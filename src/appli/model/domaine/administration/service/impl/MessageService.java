package appli.model.domaine.administration.service.impl;

import javax.inject.Named;

import appli.controller.domaine.administration.bean.MessageBean;
import appli.model.domaine.administration.service.IMessageService;
import appli.model.domaine.administration.validator.ClientValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ClientValidator.class)
@Named
public class MessageService extends GenericJpaService<MessageBean, Long> implements IMessageService {
//	@Inject
//	private IMessageDao messageDao;
//	@Inject
//	private IMailUtilService mailService;
//
//	@Override
//	@Transactional
//	public void repliqueMail(MailQueuePersistant mail, String messageBrut) {
//		final String BASE_PATH = StrimUtil.getGlobalConfigPropertie("file.upload.path");
//		EntityManager entityManager = getEntityManager();
//		
//		// Creation du message
//		MessagePersistant msgPersistant = new MessagePersistant();
//		//
//		String attachementPth = BASE_PATH + "/" + ContextAppli.getAbonneBean().getId()+"/" + ContextGloabalAppli.getEtablissementBean().getId() + "/" + mail.getPieces_path();
//		if(StringUtil.isNotEmpty(mail.getPieces_path())){
//	 		File folder = new File(attachementPth);
//			if(folder.listFiles() != null && folder.listFiles().length > 0){
//				msgPersistant.setIs_piece_jointe(1);
//			} else{
//				msgPersistant.setIs_piece_jointe(0);
//			}
//		}
//		// Msg
//		if(StringUtil.isEmpty(messageBrut)){
//			msgPersistant.setText("");
//		} else{
//			msgPersistant.setText(messageBrut+"<br><br>Votre syndic");	
//		}
//		// Sujet
//		if(StringUtil.isEmpty(mail.getSujet())){
//			msgPersistant.setSujet("");
//		} else{
//			msgPersistant.setSujet(mail.getSujet());
//		}
//		msgPersistant.setDate_envoi(new Date());
//		msgPersistant.setOpc_etablissement(ContextAppli.getEtablissementBean());
//		
//		msgPersistant = entityManager.merge(msgPersistant);
//		
//		// Copier les pieces vers le repertoire des messages
//		if(StringUtil.isNotEmpty(mail.getPieces_path())){File folder = new File(attachementPth);
//			if(folder.listFiles() != null && folder.listFiles().length > 0){
//				String messagePath = BASE_PATH + "/" + ContextAppli.getAbonneBean().getId()+"/" + ContextAppli.getEtablissementBean().getId() + "/message/";
//				for (final File file : folder.listFiles()) {
//					File newFile = new File(messagePath+"/"+msgPersistant.getId());
//					if(!newFile.isFile()){
//						newFile.mkdirs();
//					}
//					try{
//						newFile = new File(messagePath+"/"+msgPersistant.getId()+"/"+file.getName());
//						newFile.createNewFile();
//						InputStream targetStream = new FileInputStream(file); 
//						Files.copy(targetStream, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//					} catch(Exception e){
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		
//		String[] dests = StringUtil.getArrayFromStringDelim(mail.getDestinataires(), ";");
//		if(dests != null) {
//			for (String mailDest : dests) {
//				UserPersistant userPersistant = (UserPersistant) messageDao.getSingleResult(entityManager.createQuery("from UserPersistant where mail=:mail")
//						.setParameter("mail", mailDest)
//						);
//				if(userPersistant == null) {
//					continue;
//				}
//				MessageDetailPersistant msgRecu = new MessageDetailPersistant();
//				msgRecu.setOpc_etablissement(ContextAppli.getEtablissementBean());
//				msgRecu.setOpc_destinataire(userPersistant);
//				msgRecu.setOpc_expediteur(ContextAppli.getUserBean());
//				msgRecu.setOpc_message(msgPersistant);
//				msgRecu.setMsg_type("R");
//				//
//				entityManager.merge(msgRecu);
//			}
//		}
//	}
//	
//	@Override
//	@Transactional
//	public void create(MessagePersistant messageDetailBean) {
//		boolean isAdmin = StrimUtil.getGlobalConfigPropertie("admin.user").equals(ContextAppli.getUserBean().getMail());
//		UserBean userBean = ContextAppli.getUserBean();
//		EntityManager entityManager = getEntityManager();
//		final Long[] destUserIds = messageDetailBean.getDestinataires();
//		
//		// Creation du message
//		MessagePersistant msgPersistant = messageDetailBean.getOpc_message(); 
//		msgPersistant.setDate_envoi(new Date());
//		msgPersistant.setOpc_etablissement(ContextAppli.getEtablissementBean());
//		
//		if(messageDetailBean.getPiecesJointes() != null && messageDetailBean.getPiecesJointes().length > 0){
//			msgPersistant.setIs_piece_jointe(1);
//		}
//		
//		msgPersistant = entityManager.merge(msgPersistant);
//		
//		// Insertion dans les messages envoyés
//		MessagePersistant msgEnvoye = new MessagePersistant();
//		msgEnvoye.setOpc_etablissement(ContextAppli.getEtablissementBean());
//		//
//		if(isAdmin){
//			userBean = null;
//		}
//		msgEnvoye.setOpc_expediteur(userBean);
//		msgEnvoye.setOpc_message(msgPersistant);
//		msgEnvoye.setMsg_type("E");
//		//
//		entityManager.merge(msgEnvoye);
//		
//		if(destUserIds != null){
//			for(Long id : destUserIds){
//				MessagePersistant msgRecu = new MessagePersistant();
//				msgRecu.setOpc_etablissement(ContextAppli.getEtablissementBean());
//				msgRecu.setOpc_destinataire((UserPersistant) entityManager.find(UserPersistant.class, id));
//				msgRecu.setOpc_expediteur(userBean);
//				msgRecu.setOpc_message(msgPersistant);
//				msgRecu.setMsg_type("R");
//				//
//				entityManager.merge(msgRecu);
//			}
//		}
//		
//		final MessagePersistant opc_message = msgPersistant;
//		
//		if(StringUtil.isTrue(""+opc_message.getIs_mail())){
//			// Envoi d'une copie par mail
//			if(destUserIds != null){
//				for(Long id : destUserIds){
//					if(userBean != null && id.equals(userBean.getId())){
//						continue;
//					}
//					UserPersistant userPersistant = (UserPersistant) entityManager.find(UserPersistant.class, id);
//					
//					MailQueuePersistant mail = new MailQueuePersistant();
//					mail.setOpc_etablissement(ContextAppli.getEtablissementBean());
//					mail.setSujet(opc_message.getSujet());
//					mail.setText(opc_message.getMessage());
//					mail.setExpediteur_nom(ContextAppli.getUserBean().getFname()+" "+ContextAppli.getUserBean().getSname());
//					mail.setMail_signature(ContextAppli.getUserBean().getFname()+" "+ContextAppli.getUserBean().getSname());
//					mail.setMail_expediteur(ContextAppli.getUserBean().getMail());
//					mail.setDestinataires(userPersistant.getMail());
//					mail.setPieces_path("message/"+opc_message.getId());
//					mail.setSource("MESSAGE");
//					mail.setOrigine_id(opc_message.getId());
//					mail.setDate_mail(new Date());
//					
//					mail = MailUtilService.getMailContent(mail,  true);
//					
//					mailService.addMailToQueue(mail);
//				}
//			}
//		}
//		// Pour le téléchargement des pièces jointes
//		messageDetailBean.setOpc_message(opc_message);
//	}
//	
//	@Override
//	@Transactional
//	public void saveChatMessage(MessagePersistant msgPersistant){
//		EntityManager entityManager = getGenriqueDao().getEntityManager();
//		Date date_mail = new Date();
//		// Sauvegarder message
//		msgPersistant = entityManager.merge(msgPersistant);
//		
//		// Insertion dans les messages envoyés
//		MessageDetailPersistant msgEnvoye = new MessageDetailPersistant();
//		msgEnvoye.setOpc_etablissement(ContextAppli.getEtablissementBean());
//		msgEnvoye.setOpc_expediteur(ContextAppli.getUserBean());
//		msgEnvoye.setOpc_message(msgPersistant);
//		msgEnvoye.setMsg_type("E");
//		//
//		entityManager.merge(msgEnvoye);
//		
//		// Insertion dans les messages reçus
//		MessageDetailPersistant msgRecu = new MessageDetailPersistant();
//		msgRecu.setOpc_etablissement(ContextAppli.getEtablissementBean());
//		msgRecu.setOpc_destinataire(null);
//		msgRecu.setOpc_expediteur(ContextAppli.getUserBean());
//		msgRecu.setOpc_message(msgPersistant);
//		msgRecu.setMsg_type("R");
//		//
//		entityManager.merge(msgRecu);
//		
//		
//		String msg = "Copropriete : "+ContextAppli.getEtablissementBean().getNom()+"<br>"
//		+ "Mail : "+ContextAppli.getUserBean().getMail()+"<br>"
//		+ "Login copro : "+ContextAppli.getUserBean().getLogin()+"<br><br>"
//		+ "Message : <br>"+msgPersistant.getMessage();
//		
//		// Envoi copy par mail au support
//		MailQueuePersistant mail = new MailQueuePersistant();
//		mail.setText(msg);
//		mail.setExpediteur_nom(ContextAppli.getUserBean().getLogin());
//		mail.setMail_signature(ContextAppli.getUserBean().getFname()+" "+ContextAppli.getUserBean().getSname());
//		mail.setMail_expediteur(ContextAppli.getUserBean().getMail());
//		mail.setDestinataires(StrimUtil.getGlobalConfigPropertie("mail.support"));
//		mail.setSujet("MESSAGE ESPACE GESTION : "+msgPersistant.getSujet());
//		mail.setOpc_etablissement(ContextAppli.getEtablissementBean());
//		mail.setSource("SUPPORT");
//		mail.setOrigine_id(msgPersistant.getId());
//		mail.setDate_mail(date_mail);
//		
//		mail = MailUtilService.getMailContent(mail,  true);
//		
//		mailService.addMailToQueue(mail);
//		
//		// Mail en copie mail temp
//		mail = new MailQueuePersistant();
//		mail.setText(msg);
//		mail.setExpediteur_nom(ContextAppli.getUserBean().getLogin());
//		mail.setMail_signature(ContextAppli.getUserBean().getFname()+" "+ContextAppli.getUserBean().getSname());
//		mail.setMail_expediteur(ContextAppli.getUserBean().getMail());
//		mail.setDestinataires(StrimUtil.getGlobalConfigPropertie("mail.copy"));
//		mail.setSujet("MESSAGE ESPACE GESTION : "+msgPersistant.getSujet());
//		mail.setOpc_etablissement(ContextAppli.getEtablissementBean());
//		mail.setSource("SUPPORT");
//		mail.setOrigine_id(msgPersistant.getId());
//		mail.setDate_mail(date_mail);
//		
//		mail = MailUtilService.getMailContent(mail,  true);
//		
//		mailService.addMailToQueue(mail);
//	}
//	
//	@SuppressWarnings("rawtypes")
//	@Override
//	public int getNbrMessageNonLu(){
//		if(ContextAppli.getUserBean() == null){
//			return 0;
//		}
//		boolean isAdmin = StrimUtil.getGlobalConfigPropertie("admin.user").equals(ContextAppli.getUserBean().getMail());
//		IGenericJpaDao genericDao = getGenriqueDao();
//		 
//		String requete = "select count(0) from MessageDetailPersistant message where";
//		if(isAdmin){
//			requete = requete + " message.opc_user_destinataire.id is null";
//		} else{
//			requete = requete + " message.opc_user_destinataire.id =:cop_id"; 
//		}
//		requete = requete + " and message.msg_type='R' "
//				+ " and (message.is_lu is null or message.is_lu = 0)";
//		 
//		Query query = genericDao.getQuery(requete);
//		if(!isAdmin){
//			query.setParameter("cop_id", ContextAppli.getUserBean().getId());
//		}
//		 
//		return Integer.valueOf((""+genericDao.getSingleResult(query)));
//	}
//
//	/* (non-Javadoc)
//	 * @see org.metier.domaine.adm.service.IMessageDetailService#marquerMessage(java.lang.Long, boolean)
//	 */
//	@Override
//	@Transactional
//	public void marquerMessage(Long[] messageId, boolean isLu) {
//		for (Long id : messageId) {
//			if(id != null){
//				MessageDetailPersistant messageDetail = findById(id);
//				messageDetail.setIs_lu(isLu ? 1 : 0);
//			
//				super.update(messageDetail);
//			}
//		}
//	}
//
//	@Override
//	@Transactional
//	public void marquerSupprime(Long messageDeatailId, boolean isSup) {
//		MessageDetailPersistant messageDetail = findById(messageDeatailId);
//		messageDetail.setIs_supprime(isSup ? 1 : 0);
//	
//		super.update(messageDetail);
//	}
//
//	@Override
//	@Transactional
//	public void rappelerMessage(Long messageDeatailId) {
//		MessagePersistant messageDetail = findById(MessageDetailPersistant.class, messageDeatailId);
//		if(messageDetail.getOpc_expediteur().getId().equals(ContextAppli.getUserBean().getId())){
//			messageDao.getQuery("delete from MessageDetailPersistant where opc_message.id=:msgId and msg_type=:type")
//				.setParameter("msgId", messageDetail.getOpc_message().getId())
//				.setParameter("type", "R")
//				.executeUpdate();
//			
//			MessageDetailPersistant msgDetail = (MessageDetailPersistant) messageDao.getQuery("from MessageDetailPersistant where opc_message.id=:msgId and msg_type=:type")
//				.setParameter("msgId", messageDetail.getOpc_message().getId())
//				.setParameter("type", "E")
//				.getSingleResult();
//			//
//			msgDetail.setIs_rappele(1);
//			msgDetail.setIs_rappele(1);
//			
//			messageDao.update(msgDetail, false);
//		}
//	}
}
