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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class CMSFileReader {
	public String readFile(String fileName) {
		String out = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			String line = null;
			while ((line = reader.readLine()) != null) {
				out = out + line;
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}

		return out;
	}
}
class JsonUtil {
    public static Map<String, String> parse(String object) {
        return new Gson().fromJson(object, Map.class);
        
    }
}

public class EntryPoint {

	public static String[] semesters = { "Fall", "Winter", "Spring", "Summer" };
	public static Integer semIndex = 0;
	public static Integer semYear;
	public static boolean systemstarted = false;

	public static void main(String[] args) {

		stop();
		port(9000);
		setCurrentTermWithYear();
		get("/startsim/:year", startSim);
		get("/nextTerm", nextTerm);
		get("/initialize", loadDB);
		get("/getStudent/:id", getStudentById);
		get("/getRecord/:id", getStudentRecord);
		// post("/assignGrade", enterAcademicRecord);


		get("/hireInstructor/:id", (req, resp) -> {

			InstructorDAOimpl inst = new InstructorDAOimpl();
			inst.hireInstructor(Integer.parseInt(req.params(":id")));
			return "Instructor hired successfully";
		});
		get("/leaveInstructor/:id", (req, resp) -> {

			InstructorDAOimpl inst = new InstructorDAOimpl();
			inst.leaveInstructor(Integer.parseInt(req.params(":id")));
			return "Instructor is removed successfully";
		});

		get("/getInstructor/:id", (req, resp) -> {
			InstructorDAOimpl inst = new InstructorDAOimpl();
			return "Name:" + inst.returnInstructorInfo(Integer.parseInt(req.params(":id"))).name;

		});
		
		/*get("/teachCourse/:id/:cid",(req,resp)->{
			InstructorDAOimpl inst = new InstructorDAOimpl();
			int instid = Integer.parseInt(req.params(":id"));
			int courseid = Integer.parseInt(req.params(":cid"));
			int result = inst.teachCourse(instid, courseid);
			return "Result is:"+result;
			
			
		});*/
		
		
		get("/requestCourse/:id/:cid",(req,resp)->{
			
			StudentDAOimpl stud = new StudentDAOimpl();
			String returnVal = stud.requestCourse(Integer.parseInt(req.params(":id")), Integer.parseInt(req.params(":cid")), semesters[semIndex]);
			return "Return value is"+ returnVal;
			
		});
		
		 /* ===== Read index file ===== */
        coursemanagement.CMSFileReader cms = new coursemanagement.CMSFileReader();

        WekaOperator weka = new WekaOperator();
        get("/", (req, res) -> {
            return cms.readFile("index.html");
        });
        get("/index.html", (req, res) -> {
            return cms.readFile("index.html");
        });

	        /* ===== Course Handlers ===== */
        get("/getCourses/:id", (req, res) -> {
            // TODO: Get a list of courses in the following format
            String test_data = "[{\"id\":\"22\",\"desc\":\"Computer Programming\"}," +
                    "{\"id\":\"23\",\"desc\":\"Computer Networks\"}," +
                    "{\"id\":\"24\",\"desc\":\"Computer Architecture\"}]";
            
        	int studId = Integer.parseInt(req.params(":id"));
        	String query = "select uuid AS id, name AS desc1 from course where uuid IN (select courseuuid from academicRecord where studentuuid=?)";
    		DBConnection conn = new DBConnection();
    		ArrayList<tempClassHolder> templist = new ArrayList<>();
        	try {
    			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
    			preparedStmt.setInt(1, studId);
    			ResultSet rs = preparedStmt.executeQuery();
    			while(rs.next()){
    				tempClassHolder temp = new tempClassHolder(rs.getInt("id"),rs.getString("desc1"));
    				templist.add(temp);
    			}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            String json = new Gson().toJson(templist);
            return json;
      });

	        /* ===== Admin Handlers ===== */
        get("/loginAdmin/:id", (req, res) -> {
            // TODO: Call Login here
            return "Login Successful";
        });
        get("/loadData/:path", (req, res) -> {
            // TODO: Load data here
            System.out.println("Load Data");
            return "Data Import Successful";
        });
        post("/addCourse", (req, res) -> {
            // TODO: Add a new Course to the database
            // NOTE:  example: req.body() will return {"desc":"Computer Programming"}
            System.out.println(req.body());
            return "Add Course Successful";
        });
        get("/showAllCourses", (req, res) -> {
            // TODO: Show All Courses
            System.out.println("Show All Courses");
            String test_data = "[{\"id\":\"22\",\"desc\":\"Computer Programming\"}," +
                    "{\"id\":\"23\",\"desc\":\"Computer Networks\"}," +
                    "{\"id\":\"24\",\"desc\":\"Computer Architecture\"}]";

            return test_data;
        });
        get("/getCourseDetails/:cid", (req, res) -> {
            // TODO: Get Course Details for cid
            System.out.println("Get Course Details");
            String test_data = "{\"desc\":\"Computer Programming\"}";

            return test_data;
        });
        post("/editCourseDetails", (req, res) -> {
            // TODO: Get Course Details for cid
            System.out.println("editCourseDetails");
            System.out.println(req.body());

            return "Course Edited";
        });
        get("/showAllInstructors", (req, res) -> {
            // TODO: Show All Courses
            System.out.println("Show All Instructors");
            String test_data = "[" +
                    "{\"id\":\"22\",\"name\":\"Don Nelson\",\"addr\":\"22 Nerson Lane, San Jose CA 95122\",\"phone\":\"503-222-1234\"}," +
                    "{\"id\":\"23\",\"name\":\"Ron Howard\",\"addr\":\"12 To Lane, Reno NV 81122\",\"phone\":\"213-222-1234\"}," +
                    "{\"id\":\"24\",\"name\":\"Venky Ram\",\"addr\":\"1 Baba Ct, Tulsa OK 81022\",\"phone\":\"713-222-1234\"}]";

            return test_data;
        });
        get("/showAllStudents", (req, res) -> {
            // TODO: Show All Courses
            System.out.println("Show All Students");
            String test_data = "[" +
                    "{\"id\":\"22\",\"name\":\"Don Nelson\",\"addr\":\"22 Nerson Lane, San Jose CA 95122\",\"phone\":\"503-222-1234\"}," +
                    "{\"id\":\"23\",\"name\":\"Ron Howard\",\"addr\":\"12 To Lane, Reno NV 81122\",\"phone\":\"213-222-1234\"}," +
                    "{\"id\":\"24\",\"name\":\"Venky Ram\",\"addr\":\"1 Baba Ct, Tulsa OK 81022\",\"phone\":\"713-222-1234\"}]";

            return test_data;
        });
        get("/getPrerequisites/:cid", (req, res) -> {
            // TODO: Show All Courses
            System.out.println("Show All Pre-requisites");
            String test_data = "[" +
                    "{\"id\":\"22\",\"name\":\"Don Nelson\",\"addr\":\"22 Nerson Lane, San Jose CA 95122\",\"phone\":\"503-222-1234\"}," +
                    "{\"id\":\"23\",\"name\":\"Ron Howard\",\"addr\":\"12 To Lane, Reno NV 81122\",\"phone\":\"213-222-1234\"}," +
                    "{\"id\":\"24\",\"name\":\"Venky Ram\",\"addr\":\"1 Baba Ct, Tulsa OK 81022\",\"phone\":\"713-222-1234\"}]";

            return test_data;
        });
        post("/addInstructor", (req, res) -> {
        	 Map<String, String> map = new JsonUtil().parse(req.body());
        	 new InstructorDAOimpl().insertInstructorInfo(map.get("name"), map.get("address"), map.get("phoneNo"));
            return "Add Instructor Successful";
        });
        post("/addStudent", (req, res) -> {
            // TODO: Add a new Student to the database
            // NOTE:  example: req.body() will return {"name":"Rohit Pitke"}
          
        	 Map<String, String> map = new JsonUtil().parse(req.body());
        	 new StudentDAOimpl().insertStudentInfo(map.get("name"), map.get("address"), map.get("phoneNo"));
        	 return "Add Student Successful";
        });
        post("/hireInstructor", (req, res) -> {
            // TODO:
            // NOTE:  example: req.body() will return {"id":"22"}
        	   Map<String, String> map = new JsonUtil().parse(req.body());
               System.out.println(map.get("id"));
               
               InstructorDAOimpl inst = new InstructorDAOimpl();
   				boolean isSuccess = inst.hireInstructor(Integer.parseInt(map.get("id")));
   				return "Instructor hired successfully if ID is accurate";
   		});
        
        post("/leaveInstructor", (req, res) -> {
        	  Map<String, String> map = new JsonUtil().parse(req.body());
              System.out.println(map.get("id"));
              InstructorDAOimpl inst = new InstructorDAOimpl();
 			  inst.leaveInstructor(Integer.parseInt(map.get("id")));
 			  return "Instructor left successfully if ID is accurate";
           
        });
        post("/startTerm", (req, res) -> {
            // TODO:
            // NOTE:  example: req.body() will return {"term":"Fall-2017"}
            System.out.println(req.body());
            return "Term Started";
        });
        get("/advanceTerm", nextTerm);
        get("/coursesForPrereqs/:courseid", (req, res) -> {
	            /* TODO:  need a list of courses in the following format.
	                - The list should not contain courseid
	                - The list should not contain Courses that already have courseid as their pre-req
	             */
            System.out.println("Prereq called");
            /* Comment this after code is completed */
            String test_data = "[{\"id\":\"22\",\"desc\":\"Computer Programming\",\"prereq\":\"yes\"}," +
                    "{\"id\":\"23\",\"desc\":\"Computer Networks\",\"prereq\":\"no\"}," +
                    "{\"id\":\"24\",\"desc\":\"Computer Architecture\",\"prereq\":\"no\"}]";
            return test_data;
        });
        post("/setPrerequisites", (req, res) -> {
            // TODO:
            // NOTE:  example: req.body() will return {"cid":"11","prereqs":"22,23,24"}}
            System.out.println("Set Prerequisites called");
            System.out.println(req.body());
            return "Set Pre-requisites";
        });

        /* ===== Instructor Handlers ===== */
		get("/loginInstructor/:id", (req, res) -> {
			// TODO:
			return "Not implemented yet";
		});

        post("/teachCourse", (req, res) -> {
            InstructorDAOimpl inst = new InstructorDAOimpl();
            Map<String, String> map = new JsonUtil().parse(req.body());
			int instid = Integer.parseInt(map.get("iid"));
			int courseid = Integer.parseInt(map.get("cid"));
			String result = inst.teachCourse(instid, courseid);
			return result;
       });
        
		post("/assignGrade", enterAcademicRecord);

        /* ===== Student Handlers ===== */
		get("/loginStudent/:id", (req, res) -> {
			// TODO:
			return "Not implemented yet";
		});

		post("/registerCourse", (req, res) -> {
			
			Map<String, String> map = new JsonUtil().parse(req.body());
			StudentDAOimpl stud = new StudentDAOimpl();
			String returnVal = stud.requestCourse(Integer.parseInt(map.get("sid")),Integer.parseInt(map.get("cid")), semesters[semIndex]);
			return "Register course action result: "+ returnVal;
           
		});
        post("/viewGrades", (req, res) -> {
        	Map<String, String> map = new JsonUtil().parse(req.body());
        	int studId = Integer.parseInt(map.get("sid"));
        	int courseId = Integer.parseInt(map.get("cid"));
        	String query = "Select termyear AS term, instuuid AS instructor, grade, comment from academicRecord where studentuuid=? AND courseuuid=?";
    		DBConnection conn = new DBConnection();
    		ArrayList<tempClassHolder> templist = new ArrayList<>();
        	try {
    			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
    			preparedStmt.setInt(1, studId);
    			preparedStmt.setInt(2, courseId);
    			ResultSet rs = preparedStmt.executeQuery();
    			while(rs.next()){
    				String comment = rs.getString("comment");
    				comment = (comment == null) ?"No comment yet":comment;
					tempClassHolder temp = new tempClassHolder(rs.getString("term"),rs.getInt("instructor"),rs.getString("grade"),comment);
    				templist.add(temp);
    			}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            String json = new Gson().toJson(templist);
            return json;
      });
        get("/wekaReport/:id", (req, res) -> {

            String weka_data_test = "{\"Number of Students\":10, \"Course with the most students Registered\": \"CS 255\" }";
            weka.summarizeData(weka.queryWeka("select * from student"));

            String test_data = "{\"total_number_of_students\":\"55\",\"course_with_the_most_students\":\"Computer Programming\",\"Total_number_of_courses_offered\":\"150\"}";

            return test_data;
        });

        get("/wekaAnalysis/:id", (req, res) -> {
        	
            HashMap<Integer, ArrayList<Integer>> data = weka.runClassification(weka.queryWeka("select * from student"));
            System.out.println("Weka Analysis");
            // return new Gson().toJson(data);
            String test_data = "[[10,11,12],[13,21,23],[33,22,11],[55,23,12]]";

        	// weka.summarizeData(weka.queryWeka("select id, phoneno from student"));;
     
            return test_data;
        });

        StudentDAO studdao = new StudentDAOimpl();
	};

	public static Route startSim = (Request req, Response resp) -> {

		if (systemstarted)
			return "System has been started already. Use next term";

		int semyear = Integer.parseInt(req.params(":year"));
		DBConnection conn = new DBConnection();
		String query = "Insert into semTable values(?,?)";
		try {
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
			preparedStmt.setInt(1, semyear);
			preparedStmt.setInt(2, 0);
			preparedStmt.execute();
			semYear = semyear ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		systemstarted = true;
		return "System has been started";
	};

	public static Route nextTerm = (Request req, Response resp) -> {
		int semindex = 0, semyear = 0;
		DBConnection conn = new DBConnection();
		String query1 = "Select * from semTable";
		try {
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query1);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				semYear = rs.getInt("termyear");
				semIndex = rs.getInt("semIndex");
				System.out.println("Updates are " + semYear + ", index" + semIndex);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (semesters[semIndex].equals("Summer")) {
			semYear++;
			semIndex = 0;
		} else if (semIndex != semesters.length)
			semIndex++;

		String query2 = "Update semTable set termyear=?,semIndex=?";

		try {
			System.out.println("Updates are " + semYear + ", index" + semIndex);
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query2);
			preparedStmt.setInt(1, semYear);
			preparedStmt.setInt(2, semIndex);
			preparedStmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Next term is initialized, year is" + semYear + ",term is:" + semesters[semIndex];

	};
	public static Route enterAcademicRecord = (Request req, Response resp) -> {
		
		
		
    	Map<String, String> map = new JsonUtil().parse(req.body());

		int studentuuid = Integer.parseInt(map.get("grsid"));
		int instuuid = Integer.parseInt(map.get("iid"));
		int courseuuid = Integer.parseInt(map.get("grcid"));
		String grade = map.get("grade");
		String termyear = semesters[semIndex]+semYear.toString();
		String comment = map.get("grcomment") != null ? map.get("grcomment") : null;
		StudentDAO studdao = new StudentDAOimpl();
		studdao.enterAcademicRecord(studentuuid, courseuuid, grade, instuuid, termyear, comment);
		return "Grades are recorded";
		
	};

	public static Route getStudentById = (Request req, Response resp) -> {
		StudentDAO studdao = new StudentDAOimpl();
		Student stud = studdao.returnStudentInfo(Integer.parseInt(req.params(":id")));
		HashMap<String, String> model = new HashMap<>();
		model.put("name", stud.name);
		return strictVelocityEngine().render(new ModelAndView(model, "student.vm"));
	};

	public static Route getStudentRecord = (Request req, Response resp) -> {
		StudentDAO studdao = new StudentDAOimpl();

		ArrayList<academicRecord> record = studdao.returnRecordForStudent(Integer.parseInt(req.params(":id")));
		HashMap<String, String> model = new HashMap<>();
		model.put("name", record.get(0).comment);
		return strictVelocityEngine().render(new ModelAndView(model, "student.vm"));

	};

	public static Route loadDB = (Request req, Response resp) -> {
		addStudents("/home/student/Downloads/students.csv");
		addInstructors("/Users/rohitpitke/Desktop/SA/new test cases/test_case5/instructors.csv");
		addCourses("/Users/rohitpitke/Desktop/SA/new test cases/test_case5/courses.csv");
		addTermCourses("/Users/rohitpitke/Desktop/SA/new test cases/test_case5/terms.csv");
		addpreReqs("/Users/rohitpitke/Desktop/SA/new test cases/test_case5/prereqs.csv");
		addEligibleCourses("/Users/rohitpitke/Desktop/SA/new test cases/test_case5/eligible.csv");
		HashMap<String, String> model = new HashMap<>();
		model.put("name", "test");
		return strictVelocityEngine().render(new ModelAndView(model, "helloworld.vm"));
	};

	private static String print() {
		HashMap<String, String> model = new HashMap<>();
		model.put("name", "Rohit");
		return strictVelocityEngine().render(new ModelAndView(model, "helloworld.vm"));
	}

	private static VelocityTemplateEngine strictVelocityEngine() {
		VelocityEngine configuredEngine = new VelocityEngine();
		configuredEngine.setProperty("runtime.references.strict", true);
		configuredEngine.setProperty("resource.loader", "class");
		configuredEngine.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		return new VelocityTemplateEngine(configuredEngine);
	}

	public static ArrayList<String[]> readFile(String file) {
		ArrayList<String[]> texts = new ArrayList<String[]>();
		try {
			File newFile = new File(file);
			FileReader fReader = new FileReader(newFile);
			BufferedReader bReader = new BufferedReader(fReader);
			String line = bReader.readLine();
			while (line != null) {
				texts.add(line.split(","));
				line = bReader.readLine();
			}
			bReader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return texts;
	}

	public static void addStudents(String filename) {
		ArrayList<String[]> student = readFile(filename);
		DBConnection conn = new DBConnection();

		for (String[] studdata : student) {
			String query = "insert into student values(?,?,?,?)";
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

	public static void addInstructors(String filename) {
		ArrayList<String[]> student = readFile(filename);
		DBConnection conn = new DBConnection();

		for (String[] studdata : student) {
			String query = "insert into instructor values(?,?,?,?,?)";
			try {
				PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
				preparedStmt.setInt(1, Integer.parseInt(studdata[0]));
				preparedStmt.setString(2, studdata[1]);
				preparedStmt.setString(3, studdata[2]);
				preparedStmt.setString(4, studdata[3]);
				preparedStmt.setString(5, "INACTIVE");
				preparedStmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void addCourses(String filename) {
		ArrayList<String[]> student = readFile(filename);
		DBConnection conn = new DBConnection();

		for (String[] studdata : student) {
			String query = "insert into course values(?,?)";
			try {
				PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
				preparedStmt.setInt(1, Integer.parseInt(studdata[0]));
				preparedStmt.setString(2, studdata[1]);
				preparedStmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void addTermCourses(String filename) {
		ArrayList<String[]> student = readFile(filename);
		DBConnection conn = new DBConnection();

		for (String[] studdata : student) {
			String query = "insert into term values(?,?)";
			try {
				PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
				preparedStmt.setInt(2, Integer.parseInt(studdata[0]));
				preparedStmt.setString(1, studdata[1]);
				preparedStmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void addpreReqs(String filename) {
		ArrayList<String[]> student = readFile(filename);
		DBConnection conn = new DBConnection();

		for (String[] studdata : student) {
			String query = "insert into prereqs values(?,?)";
			try {
				PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
				preparedStmt.setInt(2, Integer.parseInt(studdata[0]));
				preparedStmt.setInt(1, Integer.parseInt(studdata[1]));
				preparedStmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void addEligibleCourses(String filename) {
		ArrayList<String[]> student = readFile(filename);
		DBConnection conn = new DBConnection();

		for (String[] studdata : student) {
			String query = "insert into eligibleCourses values(?,?)";
			try {
				PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
				preparedStmt.setInt(1, Integer.parseInt(studdata[0]));
				preparedStmt.setInt(2, Integer.parseInt(studdata[1]));
				preparedStmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	public static void setCurrentTermWithYear(){
		DBConnection conn = new DBConnection();
		String[] semesters = { "Fall", "Winter", "Spring", "Summer" };

		int semyear=0,semindex=0;
		String query1 = "Select * from semTable";
		try {
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query1);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				semYear = rs.getInt("termyear");
				semIndex = rs.getInt("semIndex");
				
				System.out.println("Updates are " + semYear + ", index" + semIndex);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	 private static class tempClassHolder{
     	int instructor;
     	String term;
     	String grade;
     	String comment;
     	int id;
     	String desc;
     	
     	public tempClassHolder(String term, int instructor, String grade,String comment){
     		this.instructor = instructor;
     		this.comment = comment;
     		this.grade = grade;
     		this.term = term;
     	}
     	public tempClassHolder(int id,String desc){
     		this.id= id;
     		this.desc=desc;
     	}
     	
     	
     	
     }
     


}
