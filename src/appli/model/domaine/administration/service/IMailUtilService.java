package appli.model.domaine.administration.service;

import appli.model.domaine.administration.persistant.MailQueuePersistant;

public interface IMailUtilService {
	void addMailToQueue(MailQueuePersistant mail);
	void sendAllMails();
	boolean sendMessageMail(MailQueuePersistant mail);
}
