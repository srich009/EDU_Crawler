package edu.ucr.cs.nle020.lucenesearcher;

// imports
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class snipper
{
    public static List<String> snip(List<String> kws)
    {
        String html_location = "./html"; // local html
        List<Page>   pages = soup.processFiles(html_location);
        List<String> snips = new ArrayList<String>(); // final list of snippets to return
        List<String> L1 = new ArrayList<String>();
        List<String> L2 = new ArrayList<String>();
        for(Page p : pages)
        {
            L1.add(p.content);
        }  
        for(String s : L1)
        {
            L2.add(s);
        }
        return snips;      
    }

    public static void printer(List<String> l)
    {
        for(String s : l)
        {
            System.out.println(s);
        }
    }

    public static void test()
    {
        List<String> key_words = Arrays.asList("sup1", "sup2", "sup3");
        // add key words
        List<String> snippets = snip(key_words);
        printer(snippets);
    }
}