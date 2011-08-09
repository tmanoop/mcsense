package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.entities.Task;
import com.mcsense.mqservice.Consumer;
import com.mcsense.mqservice.Producer;
import com.mcsense.services.TaskServicesLocal;
import com.mcsense.util.McUtility;

/**
 * Servlet implementation class ProviderServlet
 */
public class ProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name="com.mcsense.services.TaskServices")
	TaskServicesLocal taskServicesLocal;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProviderServlet() {
		super();
		taskServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/TaskServices!com.mcsense.services.TaskServicesLocal");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String task = "";
		// Publish in JMS Queue. Start ActiveMQ before running this. (Run
		// C:\Program Files\apache-activemq-5.5.0\bin\activemq)
		Consumer c = new Consumer();
		int taskID =0;
		try {
			task = c.recieve();
			//process task
			Task t = taskServicesLocal.updateTask(id,task);
			taskID = t.getTaskId();
			System.out.println("Task read: " + task);
		} catch (JMSException e) {
			e.printStackTrace();
		}

		// response
		PrintWriter out = response.getWriter();

		String type = "";
		type = request.getParameter("type");
		System.out.println("type: "+type);
		if (type!=null && type.equals("mobile")){
			System.out.println("respond to mobile.");
			out.println("TaskID: "+taskID+"; Task Desc: "+task);
		}
		else {
			out.println("<title>Task read</title>" + "<body bgcolor=FFFFFF>");
			out.println("<h2>TaskID: "+taskID+"; Sensing Task Read: '" + task + "' </h2>");
			out.println("<P>Return to <A HREF=../pages/Provider.jsp>Providers Screen</A>");
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
