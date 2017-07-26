package coursemanagement;

import weka.clusterers.SimpleKMeans;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.experiment.Stats;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void summarizeData(Instances data) throws Exception {

        HashMap<String, Double> completeData = new HashMap<>();

        if (data.classIndex() == 1) {
            data.setClassIndex(data.numAttributes() - 1);
        }
        //get number of attributes (notice class is not counted)
        int numAttr = data.numAttributes() - 1;
        for (int i = 0; i < numAttr; i++) {
            //check if current attr is of type nominal
            if (data.attribute(i).isNominal()) {
                System.out.println("The " + i + "th Attribute is Nominal");
                //get number of values
                int n = data.attribute(i).numValues();
                System.out.println("The " + i + "th Attribute has: " + n + " values");
            }

            //get an AttributeStats object
            AttributeStats as = data.attributeStats(i);
            int dC = as.distinctCount;
            System.out.println("The " + i + "th Attribute has: " + dC + " distinct values");

            //get a Stats object from the AttributeStats
            if (data.attribute(i).isNumeric()) {
                System.out.println("The " + i + "th Attribute is Numeric");
                Stats s = as.numericStats;
                System.out.println("The " + i + "th Attribute has min value: " + s.min + " and max value: " + s.max + " and mean value: " + s.mean + " and stdDev value: " + s.stdDev);
            }


        }
    }

    /**
     * Given source data, run a classification model and return a HashMap with Groups
     */

    public HashMap<Integer, ArrayList<Integer>> runClassification(Instances data) throws Exception{
    	
    	HashMap<Integer, ArrayList<Integer>> dataset = new HashMap<Integer, ArrayList<Integer>>();
    	
    	dataset.put(0, new ArrayList<Integer>());
    	dataset.put(1, new ArrayList<Integer>());
    	dataset.put(2, new ArrayList<Integer>());
    	dataset.put(3, new ArrayList<Integer>());
    	
    	if(data.size() < 2){
    		return dataset;
    	}

        SimpleKMeans model = new SimpleKMeans();

        model.setNumClusters(4);

        model.buildClusterer(data);
        
        int[] rawData = model.getAssignments();
        
        for(int i =0; i < rawData.length; i++){
        	int id = (int) data.instance(i).value(0);
        	dataset.get(id).add(rawData[i]);
        }
        return dataset;

    }




}
