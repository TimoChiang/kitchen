package com.timochiang.kitchen.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class UserIngredient extends AbstractIngredient {

    @Column(nullable = false, columnDefinition = "DECIMAL(6,2) UNSIGNED", precision=6, scale=2)
    private Double originalQuantity;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",timezone="GMT+9")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredAt;

    public Double getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(Double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }
}
