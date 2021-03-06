package edu.ucr.cs.nle020.lucenesearcher;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.*;


// ***** URL: http://localhost:8080/search?query=UCR


/**
 * Lucene simple demo. Based on:
 * https://lucene.apache.org/core/7_3_0/core/overview-summary.html#overview.description
 */
@RestController
@CrossOrigin("*")
public class LuceneController 
{
    @GetMapping("/search")
    public List<Result> searchLucene(@RequestParam(name="query",required=false, defaultValue="ucr") String input,
    @RequestParam(name="withPR",required=false, defaultValue="") String withPR,
    @RequestParam(name="count",required=false, defaultValue="") Integer count) 
        throws IOException, org.apache.lucene.queryparser.classic.ParseException 
    {
        return Lucene.search(input, withPR, count);
    }
}
