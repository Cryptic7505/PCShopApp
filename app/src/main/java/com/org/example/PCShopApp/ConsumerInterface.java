package com.org.example.PCShopApp;

import java.awt.Color;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;


public class ConsumerInterface extends javax.swing.JFrame {
    private double total;
    private Map<JComboBox<String>, Double> componentPrices = new HashMap<>();
    private List<ComponentConfig> componentConfigs = new ArrayList<>();
    private Map<String, Double> priceCache = new HashMap<>();
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ConsumerInterface() {
        initComponents();
        initializeComponentConfigs();
        populateComboBoxes();

        // Initialize all prices to 0
        componentConfigs.forEach(config -> {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            config.priceField.setText(currencyFormat.format(0.00));
        });

        // Set initial total
        updateTotalDisplay();
    }
    private class ComponentConfig {

        public JComboBox<String> comboBox;
        public JTextField priceField;
        public int typeId;

        public ComponentConfig(JComboBox<String> comboBox,JTextField priceField,int typeId) {
            this.comboBox = comboBox;
            this.priceField = priceField;
            this.typeId = typeId;
        }
    }
    private void populateComboBoxes() {
        populateComboBox(CBCPU, 2);        // CPU
        populateComboBox(CBMOBO, 1);       // Motherboard
        populateComboBox(CBGPU, 3);        // GPU
        populateComboBox(CBRAM, 4);        // RAM
        populateComboBox(CBStorage, 5);    // Storage
        populateComboBox(CBPSU, 6);        // Power Supply
        populateComboBox(CBCooler, 7);     // Cooling System
        populateComboBox(CBCase, 8);       // Case
        // Add other ComboBoxes as needed
    }
    
