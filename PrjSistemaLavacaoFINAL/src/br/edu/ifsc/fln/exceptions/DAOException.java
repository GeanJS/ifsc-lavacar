/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package br.edu.ifsc.fln.exceptions;

/**
 *
 * @author Aluno
 */
public class DAOException extends Exception {

    /**
     * Creates a new instance of <code>DAOException</code> without detail
     * message.
     */
    public DAOException() {
    }

    /**
     * Constructs an instance of <code>DAOException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DAOException(String msg) {
        super(msg);
    }
    
 
}
