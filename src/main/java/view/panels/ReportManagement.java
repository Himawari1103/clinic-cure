/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.panels;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.main.ReportController;
import view.components.main.components.Card;
import view.components.main.components.icon.GoogleMaterialDesignIcons;
import view.components.main.components.icon.IconFontSwing;
import view.components.main.components.scrollbar.ScrollBarCustom;
import view.components.main.model.ModelCard;

/**
 * @author Chi Cute
 */
public class ReportManagement extends javax.swing.JPanel {

    Card cardPatient;
    Card cardIncome;
    double sumIncome = 0;
    int sumVisit = 0;

    DailyRecord dailyRecord;
    PatientsList patientsList;
    DoctorsList doctorsList;

    public ReportManagement() {
        initCard();
        initComponents();
        init();
    }

    public void initCard() {
        Icon icon1 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PEOPLE, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        cardPatient = new Card();
        cardPatient.setBackground(new java.awt.Color(255, 28, 215));
        cardPatient.setColorGradient(new java.awt.Color(211, 28, 215));
        cardPatient.setData(new ModelCard("Lượt khám", 5100, 100, icon1));
        Icon icon2 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.MONETIZATION_ON, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        cardIncome = new Card();
        cardIncome.setBackground(new java.awt.Color(10, 30, 214));
        cardIncome.setColorGradient(new java.awt.Color(72, 111, 252));
        cardIncome.setData(new ModelCard("Doanh thu", 2000, 100, icon2));
    }

    public boolean hasTextEmpty() {
        return startYearTF.getText().trim().isEmpty()
                || startDateTF.getText().trim().isEmpty()
                || endYearTF.getText().trim().isEmpty()
                || endDateTF.getText().trim().isEmpty();
    }

    public void createDailyRecord() {
        dailyRecord = new DailyRecord();
        if (!hasTextEmpty()) {
            LocalDate start = LocalDate.of(
                    Integer.parseInt(startYearTF.getText().trim()),
                    Integer.parseInt((String) Objects.requireNonNull(startMonthComboBox.getSelectedItem())),
                    Integer.parseInt(startDateTF.getText().trim()));

            LocalDate end = LocalDate.of(
                    Integer.parseInt(endYearTF.getText().trim()),
                    Integer.parseInt((String) Objects.requireNonNull(endMonthComboBox.getSelectedItem())),
                    Integer.parseInt(endDateTF.getText().trim()));
            dailyRecord.addData(start, end);
            sumIncome = ReportController.computeSumIncome(start, end);
            sumVisit = ReportController.computeSumVisit(start, end);
            cardIncome.setValueDouble(sumIncome);
            cardPatient.setValueInt(sumVisit);
        } else {
            dailyRecord.addData();
            sumIncome = ReportController.computeSumIncome();
            sumVisit = ReportController.computeSumVisit();
            cardIncome.setValueDouble(sumIncome);
            cardPatient.setValueInt(sumVisit);
        }
    }


    public void createPatientsList() {
        patientsList = new PatientsList();
        if (!hasTextEmpty()) {
            LocalDate start = LocalDate.of(
                    Integer.parseInt(startYearTF.getText().trim()),
                    Integer.parseInt((String) Objects.requireNonNull(startMonthComboBox.getSelectedItem())),
                    Integer.parseInt(startDateTF.getText().trim()));

            LocalDate end = LocalDate.of(
                    Integer.parseInt(endYearTF.getText().trim()),
                    Integer.parseInt((String) Objects.requireNonNull(endMonthComboBox.getSelectedItem())),
                    Integer.parseInt(endDateTF.getText().trim()));
            patientsList.addData(start, end);
            sumIncome = ReportController.computeSumIncome(start, end);
            sumVisit = ReportController.computeSumVisit(start, end);
            cardIncome.setValueDouble(sumIncome);
            cardPatient.setValueInt(sumVisit);
        } else {
            patientsList.addData();
            sumIncome = ReportController.computeSumIncome();
            sumVisit = ReportController.computeSumVisit();
            cardIncome.setValueDouble(sumIncome);
            cardPatient.setValueInt(sumVisit);
        }
    }

