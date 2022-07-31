package cc.zjlsx.qqbinder.model;

import java.util.UUID;

public class QQBindRequest {
    private final Long expiresTime;
    private final UUID token;
    private final Long QQCode;
    private final String playerName;
    private final UUID uuid;

    public QQBindRequest(Long QQCode, String playerName, UUID uuid, Long expiresSecond) {
        long time = System.currentTimeMillis();
        this.expiresTime = time + expiresSecond * 1000;
        token = UUID.randomUUID();
        this.QQCode = QQCode;
        this.playerName = playerName;
        this.uuid = uuid;
    }

    public UUID getToken() {
        return token;
    }

    public Long getQQCode() {
        return QQCode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Long getExpiresTime() {
        return expiresTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > getExpiresTime();
    }
}
