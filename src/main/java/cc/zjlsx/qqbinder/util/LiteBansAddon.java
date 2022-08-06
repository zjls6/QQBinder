package cc.zjlsx.qqbinder.util;

import litebans.api.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LiteBansAddon {
    public static boolean isBanedByConsole(UUID uuid) {
        String query = "SELECT * FROM {bans} where uuid=? and banned_by_uuid=? and removed_by_uuid is null order by time desc";
        try (PreparedStatement st = Database.get().prepareStatement(query)) {
            st.setString(1, uuid.toString());
            st.setString(2, "CONSOLE");
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPermanentlyBaned(UUID uuid) {
        String query = "SELECT * FROM {bans} where uuid=? and until=? and removed_by_uuid is null order by time desc";
        try (PreparedStatement st = Database.get().prepareStatement(query)) {
            st.setString(1, uuid.toString());
            st.setInt(2, 0);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
