package edu.ucr.cs.nle020.lucenesearcher;

public class Result 
{
    public int docId;
    public int lucRank;
    public float pageRank;
    public String title;
    public String url;
    public String snippet;
    public float score;

    public Result(){}

    public Result(int docId, int lucr, float pr, String title, String url, float score) 
    {
        this.docId = docId;
        this.lucRank = lucr;
        this.pageRank = pr;
        this.title = title;
        this.url = url;
        this.score = score;
    }
	/*
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
	*/

    @Override
    public String toString() {
        return String.format("Article[id=%d, title=%s, url=%s, snippet=%s, score=%f]", id, title, url, snippet, score);
    }
}