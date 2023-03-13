package addressbook.service;

import addressbook.model.Contact;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ContactService {

    private List<Contact> contacts;

    public ContactService() {
        contacts = new ArrayList<>();
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }

    public Contact retrieveContact(String contactId) {
        for(Contact contact: contacts) {
            if(contact.getId().equals(contactId))
                return contact;
        }
        // the id does not exist otherwise
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
    }

    public Contact addContact(Contact contact) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        contact.setId(uuid);
        contacts.add(contact);
        return contact;
    }

    public List<Contact> deleteAllContacts() {
        List<Contact>  deleted = new ArrayList<>(contacts);
        contacts.clear();
        return deleted;
    }

    public Contact deleteContact(String contactId) {
        Optional<Contact> contactToDelete = contacts.stream().filter(contact -> contact.getId().equals(contactId)).findFirst();
        if(contactToDelete.isPresent()) {
            contacts.remove(contactToDelete.get());
            return contactToDelete.get();
        }
        // the id does not exist otherwise
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
    }

    public Contact updateContact(String contactId, Contact newContact) {
        Optional<Contact> contactToUpdate = contacts.stream().filter(contact -> contact.getId().equals(contactId)).findFirst();
        if(contactToUpdate.isPresent()) {
            contactToUpdate.get().setId(contactId);
            contactToUpdate.get().setFirstname(newContact.getFirstname());
            contactToUpdate.get().setFamilyname(newContact.getFamilyname());
            contactToUpdate.get().setEmail(newContact.getEmail());
            contactToUpdate.get().setPhonenumber(newContact.getPhonenumber());
            return contactToUpdate.get();
        }
        // the id does not exist otherwise
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
    }
}