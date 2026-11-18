public class GuildMember {
    private String name;
    private String rank;
    private int missionsCompleted;

    public GuildMember(String name, String rank, int missionsCompleted) {
        this.name = name;
        this.rank = rank;
        this.missionsCompleted = missionsCompleted;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public int getMissionsCompleted() {
        return missionsCompleted;
    }

    @Override
    public String toString() {
        return name + " (" + rank + ", Missions: " + missionsCompleted + ")";
    }
}
