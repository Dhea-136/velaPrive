package com.praktikum;

import com.praktikum.GUI.LoginFrame;
import javax.swing.*;

public class VelaPriveApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}


