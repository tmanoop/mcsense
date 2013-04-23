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


public class TaskSimulation8 extends Simulation {
	
	// CONTROL PANNEL !!!
	private static final boolean 	FIXED_EARNING_FOR_TASK 				= true; // false;
	private static final double 	FIXED_EARNING_VALUE 				= 0.10; // 10 cent
	private static final int 		PERCENTAGE_EARNING 					= 10;
	private static final int 		PERCENTAGE_TASK_FAILURE 			= 0; //11;
    private static final double 	SIM_PERIOD 							= 1000; //00;
    private static final int		NUMBER_OF_REGISTRED_PROVIDERS		= 1000;
    private static final int		TASK_T1								= 100;
    private static final int		TASK_T2								= 200;
    private static final int		INFO								= 0;
    private static final int		WARNING								= 1;
    private static final boolean    LOAD_MOBILITY_TRACES				= true;
    private static final double		PROVIDER_MIN_PRICE					= 1;
    private static final double		PROVIDER_MAX_PRICE					= 5;
    private static final double		BUDGET_MIN_PRICE					= 1;
    private static final double		BUDGET_MAX_PRICE					= 7;
    private static final int		LAMBDA_MIN							= 20;
    private static final int		LAMBDA_MAX							= 60;
    private static final int		INITIAL_TASK_ARRIVAL_MIN			= 0;
    private static final int		INITIAL_TASK_ARRIVAL_MAX			= 200;
    private static final int		EXPONENTIAL_TASK					= 11;
    private static final int		MAX_N_TASK_FOR_PROVIDER				= 3;
    private static final int		PROVIDER_START_TIME_OFFSET			= 300;

    

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
    
    public TaskSimulation8() throws IOException {
    	

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
			nusers=Integer.parseInt(st.nextToken()); //1000;
			nsteps= Integer.parseInt(st.nextToken())+1; //601;
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
		
		//-------------------------------- CREATE PROVIDERS
		array_provider = new Provider[nusers];
		for(int i=0;i<nusers;i++){
			array_provider[i] = new Provider(i);
		}
        logger.warning("created providers");

		
        //-------------------------------- INIT TASK
        new TaskArrival().schedule(random.randInt(INITIAL_TASK_ARRIVAL_MIN, INITIAL_TASK_ARRIVAL_MAX));
        logger.warning("autoinit completed");
        
        //-------------------------------- RUN SIMULATION
        runSimulation(SIM_PERIOD +1000000);
        
        //-------------------------------- PRINT MAIN RESULTS
        //report();
        logger.warning("END");
    }
    
    //----------------------------------------------------------------------------------------------------------------------
    class Task extends Link {
    	int identifier;
    	//int type;
    	//boolean interested_in_location;
    	//int location_we_are_interested_in;
    	//int duration;
    	//int sensor_required;
    	double budget;
    	int number_of_scheduling;
    	int start_time;
    	int global_deadline;
    	int relative_deadline;
    	Head providers_that_tried_task;
    	List<Integer> int_list_providers_that_tried_task;
    	int duration;
    	
        double entryTime = time();
        public Task(){
        	//number_of_scheduling=0;
        	budget=random.uniform(BUDGET_MIN_PRICE, BUDGET_MAX_PRICE);
        	duration=random.randInt(TASK_T1, TASK_T2);
        	//providers_that_tried_task = new Head();
        	int_list_providers_that_tried_task = new ArrayList<Integer>();
        	identifier=global_task_identifier_counter;
        	global_task_identifier_counter++;
    		//logger.info("task: "+identifier);
        }

