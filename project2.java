import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class project2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* Get user inputs */
		while(true) {
			Scanner input = new Scanner(System.in);
			System.out.println("Number of features per axis:");
			String num_features = input.next();
			System.out.println("Window size (in sec):");
			String window_size = input.next();
			System.out.println("Algo (1:J48 2:RF 3:SMO):");
			int algo = input.nextInt();
			
			/* Run the python script that generate features.csv */
			try {
				Process p = Runtime.getRuntime().exec("python FeatureExtraction.py "+num_features+" "+window_size); //featureExtraction.py {num_features} {window_size}
				p.waitFor();
			}catch (IOException e) {
				System.out.println(e.toString());
			}catch (InterruptedException e) {
				System.out.println(e.toString());
			}
			
			/* WEKA process */
			try {
	            /* Get the full features file */
				String[][] csvData = MyWekaUtils.readCSV("features.csv"); 
				
				/* Define feature indices */
				int[] features;
				if(num_features.equals("2")) {
					features = new int[]{0, 1, 2, 3, 4, 5}; // all the features
					//features = new int[]{0, 2, 4}; // use mean_x, mean_y, std_z 
				}else {
					features = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
				}
				
				/* Sequential feature selection */
				List<Integer> featureList = new ArrayList<Integer>();
				double max = 0;
				int index = 0;
				
				// choose the most important feature
				for(int i = 0; i<features.length; i++) {
					String arffData = MyWekaUtils.csvToArff(csvData, new int[]{i});
					double accuracy = MyWekaUtils.classify(arffData, algo);
					//System.out.println(accuracy);
					if(accuracy > max) {
						max = accuracy;
						index = i;
					}
				}
				featureList.add(index);
				double current_accuracy = max; // accuracy so far
				System.out.println("most_important_feature : "+index);
				System.out.println("current_accuracy: "+current_accuracy);
				System.out.println();
				
				// iteratively choose the most important feature from the rest until there's not much gain in accuracy
				int temp = 1;
				while(temp < features.length) {
					max = 0; // current max accuracy
					index = 0; // index of best
					
					// try all possible combinations
					for(int idx = 0; idx < features.length; idx++) {
						if(featureList.indexOf(idx) < 0) {
							featureList.add(idx);
							
							// train and get accuracy
							String arffData = MyWekaUtils.csvToArff(csvData, convertIntegers(featureList));
							double accuracy = MyWekaUtils.classify(arffData, algo);
							//System.out.println(featureList.toString());
							
							// compare accuracy
							if(accuracy > max) {
								max = accuracy;
								index = idx;
							}
							featureList.remove(temp);
						}
					}
					
					// check accuracy gain
					if(max - current_accuracy >= 1) {
						featureList.add(index);
						current_accuracy = max;
						System.out.println("new_feature: "+index);
						System.out.println("current_accuracy: "+current_accuracy);
						System.out.println();
					}else {
						if(max - current_accuracy > 0) {
							featureList.add(index);
							current_accuracy = max;
							System.out.println("new_feature: "+index);
						}
						System.out.println("gain in accuracy is less than 1%.");
						System.out.print("features selected: ");
						System.out.println(featureList.toString());
						System.out.println("accuracy: "+current_accuracy);
						System.out.println();
						break;
					}
					
					temp += 1;		
				}
				
//				/* transform csv to arff */
//				String arffData = MyWekaUtils.csvToArff(csvData, features);
//	          	// System.out.println(arffData);
//	            
//	          	// train the model
//	          	double accuracy = MyWekaUtils.classify(arffData, algo);
//	          	System.out.println(accuracy);
	            
	        } catch (Exception ex) {
	            System.out.println(ex.toString());
	        }
			
			/* Restart logic */
			System.out.println("Restart?(y/n):");
			String r = input.next();
			if(r.equals("n")) {
				input.close();
				break;
			}
		}
		
	}
	
	public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i);
        }
        return ret;
    }

}