package com.teamplanner.rest.controller;

import com.teamplanner.rest.model.EntityDtoConverter;
import com.teamplanner.rest.model.entity.Friendship;
import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.User;
import com.teamplanner.rest.service.FriendsService;
import com.teamplanner.rest.service.GamePlanService;
import com.teamplanner.rest.service.GameplanMemberService;
import com.teamplanner.rest.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping("friends")
public class FriendsController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    UserService userService;
    FriendsService friendsService;
    GamePlanService gamePlanService;
    GameplanMemberService gameplanMemberService;
    EntityDtoConverter entityDtoConverter;
    SimpMessageSendingOperations stomp;

    @Autowired
    public FriendsController(UserService userService, EntityDtoConverter entityDtoConverter,
                             GameplanMemberService gameplanMemberService, GamePlanService gamePlanService,
                             FriendsService friendsService, SimpMessageSendingOperations stomp) {
        this.userService = userService;
        this.entityDtoConverter = entityDtoConverter;
        this.gamePlanService = gamePlanService;
        this.gameplanMemberService = gameplanMemberService;
        this.friendsService = friendsService;
        this.stomp = stomp;
    }

    @GetMapping("/myFriends")
    public List<String> getFriends(Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());
        List<Friendship> initiatedFriendships = user.getInitiatedFriendships();
        List<Friendship> invitedToFriendships = user.getInvitedToFriendships();

        return entityDtoConverter.userFriendNicknames(initiatedFriendships, invitedToFriendships);
    }

    @PutMapping("/invite")
    @Transactional
    public String inviteFriend(@RequestBody String nickname, Authentication authentication){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\[\\]!@_-]{1,16}$");
        Matcher matcher = pattern.matcher(nickname);
        if(!matcher.matches()){
            return "Incorrect nickname";
        }

        User invitingUser = userService.findById((String) authentication.getPrincipal());

        User invitedUser = userService.findByNickname(nickname);


        if(invitingUser.equals(invitedUser)) return "Can't invite yourself";

        if(invitedUser!=null) {

            Friendship existingOrPendingFriendship = friendsService.checkForFriendshipOrPendingRquests(invitingUser, invitedUser);
            if(existingOrPendingFriendship!=null) {
                if(existingOrPendingFriendship.getStatus()==0){
                    return "Request is pending";
                }else{
                    return "This person is already your friend";
                }
            }

            Friendship friendship = new Friendship(invitingUser, invitedUser, 0);
            invitedUser.addIncomingFriendRequests(friendship);

            stomp.convertAndSendToUser(invitedUser.getGooglesub(), "/queue/requests",
                    "Friend request");

            return "Friend request sent";
        }

        return "Error, user doesn't exist";
    }

    @GetMapping("/incomingRequests")
    public List<String> getIncomingRequests(Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());
        List<Friendship> incomingFriendRequests = user.getIncomingFriendRequests();

        List<String> nicknames = entityDtoConverter.nicknamesOfInvitingUsers(incomingFriendRequests);
        return nicknames;
    }

    @PatchMapping("/acceptRequest")
    @Transactional
    public void acceptRequest(@RequestBody String nickname, Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());
        User invitingUser = userService.findByNickname(nickname);

        Friendship incomingRequest = friendsService.checkForFriendshipOrPendingRquests(invitingUser, user);

        if(incomingRequest!=null) incomingRequest.setStatus(1);
    }

    @DeleteMapping("/removeFriend")
    @Transactional
    public void removeFriend(@RequestBody String nickname, Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());
        User friend = userService.findByNickname(nickname);

        Friendship friendship = friendsService.checkForFriendshipOrPendingRquests(user, friend);
        friendsService.deleteById(friendship.getId());

        List<GamePlan> usersGameplans = user.getGamePlans();
        gameplanMemberService.deleteRemovedFriendFromGames(friend,usersGameplans);

        List<GamePlan> friendsGameplans = friend.getGamePlans();
        gameplanMemberService.deleteRemovedFriendFromGames(user,friendsGameplans);

        stomp.convertAndSendToUser(friend.getGooglesub(), "/queue/requests",
                "Game invite");
        stomp.convertAndSendToUser(user.getGooglesub(), "/queue/requests",
                "Game invite");

    }

    @GetMapping("/invitableToGame/{gameId}")
    public List<String> getFriendInvitableToGame(@PathVariable int gameId, Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());
        List<Friendship> initiatedFriendships = user.getInitiatedFriendships();
        List<Friendship> invitedToFriendships = user.getInvitedToFriendships();

        List<String> friendNicknames = entityDtoConverter.userFriendNicknames(initiatedFriendships, invitedToFriendships);


        List<User> invitableFriends = userService.findFriendsInvitableToGame(friendNicknames, gameId);


        List<String> invitableFriendsNicknames = entityDtoConverter.userNicknames(invitableFriends);

        return invitableFriendsNicknames;
    }

}
