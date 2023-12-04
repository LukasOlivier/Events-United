package be.howest.ti.mars.logic.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User {
    private final int userId;
    private final FullName fullName;
    private final Date birthDate;
    private final int height;
    private final String gender;
    private final String description;
    private final UserExtraInformation userExtraInformation;


    public User(int userId, FullName fullName, Date birthDate, int height, String gender, String description, UserExtraInformation userExtraInformation) {
        this.userId = userId;
        this.birthDate = Objects.requireNonNull(birthDate);
        this.height = height;
        this.gender = Objects.requireNonNull(gender);
        this.description = Objects.requireNonNull(description);
        this.userExtraInformation = userExtraInformation;
        this.fullName = fullName;
    }

    public User(int userId, FullName fullName, Date birthDate, int height, String gender, String description) {
        this(userId, fullName ,birthDate,height,gender,description, new UserExtraInformation(new ArrayList<>(),  new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    public User(int userId, FullName fullName, Date birthdate, int height, String gender, String description, List<String> userInterests) {
        this(userId, fullName, birthdate, height, gender, description, new UserExtraInformation(userInterests, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    public List<String> getInterests() {
        return userExtraInformation.getInterest();
    }

    public String getBirthDate() {
        return parseMillisecondsToDate(birthDate.getTime());
    }

    public List<Event> getFavorites() {
        return userExtraInformation.getFavorites();
    }
    public List<User> getFriends() {
        return userExtraInformation.getFriends();
    }

    public List<Ticket> getTickets() {
        return userExtraInformation.getTickets();
    }

    public int getUserId() {
        return userId;
    }

    private String parseMillisecondsToDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date resultdate = new Date(milliseconds);
        return sdf.format(resultdate);
    }

    public int getAge() {
        return (int) ((new Date().getTime() - birthDate.getTime()) / 1000 / 60 / 60 / 24 / 365);
    }

    public String getFirstName() {
        return fullName.getFirstname();
    }

    public String getLastName() {
        return fullName.getLastname();
    }

    public int getHeight() {
        return height;
    }

    public String getGender() {
        return gender;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", birthDate=" + birthDate +
                ", height=" + height +
                ", gender='" + gender + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
