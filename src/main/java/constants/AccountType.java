package constants;

import java.util.Objects;

public enum AccountType {
    ADMIN("Quản trị viên"),
    STAFF("Nhân viên"),
    EMPTY(""),
    PATIENT("Bệnh nhân");

    private String detail;

    AccountType(String detail) {
        this.detail = detail;
    }

    AccountType() {

    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static AccountType getAccountTypeFromDetail(String detail){
        for (AccountType accountType:AccountType.values()) {
            if(Objects.equals(accountType.getDetail(), detail)) {
                return accountType;
            }
        }
        return null;
    }
}
