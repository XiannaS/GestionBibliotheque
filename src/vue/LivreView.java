package vue;

import controllers.LivreController;
import model.Livre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class LivreView extends JFrame {
    private static final long serialVersionUID = 1L;
    private LivreController livreController;
    private JTextArea livresArea;
    private JButton addButton;

    // Chemin absolu du dossier ressources
    private static final String RESOURCE_PATH = "C:/Eclipse/GestionBibliothèque/src/ressources/";

    public LivreView() {
        livreController = new LivreController();
        setTitle("Gestion de Bibliothèque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // Bouton d'ajout de livre avec icône redimensionnée et couleur personnalisée
        addButton = new JButton("Ajouter Livre");
        addButton.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "add-icon.png"), 20, 20));
        addButton.setFocusPainted(false);
        addButton.setBackground(Color.decode("#004754"));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> openAddBookDialog());

        // Zone d'affichage des livres
        livresArea = new JTextArea();
        livresArea.setEditable(false);
        livresArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(livresArea), BorderLayout.CENTER);

        // Panel en bas pour les boutons et l'image de fond
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 245));

  
        // Ajouter le bouton en bas à gauche
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(addButton);
        bottomPanel.add(buttonPanel, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);

        chargerLivres();
    }

    private void openAddBookDialog() {
        JDialog addDialog = new JDialog(this, "Ajouter un Livre", true);
        addDialog.setSize(400, 500);
        addDialog.setLocationRelativeTo(this);
        addDialog.setLayout(new GridBagLayout());
        addDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addDialog.setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Champs du formulaire avec icônes redimensionnées
        JLabel titleLabel = new JLabel("Titre:");
        titleLabel.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "title-icon.png"), 20, 20));
        JTextField titreField = new JTextField();

        JLabel auteurLabel = new JLabel("Auteur:");
        auteurLabel.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "author-icon.png"), 20, 20));
        JTextField auteurField = new JTextField();

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "genre-icon.png"), 20, 20));
        JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Biographie", "Histoire"});

        JLabel anneeLabel = new JLabel("Année:");
        anneeLabel.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "year-icon.png"), 20, 20));
        JTextField anneeField = new JTextField();

        JLabel disponibleLabel = new JLabel("Disponible:");
        JCheckBox disponibleCheckBox = new JCheckBox();

        // Bouton Enregistrer avec couleur personnalisée
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "save-icon.png"), 20, 20));
        saveButton.setBackground(Color.decode("#004754"));
        saveButton.setForeground(Color.WHITE);

        saveButton.addActionListener(e -> {
            if (titreField.getText().trim().isEmpty() || auteurField.getText().trim().isEmpty() || anneeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                ajouterLivre(titreField.getText(), auteurField.getText(), (String) genreComboBox.getSelectedItem(), anneeField.getText(), disponibleCheckBox.isSelected());
                addDialog.dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        addDialog.add(titleLabel, gbc);
        gbc.gridx = 1;
        addDialog.add(titreField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addDialog.add(auteurLabel, gbc);
        gbc.gridx = 1;
        addDialog.add(auteurField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addDialog.add(genreLabel, gbc);
        gbc.gridx = 1;
        addDialog.add(genreComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addDialog.add(anneeLabel, gbc);
        gbc.gridx = 1;
        addDialog.add(anneeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addDialog.add(disponibleLabel, gbc);
        gbc.gridx = 1;
        addDialog.add(disponibleCheckBox, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        addDialog.add(saveButton, gbc);

        // Ajouter l'image en bas du formulaire
        JLabel imageLabel = new JLabel(resizeIcon(loadIcon(RESOURCE_PATH + "livreform.png"), 300, 100)); // Ajustez la taille selon vos préférences
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Pour que l'image prenne toute la largeur
        gbc.anchor = GridBagConstraints.PAGE_END;
        addDialog.add(imageLabel, gbc);

        addDialog.setVisible(true);
    }


    private void ajouterLivre(String titre, String auteur, String genre, String annee, boolean disponible) {
        int anneePublication;
        try {
            anneePublication = Integer.parseInt(annee);
            int currentYear = LocalDate.now().getYear();
            if (anneePublication < 1900 || anneePublication > currentYear) {
                JOptionPane.showMessageDialog(this, "L'année doit être entre 1900 et " + currentYear, "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Année invalide. Veuillez entrer un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Livre livre = new Livre(titre, auteur, genre, anneePublication, disponible);
        livreController.ajouterLivre(livre);
        chargerLivres();
    }

    private void chargerLivres() {
        List<Livre> livres = livreController.lireLivres();
        livresArea.setText("");
        for (Livre livre : livres) {
            livresArea.append(livre.toString() + "\n");
        }
    }

    private ImageIcon loadIcon(String path) {
        return new ImageIcon(path);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
