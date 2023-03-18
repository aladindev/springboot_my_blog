package com.aladin.springbootstudy.repository;

import com.aladin.springbootstudy.entity.KakaoProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<KakaoProfileEntity, Long> {
}
