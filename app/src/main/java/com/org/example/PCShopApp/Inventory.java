/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.org.example.PCShopApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

/**
 *
 * @author Catmosphere, Cryptic
 */
public class Inventory extends javax.swing.JFrame {

    /**
     * Creates new form Inventory
     */
    private static final String PLACEHOLDER = "Add Stock";
    Newnew addItemform;

    public Inventory() {
        FlatLaf.registerCustomDefaultsSource("com.org.example.PCShopApp");
        FlatDarkLaf.setup();
        initComponents();
        setLocationRelativeTo(null);
        setupTableModel();
        loadInventoryTable();
        setupPlaceholder();
        
        BTRemoveItem.addActionListener(e -> removeSelectedPart());
        TFAddStock.addActionListener(e -> addStockToSelectedPart());
    }

    private void setupPlaceholder() {
        TFAddStock.setText(PLACEHOLDER);
        TFAddStock.setForeground(java.awt.Color.GRAY);
        TFAddStock.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (TFAddStock.getText().equals(PLACEHOLDER)) {
                    TFAddStock.setText("");
                    TFAddStock.setForeground(java.awt.Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (TFAddStock.getText().isEmpty()) {
                    TFAddStock.setText(PLACEHOLDER);
                    TFAddStock.setForeground(java.awt.Color.GRAY);
                }
            }
        });
    }

    private void setupTableModel() {
        String[] columns = {"Part ID", "Part Name", "Model", "Type", "Stock", "Price", "Restock?"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int idx) {
                switch (idx) {
                    case 0:
                        return Integer.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return Double.class;
                    case 6:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };
        jTable1.setModel(model);
    }

    public void loadInventoryTable() {
        String sql = "SELECT p.`Part ID`, p.`Part Name`, p.`Model`, "
                + "c.`Type` AS TypeName, p.`Stock`, p.`Price`, p.`Restock?` "
                + "FROM partlist p JOIN componenttype c ON p.`Type` = c.`ID`";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("Part ID"),
                    rs.getString("Part Name"),
                    rs.getString("Model"),
                    rs.getString("TypeName"),
                    rs.getInt("Stock"),
                    rs.getDouble("Price"),
                    rs.getBoolean("Restock?")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStockToSelectedPart() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a part to add stock.");
            return;
        }
        String text = TFAddStock.getText();
        if (text.equals(PLACEHOLDER) || text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter stock quantity first.");
            return;
        }
        int partId = (int) jTable1.getValueAt(selectedRow, 0);
        try {
            int addQty = Integer.parseInt(text);
            String sql = "UPDATE partlist SET `Stock` = `Stock` + ? WHERE `Part ID` = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, addQty);
                stmt.setInt(2, partId);
                stmt.executeUpdate();
            }
            loadInventoryTable();
            TFAddStock.setText(PLACEHOLDER);
            TFAddStock.setForeground(java.awt.Color.GRAY);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void removeSelectedPart() {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a part to remove.");
            return;
        }

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove the selected part?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int partId = (int) jTable1.getValueAt(row, 0);
        String sql = "DELETE FROM partlist WHERE `Part ID` = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, partId);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Part removed successfully.");
                loadInventoryTable();
            } else {
                JOptionPane.showMessageDialog(this, "No part was removed. It may have already been deleted.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing part: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        BTBack = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        BTAddItem = new javax.swing.JButton();
        BTRemoveItem = new javax.swing.JButton();
        TFAddStock = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Shop Inventory");

        jPanel2.setBackground(new java.awt.Color(88, 105, 163));

        BTBack.setBackground(new java.awt.Color(40, 53, 98));
        BTBack.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BTBack.setForeground(new java.awt.Color(255, 255, 255));
        BTBack.setText("Back");
        BTBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTBackActionPerformed(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Port Name", "Model", "Type", "Stock", "Price", "Need to Restock?"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel4.setBackground(new java.awt.Color(40, 53, 98));
        jPanel4.setPreferredSize(new java.awt.Dimension(98, 34));

        jLabel1.setBackground(new java.awt.Color(40, 53, 98));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inventory List");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(8, 8, 8))
        );

        BTAddItem.setBackground(new java.awt.Color(40, 53, 98));
        BTAddItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BTAddItem.setForeground(new java.awt.Color(255, 255, 255));
        BTAddItem.setText("Add Item");
        BTAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTAddItemActionPerformed(evt);
            }
        });

        BTRemoveItem.setBackground(new java.awt.Color(40, 53, 98));
        BTRemoveItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BTRemoveItem.setForeground(new java.awt.Color(255, 255, 255));
        BTRemoveItem.setText("Remove Item");
        BTRemoveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTRemoveItemActionPerformed(evt);
            }
        });

        TFAddStock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TFAddStock.setText("Add Stock");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(BTBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(BTAddItem)
                        .addGap(18, 18, 18)
                        .addComponent(BTRemoveItem))
                    .addComponent(TFAddStock))
                .addGap(23, 23, 23))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BTBack)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(TFAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BTAddItem)
                            .addComponent(BTRemoveItem))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTAddItemActionPerformed
        new Newnew(this).setVisible(true);
    }//GEN-LAST:event_BTAddItemActionPerformed

    private void BTRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTRemoveItemActionPerformed
    }//GEN-LAST:event_BTRemoveItemActionPerformed

    private void BTBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTBackActionPerformed
        DataToAccess dataToAccess = new DataToAccess();
        dataToAccess.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inventory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTAddItem;
    private javax.swing.JButton BTBack;
    private javax.swing.JButton BTRemoveItem;
    private javax.swing.JTextField TFAddStock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
