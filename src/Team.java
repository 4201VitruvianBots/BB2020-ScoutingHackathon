public class Team {
    public int number;
    public int matches;
    public double teleOpPowerScore;
    //public int lowerPort, upperPort;
    public double autoPowerScore;
    //public int autoLowerPort, autoUpperPort;
    public double staging;
    public double endgamePoints;
    public double totalDefense;

    public double favor;                // Sum of ratios we care about

    public Team(int number) {
        this.number = number;
    }


}
