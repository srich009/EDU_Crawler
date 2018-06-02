
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Scanner;

public class Lucene_Indexer {
	
	public static void main(String[] args) {
		
		// Arguments passed into main function
		//index directory path
		Path dirPath = Paths.get(args[0]); //path to make lucene index
		String pagesFolder = args[1]; //folder that contains html and manifest pages
		Integer numDocs = Integer.parseInt(args[2]);
		
		List<org.jsoup.nodes.Document> docs = new ArrayList<org.jsoup.nodes.Document>();
		
		try {
			//Grab manifest information and put in hashmap
			// grab the manifest
			String manLocation = pagesFolder + "\\0-manifest.txt";    
		    File manFile = new File(manLocation);
			org.jsoup.nodes.Document manifest = Jsoup.parse(manFile, "UTF-8");
			String manText = manifest.text();
			//tokenize manifest and create hashmap
			HashMap<Integer, String> docMap = new HashMap<>();
			StringTokenizer st = new StringTokenizer(manText);
			while (st.hasMoreTokens()) {
				String temp = st.nextToken();
				Integer dID = Integer.parseInt(temp);
				temp = st.nextToken();
				docMap.put(dID, temp);
			}
			System.out.println("Hashmap was made. map size: " + docMap.size());
			
			//Grab pagerank file and put it in hashmap
			HashMap<Integer, Integer> rankMap = new HashMap<>();
			String pagerankLocation = pagesFolder + "\\pagerankFile.csv";
			Scanner scanner = new Scanner(new File(pagerankLocation));
	        scanner.useDelimiter(",");
	        while (scanner.hasNext()) {
	        	Integer dID = Integer.parseInt(scanner.next());
	        	Integer dRank = Integer.parseInt(scanner.next());
	            rankMap.put(dID, dRank);
	        }
	        scanner.close();
	        
	        // To store an index on disk:
	        Directory directory = FSDirectory.open(dirPath);
	        //config details for writer
	        Analyzer analyzer = new StandardAnalyzer();
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);
	        //instantiate writer for creating index
	        IndexWriter indexWriter = new IndexWriter(directory, config);
			
	        //Iterate through all the html documents to pass into Lucene
			for (Integer i = 1; i <= numDocs; i++) {
				//grab document associated with #i
				String docIFile = pagesFolder + "\\" + i + ".html";	    
			    File docInput = new File(docIFile);
				org.jsoup.nodes.Document doc = Jsoup.parse(docInput, "UTF-8");
				
				Document lucDoc = new Document();
	            lucDoc.add(new TextField("title", doc.title(), Field.Store.YES));
	            lucDoc.add(new TextField("url", docMap.get(i), Field.Store.YES));
	            lucDoc.add(new TextField("content", doc.body().text(), Field.Store.NO));
	            //lucDoc.setBoost(3.0f);
	            
	            //add document to index
	            indexWriter.addDocument(lucDoc);
			}
	        
	        indexWriter.close();
			
		} catch (Exception e) {
			
		} catch (Error e) {
			
		}
		
        
	}

}
