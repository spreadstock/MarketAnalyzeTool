/**
 * 
 */
package com.cooeration.marketengine.framework.exception;

/**
 * @author exubixu
 */
public class EngineException extends RuntimeException
{
    /**
     * 
     */
    private String status;
    private String errorMessage;


    public EngineException(String errorMessage, String status)
    {
        super(errorMessage);

        this.errorMessage = errorMessage;
        this.status = status;

    }
    
    public EngineException(String errorMessage, String status, Throwable e)
    {
        super(errorMessage,e);

        this.errorMessage = errorMessage;
        this.status = status;

    }
}
