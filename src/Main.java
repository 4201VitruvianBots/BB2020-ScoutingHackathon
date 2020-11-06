import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class Main {

    /*
        ============ READ ME ============
        BEFORE USE, PLEASE FAMILIARIZE YOURSELF WITH THE USER MANUAL TO PREVENT FROM INJURY.

        USER MANUAL:
        ENTER NUMBERS OF TEAMS THAT HAVE BEEN PICKED INTO THE ARRAY BELOW.

        EXAMPLE:
        static int[] alreadyPicked = {4201, 330, 294}
        (4201, 330, and 294 being the teams having been picked.)

        Have fun, stay safe, and probability says we shall never meet each other.
        So I guess I won't see you in that higher level of abstraction.
     */
    static int[] alreadyPicked = {4130, 3538, 1481, 548};

    static HashMap<Integer, Team> teamObjects = new HashMap<Integer, Team>();   // This HashMap stores Team objects as well as their numbers, so we can
                                                                                // access a team's object quickly, only knowing the number.

    static double weightOfAutoPoints, weightOfTeleOpPoints, weightOfEndgame, weightOfDefense;   // Subjective weight indexes
    static double weightOfFouls = 0.5; // How important are fouls compared to other game elements

    static int ourTeam;
    static double avgTeleOpPowerScore = 0, avgAutoPowerScore = 0, avgEndgame = 0, avgDefense = 0, avgFoul = 0;  // Averages per team per match

    public static boolean arrayContainsElement(int[] array, int element) {
        for (int i : array) {
            if (i == element) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {
        teamObjects.put(0, new Team(0));                        // A null team, just because

                                                                // Try block, because some people don't have hard drives
        try {
            System.out.print("Enter your team number: ");       // Getting the team number
            Scanner scanner = new Scanner(System.in);
            ourTeam = scanner.nextInt();

                                                                // Getting the subjective weights
            System.out.println("Enter numbers representing the importance of having the team that is best at the following game aspects");
            System.out.print("Autonomous power cells: ");
            weightOfAutoPoints = scanner.nextDouble();
            System.out.print("Tele-op power cells:    ");
            weightOfTeleOpPoints = scanner.nextDouble();
            System.out.print("Endgame:                ");
            weightOfEndgame = scanner.nextDouble();
            System.out.print("Defense:                ");
            weightOfDefense = scanner.nextDouble();

            File data = new File("src/data.CSV");               // Setting up to read the spreadsheet
            Scanner reader = new Scanner(data);
            reader.nextLine();

            while(reader.hasNextLine()) {                       // Loop through all the scouting entries
                Team team;
                String[] row = reader.nextLine().split(",");    // CSV splits on commas
                int teamNumber = Integer.parseInt(row[1]);

                if(teamObjects.containsKey(teamNumber)) {       // Get the team object if we've created it already, or create it
                    team = teamObjects.get(teamNumber);
                } else {
                    team = new Team(teamNumber);
                    teamObjects.put(teamNumber, team);
                }

                team.autoPowerScore += Integer.parseInt(row[4]) * 2;        // Parse the autonomous lower goal
                for(int i = 5; i < 10; i++) {                               // And upper goal
                    team.autoPowerScore += Integer.parseInt(row[i]) * 5;
                }

                team.teleOpPowerScore += Integer.parseInt(row[12]);         // Parse the tele-op lower goal
                for(int i = 13; i < 18; i++) {                              // And upper goal
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
                } // Gives points according to the endgame status

                if(row[23].equals("1") && row[22].equals("Climb")) {
                    team.endgamePoints += 10;
                } // And if they were level (and were responsible for that feat)

                team.fouls += Integer.parseInt(row[24]);            // Fouls
                team.fouls += Integer.parseInt(row[25]) * 5;        // Tech fouls

                team.matches++;                                     // Account for this match
            }

            System.out.println("\nWe suggest that you pick team " + pickTeam(ourTeam) + "\n");   // All of the major work, condensed down to one line!

        } catch(Exception e) {                                      // Again, people don't all have hard drives. So sad.
            System.out.println(e);
            e.printStackTrace();
        }




        // Loop through all the different entries
            // Create an array of columns for each entry
            // If the team already exists in the HashMap, continue, otherwise create it
            // Loop through all the different columns
                // Add data to their respective variables in the correct team object

    }

    // ALGORITHM FOR PICKING A TEAM
    // Find the averages for all the different fields of data
    // Find the three (or so) we're lacking in in terms of said average
    // Find the best teams with respect to said fields of data

    public static void calculateAverages() {                   // Calculate the averages per team per match (only teams that haven't been picked, excluding us)
        int teams = 0;
        double teleOpPowerScoreAvgs = 0, autoPowerScoreAvgs = 0, endgame = 0, defense = 0, foul = 0;

        for(int i : teamObjects.keySet()) {                     // Loop through all the teams
            if(arrayContainsElement(alreadyPicked, i) || i == 0)
                continue;
            
            teams++;

            Team currentTeam = teamObjects.get(i);

            teleOpPowerScoreAvgs += currentTeam.teleOpPowerScore / currentTeam.matches;     // Calculate the averages of each field for this team, add to sum
            autoPowerScoreAvgs += currentTeam.autoPowerScore / currentTeam.matches;
            endgame += currentTeam.endgamePoints / currentTeam.matches;
            defense += currentTeam.totalDefense / currentTeam.matches;
            foul += currentTeam.fouls / currentTeam.matches;
        }

        avgAutoPowerScore = autoPowerScoreAvgs / teams;                     // Divide the sums by the number of teams to get the overall averages
        avgTeleOpPowerScore = teleOpPowerScoreAvgs / teams;
        avgEndgame = endgame / teams;
        avgDefense = defense / teams;
        avgFoul = foul / teams;


                                                                            // I like numbers
        System.out.println("Averages:\nTPS: " + avgTeleOpPowerScore + "\nAPS: " + avgAutoPowerScore + "\nE:   " + avgEndgame + "\nD:   " + avgDefense + "\nF-:  " + avgFoul);
    }

    public static int pickTeam(int ourTeam) {                               // Pick the best team for us
        calculateAverages();                                    // Calculate the averages

        Team ourTeamObject = teamObjects.get(ourTeam);          // Our team

        ourTeamObject.ratioAutoPowerScore = ourTeamObject.autoPowerScore / ourTeamObject.matches / avgAutoPowerScore;       // Calculate the ratio between our team's average and the average team's average
        ourTeamObject.ratioTeleOpPowerScore = ourTeamObject.teleOpPowerScore / ourTeamObject.matches / avgTeleOpPowerScore;
        ourTeamObject.ratioEndgame = ourTeamObject.endgamePoints / ourTeamObject.matches / avgEndgame;
        ourTeamObject.ratioDefense = ourTeamObject.totalDefense / ourTeamObject.matches / avgDefense;

                                    // Numbers again
        System.out.println("Ratios:\nTPS: " + ourTeamObject.ratioTeleOpPowerScore + "\nAPS: " + ourTeamObject.ratioAutoPowerScore + "\nE:   " 
        + ourTeamObject.ratioEndgame + "\nD:   " + ourTeamObject.ratioDefense);

//        double[] ratios = {ourTeamObject.ratioTeleOpPowerScore, ourTeamObject.ratioAutoPowerScore, ourTeamObject.ratioEndgame, ourTeamObject.ratioDefense};

        return findMatch(ourTeamObject);                // Again, all the heavy lifting in one line! Amazing!
    }

    public static int findMatch(Team ourTeamObject) {               // Our world-class matchmakers will find the perfect match for you. Here is their brains:
        Team favoredTeam = teamObjects.get(0);                      // By default, the null team is the best.
        String logDump = "";                                // For those people that like numbers, we're going to dump to a log

        for(int i : teamObjects.keySet()) {
            if(arrayContainsElement(alreadyPicked, i) || i == ourTeam || i == 0)        // Skip the teams we honestly don't care about
                continue;

            Team currentTeam = teamObjects.get(i);                                       // Make sure favor value is reset

            currentTeam.ratioTeleOpPowerScore = currentTeam.teleOpPowerScore / currentTeam.matches / avgTeleOpPowerScore; // Give each team object their ratios
            currentTeam.ratioAutoPowerScore = currentTeam.autoPowerScore / currentTeam.matches / avgAutoPowerScore;
            currentTeam.ratioEndgame = currentTeam.endgamePoints / currentTeam.matches / avgEndgame;
            currentTeam.ratioDefense = currentTeam.totalDefense / currentTeam.matches / avgDefense;
            currentTeam.ratioFoul = currentTeam.fouls / currentTeam.matches / avgFoul;

            /*if(avgFoul == 0) {
                currentTeam.ratioFoul = 0;
            } else {
                currentTeam.ratioFoul = currentTeam.fouls / currentTeam.matches / avgFoul;
            }*/
            currentTeam.favor =                                     // Calculate a favorability index based on the ratios, the inverse of our ratios, and the subjective nonsense.
                weightOfAutoPoints * currentTeam.ratioAutoPowerScore / Math.sqrt(ourTeamObject.ratioAutoPowerScore)
                + weightOfTeleOpPoints * currentTeam.ratioTeleOpPowerScore / Math.sqrt(ourTeamObject.ratioTeleOpPowerScore)
                + weightOfEndgame * currentTeam.ratioEndgame / Math.sqrt(ourTeamObject.ratioEndgame)
                + weightOfDefense * currentTeam.ratioDefense / Math.sqrt(ourTeamObject.ratioDefense)
                - weightOfFouls * currentTeam.ratioFoul;

            logDump += "\n\nTeam " + i + ": " + currentTeam.favor;          // The garbage to dump
            logDump += "\n\tTPS: " + currentTeam.ratioTeleOpPowerScore;
            logDump += "\n\tAPS: " + currentTeam.ratioAutoPowerScore;
            logDump += "\n\tE:   " + currentTeam.ratioEndgame;
            logDump += "\n\tD:   " + currentTeam.ratioDefense;
            logDump += "\n\tF-:  " + currentTeam.ratioFoul;
            
            //currentTeam.favor -= 0.5 * currentTeam.foulPoints / currentTeam.matches / avgFoul;
            favoredTeam = currentTeam.favor > favoredTeam.favor ? currentTeam : favoredTeam;                    // Update our favored team if necessary
        }
        //System.out.println((double) favoredTeam.lowerPort / favoredTeam.matches / avgLowerPort);

        try {                                                           // Seriously, you should get a hard drive.
            FileWriter log = new FileWriter("src/log.fif");
            log.write(logDump);
            log.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Favored team's favor is: " + favoredTeam.favor);
        return favoredTeam.number;                                              // The answer
    }

    /*public static int[] indexOfSmallestThree(double[] array){

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
    }*/
}