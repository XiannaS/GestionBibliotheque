package model;

public class Livre {
    private String id;
    private String titre;
    private String auteur;
    private String genre;
    private int anneePublication;
    private boolean disponible;
    private String imageUrl; 
    
    // Constructeur
    public Livre(String titre, String auteur, String genre, int anneePublication, boolean disponible,String imageUrl) {
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
        this.setImageUrl(imageUrl); 
    }

    // Autres méthodes...


    // Constructeur avec ID
    public Livre(String id, String titre, String auteur, String genre, int anneePublication, boolean disponible,String imageUrl) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
        this.setImageUrl(imageUrl); 
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
    	  return id + "," + titre + "," + auteur + "," + genre + "," + anneePublication + "," + disponible + "," + imageUrl;
    }

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}