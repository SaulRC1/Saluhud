package com.uhu.saluhud.models.nutrition.test;

import com.uhu.saluhud.database.utils.models.nutrition.Allergenic;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.nutrition.SaluhudAdminAllergenicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

/**
 *
 * @author Juan Alberto Dominguez Vazquez
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/saluhud",
    "spring.datasource.username=saluhud_admin",
    "spring.datasource.password=adminUHU495",
    "spring.datasource.driver-class-name=org.postgresql.Driver"
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SaluhudAdminAllergenicsTest {

    @Autowired
    private SaluhudAdminAllergenicRepository allergenicRepository;
    
    @Test
    public void testAllergenicCRUD() {
        Allergenic pescado = new Allergenic("Pescado");
        Allergenic leche = new Allergenic("Leche");
        
        this.allergenicRepository.save(pescado);
        this.allergenicRepository.save(leche);
        this.allergenicRepository.delete(leche);
        Assert.isTrue(this.allergenicRepository.findByName("Pescado").getName().equals(pescado.getName()), "");
        Assert.isNull(this.allergenicRepository.findByName("Leche"), "");
    }
}
