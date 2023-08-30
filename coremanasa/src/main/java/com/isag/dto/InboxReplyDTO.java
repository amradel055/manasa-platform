/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author m lapan
 */

public class InboxReplyDTO extends BaseEntityDTO {

    private Integer message;

    private Integer sender;

    private Date sentDate;

    private String body;

    private String image;
    
    private String audio;
    
    private String video;

    // getters and setters
    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the audio
     */
    public String getAudio() {
        return audio;
    }

    /**
     * @param audio the audio to set
     */
    public void setAudio(String audio) {
        this.audio = audio;
    }

    /**
     * @return the video
     */
    public String getVideo() {
        return video;
    }

    /**
     * @param video the video to set
     */
    public void setVideo(String video) {
        this.video = video;
    }

}
