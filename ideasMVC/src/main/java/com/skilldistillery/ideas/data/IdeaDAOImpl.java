package com.skilldistillery.ideas.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.skilldistillery.ideas.data.comparators.SortIdeaByContreversy;
import com.skilldistillery.ideas.data.comparators.SortIdeaByDateNewFirst;
import com.skilldistillery.ideas.data.comparators.SortIdeaByDateOldFirst;
import com.skilldistillery.ideas.data.comparators.SortIdeaByDislikes;
import com.skilldistillery.ideas.data.comparators.SortIdeaByLikes;
import com.skilldistillery.ideas.data.comparators.SortIdeaByUsername;
import com.skilldistillery.ideasjpa.entities.Comment;
import com.skilldistillery.ideasjpa.entities.Idea;
import com.skilldistillery.ideasjpa.entities.IdeaLike;
import com.skilldistillery.ideasjpa.entities.IdeaLikeKey;
import com.skilldistillery.ideasjpa.entities.Profile;

@Transactional
@Component
public class IdeaDAOImpl implements IdeaDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean destroy(Idea idea) {
		Idea ideaToDelete = em.find(Idea.class, idea.getId());
		if (ideaToDelete == null) {
			em.getTransaction().commit();
			return false;
		}
		System.out.println(ideaToDelete);
		em.remove(ideaToDelete);
		return true;
	}

	@Override
	public Idea update(Idea idea) {
		Idea managed = em.find(Idea.class, idea.getId());
		managed.setContent(idea.getContent());
		managed.setName(idea.getName());
		return managed;

	}

	@Override
	public Idea makeActive(int id) {
		Idea managed = em.find(Idea.class, id);
		managed.setActive(true);
		return managed;

	}

	@Override
	public Idea makeInactive(int id) {
		Idea managed = em.find(Idea.class, id);
		managed.setActive(false);
		return managed;
	}

	@Override
	public Idea create(Idea idea, Profile profile) {
		// write the customer to the database
		List<Comment> comments = new ArrayList<>();
		idea.setComments(comments);
		idea.setProfile(profile);
		em.persist(idea);
		// update the "local" Customer object
		em.flush();
		// commit the changes (actually perform the operation)
		return idea;
	}

	@Override
	public List<Idea> showIdeasByProfile(int profileId) {
		String sql = "select i from Idea i where profile.id = :profileId AND i.active = 1";
		List<Idea> ideasByProfile = em.createQuery(sql, Idea.class).setParameter("profileId", profileId)
				.getResultList();
		return ideasByProfile;
	}

	@Override
	public List<Idea> showAllIdeas() {
		String sql = "select i from Idea i WHERE i.active = 1";
		List<Idea> ideas = em.createQuery(sql, Idea.class).getResultList();
		return ideas;
	}

	@Override
	public IdeaLike createLike(Idea idea, Profile profile, Boolean vote) throws MySQLIntegrityConstraintViolationException {
		IdeaLikeKey ilk = new IdeaLikeKey();
		ilk.setIdea(idea);
		ilk.setProfile(profile);

		IdeaLike il = new IdeaLike();
		il.setId(ilk);
		il.setVote(vote);
		em.persist(il);
		em.flush();
		return il;
	}

	@Override
	public IdeaLike updateLike(Idea idea, Profile profile, Boolean vote) {
		IdeaLikeKey ilk = new IdeaLikeKey();
		ilk.setIdea(idea);
		ilk.setProfile(profile);
		IdeaLike managed = em.find(IdeaLike.class, ilk);
		managed.setVote(vote);
		em.flush();
		return managed;
	}

	@Override
	public Idea showIdea(int id) {
		return em.find(Idea.class, id);
	}

	@Override
	public List<Idea> sortIdeasByDateNewFirst(List<Idea> ideas) {
		SortIdeaByDateNewFirst newFirst = new SortIdeaByDateNewFirst();
		ideas.sort(newFirst);
		return ideas;

	}

	@Override
	public List<Idea> sortIdeasByDateOldFirst(List<Idea> ideas) {
		SortIdeaByDateOldFirst oldFirst = new SortIdeaByDateOldFirst();
		ideas.sort(oldFirst);
		return ideas;

	}

	@Override
	public int getLikes(Idea idea) {
		int ideaId = idea.getId();
		int likeCount;
		String sql = "select il from IdeaLike il where il.id.idea.id = :ideaId and il.vote = true";
		List<IdeaLike> likes = em.createQuery(sql, IdeaLike.class).setParameter("ideaId", ideaId).getResultList();
		if (!likes.isEmpty()) {
			likeCount = likes.size();
		} else {
			likeCount = 0;
		}
		return likeCount;
	}

	@Override
	public int getDislikes(Idea idea) {
		int ideaId = idea.getId();
		int likeCount;
		String sql = "select il from IdeaLike il where il.id.idea.id = :ideaId and il.vote = false";
		List<IdeaLike> dislikes = em.createQuery(sql, IdeaLike.class).setParameter("ideaId", ideaId).getResultList();
		if (!dislikes.isEmpty()) {
			likeCount = dislikes.size();
		} else {
			likeCount = 0;
		}
		return likeCount;
	}

	@Override
	public List<Idea> sortByLikes(List<Idea> ideas) {
		SortIdeaByLikes mostLikes = new SortIdeaByLikes(this);
		ideas.sort(mostLikes);
		return ideas;
	}

	@Override
	public List<Idea> sortByDisikes(List<Idea> ideas) {
		SortIdeaByDislikes mostDislikes = new SortIdeaByDislikes(this);
		ideas.sort(mostDislikes);
		return ideas;
	}

	@Override
	public List<Idea> sortByUsername(List<Idea> ideas) {
		SortIdeaByUsername byUsername = new SortIdeaByUsername();
		ideas.sort(byUsername);
		return ideas;
	}

	@Override
	public List<Idea> sortByContreversy(List<Idea> ideas) {
		SortIdeaByContreversy byContreversy = new SortIdeaByContreversy(this);
		ideas.sort(byContreversy);
		return ideas;
	}

	@Override
	public Idea assignLikes(Idea idea) {
		idea.setLikes(getLikes(idea));
		idea.setDislikes(getDislikes(idea));
		return idea;
	}

	@Override
	public List<Idea> searchIdea(String ideaKeyword) {
		String sql = "select i from Idea i where (i.name like CONCAT('%',:ideaKeyword,'%') or i.content like CONCAT('%',:ideaKeyword,'%')) and i.active = 1";
		List <Idea> foundIdeas = em.createQuery(sql, Idea.class).setParameter("ideaKeyword", ideaKeyword).getResultList();
		
		return foundIdeas;
	}

}
