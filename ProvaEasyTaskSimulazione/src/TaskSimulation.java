import simulation.event.*;
import simset.*;
import random.*;

public class TaskSimulation extends Simulation {
    int noOfProviders;
    double simPeriod = 1000000;
    Head tasklist = new Head();
    Random random = new Random(5);
    int maxLength;
    int tot_success_tasks;
    int tot_scheduled_completed_tasks;
    int tot_failed_tasks;
    int tot_tasks_compleated_at_first_try;
    double throughTime;
    long startTime = System.currentTimeMillis();
    
    public TaskSimulation() {
        //-------------------------------- AUTOINIT
        new TaskArrival().schedule(0);
        new ProviderArrival().schedule(0);
        //--------------------------------
        runSimulation(simPeriod + 1000000);
        report();
    }
    
    void report() {
        System.out.println("No.of Providers = "							+ noOfProviders);
        System.out.println("No.of success tasks through the system = " 	+ tot_success_tasks);
        System.out.println("No.of tasks failed = " 						+ tot_failed_tasks);
        System.out.println("No.of tasks sheduled = " 					+ tot_scheduled_completed_tasks);
        System.out.println("check: "		+(tot_success_tasks+tot_failed_tasks));
        System.out.println("No.of tasks success sheduled 1st time: "	+tot_tasks_compleated_at_first_try);

        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(2);
        System.out.println("Av.elapsed time = " + fmt.format(throughTime/tot_success_tasks));
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
    	int numberOfScheduling;
    	
        double entryTime = time();
        public Task(){
        	numberOfScheduling=0;
        }
        public Task(int nos){
        	setNumberOfScheduling(nos);        
        }
        public int getDuration(){
        	// we should improve it making the value depend form what the client has specified!
        	return random.randInt(10, 20);
        }
        public int getNumberOfScheduling(){
        	return numberOfScheduling;
        }
        public void setNumberOfScheduling(int x){
        	numberOfScheduling=x;
        }
        public void increaseNumberOfScheduling(){
        	numberOfScheduling++;
        }
        public void setIdentifier(int x){
        	identifier=x;
        }
    }

    class TaskArrival extends Event {
    	Task theTask;
    	int number_of_try;
    	
    	public TaskArrival(){
    		theTask = new Task();
    	}
    	
    	public void actions() {
    		if (time() <= simPeriod) {
              //theTask = new Task();
              theTask.into(tasklist);
              int qLength = tasklist.cardinal();
              if (maxLength < qLength)
            	  maxLength = qLength;
  			  if (noOfProviders>0)
  				  new StartTaskExecution(theTask).schedule(time());
              new TaskArrival().schedule
              	(time() + random.negexp(1/11.0));
    		}
    	}
    }
    
    class ProviderArrival extends Event {
    	public void actions() {
    		if (time() <= simPeriod) {
    			noOfProviders++;
    			new ProviderArrival().schedule(time() + random.negexp(1/50.0));
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
		public boolean has_the_task_been_success_completed(){
			int temp = random.randInt(0,10);
			if (temp==0) return false;
			return true;
		}
		
		public void actions() {
			//vediamo se ho ultimato il task in caso contrario devo rischedularlo
			if(has_the_task_been_success_completed()){
				tot_success_tasks++;
	        	throughTime += time() - theTask.entryTime;
				noOfProviders++;
				if(theTask.getNumberOfScheduling()==0) tot_tasks_compleated_at_first_try++;
			}
			else {
				tot_failed_tasks++;
				theTask.increaseNumberOfScheduling();
				new StartTaskExecution(theTask).schedule(time());

			}
			//if (noOfProviders>0)
			//new StartTaskExecution(0,null).schedule(time());
			tot_scheduled_completed_tasks++;

    	}
	}
	
	public static void main(String args[]) {
		new TaskSimulation();
	}
}