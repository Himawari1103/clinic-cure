package constants;

import java.util.Objects;

public enum Gender {
    MALE("NAM"),
    FEMALE("NỮ"),
    OTHER;

    private String detail;

    Gender(String detail) {
        this.detail = detail;
    }

    Gender() {}

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static Gender getGenderFromDetail(String detail){
        for (Gender gender:Gender.values()) {
            if(Objects.equals(gender.getDetail(), detail)) {
                return gender;
            }
        }
        return Gender.OTHER;
    }
}
