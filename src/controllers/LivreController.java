package controllers;

import model.Livre;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LivreController {
    private static final String CSV_FILE = "src/database/books.csv";

    // Méthode pour lire les livres depuis le fichier CSV
    public List<Livre> lireLivres() {
        try (Stream<String> lines = Files.lines(Paths.get(CSV_FILE))) {
            return lines
                .map(line -> line.split(","))
                .filter(attributes -> attributes.length >= 7) // Vérifie que la ligne a au moins 7 éléments
                .map(attributes -> new Livre(
                    attributes[0],
                    attributes[1],
                    attributes[2],
                    attributes[3],
                    Integer.parseInt(attributes[4]),
                    Boolean.parseBoolean(attributes[5]),
                    attributes[6]
                ))
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Erreur de format dans le fichier CSV. Veuillez vérifier les données.");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

 // Méthode pour générer un ID unique
    private String genererId() {
        List<Livre> livres = lireLivres();
        // Générer un ID en fonction du nombre de livres, formaté à 4 chiffres
        return String.format("%04d", livres.size() + 1);
    }

    // Méthode pour ajouter un livre dans le fichier CSV
    public void ajouterLivre(Livre livre) {
        // Attribuer un ID unique au livre avant de l'ajouter
        livre.setId(genererId());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            bw.write(livre.toCsvFormat());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour modifier un livre dans le fichier CSV
    public void modifierLivre(Livre livreModifie) {
        List<Livre> livres = lireLivres();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            livres.stream()
                .map(livre -> livre.getId().equals(livreModifie.getId()) ? livreModifie : livre)
                .forEach(livre -> {
                    try {
                        bw.write(livre.toCsvFormat());
                        bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un livre du fichier CSV
    public void supprimerLivre(String id) {
        List<Livre> livres = lireLivres();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            livres.stream()
                .filter(livre -> !livre.getId().equals(id))
                .forEach(livre -> {
                    try {
                        bw.write(livre.toCsvFormat());
                        bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}