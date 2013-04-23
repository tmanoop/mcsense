import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.StringTokenizer;


public class ScriptAnalyzer {
	
	private static final String 	LOG_FILE_NAME 				= "app.log";
    private static final int		TP									= 0;
    private static final int		PT									= 1;
    
    //private static final int		TASK_GENERATION						= 0;
    private static final int		MATCHING_CODE						= 1;
    //private static final int		PROVIDER_ARRIVAL					= 2;
    private static final int		FINISHED_TASK						= 3;
    
    private static final int		FAIL								= 0;
    private static final int		SUCCESS								= 1;
    
    
    static int[] array_success; 
    static int[] array_deadline_miss;
    static int[] array_completion;
    static int[] array_tasklist;
    
	static int nSUCC=0;
	static int success;
	static int total=0;
	static int nFAIL=0;
	static int deadlinemissed;
	
	static int in_completion;
	static int in_tasklist;

	public static void main(String[] args) throws IOException {

		System.out.println("START");
		
		analyze_type_tasks();
		fill_vector_from_file(1);
		fill_vector_from_file(2);
		
		print_success();
		
		System.out.println("TOT tested = "+(nSUCC+nFAIL+in_completion+in_tasklist));
		
		if(nSUCC == success) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" the # of successes matches");
		if(nFAIL == deadlinemissed) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" the # of deadline miss matches");

		if(no_same_id_in_this_vector(array_success)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" no same id in array_success");
		if(no_same_id_in_this_vector(array_deadline_miss)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" no same id in array_deadline_miss");
		if(no_same_id_in_this_vector(array_completion))System.out.print("Ã");
		else System.out.print(" "); 
		System.out.println(" no same id in array_completion");
		if(no_same_id_in_this_vector(array_tasklist)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" no same id in array_tasklist");
		
		if(no_same_id_in_these_vectors(array_success, array_deadline_miss)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" comparison success - fail");
		if(no_same_id_in_these_vectors(array_success, array_completion)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" comparison success - completion");
		if(no_same_id_in_these_vectors(array_success, array_tasklist)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" comparison success - tasklist");
		
		if(no_same_id_in_these_vectors(array_deadline_miss, array_completion)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" comparison fail - completion");
		if(no_same_id_in_these_vectors(array_deadline_miss, array_tasklist)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" comparison fail - tasklist");
		
		if(no_same_id_in_these_vectors(array_completion, array_tasklist)) System.out.print("Ã");
		else System.out.print(" ");
		System.out.println(" comparison completion - tasklist");
			


		System.out.println("END");
		
	}
	
	public static boolean no_same_id_in_these_vectors(int[] v1, int[] v2){
		boolean temp=true;
		for(int i=0; i<v1.length;i++){
			for(int j=0; j<v2.length;j++){
				if (v1[i]==v2[j]){
					System.out.println("i "+i+" j "+j+" value: "+v1[i]);
					temp=false;
				}
			}
		}	
		return temp;
	}
	
	public static boolean no_same_id_in_this_vector(int[] vector){
		boolean temp=true;
		for(int i=0; i<vector.length;i++){
			for(int j=0; j<vector.length;j++){
				if(i!=j){
					if (vector[i]==vector[j]){
						System.out.println("i "+i+" j "+j+" value: "+vector[i]);
						temp=false;
					}
				}
			}
		}	
		return temp;
	}
	
	public static void fill_vector_from_file(int sel) throws IOException{
		String filename;
		int length=in_completion;
		if(sel==1) filename= "completion.txt";
		else {
			filename = "queue.txt";
			length= in_tasklist;
		}
		FileInputStream fstream = new FileInputStream(filename);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		StringTokenizer st;
		for(int i=0;i<length;i++){
			strLine = br.readLine();
			st = new StringTokenizer(strLine);
			int value = Integer.parseInt(st.nextToken());
			if(sel==1) array_completion[i]=value;
			else array_tasklist[i]=value;
		}
	}
	
	public static void print_success() throws IOException{
		FileWriter fwSUCCESS = new FileWriter("SUCCESS.txt");
		BufferedWriter bwSUCCESS= new BufferedWriter(fwSUCCESS);
		
		for(int i=0;i<array_success.length;i++){
			bwSUCCESS.write(array_success[i]+"\n");
		}
		
		bwSUCCESS.close(); fwSUCCESS.close();
	}
	
	
	
	public static void analyze_type_tasks() throws IOException {
		
		FileInputStream ffstream = new FileInputStream("summary.txt");
		DataInputStream iin = new DataInputStream(ffstream);
		BufferedReader bbr = new BufferedReader(new InputStreamReader(iin));
		
		FileInputStream fstream = new FileInputStream(LOG_FILE_NAME);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		StringTokenizer st;
		
        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(2);
		
		FileWriter fwPT = new FileWriter("output/PT.txt");
		BufferedWriter bwPT= new BufferedWriter(fwPT);
		
		FileWriter fwTP = new FileWriter("output/TP.txt");
		BufferedWriter bwTP= new BufferedWriter(fwTP);
		
		FileWriter fwTotal = new FileWriter("output/Total.txt");
		BufferedWriter bwTotal= new BufferedWriter(fwTotal);
		
		FileWriter fwSUCC = new FileWriter("output/SUCC.txt");
		BufferedWriter bwSUCC= new BufferedWriter(fwSUCC);
		
		FileWriter fwFAIL = new FileWriter("output/FAIL.txt");
		BufferedWriter bwFAIL= new BufferedWriter(fwFAIL);
		
		FileWriter fwPRICE = new FileWriter("output/PRICE.txt");
		BufferedWriter bwPRICE= new BufferedWriter(fwPRICE);
		
		FileWriter fwTT = new FileWriter("output/TT.txt");
		BufferedWriter bwTT= new BufferedWriter(fwTT);
		
		FileWriter fwQT = new FileWriter("output/QT.txt");
		BufferedWriter bwQT= new BufferedWriter(fwQT);
		
		FileWriter fwDT = new FileWriter("output/DT.txt");
		BufferedWriter bwDT= new BufferedWriter(fwDT);
		
		FileWriter fwPI = new FileWriter("output/ProviderInvolved.txt");
		BufferedWriter bwPI= new BufferedWriter(fwPI);
		
		
		int nTP=0;
		int nPT=0;
		double price;
		
		int direction;
		int selecter;
		int outcome;
		int id;
		int type;
		int provider_involved;
		
		
		double through_time;
		double queing_time;
		int duration;
		
		double tot_queing_time=0;
		double average_queing_time=0;
		double tot_price=0;
		double average_price=0;


		
		int[] fail = new int[10];
		for(int i=0;i<fail.length;i++){
			fail[i]=0;
		}
		
		// I READ THE SUMMARY
		strLine = bbr.readLine();
		st = new StringTokenizer(strLine);
		
		success= Integer.parseInt(st.nextToken());
		deadlinemissed=Integer.parseInt(st.nextToken());
		in_completion=Integer.parseInt(st.nextToken());
		in_tasklist=Integer.parseInt(st.nextToken());

		array_success = new int[success];
		array_deadline_miss = new int[deadlinemissed];
		
		array_completion = new int[in_completion];
		array_tasklist = new int[in_tasklist];
		
		
		// REAL LOG
		while ((strLine = br.readLine()) != null)   {
			strLine = br.readLine();
			st = new StringTokenizer(strLine);
			st.nextToken(); // INFO
			st.nextToken(); // time()
			//System.out.println(level);
			selecter= Integer.parseInt(st.nextToken());
			total++;
			bwTotal.write(total+"\n");
			if(selecter==MATCHING_CODE){
				//System.out.println("hey");
				direction = Integer.parseInt(st.nextToken());
				if(direction==PT) {
					//System.out.println("PT");
					nPT++;
				}
				if(direction==TP) {
					//System.out.println("TP");
					nTP++;
				}
				//System.out.println("PT on total: "+nPT+"/"+total);
				//System.out.println(+nPT+"|"+nTP+"|"+total);
				bwPT.write(nPT+"\n");
				bwTP.write(nTP+"\n");
			}
			else if(selecter==FINISHED_TASK){
				outcome = Integer.parseInt(st.nextToken());
				if(outcome==SUCCESS) {
					//tot_succ++;
					nSUCC++;
					//-------------------------------------------------
					id= Integer.parseInt(st.nextToken());
					//System.out.println(id);
					array_success[nSUCC-1]=id;
					//-------------------------------------------------
					type= Integer.parseInt(st.nextToken());
					//-------------------------------------------------
					price = Double.parseDouble(st.nextToken());
					tot_price = tot_price + price;
					average_price = tot_price/nSUCC;
					//bwPRICE.write(fmt.format(price)+"\n");
					bwPRICE.write(fmt.format(average_price)+"\n");

					//-------------------------------------------------
					through_time= Double.parseDouble(st.nextToken());
					bwTT.write(fmt.format(through_time)+"\n");
					//-------------------------------------------------
					queing_time= Double.parseDouble(st.nextToken()); // queued time, just skip for now
					tot_queing_time = tot_queing_time + queing_time;
					average_queing_time = tot_queing_time/nSUCC;
					//bwQT.write(fmt.format(queing_time)+"\n");
					bwQT.write(fmt.format(average_queing_time)+"\n");
					
					duration=Integer.parseInt(st.nextToken());
					bwDT.write(duration+"\n");

					
					provider_involved= Integer.parseInt(st.nextToken());
					bwPI.write(provider_involved+"\n");

				}
				else if (outcome==FAIL) {
					nFAIL++;
					//-------------------------------------------------
					id= Integer.parseInt(st.nextToken());
					//System.out.println(id);
					array_deadline_miss[nFAIL-1]=id;
					type= Integer.parseInt(st.nextToken());
					//System.out.println("FAIL: "+nFAIL+" type: "+type);
					int x =  fail[type];
					fail[type] = x+1;
					//for(int i=0;i<fail.length;i++){
					//	System.out.print("("+i+":"+fail[i]+") ");
					//}
					//System.out.println();
				}
				
				bwSUCC.write(nSUCC+"\n");
				bwFAIL.write(nFAIL+"\n");
			}

		}
		bwTP.close();bwPT.close();bwTotal.close();
		bwSUCC.close();bwFAIL.close();
		bwPRICE.close();
		bwTT.close();
		bwQT.close();
		bwDT.close();
		bwPI.close();
		br.close();

		print_fail_for_tasks(fail);
		
	}
	
	public static void print_fail_for_tasks(int[] f){
		for(int i=0;i<f.length;i++){
			System.out.print("("+i+":"+f[i]+") ");
		}
		System.out.println();
	}
	
	
	
	
	
	

}
