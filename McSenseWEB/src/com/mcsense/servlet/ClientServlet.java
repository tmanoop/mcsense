package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.entities.Task;
import com.mcsense.mqservice.Producer;
import com.mcsense.services.TaskServicesLocal;
import com.mcsense.util.McUtility;

/**
 * Servlet implementation class ClientServlet
 */
public class ClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name="com.mcsense.services.TaskServices")
	TaskServicesLocal taskServicesLocal;
	/**
	 * Default constructor.
	 */
	public ClientServlet() {
		taskServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/TaskServices!com.mcsense.services.TaskServicesLocal");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get test");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String task = request.getParameter("taskDesc");
		String id = request.getParameter("id");
		System.out.println("Task: " + task);
		// Publish in JMS Queue. Start ActiveMQ before running this. (Run
		// C:\Program Files\apache-activemq-5.5.0\bin\activemq)
		Producer p = new Producer();
		int taskID =0;
		try {
			//insert
			Task t = taskServicesLocal.createTask(id,task);
			taskID = t.getTaskId();
			p.send(task);
		} catch (JMSException e) {
			e.printStackTrace();
		}

		// response
		PrintWriter out = response.getWriter();
		String type = "";
		type = request.getParameter("type");
		System.out.println("type: " + type);
		if (type!=null && type.equals("mobile")) {
			System.out.println("respond to mobile.");
			out.println("Sensing Task Submitted. TaskID: "+taskID);
		} else {
			out.println("<title>Submitted</title>" + "<body bgcolor=FFFFFF>");
			out.println("<h2>Sensing Task Submitted. TaskID: "+taskID+"</h2>");
			out.println("<P>Return to <A HREF=../pages/Client.jsp>Task Submission Screen</A>");
			out.println("<P>Providers can read from <A HREF=../pages/Provider.jsp>Providers Screen</A>");
		}
		out.close();
	}

}
