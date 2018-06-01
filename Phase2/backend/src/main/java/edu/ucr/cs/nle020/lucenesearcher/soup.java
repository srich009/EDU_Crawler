package edu.ucr.cs.nle020.lucenesearcher;

import java.io.File;
import java.util.*;
//import java.io.*;

//jsoup stuff
import org.jsoup.Jsoup;
// import org.jsoup.nodes.Document;
// import org.jsoup.nodes.Element;
// import org.jsoup.select.Elements;
import org.springframework.core.annotation.Order;

// util
import java.util.Arrays;

public class soup
{
	public static List<Page> processFiles(String folder)
	{
		List<org.jsoup.nodes.Document> docs = new ArrayList<org.jsoup.nodes.Document>();
		List<Page> pages = new ArrayList<Page>();
		
		try
		{
			File directory = new File(folder);
			File[] contents = directory.listFiles();

			//files not stroed in consecutive numeric Order
			// sort files
			Arrays.sort(contents);
			
			/*for(int i = 0; i < contents.length; i++)
			{
				System.out.println(contents[i]);
			}*/

			// parse raw html to jsoup documents
			for (File f : contents) 
			{ 
			    String path = f.getAbsolutePath();		    
			    File input = new File(path);
			    
			    try
			    {
			    	org.jsoup.nodes.Document doc = Jsoup.parse(input, "UTF-8");
			    	docs.add(doc);    	
			    }
				catch (Exception | Error e)
			    {
					System.out.println("Exception | Error");
					System.out.println(e.getMessage());
				} 
			}
			
			// grab the manifest
			org.jsoup.nodes.Document manifest = docs.get(0);
			docs.remove(0);
			String man = manifest.text();
			String[] chop = man.split("\\s+");
			List<String> urls = new ArrayList<String>();
			
			// remove index numbers, keep only urls
			// urls are only in the odd positions of array
			for(int i = 1; i < chop.length; i+=2)
			{
				urls.add(chop[i]);
			}

			/*for( int i = 1; i < urls.size();  i++) // test
			{
				System.out.println(urls.get(i));
			}*/
			//System.out.printf("docs: %d\n",docs.size());
			//System.out.printf("urls: %d\n",urls.size());

			// check size must be the same
			if(docs.size() != urls.size())
			{
				throw new Exception("docs.length() != urls.length()");
			}
			
			for( int i = 0; i < docs.size(); i++)
			{
				pages.add( new Page( urls.get(i), docs.get(i).title(), docs.get(i).text(), 0.0f /*rank*/ ) );
			}
		}
		catch (Exception | Error e)
	    {
			System.out.println("Exception | Error");
			System.out.println(e.getMessage());
		} 
		
		return pages; 
		
	}// end of processFiles
}
