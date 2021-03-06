// mockup of the crawler part...

// imports

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

public class Crawler
{
	private LinkedList<url_hop> urls; //frontier of uncrawled webpages
	private Integer max_pages;
	private Integer max_hops;
	private String  output;
	private Integer count;
	private Integer running_threads;
	private Set<String> seen; //keep track of pages already crawled
	private ReentrantLock fifo_lock = new ReentrantLock();
	private ReentrantLock hash_lock = new ReentrantLock();
	private Integer fileCounter;
	private LinkedList<String> fileList = new LinkedList<String>();
	
	public Crawler(LinkedList<url_hop> u_lst, Integer num_pag, Integer num_hop, String out)
	{ 
		urls   = u_lst;
		max_pages  = num_pag;
		max_hops   = num_hop;
		output = out;
		count  = 0;
		running_threads = 100;
		seen   = new HashSet<String>();
		fileCounter = 1;
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
		// Create Pages directory
		File dir = new File(output + "/pages");
		if(dir.exists()){
			String[]entries = dir.list();
			for(String s: entries){
			    File currentFile = new File(dir.getPath(),s);
			    currentFile.delete();
			}
			dir.mkdir();
		}
		else{
			dir.mkdir();
		}
		// Initiate threads for crawling
		for (int i = 0; i < running_threads; i++) {
			String tname = "thread" + i;
			jthreads.add(new Jthread(this, tname));
		}
		//start individual threads
		for (Jthread temp_jt : jthreads) {
			temp_jt.start();
		}
		
		try{			
			do {
				try {
					//update manifest with new pages
					File file = new File(output + "/pages/0-manifest.txt");
					FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			        BufferedWriter bw = new BufferedWriter(fw);
					for (int i = fileList.size(); i > 0; i--){
						String temp = fileList.removeFirst();
						bw.write(temp); 
					}
					bw.close();
					//let users know how many pages have been pulled so far
					System.out.println("Pages pulled: " + count);
					
					Thread.sleep(10000); //sleep for 10 secs while threads continue
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Running Threads: " + running_threads);
			} while (running_threads > 0);
		}
		catch (IOException e){
			e.printStackTrace();
			System.out.println("Error with manifest file.");
		}
		
	}
	
	public String getOuputDir()
	{
		return output;
	}
	
	public String nextName(String url) 
	{
		Integer ret_num;
		synchronized (this) {
			fileList.addLast(fileCounter + " " + url + "\n");
			ret_num = fileCounter++;
		}
		return (ret_num + ".html");
	}
	//---------------------------------------
	
	public url_hop isDone() 
	{
		url_hop ret_uh = new url_hop("", -1, false);
		fifo_lock.lock();
		try {
			if (count < max_pages) {
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
			//final check on hash since last time
			if (seen.contains(url)) {
				return;
			}
			
			seen.add(url);
			if (curr_hop == max_hops) {
				return;
			}
			if (crawlable) {
				url_hop uh = new url_hop(url, curr_hop+1, false);
				fifo_lock.lock();
				try {
					urls.addLast(uh);
				} finally {
					fifo_lock.unlock();
				}
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
