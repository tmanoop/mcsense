import simulation.event.*;
import simset.*;
import random.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TaskSimulation23 extends Simulation {
	
	// CONTROL PANNEL !!!
	//---------------------------------------------------------------- EARNINGS
	private static final boolean 	FIXED_EARNING_FOR_TASK 				= false; //true; 	// false;
	private static final double 	FIXED_EARNING_VALUE 				= 0.10; 	// 10 cent
	private static final int 		PERCENTAGE_EARNING 					= 10;
	private static final int 		PERCENTAGE_TASK_FAILURE 			= 10; 		//11;
    //---------------------------------------------------------- FOR THE LOGGER
	private static final String 	LOG_FILE_NAME 						= "app.log";
    private static final int		INFO								= 0;
    private static final int		WARNING								= 1;
    //private static final boolean    LOAD_MOBILITY_TRACES				= true;
    //------------------------------------------------ MIN & MAX PRICE & BUDGET
    private static final double		BUDGET_MIN_PRICE					= 0;	//$
    private static final double		BUDGET_MAX_PRICE					= 5;	//$
    private static final double		PROVIDER_MIN_PRICE					= BUDGET_MIN_PRICE; //0.5;	//$
    private static final double		PROVIDER_MAX_PRICE					= BUDGET_MAX_PRICE; //5;	//$
    private static final int		LAMBDA_MIN							= 1800; //30m
    private static final int		LAMBDA_MAX							= 5400; //1:30h
    private static final int		INITIAL_TASK_ARRIVAL_MIN			= 0;
    private static final int		INITIAL_TASK_ARRIVAL_MAX			= 900;	//=15m
    private static final int		EXPONENTIAL_TASK					= 20; //60; //300; //60; //300;	//=5m
    private static final int		MAX_N_TASK_FOR_PROVIDER				= 3;
    private static final int		PROVIDER_START_TIME_OFFSET			= 300;
    //------------------------------------------------------------ TASK'S TYPES
    private static final int		GPS									= 0;
    private static final int		ACC									= 1;
    private static final int		BLUETOOTH							= 2;
    private static final int		ACC_AND_GPS							= 3;
    private static final int		BLUETOOTH_AND_GPS					= 4;
    private static final int		REGULAR_PHOTO						= 5;
    private static final int		BLUETOOTH_WITH_SPECIFIED_LOCATION	= 6;
    private static final int		PHOTO								= 7;
    private static final int		POLLUTION							= 8;
    private static final int		NOISE								= 9;
    //----------------------------------------------------------- TASK'S STATES
    private static final int		CREATED								= 0;
    private static final int		QUEUED								= 1;
    private static final int		EXECUTING							= 2;
    private static final int		TO_RESCHEDULE						= 2;
    private static final int		COMPLETED							= 4;
    private static final int		FAILED								= 5;

    //-------------------------------------------------------- TASK'S DEADLINES
    private static final int		DEADLINE_BLUETOOTH_WSL_MIN			= 1200;	//=20m
    private static final int		DEADLINE_BLUETOOTH_WSL_MAX			= 2400;	//=40m
    private static final int		DEADLINE_PHOTO_MIN					= 1800; //=30m
    private static final int		DEADLINE_PHOTO_MAX					= 5400;	//=1:30h
    private static final int		DEADLINE_POLLUTION_OR_NOISE_MIN		= 600; 	//=10m
    private static final int		DEADLINE_POLLUTION_OR_NOISE_MAX		= 3600; //=1h
    private static final int		DEADLINE_LONGEST_MIN				= 259200; 	//=3days
    private static final int		DEADLINE_LONGEST_MAX				= 604800;	//=7days

    
    private static final int		UNIT								= 10; //20; //10;
    //----------------------------------------------------- MATCHING "DIRECTION"
    private static final int		TP									= 0;
    private static final int		PT									= 1;
        
    private static final int		AVAILABLE_UP_TO_MIN					= 3600;		//1h
    private static final int		AVAILABLE_UP_TO_MAX					= 36000;	//10h
    
    //----------------------------------------- TO USE AS SELECTERS IN LOG FILE
    private static final int		PROVIDER_GENERATION					= 0;
    private static final int		TASK_GENERATION						= 4;
    private static final int		MATCHING_CODE						= 1;
    private static final int		PROVIDER_ARRIVAL					= 2;
    private static final int		FINISHED_TASK						= 3;
    
    private static final int		FAIL								= 0;
    private static final int		SUCCESS								= 1;
    //----------------------------------------------------------- QUEUE OPTIONS
    private static final int		FIFO								= 0;
    private static final int		LIFO								= 1;
    private static final int		PRIORITY_DEADLINE					= 2;
    
    private static final int		CATEGORY_A							= 0;
    private static final int		CATEGORY_B							= 1;
    private static final int		CATEGORY_C							= 2;
    
    private static final int		GPS_DURATION_MIN					= 3600;
    private static final int		GPS_DURATION_MAX					= 36000;
    
    private static final boolean	TRACK_PROVIDERS						= true; //false; //true;
    
    //---------------------------------------------------------- SMART POLICIES
    private static final int		FIRST								= 1;
    private static final int		LAST								= 2;
    
    private static final int		MIN_AVAILABILITY_DEADLINE			= 3;
    private static final int		MIN_BUDGET_PRICE 					= 4;
    private static final int		MAX_BUDGET_PRICE					= 5;
    private static final int		NORMALIZE_MONEY						= 6;
    private static final int		NORMALIZE_TASKS						= 7;
    private static final int		NORMALIZE_MONEY_ON_AVAILABILITY		= 8;
    private static final int		NORMALIZE_TASKS_ON_AVAILABILITY		= 9;
    private static final int		MAXIMIZE_GLOBAL_RELIABILITY			= 10;
    private static final int		MAXIMIZE_SLIDING_WINDOW_RELIABILITY = 11;
    private static final int		MAXIMIZE_WEIGHTED_RELIABILITY		= 12;
    
    private static final int		FIRST_LOCATION_DEPENDENT			= 50;
    private static final int		CLOSEST_DEADLINE					= 51;
    private static final int		CLOSEST_DEADLINE_LOCATION_DEPENDENT	= 52;
    
    
    //------------------------------------------------------------- RELIABILITY
    private static final int		SLIDING_WINDOW_SIZE					= 8;
    private static final int		MIN_RELIABILITY						= 0;
    private static final int		MAX_RELIABILITY						= 100;
    private static final int		RELIABILITY_PENALTY					= 3;
    private static final int		RELIABILITY_REWARD					= 1;
    
    private static final int		N_TASKS_TO_TEST						= 1000;
    private static final boolean	LIMIT_TASKS_TO_GET_IN 				= false;
    private static final int		N_TASKS_TO_LET_IN					= 100;
    
    private static final int		FIXED_EQUAL_AMMOUNT_TASKS_IN_AND_OUT= 1;
    private static final int		FIXED_AMMOUNT_TASKS_OUT				= 2;
    
    private static final int		REWARDING_PENALIZING_METHOD_SUM_SUB	= 1;
    private static final int		REWARDING_PENALIZING_METHOD_SUM_DIV	= 2;
    
    private static final double		WEIGHT_GLOBAL_RELIABILITY			= 1;
    private static final double		WEIGHT_SLIDING_WINDOW				= 1 - WEIGHT_GLOBAL_RELIABILITY;
    
    private static final int		NUMEBER_TASKS_FOR_REDEMPTION		= 5;
    
    private static final double		OFFSET_MATCHING						= 0.1;



    
    int queuing_modality= FIFO; //PRIORITY_DEADLINE; //LIFO;
    int policy_TP = MAXIMIZE_WEIGHTED_RELIABILITY; //MAXIMIZE_GLOBAL_RELIABILITY; //FIRST; //NORMALIZE_TASKS_ON_AVAILABILITY; //MIN_BUDGET_PRICE; //FIRST; //NORMALIZE_MONEY; //NORMALIZE_TASKS; //FIRST; //MIN_BUDGET_PRICE; //MIN_AVAILABILITY_DEADLINE; //LAST; //FIRST;
    int policy_PT = CLOSEST_DEADLINE_LOCATION_DEPENDENT; //FIRST; //_LOCATION_DEPENDENT;
    int second_policy_PT = CLOSEST_DEADLINE;
	int termination=FIXED_AMMOUNT_TASKS_OUT;
	int rewarding_penalizing_method = REWARDING_PENALIZING_METHOD_SUM_SUB;
	
    boolean take_into_consideration_just_number_of_tasks = true; //false; //true; // ok
    int enne_steps= 50000;
	
    java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();

    long startTime = System.currentTimeMillis();
    Logger logger;
    
    Random random = new Random(5); //5); //5); // a fixed seed will always give the same results

    Head task_under_completion = new Head();
    Head tasklist = new Head ();
    //Head provider_subset = new Head();
    
    Head in_matching_process_tasks = new Head();
    Head in_matching_process_provider = new Head();


    int level = INFO;
    int tot_providers_notifications;
    int max_tasklist_length;
    int tot_success_tasks;
    int tot_scheduled_completed_in_good_or_bad_tasks;
    int tot_failed_tasks;
    int tot_tasks_compleated_at_first_try;
    int tot_matched_at_task_arrival;
    int provider_arrival_id_counter;
    int task_arrival_id_counter;
    int global_task_identifier_counter;
    
    double through_time;
    double earning_McSense;
    
    Row[][] mobtraces;
    Provider[] array_provider;
    int nusers;
    int nsteps;
    
    int lung_x, lung_y;
    int slots_x, slots_y;
    
    int n_tasks_tested=0;
    int matchings=0;
    int deadline_not_respected=0;
	int provider_availability_error=0;
    int n_round=0;
    
    boolean case_location_policy = false;
    boolean over=false;

	FileInputStream fstream[];
	DataInputStream in[];
	BufferedReader br[];

	int time_matching=-1;

		
	public void check_termination_condition(){

		if(termination==FIXED_EQUAL_AMMOUNT_TASKS_IN_AND_OUT){
			if(		take_into_consideration_just_number_of_tasks &&
					tasklist.empty() && task_under_completion.empty() &&
					(tot_success_tasks + deadline_not_respected) == N_TASKS_TO_TEST				){
						over=true;			
			}
		}
		else if(termination==FIXED_AMMOUNT_TASKS_OUT){
			if(tot_success_tasks + deadline_not_respected == N_TASKS_TO_TEST){
				System.out.println((int)time()+" OVER");
				over=true;
			}
		}
		
		if(over) stopSimulation();
		
	}
    
    public void load_mobility_traces() throws IOException{
    	System.out.println("ROUND: "+n_round);
		String strLine;
		StringTokenizer st;
		int user;
		int temp_time;
		double temp_x;
		double temp_y;
    	if(n_round==0){
    		FileInputStream ffstream;
    		DataInputStream iin;
    		BufferedReader bbr;
			ffstream = new FileInputStream("data/rwmvalues.txt");
			iin = new DataInputStream(ffstream);
			bbr = new BufferedReader(new InputStreamReader(iin));
			strLine = bbr.readLine();
			st = new StringTokenizer(strLine);
		    lung_x = Integer.parseInt(st.nextToken());
		    lung_y = Integer.parseInt(st.nextToken());
		    slots_x=lung_x/UNIT;
		    slots_y=lung_y/UNIT;
			nusers = Integer.parseInt(st.nextToken()); //1000;
			nsteps = Integer.parseInt(st.nextToken())+1; //601;
			bbr.close(); iin.close(); ffstream.close();
			
			mobtraces = new Row[nusers][enne_steps]; //nsteps]; // reduced the array
			
			fstream = new FileInputStream[nusers];
			in = new DataInputStream[nusers];
			br = new BufferedReader[nusers];		
			for(int i=0;i<nusers;i++){
				fstream[i] = new FileInputStream("data/rwm"+i+".txt");
				in[i] = new DataInputStream(fstream[i]);
				br[i] = new BufferedReader(new InputStreamReader(in[i]));
			}
    	}
			
		for(int i=0;i<nusers;i++){
			for(int j=0;j<get_min_steps_for_round_load();j++){
				strLine = br[i].readLine();
				st = new StringTokenizer(strLine);
				while(st.hasMoreTokens()) {
					user = Integer.parseInt(st.nextToken());
					temp_time = Integer.parseInt(st.nextToken());
					temp_x = Double.parseDouble(st.nextToken());
					temp_y = Double.parseDouble(st.nextToken());
					mobtraces[user][temp_time-(n_round*enne_steps)]= new Row(temp_time,temp_x,temp_y);
				}	
			}
			System.out.println("User "+i+" loaded");
		}
		n_round++;
    }
    
    public int get_min_steps_for_round_load(){
    	int temp=enne_steps;
    	int temp2= nsteps - (n_round*enne_steps);
    	if(temp2<temp) temp = temp2;
    	return temp;
    }
    
    public void close_file_input_streams() throws IOException{
		for(int i=0;i<nusers;i++){
			br[i].close(); in[i].close(); fstream[i].close();
		}
    }
    
    public TaskSimulation23() throws IOException {
    	
        fmt.setMaximumFractionDigits(2);
    	
    	//-------------------------------------------------- LOGGER
    	logger = Logger.getLogger(TaskSimulation5.class.getName());
		FileHandler fileHandler = new FileHandler(LOG_FILE_NAME, false);
		SimpleFormatter formatterTxt = new SimpleFormatter();
        fileHandler.setFormatter(formatterTxt);
		logger.addHandler(fileHandler);
		if(level== INFO) logger.setLevel(Level.INFO);
		else if(level== WARNING)logger.setLevel(Level.WARNING);
		else logger.setLevel(Level.SEVERE);
		
		//------------------------------------ LOAD MOBILITY TRACES
        load_mobility_traces();
        
		//----------------------------------------- FOR POLICIES PT
        if(policy_PT==FIRST_LOCATION_DEPENDENT	|| policy_PT==CLOSEST_DEADLINE_LOCATION_DEPENDENT)
        	case_location_policy=true;

		//---------------------------------------- CREATE PROVIDERS
		array_provider = new Provider[nusers];
		for(int i=0;i<nusers;i++){
			array_provider[i] = new Provider(i);
		}

        //----------------------------------------------- INIT TASK
        new TaskArrival().schedule(random.randInt(INITIAL_TASK_ARRIVAL_MIN, INITIAL_TASK_ARRIVAL_MAX));

        //------------------------------------------ RUN SIMULATION
        runSimulation(nsteps);
        
        //-------------------------------------- PRINT MAIN RESULTS
        close_file_input_streams();
        report(); // will be "upgraded"
    	print_list_in_file(task_under_completion, "completion.txt");
    	print_list_in_file(tasklist, "queue.txt");
        print_summary_for_script_analyzer();
        print_providers_info();
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
    	int global_deadline_in_simulation;
    	List<Integer> int_list_providers_that_tried_task;
    	int duration;
    	int could_have_been_done_by;
    	
    	double price_assigned;
    	
        double entryTime = (int)time();
        boolean lock = false;
        
    	List<Integer> provider_id_that_can_do_the_task;
    	List<Integer> provider_points_that_can_do_the_task;

        
        public Task(){
        	//-------------------------------------- SET DURATION & TYPE & GLOBAL DEADLINE
        	// first of all we get the type since the other variables will depend on it
        	int global_deadline;
        	type = random.randInt(GPS,NOISE); 
        	if (type==GPS || type==ACC || type==BLUETOOTH || type==ACC_AND_GPS || 
        			type==BLUETOOTH_AND_GPS || type==REGULAR_PHOTO){
        		interested_in_location=false;
        		global_deadline = random.randInt(DEADLINE_LONGEST_MIN,DEADLINE_LONGEST_MAX);
            	global_deadline_in_simulation = (int)time() + global_deadline;
        		duration= random.randInt(GPS_DURATION_MIN, GPS_DURATION_MAX); // [1h,10h] // Manoop fixed 9h (OK)
        		if(type==BLUETOOTH) duration = 0;
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
        		
            	global_deadline_in_simulation = (int)time() + global_deadline;
        		
        		//--------------------------------------- SET X & Y OF RECT WE ARE INTERESTED IN
        		x_map= random.randInt(0, slots_x-1);
        		y_map= random.randInt(0, slots_y-1);
        	}
        	
        	
    		//--------------------------------------------------------------------------- BUDGET
        	budget=random.uniform(BUDGET_MIN_PRICE+get_min_offset_budget(type), 
        			BUDGET_MAX_PRICE-get_max_offset_budget(type));        	

        	
        	//-------------------------- SET TASK IDENTIFIER & CREATE EMPTY LIST OF PROVIDERS ID
        	int_list_providers_that_tried_task = new ArrayList<Integer>();
        	provider_id_that_can_do_the_task = new ArrayList<Integer>();
        	provider_points_that_can_do_the_task = new ArrayList<Integer>();
        	
        	identifier=global_task_identifier_counter;
        	global_task_identifier_counter++;
        	
        	//--------------------------------------------------------------------- STATE UPDATE
        	state = CREATED;
        	logger.info((int)time()+" "+TASK_GENERATION+" "+identifier+" "+type+" "
        			+budget+" "
        			+duration+" "+interested_in_location);
        }

    }
    //----------------------------------------------------------------------------------------------------------------------
    class DeadlineCheck extends Event {
    	Task theTask;
    	
    	public DeadlineCheck(Task t){
    		theTask=t;	
    	}
 
    	public void actions() {
    		if(!over){
	    		theTask.out();
	    	 	if(theTask.state!=COMPLETED){
	    			deadline_not_respected++;
	    			System.out.println("#"+deadline_not_respected+" deadline "+theTask.global_deadline_in_simulation+" not respected");
	    			theTask.state=FAILED;
					logger.info((int)time()+" "+
							FINISHED_TASK+" "+FAIL+" "+theTask.identifier+" "+theTask.type+" "+
							(int)theTask.entryTime+" "+theTask.duration+" "+
							(int)time()+" "+theTask.global_deadline_in_simulation);
					
					check_termination_condition();
	
	    		}
    		}
    	}
    	
    }
    //----------------------------------------------------------------------------------------------------------------------
	class CheckForProviderMovement extends Event {
		private Task theTask;

		CheckForProviderMovement(Task t) {
			theTask = t;
		}
		
		public void actions() {
			if(!over){
				try {
					matchTaskWithProvider(theTask);
				} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
    //----------------------------------------------------------------------------------------------------------------------
    class TaskArrival extends Event {
    	Task theTask;

    	public void actions() { // TaskArrival
    		if(!over){
	    		if ( ((int)time() <= nsteps && !LIMIT_TASKS_TO_GET_IN) ||
	    				((int)time() <= nsteps && LIMIT_TASKS_TO_GET_IN && n_tasks_tested<N_TASKS_TO_LET_IN)) {
	    			theTask = new Task(); // BUG SOLVED
	
					//-------------------------------------------------AUTOINIT
	    			new TaskArrival().schedule((int)(time() + random.negexp((double) 1/EXPONENTIAL_TASK)));
	    
					//---------------------------------------- MATCHED PROVIDER
					try {
						matchTaskWithProvider(theTask);
					} catch (IOException e) {e.printStackTrace();}	    			
	    			
	    			//when we create the task we also lunch a deadline check for the task
	    			new DeadlineCheck(theTask).schedule(theTask.global_deadline_in_simulation);
	    			n_tasks_tested++;
	    	
	    		}
    		}
    	}
    }

    //----------------------------------------------------------------------------------------------------------------------
    class Provider extends Link{
    	int id_provider;
    	double min_price[] = new double[3];
    	double max_price[] = new double[3];
    	
    	int delta;
    	int number_of_task_executing=0;
    	int available_up_to=0;
    	
    	int number_task_received=0;
    	int number_task_succeeded=0;
    	double money_made=0;
    	
    	double ipotetical_money=0;
    	
    	int time_has_been_available=0;
    	int cumulator=0; // offset
    	
    	int time_availability_sent;
    	boolean keep_updating=false;
    	
    	int reliability;
    	int[] sliding_window = new int[SLIDING_WINDOW_SIZE];
    	
    	boolean has_been_a_very_bad_provider 	= false;
    	boolean has_been_a_very_good_provider 	= false;
    	boolean is_been_a_very_bad_provider 	= false;
    	boolean is_been_a_very_good_provider 	= false;
    	
    	boolean need_redemption = false;
    	
    	
    	public int get_updated_sum_availability(){
    		if(keep_updating){
    			cumulator = (int)time() - time_availability_sent;
	    		if((int)time()>=available_up_to){
	    			keep_updating=false;
	    			cumulator=0;
	    			time_has_been_available=time_has_been_available+available_up_to-time_availability_sent;
	    		}
    		}
    		return (time_has_been_available + cumulator);
    	}
    	
    	public void update_tail_vector(){
    		for(int i=SLIDING_WINDOW_SIZE-1; i>0;i--){
    			sliding_window[i]=sliding_window[i-1];
    		}
    	}
    	
    	public double get_succ_in_sliding_window(){
    		int result = 0;
    		for(int i=0; i<SLIDING_WINDOW_SIZE; i++){
    			result = result + sliding_window[i];
    		}
    		double moltiplier = (MAX_RELIABILITY-MIN_RELIABILITY)/SLIDING_WINDOW_SIZE;
    		return result*moltiplier;
    	}

    	public void penalty(){
    		if(rewarding_penalizing_method == REWARDING_PENALIZING_METHOD_SUM_SUB)
    			reliability = reliability - RELIABILITY_PENALTY;
    		if(rewarding_penalizing_method == REWARDING_PENALIZING_METHOD_SUM_DIV)
    			reliability = (int) Math.floor((double)reliability/2);
    		update_tail_vector();
			sliding_window[0]=FAIL;
			is_been_a_very_good_provider = false;
    		if((rewarding_penalizing_method == REWARDING_PENALIZING_METHOD_SUM_SUB && reliability<MIN_RELIABILITY) ||
    		(rewarding_penalizing_method == REWARDING_PENALIZING_METHOD_SUM_DIV && reliability==MIN_RELIABILITY)){
    			reliability = MIN_RELIABILITY;
    			has_been_a_very_bad_provider = true;
    			is_been_a_very_bad_provider = true;
    			need_redemption=true;
    			System.out.println("reeeeeeeeedemption "+id_provider);
    		}
    	}
    	public void reward(){
    		reliability = reliability + RELIABILITY_REWARD;
    		update_tail_vector();
			sliding_window[0]=SUCCESS;
			is_been_a_very_bad_provider = false;
    		if(reliability>MAX_RELIABILITY){
    			reliability = MAX_RELIABILITY;
    			has_been_a_very_good_provider = true;
    			is_been_a_very_good_provider = true;
    		}
    	}
    	
    	public Provider(int id){
    		id_provider=id;
    		delta = random.randInt(LAMBDA_MIN,LAMBDA_MAX);
    		// ---------------------------------------------------------------------------------- INIT
    		new ProviderArrival(id).schedule(PROVIDER_START_TIME_OFFSET + (int)random.negexp((double) 1/delta));
    		
    		// ----------------------------------------------- SET # TASKS EXECUTING + MIN & MAX PRICE
    		number_of_task_executing=0;

    		double a,b;
    		for(int i=0;i<3;i++){
				a = random.uniform(PROVIDER_MIN_PRICE+get_min_offset_price(i), PROVIDER_MAX_PRICE-get_max_offset_price(i));
		    	b = random.uniform(PROVIDER_MIN_PRICE+get_min_offset_price(i), PROVIDER_MAX_PRICE-get_max_offset_price(i));
		    	if(a<b) { min_price[i]=a; max_price[i]=b; }
		    	else { min_price[i]=b; max_price[i]=a; }
    		}
    		// ----------------------------------------------- RELIABILITY
    		reliability = random.randInt(MIN_RELIABILITY, MAX_RELIABILITY);
    		for(int i=0; i<SLIDING_WINDOW_SIZE;i++){
    			sliding_window[i]=SUCCESS;
    		}	
	    }
    	
    	public void update_availability(){
	    	available_up_to= (int)time() + random.randInt(AVAILABLE_UP_TO_MIN, AVAILABLE_UP_TO_MAX);
    	}
    	
    	public int get_min(int a, int b){
    		if(a<b) return a;
    		else return b;
    	}
    	
    	public boolean is_respecting_max_n_tasks_for_provider(){
    		if(number_of_task_executing < MAX_N_TASK_FOR_PROVIDER)
    			return true;
    		else return false;
    	}
    	
    	public boolean is_available(){
    		if((int)time()<available_up_to)
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
    		if(!over){
	    		if ((int)time() <= nsteps) {
	    			
	        		provider_arrival_id=provider_arrival_id_counter;
	        		provider_arrival_id_counter++;
	    			tot_providers_notifications++;
	    			
	    			Provider prov = array_provider[id_provider];
	    			
	    			if(prov.is_available()) provider_availability_error++;
	    			
	    			prov.get_updated_sum_availability(); // uu
	    			
	    	    	prov.update_availability();
	    	    	
	    			prov.keep_updating=true;
	    			
	    			prov.time_availability_sent=(int)time();

	    			prov.into(in_matching_process_provider);
	    			try {
	    				matchProviderWithTask(prov);
	    			} catch (IOException e) {e.printStackTrace();}
	    			/*if(time_matching!=(int)time()){
	    				new MatchAtTimeT().schedule((int)time()+OFFSET_MATCHING);
	    				time_matching=(int)time();
	    			}*/
	    	    	
	    	    	//boolean go=true;
	    			//while(prov.is_available() && prov.is_respecting_max_n_tasks_for_provider()&& go){
						//try {
							//matchProviderWithTask(prov);
			    			//go=start_matched_TP (matchedTask, prov, PT);
	
						//} catch (IOException e) {e.printStackTrace();}
	
	    			//}//while
	    			
	    			
	    			//----------------------------------------------------- AUTOINIT
	    			new ProviderArrival(id_provider).schedule(prov.available_up_to + (int)random.negexp((double) 1/prov.delta));	// to change
			
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
    		if(!over){
    			new StopTaskExecution(theTask).schedule((int)time() + theTask.duration);
    		}
    	}
    }
    
    //----------------------------------------------------------------------------------------------------------------------
	class StopTaskExecution extends Event {
		private Task theTask;

		StopTaskExecution(Task t) {
			theTask = t;
		}
		
		public void actions() {
			if(!over){
				theTask.out();
				// retrieve the provider which completed the task
				Provider p = array_provider[theTask.int_list_providers_that_tried_task.get(0)];
							
				//there is a percentage of probability that the user will fail to complete the task
				if(probability_success(theTask)){ //true){ //random.draw((double)PERCENTAGE_TASK_FAILURE/100)){ // SUCCESS
					
					p.reward();
					tot_success_tasks++;
					double tt= (int)time() - theTask.entryTime;
		        	through_time += tt;
					theTask.out();
					theTask.state=COMPLETED;
					
					if(FIXED_EARNING_FOR_TASK) earning_McSense = earning_McSense + FIXED_EARNING_VALUE;
					else {
						earning_McSense = earning_McSense + (theTask.price_assigned*PERCENTAGE_EARNING)/100;
					}
					
					double tiq = tt -(theTask.duration*theTask.int_list_providers_that_tried_task.size());
					
					p.number_task_succeeded++;
					p.money_made += theTask.price_assigned;
					
					logger.info((int)time()+" "+
							FINISHED_TASK+" "+SUCCESS+" "
							+theTask.identifier+" "
							+theTask.type+" "
							+p.min_price[get_price_category(theTask.type)]
							+" "+tt+" "+tiq+" "+theTask.duration+" "
							+theTask.int_list_providers_that_tried_task.size()+" "
							+theTask.could_have_been_done_by);
					
					check_termination_condition();
						
	
				}
				
				else { // FAILURE
					tot_failed_tasks++;
					theTask.state=TO_RESCHEDULE;
					theTask.lock=false;
					theTask.provider_id_that_can_do_the_task.clear();		//
					theTask.provider_points_that_can_do_the_task.clear();	//
					// we immediately try to find another provider for the task that has just failed
					try {
						matchTaskWithProvider(theTask);
					} catch (IOException e) {e.printStackTrace();}
				}
				tot_scheduled_completed_in_good_or_bad_tasks++;
	
				if(!over){ // bug fixed
					// the provider frees one slot
					p.number_of_task_executing--;
					p.penalty();
					// we can see if it is wise to give a task to the provider who has just failed -> maybe it would be better
					// to examine the possibilities - if the task was manual we could try to give an automatic one..
					// it is possible that the provider that freed itself from the task is now capable of getting another task
					try {
						System.out.println("why stucks?!");
						matchProviderWithTask(p);
					} catch (IOException e) {e.printStackTrace();}
					/*if(p.is_available() && p.is_respecting_max_n_tasks_for_provider()){    	
		    	    	Task matchedTask;
						try {
							matchedTask = matchProviderWithTask(p);
			    			//start_matched_TP (matchedTask, p, PT);
						} catch (IOException e) {e.printStackTrace();}
					}*/
				}
			}

    	}
	}
	//----------------------------------------------------------------------------------------------------------------------
	public static void main(String args[]) throws IOException {
		new TaskSimulation23();
	}
	
	
	public void refresh_max_queue_value(){
        int qLength = tasklist.cardinal();
        if (max_tasklist_length < qLength)
      	  max_tasklist_length = qLength;
	}
	
	public void update_cupple_task_provider(Task taskToAdd, Provider providerToAdd){
		// task t already present
		boolean already_present = false;
		Task tAlreadyPresent = null;
		Task t = (Task) in_matching_process_tasks.first();
		System.out.println("1");
		while(t!=null){
			if (t==taskToAdd){
				already_present = true;
				tAlreadyPresent=t;
			}
		}
		System.out.println("2");
		if(!already_present){
			taskToAdd.provider_id_that_can_do_the_task.add(providerToAdd.id_provider);
			taskToAdd.into(in_matching_process_tasks);
		}
		else{
			if(!tAlreadyPresent.provider_id_that_can_do_the_task.contains(providerToAdd.id_provider))
				tAlreadyPresent.provider_id_that_can_do_the_task.add(providerToAdd.id_provider);
		}
	}
	
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	public Task matchProviderWithTask(Provider theProvider) throws IOException{
		if(time()>=(n_round)*enne_steps) load_mobility_traces();
		Task tt=null;
		Task ttt=null;
		Task matchedTask = (Task) tasklist.first();
		boolean found=false;
		
		int min_deadline= nsteps+1;
		int min_deadline_ld= nsteps+1;

		while(matchedTask!=null){
			
			if(		( theProvider.min_price[get_price_category(matchedTask.type)]
						<=matchedTask.budget ) 	&& 
					( !matchedTask.interested_in_location ||
							does_task_location_match_provider_location(matchedTask, theProvider)	) &&
					(!is_provider_in_list(theProvider,matchedTask.int_list_providers_that_tried_task)  ) &&
					((int)time()+matchedTask.duration <= 
						matchedTask.global_deadline_in_simulation)										&&
					((int)time()+matchedTask.duration <=  theProvider.available_up_to)
					&& (!matchedTask.lock)

																					){	
				found=true;
				
				// create function specific
				System.out.println("coupling");
				update_cupple_task_provider(matchedTask, theProvider);
				System.out.println("coupling done");

				
/*				if (policy_PT==FIRST) return matchedTask;
				if (policy_PT==LAST || (second_policy_PT==LAST && case_location_policy)) tt = matchedTask;
				if ((policy_PT==CLOSEST_DEADLINE || (second_policy_PT==LAST && case_location_policy)) 
						&& matchedTask.global_deadline_in_simulation<min_deadline) {
					min_deadline=matchedTask.global_deadline_in_simulation;
					tt = matchedTask;
				}
				if (policy_PT==FIRST_LOCATION_DEPENDENT && matchedTask.interested_in_location) return matchedTask;
				if (policy_PT==CLOSEST_DEADLINE_LOCATION_DEPENDENT && matchedTask.interested_in_location && 
						matchedTask.global_deadline_in_simulation<min_deadline_ld) {
					min_deadline_ld=matchedTask.global_deadline_in_simulation;
					ttt = matchedTask;
				}*/
			}

			matchedTask = (Task) matchedTask.suc();

		}//while
		System.out.println("nothing?!");
		if(found){ // && time_matching!=(int)time()) {
			//task_to_match.add(theTask.identifier);+			    theTask.into(in_matching_process);
			System.out.println("provider has found at least a task");
		    theProvider.into(in_matching_process_provider);
			if(time_matching!=(int)time()){
				new MatchAtTimeT().schedule((int)time()+OFFSET_MATCHING);
				time_matching=(int)time();

			}
		}
		return null;
			
			/*
			if(policy_PT==LAST || policy_PT==CLOSEST_DEADLINE) return tt;
			if(ttt==null) policy_PT=second_policy_PT;
			else return ttt;
			return tt;
		}
		else return null;*/
	}
	
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	public boolean matchTaskWithProvider(Task theTask) throws IOException{
		if(time()>=(n_round)*enne_steps) load_mobility_traces();
		Provider matchedProvider=null;
		int steps=0;
		boolean activate_check_in_case_of_failure=false;
		boolean found=false;
		for(int i=0;i<nusers;i++){
			
			if(	( array_provider[i].min_price[get_price_category(theTask.type)]
					<=theTask.budget ) 	&& 
				( array_provider[i].is_available())				&&
				( array_provider[i].is_respecting_max_n_tasks_for_provider())						&&

				(!is_provider_in_list(array_provider[i],theTask.int_list_providers_that_tried_task)  ) &&
				((int)time()+theTask.duration <= 
					theTask.global_deadline_in_simulation) 											&&
				((int)time()+theTask.duration <=  array_provider[i].available_up_to)
							
																					){
					steps++;
					if ( !theTask.interested_in_location ||
							does_task_location_match_provider_location(theTask, array_provider[i])		) {
						
					steps++;
					matchedProvider=array_provider[i];
					found=true;
					theTask.provider_id_that_can_do_the_task.add(matchedProvider.id_provider);
					theTask.provider_points_that_can_do_the_task.add(10);
					//matchedProvider.into(provider_subset);
					matchedProvider.get_updated_sum_availability();//update only of the possible candidates
					//break;	
				}
				if(steps==1){
					activate_check_in_case_of_failure=true;
				}
				
			}
		}
		if(!found && theTask.interested_in_location && activate_check_in_case_of_failure && TRACK_PROVIDERS  // buono
				&& theTask.state!=COMPLETED && theTask.state!=EXECUTING) 
			new CheckForProviderMovement(theTask).schedule((int)time()+1);
		
		//changed model we need to call another function*/
		theTask.could_have_been_done_by = theTask.provider_id_that_can_do_the_task.size();
				//provider_subset.cardinal();
		if(theTask.could_have_been_done_by>0)
			System.out.println("the task can be done by "+theTask.could_have_been_done_by);
		
		if(found){ // && time_matching!=(int)time()) {
			//task_to_match.add(theTask.identifier);+			    theTask.into(in_matching_process);
		    theTask.into(in_matching_process_tasks);
			if(time_matching!=(int)time()){
				new MatchAtTimeT().schedule((int)time()+OFFSET_MATCHING);
				time_matching=(int)time();

			}
		}
		else putTaskTinQueue(theTask);

		return found; //matchedProvider;
	}

	
	class MatchAtTimeT extends Event {
		
		public void actions() {
			Task t;
			
        	ArrayList<Integer> temp = new ArrayList<Integer>();
			printOnScreenPossibleMatchings();


			if(in_matching_process_tasks.cardinal()==0) {
				System.out.println("it shouldnt happen");
				stopSimulation();
			}
			else if(in_matching_process_tasks.cardinal()==1) {
				System.out.println("easy case");
				t = (Task) in_matching_process_tasks.first();
				start_matched_TP (t, array_provider[t.provider_id_that_can_do_the_task.get(0)], TP);
			}
			else{
				//stopSimulation();
				int moment;
				t = (Task) in_matching_process_tasks.first();
				while(t!=null){
					moment = t.provider_id_that_can_do_the_task.get(0);
					Task tt = (Task) t.suc(); // temporary structure to hold the pointer!
					if(!temp.contains(moment))
						System.out.println("("+t.identifier+","+t.provider_id_that_can_do_the_task.get(0)+")");
					start_matched_TP (t, array_provider[t.provider_id_that_can_do_the_task.get(0)], TP);
					t = tt;
					temp.add(moment);
				}
				temp.clear();
			}
			
			if(in_matching_process_tasks.cardinal()==0) System.out.println("all assigned");
			else {
				System.out.println("myfault "+in_matching_process_tasks.cardinal());
				stopSimulation();
				Task tt = (Task) in_matching_process_tasks.first();
				while(tt!=null){
					putTaskTinQueue(tt);
					tt = (Task) tt.suc();
				}
			}
		}
	
	}
	
	public void printOnScreenPossibleMatchings(){
		Task t = (Task) in_matching_process_tasks.first();
		while(t!=null){
			System.out.print("T["+t.identifier+"] P: ");
				for(int i=0;i<t.provider_id_that_can_do_the_task.size();i++){
					System.out.print(t.provider_id_that_can_do_the_task.get(i)+", ");
				}
				System.out.println();
			t = (Task) t.suc();
		}
	}

	public int getMatchingPoints(Task t, Provider p){
		if(t.global_deadline_in_simulation==(int)time()){
			//max
		}
		else if(t.global_deadline_in_simulation<=(int)time()+5*3600){
			//medium
		}
		return 0;
		//deadline-availability
		//budget-price
		//interested in location
		//duration
		//availability
		//fairness
	}
	

	
	public boolean does_task_location_match_provider_location(Task t, Provider p){
		Row r;
		r = mobtraces[p.id_provider][(int) time()-((n_round-1)*enne_steps)];
		if(		r.x >= t.x_map*UNIT && r.x <= (t.x_map+1)*UNIT 		&&
				r.y >= t.y_map*UNIT && r.y <= (t.y_map+1)*UNIT		 ) 
			return true;	
		return false;
	}
	
	public boolean is_provider_in_list(Provider p, List<Integer> l){
		return l.contains(p.id_provider);
	}
	
	// """""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
	public boolean start_matched_TP (Task t, Provider p, int direction){
		boolean result=false;
		
		if (p != null && t != null) { //if there is a provider we schedule the task execution
			matchings++;
			t.state = EXECUTING;
			result=true;
			t.lock=true;
			t.into(task_under_completion);
			p.number_of_task_executing++;
			p.number_task_received++;
			t.int_list_providers_that_tried_task.add(p.id_provider);
			t.price_assigned=p.min_price[get_price_category(t.type)]; // improvement
			p.ipotetical_money=p.ipotetical_money+t.price_assigned;

			Row r = mobtraces[p.id_provider][(int) time()-((n_round-1)*enne_steps)];

			if (direction==TP) 
				logger.info((int)time()+" "+MATCHING_CODE+" "+direction+
						" T "+t.identifier+" - P "+p.id_provider+" "
						+t.budget+" > "
						+p.min_price[get_price_category(t.type)]+" "+t.type+" "
						+t.interested_in_location+
						" ["+t.x_map+","+t.y_map+"] ["
						+r.x+","+r.y+"]");	
			else
				logger.info((int)time()+" "+MATCHING_CODE+" "+direction+
						" P "+p.id_provider+" - T "+t.identifier+" "
						+t.budget+" > "
						+p.min_price[get_price_category(t.type)]+" "+t.type+" "
						+t.interested_in_location+
						" ["+t.x_map+","+t.y_map+"] ["
						+r.x+","+r.y+"]");

			new StartTaskExecution(t).schedule((int)time()+1); //MNMNM
		}
		else {
			if(direction==TP) {

				putTaskTinQueue(t);
			}

		}
		return result;
	}
	
	public void putTaskTinQueue(Task t){

		    if(queuing_modality==FIFO) 							t.into(tasklist);
		    else if(queuing_modality==LIFO) 					t.precede(tasklist.first());
		    else if(queuing_modality==PRIORITY_DEADLINE) 		t.precede(get_task_with_worse_deadline(t));
		    
			refresh_max_queue_value();
			
			if(t.state==FAILED)
				t.out();
			//else t.state = QUEUED;
	}
	
	public Task get_task_with_worse_deadline(Task toInsert){
		Task scannedTask = (Task) tasklist.first();
		boolean found = false;
		while(scannedTask!=null){		
			if(	toInsert.global_deadline_in_simulation < scannedTask.global_deadline_in_simulation){	
				found=true;
				break;
			}
			if(!found) {
				scannedTask = (Task) scannedTask.suc();
			}
		}
		if(found) return scannedTask;
		else return null;
	}
	
	public double get_min_offset_budget(int type){
		if(type==ACC_AND_GPS || type == BLUETOOTH_AND_GPS || type == PHOTO)
			return 2;//CATEGORY_A;
		else if(type==GPS || type == ACC || type == BLUETOOTH || type == PHOTO)
			return 1;//CATEGORY_B;
		else return 0.5;//CATEGORY_C;
	}

	public double get_max_offset_budget(int type){
		if(type==ACC_AND_GPS || type == BLUETOOTH_AND_GPS || type == PHOTO)
			return 0;//CATEGORY_A;
		else if(type==GPS || type == ACC || type == BLUETOOTH || type == PHOTO)
			return 2;//CATEGORY_B;
		else return 3.5;//CATEGORY_C;
	}
	
	public double get_min_offset_price(int category){
		if(category==CATEGORY_A) return 1.75;//CATEGORY_A;
		else if(category==CATEGORY_B) return 0.75;//CATEGORY_B;
		else return 0.25;//CATEGORY_C;
	}
	
	public double get_max_offset_price(int category){
		if(category==CATEGORY_A) return 0.25;//CATEGORY_A;
		else if(category==CATEGORY_B) return 2.25;//CATEGORY_B;
		else return 3.75;//CATEGORY_C;
	}
	
	public int get_price_category(int type){
		if(type==ACC_AND_GPS || type == BLUETOOTH_AND_GPS || type == PHOTO)
			return CATEGORY_A;
		else if(type==GPS || type == ACC || type == BLUETOOTH || type == PHOTO)
			return CATEGORY_B;
		else return CATEGORY_C;
	}
	public boolean probability_success(Task t){

	    int base= PERCENTAGE_TASK_FAILURE;
	    int additional=0;
	    if(t.type== ACC_AND_GPS||t.type== BLUETOOTH_AND_GPS){
	    	additional = 20;
		}else if(t.type== GPS || t.type== BLUETOOTH){
			additional = 15;
		}
		else if(t.type== PHOTO){
			additional = 30;
		}
	    return random.draw((double)(base+additional)/100);
	}
	
	public void print_list_in_file(Head list, String filename) throws IOException{
		FileWriter fw = new FileWriter(filename);
		BufferedWriter bw = new BufferedWriter(fw);
		int length = list.cardinal();
		Task t = (Task) list.first();
		for(int i=0; i<length; i++){
			bw.write(t.identifier+"\n");
			t = (Task) t.suc();
		}
		bw.close();fw.close();
	}
	
	public void print_summary_for_script_analyzer() throws IOException{
		FileWriter fw = new FileWriter("summary.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(tot_success_tasks+" "+deadline_not_respected+" "+
				task_under_completion.cardinal()+" "+tasklist.cardinal()+
				"\n");
		bw.close();fw.close();
	}
	
	public void print_providers_info() throws IOException{
		//double check=0;
		FileWriter fw_nr = new FileWriter("provider/provider_number_tasks_received.txt");
		BufferedWriter bw_nr = new BufferedWriter(fw_nr);
		FileWriter fw_ns = new FileWriter("provider/provider_number_tasks_succeeded.txt");
		BufferedWriter bw_ns = new BufferedWriter(fw_ns);
		FileWriter fw_mm = new FileWriter("provider/provider_money_made.txt");
		BufferedWriter bw_mm = new BufferedWriter(fw_mm);
		FileWriter fw_im = new FileWriter("provider/provider_ipotetical_money.txt");
		BufferedWriter bw_im = new BufferedWriter(fw_im);
		FileWriter fw_mmonav = new FileWriter("provider/provider_money_made_on_availability.txt");
		BufferedWriter bw_mmonav = new BufferedWriter(fw_mmonav);
		FileWriter fw_taonav = new FileWriter("provider/tasks_assigned_on_availability.txt");
		BufferedWriter bw_taonav = new BufferedWriter(fw_taonav);
		FileWriter fw_av = new FileWriter("provider/provider_availability.txt");
		BufferedWriter bw_av = new BufferedWriter(fw_av);
		int div_zero_avoidance;
		for(int i=0;i<nusers;i++){
			div_zero_avoidance=0;
			bw_nr.write(array_provider[i].number_task_received+"\n");
			bw_ns.write(array_provider[i].number_task_succeeded+"\n");
			bw_mm.write(fmt.format(array_provider[i].money_made)+"\n"); //fmt.format(array_provider[i].money_made)+"\n");
			bw_im.write(fmt.format(array_provider[i].ipotetical_money)+"\n"); //fmt.format(array_provider[i].money_made)+"\n");
			bw_av.write(array_provider[i].time_has_been_available+"\n"); //fmt.format(array_provider[i].money_made)+"\n");
			//array_provider[i].get_updated_sum_availability();
			if(array_provider[i].time_has_been_available==0) div_zero_avoidance=1;
			bw_mmonav.write((int) (nsteps*array_provider[i].ipotetical_money/
					(array_provider[i].time_has_been_available+div_zero_avoidance))+"\n"); //fmt.format(array_provider[i].money_made)+"\n");	
			bw_taonav.write((int) (nsteps*array_provider[i].number_task_received/
					(array_provider[i].time_has_been_available+div_zero_avoidance))+"\n"); //fmt.format(array_provider[i].money_made)+"\n");	
			//check=check+array_provider[i].money_made;
		}
		bw_nr.close(); fw_nr.close();
		bw_ns.close(); fw_ns.close();
		bw_mm.close(); fw_mm.close();
		bw_im.close(); fw_im.close();
		bw_av.close(); fw_av.close();
		bw_mmonav.close(); fw_mmonav.close();
		bw_taonav.close(); fw_taonav.close();
	}

	
    void report() {
        System.out.println("No.of Providers = "							+ nusers);
        System.out.println("No.of Providers Notifications= "			+ tot_providers_notifications);
        System.out.println("No.of success tasks through the system = " 	+ tot_success_tasks);
        System.out.println("No.of tasks failed = " 						+ tot_failed_tasks);
        System.out.println("No.of tasks sheduled = " 					+ tot_scheduled_completed_in_good_or_bad_tasks);
        System.out.println("check: "									+(tot_success_tasks+tot_failed_tasks));
        System.out.println("McSense's earnings: "						+fmt.format(earning_McSense)+" $");

        System.out.println("Av.elapsed time = " + fmt.format(through_time/tot_success_tasks));
        System.out.println("Maximum queue length = " +  max_tasklist_length);
        
        System.out.println("No.of tasks arrivals = " 					+global_task_identifier_counter);
        System.out.println("Tasklist cardinality: "						+tasklist.cardinal());
        System.out.println("Tasklist under completion: "				+task_under_completion.cardinal());
        System.out.println("SUCCESSES: " 	+ tot_success_tasks);
        System.out.println("DEADLINE FAILURES: "+deadline_not_respected);
        System.out.println("MATCHINGS: "+matchings);
        System.out.println("provider availability error: "+provider_availability_error);



        System.out.println("\nExecution time: " +
            fmt.format((System.currentTimeMillis() - startTime)/1000.0) + " secs.\n");
    }
}