package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhud.administrationportal.dto.nutrition.RecipeDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.RecipeElaborationStepDTO;
import com.uhu.saluhud.administrationportal.service.localization.NutritionLocaleService;
import com.uhu.saluhud.localization.NutritionLocaleProvider;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeElaborationStep;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeElaborationStepService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalRecipeService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author juald
 */
@Controller
@RequestMapping("/elaborationSteps")
public class ElaborationStepsAdminController
{

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdministrationPortalRecipeElaborationStepService elaborationStepService;

    @Autowired
    private AdministrationPortalRecipeService recipeService;

    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;

    @Autowired
    private NutritionLocaleService nutritionLocaleService;

    @GetMapping("/home")
    public ModelAndView getElaborationSteps(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/elaborationStepsHome");
        Page<RecipeElaborationStep> elaborationStepPage = elaborationStepService.getElaborationSteps(page, size);

        List<RecipeElaborationStepDTO> listRecipeElaborationStepDTO = new ArrayList<>();
        elaborationStepPage.forEach((recipeElaborationStep) ->
        {
            String stepDescription = nutritionLocaleProvider.getTranslation(recipeElaborationStep.getStepDescription(),
                    NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX, locale);

            Recipe recipe = recipeElaborationStep.getRecipe();

            String recipeName = nutritionLocaleProvider.getTranslation(
                    recipe.getName(),
                    NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                    locale
            );

            RecipeDTO recipeDTO = new RecipeDTO(recipe.getId(), recipeName);

            RecipeElaborationStepDTO recipeElaborationStepDTO = new RecipeElaborationStepDTO(recipeElaborationStep.getId(),
                    stepDescription,
                    recipeElaborationStep.getStepNumber(),
                    recipeDTO);
            listRecipeElaborationStepDTO.add(recipeElaborationStepDTO);
        });

        modelAndView.addObject("elaborationSteps", listRecipeElaborationStepDTO);
        modelAndView.addObject("currentPage", elaborationStepPage.getNumber());
        modelAndView.addObject("totalPages", elaborationStepPage.getTotalPages());
        return modelAndView;
    }

    // Mostrar formulario para crear un nuevo paso
    @GetMapping("/create")
    public ModelAndView showCreateForm(Locale locale)
    {
        List<Recipe> recipes = recipeService.findAllRecipes();
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/createElaborationStep");

        // Traducimos los nombres de las recetas y los pasamos como Map<id, nombre traducido>
        Map<Long, String> translatedRecipes = recipes.stream()
                .collect(Collectors.toMap(
                        Recipe::getId,
                        recipe -> nutritionLocaleProvider.getTranslation(
                                recipe.getName(),
                                NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                                locale
                        )
                ));

        modelAndView.addObject("elaborationStep", new RecipeElaborationStep());
        modelAndView.addObject("recipes", translatedRecipes);
        return modelAndView;
    }

