package com.example.junitdemo.Repository;

import com.example.junitdemo.Entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
