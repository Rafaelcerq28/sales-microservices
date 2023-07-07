package com.br.inventorycontrol.productsales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException{
    
    /*
     * Sending an 404 when the user is not found
     */

    public ItemNotFoundException(String message){
        super(message);
    }

}
