package atm.jsf.uth.hn;

import java.io.*;
import java.util.*;
import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

@Named("atmBean")
@SessionScoped
public class ATM_bean implements Serializable {

	private String numeroCuenta;
	private String pinConfirmacion;
	private String pin;
	private double saldo;
	private double monto;

	private String user;
	private String pass;
	private boolean adminAutenticado = false;
	private String lista;

	private File archivo = new File(
			FacesContext.getCurrentInstance().getExternalContext().getRealPath("/clientes.txt"));

	private File historialArchivo = new File(
			FacesContext.getCurrentInstance().getExternalContext().getRealPath("/historial.txt"));

	public String login() {

		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

			String linea;

			while ((linea = br.readLine()) != null) {

				String[] partes = linea.split(",");

				if (partes.length == 3) {

					String numarchivo = partes[0];
					String pinArchivo = partes[1];
					double montoArchivo = Double.parseDouble(partes[2]);
					// System.out.println(numarchivo+" - "+pinArchivo);

					if (numarchivo.equals(numeroCuenta) && pinArchivo.equals(pin)) {
						// usuario encontrado, cargar datos
						numeroCuenta = numarchivo;
						pin = pinArchivo;
						saldo = montoArchivo;
						return "menu.xhtml?faces-redirect=true";
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acceso Denegado", null));
		return "login.xhtml?faces-redirect=true"; // login fallido
	}

	public void logout() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		if (session != null) {
			session.invalidate();
		}

		try {
			facesContext.getExternalContext().redirect("index.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void retirar() {
		if (!pinConfirmacion.equals(pin)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "PIN incorrecto", null));
			monto = 0;
			pinConfirmacion = "";
			return;
		}

		if (monto <= 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Monto inválido", null));
			monto = 0;
			pinConfirmacion = "";
			return;
		}

		if (monto > saldo) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Saldo insuficiente", null));
			monto = 0;
			pinConfirmacion = "";
			return;
		}

		saldo -= monto;

		sobrescribirArchivo();
		guardarHistorial("RETIRO");
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Retiro exitoso. Nuevo saldo: " + saldo, null));

		monto = 0;
		pinConfirmacion = "";
	}

	public void depositar() {

		if (!pinConfirmacion.equals(pin)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "PIN incorrecto", null));
			monto = 0;
			pinConfirmacion = "";
			return;
		}

		if (monto <= 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "El monto debe ser mayor que cero", null));
			monto = 0;
			pinConfirmacion = "";
			return;
		}

		saldo += monto;

		sobrescribirArchivo();
		guardarHistorial("DEPOSITO");
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Retiro exitoso. Nuevo saldo: " + saldo, null));

		monto = 0;
		pinConfirmacion = "";
	}

	private void sobrescribirArchivo() {

		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;

			while ((linea = br.readLine()) != null) {

				String[] partes = linea.split(",");

				if (partes.length == 3) {

					String numarchivo = partes[0];

					if (numarchivo.equals(numeroCuenta)) {
						// reemplazar la línea con el saldo actualizado
						sb.append(numeroCuenta).append(",").append(pin).append(",").append(saldo).append("\n");
					} else {
						sb.append(linea).append("\n"); // mantener otras líneas
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// escribir todo de nuevo
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
			bw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void leerHistorial() {
		File historialArchivo = new File(
				FacesContext.getCurrentInstance().getExternalContext().getRealPath("/historial.txt"));

		StringBuilder contenido = new StringBuilder();

		if (!historialArchivo.exists()) {
			lista = "No se encontró historial.txt";
		}

		try (BufferedReader br = new BufferedReader(new FileReader(historialArchivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				contenido.append(linea).append("<br/>"); // saltos de línea para JSF
			}
		} catch (IOException e) {
			e.printStackTrace();
			lista = " Error al leer historial: " + e.getMessage();
		}

		lista = contenido.toString();
	}

	private void guardarHistorial(String tipoOperacion) {
		String linea = tipoOperacion + "-" + numeroCuenta + "-" + monto + "-" + new Date() + "-" + saldo;

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(historialArchivo, true))) { // append
			bw.write(linea);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void validarAdmin() {

		if (user.equals("admin") && pass.equals("1234")) {
			adminAutenticado = true;
			leerHistorial();
		} else {
			adminAutenticado = false;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas", null));
		}
	}

	public String logoutAdmin() {

		adminAutenticado = false;
		user = "";
		pass = "";
		return "index.xhtml?faces-redirect=true";
	}

	public String getnumeroCuenta() {
		return numeroCuenta;
	}

	public void setnumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getPinConfirmacion() {
		return pinConfirmacion;
	}

	public void setPinConfirmacion(String pinConfirmacion) {
		this.pinConfirmacion = pinConfirmacion;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean isAdminAutenticado() {
		return adminAutenticado;
	}

	public void setAdminAutenticado(boolean adminAutenticado) {
		this.adminAutenticado = adminAutenticado;
	}

	public String getLista() {
		return lista;
	}

	public void setLista(String lista) {
		this.lista = lista;
	}

}