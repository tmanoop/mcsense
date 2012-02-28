package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.entities.People;
import com.mcsense.services.BankAdminServicesLocal;
import com.mcsense.util.McUtility;

/**
 * Servlet implementation class BankServlet
 */
public class BankServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB(name="com.mcsense.services.BankAdminServices")
	BankAdminServicesLocal bankAdminServicesLocal;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BankServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String htmlFormName = request.getParameter("htmlFormName");
		//System.out.println("htmlFormName: "+htmlFormName);
		
		People p = null;
		List<People> pList = null;
		try {
			
			String name = request.getParameter("lname");
			String id = request.getParameter("id");
			
			//System.out.println("lname: "+name);
			//System.out.println("id: "+id);
			
			bankAdminServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/BankAdminServices!com.mcsense.services.BankAdminServicesLocal");
			
			if(id!=null && id!=""){
				//String id = "1";
				p = (People)bankAdminServicesLocal.findPersonByID(id);
			}
			
			if(name!=null && name!=""){
				//String name = "Peter";
				pList = (List<People>)bankAdminServicesLocal.findPersonByName(name);
			}
			
			//dataServicesLocal.test();
						
		} catch (Exception e) {
			//System.out.println("PersonID not found.");
			e.printStackTrace();
		}
		
		// response
		PrintWriter out = response.getWriter();
		if (p!=null) {
			//System.out.println("PersonID: " + p.getPersonId());
			out.println("<br> PersonID: " + p.getPersonId() + "| FirstName: " + p.getPersonFname() + "| LastName: " + p.getPersonLname() + "| AccountNumber: " + p.getBankAccountId() );
			if(htmlFormName.equals("reputation"))
				out.print("| Reputation: " + p.getReputation().getReputationScore());
		} else if (pList!=null && pList.size()!=0) {
			//System.out.println("PersonID: " + pList.get(0).getPersonId());
			for(int i=0;i<pList.size();i++){
				People p1 = pList.get(i);
				out.println("<br> PersonID: " + p1.getPersonId() + "| FirstName: " + p1.getPersonFname() + "| LastName: " + p1.getPersonLname() + "| AccountNumber: " + p1.getBankAccountId() );
				if(htmlFormName.equals("reputation"))
					out.print("| Reputation: " + p1.getReputation().getReputationScore());
			}
		} else{
			//System.out.println("PersonID not found.");
			out.println("PersonID not found.");
		}
		if(htmlFormName.equals("reputation"))
			out.println("<P>Return to <A HREF=../pages/Reputation.jsp>Search Screen</A>");
		else
			out.println("<P>Return to <A HREF=../pages/Bank.jsp>Search Screen</A>");
		out.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("get test");		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("post test");
	}

}
