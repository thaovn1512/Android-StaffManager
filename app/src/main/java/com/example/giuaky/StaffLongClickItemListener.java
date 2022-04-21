package com.example.giuaky;

public interface StaffLongClickItemListener {

    static int STAFF_LONG_CLICK_VIEW = 1;
    static int STAFF_LONG_CLICK_EDIT = 2;
    static int STAFF_LONG_CLICK_REMOVE = 3;

    public void onStaffClick(String staffID, int typeClick);

}
