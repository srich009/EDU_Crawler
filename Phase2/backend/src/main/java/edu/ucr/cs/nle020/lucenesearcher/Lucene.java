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

// hashmapper
import org.jsoup.Jsoup;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Lucene
{
    public static void index() 
    {
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
	        for (Page page : pages)
	        {
	        	org.apache.lucene.document.Document doc = new Document();
	        	
	        	//attributes to store about each feature
	        	doc.add(new TextField("url",     page.url,             Field.Store.YES));
	            doc.add(new TextField("title",   page.title,           Field.Store.YES));
                doc.add(new TextField("content", page.content,         Field.Store.YES));
                doc.add(new TextField("rank",    page.rank.toString(), Field.Store.YES));
	            
	            //add document to index
	            indexWriter.addDocument(doc);
	        }
	 
	        indexWriter.close();
		}
		catch (Exception | Error e)
	    {
            System.out.println("Exception | Error");
            System.out.println("here is the error in soup.java");
			System.out.println(e.getMessage());
		} 
    }
    //-------------------------------------------------------------------------

    public static List<Result> search(String input) 
        throws IOException, ParseException 
    {
        String idx_location = "./indx"; // local index
        String html_location = "./html"; // local html

        //Grab manifest information and put in hashmap
        // grab the manifest
        String manLocation = html_location + "/0-manifest.txt";    
        File manFile = new File(manLocation);
        org.jsoup.nodes.Document manifest = Jsoup.parse(manFile, "UTF-8");
        String manText = manifest.text();
        //tokenize manifest and create hashmap
        HashMap<String, Integer> docMap = new HashMap<>();
        StringTokenizer st = new StringTokenizer(manText);
        while (st.hasMoreTokens()) 
        {
            String temp = st.nextToken();
            Integer dID = Integer.parseInt(temp);
            temp = st.nextToken();
            docMap.put(temp,dID);
        }
        System.out.println("Hashmap was made");
        System.out.println("map size: " + docMap.size());

        //index directory path
        Path a_path = FileSystems.getDefault().getPath(idx_location);
        
        // To store an index on disk:
        Directory a_directory = FSDirectory.open(a_path);
        
        // Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(a_directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();

        String[] fields = {"url", "title", "content", "rank"};
        Map<String, Float> boosts = new HashMap<>();
        
        //Weight importance of different document attributes
        boosts.put(fields[0], 1.0f); // url
        boosts.put(fields[1], 2.0f); // title
        boosts.put(fields[2], 1.0f); // content
        
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
             
        Query query = parser.parse(input);
        
        //Number of websites to return
        int topHitCount = 10;
        
        //Search Index for hits that match the query most
        ScoreDoc[] hits = indexSearcher.search(query,topHitCount).scoreDocs;

        // Iterate through the results: 
        //   -Assuming they are sorted by rank high to low already
        //   -Instead of outputting, should put them into JSON file
        //   -give back JSON to web server
        List<Result> results = new ArrayList<Result>(); // returning this for now - Jake
        for (int rank = 0; rank < hits.length; ++rank) 
        {
            Document hitDoc = indexSearcher.doc(hits[rank].doc);
            
            // get document number from hashmap
            int pos = docMap.get(hitDoc.get("url"));

            //grab document associated with position
            String docIFile = html_location + "/" + pos + ".html";	    
            File docInput = new File(docIFile);
            org.jsoup.nodes.Document document = Jsoup.parse(docInput, "UTF-8");

            // make snippet 
            String snip1 = "No Snippet :(";
            List<String> snipLst = snipper.snip(input,document);

            if(snipLst.size() > 0)
            {
                snip1 = snipLst.get(0);
            }

            results.add( new Result( rank+1, hitDoc.get("title"), hitDoc.get("url"), snip1, 0 ) );
            // System.out.println(indexSearcher.explain(query, hits[rank].doc));
        }
        indexReader.close();
        a_directory.close();
        return results;
    }
    //-------------------------------------------------------------------------
}
