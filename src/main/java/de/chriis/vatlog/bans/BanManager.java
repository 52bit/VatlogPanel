package de.chriis.vatlog.bans;

import de.chriis.vatlog.callbacks.BansCallback;
import litebans.api.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;

public class BanManager {

    private static HashMap<UUID, List<Ban>> playerBans = new HashMap<>();

    public static void fetchPlayerBans(UUID uuid, BansCallback callback) {
        Executors.newCachedThreadPool().execute(() -> {
            List<Ban> bans = new ArrayList<>();
            String query = "SELECT * FROM {bans} WHERE uuid=?";

            try (PreparedStatement st = Database.get().prepareStatement(query)) {
                st.setString(1, uuid.toString());
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        String reason = rs.getString("reason");
                        String bannedByUuid = rs.getString("banned_by_uuid");
                        String server = rs.getString("server_scope");
                        long time = rs.getLong("time");
                        long until = rs.getLong("until");
                        long id = rs.getLong("id");
                        boolean active = rs.getBoolean("active");

                        bans.add(new Ban(reason, bannedByUuid, time, until, id, active, server));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Collections.reverse(bans);

            playerBans.put(uuid, bans);
            callback.notifyLoaded();
        });
    }

    public static List<Ban> getPlayerBans(UUID uuid) {
        if(playerBans.containsKey(uuid)) {
            return playerBans.get(uuid);
        }

        return null;
    }
}
