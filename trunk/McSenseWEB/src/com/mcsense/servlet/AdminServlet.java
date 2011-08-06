package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.entities.People;
import com.mcsense.services.BankAdminServicesLocal;
import com.mcsense.util.McUtility;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB(name="com.mcsense.services.BankAdminServices")
	BankAdminServicesLocal bankAdminServicesLocal;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get test");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		bankAdminServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/BankAdminServices!com.mcsense.services.BankAdminServicesLocal");
		String htmlFormName = request.getParameter("htmlFormName");
		System.out.println("htmlFormName: "+htmlFormName);
		if(htmlFormName.equals("register")){
			String lname = request.getParameter("lname");
			String fname = request.getParameter("fname");
			String bankid = request.getParameter("bankid");
			String address = request.getParameter("address");		
			
			People p = new People();
			p.setBankAccountId(bankid);
			p.setPersonFname(fname);
			p.setPersonLname(lname);
			p.setPersonAddress(address);
			
			int personID = bankAdminServicesLocal.register(p);
			
			// response
			out.print("<br> Below McSense Account is created successfully.");
			out.print("<br> Your McSense ID: " + personID);
			
			out.println("<P>Return to <A HREF=../pages/Admin.jsp>Admin Screen</A>");
		}
		else if(htmlFormName.equals("delete")){
			String id = request.getParameter("id");
			bankAdminServicesLocal.deleteReputation(id);
			boolean del = bankAdminServicesLocal.deletePerson(id);
			if(del)
				out.print("<br> McSense Account#"+id+" is deleted successfully.");
			else
				out.print("<br> McSense Account#"+id+" is not deleted due to system error.");
		}
		
		out.close();
	}

}
