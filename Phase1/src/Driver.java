// main driver for crawler
// the driver should open && read in the seed file

// imports
import java.io.*;
import java.util.*;

public class Driver 
{
	/*
	 * main function
	 * -------------
	 * args = 
	 * <seed-file  : string> 
	 * <num-pages  : int> 
	 * <hops-away  : int> 
	 * <output-dir : string> ... ?? not sure if need to write to a file etc...
	 */
	public static void main(String[] args) 
	{
		System.out.println("-> inside Main");
		
		// get command line args
		String  seed   = args[0];
		Integer pages  = Integer.parseInt(args[1]);
		Integer hops   = Integer.parseInt(args[2]);
		String  output = args[3];
		System.out.println(output);
		
		// System.out.printf("seed = %s | pages = %d | hops = %d | output = %s\n",seed,pages,hops,output);
	
		// read initial urls from seed file
		List<String> urls = readFile(seed);
		LinkedList<url_hop> urls2 = new LinkedList<url_hop>();
		
		for (String s : urls) {
			urls2.addLast(new url_hop(s, 0, false));
		}
		
		// create crawler object
		Crawler web_crawler = new Crawler(urls2,pages,hops,output);
		
		// call the crawler
		web_crawler.crawl();	
		
		System.out.println("CRAWL RETURNED");
	}
	//=======================================
	//=======================================
	
	private static List<String> readFile(String filename)
	{
	    String line = null;
	    List<String> records = new ArrayList<String>();
	    try
	    {
	        BufferedReader reader = new BufferedReader(new FileReader(filename));
	        while((line = reader.readLine()) != null)
	        {
	            records.add(line);
	        }
	        reader.close();
	        return records;
	    }
	    catch (Exception e)
	    {
	        System.err.format("Exception occurred trying to read '%s'.", filename);
	        e.printStackTrace();
	        return null;
	    }
	}
	//---------------------------------------	
}
