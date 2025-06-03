package com.uhu.saluhud.mobileapp.dto.user;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
public class SaveSaluhudUserFitnessDataDTO 
{
    @DecimalMin("0")
    @NotNull
    private Float weight;
    
    @DecimalMin("0")
    @NotNull
    private Float height;
    
    @Range(min=16, max=110)
    @NotNull
    private Integer age;
    
    @NotBlank
    private String biologicalSex;
    
    @NotBlank
    private String fitnessTarget;
    
    @DecimalMin("1.2")
    @DecimalMax("2.3")
    @NotNull
    private Float activityFactor;
    
    private Float leanBodyMassPercentage;
    
    private Float bodyFatPercentage;

    private Float bodyFatWeight;

    private Float leanBodyMassWeight;

    public SaveSaluhudUserFitnessDataDTO(Float weight, Float height, Integer age, String biologicalSex, String fitnessTarget, Float activityFactor, Float leanBodyMassPercentage, Float bodyFatPercentage, Float bodyFatWeight, Float leanBodyMassWeight)
    {
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.biologicalSex = biologicalSex;
        this.fitnessTarget = fitnessTarget;
        this.activityFactor = activityFactor;
        this.leanBodyMassPercentage = leanBodyMassPercentage;
        this.bodyFatPercentage = bodyFatPercentage;
        this.bodyFatWeight = bodyFatWeight;
        this.leanBodyMassWeight = leanBodyMassWeight;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getBiologicalSex()
    {
        return biologicalSex;
    }

    public void setBiologicalSex(String biologicalSex)
    {
        this.biologicalSex = biologicalSex;
    }

    public float getActivityFactor()
    {
        return activityFactor;
    }

    public void setActivityFactor(float activityFactor)
    {
        this.activityFactor = activityFactor;
    }

    public Float getLeanBodyMassPercentage()
    {
        return leanBodyMassPercentage;
    }

    public void setLeanBodyMassPercentage(Float leanBodyMassPercentage)
    {
        this.leanBodyMassPercentage = leanBodyMassPercentage;
    }

    public Float getBodyFatPercentage()
    {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Float bodyFatPercentage)
    {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Float getBodyFatWeight()
    {
        return bodyFatWeight;
    }

    public void setBodyFatWeight(Float bodyFatWeight)
    {
        this.bodyFatWeight = bodyFatWeight;
    }

    public Float getLeanBodyMassWeight()
    {
        return leanBodyMassWeight;
    }

    public void setLeanBodyMassWeight(Float leanBodyMassWeight)
    {
        this.leanBodyMassWeight = leanBodyMassWeight;
    }

    public String getFitnessTarget()
    {
        return fitnessTarget;
    }

    public void setFitnessTarget(String fitnessTarget)
    {
        this.fitnessTarget = fitnessTarget;
    }
}
