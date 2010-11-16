package de.rowbuddy.entities;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Route
 *
 */
@Entity
public class Route implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String name;
	private String shortDescription;
	private long versionNumber;
	private double lengthKM;
	private boolean mutable;
	private static final long serialVersionUID = 1L;

	public Route() {
		super();
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public String getShortDescription() {
		return this.shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}   
	public Long getVersionNumber() {
		return this.versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}   
	public double getLengthKM() {
		return this.lengthKM;
	}

	public void setLengthKM(double lengthKM) {
		this.lengthKM = lengthKM;
	}   
	public boolean getMutable() {
		return this.mutable;
	}

	public void setMutable(boolean mutable) {
		this.mutable = mutable;
	}
   
}