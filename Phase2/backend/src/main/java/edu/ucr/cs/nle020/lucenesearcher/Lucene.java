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
import java.util.Collections;
import java.util.Comparator;

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

class ResultComp implements Comparator<Result> {

	@Override
	public int compare(Result r1, Result r2) {
		if (r1.pageRank < r2.pageRank) {
			return 1;
		}
		else if (r1.pageRank == r2.pageRank) {
			if (r1.lucRank < r2.lucRank) {
				return 1;
			} else {
				return -1;
			}
		}
		else {
			return -1;
		}
	}

}

public class Lucene
{
    
    public static List<Result> search(String input, String withPR, Integer count) 
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

        String[] fields = {"url", "title", "content", "pageRank"};
        Map<String, Float> boosts = new HashMap<>();
        
        //Weight importance of different document attributes
        boosts.put(fields[0], 1.0f); // url
        boosts.put(fields[1], 2.0f); // title
        boosts.put(fields[2], 1.0f); // content
        boosts.put(fields[3], 0.0f); //value)
        
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
             
        Query query = parser.parse(input);
        
        //Number of websites to return
        int topHitCount = count;
        
        //Search Index for hits that match the query most
        ScoreDoc[] hits = indexSearcher.search(query,topHitCount).scoreDocs;

        // Iterate through the results: 
        //   -Assuming they are sorted by rank high to low already
        List<Result> results = new ArrayList<Result>();
        for (int lucRank = 0; lucRank < hits.length; ++lucRank) 
        {
            Document hitDoc = indexSearcher.doc(hits[lucRank].doc);
            
            // get document number from hashmap
            int docID = docMap.get(hitDoc.get("url"));

            results.add( new Result( docID, lucRank+1, Float.parseFloat(hitDoc.get("pageRank")), 
            						hitDoc.get("title"), hitDoc.get("url"), 
            						Float.parseFloat(hitDoc.get("score")) ) );
            
        }


		if (withPR.equals("true")) {
			Collections.sort(results, new ResultComp());
		}

		for (int rank = 0; rank < results.size(); ++rank) {
	            //grab document associated with position
	            String docIFile = html_location + "/" + results.get(rank).docId + ".html";	    
	            File docInput = new File(docIFile);
	            org.jsoup.nodes.Document document = Jsoup.parse(docInput, "UTF-8");

	            // make snippet 
	            String snip1 = "No Snippet :(";
	            List<String> snipLst = snipper.snip(input, document);

	            if(snipLst.size() > 0)
	            {
	                snip1 = snipLst.get(0);
	            }
	            results.get(rank).snippet = snip1;
		}

	    indexReader.close();
	    a_directory.close();
	    return results;
    }
    //-------------------------------------------------------------------------
}
