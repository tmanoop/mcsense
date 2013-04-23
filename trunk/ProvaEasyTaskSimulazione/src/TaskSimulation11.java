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
import java.util.logging.SimpleFormatter;


public class TaskSimulation11 extends Simulation {
	
	// CONTROL PANNEL !!!
	//---------------------------------------------------------------- EARNINGS
	private static final boolean 	FIXED_EARNING_FOR_TASK 				= true; 	// false;
	private static final double 	FIXED_EARNING_VALUE 				= 0.10; 	// 10 cent
	private static final int 		PERCENTAGE_EARNING 					= 10;
	private static final int 		PERCENTAGE_TASK_FAILURE 			= 10; 		//11;
    //---------------------------------------------------------- FOR THE LOGGER
	private static final String 	LOG_FILE_NAME 						= "app.log";
    private static final int		INFO								= 0;
    private static final int		WARNING								= 1;
    private static final boolean    LOAD_MOBILITY_TRACES				= true;
    //------------------------------------------------ MIN & MAX PRICE & BUDGET
    private static final double		PROVIDER_MIN_PRICE					= 0.5;	//$
    private static final double		PROVIDER_MAX_PRICE					= 5;	//$
    private static final double		BUDGET_MIN_PRICE					= 1;	//$
    private static final double		BUDGET_MAX_PRICE					= 7;	//$
    private static final int		LAMBDA_MIN							= 1800; //30m
    private static final int		LAMBDA_MAX							= 5400; //1:30h
    private static final int		INITIAL_TASK_ARRIVAL_MIN			= 0;
    private static final int		INITIAL_TASK_ARRIVAL_MAX			= 900;	//=15m
    private static final int		EXPONENTIAL_TASK					= 300;	//=5m
    private static final int		MAX_N_TASK_FOR_PROVIDER				= 3;
    private static final int		PROVIDER_START_TIME_OFFSET			= 300;
    //------------------------------------------------------------ TASK'S TYPES
    private static final int		GPS									= 0;
    private static final int		ACC									= 1;
    private static final int		BLUETOOTH							= 2;
    private static final int		ACC_AND_GPS							= 3;
    private static final int		BLUETOOTH_AND_GPS					= 4;
    private static final int		BLUETOOTH_WITH_SPECIFIED_LOCATION	= 5;
    private static final int		PHOTO								= 6;
    private static final int		POLLUTION							= 7;
    private static final int		NOISE								= 8;
    //----------------------------------------------------------- TASK'S STATES
    private static final int		CREATED								= 0;
    private static final int		QUEUED								= 1;
    private static final int		EXECUTING							= 2;
    private static final int		COMPLETED							= 3;
    private static final int		FAILED								= 4;

    //-------------------------------------------------------- TASK'S DEADLINES
    private static final int		DEADLINE_BLUETOOTH_WSL_MIN			= 1200;	//=20m
    private static final int		DEADLINE_BLUETOOTH_WSL_MAX			= 2400;	//=40m
    private static final int		DEADLINE_PHOTO_MIN					= 1800; //=30m
    private static final int		DEADLINE_PHOTO_MAX					= 5400;	//=1:30h
    private static final int		DEADLINE_POLLUTION_OR_NOISE_MIN		= 600; 	//=10m
    private static final int		DEADLINE_POLLUTION_OR_NOISE_MAX		= 3600; //=1h

    
    private static final int		UNIT								= 10;
    //----------------------------------------------------- MATCHING "DIRECTION"
    private static final int		TP									= 0;
    private static final int		PT									= 1;
    
    private static final int		NUMBER_RELATIVE_IN_GLOBAL			= 3;
    
    private static final int		AVAILABLE_UP_TO_MIN					= 3600;		//1h
    private static final int		AVAILABLE_UP_TO_MAX					= 36000;	//10h
    
