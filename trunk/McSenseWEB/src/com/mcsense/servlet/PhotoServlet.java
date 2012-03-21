package com.mcsense.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mcsense.entities.Task;
import com.mcsense.services.TaskServicesLocal;
import com.mcsense.util.Base64;
import com.mcsense.util.McUtility;
import com.mcsense.util.WebConstants;
import com.mcsense.util.WebUtil;

/**
 * Servlet implementation class PhotoServlet
 */
public class PhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	private static final String DESTINATION_DIR_PATH ="/files";
//	private static final String DESTINATION_DIR_PATH ="C:\\Manoop\\McSense\\McSenseWEB\\WebContent\\files";
       
	@EJB(name="com.mcsense.services.TaskServices")
	TaskServicesLocal taskServicesLocal;
	
	private static final String TMP_DIR_PATH = "c:\\temp";
	private File tmpDir;
//	private static final String DESTINATION_DIR_PATH ="/files";
//	private static final String SENSING_DESTINATION_DIR_PATH ="C:\\Manoop\\McSense\\McSenseWEB\\WebContent\\files";
	private File destinationDir;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoServlet() {
        super();
        taskServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/TaskServices!com.mcsense.services.TaskServicesLocal");
    }

    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		tmpDir = new File(TMP_DIR_PATH);
		if(!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
		String realPath = getServletContext().getRealPath(WebConstants.DESTINATION_DIR_PATH);
		destinationDir = new File(realPath);
		if(!destinationDir.isDirectory()) {
			throw new ServletException(WebConstants.DESTINATION_DIR_PATH+" is not a directory");
		}
 
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
		//System.out.println("Photo Recieved");
		String taskId = request.getParameter("taskId");
		String providerId = request.getParameter("providerId");
		String imageString = request.getParameter("image");
		String currentLocation = request.getParameter("currentLocation");
		byte[] imageByteArray = null;
		if(imageString!=null)
			imageByteArray = Base64.decode(imageString);
		
		if ( ServletFileUpload.isMultipartContent( request ))
		{
		    List<FileItem> fileItems= null;
			try {
				fileItems = new ServletFileUpload( new DiskFileItemFactory( 1024 * 1024, new File(TMP_DIR_PATH) )).
		        parseRequest( request );
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		    for ( FileItem item : fileItems )
		    {
		        String fieldName = item.getFieldName();
		        if(fieldName.trim().equals("taskId"))
		        	taskId = item.getString();
		        else if(fieldName.trim().equals("providerId"))
		        	providerId = item.getString();
		        else if(fieldName.trim().equals("currentLocation"))
		        	currentLocation = item.getString();
		        else if(fieldName.trim().equals("image")){
		        	InputStream is = item.getInputStream();
		        	imageByteArray = new byte[is.available()];
		        	is.read(imageByteArray);
		        }
		    }
		}
		
		PrintWriter out = response.getWriter();
		Task t = taskServicesLocal.getTaskByIdAndProvider(providerId,taskId);
		if(t!=null && t.getTaskStatus().equals("IP")){
			
			FileOutputStream f =null;
			try {
//				byte[] imageByteArray = Base64.decode(imageString);
				//System.out.println("imageByteArray length: " + imageByteArray.length);
				
				String realPath = getServletContext().getRealPath(WebConstants.DESTINATION_DIR_PATH);
				File destinationDir = new File(realPath);
				
				File file = new File(destinationDir,"/"+taskId+".jpg");
//				f = new FileOutputStream(WebConstants.DESTINATION_DIR_PATH+"\\"+taskId+".jpg");
				f = new FileOutputStream(destinationDir+"\\"+taskId+".jpg");
				f.write(imageByteArray);
				String completionStatus = WebUtil.getComplationStatus(t);
				taskServicesLocal.completeTask(providerId,taskId,completionStatus,currentLocation);
				out.println("success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    f.close();
			//System.out.println("Task: " + taskId);
		}		
	}

}
