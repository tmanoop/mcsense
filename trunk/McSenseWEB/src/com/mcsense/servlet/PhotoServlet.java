package com.mcsense.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mcsense.util.Base64;

/**
 * Servlet implementation class PhotoServlet
 */
public class PhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String DESTINATION_DIR_PATH ="/files";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		System.out.println("Photo Recieved");
		String task = request.getParameter("taskDesc");
		String id = request.getParameter("id");
		String imageString = request.getParameter("image");
		
		FileOutputStream f =null;
		try {
			byte[] imageByteArray = Base64.decode(imageString);
			System.out.println("imageByteArray length: " + imageByteArray.length);
			
			String realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH);
			File destinationDir = new File(realPath);
			
			File file = new File(destinationDir,"/ProviderImage.jpg");
			f = new FileOutputStream(file);
//			f = new FileOutputStream(DESTINATION_DIR_PATH+"/ProviderImage.jpg");
			f.write(imageByteArray);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    f.close();
		System.out.println("Task: " + task);
	}

}
