package com.mcsense.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.util.WebConstants;
import com.mcsense.util.WebUtil;

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
		
		String data = request.getParameter("data");
		String dataType = request.getParameter("dataType");
		String id = request.getParameter("id");
		if(data!=null){
			System.out.println("XML: "+data);
			saveData(id, dataType, data);
			response.sendRedirect("./facebook/survey.jsp?id="+id);
		} else{
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
			String rad16 = request.getParameter("rad16");
			
			String surveyResponse = rad1+";"+rad2 +";"+rad3 +";"+rad4 +";"+rad5 +";"+rad6 +";"+rad7 +";"+rad8 +";"+rad9 +";"+rad10+";"+rad11+";"+rad12+";"+rad13+";"+rad14+";"+rad15+";"+rad16+"\n\n";
			System.out.println("Answer for Qs: "+surveyResponse);
			saveData(id, "\n\n*****SurveyResponse*****\n\n", surveyResponse);
			
			String responseText = "Thank you for your participation and help!! <p> You can now close this browser window.";
//			response
			PrintWriter out = response.getWriter();
			out.println("<title>Submitted</title>" + "<body bgcolor=FFFFFF>");
			out.println("<h2>Survey Submitted.</h2>");
			out.println("<p>"+responseText);
//			out.println("<br/><a href=\"#\" onclick=\"FB.logout();\">Logout</a><br/>");
		}
		
	}

	private void saveData(String id, String dataType, String data) {
		FileOutputStream f =null;
		try {
			String realPath = getServletContext().getRealPath(WebConstants.DESTINATION_DIR_PATH);
			File destinationDir = new File(realPath);
			
			File file = new File(destinationDir,"/"+id+".txt");
//			f = new FileOutputStream(WebConstants.DESTINATION_DIR_PATH+"\\"+taskId+".jpg");
			f = new FileOutputStream(destinationDir+"\\"+id+".txt", true);
			f.write(dataType.getBytes());			
			f.write(data.getBytes());
			 f.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}

}
