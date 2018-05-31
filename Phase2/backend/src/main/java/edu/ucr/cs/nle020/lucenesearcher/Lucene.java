package edu.ucr.cs.nle020.lucenesearcher;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lucene{
    static class Page {
        String title;
        String content;

        Page(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
    static Directory directory1;
    static Analyzer analyzer1;
    static IndexReader indexReader1;
    //directory1.close();

    public static void index() throws IOException, ParseException {
        Analyzer analyzer = new StandardAnalyzer();
        analyzer1 = analyzer;

        // Store the index in memory:
        Directory directory = new RAMDirectory();
        directory1 = directory;

        // To store an index on disk, use this instead:
        //Directory directory = FSDirectory.open("/tmp/test");
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        List<Page> pages = Arrays.asList(
                new Page("UCR article", "Search engine is considered the most successful application of IR."),
                new Page("IR class", "UCR, just the second IR discussion, eight more discussions to come!")
        );

        for (Page page: pages) {
            Document doc = new Document();
            doc.add(new TextField("title", page.title, Field.Store.YES));
            doc.add(new TextField("content", page.content, Field.Store.YES));
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
    }

    public static List<Result> search(String input) throws IOException, ParseException {
        // Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(directory1);
        indexReader1 = indexReader;
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        String[] fields = {"title", "content"};
        Map<String, Float> boosts = new HashMap<>();
        boosts.put(fields[0], 1.0f);
        boosts.put(fields[1], 0.5f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer1, boosts);
        Query query = parser.parse(input);
        // Query query = parser.parse("UCR discussion");
        // QueryParser parser = new QueryParser("content", analyzer);
        // Query query = parser.parse("(title:ucr)^1.0 (content:ucr)^0.5");
        //answer = query.toString() + " -- \n";
        int topHitCount = 10;
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;
            
        List<Result> results = new ArrayList<Result>();
        // Iterate through the results:
        for (int rank = 0; rank < hits.length; ++rank) {
            Document hitDoc = indexSearcher.doc(hits[rank].doc);
            // answer = answer + (rank + 1) + " (score:" + hits[rank].score + ") --> " +
            //                    hitDoc.get("title") + " - " + hitDoc.get("content") + "\n";
            results.add(new Result(rank+1, hitDoc.get("title"), hitDoc.get("content")));
            // System.out.println(indexSearcher.explain(query, hits[rank].doc));
        }
        indexReader1.close();
        return results;
    }
}