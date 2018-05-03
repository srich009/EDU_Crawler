// thread class call the jsoup parts to download the html

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

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
	
	// not static on purpose
	public void run()
	{
		do 
		{	
			curr_uh = jt_crawler.isDone();
			while ( (!curr_uh.isDone) && (curr_uh.url_name == "") ) {
				try {
					Thread.yield();
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
					System.out.println(curr_uh.url_name);
					
					// save file to local storage
					String body = doc.toString();
					String fileName = jt_crawler.nextName(curr_uh.url_name);
					String outDir = jt_crawler.getOuputDir();
					File file = new File(outDir + "/pages/" + fileName);
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(body);
					bw.close();
					
					// get other hyperlinks
					Elements links = doc.select("a[href]");
					System.out.println("Thread: " + t_name + " -- acquired " + links.size() + " hyperlinks");
					for (Element e : links) 
					{
						String abs_ref = e.attr("abs:href");
						//first check if it is an edu page
						if (abs_ref.contains(".edu")) {
							//check if has been seen before
							if (!jt_crawler.beenSeen(abs_ref)) {
								//now see if it's crawlable
								if (RobotExclusionUtil.robotsShouldFollow(abs_ref)) {
									jt_crawler.push(abs_ref, true, curr_uh.num_hops);
								}
								else {
									jt_crawler.push(abs_ref, false, curr_uh.num_hops);
								}
							}
						}
					}
				
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Url: " + curr_uh.url_name + " could not be reached by Jsoup.");
				} catch (Error e) {
					System.out.println("Error Catch");
					System.out.println(e.getMessage());
				}
			}
			
			
		} while(!curr_uh.isDone);
	
		System.out.println("Thread: " + t_name + " has exited while loop and will die.");
		jt_crawler.killThread();
	}
	
}
