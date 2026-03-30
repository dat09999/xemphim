package com.example.xemphim.Service;

import com.example.xemphim.DTO.User.UserRequest;
import com.example.xemphim.DTO.User.Userreponse;
import com.example.xemphim.Entity.User;

public interface Userservice {
   public Userreponse add(UserRequest user);
   public void update(int user);
   public void delete(int user);
}
