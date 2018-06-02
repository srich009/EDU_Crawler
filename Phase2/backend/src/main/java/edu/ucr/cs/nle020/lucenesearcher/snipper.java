package edu.ucr.cs.nle020.lucenesearcher;

// imports
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class snipper
{
    public static class Struct
    {
        List<String> strlst = new ArrayList<String>(); // list of each string
        List<String> keylst = new ArrayList<String>(); // list of each string
    }

    public static List<String> snip(List<String> keys)
    {
        String html_location = "./html"; // local html
        List<Page>   pages = soup.processFiles(html_location);
        List<String> snips = new ArrayList<String>(); // final list of snippets to return
        List<String> L1 = new ArrayList<String>();
        List<Struct> L2 = new ArrayList<Struct>();
        
        // process docs into pages
        for(Page p : pages)
        {
            L1.add(p.content);
            break; // only get 1 doc for now
        } 
        
        // extract each words
        for(String s1 : L1)
        {
            Struct x = new Struct();
            String[] arr = s1.split(" ");     
            for(String s2 : arr)
            {
                s2 = s2.trim();
                x.strlst.add(s2);
            }
            L2.add(x);
        }

        // look for key words
        // if found, then grab 3 behind && 3 ahead 
        for(Struct y : L2)
        {
            int limit = y.strlst.size();
            for(int i = 0; i < limit; i++)
            {
                String s3 = y.strlst.get(i);
                if( isKeyWord(s3,keys) )
                {
                    String s4 = "";
                    String tmp1 = "";
                    String tmp2 = "";
                    if(i > 0)
                    {
                        if(i-3 >= 0)
                            tmp1 = tmp1 + " " + y.strlst.get(i-3);
                        if(i-2 >= 0)
                            tmp1 = tmp1 + " " + y.strlst.get(i-2);
                        if(i-1 >= 0)
                            tmp1 = tmp1 + " " + y.strlst.get(i-1);
                    }
                    if(i < y.strlst.size())
                    {
                        if(i+1 < limit)
                            tmp2 = tmp2 + y.strlst.get(i+1);
                        if(i+2 < limit)
                            tmp2 = tmp2 + " " + y.strlst.get(i+2);
                        if(i+3 < limit)
                            tmp2 = tmp2 + " " + y.strlst.get(i+3);
                    }
                    s4 = tmp1 + " " + s3  + " " + tmp2;
                    y.keylst.add(s4);
                }
            }            
        }

        for(Struct z : L2)
        {
            for(String s : z.keylst)
            {
                System.out.println(s);
            }
        }

        return snips;      
    }

    public static boolean isKeyWord(String str,List<String> lst)
    {
        for(String s : lst)
        {
            if(s.equals(str))
            {
                // System.out.println("key: "+"--"+s+"--");
                // System.out.println("qur: "+"--"+str+"--");
                // System.out.println("true\n");
                return true;
            }
        }
        return false;
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
        List<String> key_words = Arrays.asList("professors");
        List<String> snippets = snip(key_words);
        printer(snippets);
    }
}