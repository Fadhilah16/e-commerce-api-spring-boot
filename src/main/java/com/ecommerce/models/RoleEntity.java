package com.ecommerce.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ecommerce.models.enums.RoleTypes;

@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private RoleTypes name;
	
	public RoleEntity() {
	}
	public RoleEntity(RoleTypes name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public RoleTypes getName() {
		return name;
	}
	public void setName(RoleTypes name) {
		this.name = name;
	}
}
