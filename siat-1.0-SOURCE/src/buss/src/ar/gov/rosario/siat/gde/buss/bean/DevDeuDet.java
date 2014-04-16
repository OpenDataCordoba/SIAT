//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Representa la deuda devuelta a via administrativa
 * 
 */
@Entity
@Table(name = "gde_devDeuDet")
public class DevDeuDet extends BaseBO {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DevDeuDet.class);
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idDevolucionDeuda") 
	private DevolucionDeuda devolucionDeuda; 

	@Column(name = "idDeuda")
	private Long idDeuda;                // NOT NULL,

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idConstanciaDeuOri") 
	private ConstanciaDeu constanciaDeuOri; 

	// Contructores 
	public DevDeuDet() {
		super();
	}
	
	//	 Getters y Setters

	public ConstanciaDeu getConstanciaDeuOri() {
		return constanciaDeuOri;
	}
	public void setConstanciaDeuOri(ConstanciaDeu constanciaDeuOri) {
		this.constanciaDeuOri = constanciaDeuOri;
	}
	public DevolucionDeuda getDevolucionDeuda() {
		return devolucionDeuda;
	}
	public void setDevolucionDeuda(DevolucionDeuda devolucionDeuda) {
		this.devolucionDeuda = devolucionDeuda;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	// Metodos de clase
	public static DevDeuDet getById(Long id) {
		return (DevDeuDet) GdeDAOFactory.getDevDeuDetDAO().getById(id);
	}

	public static DevDeuDet getByIdNull(Long id) {
		return (DevDeuDet) GdeDAOFactory.getDevDeuDetDAO().getByIdNull(id);
	}

	public static List<DevDeuDet> getList() {
		return (ArrayList<DevDeuDet>) GdeDAOFactory.getDevDeuDetDAO().getList();
	}

	public static List<DevDeuDet> getListActivos() {			
		return (ArrayList<DevDeuDet>) GdeDAOFactory.getDevDeuDetDAO().getListActiva();
	}
	
	public static DevDeuDet getActivoByIdDeuda(Long idDeuda) {
		return GdeDAOFactory.getDevDeuDetDAO().getActivoByIdDeuda(idDeuda);
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

		// Validaciones de requeridos y unicidad comunes
		this.validate();
		
		DeudaJudicial dj = DeudaJudicial.getById(this.getIdDeuda());

		// la deuda no se encuentre en un convenio en via judicial, excepto que este recompuesto o cancelado
		if (dj.esIncluidaEnConvenioDePago()){
			addRecoverableError(GdeError.DEUJUDICIAL_INCLUIDA_EN_CONVENIO_PAGO);
		}
		// la deuda no este indeterminada (se saca esta validacion el 23/10/08)
		/**
		try {
			if(dj.getEsIndeterminada()){
				addRecoverableError(GdeError.DEUJUDICIAL_INDETERMINADA);
			}
		} catch (Exception e) {
			log.error("Error al ejecutar getEsIndetermida() sobre la deuda");
		}
		**/
		// la deuda no este cancelada
		if(dj.esCancelada()){
			addRecoverableError(GdeError.DEUJUDICIAL_CANCELADA );
		}

		if (hasError()) {
			return false;
		}

		return !hasError();
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de requeridos y unicidad comunes
		this.validate();

		if (hasError()) {
			return false;
		}

		return !hasError();
	}


	private boolean validate(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		//Validaciones de Negocio

		return (!hasError());
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

		return !hasError();
	}

	//	 Metodos de negocio
	
	public DeudaJudicial getDeudaJudicial(){
		return DeudaJudicial.getByIdNull(this.getIdDeuda());
	}


}