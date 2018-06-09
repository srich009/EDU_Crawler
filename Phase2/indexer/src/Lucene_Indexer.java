
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Scanner;

public class Lucene_Indexer {
	
	public static void main(String[] args) {
		
		System.out.println("-> inside Indexer Main");
		String currFolder = args[0];
		String indFolder = currFolder + "/../backend/indx";
		Path dirPath = Paths.get(indFolder);
		String pagesFolder = currFolder + "/../backend/html";
		Integer numDocs = Integer.parseInt(args[1]);
		
		try {
			//Grab manifest information and put in hashmap
			// grab the manifest
			String manLocation = pagesFolder + "/0-manifest.txt";    
		    File manFile = new File(manLocation);
			org.jsoup.nodes.Document manifest = Jsoup.parse(manFile, "UTF-8");
			String manText = manifest.text();
			//tokenize manifest and create hashmap
			HashMap<Integer, String> docMap_itos = new HashMap<>();
			HashMap<String, Integer> docMap_stoi = new HashMap<>();
			StringTokenizer st = new StringTokenizer(manText);
			while (st.hasMoreTokens()) {
				String temp = st.nextToken();
				Integer dID = Integer.parseInt(temp);
				temp = st.nextToken();
				docMap_itos.put(dID, temp);
				docMap_stoi.put(temp, dID);
			}
			System.out.println("Hashmap was made. map size: " + docMap_itos.size());
			
			//Get adjacency of webgraph
			Adjacency webGraph = getPageAdjacency(numDocs, docMap_stoi, pagesFolder);
			
			//Calculate PageRank
			Float[] pageRanks = calcPageRank(numDocs, webGraph);
	        
	        // To store an index on disk:
	        Directory directory = FSDirectory.open(dirPath);
	        //config details for writer
	        Analyzer analyzer = new StandardAnalyzer();
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);
	        //instantiate writer for creating index
	        IndexWriter indexWriter = new IndexWriter(directory, config);
			
	        //Iterate through all the html documents to pass into Lucene
			for (Integer i = 1; i <= numDocs; i++) {
				try {
					//System.out.println("Starting " + i);
					//grab document associated with #i
					String docIFile = pagesFolder + "/" + i + ".html";	    
				    File docInput = new File(docIFile);
					org.jsoup.nodes.Document doc = Jsoup.parse(docInput, "UTF-8");
					
					if (i % 1000 == 0) {
						System.out.println(i);
					}
					
					Document lucDoc = new Document();
		            lucDoc.add(new TextField("title", doc.title(), Field.Store.YES));
		            lucDoc.add(new TextField("url", docMap_itos.get(i), Field.Store.YES));
		            lucDoc.add(new TextField("content", doc.body().text(), Field.Store.NO));
		            //lucDoc.add(new StoredField("pageRank", 13.0f));
		            lucDoc.add(new StoredField("pageRank", pageRanks[i-1]));
		            //System.out.println("finished " + i);
		            
		            //add document to index
		            indexWriter.addDocument(lucDoc);
				} catch (Exception | Error e){
					System.out.println(e);
				}
			}
	        indexWriter.close();
			System.out.println("Done indexing");
		} catch (Exception e) {
			System.out.println(e);
		} catch (Error e) {
			System.out.println(e);
		}  
	}
	
	static Float[] calcPageRank(Integer numDocs, Adjacency webGraph) {
		
		Float[] prevRank = new Float[numDocs];
		Float[] currRank = new Float[numDocs];
		//HashMap of count of outgoing edges
		Integer[] countList =  webGraph.getCounts();
		//HashMap of incoming edges
		HashMap<Integer, List<Integer>> incomingMap = webGraph.getLinks();
		
		for (Integer j = 0; j < numDocs; j++) {
			currRank[j] = 1.0f; //init pageranks
		}
		
		System.out.println("Starting calculations");
		//calculate pageranks
		Float d = 0.7f;
		for (Integer iter = 0; iter < 30; iter++) {
			prevRank = currRank;
			//for each node in incoming Map
			for (Map.Entry<Integer, List<Integer>> node : incomingMap.entrySet()) {
				List<Integer> myIncoming = node.getValue();
				Integer myID = node.getKey();
				if (myIncoming == null) {
					System.out.println(myID + " null");
				}
				//System.out.println(myID + " -- links: " + myIncoming.size());
				Float newBaseRank = (1.0f - d) * (1.0f / numDocs);
				Float newConRank = 0.0f;
				for (Integer income : myIncoming) {
					newConRank = newConRank + (prevRank[income-1] / countList[income - 1]);
				}
				//System.out.println("here1");
				newConRank = newConRank * d;
				currRank[myID-1] = newBaseRank + newConRank;
			}
		}
		System.out.println("Done iterating ");
		
		return currRank;
	}

	static Adjacency getPageAdjacency(Integer numDocs, HashMap<String,Integer> docMap, String folder) {
		Integer[] countList = new Integer[numDocs];
		HashMap<Integer, List<Integer>> incomingMap = new HashMap<>();
		
		for (Integer i = 1; i <= numDocs; i++) {
			try {
				//grab document associated with #i
				String docIFile = folder + "/" + i + ".html";	    
			    File docInput = new File(docIFile);
				org.jsoup.nodes.Document doc = Jsoup.parse(docInput, "UTF-8");
				//grab outgoing links of that document
				Elements links = doc.select("a[href]");
				countList[i-1] = links.size(); //update number of outgoing docs
				//make list if not already exists in Hashmap
				if (incomingMap.get(i) == null) {
	        		List<Integer> myIncoming = new ArrayList<>();
	        		incomingMap.put(i, myIncoming);
	        	}
				for (Element e : links) {
					String abs_ref = e.attr("abs:href");
					//look in hashmap for docID
					Integer linkID = docMap.get(abs_ref);
					//System
					if (linkID != null) {
						List<Integer> myIncoming = incomingMap.get(linkID);
		        		if (myIncoming == null) {
		        			//must create new list
		        			myIncoming = new ArrayList<Integer>();
		        			myIncoming.add(i); //doc_i ---hyperlink----> linkID
		        			incomingMap.put(linkID, myIncoming);
		        		} else {
		        			//must append to old list
		        			myIncoming.add(i);
		        			incomingMap.put(linkID, myIncoming);
		        		}
					}
				} //end for each link
			} //end try
			catch (Exception | Error e)
		    {
				System.out.println(e.getMessage());
			}
		} //end for all documents
		
		System.out.println("matrxText was written");
		
		System.out.println("countList size: " + countList.length);
		System.out.println("incomingMap size: " + incomingMap.size());
		
		Adjacency webGraph = new Adjacency(countList, incomingMap);
		return webGraph;
	}
}
