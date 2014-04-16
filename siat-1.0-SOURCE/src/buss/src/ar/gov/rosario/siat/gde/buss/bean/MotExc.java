//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a los distintos motivos por los que se puede originar 
 * la exclusion de deuda en un envio, a saber:

 * @author tecso
 */
@Entity
@Table(name = "gde_motExc")
public class MotExc extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_POR_EXC_OTRAS_AREAS   = 1L;  // Excluida por otras areas de MR.
	public static final long ID_POR_EXC_ENVIO         = 2L;  // Excluida para el Envio.
	public static final long ID_POR_EXC_PROC          = 3L;  // Por exclusion de Procuradores en el Envio.
	public static final long ID_POR_NO_ASIG_PROC      = 4L;  // Por no poder asignarle el Procurador.
	public static final long ID_POR_DEU_CANCELADA     = 5L;  // Por Deuda Cancelada.
	public static final long ID_POR_DEU_PRESCRIPTA    = 6L;  // Por Deuda Prescripta.
	public static final long ID_POR_DEU_ANULADA       = 7L;  // Por Deuda Anulada.
	public static final long ID_POR_DEU_CONDONADA     = 8L;  // Por Deuda Condonada.
	public static final long ID_POR_DEU_CYQ           = 9L;  // Por Deuda en CyQ.
	public static final long ID_POR_DEU_JUDICIAL      = 10L; // Por Deuda en Judicial.
	public static final long ID_POR_DEU_INDETERMINADA = 11L; // Por Deuda Indeterminada.
	public static final long ID_POR_DEU_RECLAMADA     = 12L; // Por Deuda Reclamada.
	public static final long ID_POR_DEU_CONVENIO      = 13L; // Por Deuda en Convenio.
	public static final long ID_POR_EXENCION          = 14L; // Por Deuda Excluida por Exencion.
	public static final long ID_POR_CONV_NO_VIGENTE	  = 15L;
	public static final long ID_POR_CONV_CON_INDET	  = 16L;
	public static final long ID_POR_CONV_NO_CADUCO	  = 17L;
	public static final long ID_POR_NO_DEU_ADMIN	  = 18L;
	
	public static final long ID_DEUDA_ESTADO_VIA_INVALIDO = 19L; // con estado o via invalida
	
	public static final long ID_CONV_CUOT_NO_IMPAGO         = 20L; // Por ConvenioCuota con estado no impago
	public static final long ID_POR_CONV_CUOT_RECLAMADA     = 21L; // Por ConvenioCuota Reclamada.
	public static final long ID_POR_CONV_CUOT_INDETERMINADA = 22L; // Por ConvenioCuota Indeterminada.
	public static final long ID_POR_CONV_CUOT_NO_VIGENTE    = 23L; // Por ConvenioCuota con estado no vigente.
	
	public static final long ID_POR_NO_DEU_JUDICIAL	  = 24L;
	public static final long ID_POR_NO_CONV_CUOTA	  = 25L;
	public static final long ID_CONVENIO_VIA_INVALIDO = 26L; // con via invalida

	public static final long ID_POR_BROCHE_EXENTO_ENVIO_JUD = 27; // broche exento Envio jud
	
	public static final long ID_CONVENIO_INCONSISTENTE = 28;
	
	@Column(name = "codMotExc") // not null char(10)
	private String codMotExc;

	@Column(name = "desMotExc") // not null varchar(100)
	private String desMotExc;


	// Constructores
	public MotExc(){
		super();
	}
	
	public MotExc(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static MotExc getById(Long id) {
		return (MotExc) GdeDAOFactory.getMotExcDAO().getById(id);
	}
	
	public static MotExc getByIdNull(Long id) {
		return (MotExc) GdeDAOFactory.getMotExcDAO().getByIdNull(id);
	}
	
	public static List<MotExc> getList() {
		return (ArrayList<MotExc>) GdeDAOFactory.getMotExcDAO().getList();
	}
	
	public static List<MotExc> getListActivos() {			
		return (ArrayList<MotExc>) GdeDAOFactory.getMotExcDAO().getListActiva();
	}
	
	public static MotExc getByEstadoDeudaViaDeuda(EstadoDeuda estadoDeuda, ViaDeuda viaDeuda) {
		
		if (EstadoDeuda.ID_CANCELADA == estadoDeuda.getId().longValue()){
			// por deuda cancelada
			return MotExc.getById(ID_POR_DEU_CANCELADA);
		}
		if (EstadoDeuda.ID_PRESCRIPTA == estadoDeuda.getId().longValue()){
			// por deuda prescripta
			return MotExc.getById(ID_POR_DEU_PRESCRIPTA);
		}
		if (EstadoDeuda.ID_ANULADA == estadoDeuda.getId().longValue()){
			// por deuda anulada
			return MotExc.getById(ID_POR_DEU_ANULADA);
		}
		if (EstadoDeuda.ID_CONDONADA == estadoDeuda.getId().longValue()){
			// por deuda condonada
			return MotExc.getById(ID_POR_DEU_CONDONADA);
		}
		if (EstadoDeuda.ID_JUDICIAL == estadoDeuda.getId().longValue() && 
				ViaDeuda.ID_VIA_JUDICIAL == viaDeuda.getId().longValue()){
			// por deuda en judicial
			return MotExc.getById(1L);
		}
		if (ViaDeuda.ID_VIA_CYQ == viaDeuda.getId().longValue()){
			// por deuda en CyQ
			return MotExc.getById(ID_POR_DEU_CYQ);
		}

		// TODO verificar esto
		
		return null;
	}
	
	// Getters y setters
	public String getCodMotExc() {
		return codMotExc;
	}
	public void setCodMotExc(String codMotExc) {
		this.codMotExc = codMotExc;
	}
	public String getDesMotExc() {
		return desMotExc;
	}
	public void setDesMotExc(String desMotExc) {
		this.desMotExc = desMotExc;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		/*if (StringUtil.isNullOrEmpty(getCodMotExc())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_CODCONVENIO );
		}
		
		if (StringUtil.isNullOrEmpty(getDesMotExc())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_DESCONVENIO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codMotExc");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.CONVENIO_CODCONVENIO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	
}
