package elitefour.pokemonduel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Buff extends StatusMove {
    
    private final ArrayList<Pokemon.Stat> affectedStats = new ArrayList<>();
    private final int stages;
    
    public Buff(String name) {
        
        super(name);
        
        String tempStats = "";
        int tempStage = -1;
        
        try {
            File file = new File("resources\\data\\moves.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "";

            while (line != null && !line.equals(name))
                line = reader.readLine();

            if (line.equals(name)) {
                
                for (int j = 0; j < 5; j++)
                    reader.readLine();
                
                for (int i = 0; i < 2; i++) {
                    line = reader.readLine();
                    
                    switch (i) {
                        case 0:
                            tempStats = line;
                            break;
                        case 1:
                            tempStage = Integer.parseInt(line);
                            break;
                    }
                }
            }
        } catch (IOException error) {
            error.printStackTrace(System.out);
        }
        
        String[] statsArray = tempStats.split("/");
        
        for (String stat : statsArray)
            this.affectedStats.add(Pokemon.Stat.valueOf(stat.toUpperCase()));
                 
        this.stages = tempStage;
    }
    
    @Override
    public int use(Pokemon user, Pokemon target) {
        
        deductPP(1);
        
        if (affectedStats.size() == 1) {
            
            if (user.raiseStatStage(affectedStats.get(0), stages))
                return 1;
            
            else
                return 0;
        }
        
        else { // affectedStats.size() == 2
            
            boolean first = user.raiseStatStage(affectedStats.get(0), stages);
            boolean second = user.raiseStatStage(affectedStats.get(1), stages);
            
            if (first && second)
                return 1;
            else if (!first && second)
                return 2;
            else if (first && !second)
                return 3;
            else
                return 0;
        }
    }
    
    public int stages() {
        return stages;
    }
    
    public ArrayList<Pokemon.Stat> affectedStats() {
        return affectedStats;
    }
    
    public ArrayList<String> hitText(String name, ArrayList<Boolean> success) {
        
        ArrayList<String> result = new ArrayList<>();
        
        for (int i = 0; i < affectedStats.size(); i++) {
            
            String message = name + "'s " + 
                    affectedStats.get(i).name().replace("_", " ").toLowerCase();
        
            // Affected stat stage is already at max.
            if (!success.get(i))
                result.add(message + " won't go any higher!");

            // There's still room for change.
            else {

                switch (stages()) {
                    case 1:
                        result.add(message + " rose!");
                        break;
                    case 2:
                        result.add(message + " rose sharply!");
                        break;
                }
            }
        }
        
        return result;
    }
}