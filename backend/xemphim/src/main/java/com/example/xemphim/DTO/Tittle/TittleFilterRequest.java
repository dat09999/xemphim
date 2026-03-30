package com.example.xemphim.DTO.Tittle;

import com.example.xemphim.Enum.TittleType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TittleFilterRequest    {
    private Integer year;
    private String country;
    private String genre;
    private String name;
    private TittleType tittleType;
    private String sortBy;
    private String sortDir;
}
