package fun.sleepy.voidcontrol.permissions;

public enum VoidControlPermission {
    ADMIN("voidcontrol.admin"),
    BYPASS("voidcontrol.bypass");

    private final String permissionName;

    VoidControlPermission(final String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() { return permissionName; }
}
