
package com.uhu.saluhud.administrationportal.configuration.web;

import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.repositories.administrationportal.nutrition.AdministrationPortalAllergenicRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAllergenicConverter implements Converter<String[], Set<Allergenic>> {

    @Autowired
    private AdministrationPortalAllergenicRepository allergenicRepository;

    @Override
    public Set<Allergenic> convert(String[] source) {
        Set<Allergenic> allergenics = new HashSet<>();
        for (String idStr : source) {
            try {
                Long id = Long.valueOf(idStr);
                allergenicRepository.findById(id).ifPresent(allergenics::add);
            } catch (NumberFormatException e) {
                // Omitir si el ID no es válido
            }
        }
        return allergenics;
    }
}
