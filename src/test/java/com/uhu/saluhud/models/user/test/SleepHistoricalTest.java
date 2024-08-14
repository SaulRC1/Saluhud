package com.uhu.saluhud.models.user.test;

import com.uhu.saluhud.database.utils.models.user.HistoricalEvaluation;
import com.uhu.saluhud.database.utils.models.user.SaluhudUser;
import com.uhu.saluhud.database.utils.models.user.SleepHistorical;
import com.uhu.saluhud.database.utils.models.user.SleepHistoricalEntry;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminSleepHistoricalEntryRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminSleepHistoricalRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminUserRepository;
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
public class SleepHistoricalTest {

    @Autowired
    private SaluhudAdminSleepHistoricalRepository sleepHistoricalRepository;

    @Autowired
    private SaluhudAdminSleepHistoricalEntryRepository sleepHistoricalEntryRepository;

    @Autowired
    private SaluhudAdminUserRepository saluhudUserRepository;

    @Test
    public void testSleepHistoricalCRUD() {

        SleepHistorical sleepHistorical = new SleepHistorical();
        LocalDate now = LocalDate.now();
        HistoricalEvaluation sleepEvaluation = HistoricalEvaluation.MINIMUM;

        List<SleepHistoricalEntry> entries = new ArrayList<>();
        SleepHistoricalEntry entry = new SleepHistoricalEntry(now, 6, 23, sleepEvaluation, sleepHistorical);
        entries.add(entry);

        sleepHistorical.setEntries(entries);
        SaluhudUser user1 = new SaluhudUser("Juan2k", "1235", "juan@gmail.com");
        SaluhudUser user2 = new SaluhudUser("Juan2k2", "1236", "juan2@gmail.com");
        saluhudUserRepository.save(user1);
        saluhudUserRepository.save(user2);
        
        SaluhudUser user = saluhudUserRepository.findByEmail("juan2@gmail.com").orElseThrow();
        sleepHistorical.setUser(user);

        //saluhudUserRepository.save(user);
        sleepHistoricalRepository.save(sleepHistorical);
        sleepHistoricalEntryRepository.save(entry);
    }
}
