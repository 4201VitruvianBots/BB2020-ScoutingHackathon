import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    static HashMap<Integer, Team> teamObjects = new HashMap<Integer, Team>();

    static int[] alreadyPicked = {3538};
//  3538, 1481, 548, 2591, 33, 573, 247, 6742, 3414, 4768, 6013, 240, 3096, 835, 6120, 5214, 5577, 5090, 8179, 4758, 5498, 94, 7232, 7856, 5695
    static int ourTeam = 4130;
    static double avgTeleOpPowerScore = 0, avgAutoPowerScore = 0, avgEndgame = 0, avgDefense = 0, avgFoul = 0;

    public static boolean arrayContainsElement(int[] array, int element) {
        for (int i : array) {
            if (i == element) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {
        teamObjects.put(0, new Team(0));

        int lightDefenseDeficit, heavyDefenseDeficit;

        try {
            File data = new File("src/data.CSV");
            Scanner reader = new Scanner(data);
            reader.nextLine();
            while(reader.hasNextLine()) {
                Team team;
                String[] row = reader.nextLine().split(",");
                int teamNumber = Integer.parseInt(row[1]);

                if(teamObjects.containsKey(teamNumber)) {
                    team = teamObjects.get(teamNumber);
                } else {
                    team = new Team(teamNumber);
                    teamObjects.put(teamNumber, team);
                }

                team.autoPowerScore += Integer.parseInt(row[4]) * 2;
                for(int i = 5; i < 10; i++) {
                    team.autoPowerScore += Integer.parseInt(row[i]) * 5;
                }

                team.teleOpPowerScore += Integer.parseInt(row[12]);
                for(int i = 13; i < 18; i++) {
                    team.teleOpPowerScore += Integer.parseInt(row[i]) * 2.5;
                }

                switch(row[20]) {
                    case "Light":
                        team.totalDefense++;
                        break;
                    case "Heavy":
                        team.totalDefense += 2;
                        break;
                    default:
                        break;
                } //Gives points according to the amount of defense that the team had

                switch(row[22]) {
                    case "Park":
                        team.endgamePoints += 5;
                        break;
                    case "Climb":
                        team.endgamePoints += 25;
                        break;
                    default:
                        break;
                }

                if(row[23].equals("1")) {
                    team.endgamePoints += 15;
                }

                team.foulPoints += Integer.parseInt(row[24]);

                team.matches++;
            }

            System.out.println(pickTeam(ourTeam));

        } catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }




        // Loop through all the different entries
            // Create an array of columns for each entry
            // If the team already exists in the HashMap, continue, otherwise create it
            // Loop through all the different columns
                // Add data to their respective variables in the correct team object

    }

    /*public static getTeamTotalScore() {
        for (Team i : teamObjects) {
            return lowerPort + (2.5 * upperPort) + (2 * autoLowerPort) + (5 * autoUpperPort) //Calculates an average score for the entire performance of the match
        }
    }*/

    // ALGORITHM FOR PICKING A TEAM
    // Find the averages for all the different fields of data
    // Find the three (or so) we're lacking in in terms of said average
    // Find the best teams with respect to said fields of data

    public static void calculateAverages() {
        int teams = 0;
        double teleOpPowerScoreAvgs = 0, autoPowerScoreAvgs = 0, endgame = 0, defense = 0, foul = 0;

        for(int i : teamObjects.keySet()) {
            if(arrayContainsElement(alreadyPicked, i) || i == 0)
                continue;
            
            teams++;

            Team currentTeam = teamObjects.get(i);

            teleOpPowerScoreAvgs += currentTeam.teleOpPowerScore / currentTeam.matches;
            autoPowerScoreAvgs += currentTeam.autoPowerScore / currentTeam.matches;
            endgame += currentTeam.endgamePoints / currentTeam.matches;
            defense += currentTeam.totalDefense / currentTeam.matches;
            foul += currentTeam.foulPoints / currentTeam.matches;
        }

        avgAutoPowerScore = autoPowerScoreAvgs / teams;
        avgTeleOpPowerScore = teleOpPowerScoreAvgs / teams;
        avgEndgame = endgame / teams;
        avgDefense = defense / teams;
        avgFoul = foul / teams;

        System.out.println("Averages:\nTPS: " + avgTeleOpPowerScore + "\nAPS: " + avgAutoPowerScore + "\nE:   " + avgEndgame + "\nD:   " + avgDefense + "\nF-:  " + avgFoul);
    }

    public static int pickTeam(int ourTeam) {
        calculateAverages();

        Team ourTeamObject = teamObjects.get(ourTeam);

        ourTeamObject.ratioAutoPowerScore = ourTeamObject.autoPowerScore / ourTeamObject.matches / avgAutoPowerScore;
        ourTeamObject.ratioTeleOpPowerScore = ourTeamObject.teleOpPowerScore / ourTeamObject.matches / avgTeleOpPowerScore;
        ourTeamObject.ratioEndgame = ourTeamObject.endgamePoints / ourTeamObject.matches / avgEndgame;
        ourTeamObject.ratioDefense = ourTeamObject.totalDefense / ourTeamObject.matches / avgDefense;

        System.out.println("Ratios:\nTPS: " + ourTeamObject.ratioTeleOpPowerScore + "\nAPS: " + ourTeamObject.ratioAutoPowerScore + "\nE:   " 
        + ourTeamObject.ratioEndgame + "\nD:   " + ourTeamObject.ratioDefense);

        double[] ratios = {ourTeamObject.ratioTeleOpPowerScore, ourTeamObject.ratioAutoPowerScore, ourTeamObject.ratioEndgame, ourTeamObject.ratioDefense};
        int[] lowestSkills = indexOfSmallestThree(ratios);

        return findMatch(lowestSkills);
    }

    public static int findMatch(int[] lowestSkills) {
        Team favoredTeam = teamObjects.get(0);

        for(int i : teamObjects.keySet()) {
            if(arrayContainsElement(alreadyPicked, i) || i == ourTeam || i == 0)
                continue;

            Team currentTeam = teamObjects.get(i);                                       // Make sure favor value is reset

            for(int ii : lowestSkills) {
                switch(ii) {
                    case 0:
                        currentTeam.ratioTeleOpPowerScore = currentTeam.teleOpPowerScore / currentTeam.matches / avgTeleOpPowerScore; // Add the ratio to the team's favorability
                        break;
                    case 1:
                        currentTeam.ratioAutoPowerScore = currentTeam.autoPowerScore / currentTeam.matches / avgAutoPowerScore;      // Add the ratio to the team's favorability
                        break;
                    case 2:
                        currentTeam.ratioEndgame = currentTeam.endgamePoints / currentTeam.matches / avgEndgame;   // Add the ratio to the team's favorability
                        break;
                    case 3:
                        currentTeam.ratioDefense = currentTeam.totalDefense / currentTeam.matches / avgDefense;
                    default:
                        break;
                }
            }
            currentTeam.ratioFoul = currentTeam.foulPoints / currentTeam.matches / avgFoul;
            currentTeam.favor = currentTeam.ratioAutoPowerScore + currentTeam.ratioTeleOpPowerScore + currentTeam.ratioEndgame + currentTeam.ratioDefense - 0.5 * currentTeam.ratioFoul;

            //currentTeam.favor -= 0.5 * currentTeam.foulPoints / currentTeam.matches / avgFoul;
            favoredTeam = currentTeam.favor > favoredTeam.favor ? currentTeam : favoredTeam;                    // Update our favored team if necessary
        }
        //System.out.println((double) favoredTeam.lowerPort / favoredTeam.matches / avgLowerPort);
        System.out.println("Favored team's favor is: " + favoredTeam.favor);
        return favoredTeam.number;
    }

    public static int[] indexOfSmallestThree(double[] array){

        // add this
        if (array.length == 0) 
            return null;

        int[] index = {-1, -1, -1};
        double[] m_array = array;

        for(int i = 0; i < index.length; i++) {                 // The tier of lessness
            for (int ii = 0; ii < m_array.length; ii++){          // The items to look through
                if(index[i] == -1) {
                    index[i] = ii;
                } else if(m_array[ii] < m_array[index[i]]) {
                    index[i] = ii;
                }
            }
            m_array[index[i]] = Integer.MAX_VALUE;


        }

        return index;
    }
}