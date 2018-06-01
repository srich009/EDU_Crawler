package edu.ucr.cs.nle020.lucenesearcher;

public class Result 
{
    public int id;
    public String title;
    public String url;
    public String snippet;
    public double score;

    public Result(){}

    public Result(int id, String title, String url, String snippet, double score) 
    {
        this.id = id;
        this.title = title;
        this.url = url;
        this.snippet = snippet;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("Article[id=%d, title=%s, url=%s, snippet=%s, score=%f]", id, title, url, snippet, score);
    }
}