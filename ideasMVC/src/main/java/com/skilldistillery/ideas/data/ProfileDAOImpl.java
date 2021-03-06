package com.skilldistillery.ideas.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.skilldistillery.ideasjpa.entities.Profile;
import com.skilldistillery.ideasjpa.entities.Profile;

@Transactional
@Component
public class ProfileDAOImpl implements ProfileDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean destroy(Profile profile) {
		Profile profileToDelete = em.find(Profile.class, profile.getId());
		if (profileToDelete == null) {
			return false;
		}
		System.out.println(profileToDelete);
		em.remove(profileToDelete);
		return true;
	}

	@Override
	public Profile update(Profile profile) {
		Profile managed = em.find(Profile.class, profile.getId());
		managed.setBio(profile.getBio());
		if (profile.getProfilePic().equals("") || profile.getProfilePic() == "" || profile.getProfilePic() == null) {
			managed.setProfilePic(
					"https://www.mybenshop.com/wp-content/uploads/2017/09/Rodin-the-Thinker-Sculpture-Medium-Figurine-Sandstone-Color-500x500.jpg");
		} else {
			managed.setProfilePic(profile.getProfilePic());
		}
		return managed;

	}

	@Override
	public Profile makeActive(int id) {
		Profile managed = em.find(Profile.class, id);
		managed.setActive(true);
		return managed;

	}

	@Override
	public Profile makeInactive(int id) {
		Profile managed = em.find(Profile.class, id);
		managed.setActive(false);
		return managed;
	}

	@Override
	public Profile showProfile(int id) {
		return em.find(Profile.class, id);
	}

}
