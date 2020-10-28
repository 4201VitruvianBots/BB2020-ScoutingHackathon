public class Team {
    int number;
    int matches;
    public int lowerPort, upperPort;
    public int autoLowerPort, autoUpperPort;
    public int staging;
    public int endgamePoints;
    public int totalDefense;

    public double favor;                // Sum of ratios we care about

    public Team(int number) {
        this.number = number;
    }


}
