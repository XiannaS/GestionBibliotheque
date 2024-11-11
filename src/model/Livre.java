package model;

public class Livre {
    private String id;
    private String titre;
    private String auteur;
    private String genre;
    private int anneePublication;
    private boolean disponible;
    private String imageUrl; 
    
    // Constructeur avec ID
    public Livre(String id, String titre, String auteur, String genre, int anneePublication, boolean disponible, String imageUrl) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
        this.imageUrl = imageUrl;
    }

    // Constructeur sans ID pour création
    public Livre(String titre, String auteur, String genre, int anneePublication, boolean disponible, String imageUrl) {
        this(null, titre, auteur, genre, anneePublication, disponible, imageUrl);
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

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Méthode pour formater en CSV
    public String toCsvFormat() {
        return String.join(",", 
            id != null ? id : "N/A",  // Remplace par "N/A" si id est null
            titre,
            auteur,
            genre,
            String.valueOf(anneePublication),
            String.valueOf(disponible),
            imageUrl
        );
    }

    @Override
    public String toString() {
        return toCsvFormat();
    }
}
