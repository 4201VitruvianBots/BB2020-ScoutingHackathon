import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    static HashMap<Integer, Team> teamObjects = new HashMap<Integer, Team>();

    static int[] alreadyPicked = {4768, 5498, 7865};

    static int ourTeam = 3096;


    public static void main(String args[]) {


        int[] testArray = {-5, -5, 3, 63, -2, 0, 1};
        for (int i : indexOfSmallestThree(testArray))
            System.out.println(i);

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

                team.autoLowerPort += Integer.parseInt(row[4]);
                for(int i = 5; i < 10; i++) {
                    team.autoUpperPort += Integer.parseInt(row[i]);
                }

                team.lowerPort += Integer.parseInt(row[12]);
                for(int i = 13; i < 18; i++) {
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

    public static void pickTeam(int ourTeam) {
        int matches = 0;
        double lowerPort = 0, upperPort = 0, autoLowerPort = 0, autoUpperPort = 0, endgame = 0;
        double avgLowerPort = 0, avgUpperPort = 0, avgAutoLowerPort = 0, avgAutoUpperPort = 0, avgEndgame = 0;
        double ratioLowerPort = 0, ratioUpperPort = 0, ratioAutoLowerPort = 0, ratioAutoUpperPort = 0, ratioEndgame = 0;

        for(int i : teamObjects.keySet()) {
            if(Arrays.asList(alreadyPicked).contains(i))
                continue;

            Team currentTeam = teamObjects.get(i);

            matches += currentTeam.matches;
            lowerPort += currentTeam.lowerPort;
            upperPort += currentTeam.upperPort;
            autoLowerPort += currentTeam.autoLowerPort;
            autoUpperPort += currentTeam.autoUpperPort;
            endgame += currentTeam.endgamePoints;
        }

        avgAutoLowerPort = autoLowerPort / matches;
        avgAutoUpperPort = autoUpperPort / matches;
        avgLowerPort = lowerPort / matches;
        avgUpperPort = upperPort / matches;
        avgEndgame = endgame / matches;

        ratioLowerPort = teamObjects.get(ourTeam).lowerPort / avgLowerPort;
        ratioUpperPort = teamObjects.get(ourTeam).upperPort / avgUpperPort;
        ratioAutoLowerPort = teamObjects.get(ourTeam).autoLowerPort / avgAutoLowerPort;
        ratioAutoUpperPort = teamObjects.get(ourTeam).autoUpperPort / avgAutoUpperPort;
        ratioEndgame = teamObjects.get(ourTeam).endgamePoints / avgEndgame;

        double[] ratios = {ratioLowerPort, ratioUpperPort, ratioAutoLowerPort, ratioAutoUpperPort, ratioEndgame};

    }

    public static int[] indexOfSmallestThree(int[] array){

        // add this
        if (array.length == 0) 
            return null;

        int[] index = {-1, -1, -1};
        int[] m_array = array;

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