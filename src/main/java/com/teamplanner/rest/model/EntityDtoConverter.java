package com.teamplanner.rest.model;

import com.teamplanner.rest.model.dto.GamePlanDto;
import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntityDtoConverter {

    public List<GamePlanDto> gameplansToDto(List<GamePlan> gamePlans, User user){
        List<GamePlanDto> gamePlansDto = new ArrayList<>();
        if(!gamePlans.isEmpty()) {
            for(GamePlan gameplan : gamePlans){
                GamePlanDto gamePlanDto = new GamePlanDto();
                BeanUtils.copyProperties(gameplan, gamePlanDto, "author");
                gamePlanDto.setAuthorNickname(user.getNickname());

                gamePlansDto.add(gamePlanDto);
            }
        }
        return gamePlansDto;
    }
}
