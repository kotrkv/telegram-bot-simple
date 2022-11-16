package com.kotrkv.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity(name = "userData")
public class UserData {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
}
