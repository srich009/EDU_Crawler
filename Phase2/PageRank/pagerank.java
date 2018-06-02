import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class pagerank {

	public static void main(String[] args) {
		//List<Page> pages = new ArrayList<Page>();
		String folder = "C:\\Users\\lonelogical\\Documents\\Computer_Science\\CS172_Information_Retrieval\\proj_phase2\\EDU_Crawler\\pages";
		
		try {
			// grab the manifest
			String manLocation = folder + "\\0-manifest.txt";    
		    File manFile = new File(manLocation);
			org.jsoup.nodes.Document manifest = Jsoup.parse(manFile, "UTF-8");
			String man = manifest.text();
			//tokenize manifest and create hashmap
			HashMap<String, Integer> docMap = new HashMap<>();
			StringTokenizer st = new StringTokenizer(man);
			while (st.hasMoreTokens()) {
				String temp = st.nextToken();
				Integer dID = Integer.parseInt(temp);
				temp = st.nextToken();
				//System.out.println(dID + " -- " + temp);
				if (!docMap.containsKey(temp)) {
					docMap.put(temp, dID);
				}
			}
			System.out.println("Hashmap was made. map size: " + docMap.size());
			
			String matrxFileName = "pageRankInfo.csv";
			File file = new File(folder + "\\" + matrxFileName);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			/*
			for (Entry<String, Integer> entry : docMap.entrySet()) {
			    String key = entry.getKey();
			    Integer value = entry.getValue();
			    System.out.println(value + " -- " + key);
			}
			*/
			
				//System.out.println(i + ") " + );
		  
			/*
			//for each doc, grab outgoing links
			for( int i = 1; i < docs.size(); i++)
			{
				System.out.println(i + ") " + docs.get(i).location());
				//Integer currDocID = docs.get(i).
				//pages.add( new Page( urls.get(i), docs.get(i).title(), docs.get(i).text() ) );
			*/
			Integer numDocs = 100;
			for (Integer i = 1; i <= numDocs; i++) {
				//grab document associated with #i
				String docIFile = folder + "\\" + i + ".html";	    
			    File docInput = new File(docIFile);
				org.jsoup.nodes.Document doc = Jsoup.parse(docInput, "UTF-8");
				//System.out.println(i + ") " + doc.location());
				//grab outgoing links of that document
				Elements links = doc.select("a[href]");
				String matrxText = Integer.toString(i) + ", " + Integer.toString(links.size()); 
				
				for (Element e : links) {
					String abs_ref = e.attr("abs:href");
					//look in hashmap for docID
					Integer linkID = docMap.get(abs_ref);
					//System
					if (linkID != null) {
						//System.out.println("   " + linkID + ") " + abs_ref);
						matrxText = matrxText + ", " + linkID.toString();
					}
				}
				//append text to file
				bw.write(matrxText + "\n"); 
			} //end for all documents
			bw.close(); //close buffered writer for matrix file
			System.out.println("matrxText was written and closed");
		}
		catch (Exception | Error e)
	    {
			System.out.println("Exception | Error");
			System.out.println(e.getMessage());
		}

	} // end main

}
