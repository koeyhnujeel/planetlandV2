package com.planetlandV2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planetlandV2.domain.Planet;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long>, PlanetRepositoryCustom {

	boolean existsByPlanetName(String planetName);
}
