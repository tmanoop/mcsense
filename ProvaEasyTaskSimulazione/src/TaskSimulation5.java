import simulation.event.*;
import simset.*;
import random.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TaskSimulation5 extends Simulation {
	
	// CONTROL PANNEL !!!
	private static final boolean 	FIXED_EARNING_FOR_TASK 				= true; // false;
	private static final double 	FIXED_EARNING_VALUE 				= 0.10; // 10 cent
	private static final int 		PERCENTAGE_EARNING 					= 10;
	private static final int 		PERCENTAGE_TASK_FAILURE 			= 0; //11;
    private static final double 	SIM_PERIOD 							= 100000;
    private static final int		NUMBER_OF_REGISTRED_PROVIDERS		= 100;
    private static final int		T1									= 100;
    private static final int		T2									= 200;
    private static final int		INFO								= 0;
    private static final int		WARNING								= 1;
    private static final int		SEVERE								= 2;
    private static final int		LEVEL								= INFO;

	
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

    
    public TaskSimulation5() throws IOException {

    	//-------------------------------------------------- LOGGER
    	logger = Logger.getLogger(TaskSimulation5.class.getName());
		FileHandler fileHandler = new FileHandler("app.log", false);
		logger.addHandler(fileHandler);
		if(LEVEL== INFO) logger.setLevel(Level.INFO);
		else if(LEVEL== WARNING)logger.setLevel(Level.WARNING);
		else logger.setLevel(Level.SEVERE);
		
        //-------------------------------- AUTOINIT
        new TaskArrival().schedule(100);
        new ProviderArrival().schedule(0);
        logger.info("autoinit completed");
        //--------------------------------
        runSimulation(SIM_PERIOD +1000000);
        report();
    }
    
    void report() {
        System.out.println("No.of Providers = "							+ providerlist.cardinal());//noOfProviders);
        System.out.println("No.of Providers Notifications= "			+ tot_providers_notifications);
        System.out.println("No.of success tasks through the system = " 	+ tot_success_tasks);
        System.out.println("No.of tasks arrivals = " 					+ global_task_identifier_counter);
        System.out.println("No.of tasks failed = " 						+ tot_failed_tasks);
        System.out.println("No.of tasks sheduled = " 					+ tot_scheduled_completed_in_good_or_bad_tasks);
        System.out.println("check: "		+(tot_success_tasks+tot_failed_tasks));
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
        logger.info("END");
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
    	
        double entryTime = time();
        public Task(){
        	//number_of_scheduling=0;
        	budget=random.randInt(1, 7);
        	providers_that_tried_task = new Head();
        	identifier=global_task_identifier_counter;
        	global_task_identifier_counter++;
    		logger.info("task: "+identifier);
        }
        public Task(int nos){
        	setNumberOfScheduling(nos);        
        }
        public int getDuration(){
        	// we should improve it making the value depend form what the client has specified!
        	return random.randInt(T1, T2);
        }
        public int getNumberOfScheduling(){ return number_of_scheduling; }
        public void setNumberOfScheduling(int x){ number_of_scheduling=x; }
        public void increaseNumberOfScheduling(){ number_of_scheduling++; }
        public void setIdentifier(int x){ identifier=x; }
        public double getBudget(){ return budget; }
    }
    //----------------------------------------------------------------------------------------------------------------------
    class TaskArrival extends Event {
    	Task theTask;
    	int task_arrival_id;
    	
    	public TaskArrival(){
    		//System.out.println("task arrival");
    		//logger.info("task_arrival");
    		theTask = new Task();
    	}
    	
    	public Provider function_get_first_in_queue_no_matter_what(){
			Provider matchedProvider = (Provider) providerlist.first();
    		return matchedProvider;
    	}
    	
    	public Provider function_get_first_in_queue_if_available(){
			Provider matchedProvider = (Provider) providerlist.first();
			boolean matching = false;
			while(matchedProvider!=null){
				if(matchedProvider.is_available()){
					matching=true;
					break;
				}
				if(!matching) {
					matchedProvider = (Provider) matchedProvider.suc();
				}
			}
			if(matching) return matchedProvider;
			else return null;
    	}
    	public Provider function_get_first_in_queue_available_and_price(){
			Provider matchedProvider = (Provider) providerlist.first();
			boolean matching = false;
			while(matchedProvider!=null){
				if(matchedProvider.min_price<=theTask.getBudget() && matchedProvider.is_available()){
					matching=true;
					break;
				}
				if(!matching) {
					matchedProvider = (Provider) matchedProvider.suc();
				}
			}
			if(matching) return matchedProvider;
			else return null;
    	}
    	
    	public void actions() {
    		if (time() <= SIM_PERIOD) {
				  //-------------------------------------------------AUTOINIT
    			new TaskArrival().schedule(time() + random.negexp(1/11.0));
    
    			//we call the desired function which gives a provider if there is any provider			
    			//Provider matchedProvider = function_get_first_in_queue_no_matter_what();
    			Provider matchedProvider = function_get_first_in_queue_if_available();
    			//Provider matchedProvider = function_get_first_in_queue_available_and_price();
    			
    			//if there is a provider we schedule the task execution
    			if (matchedProvider != null) {
    				theTask.out();
    				theTask.into(task_under_completition);
    				matchedProvider.set_available(false);
    				//solved the problem of duplication!
    				//System.out.println("insert provider info into task (if)");

    				Provider temp = new Provider(matchedProvider.id_provider);		
    				temp.into(theTask.providers_that_tried_task);
    				new StartTaskExecution(theTask).schedule(time());
    				logger.info("T ("+theTask.identifier+") - P ("+temp.id_provider+")");
    				//System.out.println("found t-p");
    			}
    			else {

    				theTask.into(taskrescheduled);
    				//System.out.println("taskreschedrule # "+taskrescheduled.cardinal());
    			}
    	
    		}
    	}
    }

    
    //----------------------------------------------------------------------------------------------------------------------
    class Provider extends Link {
    	int id_provider;
    	int min_price;
    	int max_price;
    	boolean available;
    	
    	public Provider(int id){
    		id_provider=id;
    		logger.info("provider_arrival "+id_provider);
    	}
    	public Provider(int id, int a, int b){
    		id_provider=id;
    		min_price=a;
    		max_price=b;
    		//set_available(true);
    	}
    	public boolean is_available(){
    		return available;
    	}
    	public void set_available(boolean x){
    		available=x;
    	}
    	public int get_min_price() { return min_price;}
    	
    }
  //----------------------------------------------------------------------------------------------------------------------
    class ProviderArrival extends Event {
    	int provider_arrival_id;
    	int min_price;
    	int max_price;
    	
    	public Provider getProviderFromID(int id){
    		Provider temp = (Provider) providerlist.first();
    		while(temp != null){
    			if(temp.id_provider==id) return temp;
    			temp = (Provider) temp.suc();
    		}
    		return null;
    	}
    	
    	public void actions() {
    		if (time() <= SIM_PERIOD) {
        		//System.out.println("provider arrival");
        		provider_arrival_id=provider_arrival_id_counter;
        		provider_arrival_id_counter++;
    			tot_providers_notifications++;
    			//-----------------------------------------------------AUTOINIT
    			new ProviderArrival().schedule(time() + random.negexp(1/50.0));
    			
    	    	int a = random.randInt(1, 5);
    	    	int b = random.randInt(1, 5);
    	    	if(a<b) { min_price=a; max_price=b; }
    	    	else { min_price=b; max_price=a; }
    	    	//available=true;
    	    	int id_provider_who_sent_notification = random.randInt(0, NUMBER_OF_REGISTRED_PROVIDERS);
    	    	Provider prov = getProviderFromID(id_provider_who_sent_notification);
    	    	if (prov == null) {
    	    		//System.out.println("null");
    	    		prov= new Provider(id_provider_who_sent_notification,a,b);
    	    		prov.into(providerlist);
    	    	}
    	    	//we set the provider available since he submitted the request
    	    	prov.set_available(true);
    	    	
    	    	//codice in cui si guarda nei task rescheduled
    	    	Task matchedTask = (Task) taskrescheduled.first();
    	    	//function_get_first_in_queue_no_matter_what();
    	    	//we remove anyway the task from the queue
    	    	//if there is a provider we schedule the task execution
    	    	if (matchedTask != null) {
    	    		//solved the problem of duplication!
    				Provider temp = new Provider(prov.id_provider);		
    	    		temp.into(matchedTask.providers_that_tried_task);
    	    		matchedTask.out();
        	    	prov.set_available(false);
    				logger.info("P ("+temp.id_provider+") - T ("+matchedTask.identifier+")");
    	    		new StartTaskExecution(matchedTask).schedule(time());
    	    		//System.out.println("found p-t");
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
    		new StopTaskExecution(theTask).schedule(time() + theTask.getDuration());
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
			if(random.draw((double)PERCENTAGE_TASK_FAILURE/100)){ 

				tot_success_tasks++;
	        	through_time += time() - theTask.entryTime;
				theTask.out();
				if(theTask.getNumberOfScheduling()==0) tot_tasks_compleated_at_first_try++;
				
				if(FIXED_EARNING_FOR_TASK) earning_McSense = earning_McSense + FIXED_EARNING_VALUE;
				else {
					Provider p = (Provider)theTask.providers_that_tried_task.first();
					earning_McSense = earning_McSense + (p.min_price*PERCENTAGE_EARNING)/100;
				}

			}
			
			else {
				//we should check again and if the check fails insert the task in rescheduled_waiting
				theTask.into(taskrescheduled);
				tot_failed_tasks++;
				theTask.increaseNumberOfScheduling();
				//new StartTaskExecution(theTask).schedule(time());

			}
			
			//if (noOfProviders>0)
			tot_scheduled_completed_in_good_or_bad_tasks++;
			//System.out.println("t"+tot_scheduled_completed_tasks);

    	}
	}
	//----------------------------------------------------------------------------------------------------------------------
	public static void main(String args[]) throws IOException {
		new TaskSimulation5();
	}
}