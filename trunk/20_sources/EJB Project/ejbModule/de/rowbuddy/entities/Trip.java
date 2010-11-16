package de.rowbuddy.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Trip
 *
 */
@Entity
public class Trip implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@ManyToOne
	private Boat boat;
	@OneToMany
	private Collection<TripMember> tripMembers;
	@ManyToOne
	private Member lastEditor;
	@ManyToOne
	private Route route;
	private static final long serialVersionUID = 1L;

	public Trip() {
		super();
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}   
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}   
	public Boat getBoat() {
		return this.boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}
	
	public Collection<TripMember> getTripMembers() {
		return tripMembers;
	}
	
	public void setTripMembers(Collection<TripMember> tripMembers) {
		this.tripMembers = tripMembers;
	}
	
	public Member getLastEditor() {
		return lastEditor;
	}
	
	public void setLastEditor(Member lastEditor) {
		this.lastEditor = lastEditor;
	}
	
	public Route getRoute() {
		return route;
	}
	
	public void setRoute(Route route) {
		this.route = route;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Trip other = (Trip) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
	
}
