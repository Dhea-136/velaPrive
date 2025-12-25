package com.praktikum.GUI;

import com.praktikum.Model.Yacht;
import com.praktikum.utils.DataManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class YachtFrame extends JFrame {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    public YachtFrame(DataManager dataManager) {
        this.dataManager = dataManager;
        initComponents();
        loadYachts();
    }

    private void initComponents() {
        setTitle("Vela Privé - Yacht Fleet");
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(AppColors.CREAM);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("YACHT FLEET");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(AppColors.GOLD);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(AppColors.GOLD);
        backBtn.setForeground(AppColors.NAVY);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> dispose());
        headerPanel.add(backBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Yacht Name", "Type", "Capacity", "Price/Day ($)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(AppColors.GOLD);
        table.setSelectionForeground(AppColors.NAVY);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadYachts() {
        tableModel.setRowCount(0);
        List<Yacht> yachts = dataManager.getYachts();
        yachts.sort(Comparator.comparing(Yacht::getId));
        for (Yacht yacht : yachts) {
            tableModel.addRow(new Object[]{
                    yacht.getId(),
                    yacht.getName(),
                    yacht.getType(),
                    yacht.getCapacity(),
                    String.format("%.2f", yacht.getPricePerDay()),
                    yacht.getStatus()
            });
        }
    }
}
