package constants;

public enum MenuType {
    PATIENT("Bệnh nhân"),
    STAFF("Nhân viên"),
    APPOINTMENT_AND_RECEIPT("Lịch hẹn và hóa đơn"),
    RECORD("Hồ sơ"),
    REPORT_AND_ACCOUNT("Báo cáo thống kê và tài khoản"),
    EMPTY;


    private String detail;

    MenuType(String detail) {
        this.detail = detail;
    }

    MenuType() {

    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
