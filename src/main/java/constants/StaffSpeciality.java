package constants;

import java.util.Objects;

public enum StaffSpeciality {
    CARDIOLOGY("Tim mạch"),
    NEUROLOGY("Thần kinh"),
    DERMATOLOGY("Da liễu"),
    OPHTHALMOLOGY("Nhãn khoa"),
    OTOLARYNGOLOGY("Tai mũi họng"),
    ENDOCRINOLOGY("Nội tiết"),
    HEMATOLOGY("Huyết học"),
    EMPTY("Không có");

    private String detail;

    StaffSpeciality(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static StaffSpeciality getStaffSpecialityFromDetail(String detail){
        for (StaffSpeciality staffSpeciality:StaffSpeciality.values()) {
            if(Objects.equals(staffSpeciality.getDetail(), detail)) {
                return staffSpeciality;
            }
        }
        return null;
    }
}
