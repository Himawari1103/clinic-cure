package view.home.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import constants.MenuType;
import net.miginfocom.swing.MigLayout;
import view.home.components.event.EventMenu;
import view.home.components.event.EventMenuSelected;
import view.home.components.model.ModelMenu;

public class MenuItem extends javax.swing.JPanel {

    public ModelMenu getMenu() {
        return menu;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

//    public boolean isOpen() {
//        return open;
//    }
//
//    public void setOpen(boolean open) {
//        this.open = open;
//    }

    public EventMenuSelected getEventSelected() {
        return eventSelected;
    }

    public void setEventSelected(EventMenuSelected eventSelected) {
        this.eventSelected = eventSelected;
    }

    private float alpha;
    private ModelMenu menu;
//    private boolean open = false;
    private EventMenuSelected eventSelected;

    private MenuButton menuButton;

    public MenuButton getMenuButton() {
        return menuButton;
    }

    public void setMenuButton(MenuButton menuButton) {
        this.menuButton = menuButton;
    }

    public MenuItem(ModelMenu menu, EventMenu event, EventMenuSelected eventSelected, MenuType menuType) {
        initComponents();
        this.menu = menu;
        this.eventSelected = eventSelected;
        setOpaque(false);
        setLayout(new MigLayout("wrap, fillx, insets 0", "[fill]", "[fill, 40!]0[fill, 35!]"));
        menuButton = new MenuButton(menu.getIcon(), "      " + menu.getMenuType().getDetail());
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
//                if (menu.getSubMenuType().length > 0) {
//                    if (event.menuPressed(MenuItem.this, !open)) {
//                        open = !open;
//                    }
//                }
                event.menuPressed(MenuItem.this, true);
                eventSelected.menuSelected(menuType, MenuType.EMPTY);
            }
        });
        add(menuButton);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        int width = getWidth();
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLUE);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.fillRect(0, 0, width, 38);
        super.paintComponent(grphcs);
    }
}
