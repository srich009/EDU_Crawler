import { Component, OnInit } from '@angular/core';

import { ArticleService } from './article.service';
import { Article } from './article';
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent implements OnInit {
  articles: Article[];
  title = 'Nhat Search';
  pageRank = "false";

  constructor(private articleService: ArticleService) { }

  ngOnInit() {    
  }

  setPR(pr:string){
    this.pageRank = pr;
  }

  search(query: string, numResults: number) {
    this.articleService.getArticles(query, this.pageRank, numResults)
        .subscribe(articles => this.articles = articles);
  }

  onClick(art : Article){
    window.open(art.url);
  }

  myFunction(){
    let popup = document.getElementById("myPopup");
    popup.classList.toggle("show");
  }
}
