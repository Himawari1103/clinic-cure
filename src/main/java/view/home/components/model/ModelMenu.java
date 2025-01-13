package view.home.components.model;

import constants.MenuType;

import javax.swing.Icon;

public class ModelMenu {

    private Icon icon;
    private MenuType menuType;
    private MenuType[] subMenuType;

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public MenuType[] getSubMenuType() {
        return subMenuType;
    }

    public void setSubMenuType(MenuType[] subMenuType) {
        this.subMenuType = subMenuType;
    }

    public ModelMenu() {
    }

    public ModelMenu(Icon icon, MenuType menuType, MenuType... subMenuType) {
        this.icon = icon;
        this.menuType = menuType;
        this.subMenuType = subMenuType;
    }
}
