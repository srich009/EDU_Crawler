// mockup of the crawler part...

// imports

import java.io.*;
import java.util.*;

public class Crawler
{
	public List<String>  urls;
	public Integer pages;
	public Integer hops;
	public String  output;
	public Integer count;
	public Set<String> seen;
	
	public Crawler(List<String> u_lst, Integer num_pag, Integer num_hop, String out)
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
		thread1.start();

	}
	//---------------------------------------
}
