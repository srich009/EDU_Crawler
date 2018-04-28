// mockup of the crawler part...

// imports

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

public class Crawler
{
	private LinkedList<url_hop> urls;
	private Integer max_pages;
	private Integer max_hops;
	private String  output;
	private Integer count;
	private Integer running_threads;
	private Set<String> seen;
	private ReentrantLock fifo_lock = new ReentrantLock();
	private ReentrantLock hash_lock = new ReentrantLock();
	//private Integer nthreads = 100;
	
	public Crawler(LinkedList<url_hop> u_lst, Integer num_pag, Integer num_hop, String out)
	{ 
		urls   = u_lst;
		max_pages  = num_pag;
		max_hops   = num_hop;
		output = out;
		count  = 0;
		running_threads = 100;
		seen   = new HashSet<String>();
	}

	/*
	 * function spawns the threads
	 * is not a static function so can use 'this' as argument to the jthread
	 */
	public void crawl()
	{
		System.out.println("-> inside Crawler");
		
		//ExecutorService executor = Executors.newFixedThreadPool(nthreads);
		List<Jthread> jthreads = new ArrayList<Jthread>();
	
		for (int i = 0; i < running_threads; i++) {
			String tname = "thread" + i;
			jthreads.add(new Jthread(this, tname));
		}
		
		for (Jthread temp_jt : jthreads) {
			temp_jt.start();
		}
		
		do {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Running Threads: " + running_threads);
		} while (running_threads > 0);
		
	}
	//---------------------------------------
	
	public url_hop isDone() 
	{
		url_hop ret_uh = new url_hop("", -1, false);
		fifo_lock.lock();
		try {
			if (count < max_pages) {
				if (count % 10 == 0) {
					System.out.println("Pages pulled: " + count);
				}
				
				// Still want thread to continue working
				if (!urls.isEmpty()) { //go ahead and return a real url
					ret_uh = this.urls.removeFirst();
					count++;
				}
				else {
					ret_uh.isDone = false;
				}
			}
			else {
				ret_uh.isDone = true;
			}
		} finally {
			fifo_lock.unlock();
		}
		return ret_uh;
	}
	
	//-------------------------------------------------
	
	public void push(String url, Boolean crawlable, int curr_hop)
	{
		hash_lock.lock();
		try {
			seen.add(url);
			if (curr_hop == max_hops) {
				return;
			}
			if (crawlable) {
				url_hop uh = new url_hop(url, curr_hop+1, false);
				urls.addLast(uh);
			}
			
		} finally {
			hash_lock.unlock();
		}
	}
	
	public Boolean beenSeen(String url)
	{
		Boolean b;
		hash_lock.lock();
		try {
			b = seen.contains(url);
		} finally {
			hash_lock.unlock();
		}
		return b;
	}
	
	public void killThread() 
	{
		synchronized(this) {
			running_threads--;
		}
	}
	//---------------------------------------

}
