// mockup of the crawler part...

// imports

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class Crawler
{
	public LinkedList<url_hop> urls;
	public Integer max_pages;
	public Integer max_hops;
	public String  output;
	public Integer count;
	public Set<String> seen;
	ReentrantLock lock = new ReentrantLock();
	
	public Crawler(LinkedList<url_hop> u_lst, Integer num_pag, Integer num_hop, String out)
	{ 
		urls   = u_lst;
		max_pages  = num_pag;
		max_hops   = num_hop;
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
	
	public url_hop isDone() 
	{
		url_hop ret_uh = new url_hop("", -1, false);
		lock.lock();
		try {
			if (count < max_pages) {
				// Still want thread to continue working
				if (!urls.isEmpty()) { //go ahead and return a real url
					ret_uh = this.urls.removeFirst();
				}
				else {
					ret_uh.isDone = false;
				}
			}
			else {
				ret_uh.isDone = true;
			}
		} finally {
			lock.unlock();
		}
		return ret_uh;
	}
	
	//-------------------------------------------------
	
	public void push(LinkedList<String> str_lst, int curr_hop)
	{
		synchronized(this)
		{
			if (curr_hop == max_hops) 
			{
				return;
			}
			for(String s : str_lst)
			{
				if(!seen.contains(s))
				{
					seen.add(s);     // add to hash of seen
					url_hop uh = new url_hop(s, curr_hop + 1, false);
					urls.addLast(uh); // add to list to crawl
				}
			}
		}
	}
	//---------------------------------------

}