    public void createDoctorsList() {
        doctorsList = new DoctorsList();
    }

    public void init() {
        createDailyRecord();
        showReport(dailyRecord);
        createPatientsList();
        createDoctorsList();

        sumIncome = ReportController.computeSumIncome();
        sumVisit = ReportController.computeSumVisit();
        cardIncome.setValueDouble(sumIncome);
        cardPatient.setValueInt(sumVisit);

        replacePanel.setOpaque(false);
        replacePanel.setLayout(new BorderLayout());

        addEvent();

//        replacePanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        choosingComboBox.setBackground(Color.WHITE);
        choosingComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = choosingComboBox.getSelectedIndex();
                if (index == 0) {
                    createDailyRecord();
                    showReport(dailyRecord);
                } else if (index == 1) {
                    createPatientsList();
                    showReport(patientsList);
                } else if (index == 2) {
                    createDoctorsList();
                    showReport(doctorsList);
                }
            }
        });
        startMonthComboBox.setBackground(Color.WHITE);
        endMonthComboBox.setBackground(Color.WHITE);
    }

    public void showReport(JPanel jPanel) {
        replacePanel.removeAll();
        replacePanel.add(jPanel);
        replacePanel.repaint();
        replacePanel.revalidate();
    }

    public void addEvent() {
        JTextField[] arrText = {startDateTF, startYearTF, endDateTF, endYearTF};
        for (int i = 0; i < arrText.length; i++) {
            arrText[i].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    System.out.println("inserting");
                    int index = choosingComboBox.getSelectedIndex();
                    if (index == 0) {
                        createDailyRecord();
                        showReport(dailyRecord);
                    } else if (index == 1) {
                        createPatientsList();
                        showReport(patientsList);
                    } else if (index == 2) {
                        createDoctorsList();
                        showReport(doctorsList);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    int index = choosingComboBox.getSelectedIndex();
                    if (index == 0) {
                        createDailyRecord();
                        showReport(dailyRecord);
                    } else if (index == 1) {
                        createPatientsList();
                        showReport(patientsList);
                    } else if (index == 2) {
                        createDoctorsList();
                        showReport(doctorsList);
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    int index = choosingComboBox.getSelectedIndex();
                    if (index == 0) {
                        createDailyRecord();
                        showReport(dailyRecord);
                    } else if (index == 1) {
                        createPatientsList();
                        showReport(patientsList);
                    } else if (index == 2) {
                        createDoctorsList();
                        showReport(doctorsList);
                    }
                }
            });
        }

        startMonthComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = choosingComboBox.getSelectedIndex();
                if (index == 0) {
                    createDailyRecord();
                    showReport(dailyRecord);
                } else if (index == 1) {
                    createPatientsList();
                    showReport(patientsList);
                } else if (index == 2) {
                    createDoctorsList();
                    showReport(doctorsList);
                }
            }
        });

        endMonthComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = choosingComboBox.getSelectedIndex();
                if (index == 0) {
                    createDailyRecord();
                    showReport(dailyRecord);
                } else if (index == 1) {
                    createPatientsList();
                    showReport(patientsList);
                } else if (index == 2) {
                    createDoctorsList();
                    showReport(doctorsList);
                }
            }
        });
    }

