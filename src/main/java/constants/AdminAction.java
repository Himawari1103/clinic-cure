package constants;

public enum AdminAction {
    ADD("THÊM"),
    UPDATE("CẬP NHẬT"),
    DELETE("XÓA"),
    SEARCH("TÌM KIẾM");

    private String detail;

    AdminAction(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
