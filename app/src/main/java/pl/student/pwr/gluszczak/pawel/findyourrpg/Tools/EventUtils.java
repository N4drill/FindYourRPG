package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;

abstract public class EventUtils {

    public static List<Map<String, Object>> prepareUpdateDatabaseList(List<User> newParticipants) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (User user : newParticipants) {
            Map<String, Object> newDbUser = new HashMap<>();
            newDbUser.put("email", user.getEmail());
            newDbUser.put("id", user.getId());
            newDbUser.put("username", user.getUsername());
            newDbUser.put("age", user.getAge());
            newDbUser.put("gamesAsMaster", user.getGamesAsMaster());
            newDbUser.put("gamesAsPlayer", user.getGamesAsPlayer());
            newDbUser.put("playerCreativity", user.getPlayerCreativity());
            newDbUser.put("playerBehaviour", user.getPlayerBehaviour());
            newDbUser.put("playerGameFeel", user.getPlayerGameFeel());
            newDbUser.put("masterCreativity", user.getMasterCreativity());
            newDbUser.put("masterBehaviour", user.getMasterBehaviour());
            newDbUser.put("masterGameFeel", user.getMasterGameFeel());
            newDbUser.put("avatarUrl", user.getAvatarUrl());
            newDbUser.put("phone", user.getPhone());
            newDbUser.put("bio", user.getBio());
            list.add(newDbUser);
        }
        return list;
    }


}
