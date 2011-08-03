package com.mcsense.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the REPUTATION database table.
 * 
 */
@Entity
@Table(name="REPUTATION",schema="APP")
public class Reputation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="REPUTATION_ID")
	private int reputationId;

	@Column(name="REPUTATION_SCORE")
	private int reputationScore;

	//bi-directional many-to-one association to People
    @OneToOne
	@JoinColumn(name="PERSON_ID")
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
	
}