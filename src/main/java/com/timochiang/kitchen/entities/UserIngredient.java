package com.timochiang.kitchen.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class UserIngredient extends AbstractIngredient {
    @Column(nullable = false, columnDefinition = "DECIMAL(6,2) UNSIGNED", precision=6, scale=2)
    private Double originalQuantity;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",timezone="GMT+9")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredAt;
}
