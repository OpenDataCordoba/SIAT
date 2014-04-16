//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Bean correspondiente a FileCorrida
 * 
 * @author tecso
 */
@Entity
@Table(name = "pro_fileCorrida")
public class FileCorrida extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "corrida";
	
	@ManyToOne(optional=false)  
    @JoinColumn(name="idCorrida")
	private Corrida corrida;
	
	@Column(name = "paso")
	private Integer paso;  
	
	@Column(name = "fileName")
	private String fileName;   
		
	@Column(name = "nombre")
	private String nombre;  
	
	@Column(name = "observacion")
	private String observacion;

	@Column(name = "ctdRegistros")
	private Long ctdRegistros;  

	@Column(name = "orden")
	private Long orden;  
	

	// Constructores
	public FileCorrida(){
		super();
	}
	
	public FileCorrida(Corrida corrida, Integer paso, String nombre, String observacion, String fileName){
		this.setCorrida(corrida);
		this.setPaso(paso);
		this.setNombre(nombre);
		this.setObservacion(observacion);
		this.setFileName(fileName);
	}
	
	public FileCorrida(Corrida corrida, Integer paso, String nombre, String observacion, String fileName, Long ctdRegistros){
		this(corrida, paso, nombre, observacion, fileName);
		this.setCtdRegistros(ctdRegistros);
	}


	// Getters y Setters
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getPaso() {
		return paso;
	}
	public void setPaso(Integer paso) {
		this.paso = paso;
	}
	public Long getCtdRegistros() {
		return ctdRegistros;
	}
	public void setCtdRegistros(Long ctdRegistros) {
		this.ctdRegistros = ctdRegistros;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	// Metodos de Clase
	public static FileCorrida getById(Long id) {
		return (FileCorrida) ProDAOFactory.getFileCorridaDAO().getById(id);  
	}
	
	public static FileCorrida getByIdNull(Long id) {
		return (FileCorrida) ProDAOFactory.getFileCorridaDAO().getByIdNull(id);
	}
	
	public static List<FileCorrida> getList() {
		return (ArrayList<FileCorrida>) ProDAOFactory.getFileCorridaDAO().getList();
	}
	
	public static List<FileCorrida> getListByCorridaYPaso(Corrida corrida, Integer paso) throws Exception {
		return (ArrayList<FileCorrida>) ProDAOFactory.getFileCorridaDAO().getListByCorridaYPaso(corrida, paso);
	}
	
	public static List<FileCorrida> getListActivos() {			
		return (ArrayList<FileCorrida>) ProDAOFactory.getFileCorridaDAO().getListActiva();
	}

	public static List<FileCorrida> getListByCorrida(Corrida corrida) throws Exception {
		return (ArrayList<FileCorrida>) ProDAOFactory.getFileCorridaDAO().getListByCorrida(corrida);
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	public boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	/**
	 * Valida la eliminacion
	 * @author 
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}
	
	/**
	 * Obtiene una planillaVO a partir de los datos contenidos en la FileCorrida
	 * @return PlanillaVO
	 */
	public PlanillaVO getPlanillaVO(){
		PlanillaVO planillaVO = new PlanillaVO();
		planillaVO.setFileName(this.getFileName());
		planillaVO.setCtdResultados(this.getCtdRegistros());
		planillaVO.setDescripcion(this.getObservacion());
		return planillaVO;
	}
	
}
