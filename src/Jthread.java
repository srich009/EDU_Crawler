// thread class call the jsoup parts to download the html

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;
import java.io.IOException;
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
					// get other hyperlinks
					System.out.println("Here1");
					String body = doc.toString();
					System.out.println("Here2");
					String fileName = jt_crawler.nextName(curr_uh.url_name);
					System.out.println("Here3");
					String outDir = jt_crawler.getOuputDir();
					System.out.println("Here4");
					File dir = new File(outDir + "/pages");
					System.out.println("Here5");
					if(!dir.exists()){
						dir.mkdir();
					}
					System.out.println("Here5");
					File file = new File(outDir + "/pages/" + fileName);
					System.out.println("Here6");
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					System.out.println("Here7");
					BufferedWriter bw = new BufferedWriter(fw);
					System.out.println("Here8");
					bw.write(body);
					System.out.println("Here9");
					bw.close();
					System.out.println("Here10");
			        
					/*try{
						
					}
					catch (IOException e){
						System.out.println("Error writing to HTML file");
					}*/
					
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
					//System.out.println("Url: " + curr_uh.url_name + " could not be reached by Jsoup.");
				} catch (Error e) {
					System.out.println(e.getMessage());
				}
			}
			
			
		} while(!curr_uh.isDone);
	
		System.out.println("Thread: " + t_name + " has exited while loop and will die.");
		jt_crawler.killThread();
	}
	
}
