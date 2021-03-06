package elitefour.pokemonduel.moves;

import elitefour.pokemonduel.battle.Pokemon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class DamageDebuff extends DamageMove {
    
    private final Pokemon.Stat affectedStat;
    private final int effectChance, stages;
    
    public DamageDebuff(String name) {
        
        super(name);
        
        Pokemon.Stat tempStat = Pokemon.Stat.CRITICAL;
        int tempChance = -1;
        int tempStage = -1;
        
        try {
            File file = new File(Move.MOVE_DIRECTORY);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "";

            while (line != null && !line.equals(name))
                line = reader.readLine();

            if (line.equals(name)) {
                
                for (int j = 0; j < 5; j++)
                    reader.readLine();
                
                for (int i = 0; i < 3; i++) {
                    line = reader.readLine();
                    
                    switch (i) {
                        case 0:
                            tempStat = Pokemon.Stat.valueOf(
                                line.toUpperCase().replace(" ", "_")
                            );
                            break;
                        case 1:
                            tempChance = Integer.parseInt(line);
                            break;
                        case 2:
                            tempStage = Integer.parseInt(line);
                    }
                }
            }
        } catch (IOException error) {
            error.printStackTrace(System.out);
        }
        
        this.affectedStat = tempStat;
        this.effectChance = tempChance;
        this.stages = tempStage;
    }
    
    public boolean useSecondary(Pokemon user, Pokemon target) {
        
        Random rng = new Random();
        boolean result = false;
        
        if (rng.nextInt(100) < effectChance)
            result = applyEffect(target);
        
        return result;
    }
    
    private boolean applyEffect(Pokemon target) {
        return target.lowerStatStage(affectedStat, stages);
    }
    
    public int stages() {
        return stages;
    }
    
    public String affectedStat() {
        return affectedStat.name();
    }
    
    public String hitText(String name, boolean success) {
        
        String message = name + "'s " + 
                affectedStat().replace("_", " ").toLowerCase();
        
        // Affected stat stage is already at min.
        if (!success)
            return message + " won't go any lower!";

        // There's still room for change.
        else {

            switch (stages()) {
                case 1:
                    return message + " fell!";
                default: // case 2
                    return message + " fell sharply!";
            }
        }
    }
}
