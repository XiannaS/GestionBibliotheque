package controllers;

import model.Livre;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LivreController {
    private static final String CSV_FILE = "src/database/books.csv";

    public List<Livre> lireLivres() {
        List<Livre> livres = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 7) { // 7 champs avec imageUrl
                    String id = fields[0];
                    String titre = fields[1];
                    String auteur = fields[2];
                    String genre = fields[3];
                    int anneePublication = Integer.parseInt(fields[4]);
                    boolean disponible = Boolean.parseBoolean(fields[5]);
                    String imageUrl = fields[6];
                    livres.add(new Livre(id, titre, auteur, genre, anneePublication, disponible, imageUrl));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return livres;
    }


    public void ajouterLivre(Livre livre) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            bw.write(livre.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void modifierLivre(Livre livreModifie) {
        List<Livre> livres = lireLivres();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (Livre livre : livres) {
                if (livre.getId().equals(livreModifie.getId())) {
                    bw.write(livreModifie.toString());
                } else {
                    bw.write(livre.toString());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void supprimerLivre(String id) {
        List<Livre> livres = lireLivres();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (Livre livre : livres) {
                if (!livre.getId().equals(id)) {
                    bw.write(livre.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}