// thread class call the jsoup parts to download the html

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;

public class Jthread extends Thread
{
	private Thread t;
	private String t_name;
	
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
	// add hyperlinks to list (jake)
	
	// not static on purpose
	public void run() //needs IOException
	{
		try{
			while(!jt_crawler.urls.isEmpty() && jt_crawler.count < jt_crawler.pages)
			{			
				url_hop urlh = jt_crawler.pull(); // pop url string off front of list
				System.out.println("crawling -> url: " + urlh.url_name + " num hops:" + urlh.num_hops);
				
				//Jsoup getting webpage, extracting hyperlinks, and adding to list
				Document doc = Jsoup.connect(urlh.url_name).get();
					// save document somehow
		        Elements links = doc.select("a[href]");
		        
		        //List<String> newURLs = new List<String>;
		        for (Element link : links) {
		        	//filter for only .edu page FIXME
		        	String tempLink = link.attr("href"); // gets full URL
		        	System.out.println("Found a link: " + tempLink + "n");
		        	//newURLS.add(tempLink);		        	
		        }
		        //jt_crawler.push(newURLS, urlh.num_hops + 1); 
			}
		}
		catch(IOException ex){
			System.out.println("Error while parsing document \n");
		}
	
	}
	
}
