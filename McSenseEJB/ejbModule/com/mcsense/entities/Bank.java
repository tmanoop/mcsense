package com.mcsense.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the BANK database table.
 * 
 */
@Entity
@Table(name="BANK",schema="APP")
public class Bank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TRANSACTION_ID")
	private int transactionId;

	private int amount;

    @Temporal( TemporalType.DATE)
	@Column(name="TRANSACTION_DATE")
	private Date transactionDate;

	@Column(name="TRANSACTION_TYPE")
	private String transactionType;

	//bi-directional many-to-one association to People
    @ManyToOne
	@JoinColumn(name="BANK_ACCOUNT_ID", referencedColumnName="BANK_ACCOUNT_ID")
	private People people;

	//bi-directional many-to-one association to Task
    @ManyToOne
	@JoinColumn(name="TASK_ID")
	private Task task;

    public Bank() {
    }

	public int getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public People getPeople() {
		return this.people;
	}

	public void setPeople(People people) {
		this.people = people;
	}
	
	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
}