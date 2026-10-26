package ru.error623.sethome;

public class HomeData {
    private final String homeName;
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;

    public HomeData(String homeName, String worldName, double x, double y, double z) {
        this.homeName = homeName;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getHomeName() {
        return homeName;
    }

    public String getWorldName() {
        return worldName;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }
}
