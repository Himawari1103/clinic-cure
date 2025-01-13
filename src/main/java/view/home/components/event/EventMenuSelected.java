package view.home.components.event;

import constants.MenuType;

public interface EventMenuSelected {

//    public void menuSelected(int menuIndex, int subMenuIndex);
    public void menuSelected(MenuType menuType, MenuType subMenuType);
}
