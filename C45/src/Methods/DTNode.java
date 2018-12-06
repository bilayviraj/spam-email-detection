
package Methods;
import Methods.DecisionTree.DecisionTreeNodeType;

public class DTNode {
private DTNode leftNode;
	private DTNode rightNode;
	Feature feature;
	private String log;
	private String outputLabel;
	private DecisionTreeNodeType nodeType;
	
        public DTNode getLeftNode() {
		return leftNode;
	}
        
        public void setLeftNode(DTNode leftNode) {
		this.leftNode = leftNode;
	}
	
        public DTNode getRightNode() {
		return rightNode;
	}
	
        public void setRightNode(DTNode rightNode) {
		this.rightNode = rightNode;
	}
        
        public Feature getFeature() {
		return feature;
	}
	
        public void setFeature(Feature feature) {
		this.feature = feature;
	}
	
        public DecisionTreeNodeType getNodeType() {
		return nodeType;
	}
	
        public void setNodeType(DecisionTreeNodeType nodeType) {
		this.nodeType = nodeType;
	}
	
        public String getLog() {
		return log;
	}
	
        public void setLog(String log) {
		this.log = log;
	}
	
        public String getOutputLabel() {
		return outputLabel;
	}
        
	public void setOutputLabel(String outputLabel) {
		this.outputLabel = outputLabel;
	}
}
