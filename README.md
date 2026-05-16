# Cuenta Bancaria – Java POO

Proyecto Java que amplía una aplicación de cuenta bancaria aplicando principios de Programación Orientada a Objetos: encapsulación, responsabilidad única, apertura/cierre e inversión de dependencias.

## Nuevas funcionalidades

1. **Transferencias entre cuentas** – Mueve saldo de una cuenta a otra con validación de fondos y registro automático en ambas cuentas.
2. **Historial de transacciones** – Consulta todos los movimientos de una cuenta o filtra por tipo (depósitos, retiros, transferencias, etc.).
3. **Intereses y cargos** – Aplica una tasa de interés porcentual al saldo o descuenta un cargo fijo con concepto personalizado.

## Estructura del proyecto

```
src/main/java/com/banco/
├── Main.java
├── modelo/        → CuentaBancaria, Transaccion, TipoTransaccion
├── servicio/      → IBancaServicio, BancaServicio
├── excepcion/     → SaldoInsuficienteException, MontoInvalidoException, CuentaInvalidaException
└── util/          → ConsolaUtil
```

## Ejecución

1. Abrir la carpeta del proyecto en **IntelliJ IDEA**.
2. Marcar `src/main/java` como *Sources Root* si no lo detecta automáticamente.
3. Ejecutar la clase `Main.java`.

La consola mostrará una demostración de las tres funcionalidades con casos de éxito y manejo de errores.
