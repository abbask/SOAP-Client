/**
 *This is WeatherClient Application for Project Number 1
 *@author Abbas Keshavarzi
 *The whole program will ask user whether they are intreseted in checking the
 * weather or see forcast for available several days. and it needs user to 
 * provide the location zipcode in order to get some information about
 * weather in that area.
 */


import com.cdyne.ws.weatherws.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

    
public class WeatherClient{

    /*Main method for infinit loop
     * and asking users wether they 
     * are intereseted in weather or
     * forecast
     * including welcome and goodbye messsage
     * and has several calls to other methods 
     */   

    public static void main (String[] args){
	
	welcomeMessage(); // calling welcome method
	int val = 0;
	while (val != 3){ // check if user wants to exit or not
	    try{
		System.out.println();
		System.out.print("Please choose one of this options:");
		System.out.println(" (1-City Forcast 2-City Weather 3-exit)");
	
		Scanner keyboard = new Scanner(System.in);
		val = keyboard.nextInt(); // getting the user selection
	    }
	    
	    catch(InputMismatchException e){
		val = 0; // if there was a mismatch type error we just set val to 0 and we will handle it in switch case
		
	    }
	    
	 
	    switch (val){
	    case 0:
		errorMessage("Invalid input. Please try again!"); // either user type 0 or there was mismatch error
		break;
	    case 1:
		getCityForecast(); // calling method for forecast
		break;
	    case 2:
		getCityWeather(); // calling method for checking weather
		break;
	    default:
		errorMessage("Invalid input. Please try again!"); // if user enters some other numbers by mistake
	    }
	}
	
	goodbyeMessage(); // calling to method for goodbye message
	
    }
    /**
     *Simply print some text to output stream 
     * 
     */
    public static void goodbyeMessage(){
	System.out.println();
        System.out.println("************************************************");
        System.out.println("*Thank you for using our application. Good bye!*");
	System.out.println("************************************************");
	System.out.println();
    }
    
    /**
     *Simply print some test to output as welcome message
     */
    public static void welcomeMessage(){
	System.out.println();
	System.out.println("*********************************");
	System.out.println("*Welcome to Weather application.*");
	System.out.println("*********************************");
	System.out.println();
    }

    /**
     *method recieved a string andf print it to output
     */
    public static void errorMessage(String msg){
	System.out.println();
	System.out.println(msg);
    }

    /**
     *The method used whenever we need to get zipcode from the user
     */
    public static String getZipcode(){
	System.out.print("Please Enter 5-digit zipcode: ");
        Scanner keyboard = new Scanner(System.in);
        String zip = keyboard.nextLine();
	return zip;
    }

    /**
     *call to getZipcode method to get zipcode and then 
     * instatiat service oject and calling the service and then 
     * printing the results with appropriate style
     */
    public static void getCityWeather(){
	try{
	    String zip = getZipcode();
	    Weather service = new Weather(); // Weather is the main class that we should instantiate a object form it
	    WeatherSoap serviceSoap = service.getWeatherSoap(); // open the port
	    WeatherReturn weatherReturn = serviceSoap.getCityWeatherByZIP(zip); // calling the method and receiving the data from webservice
	    if (weatherReturn.isSuccess()){ // check weather the call was successful or not
		System.out.println();
		System.out.println ("The City is: " + weatherReturn.getCity() + ", " + weatherReturn.getState());
		System.out.println("The current Temprature is: " + weatherReturn.getTemperature());
		if (!weatherReturn.getDescription().isEmpty()) System.out.println("Weather Description: " + weatherReturn.getDescription());
		if (!weatherReturn.getRelativeHumidity().isEmpty()) System.out.println("Relative Humidity: " + weatherReturn.getRelativeHumidity());
		if (!weatherReturn.getWind().isEmpty()) System.out.println("The wind Speed is: " + weatherReturn.getWind());
		if (!weatherReturn.getWindChill().isEmpty()) System.out.println("The wind chill is: " + weatherReturn.getWindChill());
		if (!weatherReturn.getPressure().isEmpty()) System.out.println("The Pressure is: " + weatherReturn.getPressure());
		if (!weatherReturn.getVisibility().isEmpty()) System.out.println("The Visibility is: " + weatherReturn.getVisibility());
		if (!weatherReturn.getRemarks().isEmpty()) System.out.println("Any Remarks: " + weatherReturn.getRemarks());
		System.out.println();
	    }
	    else{
		System.out.println();
		System.out.println("Could not recognize the zipcode!");
	    }
	}
	catch (Exception e){ 
	    System.out.println();
	    System.out.println("Network problem. Please try again!");

	}
	
    }

    /**
     *call to getZipcode method to get zipcode and then
     * instatiat service object  and calling the forecast in service and then
     * printing the results with appropriate style
     */

    public static void getCityForecast(){
	try{
            String zip = getZipcode(); // get the zipcode from user
            Weather service = new Weather(); //initial the weather service from stubs
            WeatherSoap serviceSoap = service.getWeatherSoap(); //opening the port
            ForecastReturn forecastReturn = serviceSoap.getCityForecastByZIP(zip); // calling the method to get forecast by zipcode
            if (forecastReturn.isSuccess()){ //check if it was successful or not
                System.out.println();
                System.out.println ("The City is: " + forecastReturn.getCity() + ", " + forecastReturn.getState());
		List<Forecast> F = forecastReturn.getForecastResult().getForecast() ; //get list of forecast for (several days)
		System.out.println("Date\t\tTemprature\tprecipitation\t"); //Print the header
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); // simpledate for formatting the date
		for (Forecast f : F){ // for each loop through the list of forecast
		    if (!f.getTemperatures().getMorningLow().isEmpty()){  // check if the result actually have data in them ( not Empty)
			Date date = f.getDate().toGregorianCalendar().getTime(); //cast date format recieved to Date format
			System.out.println( format.format(date)  + "\t" + f.getTemperatures().getMorningLow() + "F\t\t" + f.getProbabilityOfPrecipiation().getNighttime() + "%\t" );
		    }
		}
		
                System.out.println();
            }
            else{
                System.out.println();
                System.out.println("Could not recognize the zipcode!");
            }
        }
        catch (Exception e){
            System.out.println();
            System.out.println("Network problem. Please try again!");

        }

    }

}
