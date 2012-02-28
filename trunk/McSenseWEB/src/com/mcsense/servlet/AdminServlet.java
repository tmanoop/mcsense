package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.entities.Bank;
import com.mcsense.entities.People;
import com.mcsense.services.BankAdminServicesLocal;
import com.mcsense.services.DataServicesLocal;
import com.mcsense.util.McUtility;
import com.mcsense.entities.Task;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB(name="com.mcsense.services.BankAdminServices")
	BankAdminServicesLocal bankAdminServicesLocal;
	@EJB(name="com.mcsense.services.DataServices")
	DataServicesLocal dataServicesLocal;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        dataServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/DataServices!com.mcsense.services.DataServicesLocal");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		////System.out.println("get test");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		bankAdminServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/BankAdminServices!com.mcsense.services.BankAdminServicesLocal");
		String htmlFormName = request.getParameter("htmlFormName");
		//System.out.println("htmlFormName: "+htmlFormName);
		if(htmlFormName.equals("register")){
			String lname = request.getParameter("lname");
			String fname = request.getParameter("fname");
			String bankid = request.getParameter("bankid");
			String address = request.getParameter("address");	
			String emailId = request.getParameter("emailId");	
			String password = request.getParameter("password");	
			String repassword = request.getParameter("repassword");	
			if(!password.equals(repassword))
				response.sendRedirect("/pages/Admin.jsp");
			else{
				People p = new People();
				p.setBankAccountId(bankid);
				p.setPersonFname(fname);
				p.setPersonLname(lname);
				p.setPersonAddress(address);
				p.setEmailId(emailId);
				p.setPassword(password);
				
				int personID = bankAdminServicesLocal.register(p);
				
				// response
				out.print("<br> Below McSense Account is created successfully.");
				out.print("<br> Your McSense ID: " + personID);
			}
		}
		else if(htmlFormName.equals("delete")){
			String id = request.getParameter("id");
			bankAdminServicesLocal.deleteReputation(id);
			boolean del = bankAdminServicesLocal.deletePerson(id);
			if(del)
				out.print("<br> McSense Account#"+id+" is deleted successfully.");
			else
				out.print("<br> McSense Account#"+id+" is not deleted due to system error.");
		} else if(htmlFormName.equals("update")){
			String id = request.getParameter("id");
			String fName = request.getParameter("fName");
			String lName = request.getParameter("lName");
			String address = request.getParameter("address");
			String gender = request.getParameter("gender");
			String dept = request.getParameter("dept");
			String year = request.getParameter("year");
			String age = request.getParameter("age");
			
			People p = bankAdminServicesLocal.findPersonByID(id);
			p.setPersonFname(fName);
			p.setPersonLname(lName);
			p.setPersonAddress(address);
			p.setGender(gender);
			p.setNjitDepartment(dept);
			p.setNjitAcademicYear(year);
			p.setAgeGroup(age);
			
			bankAdminServicesLocal.updatePerson(p);
			out.print("<br> McSense Account#"+id+" is updated successfully.");
		} else if(htmlFormName.equals("deposit")){
			String taskId = request.getParameter("taskId");
			String amount = request.getParameter("amount");
			dataServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/DataServices!com.mcsense.services.DataServicesLocal");
			
			Task t = dataServicesLocal.findEntity(Task.class, new Integer(taskId));
			
			Bank b = new Bank();
			b.setAmount(new Integer(amount));
			b.setTaskId(new Integer(taskId));
			b.setPeople(t.getPeople());
			bankAdminServicesLocal.deposit(b);
			out.println("<P>Bank amount $"+amount+" is deposited for taskID: "+taskId+" and account#"+t.getPeople().getBankAccountId());
		} else if(htmlFormName.equals("userlogin")){
			String personId = request.getParameter("id");
			log("PersonID: "+personId);
			People p = dataServicesLocal.findEntity(People.class, new Integer(personId));
			out.println("<br> Email: "+p.getEmailId()+"; Password: "+p.getPassword());
		}
		out.println("<P>Return to <A HREF=../pages/Admin.jsp>Admin Screen</A>");
		out.close();
	}

}
