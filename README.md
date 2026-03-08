# Sistema Cajero Automatico ATM Web (JSF)

##### Grupo #9
- Cesar Leonardo Arana Santos - 201630020056
-  Eddy Josué campos Ortiz - 202320010622
   
<p>
Este proyecto consiste en una simulación básica de un cajero automático (ATM) desarrollada como aplicación web utilizando Java Server Faces (JSF). El sistema permite a los usuarios autenticarse mediante su número de cuenta y PIN, y realizar operaciones bancarias básicas como consultar saldo, depositar y retirar dinero.
</p>

![Login](https://github.com/CesarL-Aran/ATM-Tarea2/blob/main/ATM/image/login.png?raw=true)

<p>
El objetivo principal del proyecto es demostrar el uso de tecnologías Java para aplicaciones web, manejo de formularios, validación de datos y persistencia simple utilizando archivos de texto.
</p>

## Funcionalidades
- Inicio de sesión mediante número de cuenta y PIN
- Consulta de saldo disponible
- Depósito de dinero
- Retiro de dinero
- Validación de datos en los formularios
- Registro de clientes almacenado en un archivo `.txt`

![Menu](https://github.com/CesarL-Aran/ATM-Tarea2/blob/main/ATM/image/Menu.png?raw=true "Menu")

<br/>

![Transacciones](https://github.com/CesarL-Aran/ATM-Tarea2/blob/main/ATM/image/Operaciones.png?raw=true "Transacciones")

## Tecnologías utilizadas
- Java
- JavaServer Faces (JSF)
- XHTML
- Eclipse IDE
- HTML / CSS

## Estructura del proyecto
<p>
El sistema utiliza un **Managed Bean (atmBean)** para manejar la lógica del cajero automático.  
Los datos de los clientes se almacenan en un archivo de texto que contiene:
</p>

- Numero de Cuenta
- Pin
- Saldo de la Cuenta

##### Clientes de Prueba

- 1001,1234,5000
- 1002,4321,2500
- 1003,1111,10000
- 1004,2222,750
- 1005,9999,15000


## Historial de transacciones
<p>
El sistema incluye un registro de las operaciones realizadas por los usuarios.  
Cada vez que se realiza una operación como un **depósito o retiro**, la transacción se guarda en un archivo de texto (`historial.txt`).

Este archivo funciona como un registro simple de las operaciones realizadas en el cajero automático y permite llevar un control de las actividades de cada cuenta.

Cada línea del archivo representa una transacción e incluye información como:
</p>

- Tipo de operación (Depósito o Retiro)
- Número de cuenta
- Monto de la transacción
- Fecha
- Saldo resultante

![Historial](https://github.com/CesarL-Aran/ATM-Tarea2/blob/main/ATM/image/historal.png?raw=true)

<p>
Para poder acceder al historial de transacciones ese solo se puede llevar acabo por el administrador de ATM, esto evita que los usuarios no exponga información de otros usuarios.  Se debe entrar mediante el botón **ver historial**, luego ingresar usuario y contraseña, luego oprime el botón ver historial.
</p>

##### Usuario de Prueba

- Usuario: admin
- Contraseña: 1234

<p>
Gracia a los archivos podemos simular un sistema básico de historial de operaciones sin necesidad de utilizar una base de datos, utilizando únicamente almacenamiento en archivos de texto.
</p>
