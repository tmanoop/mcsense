import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import random.*;

public class RWPCreator {
	
	private static final int MIN_VELOCITY = 1;
	private static final int MAX_VELOCITY = 5;
	private static final int MIN_PAUSE = 300; // 5 min
	private static final int MAX_PAUSE = 3600; // 1h
	private static final double PROBABILITY_OF_PAUSE = 0.40;
	


	
	public static void main(String[] args) throws IOException {
		
		System.out.println("START");
		//System.out.println(java.lang.Runtime.getRuntime().maxMemory()/1024/1024); 
		//genFileConstantVelocity("rwm.txt",0,400,0,400,1,600,1000);
		genFilesUltimate(0,400,0,400,600000,50,true, false, 20 );
		//genFilesUltimate(0,400,0,400,600000,50,true, false, 20 );

		
		System.out.println("END");

	}
	
	
	public static void genFilesUltimate(int min_x,int max_x,
			int min_y,int max_y,
			int simulation_steps, int number_of_nodes,
			boolean precise,
			boolean fixed_velocity, int vel) throws IOException{
		

		Random random = new Random(5);
		double duration;
		double x,y,nextx,nexty,incrementox,incrementoy;
		String initial_file_name= "data/rwm";
		String termination_file_name =".txt";
		
		nextx= random.uniform(min_x, max_x);
		nexty= random.uniform(min_y, max_y);

		double tempx;
		double tempy;
		int node_id=0;
		double velocity = random.uniform(MIN_VELOCITY, MAX_VELOCITY); //1;
		if(fixed_velocity) velocity=vel;

		FileWriter fw = new FileWriter(initial_file_name+"values"+termination_file_name);
		BufferedWriter bw= new BufferedWriter(fw);
		bw.write((max_x-min_x)+" "+(max_y-min_y)+" "+number_of_nodes+" "+simulation_steps+"\n");
		bw.close();
		
		int approximate;
		double leftover=0;
		double length;
		int isteps=0;
		int pause=0;
		
        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(3);
       // System.out.println("Av.elapsed time = " + fmt.format(through_time/tot_success_tasks));
		
		for(;node_id<number_of_nodes; node_id++) {
			
			System.out.println("loading user "+node_id);
			fw = new FileWriter(initial_file_name+node_id+termination_file_name);
			bw = new BufferedWriter(fw);
			
			while (isteps<simulation_steps){
				if(isteps==0){bw.write(node_id+" "+isteps+" "+nextx+" "+nexty+"\n");}

				x= nextx;
				y= nexty;
				nextx= random.uniform(min_x, max_x);
				nexty= random.uniform(min_y, max_y);
				
				length = (leftover*velocity) + Math.sqrt( Math.pow((nextx-x),2) + Math.pow((nexty-y),2) );
				
				duration = length/velocity;
				approximate = (int) Math.floor(duration);
				if(approximate==0) approximate=1;
				//leftover = duration - approximate;
				//System.out.println("lenght "+length+" duration: "+duration+" velocity: "+velocity+
				//		" approximate: "+approximate+" leftover: "+leftover);
				
				incrementox= (nextx-x)/duration;
				incrementoy= (nexty-y)/duration;
	
				//System.out.println(isteps+"---("+x+" "+y+")-("+nextx+" "+nexty+")---"+duration);
				tempx=x;
				tempy=y;
				for(int j=0;j<approximate;j++){
					if(isteps>=simulation_steps) break;
					if(j==0 && precise) {
						tempx = tempx+incrementox*(1-leftover);
						tempy = tempy+incrementoy*(1-leftover);
					}
					else {
						tempx = tempx+incrementox;
						tempy = tempy+incrementoy;
					}
					isteps++;
					//System.out.println(node_id+" "+isteps+" "+tempx+" "+tempy);
					bw.write(node_id+" "+isteps+" "+tempx+" "+tempy+"\n");
				}
				
				//boolean will_I_pause=false;
				if(random.draw(PROBABILITY_OF_PAUSE)){
					pause = random.randInt(MIN_PAUSE, MAX_PAUSE);
					//System.out.println("pause of "+pause+"seconds");

					for(int k=0; k<pause; k++){
						if(isteps>=simulation_steps) break;
						isteps++;
						//System.out.println(node_id+" "+isteps+" "+nextx+" "+nexty);
						bw.write(node_id+" "+isteps+" "+nextx+" "+nexty+"\n");
					}
					// if we stop we can approximate the stop so to put to zero the left over
					// since we are going to stop at the point we where supposed to!
					leftover=0;
				}
				else {
					leftover = duration - approximate;
					if(!precise) leftover=0;
				}
				


				
			}//while
			isteps=0;
			bw.close();
			fw.close();
		}//for
	}
	
