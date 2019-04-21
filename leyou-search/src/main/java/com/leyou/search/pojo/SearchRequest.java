package com.leyou.search.pojo;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Map;

/**
 * @author zhang
 * @date 2019年04月18日 21:11
 */
public class SearchRequest {
    private String key; //搜索关键词
    private Integer page;//当前页

    private Boolean desc;
    private String sortBy;
    private static final Integer DEFAULT_SIZE=5;
    private static final Integer DEFAULT_PAGE=1;
    private Map<String,String> filter;
    public String getKey() {
        return key;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(this.page==null){
            return DEFAULT_PAGE;
        }
        return Math.max(page,DEFAULT_PAGE);
    }

    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getSize(){
        return DEFAULT_SIZE;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
