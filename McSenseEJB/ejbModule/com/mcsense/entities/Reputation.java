package com.mcsense.entities;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.persistence.*;


/**
 * The persistent class for the REPUTATION database table.
 * 
 */
@Entity
public class Reputation implements Entity,Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
//	@TableGenerator(name = "SEQUENCE", initialValue = 1, allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="reputation_id")
//    @SequenceGenerator(name="reputation_id",sequenceName="APP.reputation_id", initialValue=1,allocationSize=5)
	@Column(name="REPUTATION_ID")
	private int reputationId;

	@Column(name="REPUTATION_SCORE")
	private int reputationScore;
	
	@Column(name="PERSON_ID", updatable=false)
	private int personId;

	//bi-directional one-to-one association to People

	@OneToOne
	@PrimaryKeyJoinColumn(name="PERSON_ID",referencedColumnName="PERSON_ID")
	private People people;

    public Reputation() {
    }

	public int getReputationId() {
		return this.reputationId;
	}

	public void setReputationId(int reputationId) {
		this.reputationId = reputationId;
	}

	public int getReputationScore() {
		return this.reputationScore;
	}

	public void setReputationScore(int reputationScore) {
		this.reputationScore = reputationScore;
	}

	public People getPeople() {
		return this.people;
	}

	public void setPeople(People people) {
		this.people = people;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
}