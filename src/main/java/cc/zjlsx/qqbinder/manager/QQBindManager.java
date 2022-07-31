package cc.zjlsx.qqbinder.manager;

import cc.zjlsx.qqbinder.model.QQBindRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QQBindManager {
    private final List<QQBindRequest> qqBindRequests = new ArrayList<>();

    public void addRequest(QQBindRequest qqBindRequest) {
        qqBindRequests.add(qqBindRequest);
    }

    public void removeRequest(QQBindRequest qqBindRequest) {
        qqBindRequests.remove(qqBindRequest);
    }

    public void removeAllRequest() {
        qqBindRequests.clear();
    }

    public Optional<QQBindRequest> getRequest(UUID token) {
        return qqBindRequests.stream().filter(qqBindRequest -> qqBindRequest.getToken().equals(token)).findFirst();
    }

    public Optional<QQBindRequest> getRequest(String playerName) {
        return qqBindRequests.stream().filter(qqBindRequest -> !qqBindRequest.isExpired())
                .filter(qqBindRequest -> qqBindRequest.getPlayerName().equals(playerName)).findFirst();
    }
}
