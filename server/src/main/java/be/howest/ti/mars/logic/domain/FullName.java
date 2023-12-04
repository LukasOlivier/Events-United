package be.howest.ti.mars.logic.domain;

public class FullName {

    private String firstname;
    private String lastname;

    public FullName(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
