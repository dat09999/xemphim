package com.example.xemphim.Service;

import com.example.xemphim.DTO.Tittle.TittleFilterRequest;
import com.example.xemphim.DTO.Tittle.TittleRequest;
import com.example.xemphim.DTO.Tittle.TittleResponse;
import com.example.xemphim.DTO.Tittle.TittleUpDate;
import com.example.xemphim.Entity.Tittle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TittleService {
    public void add(TittleRequest tittle);
    public TittleResponse findById(Long id);
    public List<TittleResponse> findAll();
    public void delete(Long id);
    public List<TittleResponse> findByGenre(String genre);

    List<TittleResponse> filter(TittleFilterRequest tittle);
    List<TittleResponse> filterAdmin(TittleFilterRequest tittle);
    TittleResponse update(TittleUpDate tittle);
    public Long SumView();
//    public Page<TittleResponse> byviews(Pageable pageable);
    List<TittleResponse> findByPeople(Long Id);
    List<Tittle> findByName(String Name);
    public List<TittleResponse> findByCountry(String Country);
    public List<TittleResponse> findByYear(int Year);
    public   List<TittleResponse> getNewest3();
    public List<TittleResponse> getFeatured6();
    public int getview(Long Id);
    public Long getviews();
    public void watch(Long Id);
}
