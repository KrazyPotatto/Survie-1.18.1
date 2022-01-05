package me.kpotatto.survie.skills;

public abstract class Skills {

    protected int experience;

    public Skills(int experience){
        this.experience = experience;
    }

    public abstract int levelFunction();

    public int getLevel() {
        return levelFunction();
    }

    public void addExperience(int amount){
        setExperience(getExperience() + amount);
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