    // Guardar nuevo paso
    @PostMapping("/create")
    public ModelAndView createElaborationStep(@ModelAttribute("elaborationStep") RecipeElaborationStep elaborationStep,
            BindingResult result, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/createElaborationStep");

        if (result.hasErrors())
        {
            List<Recipe> recipes = recipeService.findAllRecipes();
            Map<Long, String> translatedRecipes = recipes.stream()
                    .collect(Collectors.toMap(
                            Recipe::getId,
                            recipe -> nutritionLocaleProvider.getTranslation(
                                    recipe.getName(),
                                    NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                                    locale
                            )
                    ));
            modelAndView.addObject("recipes", translatedRecipes);
            return modelAndView;
        }

        try
        {
            // Crear clave de traducción para stepDescription
            String baseKey = "stepDescription";
            String translationKey = baseKey + "." + UUID.randomUUID(); // clave única

            String originalText = elaborationStep.getStepDescription();

            // Guardamos la traducción en ES y EN
            List<Locale> locales = List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en"));
            for (Locale supportedLocale : locales)
            {
                nutritionLocaleService.addKeyToTranslationBundle(
                        translationKey,
                        originalText,
                        supportedLocale,
                        NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX
                );
            }

            // Guardamos la clave en la entidad
            elaborationStep.setStepDescription(translationKey);
            elaborationStepService.saveRecipeElaborationStep(elaborationStep);

            String successMessage = messageSource.getMessage("elaborationStep.success.create", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (IOException | RuntimeException e)
        {
            String errorMessage = messageSource.getMessage("elaborationStep.error.create", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;

    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/editElaborationStep");

        RecipeElaborationStep elaborationStep = elaborationStepService.getStepById(id);

        // Traducción del paso (stepDescription)
        String translatedStepDescription = nutritionLocaleProvider.getTranslation(
                elaborationStep.getStepDescription(),
                NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX,
                locale
        );
        elaborationStep.setStepDescription(translatedStepDescription);

        // Traducción de nombres de recetas
        List<Recipe> recipes = recipeService.findAllRecipes();
        Map<Long, String> translatedRecipes = recipes.stream()
                .collect(Collectors.toMap(
                        Recipe::getId,
                        recipe -> nutritionLocaleProvider.getTranslation(
                                recipe.getName(),
                                NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                                locale
                        )
                ));

        modelAndView.addObject("elaborationStep", elaborationStep);
        modelAndView.addObject("recipes", translatedRecipes);
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView updateElaborationStep(@ModelAttribute("elaborationStep") RecipeElaborationStep elaborationStep,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/editElaborationStep");

        try
        {
            String translatedValue = elaborationStep.getStepDescription();

            String bundlePath = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator
                    + NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX + "_es.properties";

            Properties translations = new Properties();
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(bundlePath), StandardCharsets.UTF_8))
            {
                translations.load(reader);
            }

            // Buscar la clave actual a partir del valor ingresado
            String translationKey = translations.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(translatedValue))
                    .map(entry -> (String) entry.getKey())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No se encontró la clave para la descripción del paso"));

            // Actualizar traducciones
            List<Locale> locales = List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en"));
            for (Locale supportedLocale : locales)
            {
                nutritionLocaleService.editKeyInTranslationBundle(
                        translationKey,
                        translatedValue,
                        supportedLocale,
                        NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX
                );
            }

            // Guardar nuevamente la clave en la entidad
            elaborationStep.setStepDescription(translationKey);
            elaborationStepService.updateRecipeElaborationStep(elaborationStep);

            String successMessage = messageSource.getMessage("elaborationStep.success.edit", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (IOException | RuntimeException e)
        {
            String errorMessage = messageSource.getMessage("elaborationStep.error.edit", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteElaborationStep(@PathVariable long id, RedirectAttributes redirectAttributes,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/elaborationSteps/home");
        try
        {
            RecipeElaborationStep elaborationStep = elaborationStepService.getStepById(id);

            // Eliminar claves de traducción del stepDescription en todos los idiomas soportados
            String translationKey = elaborationStep.getStepDescription();
            List<Locale> locales = List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en"));
            for (Locale supportedLocale : locales)
            {
                nutritionLocaleService.deleteKeyFromTranslationBundle(
                        translationKey,
                        supportedLocale,
                        NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX
                );
            }

            elaborationStepService.deleteRecipeElaborationStep(elaborationStep);
            String successMessage = messageSource.getMessage("elaborationStep.success.delete", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (IOException | NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("elaborationStep.deleted.error", new Object[]
            {
                e.getMessage()
            }, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView showElaborationStepDetails(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("elaborationSteps/details");
        RecipeElaborationStep elaborationStep = elaborationStepService.getStepById(id);

        String stepDescription = nutritionLocaleProvider.getTranslation(elaborationStep.getStepDescription(),
                NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX, locale);

        Recipe recipe = elaborationStep.getRecipe();

        String recipeName = nutritionLocaleProvider.getTranslation(
                recipe.getName(),
                NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                locale
        );

        RecipeDTO recipeDTO = new RecipeDTO(recipe.getId(), recipeName);

        RecipeElaborationStepDTO recipeElaborationStepDTO = new RecipeElaborationStepDTO(elaborationStep.getId(),
                stepDescription,
                elaborationStep.getStepNumber(),
                recipeDTO);

        modelAndView.addObject("elaborationStep", recipeElaborationStepDTO);
        return modelAndView;
    }
}