    //----------------------------------------- TO USE AS SELECTERS IN LOG FILE
    private static final int		TASK_GENERATION						= 0;
    private static final int		MATCHING_CODE						= 1;
    private static final int		PROVIDER_ARRIVAL					= 2;
    private static final int		FINISHED_TASK						= 3;
    
    private static final int		FAIL								= 0;
    private static final int		SUCCESS								= 1;


	boolean dont_consider_price 					= false;
	boolean dont_consider_availability 				= false;
	boolean dont_consider_location 					= false;
	boolean consider_failed_provider 				= false;
	boolean dont_consider_max_tasks_for_provider 	= false;
	boolean dont_consider_global_deadline			= true;


    int level = INFO;
    int tot_providers_notifications;
    Head task_under_completition = new Head();
    Head tasklist = new Head ();
    Random random = new Random(); //5); // a fixed seed will always give the same results
    int max_tasklist_length;
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
    static int nusers;
    static int nsteps;
    
    
    int lung_x, lung_y;
    int slots_x, slots_y;
    
    public TaskSimulation11() throws IOException {
    	
    	//-------------------------------------------------- LOGGER
    	logger = Logger.getLogger(TaskSimulation5.class.getName());
		FileHandler fileHandler = new FileHandler(LOG_FILE_NAME, false);
		SimpleFormatter formatterTxt = new SimpleFormatter();
        fileHandler.setFormatter(formatterTxt);
		logger.addHandler(fileHandler);
		if(level== INFO) logger.setLevel(Level.INFO);
		else if(level== WARNING)logger.setLevel(Level.WARNING);
		else logger.setLevel(Level.SEVERE);
        logger.config("START");
		
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
			mobtraces = new Row[nusers][nsteps];
			int user;
			int temp_time;
			double temp_x;
			double temp_y;
			logger.config("Loading Mobility Traces in memory: "+nusers+" users for "+nsteps+" unit steps each");
			
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
			logger.config("vector loaded");
		}

		//---------------------------------------- CREATE PROVIDERS
		array_provider = new Provider[nusers];
		for(int i=0;i<nusers;i++){
			array_provider[i] = new Provider(i);
		}
        logger.config("created providers");

        //----------------------------------------------- INIT TASK
        new TaskArrival().schedule(random.randInt(INITIAL_TASK_ARRIVAL_MIN, INITIAL_TASK_ARRIVAL_MAX));
        logger.config("init task completed");

        //------------------------------------------ RUN SIMULATION
        runSimulation(nsteps);
        
