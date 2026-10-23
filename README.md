# Array of Legends

An improved version of the java-game-character project.

## Added features

- **Three new character types:** Archer (25% critical hits, Triple Shot), Healer (Holy Light heals itself and damages a foe) and Tank (130 HP, 2 armor that reduces every hit, Iron Wall hits and halves the next damage it takes). Mage's Lightning Storm now hits every enemy.
- **GUI (Swing):** coloured theme, character list coloured by class, HP bars per fighter.
- **Create / View / Search / Update / Remove:** add with a name and class (Warrior, Mage, Archer, Healer or Tank), click a character to view it,
  filter with the search box (name or class), Rename the selected one, Remove it (with confirmation).
- **Input validation:** name cannot be empty, max 15 characters, letters/numbers/spaces only, no duplicate names,
  must select a character before renaming/removing, battle needs 2-6 fighters.
- **Battle Arena:** select 2-6 characters, press Start Battle, then Next Turn or Auto-play.
  The Fighters panel updates live as soon as you select characters. HP bars flash red when a fighter is hit. A battle log shows each action and the winner.

## How to run

Requirement: JDK 11 or newer.

Download as ZIP, extract the ZIP, open the project folder in your terminal, and run:

```bash
javac *.java
java Main
```

Or open the folder in any IDE and run `Main.java`.