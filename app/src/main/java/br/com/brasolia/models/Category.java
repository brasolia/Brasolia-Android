package br.com.brasolia.models;

/**
 * Created by Eduardo on 24/08/2016.
 */
public class Category {

    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private String image;
    public String getImage() {
        return image;
    }
    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }

    private int order;
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
}
