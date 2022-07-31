package cc.zjlsx.qqbinder.enums;

public enum Permissions {

    Admin("advancedvote.admin"),
    None("");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
