package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mcsense.entities.People;
import com.mcsense.security.SimpleCrypto;
import com.mcsense.services.LoginServicesLocal;
import com.mcsense.services.TaskServicesLocal;
import com.mcsense.util.Emailer;
import com.mcsense.util.McUtility;
import com.mcsense.util.ServiceConstants;
import com.mcsense.util.WebUtil;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB(name="com.mcsense.services.TaskServices")
	LoginServicesLocal loginServicesLocal;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        loginServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/LoginServices!com.mcsense.services.LoginServicesLocal");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		String type = request.getParameter("type");
		String htmlFormName = request.getParameter("htmlFormName");
		System.out.println("type: "+type);
		if(htmlFormName!=null && htmlFormName.equals("login")){
			String emailID = request.getParameter("emailID");
			String password = request.getParameter("password");
			String encryptPassword = getEncryptPassword(password);
			People p = null;
			if(emailID.equals(WebUtil.adminID))
				p = loginServicesLocal.loginCheck(emailID,encryptPassword);
			if(p != null){
				session.setAttribute("emailID",emailID);
				session.setAttribute("clientID",p.getPersonId());
				response.sendRedirect("welcome.jsp");
			} else {
				session.setAttribute("error","Login failed!!");
				response.sendRedirect("login.jsp");
			}
		}
		if(type!=null && type.equals("mobile")){
			String reqType = request.getParameter("reqType");
			System.out.println("reqType: "+reqType);
			if(reqType.equals("login")){
				String emailId = request.getParameter("emailId");
				String password = request.getParameter("password");
				String meid = request.getParameter("meid");
				String encryptPassword = getEncryptPassword(password);
				People p = loginServicesLocal.loginCheck(emailId,encryptPassword,meid);
				if(p != null){
					out.println(p.getPersonId());
				} else {
					out.println("failed");
				}
			} else if(reqType.equals("register")){
				String emailId = request.getParameter("emailId");	
				String password = request.getParameter("password");	
				String meid = request.getParameter("meid");
				if(loginServicesLocal.isPersonExist(emailId,meid)){
					out.println("exist");
				} else {
					People p = new People();
					p.setEmailId(emailId);
					p.setPassword(password);
					p.setMeid(meid);
					try {
						People newPerson = loginServicesLocal.register(p);
						emailToAdmin(emailId,password,meid);
						out.println(newPerson.getPersonId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						out.println("failed");
					}
				}
			}
		}
		
	}

	private void emailToAdmin(String emailId, String password, String meid) {
		String body = "User Details:" +
						"\n\n Email Address: "+emailId +
						"\n\n password: "+password +
						"\n\n Meid: "+meid;
		Emailer.sendEmail("mcsense.app@gmail.com", "mt57@njit.edu", "New McSense user registered", body);
	}

	private String getEncryptPassword(String password) {
		String encryptedText = "";
    	try {
			encryptedText = SimpleCrypto.encrypt(ServiceConstants.SEED,password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println("encryptedText:"+encryptedText);
		return encryptedText;
	}

}
