package constants;

import java.util.Objects;

public enum StaffRole {
    DOCTOR("Bác sĩ"),
    STAFF("Nhân viên");

    private String detail;

    StaffRole(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static StaffRole getStaffRoleFromDetail(String detail){
        for (StaffRole staffRole:StaffRole.values()) {
            if(Objects.equals(staffRole.getDetail(), detail)) {
                return staffRole;
            }
        }
        return null;
    }
}
