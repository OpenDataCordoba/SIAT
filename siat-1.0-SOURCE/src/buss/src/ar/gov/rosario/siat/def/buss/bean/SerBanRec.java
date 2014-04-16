//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a Servicio Banco Recurso
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_serBanRec")
public class SerBanRec extends BaseBO {
	private static final long serialVersionUID = 1L;

	@Column(name = "fechaDesde")	
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")	
	private Date fechaHasta;

	@ManyToOne()  
    @JoinColumn(name="idServicioBanco")
	private ServicioBanco servicioBanco;

	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	//Constructores
	public SerBanRec(){
		super();
	}
	// Getters y Setters
	public Date getFechaDesde(){
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde){
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta(){
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta){
		this.fechaHasta = fechaHasta;
	}
	public ServicioBanco getServicioBanco(){
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBanco servicioBanco){
		this.servicioBanco = servicioBanco;
	}
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	
	// Metodos de Clase
	public static SerBanRec getById(Long id) {
		return (SerBanRec) DefDAOFactory.getSerBanRecDAO().getById(id);  
	}
	public static SerBanRec getByIdNull(Long id) {
		return (SerBanRec) DefDAOFactory.getSerBanRecDAO().getByIdNull(id);
	}
		
	public static List<SerBanRec> getList() {
		return (ArrayList<SerBanRec>) DefDAOFactory.getSerBanRecDAO().getList();
	}
	
	public static List<SerBanRec> getListActivos() {			
		return (ArrayList<SerBanRec>) DefDAOFactory.getSerBanRecDAO().getListActiva();
	}
	
	//	 Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
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

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		//UniqueMap uniqueMap = new UniqueMap();

		//Validaciones de Requeridos
		if (getServicioBanco()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERBANREC_SERVICIOBANCO);
		}
		if (getRecurso()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERBANREC_RECURSO);
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERBANREC_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(getFechaHasta()!=null){
			if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, DefError.SERBANREC_FECHADESDE, DefError.SERBANREC_FECHAHASTA);
			}
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

	// Metodos de negocio
	
	
}
