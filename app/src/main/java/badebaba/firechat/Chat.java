package badebaba.firechat;

public class Chat {

    private String message;
    private String author;
    private String timestamp;


    private Chat() {
    }

    Chat(String message, String author,String Time) {
        this.message = message;
        this.author = author;
        this.timestamp = Time;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getTimestamp(){return timestamp;}
}
