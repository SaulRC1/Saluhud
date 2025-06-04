package com.uhu.saluhud.administrationportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhu.saluhuddatabaseutils.models.user.DailyStepsHistorical;
import com.uhu.saluhuddatabaseutils.models.user.DailyStepsHistoricalEntry;
import com.uhu.saluhuddatabaseutils.models.user.FitnessTargetEnum;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUserFitnessData;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistorical;
import com.uhu.saluhuddatabaseutils.models.user.SleepHistoricalEntry;
import com.uhu.saluhuddatabaseutils.models.user.WeightHistorical;
import com.uhu.saluhuddatabaseutils.models.user.WeightHistoricalEntry;
import com.uhu.saluhuddatabaseutils.security.PasswordEncryptionService;
import com.uhu.saluhuddatabaseutils.service.fitness.SaluhudUserFitnessDataService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalDailyStepsHistoricalEntryService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalDailyStepsHistoricalService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalSaluhudUserService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalSleepHistoricalEntryService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalSleepHistoricalService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalUserFitnessDataService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalWeightHistoricalEntryService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalWeightHistoricalService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
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
 * @author Juan Alberto Domínguez Vázquez
 */
@Controller
@RequestMapping("/users")
public class UserAdminController
{

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncryptionService passwordEncryptionService;

    @Autowired
    private AdministrationPortalSaluhudUserService saluhudUserService;

    @Autowired
    private AdministrationPortalUserFitnessDataService fitnessDataService;

    @Autowired
    private AdministrationPortalSleepHistoricalService sleepHistoricalService;

    @Autowired
    private AdministrationPortalWeightHistoricalService weightHistoricalService;

    @Autowired
    private AdministrationPortalDailyStepsHistoricalService dailyStepHistoricalService;

    @Autowired
    private AdministrationPortalDailyStepsHistoricalEntryService dailyStepsHistoricalEntryService;

    @Autowired
    private AdministrationPortalSleepHistoricalEntryService sleepHistoricalEntryService;

    @Autowired
    private AdministrationPortalWeightHistoricalEntryService weightHistoricalEntryService;

    @Autowired
    SaluhudUserFitnessDataService saluhudUserFitnessDataService;

