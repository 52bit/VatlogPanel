package de.chriis.vatlog.mutes;

import de.chriis.vatlog.bans.Ban;
import de.chriis.vatlog.callbacks.MutesCallback;
import litebans.api.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;

public class MuteManager {

    private static HashMap<UUID, List<Mute>> playerMutes = new HashMap<>();

    public static void fetchPlayerMutes(UUID uuid, MutesCallback mutesCallback) {
        Executors.newCachedThreadPool().execute(() -> {
            List<Mute> mutes = new ArrayList<>();
            String query = "SELECT * FROM {mutes} WHERE uuid=?";

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

                        mutes.add(new Mute(reason, bannedByUuid, time, until, id, active, server));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Collections.reverse(mutes);

            playerMutes.put(uuid, mutes);
            mutesCallback.notifyLoaded();
        });
    }

    public static List<Mute> getPlayerMutes(UUID uuid) {
        if(playerMutes.containsKey(uuid)) {
            return playerMutes.get(uuid);
        }

        return null;
    }
}
