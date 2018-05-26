package edu.ucr.cs.nle020.lucenesearcher;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ArticleController {
    static List<Article> articles;
    static {
        articles = new ArrayList<>();
        articles.add(new Article(1, "First Article",
                "Class Overview, Overview of Information Retrieval and Search Engines"));
        articles.add(new Article(2, "Second Article",
                "Ranking: Vector space model, Probabilistic Model, Language model"));
        articles.add(new Article(3, "Third Article",
                "Web Search: Spam, topic-specific pagerank model"));
    }

    @GetMapping("/articles")
    public String searchArticles(
            @RequestParam(required=false, defaultValue="") String query) throws IOException, ParseException{
        System.out.println("made new searcher");
        LuceneSearcher goat = new LuceneSearcher();
        goat.index();
        return goat.search(query);
    }
}
