package com.banco.excepcion;

/**
 * Excepción lanzada cuando se referencia una cuenta que no existe
 * o que no es válida para la operación.
 */
public class CuentaInvalidaException extends Exception {

    private final String numeroCuenta;

    public CuentaInvalidaException(String numeroCuenta) {
        super(String.format(
            "Cuenta inválida o no encontrada: '%s'.", numeroCuenta
        ));
        this.numeroCuenta = numeroCuenta;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
}
