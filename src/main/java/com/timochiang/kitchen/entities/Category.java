package com.timochiang.kitchen.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
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

    public List<Category> getChildren() {
        if (!isRemovedNullOfChildren) {
            // category will not be too huge
            this.children.removeAll(Collections.singleton(null));
            this.isRemovedNullOfChildren = true;
        }
        return children;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
