import java.util.ArrayList;
import java.util.List;


public class Node implements Comparable <Node> {

	int pieceID, /*player,*/ visits;
	double winDepth;
	int[] move;
	ArrayList <Node> children;
	Node parent;
	
	public Node () {
		
		parent = null;
		children = new ArrayList <Node> ();
		pieceID = -1;
		move = new int[2];
		visits = 0;
		winDepth = 1000;
		//player = 0;

	}
	
	public Node getParent () {
		
		return this.parent;
		
	}
	
	public List <Node> getChildren () {
		
		return this.children;
		
	}
	
	public boolean hasParent () {
		
		//If it has a parent, return true, otherwise false.
		return (this.parent == null) ? false : true;
		
	}

	

	@Override
	public int compareTo(Node o) {
		
		/*double thisWinRate =((this.wins + 0.01) / this.visits);
		double thatWinRate =((o.wins + 0.01) / o.visits);*/
		
		if (this.winDepth < o.winDepth) return -1;
		
		else if (this.winDepth > o.winDepth) return 1;
		
		else return 0;
	
	}
	
	/*public double getValue () {
		
		//double value = 0;
		double value = ((this.wins + 0.01) / this.visits);
		
		return value;
		
	}*/
	

	
}