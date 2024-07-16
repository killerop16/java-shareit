package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "owner_id")
    private  int idOwner;
    private  String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
//    @Column(name = "request_id")
//    private int request;
}
