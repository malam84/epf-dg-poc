package com.rh.poc.db;

import jakarta.persistence.*;
import java.io.Serializable;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

@Entity
@Table(name = "users")
public class User implements Serializable {

	
	
	
    private static final long serialVersionUID = 1L;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	 @ProtoFactory
	public User(Integer id, String email, String name, int version) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.version = version;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Version
    private int version;
    
    @ProtoField(number = 1, required = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@ProtoField(number = 2, required = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@ProtoField(number = 3, required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ProtoField(number = 4, required = true)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

    // Getters and Setters
    
}
