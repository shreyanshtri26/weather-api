package com.weather.api.repository;

import com.weather.api.model.PincodeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PincodeLocationRepository extends JpaRepository<PincodeLocation, Long> {
    Optional<PincodeLocation> findByPincode(String pincode);
}