        //public int getNumberOfScheduling(){ return number_of_scheduling; }
        //public void setNumberOfScheduling(int x){ number_of_scheduling=x; }
        //public void increaseNumberOfScheduling(){ number_of_scheduling++; }
        //public void setIdentifier(int x){ identifier=x; }
        //public double getBudget(){ return budget; }
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
				if(array_provider[i].available){
					matchedProvider=array_provider[i];
					break;
				}
			}
			return matchedProvider;
    	}
    	
    	public Provider function_get_first_in_queue_available_and_price(){
			Provider matchedProvider=null;
			for(int i=0;i<nusers;i++){
				if(array_provider[i].min_price<=theTask.budget && array_provider[i].available){
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
    			Provider matchedProvider = function_get_first_in_queue_if_available();
    			//Provider matchedProvider = function_get_first_in_queue_available_and_price();
    				
    			//if there is a provider we schedule the task execution
    			if (matchedProvider != null) {
    				//theTask.out();
    				theTask.into(task_under_completition);
    				matchedProvider.available=false;

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
    	boolean available;
    	
    	int last_refresh;
    	double last_x;
    	double last_y;
    	int delta;
    	int number_of_task_executing;
    	
    	public Provider(int id){
    		id_provider=id;
    		delta = random.randInt(LAMBDA_MIN,LAMBDA_MAX);
    		new ProviderArrival(id).schedule(time() + PROVIDER_START_TIME_OFFSET + random.negexp((double) 1/delta));
    	}
    }
    
  //----------------------------------------------------------------------------------------------------------------------
    class ProviderArrival extends Event {
    	int provider_arrival_id;
    	double min_price;
    	double max_price;
    	int id_provider;
    	
    	public ProviderArrival(int id){
			id_provider=id;
    	}
 
    	public void actions() {
    		if (time() <= SIM_PERIOD) {
    			Provider prov = array_provider[id_provider];
    			
    			//-----------------------------------------------------AUTOINIT
    			new ProviderArrival(id_provider).schedule(time() +  random.negexp((double) 1/prov.delta));	
    			
        		//System.out.println("provider arrival");
        		provider_arrival_id=provider_arrival_id_counter;
        		provider_arrival_id_counter++;
    			tot_providers_notifications++;

        		double a,b;
    			a = random.uniform(PROVIDER_MIN_PRICE, PROVIDER_MAX_PRICE);
    	    	b = random.uniform(PROVIDER_MIN_PRICE, PROVIDER_MAX_PRICE);
    	    	if(a<b) { min_price=a; max_price=b; }
    	    	else { min_price=b; max_price=a; }

    	    	// we set the provider available since he submitted the request
    	    	// to change
    	    	prov.available=true;
    	    	
    	    	//codice in cui si guarda nei task rescheduled
    	    	Task matchedTask = (Task) taskrescheduled.first();
    	    	//function_get_first_in_queue_no_matter_what();
    	    	//we remove anyway the task from the queue
    	    	//if there is a provider we schedule the task execution
    	    	if (matchedTask != null) {
    	    		//solved the problem of duplication!
    				//Provider temp = new Provider(prov.id_provider);		
    	    		//temp.into(matchedTask.providers_that_tried_task);
    	    		matchedTask.out();
    	    		//to change
        	    	prov.available=true;
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
			//there is a percentage of probability that the user will fail to complete the task
			if(random.draw((double)PERCENTAGE_TASK_FAILURE/100)){ // SUCCESS

				tot_success_tasks++;
	        	through_time += time() - theTask.entryTime;
				theTask.out();
				// to change
				//if(theTask.getNumberOfScheduling()==0) tot_tasks_compleated_at_first_try++;
				
				if(FIXED_EARNING_FOR_TASK) earning_McSense = earning_McSense + FIXED_EARNING_VALUE;
				else {
					Provider p = (Provider)theTask.providers_that_tried_task.first();
					earning_McSense = earning_McSense + (p.min_price*PERCENTAGE_EARNING)/100;
				}

			}
			
			else { // FAILURE
				//we should check again and if the check fails insert the task in rescheduled_waiting
				theTask.into(taskrescheduled);
				tot_failed_tasks++;
				//theTask.increaseNumberOfScheduling();
				//new StartTaskExecution(theTask).schedule(time());

			}
			
			tot_scheduled_completed_in_good_or_bad_tasks++;

    	}
	}
	//----------------------------------------------------------------------------------------------------------------------
	public static void main(String args[]) throws IOException {
		new TaskSimulation8();
	}
	
    void report() {
        System.out.println("No.of Providers = "							+ providerlist.cardinal());//noOfProviders);
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