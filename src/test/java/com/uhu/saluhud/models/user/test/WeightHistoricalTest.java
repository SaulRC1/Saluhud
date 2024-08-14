package com.uhu.saluhud.models.user.test;

import com.uhu.saluhud.database.utils.models.user.SaluhudUser;
import com.uhu.saluhud.database.utils.models.user.WeightHistorical;
import com.uhu.saluhud.database.utils.models.user.WeightHistoricalEntry;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminUserRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminWeightHistoricalEntryRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminWeightHistoricalRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class WeightHistoricalTest {

    @Autowired
    private SaluhudAdminWeightHistoricalRepository weightHistoricalRepository;

    @Autowired
    private SaluhudAdminWeightHistoricalEntryRepository weightHistoricalEntryRepository;

    @Autowired
    private SaluhudAdminUserRepository saluhudUserRepository;

    @Test
    public void testWeightHistoricalCRUD() {

        WeightHistorical weightHistorical = new WeightHistorical();
        LocalDate now = LocalDate.now();

        List<WeightHistoricalEntry> entries = new ArrayList<>();
        WeightHistoricalEntry entry = new WeightHistoricalEntry(now, 75, 181, 2200, weightHistorical);
        entries.add(entry);

        weightHistorical.setEntries(entries);
        
        SaluhudUser user1 = new SaluhudUser("Juan2k", "1235", "juan@gmail.com");
        saluhudUserRepository.save(user1);

        SaluhudUser user = saluhudUserRepository.findByEmail("juan@gmail.com").orElseThrow();
        weightHistorical.setUser(user);

        saluhudUserRepository.save(user);
        weightHistoricalRepository.save(weightHistorical);
        weightHistoricalEntryRepository.save(entry);
    }
}
