import java.awt.Color;
import java.util.List;

class Mage extends GameCharacter {
    public Mage(String name) { super(name, 100, 18); }

    public String attack(GameCharacter t) {
        return name + " casts a fireball at " + t.name + " for " + hit(t, 1.0) + " damage!";
    }
    // Hits every other living fighter
    public String specialMove(GameCharacter t, List<GameCharacter> all) {
        StringBuilder sb = new StringBuilder(name + " uses Lightning Storm! Hits:");
        for (GameCharacter c : all) {
            if (c != this && c.isAlive()) sb.append(" ").append(c.name).append("(").append(hit(c, 0.8)).append(")");
        }
        return sb.toString();
    }
    public String getType()  { return "Mage"; }
    public Color getColor()  { return new Color(0x9B59B6); }
}