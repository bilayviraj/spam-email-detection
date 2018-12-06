package Methods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface DTConstants {

	String OUTPUT_LABELS = "output_labels";
}


public class DataLoader implements DTConstants {

    public Features loadFeatures(File featuresFile) throws IOException {
        BufferedReader br = null;
        Features features = new Features();

        try {
            br = new BufferedReader(new FileReader(featuresFile));
            String line = "";
            List<Feature> featureList = new ArrayList<Feature>();
            int ftrIdx = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.startsWith("//")) {
                    continue;
                }

                String[] featureArr = line.split("\\s+");
                Feature f = new Feature();

                if (featureArr.length == 3) {
                    f.setfName(featureArr[0]);
                    f.setfOptions(Arrays.copyOfRange(featureArr, 1, 3));
                    f.setIdx(ftrIdx);
                    featureList.add(f);
                }
                ftrIdx++;
            }

            features.setFeatures(featureList);
            br.close();
        } catch (IOException e) {
            throw new DTException("Failed to read feature file " + featuresFile.getAbsolutePath(), e);
        } finally {
            br.close();
        }

        System.out.println("Successfully loaded features.\n No.of features= " + features.getFeatures().size());
        return features;
    }

    public Instances loadData(File dataFile, List<Feature> featureList) throws IOException {
        BufferedReader br = null;
        Instances instances = new Instances();
        int flen = featureList.size();
        try {
            br = new BufferedReader(new FileReader(dataFile));
            String line = "";
            List<Instance> instanceList = new ArrayList<Instance>();
            while ((line = br.readLine()) != null) {
                // age_lte_30 t f
                String[] instanceArr = line.split("\\s+");
                Instance i = new Instance();

                if (instanceArr.length == flen + 2) {
                    i.setUserID(instanceArr[0].trim());
                    i.setOutputIndicator(instanceArr[1].trim());
                    i.setUserFeatures(Arrays.copyOfRange(instanceArr, 2, flen + 2));

                    instanceList.add(i);
                }
            }

            instances.setExamples(instanceList);
            br.close();
        } catch (IOException e) {
            throw new DTException("Failed to read testing dataset file " + dataFile.getAbsolutePath(), e);
        } finally {
            br.close();
        }
            return instances;
    }
    
    public Instances loadTestData(File testDataFile, List<Feature> featureList) throws IOException {
		Instances instances = loadData(testDataFile, featureList);
		System.out.println("Successfully loaded testing dataset of size "+instances.getExamples().size());
		return instances;
	}
	
    public Instances loadTrainData(File trainDataFile, List<Feature> featureList) throws IOException {
		Instances instances = loadData(trainDataFile, featureList);
		System.out.println("Successfully loaded training dataset of size "+instances.getExamples().size());
		return instances;
	}

    public String[] loadOutputLabels(File featuresFile) throws IOException {
		BufferedReader br = null;
		String[] outlabels = new String[2];
		try {
			br = new BufferedReader(new FileReader(featuresFile));
			String line = "";
			while((line = br.readLine()) != null) {
				
				if(line.startsWith(OUTPUT_LABELS)) {
					String[] keyValue = line.split("=");
					String value = keyValue[1];
					outlabels = value.split("\\s+");
				}
			}
			
			br.close();
		} catch (IOException e) {
			throw new DTException("Failed to read feature file "+featuresFile.getAbsolutePath(), e);
		} finally {
			 br.close(); 
		}
		return outlabels;
	}
}

 class DTException extends RuntimeException {

	       
	public DTException(String message) {
		super(message);
	}
        
        public DTException(Throwable cause) {
		super(cause);
	}

	public DTException(String message, Throwable cause) {
		super(message, cause);
	}

}