    @GetMapping("/home")
    public ModelAndView getUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size)
    {
        ModelAndView modelAndView = new ModelAndView("users/usersHome");
        Page<SaluhudUser> usersPage = saluhudUserService.getUsers(page, size);
        modelAndView.addObject("users", usersPage.getContent());
        modelAndView.addObject("currentPage", usersPage.getNumber());
        modelAndView.addObject("totalPages", usersPage.getTotalPages());
        return modelAndView;
    }

    // Mostrar formulario para crear un nuevo usuario
    @GetMapping("/create")
    public ModelAndView showCreateForm()
    {
        List<SaluhudUser> users = saluhudUserService.findAllUsers(); // Obtén todas las recetas
        ModelAndView modelAndView = new ModelAndView("users/createUser");
        modelAndView.addObject("users", users);
        modelAndView.addObject("user", new SaluhudUser());
        return modelAndView;
    }

    // Guardar nuevo usuario
    @PostMapping("/create")
    public ModelAndView createUser(@ModelAttribute("user") SaluhudUser user, BindingResult result, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("users/createUser");

        if (result.hasErrors())
        {
            modelAndView.addObject("users", saluhudUserService.findAllUsers());
            return modelAndView;
        }

        try
        {
            if (saluhudUserService.existsByUsername(user.getUsername()))
            {
                modelAndView.addObject("errorMessage", messageSource.getMessage("user.error.username.exists", null, locale));
                return modelAndView;
            }
            if (saluhudUserService.existsByEmail(user.getEmail()))
            {
                modelAndView.addObject("errorMessage", messageSource.getMessage("user.error.email.exists", null, locale));
                return modelAndView;
            }
            if (user.getPhoneNumber() != null && saluhudUserService.existsByPhoneNumber(user.getPhoneNumber()))
            {
                modelAndView.addObject("errorMessage", messageSource.getMessage("user.error.phone.exists", null, locale));
                return modelAndView;
            }

            String encryptedPassword = passwordEncryptionService.encryptPassword(user.getPassword());
            user.setPassword(encryptedPassword);
            saluhudUserService.saveUser(user);
            modelAndView.addObject("successMessage", messageSource.getMessage("user.success.create", null, locale));
        } catch (NoSuchMessageException e)
        {
            modelAndView.addObject("errorMessage", "Error retrieving message: " + e.getMessage());
        } catch (DataIntegrityViolationException e)
        {
            modelAndView.addObject("errorMessage", messageSource.getMessage("user.error.duplicate", null, locale));
        } catch (Exception e)
        {
            modelAndView.addObject("errorMessage", messageSource.getMessage("user.error.create", new Object[]
            {
                e.getMessage()
            }, locale));
        }

        return modelAndView;
    }

    // Mostrar formulario para editar un usuario
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("users/editUser");
        SaluhudUser user = saluhudUserService.getUserById(id);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    // Guardar edición de usuario
    @PostMapping("/edit")
    public ModelAndView updateUser(@ModelAttribute("user") SaluhudUser user,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("users/editUser");
        try
        {
            String encryptedPassword = passwordEncryptionService.encryptPassword(user.getPassword());
            user.setPassword(encryptedPassword);
            saluhudUserService.updateUser(user);
            String successMessage = messageSource.getMessage("user.success.edit", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("user.error.edit", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    // Eliminar usuario
    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable long id, RedirectAttributes redirectAttributes,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/users/home");
        try
        {
            SaluhudUser user = saluhudUserService.getUserById(id);
            saluhudUserService.deleteUser(user);
            String successMessage = messageSource.getMessage("user.success.delete", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("user.error.delete", new Object[]
            {
                e.getMessage()
            }, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    // Ver detalles de un usuario
    @GetMapping("/details/{id}")
    public ModelAndView showUserDetails(@PathVariable long id)
    {
        ModelAndView modelAndView = new ModelAndView("users/details");
        SaluhudUser user = saluhudUserService.getUserById(id);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/fitness/create/{userId}")
    public ModelAndView showCreateFitnessDataForm(@PathVariable Long userId)
    {
        SaluhudUser user = saluhudUserService.getUserById(userId);
        if (user == null)
        {
            return new ModelAndView("redirect:/users/home");
        }

        SaluhudUserFitnessData fitnessData = new SaluhudUserFitnessData();
        ModelAndView mav = new ModelAndView("users/createFitnessData");
        mav.addObject("fitnessData", fitnessData);
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping("/fitness/create")
    public ModelAndView createFitnessData(@Valid @ModelAttribute("fitnessData") SaluhudUserFitnessData fitnessData,
            BindingResult result,
            @RequestParam("userId") Long userId,
            Locale locale)
    {
        ModelAndView model = new ModelAndView("users/createFitnessData");
        if (result.hasErrors())
        {
            model.addObject("userId", userId);
        }

        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);

            float weight = (float) fitnessData.getWeight();
            float height = (float) fitnessData.getHeight();
            SaluhudUserFitnessData fitness = saluhudUserFitnessDataService.buildSaluhudUserFitnessData(weight, height,
                    fitnessData.getAge(), fitnessData.getBiologicalSex(),
                    fitnessData.getActivityFactor(), fitnessData.getBodyComposition(), fitnessData.getFitnessTarget());

            fitness.setSaluhudUser(user);
            user.setFitnessData(fitness);
            fitnessDataService.saveFitnessData(fitness);
            model.addObject("successMessage", messageSource.getMessage("user.successFitness.create", null, locale));

        } catch (DataIntegrityViolationException e)
        {
            model.addObject("errorMessage", messageSource.getMessage("user.errorFitness.duplicate", null, locale) + e.getMessage());

        } catch (NoSuchElementException e)
        {
            model.addObject("errorMessage", messageSource.getMessage("user.errorFitness.find", null, locale) + e.getMessage());

        } catch (RuntimeException e)
        {
            model.addObject("errorMessage", messageSource.getMessage("user.errorFitness.saving", null, locale) + e.getMessage());
        }

        return model;
    }

    @GetMapping("/fitness/edit/{userId}")
    public ModelAndView showEditFitnessDataForm(@PathVariable Long userId)
    {
        SaluhudUser user = saluhudUserService.getUserById(userId);
        if (user == null || user.getFitnessData() == null)
        {
            return new ModelAndView("redirect:/users/home");
        }

        ModelAndView mav = new ModelAndView("users/editFitnessData");
        mav.addObject("fitnessData", user.getFitnessData());
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping("/fitness/edit/{userId}")
    public ModelAndView updateFitnessData(@PathVariable Long userId,
            @ModelAttribute("fitnessData") SaluhudUserFitnessData fitnessData,
            Locale locale)
    {
        ModelAndView mav = new ModelAndView("users/editFitnessData");

        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);

            float weight = (float) fitnessData.getWeight();
            float height = (float) fitnessData.getHeight();
            SaluhudUserFitnessData fitness = saluhudUserFitnessDataService.buildSaluhudUserFitnessData(weight, height,
                    fitnessData.getAge(), fitnessData.getBiologicalSex(),
                    fitnessData.getActivityFactor(), fitnessData.getBodyComposition(), fitnessData.getFitnessTarget());

            user.setFitnessData(fitness);
            fitnessDataService.updateFitnessData(fitness);
            String successMessage = messageSource.getMessage("user.fitnessData.success.edit", null, locale);
            mav.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("user.fitnessData.error.edit", new Object[]
            {
                e.getMessage()
            }, locale);
            mav.addObject("errorMessage", errorMessage);
        }

        mav.addObject("fitnessData", fitnessData);
        mav.addObject("userId", userId);
        return mav;
    }

    @GetMapping("/fitness/delete/{userId}")
    public ModelAndView deleteFitnessData(@PathVariable Long userId, RedirectAttributes redirectAttributes, Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("redirect:/users/home");
        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);
            SaluhudUserFitnessData fitnessData = user.getFitnessData();
            if (user != null && user.getFitnessData() != null)
            {
                user.setFitnessData(null);
                saluhudUserService.saveUser(user);
                fitnessDataService.deleteFitnessData(fitnessData);

                String successMessage = messageSource.getMessage("fitnessData.success.delete", null, locale);
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
            }
            else
            {
                String errorMessage = messageSource.getMessage("fitnessData.error.deleteNotFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            }
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("fitnessData.error.delete", new Object[]
            {
                e.getMessage()
            }, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @GetMapping("/sleepHistorics/{userId}")
    public ModelAndView showUserSleepHistorics(@PathVariable long userId)
    {
        ModelAndView mav = new ModelAndView("users/showSleepHistorics");

        SaluhudUser user = saluhudUserService.getUserById(userId);
        SleepHistorical sleepHistorical = sleepHistoricalService.findByUserId(user.getId());

        String errorMessageJsonConversion = messageSource.getMessage("user.historics.error.jsonConversion", null, LocaleContextHolder.getLocale());
        String errorMessageNoData = messageSource.getMessage("user.historics.noData", null, LocaleContextHolder.getLocale());

        if (sleepHistorical != null)
        {
            List<SleepHistoricalEntry> entries = sleepHistorical.getEntries();
            List<String> dates = entries.stream()
                    .map(e -> e.getEntryDate().toString())
                    .collect(Collectors.toList());

            List<Integer> hours = entries.stream()
                    .map(SleepHistoricalEntry::getHoursSlept)
                    .collect(Collectors.toList());

            List<Double> minutes = entries.stream()
                    .map(SleepHistoricalEntry::getMinutesSlept)
                    .collect(Collectors.toList());

            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                String datesJson = objectMapper.writeValueAsString(dates);
                String hoursJson = objectMapper.writeValueAsString(hours);
                String minutesJson = objectMapper.writeValueAsString(minutes);

                mav.addObject("dates", datesJson);
                mav.addObject("hours", hoursJson);
                mav.addObject("minutes", minutesJson);
            } catch (JsonProcessingException e)
            {
                mav.addObject("errorMessage", errorMessageJsonConversion);
            }

            mav.addObject("entries", entries);
            mav.addObject("user", user);
            mav.addObject("sleepHistorical", sleepHistorical);
        }
        else
        {
            mav.addObject("errorMessage", errorMessageNoData);
        }

        return mav;
    }

    @GetMapping("/weightHistorics/{userId}")
    public ModelAndView showUserWeightHistorics(@PathVariable long userId)
    {
        ModelAndView mav = new ModelAndView("users/showWeightHistorics");

        SaluhudUser user = saluhudUserService.getUserById(userId);
        WeightHistorical weightHistorical = weightHistoricalService.findWeightHistoricalByUserId(user.getId());

        String errorMessageJsonConversion = messageSource.getMessage("user.historics.error.jsonConversion", null, LocaleContextHolder.getLocale());
        String errorMessageNoData = messageSource.getMessage("user.weight.historics.noData", null, LocaleContextHolder.getLocale());

        if (weightHistorical != null)
        {
            List<WeightHistoricalEntry> entries = weightHistorical.getEntries();
            List<String> dates = entries.stream()
                    .map(e -> e.getEntryDate().toString())
                    .collect(Collectors.toList());

            List<Double> height = entries.stream()
                    .map(WeightHistoricalEntry::getHeightEntry)
                    .collect(Collectors.toList());

            List<Double> weight = entries.stream()
                    .map(WeightHistoricalEntry::getWeightEntry)
                    .collect(Collectors.toList());

            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                String datesJson = objectMapper.writeValueAsString(dates);
                String heightJson = objectMapper.writeValueAsString(height);
                String weightJson = objectMapper.writeValueAsString(weight);

                mav.addObject("dates", datesJson);
                mav.addObject("height", heightJson);
                mav.addObject("weight", weightJson);
            } catch (JsonProcessingException e)
            {
                mav.addObject("errorMessage", errorMessageJsonConversion);
            }

            mav.addObject("entries", entries);
            mav.addObject("user", user);
            mav.addObject("weightHistorical", weightHistorical);
        }
        else
        {
            mav.addObject("errorMessage", errorMessageNoData);
        }

        return mav;
    }

    @GetMapping("/dailyStepsHistorics/{userId}")
    public ModelAndView showUserDailyStepsHistorics(@PathVariable long userId)
    {
        ModelAndView mav = new ModelAndView("users/showDailyStepsHistorics");

        SaluhudUser user = saluhudUserService.getUserById(userId);
        List<DailyStepsHistorical> listDailyStepsHistorical = dailyStepHistoricalService.findAllByUserId(user.getId());

        String errorMessageJsonConversion = messageSource.getMessage("user.historics.error.jsonConversion", null, LocaleContextHolder.getLocale());
        String errorMessageNoData = messageSource.getMessage("user.step.historics.noData", null, LocaleContextHolder.getLocale());

        if (listDailyStepsHistorical != null && !listDailyStepsHistorical.isEmpty())
        {
            for (DailyStepsHistorical dailyStepsHistorical : listDailyStepsHistorical)
            {
                List<DailyStepsHistoricalEntry> entries = dailyStepsHistorical.getEntries();
                List<String> dates = entries.stream()
                        .map(e -> e.getEntryDate().toString())
                        .collect(Collectors.toList());

                List<Integer> doneSteps = entries.stream()
                        .map(DailyStepsHistoricalEntry::getDoneSteps)
                        .collect(Collectors.toList());

                List<Double> kcalBurned = entries.stream()
                        .map(DailyStepsHistoricalEntry::getKiloCaloriesBurned)
                        .collect(Collectors.toList());

                ObjectMapper objectMapper = new ObjectMapper();
                try
                {
                    String datesJson = objectMapper.writeValueAsString(dates);
                    String doneStepsJson = objectMapper.writeValueAsString(doneSteps);
                    String kcalBurnedJson = objectMapper.writeValueAsString(kcalBurned);

                    mav.addObject("dates", datesJson);
                    mav.addObject("doneSteps", doneStepsJson);
                    mav.addObject("kcalBurned", kcalBurnedJson);
                } catch (JsonProcessingException e)
                {
                    mav.addObject("errorMessage", errorMessageJsonConversion);
                }

                mav.addObject("entries", entries);
                mav.addObject("user", user);
                mav.addObject("stepHistorical", dailyStepsHistorical);
            }
        }
        else
        {
            mav.addObject("errorMessage", errorMessageNoData);
        }

        return mav;
    }

    @GetMapping("/dailyStepsHistorics/create/{userId}")
    public ModelAndView showCreateDailyStepsEntryForm(@PathVariable Long userId)
    {
        SaluhudUser user = saluhudUserService.getUserById(userId);
        if (user == null)
        {
            return new ModelAndView("redirect:/users/home");
        }

        DailyStepsHistoricalEntry entry = new DailyStepsHistoricalEntry();
        ModelAndView mav = new ModelAndView("users/createStepEntry");
        mav.addObject("entry", entry);
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping("/dailyStepsHistorics/create")
    public ModelAndView createDailyStepsEntry(@Valid @ModelAttribute("entry") DailyStepsHistoricalEntry entry,
            BindingResult result,
            @RequestParam("userId") Long userId,
            Locale locale)
    {
        ModelAndView mav = new ModelAndView("users/createStepEntry");

        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);
            DailyStepsHistorical dailyStepsHistorical = dailyStepHistoricalService.findByUserId(user.getId());
            entry.setDailyStepsHistorical(dailyStepsHistorical);

            dailyStepsHistoricalEntryService.saveDailyStepsHistoricalEntry(entry);

            mav.addObject("successMessage", messageSource.getMessage("user.daily.steps.entry.created.success", null, locale));
        } catch (DataIntegrityViolationException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.daily.steps.entry.error.duplicate", null, locale) + ": " + e.getMessage());
        } catch (NoSuchElementException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.daily.steps.entry.error.user", null, locale) + ": " + e.getMessage());
        } catch (NoSuchMessageException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.daily.steps.entry.error.saving", null, locale) + ": " + e.getMessage());
        }

        mav.addObject("userId", userId);
        return mav;
    }

    @GetMapping("/dailyStepsHistorics/{userId}/entries/edit/{entryId}")
    public ModelAndView showEditDailyStepsEntryForm(@PathVariable long userId,
            @PathVariable long entryId)
    {
        ModelAndView modelAndView = new ModelAndView("users/editDailyStepsEntry");
        DailyStepsHistoricalEntry entry = dailyStepsHistoricalEntryService.getDailyStepsHistoricalEntryById(entryId);
        SaluhudUser user = entry.getDailyStepsHistorical().getUser();
        modelAndView.addObject("entry", entry);
        modelAndView.addObject("userId", user.getId());
        return modelAndView;
    }

    @PostMapping("/dailyStepsHistorics/edit/{entryId}")
    public ModelAndView updateDailyStepsEntry(@ModelAttribute("entry") DailyStepsHistoricalEntry entry,
            BindingResult result,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("users/editDailyStepsEntry");
        if (result.hasErrors())
        {
            return modelAndView;
        }

        try
        {
            dailyStepsHistoricalEntryService.updateDailyStepsHistoricalEntry(entry);
            String successMessage = messageSource.getMessage("daily.steps.entry.success.edit", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("daily.steps.entry.error.edit", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @GetMapping("/dailyStepsHistorics/{userId}/entries/delete/{entryId}")
    public ModelAndView deleteDailyStepsEntry(@PathVariable long userId,
            @PathVariable long entryId,
            Locale locale)
    {
        ModelAndView mav = new ModelAndView("redirect:/users/dailyStepsHistorics/" + userId);

        try
        {
            DailyStepsHistoricalEntry entry = dailyStepsHistoricalEntryService.getDailyStepsHistoricalEntryById(entryId);
            dailyStepsHistoricalEntryService.deleteDailyStepsHistorical(entry);
            String successMessage = messageSource.getMessage("user.daily.steps.entry.deleted.success", null, locale);
            mav.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("user.daily.steps.entry.deleted.error", new Object[]
            {
                e.getMessage()
            }, locale);
            mav.addObject("errorMessage", errorMessage);
        }

        return mav;
    }

    @GetMapping("/sleepHistorics/create/{userId}")
    public ModelAndView showCreateSleepEntryForm(@PathVariable Long userId)
    {
        SaluhudUser user = saluhudUserService.getUserById(userId);
        if (user == null)
        {
            return new ModelAndView("redirect:/users/home");
        }

        SleepHistoricalEntry entry = new SleepHistoricalEntry();
        ModelAndView mav = new ModelAndView("users/createSleepEntry");
        mav.addObject("entry", entry);
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping("/sleepHistorics/create")
    public ModelAndView createSleepEntry(@Valid @ModelAttribute("entry") SleepHistoricalEntry entry,
            BindingResult result,
            @RequestParam("userId") Long userId,
            Locale locale)
    {
        ModelAndView mav = new ModelAndView("users/createSleepEntry");

        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);
            SleepHistorical sleepHistorical = sleepHistoricalService.findByUserId(user.getId());
            entry.setSleepHistorical(sleepHistorical);

            sleepHistoricalEntryService.saveSleepHistoricalEntry(entry);

            mav.addObject("successMessage", messageSource.getMessage("user.sleep.entry.created.success", null, locale));
        } catch (DataIntegrityViolationException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.sleep.entry.error.duplicate", null, locale) + ": " + e.getMessage());
        } catch (NoSuchElementException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.sleep.entry.error.user", null, locale) + ": " + e.getMessage());
        } catch (NoSuchMessageException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.sleep.entry.error.saving", null, locale) + ": " + e.getMessage());
        }

        mav.addObject("userId", userId);
        return mav;
    }

    @GetMapping("/sleepHistorics/{userId}/entries/edit/{entryId}")
    public ModelAndView showEditSleepEntryForm(@PathVariable long userId,
            @PathVariable long entryId)
    {
        ModelAndView modelAndView = new ModelAndView("users/editSleepEntry");
        SleepHistoricalEntry entry = sleepHistoricalEntryService.findSleepHistoricalEntryById(entryId);
        SaluhudUser user = entry.getSleepHistorical().getUser();
        modelAndView.addObject("entry", entry);
        modelAndView.addObject("userId", user.getId());
        return modelAndView;
    }

    @PostMapping("/sleepHistoricalEntries/edit/{entryId}")
    public ModelAndView updateSleepEntry(@ModelAttribute("entry") SleepHistoricalEntry entry,
            BindingResult result,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("users/editSleepEntry");
        if (result.hasErrors())
        {
            return modelAndView;
        }

        try
        {
            sleepHistoricalEntryService.updateSleepHistoricalEntry(entry);
            String successMessage = messageSource.getMessage("sleep.entry.success.edit", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("sleep.entry.error.edit", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @GetMapping("/sleepHistorics/{userId}/entries/delete/{entryId}")
    public ModelAndView deleteSleepEntry(@PathVariable long userId,
            @PathVariable long entryId,
            Locale locale)
    {
        ModelAndView mav = new ModelAndView("redirect:/users/sleepHistorics/" + userId);

        try
        {
            SleepHistoricalEntry entry = sleepHistoricalEntryService.findSleepHistoricalEntryById(entryId);
            sleepHistoricalEntryService.deleteSleepHistoricalEntry(entry);
            String successMessage = messageSource.getMessage("user.sleep.historics.entry.deleted.success", null, locale);
            mav.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("user.sleep.historics.entry.deleted.error",
                    new Object[]
                    {
                        e.getMessage()
                    }, locale);
            mav.addObject("errorMessage", errorMessage);
        }

        return mav;
    }

    @GetMapping("/weightHistorics/create/{userId}")
    public ModelAndView showCreateWeightEntryForm(@PathVariable Long userId)
    {
        SaluhudUser user = saluhudUserService.getUserById(userId);
        if (user == null)
        {
            return new ModelAndView("redirect:/users/home");
        }

        WeightHistoricalEntry entry = new WeightHistoricalEntry();
        ModelAndView mav = new ModelAndView("users/createWeightEntry");
        mav.addObject("entry", entry);
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping("/weightHistorics/create")
    public ModelAndView createWeightEntry(@Valid @ModelAttribute("entry") WeightHistoricalEntry entry,
            BindingResult result,
            @RequestParam("userId") Long userId,
            Locale locale)
    {
        ModelAndView mav = new ModelAndView("users/createWeightEntry");

        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);
            WeightHistorical weightHistorical = weightHistoricalService.findWeightHistoricalByUserId(user.getId());
            entry.setWeightHistorical(weightHistorical);

            weightHistoricalEntryService.saveWeightHistoricalEntry(entry);

            mav.addObject("successMessage", messageSource.getMessage("user.weight.entry.created.success", null, locale));
        } catch (DataIntegrityViolationException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.weight.entry.error.duplicate", null, locale) + ": " + e.getMessage());
        } catch (NoSuchElementException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.weight.entry.error.user", null, locale) + ": " + e.getMessage());
        } catch (NoSuchMessageException e)
        {
            mav.addObject("errorMessage", messageSource.getMessage("user.weight.entry.error.saving", null, locale) + ": " + e.getMessage());
        }

        mav.addObject("userId", userId);
        return mav;
    }

    @GetMapping("/weightHistorics/{userId}/entries/edit/{entryId}")
    public ModelAndView showEditWeightEntryForm(@PathVariable long userId,
            @PathVariable long entryId)
    {
        ModelAndView modelAndView = new ModelAndView("users/editWeightEntry");
        WeightHistoricalEntry entry = weightHistoricalEntryService.findWeightHistoricalEntryById(entryId);
        SaluhudUser user = entry.getWeightHistorical().getUser();
        modelAndView.addObject("entry", entry);
        modelAndView.addObject("userId", user.getId());
        return modelAndView;
    }

    @PostMapping("/weightHistorics/edit/{entryId}")
    public ModelAndView updateWeightEntry(@Valid @ModelAttribute("entry") WeightHistoricalEntry entry,
            BindingResult result,
            Locale locale)
    {
        ModelAndView modelAndView = new ModelAndView("users/editWeightEntry");
        if (result.hasErrors())
        {
            return modelAndView;
        }

        try
        {
            weightHistoricalEntryService.updateWeightHistoricalEntry(entry);
            String successMessage = messageSource.getMessage("weight.entry.success.edit", null, locale);
            modelAndView.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("weight.entry.error.edit", new Object[]
            {
                e.getMessage()
            }, locale);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @GetMapping("/weightHistorics/{userId}/entries/delete/{entryId}")
    public ModelAndView deleteWeightEntry(@PathVariable long userId,
            @PathVariable long entryId,
            Locale locale)
    {
        ModelAndView mav = new ModelAndView("redirect:/users/weightHistorics/" + userId);

        try
        {
            WeightHistoricalEntry entry = weightHistoricalEntryService.findWeightHistoricalEntryById(entryId);
            weightHistoricalEntryService.deleteWeightHistoricalEntry(entry);
            String successMessage = messageSource.getMessage("user.weight.historics.entry.deleted.success", null, locale);
            mav.addObject("successMessage", successMessage);
        } catch (NoSuchMessageException e)
        {
            String errorMessage = messageSource.getMessage("user.weight.historics.entry.deleted.error",
                    new Object[]
                    {
                        e.getMessage()
                    }, locale);
            mav.addObject("errorMessage", errorMessage);
        }

        return mav;
    }

    @PostMapping("/weightHistorical/create")
    public String createWeightHistorical(@RequestParam("userId") Long userId,
            RedirectAttributes redirectAttributes, Locale locale)
    {
        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);
            WeightHistorical historical = new WeightHistorical();
            historical.setUser(user);
            weightHistoricalService.saveWeightHistorical(historical);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("user.weight.historical.created.success", null, locale));
        } catch (NoSuchMessageException e)
        {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("user.weight.historical.created.error", null, locale));
        }
        return "redirect:/users/weightHistorics/" + userId;
    }

    @PostMapping("/sleepHistorical/create")
    public String createSleepHistorical(@RequestParam("userId") Long userId,
            RedirectAttributes redirectAttributes, Locale locale)
    {
        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);
            SleepHistorical historical = new SleepHistorical();
            historical.setUser(user);
            sleepHistoricalService.saveSleepHistorical(historical);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("user.sleep.historical.created.success", null, locale));
        } catch (NoSuchMessageException e)
        {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("user.sleep.historical.created.error", null, locale));
        }
        return "redirect:/users/sleepHistorics/" + userId;
    }

    @PostMapping("/stepHistorical/create")
    public String createDailyStepHistorical(@RequestParam("userId") Long userId,
            RedirectAttributes redirectAttributes, Locale locale)
    {
        try
        {
            SaluhudUser user = saluhudUserService.getUserById(userId);
            DailyStepsHistorical historical = new DailyStepsHistorical();
            historical.setUser(user);
            dailyStepHistoricalService.saveDailyStepsHistorical(historical);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("user.step.historical.created.success", null, locale));
        } catch (NoSuchMessageException e)
        {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("user.step.historical.created.error", null, locale));
        }
        return "redirect:/users/dailyStepsHistorics/" + userId;
    }

}
