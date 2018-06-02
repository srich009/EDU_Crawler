package part2;

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
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class Lucene_Indexer 
{	
	public static void main(String[] args) 
	{
		try
		{
			String html_location  = "C:\\Users\\duluoz\\Documents\\Eclipse\\part2\\src\\html_files";  // local html
			String index_location = "C:\\Users\\duluoz\\Documents\\Eclipse\\part2\\src\\index_files"; // local index
			
			//index directory path
			Path the_path = FileSystems.getDefault().getPath(index_location);
			
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
	        System.out.println("a");
		}
		catch (Exception | Error e)
	    {
			System.out.println("Exception | Error");
			System.out.println(e.getMessage());
		} 
	}
}

