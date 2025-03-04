package com.uhu.saluhud.configuration.web;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 *
 * @author juald
 * @param <T>
 */
public class PageDto<T>
{

    private List<T> content;
    private int totalPages;
    private int currentPage;
    private long totalElements;

    public PageDto(Page<T> page)
    {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.totalElements = page.getTotalElements();
    }

    public List<T> getContent()
    {
        return content;
    }

    public void setContent(List<T> content)
    {
        this.content = content;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public long getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(long totalElements)
    {
        this.totalElements = totalElements;
    }
    
}
