package com.leyou.item.pojo;

import javax.persistence.*;

/**
 * 注解地址--> https://github.com/abel533/Mapper/wiki/2.2-mapping
 */
@Table(name="tb_params")
public class SpecParam {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long categoryId;
    private Long groupId;
    //在使用关键字的情况，还会有下面的用法 查询时使用制定名称拼接sql
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean searching;
    private Boolean generic;
    private String segments;
    private String name;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean getNumeric() {
        return numeric;
    }

    public void setNumeric(Boolean numeric) {
        this.numeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getSearching() {
        return searching;
    }

    public void setSearching(Boolean searching) {
        this.searching = searching;
    }

    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SpecParam{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", groupId=" + groupId +
                ", numeric=" + numeric +
                ", unit='" + unit + '\'' +
                ", searching=" + searching +
                ", generic=" + generic +
                ", segments='" + segments + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