    private void initializeComponentConfigs() {
        // Creates associations between:
        // - Component selection combo boxes
        // - Price display text panes
        // - Part type IDs

        // Add all component configurations
        componentConfigs.add(new ComponentConfig(CBCPU, TFCPUPrice, 2));       // CPU
        componentConfigs.add(new ComponentConfig(CBMOBO, TFMOBOPrice, 1));     // Motherboard
        componentConfigs.add(new ComponentConfig(CBGPU, TFGPUPrice, 3));       // GPU
        componentConfigs.add(new ComponentConfig(CBRAM, TFRAMPrice, 4));       // RAM
        componentConfigs.add(new ComponentConfig(CBStorage, TFStoragePrice, 5)); // Storage
        componentConfigs.add(new ComponentConfig(CBPSU, TFPSUPrice, 6));       // PSU
        componentConfigs.add(new ComponentConfig(CBCooler, TFCoolerPrice, 7)); // Cooler
        componentConfigs.add(new ComponentConfig(CBCase, TFCasePrice, 8));     // Case

        // Add listeners to all components
        componentConfigs.forEach(config -> {
            config.comboBox.addActionListener(e -> {
                updateComponentPriceDisplay(config);
                updateTotalDisplay();
            });
        });
    }
    private void updateComponentPriceDisplay(ComponentConfig config) {
        // Called when combo box selection changes
        String selected = (String) config.comboBox.getSelectedItem();
        double price = 0.0;

        if (selected != null && !"none".equals(selected)) {
            price = priceCache.getOrDefault(selected, 0.0);
        }
        // Update individual price display
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        String formattedPrice = currencyFormat.format(price);

        // Update both the price pane and cache
        config.priceField.setText(formattedPrice);
        componentPrices.put(config.comboBox, price);
    }
    
    
    public class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/mydb";
        private static final String USER = "root";
        private static final String PASSWORD = "12345678";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
    private void populateComboBox(JComboBox<String> comboBox, int typeId) {
        // Loads parts from database for a specific category
        // Populates both the combo box and price cache
        comboBox.removeAllItems();
        comboBox.addItem("none");

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT `Part Name`, Price FROM partlist WHERE Type = ?")) {

            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String partName = rs.getString("Part Name");
                double price = rs.getDouble("Price");
                comboBox.addItem(partName);
                priceCache.put(partName, price);  // Cache prices
            }
        } catch (SQLException e) {
            // Handle error
        }
        priceCache.put("none", 0.0);
    }
    private void updateTotalDisplay() {
        // Calculates sum of all component prices
        // Calculate total from cached prices instead of parsing text
        double total = componentConfigs.stream()
                .mapToDouble(config -> componentPrices.getOrDefault(config.comboBox, 0.0))
                .sum();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        TAPartsTotal.setText(currencyFormat.format(total));
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        TPOptions = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        CBCPU = new javax.swing.JComboBox<>();
        CBMOBO = new javax.swing.JComboBox<>();
        CBPSU = new javax.swing.JComboBox<>();
        CBRAM = new javax.swing.JComboBox<>();
        PanCPU = new javax.swing.JPanel();
        TFCPUPrice = new javax.swing.JTextField();
        PanMOBO = new javax.swing.JPanel();
        TFMOBOPrice = new javax.swing.JTextField();
        PanPSU = new javax.swing.JPanel();
        TFPSUPrice = new javax.swing.JTextField();
        PanRAM = new javax.swing.JPanel();
        TFRAMPrice = new javax.swing.JTextField();
        PanCooler = new javax.swing.JPanel();
        TFCoolerPrice = new javax.swing.JTextField();
        CBCooler = new javax.swing.JComboBox<>();
        CBCase = new javax.swing.JComboBox<>();
        PanCase = new javax.swing.JPanel();
        TFCasePrice = new javax.swing.JTextField();
        CBGPU = new javax.swing.JComboBox<>();
        PanGPU = new javax.swing.JPanel();
        TFGPUPrice = new javax.swing.JTextField();
        CBStorage = new javax.swing.JComboBox<>();
        PanStorage = new javax.swing.JPanel();
        TFStoragePrice = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        TFFullName = new javax.swing.JTextField();
        TFPhoneNO = new javax.swing.JTextField();
        TFEmail = new javax.swing.JTextField();
        TFAdress = new javax.swing.JTextField();
        TFCity = new javax.swing.JTextField();
        TFProvince = new javax.swing.JTextField();
        BTBuild = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TAPartsTotal = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PCBuilder");
        setForeground(java.awt.Color.darkGray);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 48, 85));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setPreferredSize(new java.awt.Dimension(1283, 979));

        TPOptions.setBackground(new java.awt.Color(40, 53, 98));
        TPOptions.setPreferredSize(new java.awt.Dimension(1263, 827));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setFocusable(false);
        jPanel2.setMaximumSize(new java.awt.Dimension(1263, 767));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(1263, 767));
        jPanel2.setRequestFocusEnabled(false);

        CBCPU.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBCPU.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBCPU.setPreferredSize(new java.awt.Dimension(265, 44));
        CBCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBCPUActionPerformed(evt);
            }
        });

        CBMOBO.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBMOBO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBMOBO.setPreferredSize(new java.awt.Dimension(265, 44));
        CBMOBO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBMOBOActionPerformed(evt);
            }
        });

        CBPSU.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBPSU.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBPSU.setPreferredSize(new java.awt.Dimension(265, 44));
        CBPSU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBPSUActionPerformed(evt);
            }
        });

        CBRAM.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBRAM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBRAM.setPreferredSize(new java.awt.Dimension(265, 44));
        CBRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBRAMActionPerformed(evt);
            }
        });

        PanCPU.setPreferredSize(new java.awt.Dimension(265, 276));

        TFCPUPrice.setEditable(false);
        TFCPUPrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFCPUPrice.setAutoscrolls(false);
        TFCPUPrice.setFocusable(false);

        javax.swing.GroupLayout PanCPULayout = new javax.swing.GroupLayout(PanCPU);
        PanCPU.setLayout(PanCPULayout);
        PanCPULayout.setHorizontalGroup(
            PanCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCPULayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFCPUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanCPULayout.setVerticalGroup(
            PanCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCPULayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFCPUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        PanMOBO.setPreferredSize(new java.awt.Dimension(265, 276));

        TFMOBOPrice.setEditable(false);
        TFMOBOPrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFMOBOPrice.setAutoscrolls(false);
        TFMOBOPrice.setFocusable(false);

        javax.swing.GroupLayout PanMOBOLayout = new javax.swing.GroupLayout(PanMOBO);
        PanMOBO.setLayout(PanMOBOLayout);
        PanMOBOLayout.setHorizontalGroup(
            PanMOBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanMOBOLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFMOBOPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanMOBOLayout.setVerticalGroup(
            PanMOBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanMOBOLayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFMOBOPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        PanPSU.setPreferredSize(new java.awt.Dimension(265, 276));

        TFPSUPrice.setEditable(false);
        TFPSUPrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFPSUPrice.setAutoscrolls(false);
        TFPSUPrice.setFocusable(false);

        javax.swing.GroupLayout PanPSULayout = new javax.swing.GroupLayout(PanPSU);
        PanPSU.setLayout(PanPSULayout);
        PanPSULayout.setHorizontalGroup(
            PanPSULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanPSULayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFPSUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanPSULayout.setVerticalGroup(
            PanPSULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanPSULayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFPSUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        PanRAM.setPreferredSize(new java.awt.Dimension(265, 276));

        TFRAMPrice.setEditable(false);
        TFRAMPrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFRAMPrice.setAutoscrolls(false);
        TFRAMPrice.setFocusable(false);

        javax.swing.GroupLayout PanRAMLayout = new javax.swing.GroupLayout(PanRAM);
        PanRAM.setLayout(PanRAMLayout);
        PanRAMLayout.setHorizontalGroup(
            PanRAMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanRAMLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFRAMPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanRAMLayout.setVerticalGroup(
            PanRAMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanRAMLayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFRAMPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        PanCooler.setPreferredSize(new java.awt.Dimension(265, 276));

        TFCoolerPrice.setEditable(false);
        TFCoolerPrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFCoolerPrice.setAutoscrolls(false);
        TFCoolerPrice.setFocusable(false);

        javax.swing.GroupLayout PanCoolerLayout = new javax.swing.GroupLayout(PanCooler);
        PanCooler.setLayout(PanCoolerLayout);
        PanCoolerLayout.setHorizontalGroup(
            PanCoolerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCoolerLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFCoolerPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanCoolerLayout.setVerticalGroup(
            PanCoolerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCoolerLayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFCoolerPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        CBCooler.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBCooler.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBCooler.setPreferredSize(new java.awt.Dimension(265, 44));
        CBCooler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBCoolerActionPerformed(evt);
            }
        });

        CBCase.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBCase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBCase.setPreferredSize(new java.awt.Dimension(265, 44));
        CBCase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBCaseActionPerformed(evt);
            }
        });

        PanCase.setPreferredSize(new java.awt.Dimension(265, 276));

        TFCasePrice.setEditable(false);
        TFCasePrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFCasePrice.setAutoscrolls(false);
        TFCasePrice.setFocusable(false);

        javax.swing.GroupLayout PanCaseLayout = new javax.swing.GroupLayout(PanCase);
        PanCase.setLayout(PanCaseLayout);
        PanCaseLayout.setHorizontalGroup(
            PanCaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCaseLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFCasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanCaseLayout.setVerticalGroup(
            PanCaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCaseLayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFCasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        CBGPU.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBGPU.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBGPU.setPreferredSize(new java.awt.Dimension(265, 44));
        CBGPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBGPUActionPerformed(evt);
            }
        });

        PanGPU.setPreferredSize(new java.awt.Dimension(265, 276));

        TFGPUPrice.setEditable(false);
        TFGPUPrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFGPUPrice.setAutoscrolls(false);
        TFGPUPrice.setFocusable(false);

        javax.swing.GroupLayout PanGPULayout = new javax.swing.GroupLayout(PanGPU);
        PanGPU.setLayout(PanGPULayout);
        PanGPULayout.setHorizontalGroup(
            PanGPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanGPULayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFGPUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanGPULayout.setVerticalGroup(
            PanGPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanGPULayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFGPUPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        CBStorage.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        CBStorage.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "" }));
        CBStorage.setPreferredSize(new java.awt.Dimension(265, 44));
        CBStorage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBStorageActionPerformed(evt);
            }
        });

        PanStorage.setPreferredSize(new java.awt.Dimension(265, 276));

        TFStoragePrice.setEditable(false);
        TFStoragePrice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TFStoragePrice.setAutoscrolls(false);
        TFStoragePrice.setFocusable(false);

        javax.swing.GroupLayout PanStorageLayout = new javax.swing.GroupLayout(PanStorage);
        PanStorage.setLayout(PanStorageLayout);
        PanStorageLayout.setHorizontalGroup(
            PanStorageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanStorageLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(TFStoragePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanStorageLayout.setVerticalGroup(
            PanStorageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanStorageLayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(TFStoragePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBStorage, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanStorage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBCPU, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(CBMOBO, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PanMOBO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(CBGPU, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PanGPU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBPSU, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanPSU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBCase, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanCase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CBCooler, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanCooler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBRAM, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanRAM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CBCPU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBMOBO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBPSU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBRAM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanMOBO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanPSU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(CBCooler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(PanCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(CBCase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(PanCase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(CBGPU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(PanGPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(CBStorage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PanStorage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        TPOptions.addTab("Components", jPanel2);

        jPanel3.setBackground(new java.awt.Color(26, 43, 59));
        jPanel3.setPreferredSize(new java.awt.Dimension(1263, 860));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        TPOptions.addTab("Peripherals", jPanel3);

        TFFullName.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        TFFullName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFFullName.setText("Full Name");
        TFFullName.setToolTipText("");
        TFFullName.setPreferredSize(new java.awt.Dimension(230, 40));
        TFFullName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFFullNameActionPerformed(evt);
            }
        });

        TFPhoneNO.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        TFPhoneNO.setText("#");
        TFPhoneNO.setPreferredSize(new java.awt.Dimension(230, 40));

        TFEmail.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        TFEmail.setText("Email");
        TFEmail.setPreferredSize(new java.awt.Dimension(230, 40));

        TFAdress.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        TFAdress.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFAdress.setText("Addres");
        TFAdress.setToolTipText("");
        TFAdress.setPreferredSize(new java.awt.Dimension(230, 40));
        TFAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFAdressActionPerformed(evt);
            }
        });

        TFCity.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        TFCity.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFCity.setText("City");
        TFCity.setToolTipText("");
        TFCity.setPreferredSize(new java.awt.Dimension(230, 40));
        TFCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFCityActionPerformed(evt);
            }
        });

        TFProvince.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        TFProvince.setText("Province");
        TFProvince.setPreferredSize(new java.awt.Dimension(230, 40));
        TFProvince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFProvinceActionPerformed(evt);
            }
        });

        BTBuild.setBackground(new java.awt.Color(25, 36, 57));
        BTBuild.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        BTBuild.setText("Finalize Build");
        BTBuild.setToolTipText("Prepairs List of your build and sent to system");
        BTBuild.setPreferredSize(new java.awt.Dimension(220, 50));
        BTBuild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTBuildActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setToolTipText("Price");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(220, 50));

        TAPartsTotal.setEditable(false);
        TAPartsTotal.setColumns(20);
        TAPartsTotal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TAPartsTotal.setLineWrap(true);
        TAPartsTotal.setRows(1);
        TAPartsTotal.setTabSize(1);
        TAPartsTotal.setFocusable(false);
        TAPartsTotal.setPreferredSize(new java.awt.Dimension(220, 50));
        jScrollPane1.setViewportView(TAPartsTotal);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFPhoneNO, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFCity, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFProvince, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BTBuild, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(TPOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TFAdress, TFCity, TFEmail, TFFullName, TFPhoneNO, TFProvince});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFPhoneNO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFProvince, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTBuild, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(TPOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(180, 180, 180))
        );

        TPOptions.getAccessibleContext().setAccessibleName("Computer Parts");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TFFullNameActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFFullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFFullNameActionPerformed

    private void TFAdressActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFAdressActionPerformed

    private void TFCityActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFCityActionPerformed

    private void TFProvinceActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFProvinceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFProvinceActionPerformed

    private void BTBuildActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTBuildActionPerformed
        // Collect customer info
        String fullName = TFFullName.getText().trim();
        String phone = TFPhoneNO.getText().trim();
        String email = TFEmail.getText().trim();
        String address = TFAdress.getText().trim();
        String city = TFCity.getText().trim();
        String province = TFProvince.getText().trim();

        if (phone.length() > 20) { // Match your database column size
            JOptionPane.showMessageDialog(this,
                    "Phone number too long (max 20 characters)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cleanedPhone = phone.replaceAll("[^0-9]", "");
        if (cleanedPhone.length() < 7) { // Minimum reasonable phone number length
            JOptionPane.showMessageDialog(this,
                    "Invalid phone number",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate inputs
        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || city.isEmpty() || province.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all customer fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert customer
        int customerId = -1;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO customers (`Full Name`, `PhoneNo`, `Email`, `Address`, `City`, `Province`, `Note`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fullName);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.setString(5, city);
            stmt.setString(6, province);
            stmt.setString(7, ""); // Note
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customerId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No customer ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Collect selected parts
        List<Integer> partIds = new ArrayList<>();
        total = 0.0;  // Reset total

        // Check each ComboBox
        if (!addPart(CBCPU, 2, partIds)) {
            return;
        }
        if (!addPart(CBMOBO, 1, partIds)) {
            return;
        }
        if (!addPart(CBGPU, 3, partIds)) {
            return;
        }
        if (!addPart(CBRAM, 4, partIds)) {
            return;
        }
        if (!addPart(CBStorage, 5, partIds)) {
            return;
        }
        if (!addPart(CBPSU, 6, partIds)) {
            return;
        }
        if (!addPart(CBCooler, 7, partIds)) {
            return;
        }
        if (!addPart(CBCase, 8, partIds)) {
            return;
        }

        if (partIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No parts selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert order
        String itemPurchased = partIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO `orders` (`Customer ID`, `Date of Order`, `Revenue`, `Item Purchased`, `Queue Order`) "
                + "VALUES (?, CURRENT_DATE(), ?, ?, 0)")) {
            stmt.setInt(1, customerId);
            stmt.setDouble(2, total);
            stmt.setString(3, itemPurchased);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update stock
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "UPDATE partlist SET `Stock` = `Stock` - 1 WHERE `Part ID` = ? AND `Stock` > 0")) {
            for (int partId : partIds) {
                stmt.setInt(1, partId);
                stmt.addBatch();
            }
            int[] updateCounts = stmt.executeBatch();
            for (int count : updateCounts) {
                if (count == 0) {
                    JOptionPane.showMessageDialog(this, "A part is out of stock.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Order finalized successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean addPart(JComboBox<String> comboBox, int expectedType, List<Integer> partIds) {
        String selected = (String) comboBox.getSelectedItem();
        if ("none".equals(selected)) {
            return true;
        }

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT `Part ID`, `Price`, `Stock` FROM partlist WHERE `Part Name` = ? AND `Type` = ?")) {
            stmt.setString(1, selected);
            stmt.setInt(2, expectedType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int partId = rs.getInt("Part ID");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock");
                if (stock <= 0) {
                    JOptionPane.showMessageDialog(this, selected + " is out of stock.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                partIds.add(partId);
                total += price;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid part: " + selected, "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking part: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }//GEN-LAST:event_BTBuildActionPerformed

    private void CBCPUActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBCPUActionPerformed

    }//GEN-LAST:event_CBCPUActionPerformed

    private void CBMOBOActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBMOBOActionPerformed

    }//GEN-LAST:event_CBMOBOActionPerformed

    private void CBPSUActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBPSUActionPerformed

    }//GEN-LAST:event_CBPSUActionPerformed

    private void CBRAMActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBRAMActionPerformed

    }//GEN-LAST:event_CBRAMActionPerformed

    private void CBCoolerActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBCoolerActionPerformed
        
    }//GEN-LAST:event_CBCoolerActionPerformed

    private void CBCaseActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBCaseActionPerformed
        
    }//GEN-LAST:event_CBCaseActionPerformed

    private void CBGPUActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBGPUActionPerformed
        
    }//GEN-LAST:event_CBGPUActionPerformed

    private void CBStorageActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBStorageActionPerformed
        
    }//GEN-LAST:event_CBStorageActionPerformed

    @SuppressWarnings({"Convert2Lambda", "override"})
    public static void main(String args[]) {
        FlatLaf.registerCustomDefaultsSource("com.org.example.PCShopApp");
        FlatDarkLaf.setup();
        ConsumerInterface frame = new ConsumerInterface();
        frame.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTBuild;
    private javax.swing.JComboBox<String> CBCPU;
    private javax.swing.JComboBox<String> CBCase;
    private javax.swing.JComboBox<String> CBCooler;
    private javax.swing.JComboBox<String> CBGPU;
    private javax.swing.JComboBox<String> CBMOBO;
    private javax.swing.JComboBox<String> CBPSU;
    private javax.swing.JComboBox<String> CBRAM;
    private javax.swing.JComboBox<String> CBStorage;
    private javax.swing.JPanel PanCPU;
    private javax.swing.JPanel PanCase;
    private javax.swing.JPanel PanCooler;
    private javax.swing.JPanel PanGPU;
    private javax.swing.JPanel PanMOBO;
    private javax.swing.JPanel PanPSU;
    private javax.swing.JPanel PanRAM;
    private javax.swing.JPanel PanStorage;
    private javax.swing.JTextArea TAPartsTotal;
    private javax.swing.JTextField TFAdress;
    private javax.swing.JTextField TFCPUPrice;
    private javax.swing.JTextField TFCasePrice;
    private javax.swing.JTextField TFCity;
    private javax.swing.JTextField TFCoolerPrice;
    private javax.swing.JTextField TFEmail;
    private javax.swing.JTextField TFFullName;
    private javax.swing.JTextField TFGPUPrice;
    private javax.swing.JTextField TFMOBOPrice;
    private javax.swing.JTextField TFPSUPrice;
    private javax.swing.JTextField TFPhoneNO;
    private javax.swing.JTextField TFProvince;
    private javax.swing.JTextField TFRAMPrice;
    private javax.swing.JTextField TFStoragePrice;
    private javax.swing.JTabbedPane TPOptions;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
}
