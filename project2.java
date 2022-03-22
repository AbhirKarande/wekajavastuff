import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class project2 {
	public static void main (String[] args) {
		try {
            //  first get the full features file
			String[][] csvData = MyWekaUtils.readCSV("features.csv");      
			//System.out.println(csvData[0][0]);
            //int[] features = {0, 1, 2, 3, 4, 5}; // all the features
			int[] features = {0, 1, 2,3,4,5}; // use mean_x, mean_y, std_z 
			List<Integer> featureList = new ArrayList<Integer>();
//			int[] temp = new int[6];
			double max = 0;
			int index = 0;
			for(int i = 0; i<features.length; i++) {
				String arffData = MyWekaUtils.csvToArff(csvData, new int[] {i});
				double accuracy = MyWekaUtils.classify(arffData, 1);
				//System.out.println(accuracy);
				if(accuracy > max) {
					max = accuracy;
					index = i;
				}
				
				
				
	            
			}
			featureList.add(index);
			
			int temp = 1;
			
			while(temp < featureList.size()) {
				List<Double> scores = new ArrayList<Double>();
				List<Integer> indeces = new ArrayList<Integer>();
				
				int idx = 0;
				while(idx < featureList.size()-1) {
					if(features[idx] != featureList.get(0)) {
						featureList.add(features[idx]);
						System.out.println("temp" + featureList);
						String arffData = MyWekaUtils.csvToArff(csvData, convertIntegers(featureList));
						featureList.remove(features[idx]);
						scores.add(Double.parseDouble(arffData));
						indeces.add(features[idx]);
						
					}
					idx += 1;
				int IndexOfBest = scores.indexOf(Collections.max(scores));
				featureList.add(indeces.get(IndexOfBest));
				System.out.println(IndexOfBest);
				temp += 1;
					
					
				}
			}
			
			System.out.println(featureList);
			//System.out.println(featureList[0]);
			
			//System.out.println(max + " " + index);
			
//            String arffData = MyWekaUtils.csvToArff(csvData, features);
//            System.out.println(arffData);
//            
//            double accuracy = MyWekaUtils.classify(arffData, 1);
//            System.out.println(accuracy);
			
			
                        
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
	}
	public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}
