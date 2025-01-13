package view.home.components.panels;

import constants.MenuType;
import view.home.main.panels.*;

import java.awt.*;
import javax.swing.border.MatteBorder;

public class MainPanel extends javax.swing.JPanel {
    private MenuType menuType;

    public MainPanel() {
        initComponents();
        setOpaque(false);
        setLayout(new BorderLayout());
//        setBorder(new EmptyBorder(10, 20, 10, 20));
        setBorder(new MatteBorder(10, 20, 10, 20, Color.WHITE));
    }

    public void showForm(MenuType menuType) {
        if(this.menuType == null || this.menuType != menuType){
            removeAll();
            switch (menuType) {
                case REPORT -> add(new ReportManagement());
                case PATIENT -> add(new PatientsManagement());
                case STAFF -> add(new StaffManagement());
                case APPOINTMENT -> add(new AppointmentManagement());
                case RECORD -> add(new RecordsMagagement());
                case RECEIPT -> add(new ReceiptsManagement());
                case ACCOUNT -> add(new AccountManagement());
            }
            repaint();
            revalidate();
            this.menuType = menuType;
        }
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
