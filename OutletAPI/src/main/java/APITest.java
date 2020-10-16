import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class APITest {

	
	private static final String apiKey = "2cb1015919c4e9203c15cd52627cdd6d7ca53e5500593b13e42638002384";
	
	public static void main(String[] args) {
		System.out.println("Welcome to Outlet, please use the following menu");
		
		while(true) {
			System.out.println("Enter 1 to find current rate");
			System.out.println("Enter 2 to buy");
			System.out.println("Enter 3 to see your earning");
			System.out.println("Enter 4 to exit");
			
			// read from command line: if 1, return rate. if 2, ask for amount. if 3, ask for startdate, interest, and balance
			Scanner scanner = new Scanner(System.in);
			
			// get their input as a String
		    int option = scanner.nextInt();
		    
		    try{
		    	if(option == 1)
		    		System.out.println("The rate is "+findRate()+"\n\n");
		    	else if (option == 2) {
		    		System.out.println("Enter amount");
		    		buyUSDC(scanner.nextDouble());
		    	}
		    	else if(option == 3) {
		    		System.out.println("Enter start date。 Format: 2020-09-20");
		    		String date = scanner.next();
		    		System.out.println("Enter interest. Format: enter 6 for 6%");
		    		double interest = scanner.nextDouble();
		    		System.out.println("Enter original balance");
		    		double balance = scanner.nextDouble();
		    		calcEarning(date, interest, balance);
		    	
		    		
		    	}
		    	else if(option == 4)
		    		System.exit(0);
		    	else {
		    	
		    		System.out.println("Invalid entry");
		    	}
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
		}

	}

	static String getJson()  {
		try {

	            URL url = new URL("https://data-api.defipulse.com/api/v1/defipulse/api/GetRates?token=USDC&amount=10000&api-key=2cb1015919c4e9203c15cd52627cdd6d7ca53e5500593b13e42638002384");//your url i.e fetch data from .
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("Accept", "application/json");
	            if (conn.getResponseCode() != 200) {
	                throw new RuntimeException("Failed : HTTP Error code : "
	                        + conn.getResponseCode());
	            }
	            
	            
	            //output to file
	/*            InputStream is = conn.getInputStream(); 
	    		
	            byte[] buf = new byte[1024];
	            int byteRead=0;
	            
	            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream("defiPulse.json"));
	    		while ((byteRead = is.read(buf)) != -1) {
	                outStream.write(buf, 0, byteRead);
	                
	            }
	    		
	    		is.close();
	    		outStream.close(); */
	            
	   
	            //output to stdout
	    		InputStreamReader in = new InputStreamReader(conn.getInputStream());
	            BufferedReader br = new BufferedReader(in);
	            StringBuffer sb = new StringBuffer();
	            String output;
	            while ((output = br.readLine()) != null) {
	                sb.append(output);
	            }
	            conn.disconnect();
	            return sb.toString();

	        } catch (Exception e) {
	            System.out.println("Exception in NetClientGet:- " + e);
	            e.printStackTrace();
	        }
			return null;
	}
	
	static double findRate() throws Exception {
		return findRateForUnitTest(getJson());
	}
	
	/**
	 * 1. call the defi pulse api
	 * 2. process the return
	 * @param token
	 * @return
	 */
	static double findRateForUnitTest(String json) throws Exception{
		
		double rate = 0;
		double rateCount = 0;
		 // parsing file "JSONExample.json" 
		
        Object obj = new JSONParser().parse(json); 
//	        JSONObject a = new JSONObject("saa");
        
          
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) obj; 
        
        
        
//        System.out.println("price is "+price);
        
        Map rates = (Map)jo.get("rates");
        
        JSONObject jo1 = (JSONObject)jo.get("rates");
        
        
        Iterator itr2 = rates.keySet().iterator();
       // Iterator<Map.Entry> itr1;  
        while (itr2.hasNext())  
        {
        	String exchange = (String)itr2.next();
             JSONObject jo2 = (JSONObject) jo1.get(exchange);
             Map lendRate = (Map)jo2.get("lend");    
             if(lendRate != null) {
            	 if(lendRate.get("rate") instanceof String)
            		 continue;
            	 rate += (Double)lendRate.get("rate");
            	 rateCount++;
             }
            
        } 
	
        
        double avgRate = (double)(rate / rateCount)/100;
        if (avgRate > .06)
        	return .06;
        else
        	return avgRate;
	        
	}
	
	
	static void buyUSDC(double amount) throws Exception {
		
        Object obj = new JSONParser().parse(getJson()); 
//	        JSONObject a = new JSONObject("saa");
        
          
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) obj; 
        
        
        
        Map token = (Map)jo.get("token");
        double price = (Double)token.get("price");//connect to api to find the rate
		
		//Test amount limit
		//print result
        if (amount < price) {
        	System.out.println("The amount you entered is too small, it needs to be at least "+price);
        	return;
        }
        
        if(amount > price * 1000) {
        	System.out.println("The amount you entered is too large, you can only buy 1000 USDC and its current rate is "+price);
        	return;
        }
    //    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        double amountBought = amount / price;
        System.out.println("bought "+amountBought+" USDC at rate "+price+" at timestamp "+ new Timestamp(System.currentTimeMillis())+"\n\n");
        
	}
	
	/**
	 * 
	 * @param startDate
	 * @param interest
	 * @param balance
	 */
	static void calcEarning(String startDate, double interest, double balance) {
		//formula to calculate weekly compound.
		
		//figure out how many days
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		String inputString1 = "08 10 2020";
//		String inputString2 = "09 10 2020";

		
		LocalDate date1 = LocalDate.parse(startDate, dtf);
	    LocalDate date2 = LocalDate.now();

	    long days = ChronoUnit.DAYS.between(date1, date2);
//	    System.out.println ("Days: " + days);
	    
	    //apply formula for rate calculation: a = p*(1+(r/n))^nt
	    interest = interest / 100;
	    
	    double intBase = (1+(interest/52));
	    double intPow = (double)52*days/365;
	   
	    double totalBalance = balance *Math.pow(intBase, intPow);
	    double earning = totalBalance - balance;
	   
	    System.out.println("original balance: "+balance+"\nnew balance: "+totalBalance+"\nyou earned: "+earning+"\ncompounded weekly for "+days+" days with interest "+interest+"\n\n");
		
	}
}

