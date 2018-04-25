// thread class call the jsoup parts to download the html

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class Jthread extends Thread
{
	private Thread t;
	private String t_name;
	private url_hop curr_uh;
	
	public Crawler jt_crawler;
	public RobotExclusionUtil jt_robo;
	
	public Jthread(Crawler c, String s)
	{
		t_name = s;
		jt_crawler = c;
		jt_robo = new RobotExclusionUtil();
	}

	// not static on pupose
	public void start() 
	{
		if(t == null)
		{
			System.out.println("start thread: " + t_name);
			t = new Thread(this, t_name);
			t.start();
		}		
	}
	
	// download/parse html
	// add hyperlinks to list
	
	// not static on purpose
	public void run()
	{
		do 
		{	
			curr_uh = jt_crawler.isDone();
			while ( (!curr_uh.isDone) && (curr_uh.url_name == "") ) {
				try {
					sleep(10);
				} catch (Exception e) {
					System.out.println("Sleep was interrupted.");
				}
				curr_uh = jt_crawler.isDone();
			}
			
			if (!curr_uh.isDone)
			{
				try {
					// get document connection with jsoup
					Document doc = Jsoup.connect(curr_uh.url_name).get();
					// get other hyperlinks
					Elements links = doc.select("a[href]");
					LinkedList<String> hyperlinks = new LinkedList<String>();
					for (Element e : links) 
					{
						String abs_ref = e.attr("abs:href");
						System.out.println(abs_ref);
						hyperlinks.add(abs_ref);
					}
					
					jt_crawler.push(hyperlinks, curr_uh.num_hops);
				
				
				} catch (Exception e) {
					System.out.println("Url: " + curr_uh.url_name + " could not be reached by Jsoup.");
				}
				
				
				
			
			}
			
			
		} while(!curr_uh.isDone);
	}
	
}
