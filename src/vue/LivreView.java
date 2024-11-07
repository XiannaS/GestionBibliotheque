package vue;

import controllers.LivreController;
import model.Livre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;

public class LivreView extends JFrame {
    private static final long serialVersionUID = 1L;
    private LivreController livreController;
    private JTextField titreField, auteurField, anneeField, searchField;
    private JTextArea livresArea;
    private JComboBox<String> genreComboBox; // Pour le genre
    private JCheckBox disponibleCheckBox; // Pour la disponibilité
    private JComboBox<Integer> yearComboBox; // Pour filtrer par année
    private JList<String> resultsList; // Pour afficher les résultats de recherche
    private DefaultListModel<String> listModel; // Modèle pour la JList

    public LivreView() {
        livreController = new LivreController();
        setTitle("Gestion de Bibliothèque");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panneau pour les champs de saisie
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2)); // Augmenter le nombre de lignes pour inclure la case à cocher

        inputPanel.add(new JLabel("Titre:"));
        titreField = new JTextField();
        inputPanel.add(titreField);

        inputPanel.add(new JLabel("Auteur:"));
        auteurField = new JTextField();
        inputPanel.add(auteurField);

        inputPanel.add(new JLabel("Genre:"));
        String[] genres = {"Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Biographie", "Histoire"};
        genreComboBox = new JComboBox<>(genres); // Initialisation de JComboBox
        inputPanel.add(genreComboBox);

        inputPanel.add(new JLabel("Année de Publication:"));
        anneeField = new JTextField();
        inputPanel.add(anneeField);

        inputPanel.add(new JLabel("Disponible:"));
        disponibleCheckBox = new JCheckBox(); // Initialisation de JCheckBox
        inputPanel.add(disponibleCheckBox);

        JButton ajouterButton = new JButton("Ajouter Livre");
        ajouterButton.addActionListener(e -> ajouterLivre());
        inputPanel.add(ajouterButton);

        // Champ de recherche
        inputPanel.add(new JLabel("Recherche:"));
        searchField = new JTextField();
        inputPanel.add(searchField);

        // ComboBox pour filtrer par année
        yearComboBox = new JComboBox<>(getAvailableYears());
        inputPanel.add(new JLabel("Filtrer par année:"));
        inputPanel.add(yearComboBox);

        add(inputPanel, BorderLayout.NORTH);

        // Initialisation et ajout de la zone de texte pour afficher tous les livres
        livresArea = new JTextArea();
        livresArea.setEditable(false); // Rendre non-éditable
        JScrollPane livresScrollPane = new JScrollPane(livresArea);
        add(livresScrollPane, BorderLayout.CENTER);

        // Liste pour afficher les résultats de recherche
        listModel = new DefaultListModel<>();
        resultsList = new JList<>(listModel);
        add(new JScrollPane(resultsList), BorderLayout.SOUTH); // Utiliser SOUTH pour séparer la liste des résultats et l'affichage complet

        // Écouteur pour le champ de recherche
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateSearchResults();
            }
        });

        // Charger les livres au démarrage
        chargerLivres();
    }

    private Integer[] getAvailableYears() {
        // Remplir avec les années disponibles (exemple)
        return new Integer[]{2023, 2022, 2021, 2020, 2019}; // Remplacez par vos années réelles
    }

    private void ajouterLivre() {
        String titre = titreField.getText();
        String auteur = auteurField.getText();
        String genre = (String) genreComboBox.getSelectedItem();
        int anneePublication;

        try {
            anneePublication = Integer.parseInt(anneeField.getText());
            if (anneePublication < 1900 || anneePublication > LocalDate.now().getYear()) {
                throw new IllegalArgumentException("L'année doit être entre 1900 et " + LocalDate.now().getYear());
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean disponible = disponibleCheckBox.isSelected();
        Livre livre = new Livre(titre, auteur, genre, anneePublication, disponible);
        livreController.ajouterLivre(livre);
        chargerLivres(); // Recharger la liste des livres
    }

    private void updateSearchResults() {
        String searchTerm = searchField.getText().toLowerCase();
        Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
        listModel.clear(); // Réinitialiser la liste des résultats

        List<Livre> livres = livreController.lireLivres();
        for (Livre livre : livres) {
            boolean matches = livre.getTitre().toLowerCase().contains(searchTerm) ||
                    livre.getAuteur().toLowerCase().contains(searchTerm) ||
                    livre.getGenre().toLowerCase().contains(searchTerm);

            if (selectedYear != null) {
                matches = matches && (livre.getAnneePublication() == selectedYear);
            }

            if (matches) {
                listModel.addElement(livre.toString()); // Ajouter le livre à la liste des résultats
            }
        }
    }

    private void chargerLivres() {
        List<Livre> livres = livreController.lireLivres();
        livresArea.setText(""); // Réinitialiser la zone de texte
        for (Livre livre : livres) {
            livresArea.append(livre.toString() + "\n");
        }
    }
}
