//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a Proceso de Generacion
 * de archivos de Pago Automatico de Servicios 
 * y de Debito
 * 
 * @author tecso
 */
@Entity
@Table(name = "emi_proPasDeb")
public class ProPasDeb extends BaseBO {
	
	public static final String ADP_PARAM_ID = "idProPasDeb";
	
	@Transient
	private Logger log = Logger.getLogger(ProPasDeb.class);
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAtributo") 
	private Atributo atributo;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCorrida") 
	private Corrida corrida;
	
	@Column(name="atrValor")
	private String atrValor;
	
	@Column(name="anio")
	private Integer anio;

	@Column(name="periodo")
	private Integer periodo;

	@Column(name="fechaEnvio")
	private Date fechaEnvio;

	// Constructores
	public ProPasDeb(){
		super();
	}
	
	public ProPasDeb(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ProPasDeb getById(Long id) {
		return (ProPasDeb) EmiDAOFactory.getProPasDebDAO().getById(id);
	}
	
	public static ProPasDeb getByIdNull(Long id) {
		return (ProPasDeb) EmiDAOFactory.getProPasDebDAO().getByIdNull(id);
	}
	
	public static List<ProPasDeb> getList() {
		return (ArrayList<ProPasDeb>) EmiDAOFactory.getProPasDebDAO().getList();
	}
	
	public static List<ProPasDeb> getListActivos() {			
		return (ArrayList<ProPasDeb>) EmiDAOFactory.getProPasDebDAO().getListActiva();
	}
	
	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}

	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public String getAtrValor() {
		return atrValor;
	}

	public void setAtrValor(String atrValor) {
		this.atrValor = atrValor;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	
	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
        //	Validaciones        
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.PROPASDEB_RECURSO);
		}
		
		if (getAnio() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.PROPASDEB_ANIO);
		}

		if (getPeriodo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.PROPASDEB_PERIODO);
		}

		if (getPeriodo() != null && getRecurso() != null && !getRecurso().validatePeriodo(getPeriodo())) {
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EmiError.PROPASDEB_PERIODO);
		}
		
		if (getFechaEnvio() ==  null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.PROPASDEB_FECHA_ENVIO);
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}

	public void cambiarFechaEnvio(Date newFechaEnvio) {
		try {
			log.debug("cambiarFechaEnvio: enter");

			Corrida corrida = this.getCorrida();
			List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
			
			for (FileCorrida fileCorrida: listFileCorrida) {
				String fileName = fileCorrida.getNombre();
				String filePath = fileCorrida.getFileName();
			
				// Si es un archivo Link
				if (fileName.startsWith(LinkFile.FILE_NAME_PREFIX)) {
					File file = new File(filePath);
					if (!file.exists()) {
						log.debug("No existe el archivo " + filePath);
						addRecoverableValueError("No existe el archivo Link: " + filePath);
					} else {
						log.debug("cambiarFechaEnvio: procesado " + file.getName());

						File newFile = LinkFile.modificarFechaProc(file, newFechaEnvio);
						fileCorrida.setNombre(newFile.getName());
						fileCorrida.setFileName(newFile.getAbsolutePath());
					}
				}
				
				// Si es un archivo de control Link
				if (fileName.startsWith(LinkFile.CTRL_FILE_NAME_PREFIX)) {
					File file = new File(filePath);
					if (!file.exists()) {
						log.debug("No existe el archivo " + filePath);
						addRecoverableValueError("No existe el archivo de Control Link: " + filePath);
					} else {
						log.debug("cambiarFechaEnvio: procesado " + file.getName());

						File newFile = LinkFile.modificarFechaProcCtrl(file, newFechaEnvio);
						fileCorrida.setNombre(newFile.getName());
						fileCorrida.setFileName(newFile.getAbsolutePath());
					}
				}

				// Si es un archivo Banelco
				if (fileName.startsWith(BanelcoFile.FILE_NAME_PREFIX)) {
					File file = new File(filePath);
					if (!file.exists()) {
						log.debug("No existe el archivo " + filePath);
						addRecoverableValueError("No existe el archivo Banelco: " + filePath);
					} else {
						log.debug("cambiarFechaEnvio: procesado " + file.getName());
						
						File newFile = BanelcoFile.modificarFechaProc(file, newFechaEnvio);	
						fileCorrida.setNombre(newFile.getName());
						fileCorrida.setFileName(newFile.getAbsolutePath());
					}
				}
				
				// Si es un archivo de Debitos
				if (fileName.startsWith(DebitoFile.FILE_NAME_PREFIX)) {
					File file = new File(filePath);
					if (!file.exists()) {
						log.debug("No existe el archivo " + filePath);
						addRecoverableValueError("No existe el archivo de Debito: " + filePath);
					} else {
						log.debug("cambiarFechaEnvio: procesado " + file.getName());
						
						File newFile = DebitoFile.modificarFechaProc(file, newFechaEnvio);
						fileCorrida.setNombre(newFile.getName());
						fileCorrida.setFileName(newFile.getAbsolutePath());
					}
				}

				corrida.updateFileCorrida(fileCorrida);
			}
			
			log.debug("cambiarFechaEnvio: exit");
			
		} catch (Exception e) {
			log.error("Ocurrio una excepcion en el cambio de fecha de envio ", e);
			addRecoverableValueError("Ocurrio una excepcion en el cambio de fecha de envio. Consulte los logs");
		}
	}
}
