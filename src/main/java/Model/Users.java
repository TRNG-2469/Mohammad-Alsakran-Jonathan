package Model;

public class Users {
    private int user_id;
    private String username;
    private String password;
    private boolean role;
    private String first_name;
    private String last_name;
    private int department_id;

    public Users() {
    }

    public Users(int user_id,String username, String password, boolean role, String first_name, String last_name, int department_id) {
        this.user_id = user_id;
        this.last_name = last_name;
        this.department_id = department_id;
        this.first_name = first_name;
        this.role = role;
        this.password = password;
        this.username = username;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", role=" + role +
                ", department_id=" + department_id +
                '}';
    }
}
