package com.teamplanner.rest.model;

import com.teamplanner.rest.model.dto.GamePlanDto;
import com.teamplanner.rest.model.entity.Friendship;
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

    public GamePlanDto gameplanToDto (GamePlan gamePlan, User user){
        GamePlanDto gamePlanDto = new GamePlanDto();
        BeanUtils.copyProperties(gamePlan, gamePlanDto, "author");
        gamePlanDto.setAuthorNickname(user.getNickname());

        return gamePlanDto;
    }

    public List<String> userFriendNicknames (List<Friendship> initiatedFriendships, List<Friendship> invitedToFriendships){

        List<String> friendNicknames = new ArrayList<>();

        if(!initiatedFriendships.isEmpty()) {
            for(Friendship friendship : initiatedFriendships){
                friendNicknames.add(friendship.getInvitedUser().getNickname());
            }
        }
        if(!invitedToFriendships.isEmpty()){
            for(Friendship friendship : invitedToFriendships){
                friendNicknames.add(friendship.getInvitingUser().getNickname());
            }
        }

        return friendNicknames;
    }

    public List<String> nicknamesOfInvitingUsers(List<Friendship> incomingFriendRequests) {
        List<String> invitingUsersNicknames = new ArrayList<>();

        if(!incomingFriendRequests.isEmpty()){
            for(Friendship friendship : incomingFriendRequests){
                invitingUsersNicknames.add(friendship.getInvitingUser().getNickname());
            }
        }

        return invitingUsersNicknames;
    }
}