//    public void setComboBoxCustomDisabled(JComboBox<?> comboBox, boolean disabled) {
//        if (disabled) {
//            comboBox.setEnabled(false);
//            comboBox.setRenderer(new DefaultListCellRenderer() {
//                @Override
//                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                    label.setBackground(Color.GRAY);
//                    label.setForeground(Color.BLACK);
//                    label.setOpaque(true);
//                    return label;
//                }
//            });
//        } else {
//            comboBox.setEnabled(true);
//            comboBox.setRenderer(new DefaultListCellRenderer() {
//                @Override
//                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                    label.setBackground(Color.WHITE);
//                    label.setForeground(Color.BLACK);
//                    if (isSelected) {
//                        label.setBackground(Color.LIGHT_GRAY);
//                    }
//                    label.setOpaque(true);
//                    return label;
//                }
//            });
//            comboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
//                @Override
//                public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
//                    JComponent popup = (JComponent) comboBox.getUI().getAccessibleChild(comboBox, 0);
//                    if (popup instanceof JPopupMenu) {
//                        for (Component component : popup.getComponents()) {
//                            if (component instanceof JScrollPane scrollPane) {
//
//                                scrollPane.getViewport().setBackground(Color.WHITE);
//                                scrollPane.setVerticalScrollBar(new ScrollBarCustom());
//                                JPanel p = new JPanel();
//                                p.setBackground(Color.WHITE);
//                                scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
//                }
//
//                @Override
//                public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
//                }
//            });
//        }
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        totalTurnoverTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        totalVisitationTextField = new javax.swing.JTextField();
        replacePanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        startYearTF = new javax.swing.JTextField();
        endYearTF = new javax.swing.JTextField();
        choosingComboBox = new javax.swing.JComboBox<>();
        startMonthComboBox = new javax.swing.JComboBox<>();
        endMonthComboBox = new javax.swing.JComboBox<>();
        startDateTF = new javax.swing.JTextField();
        endDateTF = new javax.swing.JTextField();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Báo cáo thống kê", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(229, 245, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)));


//        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
//        jLabel8.setText("Tổng doanh thu");

        totalTurnoverTextField.setEditable(false);
        totalTurnoverTextField.setBackground(new java.awt.Color(153, 255, 0));
        totalTurnoverTextField.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        totalTurnoverTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        totalTurnoverTextField.setBorder(null);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 153, 153));
        jLabel10.setText("VNĐ");


        totalVisitationTextField.setEditable(false);
        totalVisitationTextField.setBackground(new java.awt.Color(255, 204, 204));
        totalVisitationTextField.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        totalVisitationTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        totalVisitationTextField.setBorder(null);

        javax.swing.GroupLayout replacePanelLayout = new javax.swing.GroupLayout(replacePanel);
        replacePanel.setLayout(replacePanelLayout);
        replacePanelLayout.setHorizontalGroup(
                replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        replacePanelLayout.setVerticalGroup(
                replacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 319, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cardIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cardPatient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(replacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cardIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cardPatient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(replacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(229, 245, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Ngày bắt đầu:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Ngày kết thúc:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("-- Chọn thời gian --");


        refreshButton.setBackground(new java.awt.Color(255, 204, 204));
        refreshButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        refreshButton.setText("Làm mới");
        refreshButton.setFocusPainted(false);
        refreshButton.setFocusable(false);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("-- Chọn thông tin muốn xem --");

        startYearTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        startYearTF.setText("2025");

        endYearTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        endYearTF.setText("2025");

        choosingComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Thống kê theo ngày", "Thống kê theo bệnh nhân", "Thống kê theo bác sĩ"}));
        choosingComboBox.setFocusable(false);

        startMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
        startMonthComboBox.setFocusable(false);

        endMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
        endMonthComboBox.setFocusable(false);

        startDateTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        endDateTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(startDateTF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(startMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(startYearTF, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(22, 22, 22)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(endDateTF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(endMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(endYearTF, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(84, 84, 84)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(choosingComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(87, 87, 87)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(72, 72, 72))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(refreshButton)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                )
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGap(10, 10, 10)
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel5)
                                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(startYearTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(startMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(startDateTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(endYearTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(endMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(endDateTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(choosingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 980, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 620, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        startDateTF.setText("");
        startYearTF.setText("");
        startMonthComboBox.setSelectedItem("1");

        endDateTF.setText("");
        endMonthComboBox.setSelectedItem("1");
        endYearTF.setText("");

        choosingComboBox.setSelectedItem("Thống kê theo ngày");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> choosingComboBox;
    private javax.swing.JTextField endDateTF;
    private javax.swing.JComboBox<String> endMonthComboBox;
    private javax.swing.JTextField endYearTF;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel replacePanel;
    private javax.swing.JTextField startDateTF;
    private javax.swing.JComboBox<String> startMonthComboBox;
    private javax.swing.JTextField startYearTF;
    private javax.swing.JTextField totalTurnoverTextField;
    private javax.swing.JTextField totalVisitationTextField;
    // End of variables declaration//GEN-END:variables
}
