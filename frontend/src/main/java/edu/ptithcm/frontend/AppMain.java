package edu.ptithcm.frontend;

import javax.swing.SwingUtilities;
import edu.ptithcm.frontend.view.LoginForm;

public class AppMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
