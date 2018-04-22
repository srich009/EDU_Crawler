// mockup of the crawler part...

// imports

import java.io.*;
import java.util.*;

public class Crawler
{
	public LinkedList<url_hop>  urls;
	public Integer pages;
	public Integer hops;
	public String  output;
	public Integer count;
	public Set<String> seen;
	
	public Crawler(LinkedList<url_hop> u_lst, Integer num_pag, Integer num_hop, String out)
	{ 
		urls   = u_lst;
		pages  = num_pag;
		hops   = num_hop;
		output = out;
		count  = 0;
		seen   = new HashSet<String>();
	}

	/*
	 * function spawns the threads
	 * is not a static function so can use 'this' as argument to the jthread
	 */
	public void crawl()
	{
		System.out.println("-> inside Crawler");
		
		// do jsoup stuff with threads
		Jthread thread1 = new Jthread(this, "thread1");
		Jthread thread2 = new Jthread(this, "thread2");
		Jthread thread3 = new Jthread(this, "thread3");
		Jthread thread4 = new Jthread(this, "thread4");
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();

	}
	//---------------------------------------
	
	public void push(List<String> str_lst, int curr_hop)
	{
		synchronized(this)
		{
			for(String s : str_lst)
			{
				if(!seen.contains(s))
				{
					seen.add(s);     // add to hash of seen
					url_hop uh = new url_hop();
					uh.url_name = s;
					uh.num_hops = curr_hop;
					urls.addLast(uh); // add to list to crawl
				}
			}
		}
	}
	//---------------------------------------

	public url_hop pull()
    {
		synchronized(this)
		{
			if(!urls.isEmpty())
			{
				return this.urls.remove(0);
			}
			else
			{
				url_hop uh = new url_hop();
				uh.url_name = "";
				return uh; // ?
			}
		}
    }
	//---------------------------------------
	
	

}
