package com.timochiang.kitchen.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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

    public void setScheduleDateTime() {
        try {
            this.scheduleDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(this.scheduleDate + " " + this.scheduleTime + ":00:00");
        } catch(ParseException e) {
            e.printStackTrace();
            System.out.print("日程フォーマットが間違っている。");
        }
    }

    public void setIngredients(List<DishIngredient> ingredients) {
        for (DishIngredient ingredient : ingredients) {
            ingredient.setDish(this);
        }
        this.ingredients = ingredients;
    }
}


