

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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Lucene_Indexer {
	
	static class Page {
        String title;
        String content;

        Page(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
	
	public static void main(String[] args) {
		
		// Arguments passed into main function
		//index directory path
		String index_dir_path = args[1];
		
		
        // Store the index in memory:
        //Directory directory = new RAMDirectory();
        // To store an index on disk:
        Directory directory = FSDirectory.open(index_dir_path);
        //config details for writer
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //instantiate writer for creating index
        IndexWriter indexWriter = new IndexWriter(directory, config);

        //Dummy creater of our data page list
        List<Page> pages = Arrays.asList(
                new Page("UCR article", "Search engine is considered the most successful application of IR."),
                new Page("IR class", "UCR, just the second IR discussion, eight more discussions to come!")
        );
        
        //Need to parse our html files and created a real list of pages
        //-------------------------------
        
        //-------------------------------

        //for each page - grab necessary attributes
        for (Page page: pages) {
            Document doc = new Document();
            doc.add(new TextField("title", page.title, Field.Store.YES));
            doc.add(new TextField("content", page.content, Field.Store.YES));
            //more attributes to store about each feature
            
            //add document to index
            indexWriter.addDocument(doc);
        }
        
        indexWriter.close();
	}

}
