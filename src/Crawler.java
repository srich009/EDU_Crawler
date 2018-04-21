// mockup of the crawler part...

// imports

import java.io.*;
import java.util.*;

public class Crawler
{
	public Integer count = 0;
	public Set<String> seen = new HashSet<String>();

	/*
	 * function spawns the threads
	 * is not a static function so can use 'this' as argument to the jthread
	 */
	public void crawl(List<String> urls, Integer pages, Integer hops, String output)
	{
		System.out.println("-> inside Crawler");
		
		Integer pages_visited = 0;

		while(!urls.isEmpty() && pages_visited < pages)
		{
			System.out.printf("current url: %s\n", urls.get(0));
			
			String url = urls.remove(0); // pop front of list
			
			// do jsoup stuff with threads
			Jthread thread2 = new Jthread(this);
			
			// etc ...
			
		}

	}
	//---------------------------------------
}
