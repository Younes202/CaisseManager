//package appli.controller.domaine.caisse;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.Type;
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//
//import appli.controller.domaine.administration.bean.UserBean;
//import appli.controller.domaine.personnel.bean.ClientBean;
//import appli.model.domaine.administration.persistant.UserPersistant;
//import appli.model.domaine.administration.service.IUserService;
//import appli.model.domaine.caisse.service.ICaisseMobileService;
//import appli.model.domaine.personnel.persistant.ClientPersistant;
//import appli.model.domaine.personnel.service.IClientService;
//import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
//import framework.controller.ControllerUtil;
//import framework.controller.FileUtilController;
//import framework.model.common.util.ControllerBeanUtil;
//import framework.model.common.util.DateUtil;
//import framework.model.common.util.ServiceUtil;
//import framework.model.common.util.StrimUtil;
//import framework.model.common.util.StringUtil;
//import framework.model.util.AnnotationExclusionStrategy;
//import framework.model.util.HibernateProxyTypeAdapter;
//
///**
// * Servlet implementation class PrintController
// */
//@WebServlet("/mobileController")
//public class MobileControllerOLD extends HttpServlet {
//	
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	@Override
//	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String param = getBody(request);
//		String urlCloud = StrimUtil.getGlobalConfigPropertie("caisse.cloud.url");
//		urlCloud = urlCloud.substring(0, urlCloud.lastIndexOf("/"));
//		
//		Gson gson = new GsonBuilder().create();
//		Type type = new TypeToken<Map<String, Object>>(){}.getType();
//		Map<String, Object> myMap = gson.fromJson(param, type);
//		
//		JsonObject json = new JsonObject();
//		
//		String act =  null;
//		if(request.getParameter("act") != null){
//			act = request.getParameter("act");
//		}
//		if(myMap != null){
//			act = ""+myMap.get("act");
//		}
//		
//		//System.out.println(" ------- "+act);
//		
//		IUserService userService = (IUserService) ServiceUtil.getBusinessBean(IUserService.class);
//		IClientService clientService = (IClientService) ServiceUtil.getBusinessBean(IClientService.class);
//		ICaisseMobileService mobileService = (ICaisseMobileService) ServiceUtil.getBusinessBean(ICaisseMobileService.class);
//		
//		response.setCharacterEncoding("UTF-8");
//		
//		if(act.equals("commander")){
//			String login = ""+myMap.get("login");
//			String pw = ""+myMap.get("pw");
//			UserPersistant userPersistant = userService.getUserByLoginAndPw(login, pw);
//			if(userPersistant == null || userPersistant.getIs_desactive()){
//				json.addProperty("KO", "KO");
//			} else {
//				if(StringUtil.isEmpty(userPersistant.getOpc_client().getStatut()) || userPersistant.getOpc_client().getStatut().equals("VAL")){
//					json.addProperty("OK", "OK");
//				}
//				else if(userPersistant.getOpc_client().getStatut().equals("REJ")){
//					json.addProperty("NonVal", "Votre compte est rejeté");
//				}
//				else{
//					json.addProperty("NonVal", "Votre compte n'est pas encore validé");
//				}
//			}
//	        response.getWriter().write(json.toString());
//	        return;
//		}
//		
//		if(act.equals("createCompte")){
//			
//			myMap.put("date_connexion", DateUtil.dateToString(new Date()));
//			UserBean userBean = (UserBean) ControllerBeanUtil.mapToBean(UserBean.class, myMap);
//			ClientBean clientBean = (ClientBean) ControllerBeanUtil.mapToBean(ClientBean.class, myMap);
//			String codeAuth = ""+myMap.get("code_authentification");
//			String type_appli = ""+myMap.get("type_appli");
//			
//			UserBean user = userService.getUserByLoginAndPw(userBean.getLogin(), userBean.getPassword());
//			if(user!=null){
//				mobileService.activerCompte(userBean.getLogin(), userBean.getPassword());
//				
//				try {
//					FileUtilController.callURL(urlCloud + "/mobileCtrl?act=activeCompte&login="+userBean.getLogin()+"&pw="+userBean.getPassword()
//						+"&codeAuth="+codeAuth+"&type_appli="+type_appli);
//				} catch(Exception e) {
//					System.out.println(e.getMessage());
//				}
//			}
//			else{
//				mobileService.createCompte(userBean, clientBean);
//				
//				FileUtilController.callURL(urlCloud + "/mobileCtrl?act=createCompteEtablissement&login="+userBean.getLogin()
//						+"&pw="+userBean.getPassword()+"&codeAuth="+codeAuth+"&type_appli="+type_appli);
//			}
//			
//			json.addProperty("success", "Votre demande à été envoyée");
//			response.getWriter().write(json.toString());
//	        return;
//		}
//		
//		if(act.equals("getSolde")){
//			String login = ""+myMap.get("login");
//			String pw = ""+myMap.get("pw");
//			ClientPersistant clientPersistant = clientService.getClientByLoginAndPw(login, pw);
//			BigDecimal[] soldes = mobileService.getSolde(clientPersistant);
//			GsonBuilder b = new GsonBuilder();
//			b.setExclusionStrategies(new AnnotationExclusionStrategy()).create();
//			b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
//			Gson gson2 = b.create();
//			String json2 = gson2.toJson(soldes);
//			response.getWriter().write(json2);
//	        return;
//		}
//		
//		if(act.equals("isNoter")){
//			String login = ""+myMap.get("login");
//			String pw = ""+myMap.get("pw");
//			ClientPersistant clientPersistant = clientService.getClientByLoginAndPw(login, pw);
//			String retour = FileUtilController.callURL(urlCloud + "/mobileCtrl?act=getAvis&login="+login+"&pw="+pw);
//			Map<String, Object> data = gson.fromJson(retour, type);
//			if(data != null){
//				String dateAvis = (String) data.get("dateAvis");
//				Date date = DateUtil.stringToDate(dateAvis,"yyyy-MM-dd HH:mm:ss");
//				int n = mobileService.isNoter(clientPersistant, date);
//				if(n == 1 || n == 2){
//					json.addProperty("OK", "OK");
//				} else if(n == -1){
//					json.addProperty("KO", "Vous avez déjà envoyé un avis");
//				}
//					json.addProperty("KO", "Vous pouvez pas envoyé un avis");
//			}
//			
//			response.getWriter().write(json.toString());
//	        return;
//		}
//		
//		if(act.equals("supprimerCompte")){
//			String login = ""+myMap.get("login");
//			String pw = ""+myMap.get("pw");
//			String codeAuth = ""+myMap.get("codeAuth");
//			String type_appli = ""+myMap.get("type_appli");
//			FileUtilController.callURL(urlCloud + "/mobileCtrl?act=desactiveCompte&login="+login+"&pw="+pw
//					+"&codeAuth="+codeAuth+"&type_appli="+type_appli);
//			mobileService.desactiverCompte(login, pw);
//			json.addProperty("success", "Votre compte a été supprimé");
//			response.getWriter().write(json.toString());
//	        return;
//		}
//		
//		if(act.equals("listHistorique")){
//			String login = request.getParameter("login");
//			String pw = request.getParameter("pw");
//			ClientPersistant clientPersistant = clientService.getClientByLoginAndPw(login, pw);
//			List<CaisseMouvementPersistant> listMouvement =  mobileService.listHistorique(clientPersistant);
//			request.setAttribute("listMouvement", listMouvement);
//			ControllerUtil.forward(this.getServletContext(), request, response, "/domaine/caisse-mobile/historique-list.jsp");
//	        return;
//		}
//		
//		if(act.equals("telechargerFedilite")){
//			String login = ""+myMap.get("login");
//			String pw = ""+myMap.get("pw");
//			ClientPersistant clientPersistant = clientService.getClientByLoginAndPw(login, pw);
//			json.addProperty("client_id", clientPersistant.getId());
//			response.getWriter().write(json.toString());
//	        return;
//		}
//		
//	}
//	
//	public static String getBody(HttpServletRequest request) throws IOException {
//
//	    String body = null;
//	    StringBuilder stringBuilder = new StringBuilder();
//	    BufferedReader bufferedReader = null;
//
//	    try {
//	        InputStream inputStream = request.getInputStream();
//	        if (inputStream != null) {
//	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//	            char[] charBuffer = new char[128];
//	            int bytesRead = -1;
//	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//	                stringBuilder.append(charBuffer, 0, bytesRead);
//	            }
//	        } else {
//	            stringBuilder.append("");
//	        }
//	    } catch (IOException ex) {
//	        throw ex;
//	    } finally {
//	        if (bufferedReader != null) {
//	            try {
//	                bufferedReader.close();
//	            } catch (IOException ex) {
//	                throw ex;
//	            }
//	        }
//	    }
//
//	    body = stringBuilder.toString();
//	    return body;
//	}
//}
