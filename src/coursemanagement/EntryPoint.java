package coursemanagement;
import static spark.Spark.*;
import spark.*;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;


public class EntryPoint {
	
	 public static void main(String[] args) {
		 
		 stop();
		 port(9000);
		 get("/initialize",loadDB);
		 
		 
		 
		 
}
	 
	 
	 
	 public static Route loadDB = (Request req, Response resp)->{
		 
		 addStudents("/Users/rohitpitke/Desktop/SA/new test cases/test_case1/students.csv");
		 HashMap<String,String>model = new HashMap<>();
		 model.put("name", "test");
		return strictVelocityEngine().render(new ModelAndView(model,"helloworld.vm")); 
		 
	};
	 
	 
	 private static String print(){
		
		
		HashMap<String,String>model = new HashMap<>();
		model.put("name", "Rohit");
		return strictVelocityEngine().render(new ModelAndView(model,"helloworld.vm"));
		
		
	}
	

    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
    
    public static ArrayList<String[]> readFile(String file){
		ArrayList<String[]> texts = new ArrayList<String[]>();
		try{
			File newFile = new File(file);
			FileReader fReader = new FileReader(newFile);
			BufferedReader bReader = new BufferedReader(fReader);
			String line = bReader.readLine();
			while (line != null){
				texts.add(line.split(","));
				line = bReader.readLine();
			}
			bReader.close();
		}catch (IOException ex){
			ex.printStackTrace();
		}
		return texts;
	}
    
    public static void addStudents(String filename){
		ArrayList<String[]> student = readFile(filename);
		 DBConnection conn = new DBConnection();

		for(String[] studdata:student){
			//Student stud = new Student(Integer.parseInt(studdata[0]),studdata[1],studdata[2],studdata[3]);
			String query= "insert into student values(?,?,?,?)";
		      try {
				PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
				preparedStmt.setInt(1, Integer.parseInt(studdata[0]));
				preparedStmt.setString(2, studdata[1]);
				preparedStmt.setString(3, studdata[2]);
				preparedStmt.setString(4, studdata[3]);
			    preparedStmt.execute();

				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      


			
			
			
		
			}
		
	}
	
}
