package com.mcsense.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

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
		String taskDesc = request.getParameter("taskDesc");
		System.out.println("Task Name: " + taskDesc);
		// Publish in JMS Queue. Start ActiveMQ before running this. (Run
		// C:\Program Files\apache-activemq-5.5.0\bin\activemq)
//		Sensors requiredSensors = 
//			getSensorsIndicators(request);
		Producer p = new Producer();
		int taskID =0;
		try {
			//insert
//			Task t = taskServicesLocal.createTask(clientId,taskName, taskType);
			Task t = prepareTask(request);
			t = taskServicesLocal.createTask(t);
			taskID = t.getTaskId();
//			p.send(taskDesc);
		} catch (Exception e) {
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

	private Task prepareTask(HttpServletRequest request) {
		//parse input
		String taskDesc = request.getParameter("taskDesc");
		String clientId = request.getParameter("id");
		String taskType = request.getParameter("taskType");
		String clientPay = request.getParameter("pay");
		String duration = request.getParameter("duration");
		String taskName = request.getParameter("name");
		String accelerometer = request.getParameter("accelerometer");
		String gps = request.getParameter("gps");
		String camera = request.getParameter("camera");
		String mic = request.getParameter("mic");
		String wifi = request.getParameter("wifi");
		String bluetooth = request.getParameter("bluetooth");
		String magnetometer = request.getParameter("magnetometer");
		String proximity = request.getParameter("proximity");
		String ambient = request.getParameter("ambient");
		
		Task t = new Task();
		t.setClientPersonId(new Integer(clientId));
		t.setTaskDesc(taskDesc);
		t.setTaskType(taskType);
		t.setClientPay(new Integer(clientPay));
		t.setTaskDuration(new Integer(duration));
		t.setTaskName(taskName);
		t.setAccelerometer(accelerometer);
		t.setGps(gps);
		t.setCamera(camera);
		t.setMagnetometer(magnetometer);
		t.setMic(mic);
		t.setWifi(wifi);
		t.setBluetooth(bluetooth);
		t.setProximitySensor(proximity);
		t.setAmbientLightSensor(ambient);
		return t;
	}

	private void getSensorsIndicators(HttpServletRequest request) {
		String accelerometer = request.getParameter("accelerometer");
		String gps = request.getParameter("gps");
		String camera = request.getParameter("camera");
		String mic = request.getParameter("mic");
		String wifi = request.getParameter("wifi");
		String bluetooth = request.getParameter("bluetooth");
		String magnetometer = request.getParameter("magnetometer");
		String proximity = request.getParameter("proximity");
		String ambient = request.getParameter("ambient");
		
		System.out.println("Sensors:"+accelerometer+","+gps+","+camera+","+mic+","+wifi+","+bluetooth+","+magnetometer+","+proximity+","+ambient);
	}

}
