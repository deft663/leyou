package com.leyou.item.pojo;

import javax.persistence.*;
import java.util.List;

@Table(name="tb_group")
public class SpecGroup {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long categoryId;

    private String name;

    @Transient
    private List<SpecParam> params; // 该组下的所有规格参数集合
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecParam> getParams() {
        return params;
    }

    public void setParams(List<SpecParam> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }
}
