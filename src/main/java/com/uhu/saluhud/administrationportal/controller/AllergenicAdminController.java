package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhud.administrationportal.dto.nutrition.AllergenicDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.IngredientDTO;
import com.uhu.saluhud.administrationportal.service.localization.NutritionLocaleService;
import com.uhu.saluhud.localization.NutritionLocaleProvider;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author juald
 */
@Controller
@RequestMapping("/allergenic")
public class AllergenicAdminController
{

    @Autowired
    private AdministrationPortalAllergenicService allergenicService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;

    @Autowired
    private NutritionLocaleService nutritionLocaleService;

    @GetMapping("/home")
    public ModelAndView getAlergens(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/allergenicHome");
        Page<Allergenic> alergenPage = allergenicService.getAlergens(page, size);

        Set<AllergenicDTO> listAllergensDTO = new HashSet();
        List<IngredientDTO> listIngredientDTO = new ArrayList<>();
        alergenPage.forEach((allergen) ->
        {
            String allergenName = nutritionLocaleProvider.getTranslation(allergen.getName(),
                    NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX, locale);

            List<Ingredient> ingredients = allergen.getIngredients();
            ingredients.forEach((ingredient) ->
            {
                String ingredientName = nutritionLocaleProvider.getTranslation(
                        ingredient.getName(), NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX, locale);
                IngredientDTO ingredientDTO = new IngredientDTO(ingredient.getId(),
                        ingredientName);
                listIngredientDTO.add(ingredientDTO);
            });

            AllergenicDTO allergenicDTO = new AllergenicDTO(allergen.getId(), allergenName,
                    listIngredientDTO);
            listAllergensDTO.add(allergenicDTO);
        });

        modelAndView.addObject("allergenics", listAllergensDTO);
        modelAndView.addObject("currentPage", alergenPage.getNumber());
        modelAndView.addObject("totalPages", alergenPage.getTotalPages());
        return modelAndView;
    }

    // Mostrar formulario para editar alergeno
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/editAlergen");
        Allergenic allergenic = allergenicService.findById(id);

        // Mostrar la traducción del nombre
        String translatedName = nutritionLocaleProvider.getTranslation(
                allergenic.getName(),
                NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                locale
        );
        allergenic.setName(translatedName);

        modelAndView.addObject("allergenic", allergenic);
        return modelAndView;
    }

    // Guardar edición de alergeno
    @PostMapping("/edit")
    public ModelAndView updateAllergenic(@ModelAttribute Allergenic allergenic,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/editAlergen");

        try
        {
            // 1. Determinar la clave de traducción
            String baseKey = "allergenic.";
            String key = baseKey + allergenic.getId();

            // 2. Guardar la traducción en los archivos de propiedades
            List<Locale> supportedLocales = List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en"));
            for (Locale lang : supportedLocales)
            {
                nutritionLocaleService.addKeyToTranslationBundle(
                        key,
                        allergenic.getName(), // Nombre recibido en el idioma del formulario
                        lang,
                        NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX
                );
            }

            // 3. Asignar la clave al objeto persistido
            allergenic.setName(key);

            // 4. Guardar en BD
            allergenicService.updateAllergenic(allergenic);

            String successMessage = messageSource.getMessage("allergen.updated.success", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (IOException | RuntimeException e)
        {
            String errorMessage = messageSource.getMessage("allergen.updated.error", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView showAllergenicDetails(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("allergenic/details");
        Allergenic allergenic = allergenicService.findById(id);

        String allergenName = nutritionLocaleProvider.getTranslation(allergenic.getName(),
                NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX, locale);

        List<IngredientDTO> listIngredientDTO = new ArrayList<>();
        List<Ingredient> ingredients = allergenic.getIngredients();
        ingredients.forEach((ingredient) ->
        {
            String ingredientName = nutritionLocaleProvider.getTranslation(
                    ingredient.getName(), NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX, locale);
            IngredientDTO ingredientDTO = new IngredientDTO(ingredient.getId(),
                    ingredientName);
            listIngredientDTO.add(ingredientDTO);
        });

        AllergenicDTO allergenicDTO = new AllergenicDTO(allergenic.getId(), allergenName,
                listIngredientDTO);

        modelAndView.addObject("allergenic", allergenicDTO);
        return modelAndView;
    }

}
