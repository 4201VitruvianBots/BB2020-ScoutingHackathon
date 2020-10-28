import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    static HashMap<Integer, Team> teamObjects;


    public static void main(String args[]) {

        System.out.println(indexOfSmallestThree([6, 7, 2, 8, 4, 5]));

        /*int ourTeam = 3096;
        int[] alreadyPicked = [4768, 5498, 7865];

        int lightDefenseDeficit, heavyDefenseDeficit;

        try {
            File data = new File("data.CSV");
            Scanner reader = new Scanner(data);
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

                team.autoLowerPort += Integer.parseInt(row[3]);
                for(int i = 4; i < 9; i++) {
                    team.autoUpperPort += Integer.parseInt(row[i]);
                }

                team.lowerPort += Integer.parseInt(row[11]);
                for(int i = 12; i < 17; i++) {
                    team.upperPort += Integer.parseInt(row[i]);
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

                team.matches++;
            }

        } catch(Exception e) {
            System.out.println(e);
        }*/




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

    public static void pickTeam(int ourTeam) {
        int matches;
        double lowerPort, upperPort, autoLowerPort, autoUpperPort, endgame;
        double avgLowerPort, avgUpperPort, avgAutoLowerPort, avgAutoUpperPort, avgEndgame;
        double ratioLowerPort, ratioUpperPort, ratioAutoLowerPort, ratioAutoUpperPort, ratioEndgame;

        for(Team i : teamObjects) {
            if(alreadyPicked.contains(i.getKey()))
                continue;

            matches += i.matches;
            lowerPort += i.lowerPort;
            upperPort += i.upperPort;
            autoLowerPort += i.autoLowerPort;
            autoUpperPort += i.autoUpperPort;
            endgame += i.endgamePoints;
        }

        avgAutoLowerPort = autoLowerPort / matches;
        avgAutoUpperPort = autoUpperPort / matches;
        avgLowerPort = lowerPort / matches;
        avgUpperPort = upperPort / matches;
        avgEndgame = endgame / matches;

        ratioLowerPort = teamObjects[ourTeam].lowerPort / avgLowerPort;
        ratioUpperPort = teamObjects[ourTeam].upperPort / avgUpperPort;
        ratioAutoLowerPort = teamObjects[ourTeam].autoLowerPort / avgAutoLowerPort;
        ratioAutoUpperPort = teamObjects[ourTeam].autoUpperPort / avgAutoUpperPort;
        ratioEndgame = teamObjects[ourTeam].endgamePoints / avgEndgame;

        double[] ratios = [ratioLowerPort, ratioUpperPort, ratioAutoLowerPort, ratioAutoUpperPort, ratioEndgame];

    }

    public static int[] indexOfSmallestThree(int[] array){

        // add this
        if (array.length == 0)
            return -1;

        int[] index = [0, null, null];
        int min = array[index];

        for(int i = 0; i < min.length; i++) {
            for (int ii = 0; i < array.length; ii++){
                if (array[ii] <= min && !index.includes(ii)){
                    min = array[ii];
                    index[i] = ii;
                }
            }

        }
        return index;
    }
}