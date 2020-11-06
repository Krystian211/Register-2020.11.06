package root.model;

public class User implements Comparable<User>{
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int compareTo(User user) {
        return this.username.compareToIgnoreCase(user.username);

    }

    @Override
    public String toString() {
        return this.username;
    }
}
