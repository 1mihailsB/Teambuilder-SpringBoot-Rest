package com.teamplanner.rest.dao;

import com.teamplanner.rest.model.entity.GamePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlanRepository extends JpaRepository<GamePlan, Integer> {

}
