package com.mcsense.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.mcsense.entities.Task;
import com.mcsense.json.JTask;
import com.mcsense.mqservice.Consumer;
import com.mcsense.services.TaskServicesLocal;
import com.mcsense.util.Base64;
import com.mcsense.util.McUtility;
import com.mcsense.util.WebUtil;

/**
 * Servlet implementation class ProviderServlet
 */
public class ProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name="com.mcsense.services.TaskServices")
	TaskServicesLocal taskServicesLocal;
	
	private static final String TMP_DIR_PATH = "c:\\temp";
	private File tmpDir;
	private static final String DESTINATION_DIR_PATH ="/files";
	private static final String SENSING_DESTINATION_DIR_PATH ="C:\\Manoop\\McSense\\McSenseWEB\\WebContent\\files";
	private File destinationDir;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProviderServlet() {
		super();
		taskServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/TaskServices!com.mcsense.services.TaskServicesLocal");
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		tmpDir = new File(TMP_DIR_PATH);
		if(!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
		String realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH);
		destinationDir = new File(realPath);
		if(!destinationDir.isDirectory()) {
			throw new ServletException(DESTINATION_DIR_PATH+" is not a directory");
		}
 
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
		List<Task> taskList = null;
		Task t = null;
		try {
			//If there is no task with "P" status
			taskList = taskServicesLocal.getTasks("P", "");
			if(taskList.size() != 0){
//				//return none else call mq receive
//				task = c.recieve();
//				//process task
//				t = taskServicesLocal.updateTask(id,task);
				t = taskList.get(taskList.size()-1);
				taskID = t.getTaskId();
				System.out.println("Task read: " + task);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}

		// response
		PrintWriter out = response.getWriter();

		String type = "";
		type = request.getParameter("type");
		System.out.println("type: "+type);
		if (type!=null && type.equals("mobile")){
			System.out.println("respond to mobile.");
			if(t!=null){
				List<JTask> jTaskList = new ArrayList<JTask>();
//				Task t = tList.get(i);
				JTask jTask = WebUtil.mapToJsonTask(t);
				jTaskList.add(jTask);
				//if wnat to show only one task, then use jTaskList
				out.println(new Gson().toJson(jTaskList));
			} else {
				out.println("No New Tasks");
			}
//			out.println("TaskID: "+taskID+"; Task Desc: "+task);
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
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		String htmlFormName = request.getParameter("htmlFormName");
		System.out.println("htmlFormName: "+htmlFormName);
		
		String providerId = request.getParameter("providerId");
		String taskId = request.getParameter("taskId");
		String taskStatus = request.getParameter("taskStatus");
		System.out.println("TaskID"+taskId);
		if (taskStatus != null){ 
			if(taskStatus.equals("Accepted")) {
				Task t = taskServicesLocal.getTaskById(taskId);
				if(t!=null && t.getTaskStatus().equals("P")){
					taskServicesLocal.acceptTask(providerId,taskId);
					out.println("Accepted");
				} else{
					out.println("Task not available.");
				}
			} else if(taskStatus.equals("Pending")) {
				Task t = taskServicesLocal.getTaskById(taskId);
				if(t!=null && t.getTaskStatus().equals("IP")){
					long currentTime = WebUtil.getCurrentTime();
					out.println(currentTime);
				} else{
					out.println("0");
				}
			} else if (taskStatus.equals("Completed")){
				Task t = taskServicesLocal.getTaskByIdAndProvider(providerId,taskId);
				if(t!=null && t.getTaskStatus().equals("IP")){
					System.out.println("Sensed Data Recieved");
					String sensedData = request.getParameter("sensedData");
					String completionStatus = request.getParameter("completionStatus");
					
					FileOutputStream f =null;
					try {
						byte[] sensedDataByteArray = Base64.decode(sensedData);
						System.out.println("sensedDataByteArray length: " + sensedDataByteArray.length);
						
						f = new FileOutputStream(SENSING_DESTINATION_DIR_PATH+"\\"+taskId+".txt");
//						f = new FileOutputStream(DESTINATION_DIR_PATH+"/ProviderImage.jpg");
						f.write(sensedDataByteArray);
						
						taskServicesLocal.completeTask(providerId,taskId,completionStatus);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    f.close();
					System.out.println("Task: " + taskId);
				}				
			}
		} else if(htmlFormName == null || htmlFormName.equals("completetask")){
			String taskid = request.getParameter("taskid");
//			String picloc = request.getParameter("picloc");
			
			DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
			/*
			 *Set the size threshold, above which content will be stored on disk.
			 */
			fileItemFactory.setSizeThreshold(1*1024*1024); //1 MB
			/*
			 * Set the temporary directory to store the uploaded files of size above threshold.
			 */
			fileItemFactory.setRepository(tmpDir);
	 
			ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
			try {
				/*
				 * Parse the request
				 */
				List items = uploadHandler.parseRequest(request);
				Iterator itr = items.iterator();
				while(itr.hasNext()) {
					FileItem itemID = (FileItem) itr.next();
					FileItem item = null;
					if(itr.hasNext())
						item = (FileItem) itr.next();
					else {
						out.println("No File!!");
						break;
					}
					/*
					 * Handle Form Fields.
					 */
					if(item.isFormField()) {
						out.println("File Name = "+item.getFieldName()+", Value = "+item.getString());
					} else {
						//Handle Uploaded files.
						out.println("Field Name = "+item.getFieldName()+
							", File Name = "+item.getName()+
							", Content type = "+item.getContentType()+
							", File Size = "+item.getSize());
						/*
						 * Write file to the ultimate location.
						 */
						File file = new File(destinationDir,item.getName().substring(3));
						item.write(file);
					}
					out.close();
				}
			}catch(FileUploadException ex) {
				log("Error encountered while parsing the request",ex);
			} catch(Exception ex) {
				log("Error encountered while uploading file",ex);
			}
			out.println("<P>Return to <A HREF=../pages/Provider.jsp>Providers Screen</A>");
		}
		
		out.close();
	}

}
