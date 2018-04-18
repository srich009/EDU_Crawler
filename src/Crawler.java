// mockup of the crawler part..

// imports
import java.io.*;
import java.util.*;

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler
{
	/*
	 * this function should 
	 */
	public static void crawl(List<String> urls, Integer pages, Integer hops, String output)
	{
		System.out.println("-> inside Crawler");
		
		Integer pages_visited = 0;

		while(!urls.isEmpty() && pages_visited < pages)
		{
			System.out.printf("current url: %s\n", urls.get(0));
			
			String url = urls.remove(0); // pop front of list
			
			// do jsoup stuff
			
			// download/parse html
			
			// add hyperlinks to list
			
			// etc ...
			
		}

	}
	//---------------------------------------
}
