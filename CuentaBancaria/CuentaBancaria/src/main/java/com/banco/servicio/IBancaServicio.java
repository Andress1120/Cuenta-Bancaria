package com.banco.servicio;

import com.banco.excepcion.CuentaInvalidaException;
import com.banco.excepcion.MontoInvalidoException;
import com.banco.excepcion.SaldoInsuficienteException;
import com.banco.modelo.CuentaBancaria;
import com.banco.modelo.TipoTransaccion;
import com.banco.modelo.Transaccion;

import java.util.List;

/**
 * Contrato del servicio bancario.
 *
 * Principio de inversión de dependencias (DIP):
 * el resto del sistema depende de esta abstracción, no de la implementación concreta.
 */
public interface IBancaServicio {

    // ── Gestión de cuentas ───────────────────────────────────────────────────

    CuentaBancaria crearCuenta(String numeroCuenta, String titular, double saldoInicial)
            throws MontoInvalidoException;

    CuentaBancaria buscarCuenta(String numeroCuenta) throws CuentaInvalidaException;

    List<CuentaBancaria> listarCuentas();

    // ── Operaciones ──────────────────────────────────────────────────────────

    void depositar(String numeroCuenta, double monto)
            throws CuentaInvalidaException, MontoInvalidoException;

    void retirar(String numeroCuenta, double monto)
            throws CuentaInvalidaException, MontoInvalidoException, SaldoInsuficienteException;

    // ── Funcionalidad 1: Transferencia ───────────────────────────────────────

    void transferir(String cuentaOrigen, String cuentaDestino, double monto)
            throws CuentaInvalidaException, MontoInvalidoException, SaldoInsuficienteException;

    // ── Funcionalidad 2: Historial ───────────────────────────────────────────

    List<Transaccion> obtenerHistorial(String numeroCuenta) throws CuentaInvalidaException;

    List<Transaccion> obtenerHistorialPorTipo(String numeroCuenta, TipoTransaccion tipo)
            throws CuentaInvalidaException;

    // ── Funcionalidad 3: Intereses y cargos ──────────────────────────────────

    double aplicarInteres(String numeroCuenta, double tasaAnual)
            throws CuentaInvalidaException, MontoInvalidoException;

    void aplicarCargo(String numeroCuenta, double monto, String concepto)
            throws CuentaInvalidaException, MontoInvalidoException, SaldoInsuficienteException;
}
