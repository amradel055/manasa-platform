/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AhmedEssam
 */
public class NotificationDTO {

    private Integer id;
    private String title;
    private String message;
    private String icon;
    private String image;
    private String badge;
    private String dir;
    private Integer sendType;
    private String token;
    private List<String> tokens;
    private List<String> images;
    private List<IgmaUserDTO> isagUserDTOList;
    private IgmaUserDTO isagUserDTOSelected;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
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
     * @return the badge
     */
    public String getBadge() {
        return badge;
    }

    /**
     * @param badge the badge to set
     */
    public void setBadge(String badge) {
        this.badge = badge;
    }

    /**
     * @return the dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * @return the sendType
     */
    public Integer getSendType() {
        return sendType;
    }

    /**
     * @param sendType the sendType to set
     */
    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the tokens
     */
    public List<String> getTokens() {
        return tokens;
    }

    /**
     * @param tokens the tokens to set
     */
    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    /**
     * @return the images
     */
    public List<String> getImages() {
        if (images == null || images.isEmpty()) {
            images = new ArrayList<String>();
        }
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * @return the isagUserDTOList
     */
    public List<IgmaUserDTO> getIsagUserDTOList() {
        if (isagUserDTOList == null || isagUserDTOList.isEmpty()) {
            isagUserDTOList = new ArrayList<>();
        }
        return isagUserDTOList;
    }

    /**
     * @param isagUserDTOList the isagUserDTOList to set
     */
    public void setIsagUserDTOList(List<IgmaUserDTO> isagUserDTOList) {
        this.isagUserDTOList = isagUserDTOList;
    }

    /**
     * @return the isagUserDTOSelected
     */
    public IgmaUserDTO getIsagUserDTOSelected() {
        return isagUserDTOSelected;
    }

    /**
     * @param isagUserDTOSelected the isagUserDTOSelected to set
     */
    public void setIsagUserDTOSelected(IgmaUserDTO isagUserDTOSelected) {
        this.isagUserDTOSelected = isagUserDTOSelected;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

}
