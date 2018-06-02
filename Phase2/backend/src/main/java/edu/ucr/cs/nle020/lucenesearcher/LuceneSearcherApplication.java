package edu.ucr.cs.nle020.lucenesearcher;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LuceneSearcherApplication 
{
	public static void main(String[] args) 
		throws IOException, ParseException
	{
		// test
		snipper.test();
		// test
		
		// Lucene.index(); // THIS SHOULD BE COMMENTED OUT ONCE THE FINAL STABLE INDEX IS MADE

		SpringApplication.run(LuceneSearcherApplication.class, args);
	}
}
