import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static HashMap<Integer, Team> teamObjects; 


    public static void main(String args[]) {

        int ourTeam = 3096;

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
                    case "None":
                        break;
                    case "Light":
                        team.totalDefense++;
                        break;
                    case "Heavy":
                        team.totalDefense += 2;
                        break;
                    default:
                        break;
                } //Gives points according to the amount of defense that the team had

                team.matches++;
            }

        } catch(Exception e) {
            System.out.println(e);
        }



        for(Team i : teamObjects) {
            return(lowerPort + (2.5*upperPort) + (2*autoLowerPort) + (5*autoUpperPort) //Calculates an average score for the entire performance of the match
        }

        // Loop through all the different entries
            // Create an array of columns for each entry
            // If the team already exists in the HashMap, continue, otherwise create it
            // Loop through all the different columns
                // Add data to their respective variables in the correct team object

    }

    
}