        //-------------------------------------- PRINT MAIN RESULTS
        report();
        logger.config("END");
    }
    
    //----------------------------------------------------------------------------------------------------------------------
    class Task extends Link {
    	int identifier;
    	int type;
    	int state;
    	boolean interested_in_location;
    	int x_map; // location we are interested in
    	int y_map; // location we are interested in
    	double budget;
    	int start_time;
    	int global_deadline;
    	int global_deadline_in_simulation;
    	int relative_deadline;
    	int relative_deadline_in_simulation;
    	List<Integer> int_list_providers_that_tried_task;
    	int duration;
    	
        double entry_time_in_queue;
    	double time_in_queue;
    	
        double entryTime = time();
        
        public Task(){
        	//----------------------------------------------------- SET BUDGET & DURATION & TYPE
        	
        	// first of all we get the type since the other variables will depend on it
        	type = random.randInt(GPS,NOISE); 
        	if (type==GPS || type==ACC || type==BLUETOOTH || type==ACC_AND_GPS || type==BLUETOOTH_AND_GPS){
        		interested_in_location=false;
        		global_deadline = nsteps;
        		duration= random.randInt(3600, 36000); // [1h,10h] // Manoop fixed 9h (OK)
        		if(type==BLUETOOTH) duration = 0;
        		//if(type == BLUETOOTH_AND_GPS) duration = 0;
        	}
        	else {
        		interested_in_location=true;
        		duration=0;
        		if(type == BLUETOOTH_WITH_SPECIFIED_LOCATION){ // EVENTS (CONCERTS)
        			global_deadline = random.randInt(DEADLINE_BLUETOOTH_WSL_MIN,DEADLINE_BLUETOOTH_WSL_MAX);
        		}
        		else if(type == PHOTO) {
        			global_deadline = random.randInt(DEADLINE_PHOTO_MIN,DEADLINE_PHOTO_MAX);
        			duration = random.randInt(duration, global_deadline); // particular case!
        		}
        		else { // POLLUTION & NOISE
        			global_deadline = random.randInt(DEADLINE_POLLUTION_OR_NOISE_MIN,DEADLINE_POLLUTION_OR_NOISE_MAX);        			
        		}
        		
        		//--------------------------------------- SET X & Y OF RECT WE ARE INTERESTED IN
        		x_map= random.randInt(0, slots_x-1);
        		y_map= random.randInt(0, slots_y-1);
        	}
        	
        	//--------------------------------------------------- SET GLOBAL & RELATIVE DEADLINE
        	relative_deadline = (int) Math.floor((double)global_deadline/NUMBER_RELATIVE_IN_GLOBAL);
        	global_deadline_in_simulation = (int)time()+global_deadline;
        	relative_deadline_in_simulation = (int)time() + relative_deadline;
        	
        	// to see if we want to differentiate them
        	budget=random.uniform(BUDGET_MIN_PRICE, BUDGET_MAX_PRICE);        	

        	
        	//-------------------------- SET TASK IDENTIFIER & CREATE EMPTY LIST OF PROVIDERS ID
        	int_list_providers_that_tried_task = new ArrayList<Integer>();
        	
        	identifier=global_task_identifier_counter;
        	global_task_identifier_counter++;
        	
        	//--------------------------------------------------------------------- STATE UPDATE
        	state = CREATED;
        	time_in_queue=0;
        }

    }
    
    class DeadlineCheck extends Event {
    	Task theTask;
    	
    	public DeadlineCheck(Task t){
    		theTask=t;	
    	}
 
    	public void actions() {
    		if(theTask.state!=COMPLETED){
    			System.out.println("deadline "+theTask.global_deadline_in_simulation+" not respected");
    			theTask.state=FAILED;
				logger.info(FINISHED_TASK+" "+FAIL );
    		}
    		theTask.out();
    	}
    	
    }
    //----------------------------------------------------------------------------------------------------------------------
    class TaskArrival extends Event {
    	Task theTask;
    	int task_arrival_id;
    	
    	public TaskArrival(){
    		theTask = new Task();
    		//when we create the task we also lunch a deadline check for the task
			new DeadlineCheck(theTask).schedule(theTask.global_deadline_in_simulation);

       	}
  	
    	public void actions() { // TaskArrival
    		if (time() <= nsteps) {
				//-------------------------------------------------AUTOINIT
    			new TaskArrival().schedule(time() + random.negexp((double) 1/EXPONENTIAL_TASK));
    
				//---------------------------------------- MATCHED PROVIDER
    			Provider matchedProvider = matchTaskWithProvider(theTask);
    			start_matched_TP (theTask, matchedProvider, TP);
    	
    		}
    	}
    }

    //----------------------------------------------------------------------------------------------------------------------
    class Provider {
    	int id_provider;
    	double min_price;
    	double max_price;
    	
    	int delta;
    	int number_of_task_executing;
    	int available_up_to;
    	
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
    		// ----------------------------------------------- AVAILABILITY
	    	update_availability();  	
	    	}
    	
    	public void update_availability(){
	    	available_up_to= (int)time() + random.randInt(AVAILABLE_UP_TO_MIN, AVAILABLE_UP_TO_MAX);
    	}
    	
    	public boolean is_respecting_max_n_tasks_for_provider(){
    		if( number_of_task_executing < MAX_N_TASK_FOR_PROVIDER)
    			return true;
    		else return false;
    	}
    	
    	public boolean is_available(){
    		if(time()<available_up_to)
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
    		if (time() <= nsteps) {
    			
    			Provider prov = array_provider[id_provider];
    			
    			//----------------------------------------------------- AUTOINIT
    			double base = time();
    			if(prov.available_up_to>base) {
    				base = prov.available_up_to;
    			}
    			new ProviderArrival(id_provider).schedule(base +  random.negexp((double) 1/prov.delta));	

    			
        		provider_arrival_id=provider_arrival_id_counter;
        		provider_arrival_id_counter++;
    			tot_providers_notifications++;
    			
    	    	prov.update_availability();  	

    			if(prov.is_available() && prov.is_respecting_max_n_tasks_for_provider()){
    	    	
	    	    	Task matchedTask = matchProviderWithTask(prov);
	    			start_matched_TP (matchedTask, prov, PT);

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
			
			theTask.time_in_queue = theTask.time_in_queue + time()-theTask.entry_time_in_queue;
			
			//there is a percentage of probability that the user will fail to complete the task
			if(random.draw((double)PERCENTAGE_TASK_FAILURE/100)){ // SUCCESS
				
				tot_success_tasks++;
				double tt= time() - theTask.entryTime;
	        	through_time += tt;
				theTask.out();
				theTask.state=COMPLETED;
				
				if(FIXED_EARNING_FOR_TASK) earning_McSense = earning_McSense + FIXED_EARNING_VALUE;
				else {
					earning_McSense = earning_McSense + (p.min_price*PERCENTAGE_EARNING)/100;
				}
				
				logger.info(FINISHED_TASK+" "+SUCCESS+" "+theTask.type+" "
						+p.min_price +" "+tt+" "+theTask.time_in_queue+" "
						+theTask.int_list_providers_that_tried_task.size());


			}
			
			else { // FAILURE
				tot_failed_tasks++;
				// we immediately try to find another provider for the task that has just failed
    			Provider matchedProvider = matchTaskWithProvider(theTask);
    			start_matched_TP (theTask, matchedProvider, TP);
			}
			tot_scheduled_completed_in_good_or_bad_tasks++;

			// the provider frees one slot
			p.number_of_task_executing--;
			// it is possible that the provider that freed itself from the task is now capable of getting another task
			if(p.is_available() && p.is_respecting_max_n_tasks_for_provider()){    	
    	    	Task matchedTask = matchProviderWithTask(p);
    			start_matched_TP (matchedTask, p, PT);
			}

    	}
	}
	//----------------------------------------------------------------------------------------------------------------------
	public static void main(String args[]) throws IOException {
		new TaskSimulation11();
	}
	
	
	public void refresh_max_queue_value(){
        int qLength = tasklist.cardinal();
        if (max_tasklist_length < qLength)
      	  max_tasklist_length = qLength;
	}
	
	public Task matchProviderWithTask(Provider theProvider){
		Task matchedTask = (Task) tasklist.first();
		boolean matching = false;
		while(matchedTask!=null){
			
			if(		( dont_consider_price 		 || 	theProvider.min_price<=matchedTask.budget ) 	&& 
					( dont_consider_location	 ||		!matchedTask.interested_in_location ||
							does_task_location_match_provider_location(matchedTask, theProvider)	) &&
					(consider_failed_provider     || 	
							!is_provider_in_list(theProvider,matchedTask.int_list_providers_that_tried_task)  ) &&
					(dont_consider_global_deadline || 
							theProvider.available_up_to + matchedTask.duration 
								<= matchedTask.global_deadline_in_simulation)
																					){	
				
				matching=true;
				break;
			}
			if(!matching) {
				matchedTask = (Task) matchedTask.suc();
			}
		}
		if(matching) return matchedTask;
		else return null;
	}
	
	public Provider matchTaskWithProvider(Task theTask){
		Provider matchedProvider=null;
		for(int i=0;i<nusers;i++){
			
			if(	( dont_consider_price 		 || 	array_provider[i].min_price<=theTask.budget ) 	&& 
				( dont_consider_availability ||		array_provider[i].is_available())				&&
				( dont_consider_max_tasks_for_provider ||		
					array_provider[i].is_respecting_max_n_tasks_for_provider())						&&
				( dont_consider_location	 ||		!theTask.interested_in_location ||
					does_task_location_match_provider_location(theTask, array_provider[i])		) 	&&
				(consider_failed_provider     || 	
					!is_provider_in_list(array_provider[i],theTask.int_list_providers_that_tried_task)  ) &&
					(dont_consider_global_deadline || 
							array_provider[i].available_up_to + theTask.duration 
								<= theTask.global_deadline_in_simulation)
																					){
				matchedProvider=array_provider[i];
				break;		
			
				
			}
		}
		return matchedProvider;
	}
	
	public boolean does_task_location_match_provider_location(Task t, Provider p){
		Row r;
		r = mobtraces[p.id_provider][(int) time()];
		if(		r.x >= t.x_map*UNIT && r.x <= (t.x_map+1)*UNIT 		&&
				r.y >= t.y_map*UNIT && r.y <= (t.y_map+1)*UNIT		 ) 
			return true;	
		return false;
	}
	
	public boolean is_provider_in_list(Provider p, List<Integer> l){
		return l.contains(p.id_provider);
	}
	
	public void start_matched_TP (Task t, Provider p, int direction){
		
		if (p != null && t != null) { //if there is a provider we schedule the task execution
			
			t.state = EXECUTING;

			t.into(task_under_completition);
			p.number_of_task_executing++;
			t.int_list_providers_that_tried_task.add(p.id_provider);
			
			Row r = mobtraces[p.id_provider][(int)time()];

			if (direction==TP) 
				logger.info(MATCHING_CODE+" "+direction+" T "+t.identifier+" - P "+p.id_provider+" "
						+t.budget+" > "+p.min_price+" "+t.type+" "+t.interested_in_location+
						" ["+t.x_map+","+t.y_map+"] ["+r.x+","+r.y+"]");	
			else
				logger.info(MATCHING_CODE+" "+direction+" P "+p.id_provider+" - T "+t.identifier+" "
						+t.budget+" > "+p.min_price+" "+t.type+" "+t.interested_in_location+
						" ["+t.x_map+","+t.y_map+"] ["+r.x+","+r.y+"]");

			new StartTaskExecution(t).schedule(time());
		}
		else {
			if(direction==TP) {
				t.entry_time_in_queue=time();
				t.into(tasklist);
				refresh_max_queue_value();

			}
			if(t!=null) t.state = QUEUED;

		}

	}

	
    void report() {
        System.out.println("No.of Providers = "							+ nusers);
        System.out.println("No.of Providers Notifications= "			+ tot_providers_notifications);
        System.out.println("No.of success tasks through the system = " 	+ tot_success_tasks);
        System.out.println("No.of tasks arrivals = " 					+ global_task_identifier_counter);
        System.out.println("No.of tasks failed = " 						+ tot_failed_tasks);
        System.out.println("No.of tasks sheduled = " 					+ tot_scheduled_completed_in_good_or_bad_tasks);
        System.out.println("check: "									+(tot_success_tasks+tot_failed_tasks));
        System.out.println("No.of tasks success sheduled 1st time: "	+tot_tasks_compleated_at_first_try);
        System.out.println("McSense's earnings: "						+earning_McSense+" $");
        System.out.println("Tot matched at task arrivl: "				+tot_matched_at_task_arrival);
        System.out.println("Tasklist rescheduled cardinality: "			+tasklist.cardinal());
        System.out.println("Tasklist under completition: "				+task_under_completition.cardinal());


        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(2);
        System.out.println("Av.elapsed time = " + fmt.format(through_time/tot_success_tasks));
        System.out.println("Maximum queue length = " +  max_tasklist_length);
        System.out.println("\nExecution time: " +
            fmt.format((System.currentTimeMillis() - startTime)/1000.0) + " secs.\n");
    }
}