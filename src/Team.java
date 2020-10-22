public class Team {
    int number;
    int totalScore, matches;
    public int lowerPort, upperPort;
    public int autoLowerPort, autoUpperPort;
    public int staging;
    public int totalDefense;

    public Team(int number) {
        this.number = number;
    }

    public int getAverageScore() {
        return totalScore / matches;
    }

    public void appendScore(int score) {
        totalScore += score;
        matches++;
    }
}
