package vue;

import controllers.LivreController;
import model.Livre;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.File;

public class LivreView extends JFrame {
    private static final long serialVersionUID = 1L;
    private LivreController livreController;
    private JButton addButton;
    private String imageUrl; // Variable pour stocker le chemin de l'image
    // Chemin absolu du dossier ressources
    private static final String RESOURCE_PATH = "C:/Eclipse/GestionBibliothèque/src/ressources/";
    
    // Déclaration des variables pour la recherche et le filtrage
    private JTextField searchField;
    private JCheckBox availableCheckBox;
    private JCheckBox borrowedCheckBox;
    
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

        // Panel pour la recherche et le filtrage
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 245, 245));

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher");
        availableCheckBox = new JCheckBox("Disponibles");
        borrowedCheckBox = new JCheckBox("Empruntés");

        searchButton.addActionListener(e -> filterBooks());

        searchPanel.add(new JLabel("Recherche:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(availableCheckBox);
        searchPanel.add(borrowedCheckBox);

        add(searchPanel, BorderLayout.NORTH); // Ajoutez le panel de recherche en haut

        
        add(bottomPanel, BorderLayout.SOUTH);

        chargerLivres(livreController.lireLivres(), booksPanel); // Charger les livres après l'initialisation
    }

	 


private void openAddBookDialog() {
    JDialog addDialog = new JDialog(this, "Ajouter un Livre", true);
    addDialog.setSize(400, 600);
    addDialog.setLocationRelativeTo(this);
    addDialog.setLayout(new GridBagLayout());
    addDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    addDialog.setResizable(false);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Champs du formulaire
    JLabel titleLabel = new JLabel("Titre:");
    JTextField titreField = new JTextField();

    JLabel auteurLabel = new JLabel("Auteur:");
    JTextField auteurField = new JTextField();

    JLabel genreLabel = new JLabel("Genre:");
    JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Biographie", "Histoire"});

    JLabel anneeLabel = new JLabel("Année:");
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
            imageUrl = selectedFile.getPath(); // Stocker le chemin de l'image
            ImageIcon couvertureIcon = new ImageIcon(imageUrl);
            couverturePreview.setIcon(resizeIcon(couvertureIcon, 100, 150)); // Redimensionner l'image pour l'aperçu
        }
    });

    // Bouton Enregistrer
    JButton saveButton = new JButton("Enregistrer");
    saveButton.addActionListener(e -> {
        if (titreField.getText().trim().isEmpty() || auteurField.getText().trim().isEmpty() || anneeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else {
            // Validation de l'année
            try {
                int anneePublication = Integer.parseInt(anneeField.getText());
                int currentYear = LocalDate.now().getYear();
                if (anneePublication < 1600 || anneePublication > currentYear) {
                    JOptionPane.showMessageDialog(this, "L'année doit être entre 1600 et " + currentYear, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer une année valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Créer un objet Livre
            String titre = titreField.getText();
            String auteur = auteurField.getText ();
            String genre = (String) genreComboBox.getSelectedItem();
            String annee = anneeField.getText();
            boolean disponible = disponibleCheckBox.isSelected();

            // Appeler la méthode ajouterLivre avec tous les paramètres requis
            ajouterLivre(titre, auteur, genre, annee, disponible, imageUrl);
            // Effacer les champs après l'ajout
            titreField.setText("");
            auteurField.setText("");
            anneeField.setText("");
            disponibleCheckBox.setSelected(false);
            couverturePreview.setIcon(null); // Réinitialiser l'aperçu de la couverture
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
private void ajouterLivre(String titre, String auteur, String genre, String annee, boolean disponible, String imageUrl) {
    int anneePublication;
    try {
        anneePublication = Integer.parseInt(annee);
        int currentYear = LocalDate.now().getYear();
        if (anneePublication < 1600 || anneePublication > currentYear) {
            JOptionPane.showMessageDialog(this, "L'année doit être entre 1600 et " + currentYear, "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Année invalide. Veuillez entrer un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Livre livre = new Livre(UUID.randomUUID().toString(), titre, auteur, genre, anneePublication, disponible, imageUrl);
    livreController.ajouterLivre(livre);

    // Récupérer le JScrollPane et son JPanel
    JScrollPane scrollPane = (JScrollPane) getContentPane().getComponent(0);
    JPanel booksPanel = (JPanel) scrollPane.getViewport().getView(); // Obtenir le JPanel à l'intérieur du JScrollPane
    chargerLivres(livreController.lireLivres(), booksPanel); // Passer la liste des livres
}


	private void chargerLivres(List<Livre> livres, JPanel booksPanel) {
	    booksPanel.removeAll();
	
	    livres.stream().forEach(livre -> {
	        JPanel livrePanel = new JPanel();
	        livrePanel.setLayout(new BorderLayout());
	        livrePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	        livrePanel.setBackground(new Color(255, 255, 255));
	        livrePanel.setPreferredSize(new Dimension(200, 300));
	
	        String imagePath = livre.getImageUrl() != null && !livre.getImageUrl().isEmpty()
	            ? livre.getImageUrl()
	            : RESOURCE_PATH + "default-book.jpeg";
	        JLabel imageLabel = new JLabel(resizeIcon(loadIcon(imagePath), 120, 180));
	        livrePanel.add(imageLabel, BorderLayout.CENTER);
	
	        JPanel textPanel = new JPanel(new GridLayout(2, 1));
	        textPanel.setBackground(new Color(255, 255, 255));
	        JLabel titleLabel = new JLabel(livre.getTitre(), JLabel.CENTER);
	        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
	        JLabel authorLabel = new JLabel(livre.getAuteur(), JLabel.CENTER);
	        textPanel.add(titleLabel);
	        textPanel.add(authorLabel);
	
	        livrePanel.add(textPanel, BorderLayout.SOUTH);
	        booksPanel.add(livrePanel);
	    });
	
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
    
    private void filterBooks() {
        String searchText = searchField.getText().toLowerCase();
        boolean showAvailable = availableCheckBox.isSelected();
        boolean showBorrowed = borrowedCheckBox.isSelected();

        List<Livre> livres = livreController.lireLivres();

        List<Livre> filteredLivres = livres.stream()
            .filter(livre -> {
                boolean matchesSearch = livre.getTitre().toLowerCase().contains(searchText) ||
                                        livre.getAuteur().toLowerCase().contains(searchText) ||
                                        livre.getGenre().toLowerCase().contains(searchText);

                boolean matchesAvailability = (showAvailable && livre.isDisponible()) ||
                                              (showBorrowed && !livre.isDisponible());

                // Utilisez matchesAvailability dans la condition de filtrage
                return matchesSearch && (matchesAvailability || (!showAvailable && !showBorrowed));
            })
            .collect(Collectors.toList());

        // Mettre à jour l'affichage avec les livres filtrés
        JPanel booksPanel = (JPanel) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView();
        chargerLivres(filteredLivres, booksPanel); // Passer la liste filtrée
    }
}