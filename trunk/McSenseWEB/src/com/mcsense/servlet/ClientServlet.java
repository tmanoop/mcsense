package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.service.Producer;

import sun.rmi.runtime.Log;

/**
 * Servlet implementation class ClientServlet
 */
public class ClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ClientServlet() {
		// TODO Auto-generated constructor stub
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
		System.out.println("Task: " + task);
		// Publish in JMS Queue. Start ActiveMQ before running this. (Run
		// C:\Program Files\apache-activemq-5.5.0\bin\activemq)
		Producer p = new Producer();
		try {
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
			out.println("Sensing Task Submitted.");
		} else {
			out.println("<title>Submitted</title>" + "<body bgcolor=FFFFFF>");
			out.println("<h2>Sensing Task Submitted.</h2>");
			out.println("<P>Return to <A HREF=../pages/Client.jsp>Task Submission Screen</A>");
			out.println("<P>Providers can read from <A HREF=../pages/Provider.jsp>Providers Screen</A>");
		}
		out.close();
	}

}
