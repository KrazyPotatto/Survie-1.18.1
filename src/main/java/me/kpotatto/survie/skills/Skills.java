package me.kpotatto.survie.skills;

import org.bukkit.OfflinePlayer;

public abstract class Skills {

    protected int experience;
    protected int level;
    protected OfflinePlayer player;

    public Skills(int experience, OfflinePlayer p){
        this.experience = experience;
        this.level = getLevel();
        this.player = p;
    }

    public abstract int levelFunction(int exp);
    public abstract int expForLevel(int level);
    public abstract void levelUP();

    public int getLevel() {
        return levelFunction(this.experience);
    }

    public void addExperience(int amount){
        if(getLevel() >= 30) return;
        experience += amount;
        if(level < getLevel()){
            level = getLevel();
            levelUP();
        }
    }

    public void addExperience(){
        if(getLevel() >= 30) return;
        experience++;
        if(level < getLevel()){
            level = getLevel();
            levelUP();
        }
    }

    public void removeExperience(int amount){
        setExperience(getExperience() - amount);
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
