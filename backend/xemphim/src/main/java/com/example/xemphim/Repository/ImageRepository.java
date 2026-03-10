package com.example.xemphim.Repository;

import com.example.xemphim.Entity.Image;
import com.example.xemphim.Entity.PassWordOtp;
import com.example.xemphim.Enum.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByOwnerID(Long ownerID);

    void deleteAllByOwnerID(Long ownerID);

    Optional<Image> findByOwnerID(Long ownerId);

    Optional<Image> findByOwnerIDAndKind(Long ownerID, Img kind);
}