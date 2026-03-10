package com.example.xemphim.Service;

import com.example.xemphim.DTO.User.UserRequest;
import com.example.xemphim.DTO.User.Userreponse;
import com.example.xemphim.Entity.User;

public interface Userservice {
   public Userreponse add(UserRequest user);
   public Userreponse update(User user);
   public Userreponse delete(User user);
}
