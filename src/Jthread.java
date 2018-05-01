// thread class call the jsoup parts to download the html

// jsoup stuff
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;

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
	// add hyperlinks to list (jake)
	
	// not static on purpose
	public void run() //needs IOException
	{
<<<<<<< HEAD
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
					String body = doc.toString();
					String currDir = System.getProperty("user.dir"); //change to jt_crawler.output. currently just pages
					System.out.println(currDir);
					String fileName = jt_crawler.nextName(curr_uh.url_name);
					File dir = new File(currDir + "/pages");
					if(!dir.exists()){
						dir.mkdir();
					}
					File file = new File(currDir + "/pages/" + fileName);
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
			        BufferedWriter bw = new BufferedWriter(fw);
			        bw.write(body);
			        bw.close();
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
=======
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
	
>>>>>>> e1952c6e0d8c44788eeb334e59d67d3a622a4c20
	}
	
}
