package edu.ucr.cs.nle020.lucenesearcher;

public class Page 
{
	String url;
    String title;
    String content;
    Float  rank;

    Page(String url, String title, String content, Float rank) 
    {
    	this.url     = url;
        this.title   = title;
        this.content = content;
        this.rank    = rank;
    }
    
    void pagePrint()
    {			
    	System.out.println("URL");
        System.out.println(this.url);
        System.out.println("RANK");
		System.out.println(this.rank);
    	System.out.println("TITLE");
		System.out.println(this.title);
		System.out.println("CONTENT");
		System.out.println(this.content);
    }
}

/*
 * using jsoup to get title && content for the pages
 * public String title​() = Get the string contents of the document's title element.
 * public String text​()  = Get the combined text of all the matched elements.
 * url comes from parsing the manifest
 */