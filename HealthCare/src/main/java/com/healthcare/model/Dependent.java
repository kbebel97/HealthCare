package com.healthcare.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "dependent")
public class Dependent implements Serializable {
	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="Dependent_ID")
	private Long id;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;

	@Column  
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "Enrollee_ID" ,referencedColumnName = "Enrollee_ID")
	private Enrollee enrollee;
	
	public Dependent() {
		this(-1L, "N/A", "N/A", null);
	}
	
	public Dependent(Long id, String firstName, String lastName, Date date) {
		this.id=id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = date;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Enrollee getEnrollee() {
		return enrollee;
	}

	public void setEnrollee(Enrollee enrollee) {
		this.enrollee = enrollee;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
