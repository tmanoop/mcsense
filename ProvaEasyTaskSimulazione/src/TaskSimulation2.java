import simulation.event.*;
import simset.*;
import random.*;



public class TaskSimulation2 extends Simulation {
	
	// CONTROL PANNEL !!!
	private static final double 	FIXED_EARNING_VALUE 	= 0.10; // 10 cent
	private static final boolean 	FIXED_EARNING_FOR_TASK 	= true; // false;
	private static final int 		PERCENTAGE_EARNING 		= 10;
	private static final int 		PERCENTAGE_TASK_FAILURE = 11;
    private static final double 	SIM_PERIOD 				= 1000000;
    private static final int		NUMBER_OF_REGISTRED_PROVIDERS		= 500;

	
    int noOfProviders;

    Head tasklist = new Head();
    Head availabilitylist = new Head();    	// to remove probabily
    Random random = new Random(); //5); 			// da rivedere
    int maxLength;
    int tot_success_tasks;
    int tot_scheduled_completed_tasks;
    int tot_failed_tasks;
    int tot_tasks_compleated_at_first_try;
    int tot_matched_at_task_arrival;
    double through_time;
    double earning_McSense;
    int provider_arrival_id_counter;
    int task_arrival_id_counter;

    long startTime = System.currentTimeMillis();
    
    public TaskSimulation2() {
        //-------------------------------- AUTOINIT
        new TaskArrival().schedule(0);
        new ProviderArrival().schedule(0);
        //--------------------------------
        runSimulation(SIM_PERIOD +1000000);
        report();
    }
    
    void report() {
        System.out.println("No.of Providers = "							+ noOfProviders);
        System.out.println("No.of success tasks through the system = " 	+ tot_success_tasks);
        System.out.println("No.of tasks failed = " 						+ tot_failed_tasks);
        System.out.println("No.of tasks sheduled = " 					+ tot_scheduled_completed_tasks);
        System.out.println("check: "		+(tot_success_tasks+tot_failed_tasks));
        System.out.println("No.of tasks success sheduled 1st time: "	+tot_tasks_compleated_at_first_try);
        System.out.println("McSense's earnings: "	+earning_McSense+" $");
        System.out.println("Tot matched at task arrivl: "	+tot_matched_at_task_arrival);


        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(2);
        System.out.println("Av.elapsed time = " + fmt.format(through_time/tot_success_tasks));
        System.out.println("Maximum queue length = " +  maxLength);
        System.out.println("\nExecution time: " +
            fmt.format((System.currentTimeMillis() - startTime)/1000.0) + " secs.\n");
    }
    
    
    class Task extends Link {
    	int identifier;
    	int type;
    	boolean interested_in_location;
    	int location_we_are_interested_in;
    	int duration;
    	int sensor_required;
    	double budget;
    	int number_of_scheduling;
    	int global_deadline;
    	int relative_deadline;
    	
        double entryTime = time();
        public Task(){
        	number_of_scheduling=0;
        	budget=random.randInt(1, 7);
        }
        public Task(int nos){
        	setNumberOfScheduling(nos);        
        }
        public int getDuration(){
        	// we should improve it making the value depend form what the client has specified!
        	return random.randInt(10, 20);
        }
        public int getNumberOfScheduling(){
        	return number_of_scheduling;
        }
        public void setNumberOfScheduling(int x){
        	number_of_scheduling=x;
        }
        public void increaseNumberOfScheduling(){
        	number_of_scheduling++;
        }
        public void setIdentifier(int x){
        	identifier=x;
        }
        public double getBudget(){
        	return budget;
        }
    }

    class TaskArrival extends Event {
    	Task theTask;
    	int number_of_try;
    	int task_arrival_id;
    	
    	public TaskArrival(){
    		theTask = new Task();
    		task_arrival_id=task_arrival_id_counter;
    		task_arrival_id_counter++;
    	}
    	
