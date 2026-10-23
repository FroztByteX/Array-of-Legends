import java.awt.Color;
import java.util.List;

class Archer extends GameCharacter {
    public Archer(String name) { 
    	super(name, 100, 15); 
    }

    // 25% chance of a critical hit
    public String attack(GameCharacter t) {
        boolean crit = rng.nextInt(100) < 25;
        return name + " shoots " + t.name + " for " + hit(t, crit ? 2.0 : 1.0) + " damage!" + (crit ? " CRITICAL!" : "");
    }
    public String specialMove(GameCharacter t, List<GameCharacter> all) {
        int total = hit(t, 0.7) + hit(t, 0.7) + hit(t, 0.7);
        return name + " uses Triple Shot on " + t.name + " for " + total + " total damage!";
    }
    public String getType() { return "Archer"; }
    public Color getColor() { return new Color(0x27AE60); }
}