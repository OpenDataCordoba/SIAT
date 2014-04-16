//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a EstadoCueExe
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_hisEstCueExe")
public class HisEstCueExe extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCueExe") 
	private CueExe cueExe;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstadoCueExe") 
	private EstadoCueExe estadoCueExe;

	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "logCambios")
	private String logCambios;
	
	@Column(name = "observaciones")
	private String observaciones;


	//<#Propiedades#>
	
	// Constructores
	public HisEstCueExe(){
		super();
		// Seteo de valores default			
	}
	
	public HisEstCueExe(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static HisEstCueExe getById(Long id) {
		return (HisEstCueExe) ExeDAOFactory.getHisEstCueExeDAO().getById(id);
	}
	
	public static HisEstCueExe getByIdNull(Long id) {
		return (HisEstCueExe) ExeDAOFactory.getHisEstCueExeDAO().getByIdNull(id);
	}
	
	public static List<HisEstCueExe> getList() {
		return (List<HisEstCueExe>) ExeDAOFactory.getHisEstCueExeDAO().getList();
	}
	
	public static List<HisEstCueExe> getListActivos() {			
		return (List<HisEstCueExe>) ExeDAOFactory.getHisEstCueExeDAO().getListActiva();
	}


	// Getters y setters
	public CueExe getCueExe() {
		return cueExe;
	}

	public void setCueExe(CueExe cueExe) {
		this.cueExe = cueExe;
	}

	public EstadoCueExe getEstadoCueExe() {
		return estadoCueExe;
	}

	public void setEstadoCueExe(EstadoCueExe estadoCueExe) {
		this.estadoCueExe = estadoCueExe;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getLogCambios() {
		return logCambios;
	}

	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		/* Fue quitado por #Bug 629 / Validaciones        
		if (StringUtil.isNullOrEmpty(getObservaciones())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.HISESTCUEEXE_OBSERVACIONES);
		}*/
		
		if (hasError()) {
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
}