    	public void actions() {
    		if (time() <= SIM_PERIOD) {
              //theTask = new Task();
              theTask.into(tasklist);
              int qLength = tasklist.cardinal();
              if (maxLength < qLength)
            	  maxLength = qLength;
              
              
              //********************
  			  if (noOfProviders>0) {
  				  boolean matching = false;
  				  AvailabilityNotification matchedProvider = (AvailabilityNotification) availabilitylist.first();
  				  
  				  while(matchedProvider!=null){
  				  //or(int i=0; i<availabilitylist.cardinal();i++){
  					  if(matchedProvider.get_min_price()<=theTask.getBudget() && matchedProvider.is_available()){
  						  matching=true;
  						  break;
  					  }
  					  if(!matching) {
  						  matchedProvider = (AvailabilityNotification) matchedProvider.suc();
  						  //matchedProvider.
  						  //System.out.println("bbb " );
  					  }

  				  }
  				  if(matching){
  					  System.out.println(availabilitylist.cardinal()+" task matched: budget = "+theTask.getBudget()+" - min_price = "+
  							  matchedProvider.get_min_price()+ " idt: "+task_arrival_id+" idp: "
  							  +matchedProvider.id_provider_arrival);
  					  matchedProvider.set_available(false);
  					  tot_matched_at_task_arrival++;
  				  }
  			  
  				  new StartTaskExecution(theTask).schedule(time());
  			  }
              new TaskArrival().schedule
              	(time() + random.negexp(1/11.0));
    		}
    	}
    }
    

    
    class AvailabilityNotification extends Link{
    	private int min_price;
    	private int max_price;
    	//private int duration_availability; // oppure orario interruzione?
    	private int id_provider;
    	int id_provider_arrival;
    	boolean available;
    	
    	public AvailabilityNotification() {
	    	int a = random.randInt(1, 5);
	    	int b = random.randInt(1, 5);
	    	if(a<b) { min_price=a; max_price=b; }
	    	else { min_price=b; max_price=a; }
	    	id_provider_arrival=provider_arrival_id_counter;
	    	available=true;
    	}
    	
    	public int get_min_price(){
    		return min_price;
    	}
    	public int get_max_price(){
    		return max_price;
    	}
    	public int get_range_price(){
    		return (max_price - min_price);
    	}
    	public int get_id_provider(){
    		return id_provider;
    	}
    	public boolean is_available(){
    		return available;
    	}
    	public void set_available(boolean x){
    		available=x;
    	}

    }
    

    
    class ProviderArrival extends Event {
    	AvailabilityNotification an;
    	int provider_arrival_id;
    	
    	ProviderArrival(){
    		provider_arrival_id=provider_arrival_id_counter;
    		provider_arrival_id_counter++;
    	}
    	public void actions() {
    		if (time() <= SIM_PERIOD) {
    			noOfProviders++;
    			new ProviderArrival().schedule(time() + random.negexp(1/50.0));
    			
    			// codice richiesta
    			an = new AvailabilityNotification();
                an.into(availabilitylist);
    			
    			
    			
    		}
    	}
    }
    
    
    class StartTaskExecution extends Event {
    	
    	private Task theTask;
		public StartTaskExecution(Task t)  {
    		theTask=t;
    	}
    	public void actions() {
    		noOfProviders--;
    		if(theTask.getNumberOfScheduling()==0){
    			theTask = (Task) tasklist.first();
        		theTask.out();
    		}

    		new StopTaskExecution(theTask).schedule(time() + theTask.getDuration());
    	}
    }
    
	class StopTaskExecution extends Event {
		private Task theTask;

		StopTaskExecution(Task t) {
			theTask = t;
		}
		
		public void actions() {
			//vediamo se ho ultimato il task in caso contrario devo rischedularlo
			if(random.draw((double)PERCENTAGE_TASK_FAILURE/100)){ 
				tot_success_tasks++;
	        	through_time += time() - theTask.entryTime;
				noOfProviders++;
				if(theTask.getNumberOfScheduling()==0) tot_tasks_compleated_at_first_try++;
				if(FIXED_EARNING_FOR_TASK) earning_McSense = earning_McSense + FIXED_EARNING_VALUE;
				else earning_McSense = earning_McSense + (theTask.getBudget()*PERCENTAGE_EARNING)/100;

			}
			else {
				tot_failed_tasks++;
				theTask.increaseNumberOfScheduling();
				new StartTaskExecution(theTask).schedule(time());

			}
			
		
			
			//if (noOfProviders>0)
			//new StartTaskExecution(0,null).schedule(time());
			tot_scheduled_completed_tasks++;
			//System.out.println("t"+tot_scheduled_completed_tasks);

    	}
	}
	
	public static void main(String args[]) {
		new TaskSimulation2();
	}
}