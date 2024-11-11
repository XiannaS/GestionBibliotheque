package controllers;
import javax.swing.SwingUtilities;
import vue.LivreView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LivreView livreView = new LivreView();
            livreView.setVisible(true);
        });
    }
}