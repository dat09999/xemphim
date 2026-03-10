package com.example.xemphim.Service;

import com.example.xemphim.DTO.Tittle.TittleRequest;
import com.example.xemphim.DTO.Tittle.TittleResponse;
import com.example.xemphim.Entity.Tittle;

import java.util.List;

public interface TittleService {
    public void add(TittleRequest tittle);
    public TittleResponse findById(Long id);
    public List<TittleResponse> findAll();
    public void delete(Long id);
    public List<TittleResponse> findByGenre(String genre);



    List<TittleResponse> findByPeople(Long Id);

    public List<TittleResponse> findByCountry(String Country);
    public List<TittleResponse> findByYear(int Year);
    public   List<TittleResponse> getNewest3();
    public List<TittleResponse> getFeatured6();
}
