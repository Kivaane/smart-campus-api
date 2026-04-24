package com.smartcampus.exception;

public class LinkedResourceNotFoundException extends Exception {
    private String resource;
    private String resourceId;

    public LinkedResourceNotFoundException(String resource, String id) {
        super(resource + " with ID '" + id + "' does not exist");
        this.resource = resource;
        this.resourceId = id;
    }

    public String getResource() {
        return resource;
    }

    public String getResourceId() {
        return resourceId;
    }
}
