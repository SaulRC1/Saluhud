package com.uhu.saluhud.mobileapp.dto.user;

import com.uhu.saluhuddatabaseutils.models.user.BodyComposition;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class SaluhudUserFitnessDataDTO 
{
    private double weight;

    private double height;

    private String biologicalSex;

    private int age;

    private BodyComposition bodyComposition;
    
    private int recommendedDailyWaterLiters;

    private int recommendedSleepHours;

    private int recommendedDailySteps;

    private int dailyKilocaloriesObjective;

    private double bodyMassIndex;
    
    private float activityFactor;

    public SaluhudUserFitnessDataDTO(double weight, double height, String biologicalSex, 
            int age, BodyComposition bodyComposition, int recommendedDailyWaterLiters, int recommendedSleepHours, 
            int recommendedDailySteps, int dailyKilocaloriesObjective, double bodyMassIndex, 
            float activityFactor)
    {
        this.weight = weight;
        this.height = height;
        this.biologicalSex = biologicalSex;
        this.age = age;
        this.bodyComposition = bodyComposition;
        this.recommendedDailyWaterLiters = recommendedDailyWaterLiters;
        this.recommendedSleepHours = recommendedSleepHours;
        this.recommendedDailySteps = recommendedDailySteps;
        this.dailyKilocaloriesObjective = dailyKilocaloriesObjective;
        this.bodyMassIndex = bodyMassIndex;
        this.activityFactor = activityFactor;
    }

    public SaluhudUserFitnessDataDTO()
    {
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public String getBiologicalSex()
    {
        return biologicalSex;
    }

    public void setBiologicalSex(String biologicalSex)
    {
        this.biologicalSex = biologicalSex;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public BodyComposition getBodyComposition()
    {
        return bodyComposition;
    }

    public void setBodyComposition(BodyComposition bodyComposition)
    {
        this.bodyComposition = bodyComposition;
    }

    public int getRecommendedDailyWaterLiters()
    {
        return recommendedDailyWaterLiters;
    }

    public void setRecommendedDailyWaterLiters(int recommendedDailyWaterLiters)
    {
        this.recommendedDailyWaterLiters = recommendedDailyWaterLiters;
    }

    public int getRecommendedSleepHours()
    {
        return recommendedSleepHours;
    }

    public void setRecommendedSleepHours(int recommendedSleepHours)
    {
        this.recommendedSleepHours = recommendedSleepHours;
    }

    public int getRecommendedDailySteps()
    {
        return recommendedDailySteps;
    }

    public void setRecommendedDailySteps(int recommendedDailySteps)
    {
        this.recommendedDailySteps = recommendedDailySteps;
    }

    public int getDailyKilocaloriesObjective()
    {
        return dailyKilocaloriesObjective;
    }

    public void setDailyKilocaloriesObjective(int dailyKilocaloriesObjective)
    {
        this.dailyKilocaloriesObjective = dailyKilocaloriesObjective;
    }

    public double getBodyMassIndex()
    {
        return bodyMassIndex;
    }

    public void setBodyMassIndex(double bodyMassIndex)
    {
        this.bodyMassIndex = bodyMassIndex;
    }

    public float getActivityFactor()
    {
        return activityFactor;
    }

    public void setActivityFactor(float activityFactor)
    {
        this.activityFactor = activityFactor;
    }
    
}
