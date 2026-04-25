package com.divami.java_project.exception;

/**
 * Thrown when a requested entity does not exist or has been soft-deleted.
 * Maps to HTTP 404 via {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(resourceName + " not found with id: " + resourceId);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() { return resourceName; }
    public Object getResourceId() { return resourceId; }
}
