package com.bmatyganov.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bmatyganov.model.ApplicationUser;
import com.bmatyganov.model.Customer;
import com.bmatyganov.model.Note;
import com.bmatyganov.repository.ApplicationUserRepository;
import com.bmatyganov.service.CustomerServiceImpl;
import com.bmatyganov.service.NoteServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private NoteServiceImpl noteService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @GetMapping(value = "/user")
    public String listCustomers(Model model,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Customer> customers = customerService.findAll(pageRequest);

        int totalPages = customers.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("customerPage", customers);
        return "user/index.html";
    }

    @GetMapping(value = "/user/profile")
    public String userProfile(Model model,
                              @RequestParam(value = "id") long id,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size) {
        Optional<Customer> customer = customerService.findOne(id);

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Note> notes = noteService.findAllByCustomerId(id, pageRequest);

        int totalPages = notes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("customer", customer.get());
        model.addAttribute("notes", notes);


        return "user/profile.html";
    }

    @GetMapping(value = "/user/profile-edit")
    public String updateProfilePage(Model model,
                                    @RequestParam(value = "id") long id) {
        Customer customer = customerService.findOne(id).get();

        model.addAttribute("customer", customer);
        return "user/profile-edit.html";
    }

    @PostMapping(value = "/user/update-customer")
    public String updateCustomer(Model model,
                                 @ModelAttribute(value = "customer") Customer customer) {
        customerService.save(customer);

        return "redirect:/user/profile?id=" + customer.getId();
    }

    @GetMapping(value = "/user/note")
    public String newNote(Model model,
                          @RequestParam(value = "id") long id) {
        Note note = new Note();
        note.setCustomerId(id);
        model.addAttribute("note", note);
        System.out.println("Save note #############################" + note.getId());
        return "user/note.html";
    }

    @PostMapping(value = "/user/save-note")
    public String saveNewNote(Model model, Principal principal,
                              @ModelAttribute(value = "note") Note note) {

        ApplicationUser user = applicationUserRepository.findOneByEmail(principal.getName());
        if("".equals(note.getCreatedBy())){
            note.setCreatedBy(String.format("%s %s", user.getFirstName(), user.getLastName()));
            System.out.println("Inside if statement #############################" );
        }

        System.out.println("Update note #############################" + note.getId());
        System.out.println("Customer id #############################" + note.getCustomerId());
        System.out.println("Created by id #############################" + note.getCreatedBy());

        note.setUpdatedBy(String.format("%s %s", user.getFirstName(), user.getLastName()));
        noteService.save(note);



        return "redirect:/user/profile?id=" + note.getCustomerId();
    }

    @GetMapping(value = "/user/update-note")
    public String updateNote(Model model,
                             @RequestParam(value = "id") long id){

        Note note = noteService.findOne(id).get();

        System.out.println("Note to update #############################" + note.getId());

        model.addAttribute("note" , note);

        return "user/note.html";
    }
}
