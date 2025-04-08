package com.uhu.saluhud.administrationportal.controller;

import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUserFitnessData;
import com.uhu.saluhuddatabaseutils.security.PasswordEncryptionService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalSaluhudUserService;
import com.uhu.saluhuddatabaseutils.services.administrationportal.user.AdministrationPortalUserFitnessDataService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
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
            fitnessData.setSaluhudUser(user);
            user.setFitnessData(fitnessData);
            fitnessDataService.saveFitnessData(fitnessData);
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
}
