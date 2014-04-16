//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a la Seleccion Almacenada
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_selAlm")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="idTipoSelAlm", discriminatorType=DiscriminatorType.INTEGER)
public abstract class SelAlm extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlm";

	@ManyToOne(optional=false) 
    @JoinColumn(name="idTipoSelAlm",insertable=false, updatable=false) 
	private TipoSelAlm tipoSelAlm;   
	
	// Constructores
	public SelAlm(){
		super();
	}
	
	public SelAlm(TipoSelAlm tipoSelAlm){
		super();
		this.tipoSelAlm = tipoSelAlm;
	}
	
	// Getters y Setters
	public TipoSelAlm getTipoSelAlm() {
		return tipoSelAlm;
	}
	public void setTipoSelAlm(TipoSelAlm tipoSelAlm) {
		this.tipoSelAlm = tipoSelAlm;
	}
	//public List<SelAlmDet> getListSelAlmDet() {
	//	return listSelAlmDet;
	//}
	//public void setListSelAlmDet(List<SelAlmDet> listSelAlmDet) {
	//	this.listSelAlmDet = listSelAlmDet;
	//}

	// Metodos de Clase
	public static SelAlm getById(Long id) {
		return (SelAlm) GdeDAOFactory.getSelAlmDAO().getById(id);  
	}
	
	public static SelAlm getByIdNull(Long id) {
		return (SelAlm) GdeDAOFactory.getSelAlmDAO().getByIdNull(id);
	}
	
	public static List<SelAlm> getList() {
		return (ArrayList<SelAlm>) GdeDAOFactory.getSelAlmDAO().getList();
	}
	
	public static List<SelAlm> getListActivos() {			
		return (ArrayList<SelAlm>) GdeDAOFactory.getSelAlmDAO().getListActiva();
	}

	

	// Metodos de Instancia

	//Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
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
	
	// Metodos de negocio
	
	// Obtiene la cantidad de cuentas distintas existentes en la SelAlm de acuerdo al tipoSelAlmdet
	//public Long obtenerCantidadCuentas(TipoSelAlm tipoSelAlmDet){
		// implementada en SelAlmDeuda por ahora
	//	return null;
	//}
	
	// Cantidad total de registros de deuda
	//public Long obtenerCantidadRegistros(){
		// implementada en SelAlmDeuda por ahora		
	//	return null;
	//}
	// Sumatoria de saldos historicos de todos los registros de deuda existentes
	//public Double obtenerImporteHistoricoTotal(){
		// implementada en SelAlmDeuda por ahora		
	//	return null;
	//}

	// No se realiza Sumatoria de saldos de todos los registros de deuda existentes, actualizados a la fecha de envio
	
	/**
	 * Crea el SelAlmLog
	 * @param  selAlmLog
	 * @return SelAlmLog
	 * @throws Exception
	 */
	public SelAlmLog createSelAlmLog(SelAlmLog selAlmLog) throws Exception {
		
		selAlmLog.setSelAlm(this);
		
		// Validaciones de negocio
		if (!selAlmLog.validateCreate()) {
			return selAlmLog;
		}

		GdeDAOFactory.getSelAlmLogDAO().update(selAlmLog);

		return selAlmLog;
	}	

	/**
	 * Obtiene la lista de SelAlmLog de la SelAlm 
	 * @return List<SelAlmLog>
	 */
	public List<SelAlmLog> obtenerListSelAlmLog(){
		return GdeDAOFactory.getSelAlmLogDAO().getListSelAlmLogByIdSelAlm(this);
	}

	/**
 	 *  Borra la lista de Detalles de la Seleccion Almacenada 
	 * @return long
	 */
	public long deleteListSelAlmDet(){
		
		// TODO correr validaciones y devolver errores en la misma instancia de la SelAlm
		
		return GdeDAOFactory.getSelAlmDetDAO().deleteListSelAlmDetBySelAlm(this);
	}

	/**
	 * Borra la lista de SelAlmLog de la SelAlm
	 * @return int
	 */
	public int deleteListSelAlmLog(){
		
		// TODO correr validaciones y devolver errores en la misma instancia de la SelAlm
		
		return GdeDAOFactory.getSelAlmLogDAO().deleteListSelAlmLogBySelAlm(this);
	}

	/**
	 * Determina si la SelAlm contiene alguna SelAlmDet para el  idElemento y el idTipoSelAlmDet 
	 * @param idElemento
	 * @param idTipoSelAlmDet
	 * @return boolean
	 */
	public boolean contieneElemento(Long idElemento, Long idTipoSelAlmDet){
		return GdeDAOFactory.getSelAlmDetDAO().contieneSelAlmDet(this, idElemento, idTipoSelAlmDet);
	}

}
