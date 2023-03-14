package addressbook.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Contact {

    private String id;

    @NotNull
    @NotEmpty
    private String firstname;

    @NotNull
    @NotEmpty
    private String familyname;

    private String phonenumber;

    private String email;

    Contact() {}

    Contact(String firstname, String familyname) {
        this.firstname = firstname;
        this.familyname = familyname;
    }

    Contact(String firstname, String familyname, String phonenumber, String email) {
        this.firstname = firstname;
        this.familyname = familyname;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Contact))
            return false;
        Contact contact = (Contact) o;
        return Objects.equals(this.id, contact.id) && Objects.equals(this.firstname, contact.firstname)
                && Objects.equals(this.familyname, contact.familyname) &&
                Objects.equals(this.phonenumber, contact.phonenumber) &&
                Objects.equals(this.email, contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstname, this.familyname, this.phonenumber, this.email);
    }
}