package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.service.Consumer;
import com.mcsense.service.Producer;

/**
 * Servlet implementation class ProviderServlet
 */
public class ProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProviderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String task = "";
		//Publish in JMS Queue. Start ActiveMQ before running this. (Run C:\Program Files\apache-activemq-5.5.0\bin\activemq)
		Consumer c = new Consumer();
    	try {
    		task = c.recieve();
    		System.out.println("Task read: "+task);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		//response
		PrintWriter out = response.getWriter();

	    out.println("<title>Task read</title>" +
	       "<body bgcolor=FFFFFF>");
	    out.println("<h2>Sensing Task Read: '"+task+"' </h2>");
	    out.println("<P>Return to <A HREF=../pages/Provider.jsp>Providers Screen</A>");
	    out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
