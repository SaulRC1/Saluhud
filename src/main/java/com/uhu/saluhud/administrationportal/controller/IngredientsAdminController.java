package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhud.administrationportal.dto.nutrition.AllergenicDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.IngredientDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.RecipeDTO;
import com.uhu.saluhud.administrationportal.dto.nutrition.RecipeIngredientDTO;
import com.uhu.saluhud.administrationportal.service.localization.NutritionLocaleService;
import com.uhu.saluhud.localization.NutritionLocaleProvider;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.Ingredient;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalAllergenicService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.nutrition.AdministrationPortalIngredientService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/ingredients")
public class IngredientsAdminController
{

    @Autowired
    private AdministrationPortalIngredientService ingredientService;

    @Autowired
    private AdministrationPortalAllergenicService allergenicService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;

    @Autowired
    private NutritionLocaleService nutritionLocaleService;

    @GetMapping("/home")
    public ModelAndView getIngredients(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/ingredientsHome");
        Page<Ingredient> ingredientPage = ingredientService.getIngredients(page, size);

        List<IngredientDTO> listIngredientDTO = new ArrayList<>();
        ingredientPage.forEach((ingredient) ->
        {
            String ingredientName = nutritionLocaleProvider.getTranslation(ingredient.getName(),
                    NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX, locale);

            IngredientDTO ingredientDTO = new IngredientDTO(ingredient.getId(),
                    ingredientName, ingredient.getKilocalories(), ingredient.getProteinAmount(),
                    ingredient.getCarbohydratesAmount(), ingredient.getFatAmount());
            listIngredientDTO.add(ingredientDTO);
        });

        modelAndView.addObject("ingredients", listIngredientDTO);
        modelAndView.addObject("currentPage", ingredientPage.getNumber());
        modelAndView.addObject("totalPages", ingredientPage.getTotalPages());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm(Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/createIngredient");

        // Obtener y traducir alérgenos
        List<Allergenic> allergenics = allergenicService.findAllAllergenics();
        Map<Long, String> translatedAllergenics = allergenics.stream()
                .collect(Collectors.toMap(
                        Allergenic::getId,
                        allergenic -> nutritionLocaleProvider.getTranslation(
                                "allergenic." + allergenic.getId(),
                                NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                                locale)
                ));

        modelAndView.addObject("ingredient", new Ingredient());
        modelAndView.addObject("allergenics", translatedAllergenics);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createIngredient(@ModelAttribute Ingredient ingredient,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/createIngredient");

        try
        {
            // 1. Añadir traducciones del nombre en todos los idiomas
            String baseKey = "ingredient.name." + ingredient.getName();

            for (Locale langLocale : List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en")))
            {
                nutritionLocaleService.addKeyToTranslationBundle(
                        baseKey, ingredient.getName(), langLocale,
                        NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX);
            }

            // 2. Leer archivo de traducciones del idioma actual
            String path = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator
                    + NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX + "_" + locale.toLanguageTag() + ".properties";

            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path),
                    StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader))
            {
                Properties translations = new Properties();
                translations.load(reader);

                // 3. Buscar la clave asociada al valor
                String nameKey = translations.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(ingredient.getName()))
                        .map(entry -> (String) entry.getKey())
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No se encontró la clave para el nombre del ingrediente"));

                // 4. Reemplazar el nombre por la clave
                ingredient.setName(nameKey);
            } catch (FileNotFoundException ex)
            {
                Logger.getLogger(IngredientsAdminController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex)
            {
                Logger.getLogger(IngredientsAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }

            // 5. Guardar ingrediente
            ingredientService.saveIngredient(ingredient);

            String successMessage = messageSource.getMessage("ingredients.created.success", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (RuntimeException e)
        {
            String errorMessage = messageSource.getMessage("ingredients.created.error", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        } catch (IOException ex)
        {
            Logger.getLogger(IngredientsAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/editIngredient");

        Ingredient ingredient = ingredientService.getIngredientById(id);

        // Restauramos traducción
        String translatedName = nutritionLocaleProvider.getTranslation(
                ingredient.getName(), NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX, locale);
        ingredient.setName(translatedName);

        // Alérgenos traducidos
        List<Allergenic> allergenics = allergenicService.findAllAllergenics();
        Map<Long, String> translatedAllergenics = allergenics.stream()
                .collect(Collectors.toMap(
                        Allergenic::getId,
                        allergenic -> nutritionLocaleProvider.getTranslation(
                                allergenic.getName(),
                                NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                                locale
                        )
                ));

        modelAndView.addObject("ingredient", ingredient);
        modelAndView.addObject("allergenics", translatedAllergenics);
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView updateIngredient(@ModelAttribute("ingredient") Ingredient ingredient,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/editIngredient");

        try
        {
            storeTranslatableFields(ingredient);
            readTranslationKeys(ingredient, "es");

            ingredientService.updateIngredient(ingredient);

            String successMessage = messageSource.getMessage("ingredients.updated.success", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (IOException | RuntimeException e)
        {
            String errorMessage = messageSource.getMessage("ingredients.updated.error", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    private void storeTranslatableFields(Ingredient ingredient) throws IOException
    {
        String baseKey = "allergenic";
        String nameKey = baseKey + ".name." + ingredient.getName();

        List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en")).forEach(lang ->
        {
            try
            {
                nutritionLocaleService.addKeyToTranslationBundle(nameKey, ingredient.getName(), lang,
                        NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX);
            } catch (IOException ex)
            {
                Logger.getLogger(RecipesAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void readTranslationKeys(Ingredient ingredient, String languageTag) throws IOException
    {
        String bundlePath = nutritionLocaleProvider.getTranslationsRootFolder() + File.separator
                + NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX + "_" + languageTag + ".properties";

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(bundlePath),
                StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(reader))
        {

            Properties translations = new Properties();
            translations.load(bufferedReader);

            ingredient.setName(findTranslationKey(ingredient.getName(), translations, "nombre del ingrediente"));
        }
    }

    private String findTranslationKey(String value, Properties translations, String fieldName)
    {
        return translations.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(entry -> (String) entry.getKey())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró la clave para la " + fieldName));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteIngredient(@PathVariable long id, RedirectAttributes redirectAttributes,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/ingredients/home");
        try
        {
            Ingredient ingredient = ingredientService.findById(id);

            // Eliminar traducciones asociadas al nombre
            deleteIngredientTranslations(ingredient);

            ingredientService.deleteIngredient(ingredient);

            String successMessage = messageSource.getMessage("ingredient.deleted.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (IllegalArgumentException | NoSuchMessageException | IOException e)
        {
            String errorMessage = messageSource.getMessage("ingredient.deleted.error", new Object[]
            {
                e.getMessage()
            }, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            Logger.getLogger(IngredientsAdminController.class.getName()).log(Level.SEVERE, "Error eliminando ingrediente", e);
        }

        return modelAndView;

    }

    private void deleteIngredientTranslations(Ingredient ingredient) throws IOException
    {
        String nameKey = ingredient.getName();

        for (Locale langLocale : List.of(Locale.forLanguageTag("es"), Locale.forLanguageTag("en")))
        {
            nutritionLocaleService.deleteKeyFromTranslationBundle(
                    nameKey,
                    langLocale,
                    NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX
            );
        }
    }

    @GetMapping("/details/{id}")
    public ModelAndView showIngredientDetails(@PathVariable long id, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("ingredients/details");
        Ingredient ingredient = ingredientService.getIngredientById(id);
        Set<Allergenic> allergens = ingredient.getAllergens();

        Set<AllergenicDTO> listAllergensDTO = new HashSet();
        allergens.forEach((allergen) ->
        {
            String allergenName = nutritionLocaleProvider.getTranslation(allergen.getName(),
                    NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX, locale);
            AllergenicDTO allergenicDTO = new AllergenicDTO(allergen.getId(), allergenName);
            listAllergensDTO.add(allergenicDTO);
        });

        List<RecipeIngredientDTO> listRecipeIngredientsDTO = ingredient.getRecipeIngredients().stream()
                .map(recipeIngredient ->
                {
                    Recipe recipe = recipeIngredient.getRecipe();
                    String recipeName = nutritionLocaleProvider.getTranslation(
                            recipe.getName(),
                            NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, // usa el prefijo que estés usando
                            locale
                    );

                    RecipeDTO recipeDTO = new RecipeDTO(recipe.getId(), recipeName);

                    Ingredient ing = recipeIngredient.getIngredient();
                    String ingredientName = nutritionLocaleProvider.getTranslation(
                            ing.getName(),
                            NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                            locale
                    );

                    IngredientDTO ingredientDTO = new IngredientDTO(ing.getId(), ingredientName);

                    String unit = nutritionLocaleProvider.getTranslation(recipeIngredient.getUnit(),
                            NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX, locale);
                    return new RecipeIngredientDTO(
                            recipeIngredient.getId(),
                            recipeDTO,
                            ingredientDTO,
                            recipeIngredient.getQuantity(),
                            unit
                    );
                })
                .collect(Collectors.toList());

        String ingredientName = nutritionLocaleProvider.getTranslation(ingredient.getName(),
                NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX, locale);

        IngredientDTO ingredientDTO = new IngredientDTO(ingredient.getId(),
                ingredientName, ingredient.getKilocalories(), ingredient.getProteinAmount(),
                ingredient.getCarbohydratesAmount(), ingredient.getFatAmount(),
                listAllergensDTO, listRecipeIngredientsDTO);

        modelAndView.addObject("ingredient", ingredientDTO);
        return modelAndView;
    }
}
