package com.uhu.saluhud.models.user.test;

import com.uhu.saluhud.database.utils.models.user.SaluhudUser;
import com.uhu.saluhud.database.utils.models.user.SaluhudUserFitnessData;
import com.uhu.saluhud.database.utils.models.user.SaluhudUserPersonalData;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminUserFitnessDataRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminUserPersonalDataRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminUserRepository;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SaluhudUserTest {

    @Autowired
    private SaluhudAdminUserRepository saluhudUserRepository;

    @Autowired
    private SaluhudAdminUserFitnessDataRepository saluhudUserFitnessDataRepository;

    @Autowired
    private SaluhudAdminUserPersonalDataRepository saluhudPersonalDataRepository;

    @Test
    public void testUserCRUD() {

        SaluhudUserPersonalData userPersonalData = new SaluhudUserPersonalData("Saul", "Rodríguez", 123456789);
        SaluhudUserFitnessData userFitnessData = new SaluhudUserFitnessData(90, 170, "Men", 22, "Hectomorfo", 2, 8, 10000, 2100, "10%");
        SaluhudUser user = new SaluhudUser("SaulRC1", "1235", "saul@gmail.com", userPersonalData, userFitnessData);
        SaluhudUser user2 = new SaluhudUser("Juan2k", "1235", "juan@gmail.com");

        saluhudPersonalDataRepository.save(userPersonalData);
        saluhudUserFitnessDataRepository.save(userFitnessData);
        saluhudUserRepository.save(user);
        saluhudUserRepository.save(user2);
        saluhudUserRepository.delete(saluhudUserRepository.findByEmail("juan@gmail.com").orElseThrow());
        
        List<SaluhudUser> users = this.saluhudUserRepository.findAll();
        assertTrue(users.contains(user), "El usuario" + user.getUsername() + " no existe.");         
    }
}
