package Project;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

/**
 * ObjectToJson - Conversion of Object to JSON string using Jackson API
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class ObjectToJson {
	
	Object obj;
	ObjectMapper mapper = new ObjectMapper();	
	
	public ObjectToJson(Object obj) {
		this.obj = obj;
	}	
	
	/**
	 *Convert object to JSON using Jackson API	 
	 *@return string representation of JSON result
	 */
	public String convertToJson() throws DLException {
		String jsonStr = "";
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			//mapper.setSerializationInclusion(Include.NON_NULL);			
			jsonStr = mapper.writeValueAsString(obj);							
		}
		catch(JsonProcessingException jsonEx) {
			System.out.println("message "+jsonEx.getMessage());
			throw new DLException(jsonEx, "Error in converting to JSON.");
		}
		catch(Exception ex) {
			throw new DLException(ex, "Couldn't complete operation");
		}
		return jsonStr;
	}		
		
}
