package cc.zjlsx.qqbinder.util;

import litebans.api.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LiteBansAddon {
    public static boolean isBanedByConsole(UUID uuid) {
        String query = "SELECT COUNT(*) FROM {bans} WHERE uuid=? AND banned_by_uuid='CONSOLE' AND removed_by_uuid IS NULL";
        return checkIfBanned(uuid, query);
    }

    public static boolean isPermanentlyBaned(UUID uuid) {
        String query = "SELECT COUNT(*) FROM {bans} WHERE uuid=? AND until=0 AND removed_by_uuid IS NULL";
        return checkIfBanned(uuid, query);
    }

    private static boolean checkIfBanned(UUID uuid, String query) {
        try (PreparedStatement st = Database.get().prepareStatement(query)) {
            st.setString(1, uuid.toString());
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
