// thread class call the jsoup parts to download the html

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Jthread 
{
	public Crawler jt_crawler;
	RobotExclusionUtil jt_robo;
	
	public Jthread(Crawler c)
	{
		jt_crawler = c;
		jt_robo = new RobotExclusionUtil();
	}

	// download/parse html
	
	// add hyperlinks to list
	
}
