package constants;

public enum MenuType {
    REPORT("Báo cáo thống kê"),
    PATIENT("Bệnh nhân"),
    STAFF("Nhân viên"),
    APPOINTMENT("Lịch hẹn"),
    RECEIPT("Hóa đơn"),
    RECORD("Hồ sơ"),
    ACCOUNT("Tài khoản"),
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
