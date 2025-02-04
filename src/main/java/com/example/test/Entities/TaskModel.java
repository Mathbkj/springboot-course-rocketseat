package com.example.test.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID taskId;
    private String description;
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public UUID getTaskId() {
        return taskId;
    }


    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public String getPriority() {
        return priority;
    }

    public UUID getIdUser() {
        return idUser;
    }
    public void setIdUser(UUID idUser){
        this.idUser=idUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
    public void setTile(String title) throws Exception{
        if(title.length()>=50){
            throw new Exception("O campo title deve conter no máximo 50 caracteres");
        }
        this.title=title;
    }
}
