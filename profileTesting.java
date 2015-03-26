import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileInputTesting{
	
	//**********************************************************************************
	//this file serves to test inputs on profile creation. i.e name, age fields, etc.
	//will be done in terms of BVA and valid/invalid types
	//***********************************************************************************
	
	public static void main(String[] args){
		
		//creates testing file to display the test report data
		PrintWriter writer = new PrintWriter("Profile_Testing_Report.txt", "UTF-8");
		
		//does the work for getting the current time to stamp on the text file
		 
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	writer.println( sdf.format(cal.getTime()) );
    


		
		
		
		int failNum, passNum = 0;
		writer.println("*********************************");
		writer.println("Testing Suite Report: ");
		//run tests
		if(nameCheck1("A") == 0){
			failNum++;
			writer.println("nameCheck1 fails. Too few characters in name.");

		}
		else{
			passNum++;
		}
		
		if(nameCheck2("AAAAAAAAAAAAAAAA") == 0){
			writer.println("nameCheck2 fails. Too many characters in name.");
			failNum++;
		}
		else{
			passNum++;
		}
		
		if(ageCheck1("a") == 0){
			failNum++;
			writer.println("ageCheck 1 fails. Age must be an integer.");
		}
		else{
			passNum++;
		}
		
		if(ageCheck2(17) == 0){
			failNum++;
			writer.println("ageCheck 2 fails. Must be an adult to use this app.");

		}
		else{
			passNum++;
		}
		
		if(ageCheck3(101) == 0){
			failNum++;
			writer.println("ageCheck 3 fails. Please enter a valid age.");

		}
		else{
			passNum++;
		}
		
		writer.println("Number of fails: " + failNum);
		writer.println("Number of passes: " + passNum);
		writer.println("****************************************");
		writer.close();
	}
	
	
	/* 
	************************************************
		Functions return 0 if test fails
						 1 if test passes
	**************************************************
	*/
	
	
	/* 
		for this application, as defined in our requirements, a user's name can consist of 
		whatever characters he/she wishes, so long as it is more than 1 character and less than
		15 characters
	*/
	
	//checks for too few of characters
	public int nameCheck1(String name){
		if(name.length() < 2){
			return 0;
		}
		return 1;
	}
	
	//checks for too many characters
	public int nameCheck2(String name){
		if(name.length() > 15){
			return 0;
		}
		return 1;
	}
	
	
	/*  
		as defined by our requirements, a user's age must be an integer, 
		greater than 18 but less than 100
	*/
	
	public int ageCheck1(int age){
		if(age !instanceof int){
			return 0;

		}
		return 1;
		
	}
	
	public int ageCheck2(int age){
		if(age < 18){
			return 0;
		}
		return 1;
	}
	
	public int ageCheck3(int age){
		if(age > 100){
			return 0;
		}
		return 1;
	}
	
	
}
