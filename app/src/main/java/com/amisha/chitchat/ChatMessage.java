package com.amisha.chitchat;

public class ChatMessage {

    String name;
    String message;
    String url;

   /* public  ChatMessage(String name,String message,String url)
    {
        this.name=name;
        this.message=message;
        this.url=url;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
