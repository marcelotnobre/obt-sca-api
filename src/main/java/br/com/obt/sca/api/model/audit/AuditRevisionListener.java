package br.com.obt.sca.api.model.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.hibernate.envers.RevisionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;

import br.com.obt.sca.api.AccessControlApiApplication;
import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.service.UsuarioService;

public class AuditRevisionListener implements RevisionListener {

	private static final Logger logger = LoggerFactory.getLogger(AuditRevisionListener.class);

	@Override
	public void newRevision(Object revisionEntity) {
		AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Optional<Usuario> usuarioOptional = ((UsuarioService) AccessControlApiApplication.getBean(UsuarioService.class))
				.findByEmailOrLogin(auth.getName());
		Usuario usuario = usuarioOptional.orElseThrow(() -> new InvalidGrantException("Usuario ou senha invalido."));

		audit.setUsuarioLogin(usuario.getLogin());
		audit.setUsuarioEmail(usuario.getEmail());

		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();
			audit.setIp(ip.getHostAddress());
			audit.setHostName(ip.getHostName());

		} catch (UnknownHostException ex) {
			if (logger.isDebugEnabled()) {
				logger.error("Erro Host  {} .", ex.getMessage());
			}
		}

		// String numeroSerial = "";
		// String so = String.valueOf( System.getProperty("os.name"));
		// if (so.startsWith("Linux")) {
		// try {
		// numeroSerial = getHardDiskSerialNumberLinux();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// } else if (so.startsWith("Windows")) {
		// numeroSerial = getHardDiskSerialNumberWindows();
		// }
		// if (logger.isDebugEnabled()) {
		// logger.info(" Log de auditoria registrado com suecesso. Error : {} .",
		// audit.toString());
		// logger.info(" HDD Numero do Seriado: {} .", numeroSerial);
		// }
	}

	// Get Hard Disk Drive Serial Number on Linux:
	// public static String getHardDiskSerialNumberLinux() throws Exception {
	//
	// String sc = "/sbin/udevadm info --query=property --name=sda"; // get HDD
	// parameters as non root user
	// String[] scargs = {"/bin/sh", "-c", sc};
	//
	// Process p = Runtime.getRuntime().exec(scargs);
	// p.waitFor();
	//
	// BufferedReader reader = new BufferedReader(new
	// InputStreamReader(p.getInputStream()));
	// String line;
	// StringBuilder sb = new StringBuilder();
	//
	// while ((line = reader.readLine()) != null) {
	// if (line.indexOf("ID_SERIAL_SHORT") != -1) { // look for ID_SERIAL_SHORT or
	// ID_SERIAL
	// sb.append(line);
	// }
	// }
	//
	// String numerSerialHD = sb.toString().substring(sb.toString().indexOf("=") +
	// 1);
	//
	// if (logger.isDebugEnabled()) {
	// logger.info(" HDD Serial number: {} .", numerSerialHD);
	// }
	//
	// return sb.toString().substring(sb.toString().indexOf("=") + 1);
	// }

	//// Get Hard Disk Drive Serial Number (Manufacture) Windows:
	// public static String getHardDiskSerialNumberWindows() {
	// String sc = "cmd /c" + "wmic diskdrive get serialnumber";
	//
	// Process p = null;
	// try {
	// p = Runtime.getRuntime().exec(sc);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// try {
	// p.waitFor();
	// } catch (InterruptedException e) {
	// logger.error(" Erro ao tentar ler Serial number Windows: {} .",
	//// e.getMessage());
	//// e.printStackTrace();
	// }
	//
	//
	// BufferedReader reader = new BufferedReader(new
	//// InputStreamReader(p.getInputStream()));
	//
	// String line;
	// StringBuilder sb = new StringBuilder();
	//
	// try {
	// while ((line = reader.readLine()) != null) {
	// sb.append(line);
	// }
	// } catch (IOException e) {
	// logger.error(" Erro ao tentar ler Serial number Windows: {} .",
	//// e.getMessage());
	//// e.printStackTrace();
	// }
	//
	// String numerSerialHD = sb.substring(sb.toString().lastIndexOf("r") +
	//// 1).trim();
	//
	// if (logger.isDebugEnabled()) {
	// logger.info(" HDD Serial number: {} .", numerSerialHD);
	// }
	//
	// return sb.toString().substring(sb.toString().indexOf("=") + 1);
	// }

}