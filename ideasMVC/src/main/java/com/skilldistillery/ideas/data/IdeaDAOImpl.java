package com.skilldistillery.ideas.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	public Idea create(Idea idea) {
		// write the customer to the database
		List<Comment> comments = new ArrayList<>();
		idea.setComments(comments);
		em.persist(idea);
		// update the "local" Customer object
		em.flush();
		// commit the changes (actually perform the operation)
		return idea;
	}
	@Override
	public List<Idea> showIdeasByProfile(int profileId){
		String sql = "select i from Idea i where profile.id = :profileId";
		List<Idea> ideasByProfile = em.createQuery(sql, Idea.class).setParameter("profileId", profileId).getResultList();
		return ideasByProfile;
	}
	@Override
	public List<Idea> showAllIdeas() {
		String sql = "select i from Idea i";
		List<Idea> ideas = em.createQuery(sql, Idea.class).getResultList();
		return ideas;
	}
	@Override
	public IdeaLike createLike(Idea idea, Profile profile, Boolean vote) {
		em.getTransaction().begin();
		IdeaLikeKey ilk = new IdeaLikeKey();
		ilk.setIdea(idea);
		ilk.setProfile(profile);
		
		IdeaLike il = new IdeaLike();
		il.setId(ilk);
		il.setVote(vote);
		em.persist(il);
		em.flush();
		em.getTransaction().commit();
		return il;
	}

	@Override
	public IdeaLike updateLike(Idea idea, Profile profile, Boolean vote) {
		em.getTransaction().begin();
		IdeaLikeKey ilk = new IdeaLikeKey();
		ilk.setIdea(idea);
		ilk.setProfile(profile);
		IdeaLike managed = em.find(IdeaLike.class, ilk);
		managed.setVote(vote);
		em.flush();
		em.getTransaction().commit();
		return managed;
	}
	@Override
	public Idea showIdea(int id ) {
		return em.find(Idea.class, id);
	}

}