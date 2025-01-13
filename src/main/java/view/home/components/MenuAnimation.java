package view.home.components;

import java.awt.*;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;

public class MenuAnimation {

    private final MigLayout layout;
    private final MenuItem menuItem;
    private Animator animator;
    private boolean open;

    public MenuAnimation(MigLayout layout, Component component) {
        this.layout = layout;
        this.menuItem = (MenuItem) component;
    }

    public void openMenu() {
        open = true;

        float h = 60;
        menuItem.setAlpha(1f);
        layout.setComponentConstraints(menuItem, "h " + h + "!");

        menuItem.getMenuButton().setFont(new Font("sansserif", Font.BOLD, 18));

        menuItem.revalidate();
        menuItem.repaint();

    }

    public void closeMenu() {
        open = false;

        float h = 60;
        menuItem.setAlpha(0);
        layout.setComponentConstraints(menuItem, "h " + h + "!");

        menuItem.getMenuButton().setFont(new Font("sansserif", Font.BOLD, 15));

        menuItem.revalidate();
        menuItem.repaint();
    }
}
