package com.teamplanner.rest.dao;

import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.GameplanMember;
import com.teamplanner.rest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameplanMembersRepository extends JpaRepository<GameplanMember, Integer> {

    GameplanMember findByGamePlanAndMember(GamePlan gamePlan, User member);

    List<GameplanMember> findByMemberAndStatusEquals(User member, int status);

    List<GameplanMember> deleteByGamePlan(GamePlan gamePlan);

    GameplanMember deleteById(int id);

    List<GameplanMember> findByGamePlanAndStatusEquals(GamePlan gamePlan, int status);

    List<GameplanMember> deleteByMemberAndGamePlanIn(User member, List<GamePlan> gameplans);
}
