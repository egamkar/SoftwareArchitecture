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

public class EntryPoint {

    public static void main(String[] args) {

        stop();
        port(9000);
        get("/initialize",loadDB);
        get("/getStudent/:id",getStudentById);

        /* ===== Read index file ===== */
        coursemanagement.CMSFileReader cms = new coursemanagement.CMSFileReader();
        get("/", (req, res) -> {
            return cms.readFile("index.html");
        });
        get("/index.html", (req, res) -> {
            return cms.readFile("index.html");
        });

        /* ===== Course Handlers ===== */
        get("/getCourses", (req, res) -> {
            // TODO: Call Login here
            String test_data = "[{'id':'22','desc':'Computer Programming'}, " +
                    "{'id':'23','desc':'Computer Networks'}, " +
                    "{'id':'24','desc':'Computer Architecture'}]";
            return test_data;
        });

        /* ===== Admin Handlers ===== */
        get("/loginAdmin/:id", (req, res) -> {
            // TODO: Call Login here
            return "Login Successful";
        });
        get("/loadData/:path", (req, res) -> {
            // TODO: Load data here
            return "Data Import Successful";
        });
        post("/addCourse", (req, res) -> {
            // TODO: Add a new Course to the database
            // NOTE:  example: req.body() will return {"desc":"Computer Programming"}
            System.out.println(req.body());
            return "Add Course Successful";
        });
        post("/addInstructor", (req, res) -> {
            // TODO: Add a new Instructor to the database
            // NOTE:  example: req.body() will return {"name":"Mark Moss"}
            System.out.println(req.body());
            return "Add Instructor Successful";
        });
        post("/addStudent", (req, res) -> {
            // TODO: Add a new Student to the database
            // NOTE:  example: req.body() will return {"name":"Rohit Pitke"}
            System.out.println(req.body());
            return "Add Student Successful";
        });
        post("/hireInstructor", (req, res) -> {
            // TODO:
            // NOTE:  example: req.body() will return {"id":"22"}
            System.out.println(req.body());
            return "Instructor Hired";
        });
        post("/leaveInstructor", (req, res) -> {
            // TODO:
            // NOTE:  example: req.body() will return {"id":"22"}
            System.out.println(req.body());
            return "Instructor Left";
        });
        post("/startTerm", (req, res) -> {
            // TODO:
            // NOTE:  example: req.body() will return {"term"   :"Fall-2017"}
            System.out.println(req.body());
            return "Term Started";
        });
        get("/advanceTerm", (req, res) -> {
            // TODO:
            System.out.println("Advance Term");
            return "Term Advanced";
        });
        get("/coursesForPrereqs/:courseid", (req, res) -> {
            /* TODO:  need a list of courses in the following format.
                - The list should not contain courseid
                - The list should not contain Courses that already have courseid as their pre-req
             */
            System.out.println("Prereq called");
            /* Comment this after code is completed */
            String test_data = "[{'id':'22','desc':'Computer Programming','prereq':'yes'}, " +
                    "{'id':'23','desc':'Computer Networks','prereq':'no'}, " +
                    "{'id':'24','desc':'Computer Architecture','prereq':'no'}]";
            System.out.println(test_data);
            return "Hello World";
        });


        /* ===== Student Handlers ===== */
        get("/loginStudent/:id", (req, res) -> {
            // TODO:
            return "Not implemented yet";
        });

        /* ===== Instructor Handlers ===== */
        get("/loginInstructor/:id", (req, res) -> {
            // TODO:
            return "Not implemented yet";
        });



    }



    public static Route getStudentById = (Request req,Response resp)->{
        StudentDAO studdao = new StudentDAOimpl();
        Student stud = studdao.returnStudentInfo(Integer.parseInt(req.params(":id")));
        HashMap<String,String>model = new HashMap<>();
        model.put("name", stud.name);
        return strictVelocityEngine().render(new ModelAndView(model,"student.vm"));




    };


    public static Route loadDB = (Request req, Response resp)->{
        addStudents("/Users/rohitpitke/Desktop/SA/new test cases/test_case1/students.csv");
        addInstructors("/Users/rohitpitke/Desktop/SA/new test cases/test_case1/instructors.csv");
        addCourses("/Users/rohitpitke/Desktop/SA/new test cases/test_case1/courses.csv");
        addTermCourses("/Users/rohitpitke/Desktop/SA/new test cases/test_case1/terms.csv");
        addpreReqs("/Users/rohitpitke/Desktop/SA/new test cases/test_case1/prereqs.csv");
        addEligibleCourses("/Users/rohitpitke/Desktop/SA/new test cases/test_case1/eligible.csv");
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
    public static void addInstructors(String filename){
        ArrayList<String[]> student = readFile(filename);
        DBConnection conn = new DBConnection();

        for(String[] studdata:student){
            String query= "insert into instructor values(?,?,?,?)";
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

    public static void addCourses(String filename){
        ArrayList<String[]> student = readFile(filename);
        DBConnection conn = new DBConnection();

        for(String[] studdata:student){
            String query= "insert into course values(?,?)";
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

    public static void addTermCourses(String filename){
        ArrayList<String[]> student = readFile(filename);
        DBConnection conn = new DBConnection();

        for(String[] studdata:student){
            String query= "insert into term values(?,?)";
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

    public static void addpreReqs(String filename){
        ArrayList<String[]> student = readFile(filename);
        DBConnection conn = new DBConnection();

        for(String[] studdata:student){
            String query= "insert into prereqs values(?,?)";
            try {
                PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
                preparedStmt.setInt(2, Integer.parseInt(studdata[0]));
                preparedStmt.setInt(1,Integer.parseInt(studdata[1]));
                preparedStmt.execute();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void addEligibleCourses(String filename){
        ArrayList<String[]> student = readFile(filename);
        DBConnection conn = new DBConnection();

        for(String[] studdata:student){
            String query= "insert into eligibleCourses values(?,?)";
            try {
                PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query);
                preparedStmt.setInt(1, Integer.parseInt(studdata[0]));
                preparedStmt.setInt(2,Integer.parseInt(studdata[1]));
                preparedStmt.execute();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }



}
