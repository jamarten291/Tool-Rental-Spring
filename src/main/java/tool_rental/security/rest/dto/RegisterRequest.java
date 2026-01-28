package tool_rental.security.rest.dto;

public class RegisterRequest {

    private String username;
    private String password;
    private String rol; // Ej: "ADMIN", "USER"

    // Constructor vacío
    public RegisterRequest() {
    }

    // Constructor con todos los campos
    public RegisterRequest(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // toString()
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}

