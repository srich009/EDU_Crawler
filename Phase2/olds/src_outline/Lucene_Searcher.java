import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
// in tutorial points
import org.apache.lucene.util.Version;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.queryParser.

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Lucene_Searcher {

	public static void main(String[] args) {
		// Opens currently existing directory
		// should catch errors and exceptions
		Directory directory = FSDirectory.open("/tmp/test");
		// Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();

        String[] fields = {"title", "content"};
        Map<String, Float> boosts = new HashMap<>();
        //Weight importance of different document attributes
        boosts.put(fields[0], 1.0f);
        boosts.put(fields[1], 0.5f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        Query query = parser.parse("UCR");
        // Query query = parser.parse("UCR discussion");
        // QueryParser parser = new QueryParser("content", analyzer);
        // Query query = parser.parse("(title:ucr)^1.0 (content:ucr)^0.5");
        
        //Don't know what this outputs, but it'll be more than query string
        System.out.println(query.toString());
        
        //Number of websites to return
        int topHitCount = 100;
        //Search Index for hits that match the query most
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;

        // Iterate through the results: 
        //   -Assuming they are sorted by rank high to low already
        //   -Instead of outputting, should put them into JSON file to 
        //      give back to web server
        for (int rank = 0; rank < hits.length; ++rank) {
            Document hitDoc = indexSearcher.doc(hits[rank].doc);
            System.out.println((rank + 1) + " (score:" + hits[rank].score + ") --> " +
                               hitDoc.get("title") + " - " + hitDoc.get("content"));
            // System.out.println(indexSearcher.explain(query, hits[rank].doc));
        }
        indexReader.close();
        directory.close();
	}

}