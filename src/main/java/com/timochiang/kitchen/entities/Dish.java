package com.timochiang.kitchen.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "INT(11) UNSIGNED")
    private Integer recipeId;

    @Transient
    private String scheduleDate;

    @Transient
    private String scheduleTime;

    @Column(columnDefinition="DATETIME")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:ii")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:ii")
    private Date scheduleDateTime;

    @Column(updatable = false, columnDefinition="DATETIME")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition="DATETIME")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dish")
    private List<DishIngredient> ingredients;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Date getScheduleDateTime() { return scheduleDateTime; }

    public void setScheduleDateTime() {
        try {
            this.scheduleDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(this.scheduleDate + " " + this.scheduleTime + ":00:00");
        } catch(ParseException e) {
            e.printStackTrace();
            System.out.print("日程フォーマットが間違っている。");
        }
    }

    public List<DishIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<DishIngredient> ingredients) {
        for (DishIngredient ingredient : ingredients) {
            ingredient.setDish(this);
        }
        this.ingredients = ingredients;
    }
}


