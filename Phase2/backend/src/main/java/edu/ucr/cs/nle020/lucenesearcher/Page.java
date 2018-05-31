package edu.ucr.cs.nle020.lucenesearcher;

public class Page 
{
	String   url;
    String   title;
    String   content;
    /*String[] meta;
    String[] header;*/

    Page(String url, String title, String content /*, String[] meta, String[] header*/) 
    {
    	this.url     = url;
        this.title   = title;
        this.content = content;
        /*this.meta    = meta;
        this.header  = header;*/
    }
    
    void pagePrint()
    {			
    	System.out.println("URL");
    	System.out.println(this.url);
    	System.out.println("TITLE");
		System.out.println(this.title);
		System.out.println("CONTENT");
		System.out.println(this.content);
		/*System.out.println("META");
		System.out.println(this.meta);
		System.out.println("HEADER");
		System.out.println(this.header);*/
    }
}


/*
 * using jsoup to get title && content for the pages
 * public String title​() = Get the string contents of the document's title element.
 * public String text​()  = Get the combined text of all the matched elements.
 * url comes from parsing the manifest
 */