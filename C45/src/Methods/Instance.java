package Methods;

import java.util.Arrays;

public class Instance {
	
        private String userID;
	private String outputIndicator;
	private String[] userFeatures;
	
	public String getUserID() {
		return userID;
	}
	
        public void setUserID(String userID) {
		this.userID = userID;
	}
	
        public String getOutputIndicator() {
		return outputIndicator;
	}
	
        public void setOutputIndicator(String outputIndicator) {
		this.outputIndicator = outputIndicator;
	}
	
        public String[] getUserFeatures() {
		return userFeatures;
	}
	
        public void setUserFeatures(String[] userFeatures) {
		this.userFeatures = userFeatures;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return userID+";"+outputIndicator+";"+Arrays.toString(userFeatures);
	}
}
