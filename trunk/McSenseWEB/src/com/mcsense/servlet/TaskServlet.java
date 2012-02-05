package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mcsense.entities.People;
import com.mcsense.entities.Task;
import com.mcsense.json.JTask;
import com.mcsense.services.BankAdminServicesLocal;
import com.mcsense.services.TaskServicesLocal;
import com.mcsense.util.McUtility;
import com.mcsense.util.WebUtil;

/**
 * Servlet implementation class TaskServlet
 */
public class TaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB(name="com.mcsense.services.TaskServices")
	TaskServicesLocal taskServicesLocal;

	@EJB(name="com.mcsense.services.BankAdminServices")
	BankAdminServicesLocal bankAdminServicesLocal;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaskServlet() {
        super();
        bankAdminServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/BankAdminServices!com.mcsense.services.BankAdminServicesLocal");
		taskServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/TaskServices!com.mcsense.services.TaskServicesLocal");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String htmlFormName = request.getParameter("htmlFormName");
		System.out.println("htmlFormName: "+htmlFormName);
		String type = "";
		type = request.getParameter("type");
		System.out.println("type: "+type);
		String status = request.getParameter("status");	
		if(htmlFormName.equals("tasklookup")){
			
			String providerId = request.getParameter("providerId");
			List<Task> tList = taskServicesLocal.getTasksbyStatus(status,providerId);
			
			if (type!=null && type.equals("mobile")){
				if (tList!=null) {
					List<JTask> jTaskList = new ArrayList<JTask>();
					for (int i = 0; i < tList.size(); i++) {
						Task t = tList.get(i);
						JTask jTask = WebUtil.mapToJsonTask(t);
						System.out.println("Exp time: "+jTask.getTaskExpirationTime());
//						Timestamp now = new Timestamp(System.currentTimeMillis());
						//Filter new sensing tasks after 4pm
						if(!status.equals("P") || (jTask.getTaskType().equals("photo") || WebUtil.hasEnoughSensingTime(jTask.getTaskDuration(),jTask.getTaskExpirationTime())))
							jTaskList.add(jTask);
					}
					out.println(new Gson().toJson(jTaskList));
				} else {
					out.println("No Tasks");
				}
			} 
			
		} else if(htmlFormName.equals("tasklookupHtml")){

			List<Task> tList = taskServicesLocal.getAllTasksbyStatus(status);
			for(int i=0;i<tList.size();i++){
				Task t = tList.get(i);
				out.println("<BR> TaskID: " + t.getTaskId() + "| TaskStatus: " + t.getTaskStatus() + "| ProviderID: " + t.getProviderPersonId() + "| ClientID: " + t.getClientPersonId() + "| Task Name: " + t.getTaskName() + "| Task Accepted time: " + t.getTaskAcceptedTime() + "| Task Completion time: " + t.getTaskCompletionTime() + "| Task Expiration time: " + t.getTaskExpirationTime() + "| Task Created time: " + t.getTaskCreatedTime());
			}
		} 
		if (type==null)
			out.println("<P>Return to <A HREF=../pages/TaskLookup.jsp>Task Lookup Screen</A>");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		String htmlFormName = request.getParameter("htmlFormName");
		System.out.println("htmlFormName: "+htmlFormName);
		if(htmlFormName.equals("task")){
			String id = request.getParameter("id");
			String taskDesc = request.getParameter("taskDesc");		
			
			Task t = new Task();
			t.setClientPersonId(new Integer(id));
			t.setTaskType(taskDesc);
			
			People p = (People)bankAdminServicesLocal.findPersonByID(id);
			t.setPeople(p);
			t = taskServicesLocal.createTask(t);
		
			// response
			out.print("<br> Below McSense Task is created successfully.");
			out.print("<br> Your McSense ID: " + t.getTaskId());
			out.println("<P>Return to <A HREF=../pages/Task.jsp>Task Screen</A>");
		} else if(htmlFormName.equals("updateTask")){
			String status = request.getParameter("status");
			String taskId = request.getParameter("taskId");
			Task t = taskServicesLocal.getTaskById(taskId);
			t.setTaskStatus(status);
			taskServicesLocal.updateTask(t);
			out.print("<br> McSense Task ID: " + t.getTaskId() + " is updated successfully.");
			out.println("<P>Return to <A HREF=../pages/Task.jsp>Task Service Screen</A>");
		}
		out.close();
	}

	private Timestamp getSensingFilterTimestamp() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		now.setHours(16);
		now.setMinutes(0);
		now.setNanos(0);
		now.setSeconds(0);
		return now;
	}
	
}
