package addressbook.controller;

import addressbook.model.Contact;
import addressbook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class ContactController {

    @Autowired
    ContactService contactService;

    @GetMapping("/api/contacts")
    public @ResponseBody List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/api/contacts/{contactId}")
    public @ResponseBody Contact getContact(@PathVariable String contactId) {
        Contact retContact =  contactService.retrieveContact(contactId);
        return retContact;
    }

    @PostMapping("/api/contacts")
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        // Need to check how to validate input in this case
        contactService.addContact(contact);
        return new ResponseEntity<Contact>(contact, HttpStatus.CREATED);

    }

    @DeleteMapping("/api/contacts")
    public @ResponseBody List<Contact> deleteAllContacts() {
        return contactService.deleteAllContacts();
    }

    @PutMapping("/api/contacts/{contactId}")
    public @ResponseBody Contact updateContact(@RequestBody Contact newContact, @PathVariable String contactId) {
        return contactService.updateContact(contactId, newContact);
    }

    @DeleteMapping("/api/contacts/{contactId}")
    public @ResponseBody Contact deleteContact(@PathVariable String contactId) {
        return contactService.deleteContact(contactId);
    }

}