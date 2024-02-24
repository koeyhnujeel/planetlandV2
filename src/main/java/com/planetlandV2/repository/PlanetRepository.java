package com.planetlandV2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.planetlandV2.domain.Planet;

import jakarta.persistence.LockModeType;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long>, PlanetRepositoryCustom {

	boolean existsByPlanetName(String planetName);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = "SELECT p FROM Planet p WHERE p.id = :id")
	Optional<Planet> findByIdForBuy(@Param("id") Long planetId);
}
