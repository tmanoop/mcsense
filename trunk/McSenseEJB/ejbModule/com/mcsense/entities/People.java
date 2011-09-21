package com.mcsense.entities;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the PEOPLE database table.
 * 
 */
@Entity
@Table(name="PEOPLE",schema="APP", uniqueConstraints = {@UniqueConstraint(columnNames={"BANK_ACCOUNT_ID"})})
@NamedQueries({
  @NamedQuery(name="People.findByLName",
              query="Select e from People e where e.personLname = :name"),
  @NamedQuery(name="People.findByPrimaryKey",
              query="SELECT e FROM People e WHERE e.personId = :id"),
})
public class People implements Entity,Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
//	@TableGenerator(name = "SEQUENCE", initialValue = 1, allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="people_id")
//	@SequenceGenerator(name="people_id",sequenceName="APP.people_id", initialValue=1, allocationSize=5)
	@Column(name="PERSON_ID")
	private int personId;

	@Column(name="PERSON_ADDRESS")
	private String personAddress;

	@Column(name="PERSON_FNAME")
	private String personFname;

	@Column(name="PERSON_LNAME")
	private String personLname;
	
	@Column(name="BANK_ACCOUNT_ID", unique=true)
	private String bankAccountId;

	@Column(name="EMAILID")
	private String emailId;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="NJIT_DEPARTMENT")
	private String njitDepartment;
	
	@Column(name="NJIT_ACADEMIC_YEAR")
	private String njitAcademicYear;
	
	@Column(name="AGE_GROUP")
	private String ageGroup;
	
	//bi-directional many-to-one association to Bank
	@OneToMany(mappedBy="people")
	private Set<Bank> banks;
	
	@OneToMany(mappedBy="people")
	private Set<Task> tasks;

	//bi-directional many-to-one association to Reputation
	@OneToOne(orphanRemoval=true,mappedBy="people",cascade = {CascadeType.REMOVE,CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
//	@JoinColumn(name="PERSON_ID",referencedColumnName="PERSON_ID", insertable=false, updatable=false)
	private Reputation reputation;

    public People() {
    }

	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getPersonAddress() {
		return this.personAddress;
	}

	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}

	public String getPersonFname() {
		return this.personFname;
	}

	public void setPersonFname(String personFname) {
		this.personFname = personFname;
	}

	public String getPersonLname() {
		return this.personLname;
	}

	public void setPersonLname(String personLname) {
		this.personLname = personLname;
	}
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNjitDepartment() {
		return njitDepartment;
	}

	public void setNjitDepartment(String njitDepartment) {
		this.njitDepartment = njitDepartment;
	}

	public String getNjitAcademicYear() {
		return njitAcademicYear;
	}

	public void setNjitAcademicYear(String njitAcademicYear) {
		this.njitAcademicYear = njitAcademicYear;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public Set<Bank> getBanks() {
		return this.banks;
	}

	public void setBanks(Set<Bank> banks) {
		this.banks = banks;
	}

	public Reputation getReputation() {
		return reputation;
	}

	public void setReputation(Reputation reputation) {
		this.reputation = reputation;
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

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
}