	public static void genFilesConstantVelocity(double min_x,double max_x,
			double min_y,double max_y,
			int simulation_steps, int number_of_nodes
			) throws IOException{
		

		Random random = new Random();
		int i=0;
		double duration;
		double x,y,nextx,nexty,incrementox,incrementoy;
		String initial_file_name= "data/rwm";
		String termination_file_name =".txt";
		
		nextx= random.uniform(min_x, max_x);
		nexty= random.uniform(min_y, max_y);

		double tempx;
		double tempy;
		int node_id=0;
		double velocity = random.uniform(MIN_VELOCITY, MAX_VELOCITY); //1;

		FileWriter fw = new FileWriter(initial_file_name+"values"+termination_file_name);
		BufferedWriter bw= new BufferedWriter(fw);
		bw.write(number_of_nodes+" "+simulation_steps+"\n");
		bw.close();
		
		int approximate;
		double leftover=0;
		double length;
		
		for(;node_id<number_of_nodes; node_id++) {
			fw = new FileWriter(initial_file_name+node_id+termination_file_name);
			bw = new BufferedWriter(fw);
			while (i<simulation_steps){
				if(i==0){bw.write(node_id+" "+i+" "+nextx+" "+nexty+"\n");}

				x= nextx;
				y= nexty;
				nextx= random.uniform(min_x, max_x);
				nexty= random.uniform(min_y, max_y);
				
				length = (leftover*velocity) + Math.sqrt( Math.pow((nextx-x),2) + Math.pow((nexty-y),2) );
				System.out.println("lenght "+length);
				
				duration = length/velocity;
				approximate = (int) Math.floor(duration);
				leftover = duration - approximate;
				System.out.println(" duration: "+duration+" approximate: "+approximate+" leftover: "+leftover);
				
				incrementox= (nextx-x)/duration;
				incrementoy= (nexty-y)/duration;
	
				System.out.println("---"+x+" "+y+"-"+nextx+" "+nexty+"---"+duration);
				tempx=x;
				tempy=y;
				for(int j=0;j<approximate;j++){
					if(i==simulation_steps) break;
					if(j==0) {
						tempx = tempx+incrementox*(1-leftover);
						tempy = tempy+incrementoy*(1-leftover);
					}
					tempx = tempx+incrementox;
					tempy = tempy+incrementoy;
					i++;
					System.out.println(node_id+" "+i+" "+tempx+" "+tempy);
					bw.write(node_id+" "+i+" "+tempx+" "+tempy+"\n");
				}

			}
			i=0;
			bw.close();
			fw.close();

		}
	}
	
	// CONSTANT VELOCITY
	public static void genFileConstantVelocity(String file_name,double min_x,double max_x,
			double min_y,double max_y,
			int velocity,
			int simulation_steps, int number_of_nodes
			) throws IOException{
		

		Random random = new Random();
		int i=0;
		double duration;
		double x,y,nextx,nexty,incrementox,incrementoy;
		
		FileWriter fw = new FileWriter(file_name);
		BufferedWriter bw = new BufferedWriter(fw);
		
		nextx= random.uniform(min_x, max_x);
		nexty= random.uniform(min_y, max_y);

		double tempx;
		double tempy;
		
		int node_id=0;
		bw.write(number_of_nodes+" "+simulation_steps+"\n");
		//bw.write(0+" "+i+" "+nextx+" "+nexty+"\n");
		int approximate;
		double leftover=0;
		double length;
		
		for(;node_id<number_of_nodes; node_id++) {
			while (i<simulation_steps){
				if(i==0){bw.write(node_id+" "+i+" "+nextx+" "+nexty+"\n");}

				x= nextx;
				y= nexty;
				nextx= random.uniform(min_x, max_x);
				nexty= random.uniform(min_y, max_y);
				
				length = (leftover*velocity) + Math.sqrt( Math.pow((nextx-x),2) + Math.pow((nexty-y),2) );
				System.out.println("lenght "+length);
				
				duration = length/velocity;
				approximate = (int) Math.floor(duration);
				leftover = duration - approximate;
				System.out.println(" duration: "+duration+" approximate: "+approximate+" leftover: "+leftover);
				
				incrementox= (nextx-x)/duration;
				incrementoy= (nexty-y)/duration;
	
				System.out.println("---"+x+" "+y+"-"+nextx+" "+nexty+"---"+duration);
				tempx=x;
				tempy=y;
				for(int j=0;j<approximate;j++){
					if(i==simulation_steps) break;
					if(j==0) {
						tempx = tempx+incrementox*(1-leftover);
						tempy = tempy+incrementoy*(1-leftover);
					}
					tempx = tempx+incrementox;
					tempy = tempy+incrementoy;
					i++;
					System.out.println(node_id+" "+i+" "+tempx+" "+tempy);
					bw.write(node_id+" "+i+" "+tempx+" "+tempy+"\n");
				}

				
					
			}
			i=0;
		}
		bw.close();
	}
	
