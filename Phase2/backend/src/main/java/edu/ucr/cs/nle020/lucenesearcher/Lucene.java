package edu.ucr.cs.nle020.lucenesearcher;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

//import java.io.IOException;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Seans imports
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.FileSystems;

public class Lucene{

    public static void index() {
        try
		{
            String html_location  = "./html"; // local html
			String index_location = "./indx"; // local index
			
			//index directory path
            Path the_path = Paths.get(index_location);
			
	        // To store an index on disk:
            Directory directory = FSDirectory.open(the_path);
	        
	        //config details for writer
	        Analyzer analyzer = new StandardAnalyzer();
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);
	        
	        //instantiate writer for creating index
	        IndexWriter indexWriter = new IndexWriter(directory, config);
	        
            //Need to parse our html files and created a real list of pages
	        //-------------------------------
            List<Page> pages = soup.processFiles(html_location);
	        //-------------------------------
	
	        //for each page - grab necessary attributes
	        for (Page page: pages)
	        {
	        	org.apache.lucene.document.Document doc = new Document();
	        	
	        	//attributes to store about each feature
	        	doc.add(new TextField("url", page.url, Field.Store.YES));
	            doc.add(new TextField("title", page.title, Field.Store.YES));
	            doc.add(new TextField("content", page.content, Field.Store.YES));
	            
	            //add document to index
	            indexWriter.addDocument(doc);
	        }
	 
	        indexWriter.close();
		}
		catch (Exception | Error e)
	    {
			System.out.println("Exception | Error");
			System.out.println(e.getMessage());
		} 
    }

    public static List<Result> search(String input) throws IOException, ParseException {
        // Opens currently existing directory
        // should catch errors and exceptions
        
        String idx_location = "./indx"; // local index
        
        //index directory path
        Path a_path = FileSystems.getDefault().getPath(idx_location);
        
        // To store an index on disk:
        Directory a_directory = FSDirectory.open(a_path);
        
        // Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(a_directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();

        String[] fields = {"url", "title", "content"};
        Map<String, Float> boosts = new HashMap<>();
        
        //Weight importance of different document attributes
        boosts.put(fields[0], 1.0f); // url
        boosts.put(fields[1], 2.0f); // title
        boosts.put(fields[2], 1.0f); // content
        
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
                
        // Query query = parser.parse("UCR");
        Query query = parser.parse("UCR discussion");
        
        /* parser for if want to specify fields and do fancy stuff */
        // QueryParser parser = new QueryParser("content", analyzer);
        // Query query = parser.parse("(title:ucr)^1.0 (content:ucr)^0.5");
        
        //Don't know what this outputs, but it'll be more than query string
        System.out.println(query.toString());
        
        //Number of websites to return
        int topHitCount = 10;
        
        //Search Index for hits that match the query most
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;

        // Iterate through the results: 
        //   -Assuming they are sorted by rank high to low already
        //   -Instead of outputting, should put them into JSON file
        //   -give back JSON to web server
        List<Result> results = new ArrayList<Result>(); // returning this for now - Jake
        for (int rank = 0; rank < hits.length; ++rank) {
            Document hitDoc = indexSearcher.doc(hits[rank].doc);
            results.add( new Result( rank+1, hitDoc.get("title"), hitDoc.get("url"), "SNIPPET" /*hitDoc.get("content")*/, 0 ) ); // need to trim snippet out of content
            // System.out.println(indexSearcher.explain(query, hits[rank].doc));
        }
        indexReader.close();
        a_directory.close();
        return results;
    }
}