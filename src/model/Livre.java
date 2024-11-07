package model;

public class Livre {
    private String id;
    private String titre;
    private String auteur;
    private String genre;
    private int anneePublication;
    private boolean disponible;

    // Constructeur
    public Livre(String titre, String auteur, String genre, int anneePublication, boolean disponible) {
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    // Autres méthodes...


    // Constructeur avec ID
    public Livre(String id, String titre, String auteur, String genre, int anneePublication, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getGenre() {
        return genre;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public String toString() {
        return id + "," + titre + "," + auteur + "," + genre + "," + anneePublication + "," + disponible;
    }
}