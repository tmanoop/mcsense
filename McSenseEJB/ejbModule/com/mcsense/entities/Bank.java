package com.mcsense.entities;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the BANK database table.
 * 
 */
@Entity
public class Bank implements Entity,Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bank_id")
    @SequenceGenerator(name="bank_id",sequenceName="bank_id", allocationSize=1)
	@Column(name="TRANSACTION_ID")
	private int transactionId;

	private int amount;

	@Column(name="TASK_ID")
	private int taskId;

    @Temporal( TemporalType.DATE)
	@Column(name="TRANSACTION_DATE")
	private Date transactionDate;

	@Column(name="TRANSACTION_TYPE")
	private String transactionType;

	//bi-directional many-to-one association to People
    @ManyToOne
	@JoinColumn(name="BANK_ACCOUNT_ID", referencedColumnName="BANK_ACCOUNT_ID")
	private People people;

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

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
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
	
}