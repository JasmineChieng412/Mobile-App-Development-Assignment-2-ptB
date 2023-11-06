package curtin.edu.ptb;

public class ImageItems {

    private String imageUrl, tags;
    private int likes;

    public ImageItems(String imageUrl, String tags, int likes){
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.likes = likes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTags() {
        return tags;
    }
}
