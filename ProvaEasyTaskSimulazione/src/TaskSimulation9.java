import simulation.event.*;
import simset.*;
import random.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TaskSimulation9 extends Simulation {
	
	// CONTROL PANNEL !!!
	private static final boolean 	FIXED_EARNING_FOR_TASK 				= true; // false;
	private static final double 	FIXED_EARNING_VALUE 				= 0.10; // 10 cent
	private static final int 		PERCENTAGE_EARNING 					= 10;
	private static final int 		PERCENTAGE_TASK_FAILURE 			= 10; //11;
    private static final double 	SIM_PERIOD 							= 100000; //00;
    private static final int		NUMBER_OF_REGISTRED_PROVIDERS		= 1000;
    private static final int		INFO								= 0;
    private static final int		WARNING								= 1;
    private static final boolean    LOAD_MOBILITY_TRACES				= true;
    private static final double		PROVIDER_MIN_PRICE					= 0.5;
    private static final double		PROVIDER_MAX_PRICE					= 5;
    private static final double		BUDGET_MIN_PRICE					= 1;
    private static final double		BUDGET_MAX_PRICE					= 7;
    private static final int		LAMBDA_MIN							= 20;
    private static final int		LAMBDA_MAX							= 60;
    private static final int		INITIAL_TASK_ARRIVAL_MIN			= 0;
    private static final int		INITIAL_TASK_ARRIVAL_MAX			= 300;
    private static final int		EXPONENTIAL_TASK					= 11;
    private static final int		MAX_N_TASK_FOR_PROVIDER				= 3;
    private static final int		PROVIDER_START_TIME_OFFSET			= 300;
    private static final int		GPS									= 0;
    private static final int		ACC									= 1;
    private static final int		BLUETOOTH_NO_LOC					= 2;
    private static final int		BLUETOOTH_LOC						= 3;
    private static final int		PHOTO								= 4;
    private static final int		POLLUTION							= 5;
    private static final int		NOISE								= 6;
    //private static final int		TASK_T1								= 1000;
    //private static final int		TASK_T2								= 2000;
    private static final int		GPS_TASK_T1							= 1000;
    private static final int		GPS_TASK_T2							= 3000;
    private static final int		ACC_TASK_T1							= 500;
    private static final int		ACC_TASK_T2							= 1500;
    private static final int		BLUETOOTH_NO_LOC_TASK_T1			= 1000;
    private static final int		BLUETOOTH_NO_LOC_TASK_T2			= 2000;
    private static final int		BLUETOOTH_LOC_TASK_T1				= 1000;
    private static final int		BLUETOOTH_LOC_TASK_T2				= 2000;
    private static final int		PHOTO_TASK_T1						= 1000;
    private static final int		PHOTO_TASK_T2						= 4000;
    private static final int		POLLUTION_TASK_T1					= 1000;
    private static final int		POLLUTION_TASK_T2					= 5000;
    private static final int		NOISE_TASK_T1						= 1000;
    private static final int		NOISE_TASK_T2						= 2500;
    private static final int		GLOBAL_DEADLINE_MIN					= 10000;
    private static final int		GLOBAL_DEADLINE_MAX					= 50000;
    private static final int		UNIT								= 10;


    

    int level = INFO;
    int tot_providers_notifications;
    Head tasklist = new Head();
    Head task_under_completition = new Head();
    Head taskrescheduled = new Head (); // or taskrescheduledandjustarrived
    Head providerlist = new Head();
    Random random = new Random(); //5);
    int maxLength;
    int tot_success_tasks;
    int tot_scheduled_completed_in_good_or_bad_tasks;
    int tot_failed_tasks;
    int tot_tasks_compleated_at_first_try;
    int tot_matched_at_task_arrival;
    double through_time;
    double earning_McSense;
    int provider_arrival_id_counter;
    int task_arrival_id_counter;
    int global_task_identifier_counter;
    Logger logger;

    long startTime = System.currentTimeMillis();
    
    Row[][] mobtraces;
    Provider[] array_provider;
    int nusers = NUMBER_OF_REGISTRED_PROVIDERS;
    int nsteps;
    
    int lung_x, lung_y;
    int slots_x, slots_y;
    
    public TaskSimulation9() throws IOException {
    	

    	//-------------------------------------------------- LOGGER
    	logger = Logger.getLogger(TaskSimulation5.class.getName());
		FileHandler fileHandler = new FileHandler("app.log", false);
		logger.addHandler(fileHandler);
		if(level== INFO) logger.setLevel(Level.INFO);
		else if(level== WARNING)logger.setLevel(Level.WARNING);
		else logger.setLevel(Level.SEVERE);
        logger.warning("START");
		
		//------------------------------------ LOAD MOBILITY TRACES
		if(LOAD_MOBILITY_TRACES){
			FileInputStream fstream = new FileInputStream("data/rwmvalues.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringTokenizer st;
			strLine = br.readLine();
			st = new StringTokenizer(strLine);
		    lung_x = Integer.parseInt(st.nextToken());
		    lung_y = Integer.parseInt(st.nextToken());
		    slots_x=lung_x/UNIT;
		    slots_y=lung_y/UNIT;
			nusers = Integer.parseInt(st.nextToken()); //1000;
			nsteps = Integer.parseInt(st.nextToken())+1; //601;
			br.close(); in.close(); fstream.close();
			//System.out.println(nusers+" - "+nsteps);
			mobtraces = new Row[nusers][nsteps];
			int user;
			int temp_time;
			double temp_x;
			double temp_y;
			logger.warning("Loading Mobility Traces in memory: "+nusers+" users for "+nsteps+" unit steps each");
			
			for(int i=0;i<nusers;i++){
				fstream = new FileInputStream("data/rwm"+i+".txt");
				in = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(in));
				for(int j=0;j<nsteps;j++){
					strLine = br.readLine();
					st = new StringTokenizer(strLine);
					while(st.hasMoreTokens()) {
						user = Integer.parseInt(st.nextToken());
						temp_time = Integer.parseInt(st.nextToken());
						temp_x = Double.parseDouble(st.nextToken());
						temp_y = Double.parseDouble(st.nextToken());
						mobtraces[user][temp_time]= new Row(temp_time,temp_x,temp_y);
					}	
				}
				System.out.println("User "+i+" loaded");
				br.close(); in.close(); fstream.close();
			}
			logger.warning("vector loaded");
		}

		//---------------------------------------- CREATE PROVIDERS
		array_provider = new Provider[nusers];
		for(int i=0;i<nusers;i++){
			array_provider[i] = new Provider(i);
		}
        logger.warning("created providers");

        //----------------------------------------------- INIT TASK
        new TaskArrival().schedule(random.randInt(INITIAL_TASK_ARRIVAL_MIN, INITIAL_TASK_ARRIVAL_MAX));
        logger.warning("init task completed");

        //------------------------------------------ RUN SIMULATION
        runSimulation(SIM_PERIOD +1000000);
        
        //-------------------------------------- PRINT MAIN RESULTS
        report();
        logger.warning("END");
    }
    
    //----------------------------------------------------------------------------------------------------------------------
    class Task extends Link {
    	int identifier;
    	int type;
    	boolean interested_in_location;
    	int x_map; // location we are interested in
    	int y_map;
    	//int sensor_required;
    	double budget;
    	int start_time;
    	int global_deadline;
    	int relative_deadline;
    	//Head providers_that_tried_task;
    	List<Integer> int_list_providers_that_tried_task;
    	int duration;
    	
        double entryTime = time();
        public Task(){
        	//----------------------------------------------------- SET BUDGET & DURATION & TYPE
        	type = random.randInt(GPS,NOISE);
        	if (type==GPS || type==ACC || type == BLUETOOTH_NO_LOC){
        		interested_in_location=false;
        		if(type == GPS) duration = random.randInt(GPS_TASK_T1,GPS_TASK_T2);
        		else if(type == ACC) duration = random.randInt(ACC_TASK_T1,ACC_TASK_T2);
        		else duration = random.randInt(BLUETOOTH_NO_LOC_TASK_T1,BLUETOOTH_NO_LOC_TASK_T2);
        	}
        	else {
        		interested_in_location=true;
        		if(type == BLUETOOTH_LOC) duration = random.randInt(BLUETOOTH_LOC_TASK_T1,BLUETOOTH_LOC_TASK_T2);
        		else if(type == PHOTO) duration = random.randInt(PHOTO_TASK_T1,PHOTO_TASK_T2);
        		else if(type == POLLUTION) duration = random.randInt(POLLUTION_TASK_T1,POLLUTION_TASK_T2);
        		else duration = random.randInt(NOISE_TASK_T1,NOISE_TASK_T2);
        		
        		//--------------------------------------- SET X & Y OF RECT WE ARE INTERESTED IN
        		x_map= random.randInt(0, slots_x-1);
        		y_map= random.randInt(0, slots_y-1);
        	}
        	
        	budget=random.uniform(BUDGET_MIN_PRICE, BUDGET_MAX_PRICE);
        	//duration=random.randInt(TASK_T1, TASK_T2); // it was for general tasks
        	
        	//-------------------------------------------------- SET GLOBAL & RELATIVE DEAD LINE
        	global_deadline = random.randInt(duration*3, 20000);
        	relative_deadline=0; // to define
        	
        	//-------------------------- SET TASK IDENTIFIER & CREATE EMPTY LIST OF PROVIDERS ID
        	int_list_providers_that_tried_task = new ArrayList<Integer>();
        	identifier=global_task_identifier_counter;
        	global_task_identifier_counter++;
        }

    }
    //----------------------------------------------------------------------------------------------------------------------
    class TaskArrival extends Event {
    	Task theTask;
    	int task_arrival_id;
    	
    	public TaskArrival(){
    		theTask = new Task();
    	}
    	
    	public Provider function_get_first_in_queue_no_matter_what(){
    		return array_provider[0];
    	}
    	
    	public Provider function_get_first_in_queue_if_available(){
			Provider matchedProvider=null;
			for(int i=0;i<nusers;i++){
				if(array_provider[i].is_available()){
					matchedProvider=array_provider[i];
					break;
				}
			}
			return matchedProvider;
    	}
    	
    	public Provider function_get_first_in_queue_available_and_price(){
			Provider matchedProvider=null;
			for(int i=0;i<nusers;i++){
				if(array_provider[i].min_price<=theTask.budget && array_provider[i].is_available()){
					matchedProvider=array_provider[i];
					break;
				}
			}
			return matchedProvider;
    	}

    	
    	public void actions() { // TaskArrival
    		if (time() <= SIM_PERIOD) {
				  //-------------------------------------------------AUTOINIT
    			new TaskArrival().schedule(time() + random.negexp((double) 1/EXPONENTIAL_TASK));
    
    			//we call the desired function which gives a provider if there is any provider			
    			//Provider matchedProvider = function_get_first_in_queue_no_matter_what();
    			//Provider matchedProvider = function_get_first_in_queue_if_available();
    			Provider matchedProvider = function_get_first_in_queue_available_and_price();
    				
    			//if there is a provider we schedule the task execution
    			if (matchedProvider != null) {
    				//theTask.out();
    				theTask.into(task_under_completition);
    				matchedProvider.number_of_task_executing++;// available=false;

    				theTask.int_list_providers_that_tried_task.add(matchedProvider.id_provider);
    				new StartTaskExecution(theTask).schedule(time());
    				logger.info("T ("+theTask.identifier+") - P ("+matchedProvider.id_provider+")");
    			}
    			else {

    				theTask.into(taskrescheduled); // we should change the name to normal task I guess
    				//System.out.println("taskreschedrule # "+taskrescheduled.cardinal());
    			}
    	
    		}
    	}
    }

    //----------------------------------------------------------------------------------------------------------------------
    class Provider extends Link {
    	int id_provider;
    	double min_price;
    	double max_price;
    	
    	int last_refresh;
    	double last_x;
    	double last_y;
    	
    	int delta;
    	int number_of_task_executing;
    	
    	public Provider(int id){
    		id_provider=id;
    		delta = random.randInt(LAMBDA_MIN,LAMBDA_MAX);
    		// ---------------------------------------------------------------------------------- INIT
    		new ProviderArrival(id).schedule(time() + PROVIDER_START_TIME_OFFSET + random.negexp((double) 1/delta));
    		
    		// ----------------------------------------------- SET # TASKS EXECUTING + MIN & MAX PRICE
    		number_of_task_executing=0;
    		double a,b;
			a = random.uniform(PROVIDER_MIN_PRICE, PROVIDER_MAX_PRICE);
	    	b = random.uniform(PROVIDER_MIN_PRICE, PROVIDER_MAX_PRICE);
	    	if(a<b) { min_price=a; max_price=b; }
	    	else { min_price=b; max_price=a; }
    	}
    	
    	public boolean is_available(){
    		if( number_of_task_executing < MAX_N_TASK_FOR_PROVIDER)
    			return true;
    		else return false;
    	}
    }
    
  //----------------------------------------------------------------------------------------------------------------------
    class ProviderArrival extends Event {
    	int provider_arrival_id;
    	int id_provider;
    	
    	public ProviderArrival(int id){
			id_provider=id;
    	}
 
    	public void actions() {
    		if (time() <= SIM_PERIOD) {
    			Provider prov = array_provider[id_provider];
    			
    			//----------------------------------------------------- AUTOINIT
    			new ProviderArrival(id_provider).schedule(time() +  random.negexp((double) 1/prov.delta));	
    			
        		provider_arrival_id=provider_arrival_id_counter;
        		provider_arrival_id_counter++;
    			tot_providers_notifications++;



    	    	// we set the provider available since he submitted the request
    	    	// to change
    	    	//prov.available=true;
    	    	
    	    	//codice in cui si guarda nei task rescheduled
    	    	Task matchedTask = (Task) taskrescheduled.first(); // nn si controlla number_of_task_executing
    	    														// quindi funziona cmq
    	    	//function_get_first_in_queue_no_matter_what();
    	    	//we remove anyway the task from the queue
    	    	//if there is a provider we schedule the task execution
    	    	if (matchedTask != null) {
    	    		//solved the problem of duplication!
    				//Provider temp = new Provider(prov.id_provider);		
    	    		//temp.into(matchedTask.providers_that_tried_task);
    	    		matchedTask.out();
    	    		//to change
        	    	//prov.available=true;
    				matchedTask.int_list_providers_that_tried_task.add(id_provider);

    	    		prov.number_of_task_executing++;
    				logger.info("P ("+id_provider+") - T ("+matchedTask.identifier+")");
    	    		new StartTaskExecution(matchedTask).schedule(time());
    	    	}
    	    	else {

    	    	}

    		}
    	}
    }
    
  //----------------------------------------------------------------------------------------------------------------------
    class StartTaskExecution extends Event {
    	
    	private Task theTask;
		public StartTaskExecution(Task t)  {
    		theTask=t;
    	}
    	public void actions() {
    		new StopTaskExecution(theTask).schedule(time() + theTask.duration);
    	}
    }
    
    //----------------------------------------------------------------------------------------------------------------------
	class StopTaskExecution extends Event {
		private Task theTask;

		StopTaskExecution(Task t) {
			theTask = t;
		}
		
		public void actions() {
			
			// retrieve the provider which completed the task
			Provider p = array_provider[theTask.int_list_providers_that_tried_task.get(0)];
			
			//there is a percentage of probability that the user will fail to complete the task
			if(random.draw((double)PERCENTAGE_TASK_FAILURE/100)){ // SUCCESS

				tot_success_tasks++;
	        	through_time += time() - theTask.entryTime;
				theTask.out();
				
				if(FIXED_EARNING_FOR_TASK) earning_McSense = earning_McSense + FIXED_EARNING_VALUE;
				else {

					//Provider p = (Provider) theTask.providers_that_tried_task.first();
					earning_McSense = earning_McSense + (p.min_price*PERCENTAGE_EARNING)/100;
				}

			}
			
			else { // FAILURE
				//we should check again and if the check fails insert the task in rescheduled_waiting
				theTask.into(taskrescheduled);
				tot_failed_tasks++;
				//new StartTaskExecution(theTask).schedule(time());

			}
			// the provider frees one slot
			p.number_of_task_executing--;
			tot_scheduled_completed_in_good_or_bad_tasks++;

    	}
	}
	//----------------------------------------------------------------------------------------------------------------------
	public static void main(String args[]) throws IOException {
		new TaskSimulation9();
	}
	
    void report() {
        System.out.println("No.of Providers = "							+ nusers);//noOfProviders);
        System.out.println("No.of Providers Notifications= "			+ tot_providers_notifications);
        System.out.println("No.of success tasks through the system = " 	+ tot_success_tasks);
        System.out.println("No.of tasks arrivals = " 					+ global_task_identifier_counter);
        System.out.println("No.of tasks failed = " 						+ tot_failed_tasks);
        System.out.println("No.of tasks sheduled = " 					+ tot_scheduled_completed_in_good_or_bad_tasks);
        System.out.println("check: "									+(tot_success_tasks+tot_failed_tasks));
        System.out.println("No.of tasks success sheduled 1st time: "	+tot_tasks_compleated_at_first_try);
        System.out.println("McSense's earnings: "	+earning_McSense+" $");
        System.out.println("Tot matched at task arrivl: "				+tot_matched_at_task_arrival);
        System.out.println("Tasklist cardinality: "	+tasklist.cardinal());
        System.out.println("Tasklist rescheduled cardinality: "			+taskrescheduled.cardinal());
        System.out.println("Tasklist under completition: "				+task_under_completition.cardinal());


        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(2);
        System.out.println("Av.elapsed time = " + fmt.format(through_time/tot_success_tasks));
        System.out.println("Maximum queue length = " +  maxLength);
        System.out.println("\nExecution time: " +
            fmt.format((System.currentTimeMillis() - startTime)/1000.0) + " secs.\n");
    }
}