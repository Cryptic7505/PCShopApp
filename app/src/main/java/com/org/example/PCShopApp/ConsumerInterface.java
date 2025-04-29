package com.org.example.PCShopApp;

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
import javax.swing.JTextPane;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;


public class ConsumerInterface extends javax.swing.JFrame {
    private double total;
    private Map<JComboBox<String>, Double> componentPrices = new HashMap<>();
    private List<ComponentConfig> componentConfigs = new ArrayList<>();
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ConsumerInterface() {
        initComponents();
        initializePriceTracking();
        populateComboBoxes();
    }
    private class ComponentConfig {
        JComboBox<String> comboBox;
        JTextPane pricePane;
        int typeId;

        public ComponentConfig(JComboBox<String> comboBox, JTextPane pricePane, int typeId) {
            this.comboBox = comboBox;
            this.pricePane = pricePane;
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
    private void initializePriceTracking() {
        // Initialize all combo boxes with 0 price
        componentPrices.put(CBCPU, 0.0);
        componentPrices.put(CBMOBO, 0.0);
        componentPrices.put(CBGPU, 0.0);
        componentPrices.put(CBRAM, 0.0);
        componentPrices.put(CBStorage, 0.0);
        componentPrices.put(CBPSU, 0.0);
        componentPrices.put(CBCooler, 0.0);
        componentPrices.put(CBCase, 0.0);

        // Add listeners to all combo boxes
        addPriceListener(CBCPU, 2);  // CPU type ID
        addPriceListener(CBMOBO, 1); // Motherboard type ID
        addPriceListener(CBGPU, 3);  // GPU type ID
        addPriceListener(CBRAM, 4);  // RAM type ID
        addPriceListener(CBStorage, 5); // Storage type ID
        addPriceListener(CBPSU, 6);  // PSU type ID
        addPriceListener(CBCooler, 7); // Cooler type ID
        addPriceListener(CBCase, 8); // Case type ID
    }
    private void initializeComponentConfigs() {
        // Add all component configurations
        componentConfigs.add(new ComponentConfig(CBCPU, TPCPUPrice, 2));       // CPU
        componentConfigs.add(new ComponentConfig(CBMOBO, TPMOBOPrice, 1));     // Motherboard
        componentConfigs.add(new ComponentConfig(CBGPU, TPGPUPrice, 3));       // GPU
        componentConfigs.add(new ComponentConfig(CBRAM, TPRAMPrice, 4));       // RAM
        componentConfigs.add(new ComponentConfig(CBStorage, TPStoragePrice, 5)); // Storage
        componentConfigs.add(new ComponentConfig(CBPSU, TPPSUPrice, 6));       // PSU
        componentConfigs.add(new ComponentConfig(CBCooler, TPCoolerPrice, 7)); // Cooler
        componentConfigs.add(new ComponentConfig(CBCase, TPCasePrice, 8));     // Case

        // Add listeners to all components
        componentConfigs.forEach(config -> {
            config.comboBox.addActionListener(e -> {
                updateComponentPriceDisplay(config);
                updateTotalDisplay();
            });
            config.pricePane.setContentType("text/html");
        });
    }
    private void updateComponentPriceDisplay(ComponentConfig config) {
        String selected = (String) config.comboBox.getSelectedItem();
        double price = 0.0;
        
        if (selected != null && !"none".equals(selected)) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "SELECT Price FROM partlist WHERE `Part Name` = ? AND Type = ?")) {
                
                stmt.setString(1, selected);
                stmt.setInt(2, config.typeId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    price = rs.getDouble("Price");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error loading price for " + selected,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Format price with currency and update display
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        String formattedPrice = "<html><div style='text-align: center; font-size: 16pt;'>" +
                               currencyFormat.format(price) + "</div></html>";
        
        config.pricePane.setText(formattedPrice);
        config.pricePane.setBackground(new Color(240, 240, 240)); // Set background color
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
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT `Part Name` FROM partlist WHERE `Type` = ?")) {
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();
            comboBox.removeAllItems();
            comboBox.addItem("none");
            while (rs.next()) {
                comboBox.addItem(rs.getString("Part Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading parts", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addPriceListener(JComboBox<String> comboBox, int componentType) {
        comboBox.addActionListener(e -> {
            updatePriceForComponent(comboBox, componentType);
            updateTotalDisplay();
        });
    }
    private void updatePriceForComponent(JComboBox<String> comboBox, int componentType) {
        String selected = (String) comboBox.getSelectedItem();
        double price = 0.0;

        if (selected != null && !"none".equals(selected)) {
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                    "SELECT Price FROM partlist WHERE `Part Name` = ? AND Type = ?")) {

                stmt.setString(1, selected);
                stmt.setInt(2, componentType);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    price = rs.getDouble("Price");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading price", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Update the price map
        componentPrices.put(comboBox, price);
    }
    private void updateTotalDisplay() {
        double total = componentPrices.values().stream()
                .mapToDouble(config -> {
                    try {
                        String text = config.pricePane.getText()
                                .replaceAll("[^\\d.]", "");
                        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
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
        jScrollPane2 = new javax.swing.JScrollPane();
        TPCPUPrice = new javax.swing.JTextPane();
        PanMOBO = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TPMOBOPrice = new javax.swing.JTextPane();
        PanPSU = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TPPSUPrice = new javax.swing.JTextPane();
        PanRAM = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TPRAMPrice = new javax.swing.JTextPane();
        PanCooler = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TPCoolerPrice = new javax.swing.JTextPane();
        CBCooler = new javax.swing.JComboBox<>();
        CBCase = new javax.swing.JComboBox<>();
        PanCase = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TPCasePrice = new javax.swing.JTextPane();
        CBGPU = new javax.swing.JComboBox<>();
        PanGPU = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TPGPUPrice = new javax.swing.JTextPane();
        CBStorage = new javax.swing.JComboBox<>();
        PanStorage = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        TPStoragePrice = new javax.swing.JTextPane();
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

        jScrollPane2.setViewportView(TPCPUPrice);

        javax.swing.GroupLayout PanCPULayout = new javax.swing.GroupLayout(PanCPU);
        PanCPU.setLayout(PanCPULayout);
        PanCPULayout.setHorizontalGroup(
            PanCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCPULayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanCPULayout.setVerticalGroup(
            PanCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCPULayout.createSequentialGroup()
                .addContainerGap(213, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        PanMOBO.setPreferredSize(new java.awt.Dimension(265, 276));

        jScrollPane3.setViewportView(TPMOBOPrice);

        javax.swing.GroupLayout PanMOBOLayout = new javax.swing.GroupLayout(PanMOBO);
        PanMOBO.setLayout(PanMOBOLayout);
        PanMOBOLayout.setHorizontalGroup(
            PanMOBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanMOBOLayout.createSequentialGroup()
                .addContainerGap(123, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanMOBOLayout.setVerticalGroup(
            PanMOBOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanMOBOLayout.createSequentialGroup()
                .addContainerGap(213, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        PanPSU.setPreferredSize(new java.awt.Dimension(265, 276));

        jScrollPane4.setViewportView(TPPSUPrice);

        javax.swing.GroupLayout PanPSULayout = new javax.swing.GroupLayout(PanPSU);
        PanPSU.setLayout(PanPSULayout);
        PanPSULayout.setHorizontalGroup(
            PanPSULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanPSULayout.createSequentialGroup()
                .addContainerGap(122, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanPSULayout.setVerticalGroup(
            PanPSULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanPSULayout.createSequentialGroup()
                .addContainerGap(213, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        PanRAM.setPreferredSize(new java.awt.Dimension(265, 276));

        TPRAMPrice.setAutoscrolls(false);
        jScrollPane5.setViewportView(TPRAMPrice);

        javax.swing.GroupLayout PanRAMLayout = new javax.swing.GroupLayout(PanRAM);
        PanRAM.setLayout(PanRAMLayout);
        PanRAMLayout.setHorizontalGroup(
            PanRAMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanRAMLayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanRAMLayout.setVerticalGroup(
            PanRAMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanRAMLayout.createSequentialGroup()
                .addGap(216, 216, 216)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        PanCooler.setPreferredSize(new java.awt.Dimension(265, 276));

        TPCoolerPrice.setAutoscrolls(false);
        jScrollPane6.setViewportView(TPCoolerPrice);

        javax.swing.GroupLayout PanCoolerLayout = new javax.swing.GroupLayout(PanCooler);
        PanCooler.setLayout(PanCoolerLayout);
        PanCoolerLayout.setHorizontalGroup(
            PanCoolerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCoolerLayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanCoolerLayout.setVerticalGroup(
            PanCoolerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanCoolerLayout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        TPCasePrice.setAutoscrolls(false);
        jScrollPane7.setViewportView(TPCasePrice);

        javax.swing.GroupLayout PanCaseLayout = new javax.swing.GroupLayout(PanCase);
        PanCase.setLayout(PanCaseLayout);
        PanCaseLayout.setHorizontalGroup(
            PanCaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanCaseLayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanCaseLayout.setVerticalGroup(
            PanCaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanCaseLayout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        TPGPUPrice.setAutoscrolls(false);
        jScrollPane8.setViewportView(TPGPUPrice);

        javax.swing.GroupLayout PanGPULayout = new javax.swing.GroupLayout(PanGPU);
        PanGPU.setLayout(PanGPULayout);
        PanGPULayout.setHorizontalGroup(
            PanGPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanGPULayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        PanGPULayout.setVerticalGroup(
            PanGPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanGPULayout.createSequentialGroup()
                .addGap(213, 213, 213)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        TPStoragePrice.setAutoscrolls(false);
        TPStoragePrice.setMinimumSize(new java.awt.Dimension(62, 4));
        jScrollPane9.setViewportView(TPStoragePrice);

        javax.swing.GroupLayout PanStorageLayout = new javax.swing.GroupLayout(PanStorage);
        PanStorage.setLayout(PanStorageLayout);
        PanStorageLayout.setHorizontalGroup(
            PanStorageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanStorageLayout.createSequentialGroup()
                .addContainerGap(119, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        PanStorageLayout.setVerticalGroup(
            PanStorageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanStorageLayout.createSequentialGroup()
                .addContainerGap(213, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        initializeComponentConfigs();
    }//GEN-LAST:event_CBCPUActionPerformed

    private void CBMOBOActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBMOBOActionPerformed
        initializeComponentConfigs();
    }//GEN-LAST:event_CBMOBOActionPerformed

    private void CBPSUActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBPSUActionPerformed
        initializeComponentConfigs();
    }//GEN-LAST:event_CBPSUActionPerformed

    private void CBRAMActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBRAMActionPerformed
        initializeComponentConfigs();
    }//GEN-LAST:event_CBRAMActionPerformed

    private void CBCoolerActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBCoolerActionPerformed
        initializeComponentConfigs();
    }//GEN-LAST:event_CBCoolerActionPerformed

    private void CBCaseActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBCaseActionPerformed
        initializeComponentConfigs();
    }//GEN-LAST:event_CBCaseActionPerformed

    private void CBGPUActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBGPUActionPerformed
        initializeComponentConfigs();
    }//GEN-LAST:event_CBGPUActionPerformed

    private void CBStorageActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBStorageActionPerformed
        initializeComponentConfigs();
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
    private javax.swing.JTextField TFCity;
    private javax.swing.JTextField TFEmail;
    private javax.swing.JTextField TFFullName;
    private javax.swing.JTextField TFPhoneNO;
    private javax.swing.JTextField TFProvince;
    private javax.swing.JTextPane TPCPUPrice;
    private javax.swing.JTextPane TPCasePrice;
    private javax.swing.JTextPane TPCoolerPrice;
    private javax.swing.JTextPane TPGPUPrice;
    private javax.swing.JTextPane TPMOBOPrice;
    private javax.swing.JTabbedPane TPOptions;
    private javax.swing.JTextPane TPPSUPrice;
    private javax.swing.JTextPane TPRAMPrice;
    private javax.swing.JTextPane TPStoragePrice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    // End of variables declaration//GEN-END:variables
    
}
