package com.banco.servicio;

import com.banco.excepcion.CuentaInvalidaException;
import com.banco.excepcion.MontoInvalidoException;
import com.banco.excepcion.SaldoInsuficienteException;
import com.banco.modelo.CuentaBancaria;
import com.banco.modelo.TipoTransaccion;
import com.banco.modelo.Transaccion;

import java.util.*;

/**
 * Implementación del servicio bancario.
 *
 * Responsabilidad única: coordina operaciones de alto nivel
 * delegando la lógica de negocio al modelo {@link CuentaBancaria}.
 */
public class BancaServicio implements IBancaServicio {

    // Repositorio en memoria: numeroCuenta → CuentaBancaria
    private final Map<String, CuentaBancaria> cuentas = new LinkedHashMap<>();

    // ── Gestión de cuentas ───────────────────────────────────────────────────

    @Override
    public CuentaBancaria crearCuenta(String numeroCuenta, String titular, double saldoInicial)
            throws MontoInvalidoException {

        String clave = numeroCuenta.trim().toUpperCase();
        if (cuentas.containsKey(clave)) {
            throw new IllegalStateException(
                "Ya existe una cuenta con el número: " + numeroCuenta);
        }
        CuentaBancaria nueva = new CuentaBancaria(clave, titular, saldoInicial);
        cuentas.put(clave, nueva);
        return nueva;
    }

    @Override
    public CuentaBancaria buscarCuenta(String numeroCuenta) throws CuentaInvalidaException {
        CuentaBancaria cuenta = cuentas.get(numeroCuenta.trim().toUpperCase());
        if (cuenta == null) {
            throw new CuentaInvalidaException(numeroCuenta);
        }
        return cuenta;
    }

    @Override
    public List<CuentaBancaria> listarCuentas() {
        return Collections.unmodifiableList(new ArrayList<>(cuentas.values()));
    }

    // ── Operaciones básicas ──────────────────────────────────────────────────

    @Override
    public void depositar(String numeroCuenta, double monto)
            throws CuentaInvalidaException, MontoInvalidoException {
        buscarCuenta(numeroCuenta).depositar(monto);
    }

    @Override
    public void retirar(String numeroCuenta, double monto)
            throws CuentaInvalidaException, MontoInvalidoException, SaldoInsuficienteException {
        buscarCuenta(numeroCuenta).retirar(monto);
    }

    // ── Funcionalidad 1: Transferencia ───────────────────────────────────────

    @Override
    public void transferir(String cuentaOrigen, String cuentaDestino, double monto)
            throws CuentaInvalidaException, MontoInvalidoException, SaldoInsuficienteException {

        CuentaBancaria origen  = buscarCuenta(cuentaOrigen);
        CuentaBancaria destino = buscarCuenta(cuentaDestino);
        origen.transferirA(destino, monto);
    }

    // ── Funcionalidad 2: Historial ───────────────────────────────────────────

    @Override
    public List<Transaccion> obtenerHistorial(String numeroCuenta)
            throws CuentaInvalidaException {
        return buscarCuenta(numeroCuenta).getHistorial();
    }

    @Override
    public List<Transaccion> obtenerHistorialPorTipo(String numeroCuenta, TipoTransaccion tipo)
            throws CuentaInvalidaException {
        return buscarCuenta(numeroCuenta).getHistorialPorTipo(tipo);
    }

    // ── Funcionalidad 3: Intereses y cargos ──────────────────────────────────

    @Override
    public double aplicarInteres(String numeroCuenta, double tasaAnual)
            throws CuentaInvalidaException, MontoInvalidoException {
        return buscarCuenta(numeroCuenta).aplicarInteres(tasaAnual);
    }

    @Override
    public void aplicarCargo(String numeroCuenta, double monto, String concepto)
            throws CuentaInvalidaException, MontoInvalidoException, SaldoInsuficienteException {
        buscarCuenta(numeroCuenta).aplicarCargo(monto, concepto);
    }
}
