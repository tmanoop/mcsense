package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FacebookServlet
 */
public class FacebookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FacebookServlet() {
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
		System.out.println("post test");
		/**
		 * Stores the participants Facebook public profile objects including wall writings, comments, number of photos, videos, Checkins, Notes, Status messages.
		 * Friends, Wall, Likes, Movies, Music, Books, Notes, Permissions, no. of photos & videos, Events, Groups, Checkins, Objects with Location.
		 */
		String rad1 = request.getParameter("rad1");
		String rad2 = request.getParameter("rad2");
		String rad3 = request.getParameter("rad3");
		String rad4 = request.getParameter("rad4");
		String rad5 = request.getParameter("rad5");
		String rad6 = request.getParameter("rad6");
		String rad7 = request.getParameter("rad7");
		String rad8 = request.getParameter("rad8");
		String rad9 = request.getParameter("rad9");
		String rad10 = request.getParameter("rad10");
		String rad11 = request.getParameter("rad11");
		String rad12 = request.getParameter("rad12");
		String rad13 = request.getParameter("rad13");
		String rad14 = request.getParameter("rad14");
		String rad15 = request.getParameter("rad15");
		
		System.out.println("Answer for Qs: "+rad1+rad2 +rad3 +rad4 +rad5 +rad6 +rad7 +rad8 +rad9 +rad10+rad11+rad12+rad13+rad14+rad15);
		
		String responseText = "Thank you for your participation and help!!";
//		response
		PrintWriter out = response.getWriter();
		out.println("<title>Submitted</title>" + "<body bgcolor=FFFFFF>");
		out.println("<h2>Survey Submitted.</h2>");
		out.println("<p>"+responseText);
	}

}
