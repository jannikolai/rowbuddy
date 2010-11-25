package de.rowbuddy.business;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.LocalBean;
import javax.ejb.RemoveException;
import javax.ejb.Stateful;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.rowbuddy.business.dtos.BoatDTO;
import de.rowbuddy.entities.Boat;
import de.rowbuddy.entities.Member;
import de.rowbuddy.exceptions.NotLoggedInException;
import de.rowbuddy.exceptions.RowBuddyException;

/**
 * Session Bean implementation class RowBuddyFacade
 */
@Interceptors(SecuritySalamander.class)
@Stateful
@LocalBean
public class RowBuddyFacade {
	
	private Member member;

	@PersistenceContext
	EntityManager em;
	@EJB
	private BoatManagement boatManagement;

	/**
	 * Default constructor.
	 */
	public RowBuddyFacade() {

	}

	@ExcludeClassInterceptors
	public void login(Member member) throws NotLoggedInException {
		if (member.getEmail() == null || member.getPassword() == null)
			throw new NotLoggedInException("You didn't specify an E-Mail address and/or a password");

		Query q = em.createQuery("SELECT m FROM Member m WHERE m.email = :email");
		q.setParameter("email", member.getEmail());
		Member m = null;
		try {
			m = (Member) q.getSingleResult();
		} catch (NoResultException nre) {
			throw new NotLoggedInException("This member doesn't exist in our database");
		}
		if (m == null)
			throw new NotLoggedInException("This member doesn't exist in our database");

		if (!m.getPassword().equals(member.getPassword())) {
			throw new NotLoggedInException("The password you specified is not correct");
		} else {
			this.member = m;
		}
	}

	@ExcludeClassInterceptors
	public void logout() {
		member = null;
	}

	@ExcludeClassInterceptors
	public boolean isLoggedIn() {
		return (member != null);
	}
	
	public List<BoatDTO> getBoatOverview() {
		return boatManagement.getBoatOverview();
	}

	public Boat getBoat(Long id) throws FinderException {
		return boatManagement.getBoat(id);
	}
	
	@AllowedRoles(values = {"admin"})
	public Boat addBoat(Boat addBoat) throws CreateException {
		return boatManagement.addBoat(addBoat);
	}

	@AllowedRoles(values = {"admin"})
	public Boat updateBoat(Boat updateBoat) throws FinderException,
			RowBuddyException {
		return boatManagement.updateBoat(updateBoat);
	}
	
	@AllowedRoles(values = {"admin"})
	public void deleteBoat(Long id) throws RemoveException {
		boatManagement.deleteBoat(id);
	}

}