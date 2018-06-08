import java.util.HashMap;
import java.util.List;

public class Adjacency {
	static Integer[] countList;
	static HashMap<Integer, List<Integer>> incomingMap = new HashMap<>();
	
	Adjacency(Integer[] i, HashMap<Integer, List<Integer>> imap) {
		countList = i;
		incomingMap = imap;
	}
	
	Integer[] getCounts() {
		return countList;
	}
	
	HashMap<Integer, List<Integer>> getLinks() {
		return incomingMap;
	}
	
	
}
