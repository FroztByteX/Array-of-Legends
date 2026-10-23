import java.awt.Color;
import java.util.List;
import java.util.Random;

abstract class GameCharacter {
    static final Random rng = new Random();

    String name;
    int hp, maxHp, power;
    int cooldown;			// turns until the special move can be used again
    int armor;				// flat damage reduction on every hit taken
    boolean shielded;		// if true, the next hit taken is halved

    public GameCharacter(String name, int maxHp, int power) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.power = power;
    }

    // Abstract methods +++++++++++++++++
    public abstract String attack(GameCharacter target);
    public abstract String specialMove(GameCharacter target, List<GameCharacter> all);
    public abstract String getType();
    public abstract Color getColor();

    // Concrete methods +++++++++++++++++
    public void showName() {
        System.out.println("Character: " + name);
    }

    int hit(GameCharacter target, double multiplier) {
        int dmg = (int) (power * multiplier) + rng.nextInt(5);
        dmg = Math.max(1, dmg - target.armor);
        if (target.shielded) {
            dmg = Math.max(1, dmg / 2);
            target.shielded = false;
        }
        target.hp = Math.max(0, target.hp - dmg);
        return dmg;
    }

    boolean isAlive() { return hp > 0; }
    boolean specialReady() { return cooldown == 0; }

    String useSpecial(GameCharacter target, List<GameCharacter> all) {
        cooldown = 3;
        return specialMove(target, all);
    }

    void endTurn() { if (cooldown > 0) cooldown--; }
    void resetForBattle() { hp = maxHp; cooldown = 0; shielded = false; }

    @Override
    public String toString() { return name + " (" + getType() + ")"; }
}
