package edu.ucr.cs.nle020.lucenesearcher;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LuceneSearcherApplication {

	public static void main(String[] args) throws IOException, ParseException{
		Lucene.index();
		SpringApplication.run(LuceneSearcherApplication.class, args);
	}
}
