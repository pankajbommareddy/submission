package com.concretepage.dao;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.concretepage.entity.Article;
@Transactional
@Repository
public class ArticleDAO implements IArticleDAO {
	@PersistenceContext	
	private EntityManager entityManager;
	@Override
	public Article getArticleById(int articleId) {
		
		return entityManager.find(Article.class, articleId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Article> getAllArticles() {
		String hql = "FROM Article as atcl ORDER BY atcl.articleId DESC";
		List<Article> l= (List<Article>) entityManager.createQuery(hql).getResultList();
		System.out.println(l);
		return l;
		
	}	
	@Override
	public void createArticle(Article article) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		article.setDate(date);
		entityManager.persist(article);
	}
	@Override
	public void updateArticle(Article article) {
		Article artcl = getArticleById(article.getArticleId());
		artcl.setTitle(article.getTitle());
		artcl.setCategory(article.getCategory());
		artcl.setAuthorName(article.getAuthorName());
		artcl.setTags(article.getTags());
		entityManager.flush();
	}
	@Override
	public void deleteArticle(int articleId) {
		entityManager.remove(getArticleById(articleId));
	}
	@Override
	public boolean articleExists(String title, String category, String authorName, String tags) {
		String hql = "FROM Article as atcl WHERE atcl.title = ? and atcl.category = ? and atcl.authorName = ? and atcl.tags = ?";
		int count = entityManager.createQuery(hql).setParameter(1, title)
		              .setParameter(2, category).setParameter(3, authorName)
		              .setParameter(4, tags).getResultList().size();
		return count > 0 ? true : false;
	}
}
