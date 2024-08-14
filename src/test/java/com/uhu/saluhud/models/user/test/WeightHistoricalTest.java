package com.uhu.saluhud.models.user.test;

import com.uhu.saluhud.database.utils.models.user.SaluhudUser;
import com.uhu.saluhud.database.utils.models.user.WeightHistorical;
import com.uhu.saluhud.database.utils.models.user.WeightHistoricalEntry;
import com.uhu.saluhud.database.utils.services.saluhud.admin.user.SaluhudAdminUserService;
import com.uhu.saluhud.database.utils.services.saluhud.admin.user.SaluhudAdminWeightHistoricalEntryService;
import com.uhu.saluhud.database.utils.services.saluhud.admin.user.SaluhudAdminWeightHistoricalService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Juan Alberto Dominguez Vazquez
 */
public class WeightHistoricalTest {

    @Autowired
    private SaluhudAdminWeightHistoricalService weightHistoricalService;

    @Autowired
    private SaluhudAdminWeightHistoricalEntryService weightHistoricalEntryService;

    @Autowired
    private SaluhudAdminUserService saluhudUserService;

    @Test
    public void testWeightHistoricalCRUD() {

        WeightHistorical weightHistorical = new WeightHistorical();
        LocalDate now = LocalDate.now();

        List<WeightHistoricalEntry> entries = new ArrayList<>();
        WeightHistoricalEntry entry = new WeightHistoricalEntry(now, 75, 181, 2200, weightHistorical);
        entries.add(entry);

        weightHistorical.setEntries(entries);

        SaluhudUser user = saluhudUserService.getUserById(7);
        weightHistorical.setUser(user);

        saluhudUserService.saveUser(user);
        weightHistoricalService.saveWeightHistorical(weightHistorical);
        weightHistoricalEntryService.saveWeightHistoricalEntry(entry);
    }
}
