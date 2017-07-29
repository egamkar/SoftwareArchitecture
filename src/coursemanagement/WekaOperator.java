package coursemanagement;

import weka.clusterers.SimpleKMeans;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.experiment.Stats;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class WekaOperator {

    /**
     * Query MySQL to Get Data For Weka
     */
    public Instances queryWeka(String rawQuery) throws Exception {
    	
    	Class.forName("com.mysql.jdbc.Driver");
        InstanceQuery query = new InstanceQuery();
        query.setDatabaseURL("jdbc:mysql://localhost:3306/softwareArch?#characterEncoding=UTF-8");
        query.setUsername("root");
        query.setPassword("cs6310");
        query.setQuery(rawQuery);
        
        return query.retrieveInstances();
    }

    /**
     * Summarize Data Print to Console for Now but return JSON later
     * //TODO: Push attributes to a bash map for JSON conversion
     */
    public HashMap<String, String> summarizeData() throws Exception {

        HashMap<String, String> completeData = new HashMap<>();
        
        completeData.put("total_number_of_students", String.valueOf(queryTotalStudents()));
        completeData.put("course_with_the_most_students", queryTopCourse());
        completeData.put("Total_number_of_courses_offered", String.valueOf(queryCourseCount()));
        
        return completeData;
    }
    
    public int queryTotalStudents(){
    	DBConnection conn = new DBConnection();
		String query1 = "select count(*) as count from student";
		
		int data = 0;
		try {
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query1);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				data = rs.getInt("count");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 0;
		}
		
		return data;
    }
    
    public String queryTopCourse(){
    	DBConnection conn = new DBConnection();
		String query1 = "select top 1 count(*) as count, courseuuid from academicRecord group by courseuuid order by count desc";
		
		String data = "";
		try {
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query1);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				data = rs.getString("courseuuid");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return "N/A";
		}
		
		return data;
    }
    
    public int queryCourseCount(){
    	DBConnection conn = new DBConnection();
		String query1 = "select COUNT(*) as count from course";
		
		int data = 0;
		try {
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query1);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				data = rs.getInt("count");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		return data;
    }

    /**
     * Given source data, run a classification model and return a HashMap with Groups
     */

    public ArrayList<ArrayList<Integer>> runClassification(Instances data) throws Exception{
    	
    	HashMap<Integer, ArrayList<Integer>> dataset = new HashMap<Integer, ArrayList<Integer>>();
    	ArrayList<ArrayList<Integer>> lastData = new ArrayList<ArrayList<Integer>>();
    	dataset.put(0, new ArrayList<Integer>());
    	dataset.put(1, new ArrayList<Integer>());
    	dataset.put(2, new ArrayList<Integer>());
    	dataset.put(3, new ArrayList<Integer>());
    	
    	if(data.size() < 6){
    		return lastData;
    	}

        SimpleKMeans model = new SimpleKMeans();
        

        model.setNumClusters(4);
        model.setSeed(10);
        model.setPreserveInstancesOrder(true);
        model.buildClusterer(data);
        
        
        int[] rawData = model.getAssignments();

        for(int i =0; i < rawData.length; i++){
        	int id = (int) data.instance(i).value(0);
        	dataset.get(rawData[i]).add(id);
        }
        
           
        for (int key: dataset.keySet()){
        	lastData.add(dataset.get(key));
        }
        return lastData;

    }




}
