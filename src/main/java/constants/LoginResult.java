package constants;

public enum LoginResult {
    USERNAME_ERR("Username does not exist"),
    PASSWORD_ERR("Password is incorrect"),
    SUCCESS;

    private String ms;

    LoginResult(String ms) {
        this.ms = ms;
    }

    LoginResult() {

    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }
}
