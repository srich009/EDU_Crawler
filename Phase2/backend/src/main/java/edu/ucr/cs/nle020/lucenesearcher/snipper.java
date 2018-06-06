package edu.ucr.cs.nle020.lucenesearcher;

//jsoup
// import org.jsoup.Jsoup;

// lucene
// import org.apache.lucene.document.Document;

// imports
// import java.util.*;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
// import java.io.File;


public class snipper
{
    public static class Struct
    {
        List<String> strlst = new ArrayList<String>(); // list of each string
        List<String> keylst = new ArrayList<String>(); // list of each string
    }

    public static List<String> snip(String in, org.jsoup.nodes.Document doc)
    {
        List<String> keys = Arrays.asList(in.split(" ")); // break input into keywords

        // extract each word from doc body
        String s1 = doc.text();
        Struct x = new Struct();
        String[] arr = s1.split(" ");     
        for(String s2 : arr)
        {
            s2 = s2.trim();
            x.strlst.add(s2);
        }

        // look for key words
        // if found, then grab 3 behind && 3 ahead 
        int limit = x.strlst.size();
        for(int i = 0; i < limit; i++)
        {
            String s3 = x.strlst.get(i);
            if( isKeyWord(s3,keys) )
            {
                String s4 = "";
                String tmp1 = "";
                String tmp2 = "";
                if(i > 0)
                {
                    if(i-3 >= 0)
                        tmp1 = tmp1 + " " + x.strlst.get(i-3);
                    if(i-2 >= 0)
                        tmp1 = tmp1 + " " + x.strlst.get(i-2);
                    if(i-1 >= 0)
                        tmp1 = tmp1 + " " + x.strlst.get(i-1);
                }
                if(i < x.strlst.size())
                {
                    if(i+1 < limit)
                        tmp2 = tmp2 + x.strlst.get(i+1);
                    if(i+2 < limit)
                        tmp2 = tmp2 + " " + x.strlst.get(i+2);
                    if(i+3 < limit)
                        tmp2 = tmp2 + " " + x.strlst.get(i+3);
                }
                s4 = tmp1 + " " + s3  + " " + tmp2;
                x.keylst.add(s4);
            }
        }            

        for(String s : x.keylst)
        {
            System.out.println(s);
        }

        return x.keylst;      
    }

    public static boolean isKeyWord(String str,List<String> lst)
    {
        for(String s : lst)
        {
            if( (s.toUpperCase()).equals(str.toUpperCase()) )
            {
                return true;
            }
        }
        return false;
    }
}