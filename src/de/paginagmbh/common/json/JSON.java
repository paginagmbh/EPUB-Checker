package de.paginagmbh.common.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;



/**
  * this is a collection of JSON functions
  */
public class JSON {
	
	
	/**
	  * reads a resource file and returns it as a string
	  * 
	  * idea: http://my.opera.com/Martinovic/blog/2010/08/24/java-read-an-internal-text-resource-into-string
	  * 
	  * @author		Tobias Fischer
	  * @copyright	pagina GmbH, Tübingen
	  * @version	1.0
	  * @date 		2012-02-07
	  * @lastEdit	Tobias Fischer
	  * 
	  */
    public static <T> String readResourceAsString(Class<T> clazz, String resourceName) throws IOException {
      	
    	StringBuffer sb = new StringBuffer(1024);
    	BufferedReader inStream = new BufferedReader(new InputStreamReader(clazz.getResourceAsStream(resourceName), "UTF-8"));
      	String readLine;
      	
      	while( (readLine  = inStream.readLine()) != null){
      		sb.append(readLine);
    	}
      	
      	inStream.close();
        
      	return sb.toString();
    }
	
    
	
	/**
	  * parses a string as a JSONObject
	  *	
	  * @author		Tobias Fischer
	  * @copyright	pagina GmbH, Tübingen
	  * @version	1.0
	  * @date 		2012-02-07
	  * @lastEdit	Tobias Fischer
	  * 
	  */
    public static JSONObject parseString(String content) {
		
    	// System.out.println(content);
		
		/*
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = buff.readLine()) != null;) {
		    builder.append(line).append("\n");
		}
		JSONTokener tokener = new JSONTokener(builder.toString());
		 */
		
		JSONTokener tokener = new JSONTokener(content);
		
		JSONObject object = null;
		
		try {
			object = new JSONObject(tokener);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object;
	
    }
}
