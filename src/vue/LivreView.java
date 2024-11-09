package vue;

import controllers.LivreController;
import model.Livre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.io.File;

public class LivreView extends JFrame {
    private static final long serialVersionUID = 1L;
    private LivreController livreController;
    private JButton addButton;

    // Chemin absolu du dossier ressources
    private static final String RESOURCE_PATH = "C:/Eclipse/GestionBibliothèque/src/ressources/";

    public LivreView() {
        livreController = new LivreController();
        setTitle("Gestion de Bibliothèque");
        setSize(1000, 600); // Taille modifiée pour plus d'espace
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // Bouton d'ajout de livre
        addButton = new JButton("Ajouter Livre");
        addButton.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "add-icon.png"), 20, 20));
        addButton.setFocusPainted(false);
        addButton.setBackground(Color.decode("#004754"));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> openAddBookDialog());

        // Panel pour afficher les livres
        JPanel booksPanel = new JPanel();
        booksPanel.setLayout(new GridLayout(0, 3, 10, 10)); // Trois livres par ligne, avec un espacement de 10px
        booksPanel.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(booksPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel en bas pour le bouton
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 245));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(addButton);
        bottomPanel.add(buttonPanel, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);

        chargerLivres(booksPanel); // Charger les livres après l'initialisation
    }

	 
	private void openAddBookDialog() {
	    JDialog addDialog = new JDialog(this, "Ajouter un Livre", true);
	    addDialog.setSize(400, 600); // Augmentation de la taille pour inclure la couverture
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
	
	    // Champ de sélection et aperçu de la couverture
	    JLabel couvertureLabel = new JLabel("Couverture:");
	    JButton couvertureButton = new JButton("Choisir une image");
	    JLabel couverturePreview = new JLabel();
	    couverturePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	    couverturePreview.setPreferredSize(new Dimension(100, 150)); // Taille de prévisualisation de la couverture
	
	    couvertureButton.addActionListener(e -> {
	        JFileChooser fileChooser = new JFileChooser();
	        int result = fileChooser.showOpenDialog(this);
	        if (result == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = fileChooser.getSelectedFile();
	            ImageIcon couvertureIcon = new ImageIcon(selectedFile.getPath());
	            couverturePreview.setIcon(resizeIcon(couvertureIcon, 100, 150)); // Redimensionner l'image pour l'aperçu
	        }
	    });
	
        // Bouton Enregistrer avec couleur personnalisée
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setIcon(resizeIcon(loadIcon(RESOURCE_PATH + "save-icon.png"), 20, 20));
        saveButton.setBackground(Color.decode("#004754"));
        saveButton.setForeground(Color.WHITE);

        saveButton.addActionListener(e -> {
            if (titreField.getText().trim().isEmpty() || auteurField.getText().trim().isEmpty() || anneeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                // Créer un objet Livre
                String titre = titreField.getText();
                String auteur = auteurField.getText();
                String genre = (String) genreComboBox.getSelectedItem();
                String annee = anneeField.getText();
                boolean disponible = disponibleCheckBox.isSelected();
                String imageUrl = couverturePreview.getIcon() != null ? couverturePreview.getIcon().toString() : null;

                // Récupérer le JScrollPane et le JPanel
                JScrollPane booksScrollPane = (JScrollPane) getContentPane().getComponent(0);
                JPanel booksPanel = (JPanel) booksScrollPane.getViewport().getView();

                // Appeler la méthode ajouterLivre avec tous les paramètres requis
                ajouterLivre(titre, auteur, genre, annee, disponible, imageUrl, booksPanel);
                addDialog.dispose();
            }
        });
	
	    // Disposition des composants
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
	
	    // Disposition des composants pour la couverture
	    gbc.gridx = 0;
	    gbc.gridy++;
	    addDialog.add(couvertureLabel, gbc);
	    gbc.gridx = 1;
	    addDialog.add(couvertureButton, gbc);
	
	    gbc.gridx = 1;
	    gbc.gridy++;
	    addDialog.add(couverturePreview, gbc);
	
	    gbc.gridx = 1;
	    gbc.gridy++;
	    addDialog.add(saveButton, gbc);
	
	    addDialog.setVisible(true);
	}

	private void ajouterLivre(String titre, String auteur, String genre, String annee, boolean disponible, String imageUrl, JPanel booksPanel) {
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

	    Livre livre = new Livre(UUID.randomUUID().toString(), titre, auteur, genre, anneePublication, disponible, imageUrl);
	    livreController.ajouterLivre(livre);

	    // Récupérer le JScrollPane et le JPanel
	    JScrollPane booksScrollPane = (JScrollPane) getContentPane().getComponent(0);
	    JPanel panelFromScrollPane = (JPanel) booksScrollPane.getViewport().getView(); // Renommé ici

	    // Mise à jour de l'affichage
	    chargerLivres(panelFromScrollPane); // Utilisez le nouveau nom ici
	}
	
	
    private void chargerLivres(JPanel booksPanel) {
        List<Livre> livres = livreController.lireLivres();
        booksPanel.removeAll(); // Vider le panneau avant de le remplir

        for (Livre livre : livres) {
            JPanel livrePanel = new JPanel();
            livrePanel.setLayout(new BorderLayout());
            livrePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            livrePanel.setBackground(new Color(255, 255, 255));
            livrePanel.setPreferredSize(new Dimension(200, 300)); // Taille des cartes

            // Image du livre
            JLabel imageLabel = new JLabel(resizeIcon(loadIcon(RESOURCE_PATH + "default-book.jpg"), 120, 180));
            livrePanel.add(imageLabel, BorderLayout.CENTER);

            // Titre et auteur
            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setBackground(new Color(255, 255, 255));
            JLabel titleLabel = new JLabel(livre.getTitre(), JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            JLabel authorLabel = new JLabel(livre.getAuteur(), JLabel.CENTER);
            textPanel.add(titleLabel);
            textPanel.add(authorLabel);

            livrePanel.add(textPanel, BorderLayout.SOUTH);

            booksPanel.add(livrePanel);
        }

        booksPanel.revalidate();
        booksPanel.repaint();
    }

    private ImageIcon loadIcon(String path) {
        return new ImageIcon(path);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
