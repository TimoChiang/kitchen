package com.timochiang.kitchen.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany
    @OrderColumn
    @JoinColumn(name = "parent_id")
    private List<Category> children = new LinkedList<Category>();

    @Transient
    private Boolean isRemovedNullOfChildren = false;

    @Column(name = "children_order")
    private Integer order;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(updatable = false, columnDefinition="DATETIME")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition="DATETIME")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
    @Where(clause = "quantity > 0")
    private List<UserIngredient> userIngredients;

    public List<Category> getChildren() {
        if (!isRemovedNullOfChildren) {
            // category will not be too huge
            this.children.removeAll(Collections.singleton(null));
            this.isRemovedNullOfChildren = true;
        }
        return children;
    }
}
