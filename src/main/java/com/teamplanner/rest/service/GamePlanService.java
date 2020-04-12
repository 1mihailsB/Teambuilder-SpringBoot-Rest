package com.teamplanner.rest.service;

import com.teamplanner.rest.model.entity.GamePlan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GamePlanService {

    public List<GamePlan> findAll();

    public GamePlan findById(int id);

    public void save(GamePlan gamePlan);

    public void deleteById(int id);
}