	// PSEUDO CONSTANT VELOCITY
	public static void genFilePseudoConstantVelocity(String file_name,double min_x,double max_x,
			double min_y,double max_y,
			int velocity,
			int simulation_steps, int number_of_nodes
			) throws IOException{
		
		Random random = new Random();
		int i=0;
		int duration;
		double x,y,nextx,nexty,incrementox,incrementoy;
		
		FileWriter fw = new FileWriter(file_name);
		BufferedWriter bw = new BufferedWriter(fw);
		
		nextx= random.uniform(min_x, max_x);
		nexty= random.uniform(min_y, max_y);

		double tempx;
		double tempy;
		
		int node_id=0;
		bw.write(0+" "+i+" "+nextx+" "+nexty+"\n");
		
		for(;node_id<number_of_nodes; node_id++) {
			while (i<simulation_steps){
				x= nextx;
				y= nexty;
				nextx= random.uniform(min_x, max_x);
				nexty= random.uniform(min_y, max_y);
				
				double length = Math.sqrt( Math.pow((nextx-x),2) + Math.pow((nexty-y),2) );
				System.out.println("lenght "+length);
				
				duration = (int) length/velocity;
				if (duration==0) duration=1;
				
				incrementox= (nextx-x)/duration;
				incrementoy= (nexty-y)/duration;
	
				System.out.println("---"+x+" "+y+"-"+nextx+" "+nexty+"---"+duration);
				tempx=x;
				tempy=y;
				for(int j=0;j<duration;j++){
					if(i==simulation_steps) break;
					tempx = tempx+incrementox;
					tempy = tempy+incrementoy;
					i++;
					System.out.println(node_id+" "+i+" "+tempx+" "+tempy);
					bw.write(node_id+" "+i+" "+tempx+" "+tempy+"\n");
				}
					
			}
			i=0;
		}
		bw.close();
	}
	
	// CONSTANT VELOCITY FOR EACH LEG
	public static void genFileConstantLegVelocity(String file_name,double min_x,double max_x,
			double min_y,double max_y,
			int min_dur, int max_dur, 
			int simulation_steps, int number_of_nodes
			) throws IOException{
		
		Random random = new Random();
		int i=0;
		int duration;
		double x,y,nextx,nexty,incrementox,incrementoy;
		
		FileWriter fw = new FileWriter(file_name);
		BufferedWriter bw = new BufferedWriter(fw);
		
		nextx= random.uniform(min_x, max_x);
		nexty= random.uniform(min_y, max_y);

		double tempx;
		double tempy;
		
		int node_id=0;
		bw.write(0+" "+i+" "+nextx+" "+nexty+"\n");
		
		for(;node_id<number_of_nodes; node_id++) {
			while (i<simulation_steps){
				x= nextx;
				y= nexty;
				nextx= random.uniform(min_x, max_x);
				nexty= random.uniform(min_y, max_y);
				
				duration = random.randInt(min_dur, max_dur);
				incrementox= (nextx-x)/duration;
				incrementoy= (nexty-y)/duration;
	
				System.out.println("---"+x+" "+y+"-"+nextx+" "+nexty+"---"+duration);
				tempx=x;
				tempy=y;
				for(int j=0;j<duration;j++){
					if(i==simulation_steps) break;
					tempx = tempx+incrementox;
					tempy = tempy+incrementoy;
					i++;
					System.out.println(node_id+" "+i+" "+tempx+" "+tempy);
					bw.write(node_id+" "+i+" "+tempx+" "+tempy+"\n");
				}	
			}
			i=0;
		}
		bw.close();
	}
}
