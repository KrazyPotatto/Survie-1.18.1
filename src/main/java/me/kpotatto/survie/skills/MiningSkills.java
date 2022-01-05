package me.kpotatto.survie.skills;

public class MiningSkills extends Skills{

    public MiningSkills(int experience) {
        super(experience);
    }

    @Override
    public int levelFunction() {
        return (int)Math.sqrt(this.experience / 120f);
    }

}
