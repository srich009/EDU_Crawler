// thread class call the jsoup parts to download the html

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	// add hyperlinks to list
	
	// not static on purpose
	public void run()
	{
		while(!jt_crawler.urls.isEmpty() && jt_crawler.count < jt_crawler.pages)
		{
			System.out.printf("current url: %s\n", jt_crawler.urls.get(0));
			
			String url = jt_crawler.urls.remove(0); // pop front of list
			
			// etc ...
			
		}
	}
	
}
