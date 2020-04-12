package com.teamplanner.rest.service;

import com.teamplanner.rest.dao.GamePlanRepository;
import com.teamplanner.rest.model.entity.GamePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GamePlanServiceImpl implements GamePlanService{

    private GamePlanRepository gamePlanRepository;

    @Autowired
    public GamePlanServiceImpl(GamePlanRepository gamePlanRepository) {
        this.gamePlanRepository = gamePlanRepository;
    }

    @Override
    public List<GamePlan> findAll() {
        return gamePlanRepository.findAll();
    }

    @Override
    public GamePlan findById(int id) {
        Optional<GamePlan> result = gamePlanRepository.findById(id);

        GamePlan gamePlan;

        if(result.isPresent()){
            gamePlan = result.get();
        }else{
            gamePlan = null;
        }
        return gamePlan;
    }

    @Override
    public void save(GamePlan gamePlan) {
        gamePlanRepository.save(gamePlan);
    }

    @Override
    public void deleteById(int id) {
        gamePlanRepository.deleteById(id);
    }
}
