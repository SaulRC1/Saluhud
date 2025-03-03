package com.uhu.saluhud.mobileapp.enums;

/**
 * Private claims that must be shared between the Saluhud's mobile app and the
 * backend.
 * 
 * @author Saúl Rodríguez Naranjo
 */
public enum JWTSaluhudPrivateClaim
{
    /**
     * The "salusr" (Saluhud user) claim identifies the user for which the JWT
     * has been issued for.
     */
    SALUHUD_USERNAME("salusr");
    
    private final String claimName;
    
    private JWTSaluhudPrivateClaim(String claimName)
    {
        this.claimName = claimName;
    }

    public String getClaimName()
    {
        return claimName;
    }
}
