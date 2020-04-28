package com.apica.backend.integrationservice.exception;

import java.util.Set;

public class DocumentUnSupportedFieldPatchException extends RuntimeException {

    public DocumentUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}
