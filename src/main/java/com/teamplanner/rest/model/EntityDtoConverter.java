package com.teamplanner.rest.model;

import com.teamplanner.rest.model.dto.GamePlanDto;
import com.teamplanner.rest.model.entity.Friendship;
import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.GameplanMember;
import com.teamplanner.rest.model.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntityDtoConverter {

    public List<GamePlanDto> gameplansToDto(List<GamePlan> gamePlans, User user, List<GamePlan> userGuestGameplans){
        List<GamePlanDto> gamePlansDto = new ArrayList<>();
        if(!gamePlans.isEmpty()) {
            for(GamePlan gameplan : gamePlans){
                GamePlanDto gamePlanDto = new GamePlanDto();
                BeanUtils.copyProperties(gameplan, gamePlanDto, "author");
                gamePlanDto.setAuthorNickname(user.getNickname());

                gamePlansDto.add(gamePlanDto);
            }
        }

        if(!userGuestGameplans.isEmpty()){
            for(GamePlan gameplan : userGuestGameplans){
                GamePlanDto gamePlanDto = new GamePlanDto();
                BeanUtils.copyProperties(gameplan,gamePlanDto, "author");
                gamePlanDto.setAuthorNickname(gameplan.getAuthor().getNickname());

                gamePlansDto.add(gamePlanDto);
            }
        }
        return gamePlansDto;
    }

    public GamePlanDto gameplanToDto (GamePlan gamePlan, List<GameplanMember> members){
        GamePlanDto gamePlanDto = new GamePlanDto();
        BeanUtils.copyProperties(gamePlan, gamePlanDto, "author");
        gamePlanDto.setAuthorNickname(gamePlan.getAuthor().getNickname());

        List<String> memberNicknames = new ArrayList<>();
        if(!members.isEmpty()){
            for(GameplanMember gpm : members){
                memberNicknames.add(gpm.getMember().getNickname());
            }
        }

        gamePlanDto.setMembers(memberNicknames);

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

    public List<String> userNicknames (List<User> users) {
        List<String> userNicknames = new ArrayList<>();

        if (!users.isEmpty()) {
            for (User user : users) {
                userNicknames.add(user.getNickname());
            }
        }

        return userNicknames;
    }

    public List<String[]> gameplanMembersToAuthorAndTitleMap(List<GameplanMember> pendingInvites){
        List<String[]> authorsAndGametitles = new ArrayList<>();

        if(!pendingInvites.isEmpty()){
            for(GameplanMember gpm : pendingInvites){
                String[] authorAndGametitle = new String[3];
                authorAndGametitle[0] = gpm.getGamePlan().getAuthor().getNickname();
                authorAndGametitle[1] = gpm.getGamePlan().getTitle();
                authorAndGametitle[2] = Integer.toString(gpm.getId());
                authorsAndGametitles.add(authorAndGametitle);
            }
        }

        return authorsAndGametitles;
    }

}
