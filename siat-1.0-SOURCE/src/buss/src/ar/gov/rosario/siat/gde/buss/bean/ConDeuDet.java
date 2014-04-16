//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetPrint;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente al detalle de una Constancia de Deuda.
 * Los estados posibles de un detalle son:
 *  - Activo - Inactivo: por traspado, por devolución de envío, por devolución puntual de deuda de una cuenta. '
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_conDeuDet")
public class ConDeuDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String OBS_CREACION_MANUAL = "Inclusi�n manual de deuda a la Constancia de Deuda";
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="idConstanciaDeu") 
	private ConstanciaDeu constanciaDeu; // NOT NULL,
	
	@Column(name = "idDeuda")
	private Long idDeuda;                // NOT NULL,
	
	@Column(name = "observacion")
	private String observacion;          // VARCHAR(255)

	
	// Constructores
	public ConDeuDet(){
		super();
	}
	
	// Metodos de Clase
	public static ConDeuDet getById(Long id) {
		return (ConDeuDet) GdeDAOFactory.getConDeuDetDAO().getById(id);
	}
	
	public static ConDeuDet getByIdNull(Long id) {
		return (ConDeuDet) GdeDAOFactory.getConDeuDetDAO().getByIdNull(id);
	}
	
	public static List<ConDeuDet> getList() {
		return (ArrayList<ConDeuDet>) GdeDAOFactory.getConDeuDetDAO().getList();
	}
	
	public static List<ConDeuDet> getListActivos() {			
		return (ArrayList<ConDeuDet>) GdeDAOFactory.getConDeuDetDAO().getListActiva();
	}
	
	/**
	 * Obtiene la lista de ConDeuDet filtrando por deuda y estado
	 * @param idDeuda si es nulo o <=0 no se tiene en cuenta
	 * @param idEstadoActivoInactivo si es nulo o <=0 no se tiene en cuenta
	 * @return
	 */
	public static List<ConDeuDet> getByDeudaYEstado(Long idDeuda, Integer idEstadoActivoInactivo){
		return (ArrayList<ConDeuDet>) GdeDAOFactory.getConDeuDetDAO().getByDeudaYEstado(idDeuda, idEstadoActivoInactivo);
	}
	
	/**
	 * Es Deuda En Constancia Habilitada, chequea si el idDeuda recibido existe en una constancia y 
	 * si el estado de la misma es habilitado. 
	 * 
	 * Si no existe en Constancia, por ser migrada, devuelve true
	 * 
	 * @author Cristian
	 * @param idDeuda
	 * @return boolean
	 */
	public static boolean existeEnConstanciaHabilitada(Long idDeuda) {
		return GdeDAOFactory.getConDeuDetDAO().existeEnConstanciaHabilitada(idDeuda);
	}
	
	// Getters y setters
	public ConstanciaDeu getConstanciaDeu() {
		return constanciaDeu;
	}
	public void setConstanciaDeu(ConstanciaDeu constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * Convierte la ConDeuDet a VO y le setea la deudaJudicial y la descripcion del estado en desEstado
	 * @return
	 * @throws Exception
	 */
	public ConDeuDetVO toVOConDeudaJudicial(boolean copiarList) throws Exception{
		ConDeuDetVO conDeuDetVO = (ConDeuDetVO) this.toVO(0, copiarList);
		conDeuDetVO.setDesEstado(this.getEstadoView());
		conDeuDetVO.setConstanciaDeu(this.constanciaDeu.toVOLight());
		Deuda deuda = getDeuda();
		conDeuDetVO.setDeuda(deuda.toVOForView());
		conDeuDetVO.getDeuda().getCuenta().setRecurso((RecursoVO) deuda.getRecurso().toVO(0, false));
		return conDeuDetVO;
	}
	
	/**
	 * Hace el toVO de nivel 0. A la constanciaDeu le setea solo el id. <br>
	 * A las deudas les actualiza el saldo y no les setea los conceptos 
	 * @return
	 * @throws Exception
	 */
	public ConDeuDetVO toVOforView() throws Exception{
		ConDeuDetVO conDeuDetVO = (ConDeuDetVO) this.toVO(0, false);
		conDeuDetVO.setDesEstado(this.getEstadoView());
		ConstanciaDeuVO constanciaDeuVO = new ConstanciaDeuVO();
		constanciaDeuVO.setId(this.constanciaDeu.getId());
		conDeuDetVO.setConstanciaDeu(constanciaDeuVO);
		
		Deuda deuda = getDeuda();
		conDeuDetVO.setDeuda((DeudaVO) deuda.toVO(0, false));
		conDeuDetVO.getDeuda().setListDeuRecCon(null);
		// actualiza el saldo de la deuda
		DeudaAct act = deuda.actualizacionSaldo(new Date());
		conDeuDetVO.getDeuda().setSaldoActualizado(act.getImporteAct());

		return conDeuDetVO;
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
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodConvenioCuota())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIOCUOTA_CODCONVENIOCUOTA );
		}
		
		if (StringUtil.isNullOrEmpty(getDesConvenioCuota())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIOCUOTA_DESCONVENIOCUOTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codConvenioCuota");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.CONVENIOCUOTA_CODCONVENIOCUOTA);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Obtiene la deuda judicial
	 * @return DeudaJudicial
	 */
	public DeudaJudicial getDeudaJudicial(){
		return DeudaJudicial.getByIdNull(this.getIdDeuda());
	}
	
	public Deuda getDeuda(){
		return Deuda.getById(this.getIdDeuda());
	}
	
	/** 
	 * Obtiene el ConDeuDetPrint cargado con los datos de la deuda
	 * @return ConDeuDetPrint
	 */
	public ConDeuDetPrint getConDeuDetPrint(){
	
		DeudaJudicial deudaJudicial = this.getDeudaJudicial(); 
		ConDeuDetPrint conDeuDetPrint = new ConDeuDetPrint(
				deudaJudicial.getAnio(),deudaJudicial.getPeriodo(), deudaJudicial.getSaldo(), deudaJudicial.getFechaVencimiento());
		return conDeuDetPrint;
	}
    
}