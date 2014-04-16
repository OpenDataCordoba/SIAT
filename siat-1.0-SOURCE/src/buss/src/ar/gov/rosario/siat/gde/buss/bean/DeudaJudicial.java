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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.DeuJudRecConVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaJudicialVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;

/**
 * Deuda Judicial
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_deudaJudicial")
public class DeudaJudicial extends Deuda {

	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idDeuda")
	private List<DeuJudRecCon> listDeuRecCon;

	@Column(name="fechaEnvio")
	private Date fechaEnvio;
	
	// Contructores 
	public DeudaJudicial() {
		super();
	}
	
	// Getters y Setters
	@Override
	public List<DeuJudRecCon> getListDeuRecCon() {
		return listDeuRecCon;
	}

	public void setListDeuRecCon(List<DeuJudRecCon> listDeuRecCon) {
		this.listDeuRecCon = listDeuRecCon;
	}

	public synchronized Date getFechaEnvio() {
		return fechaEnvio;
	}

	public synchronized void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	// metodos de instancia
	
	/**
	 * Hace el toVO de nivel 1. Pasa al VO tambien la listDeuJudRecConVO
	 */
	public DeudaJudicialVO toVOForView() throws Exception{
		DeudaJudicialVO deudaJudicialVO = (DeudaJudicialVO) this.toVO(1, false);
		if(listDeuRecCon!=null){
			List<DeuJudRecConVO> listDeuJudRecConVO = new ArrayList<DeuJudRecConVO>();
			for(DeuRecCon deuRecCon: listDeuRecCon){
				DeuJudRecConVO deuJudRecConVO = new DeuJudRecConVO();
				deuJudRecConVO.setDescripcion(deuRecCon.getRecCon().getDesRecCon());
				deuJudRecConVO.setImporte(String.valueOf(deuRecCon.getImporte()));
				listDeuJudRecConVO.add(deuJudRecConVO);
			}
			deudaJudicialVO.setListDeuRecCon(listDeuJudRecConVO);
		}
		return deudaJudicialVO;
	}
	
	/**
	 * Hace el toVO de nivel 0. y no pasa al VO la listDeuJudRecConVO
	 */
	public DeudaJudicialVO toVOForViewSinConceptos() throws Exception{
		DeudaJudicialVO deudaJudicialVO = (DeudaJudicialVO) this.toVO(0, false);
		return deudaJudicialVO;
	}	
	// Metodos de clase

	public static DeudaJudicial getById(Long id) {
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getById(id);
	}
	
	public static DeudaJudicial getByIdNull(Long id) {
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getByIdNull(id);
	}
	
	public static DeudaJudicial getByCodRefPag(Long codRefPag){
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getByCodRefPag(codRefPag);
	}
	
	public static List<Deuda> getByCodRefPagBySearchPage(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		return (List<Deuda>) GdeDAOFactory.getDeudaAdminDAO().getByCodRefPagBySearchPage(liqCodRefPagSearchPage);
	}
	
	public static Deuda getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
	}
	
	public static Deuda getByCtaPerAnioSisResForRS(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema, resto);
	}

	public static Deuda getOriginalByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
	}

	public static List<Deuda> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		listDeuda.addAll(GdeDAOFactory.getDeudaJudicialDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto));
		//return (ArrayList<Deuda>) GdeDAOFactory.getDeudaJudicialDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
		return listDeuda;
	}
	
	/**
	 * Busca deudas por numero de cuenta y id de procurador
	 * @param nroCuenta Si es nulo o menor a 0 no se tiene en cuenta  
	 * @param idProcurador Si es nulo o menor a  0 no se tiene en cuenta
	 * @return
	 */
	public static List<DeudaJudicial> getByNroCtaYProcurador(String nroCuenta, Long idProcurador){
		return GdeDAOFactory.getDeudaJudicialDAO().getByNroCtaYProcurador(nroCuenta, idProcurador);
	}
	
	/**
	 * Obtiene deudas judiciales asignada al idProcurador pasado como parametro, que no esten en ninguna constancia de deuda y que tampoco esten en ninguna Gestion Judicial
	 * @param idProcurador - si es nulo no se tiene en cuenta (trae todas las deudas)
	 * @param estadoActivo - Si es True se traen solo las activas
	 * @return
	 * @throws Exception
	 */
	public static List<DeudaJudicial> getDeuJudSinConstanciaYSinGesJud(Long idProcurador, Long idCuenta, boolean estadoActivo) throws Exception {
		return GdeDAOFactory.getDeudaJudicialDAO().getSinConstanciaYSinGesJud(idProcurador, idCuenta, estadoActivo);
	}
	
	public static List<DeudaJudicial> getList() {
		return (ArrayList<DeudaJudicial>) GdeDAOFactory.getDeudaJudicialDAO().getList();
	}
	
	public static List<DeudaJudicial> getListActivos() {			
		return (ArrayList<DeudaJudicial>) GdeDAOFactory.getDeudaJudicialDAO().getListActiva();
	}
	
	public static List<Deuda> existeDeudaNotIn(Cuenta cuenta, Long[] listIdsAExcluir,  
			Long[] listIdsRecClaDeuExcluir, Date fechaVenDeuDes, Date fechaVenDeuHas, Long idViaDeudaSelected) {
		
		return GdeDAOFactory.getDeudaJudicialDAO().existeDeudaNotIn(cuenta, listIdsAExcluir, 
				listIdsRecClaDeuExcluir, fechaVenDeuDes, fechaVenDeuHas, idViaDeudaSelected);		
	}
	
	public static Deuda getByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getByCuentaPeriodoAnio(cuenta, periodo, anio);
	}
	
	public static List<DeudaJudicial> getByProcedimientoCyq(Long idProcedimiento){
		return GdeDAOFactory.getDeudaJudicialDAO().getByProcedimientoCyq(idProcedimiento);
	}
	
	// Metodos de Instancia
	
	/**
	 * verifica si cumple con los requisitos para poder ser agregada a una constancia de deuda:<br>
	 * 	- que no sea indeterminada
	 * 	- que no este CANCELADA
	 * 	- Si esta en via Judicial, que este en estado JUDICIAL
	 * @param idProcurador - Verifica si la deuda pertenece a este procurador
	 * @param idCuenta - Verifica si la deuda pertenece a esta cuenta
	 * @throws Exception 
	 */
	public boolean esValidaParaConstancia(Long idProcurador, Long idCuenta) throws Exception{
		if(getEsConvenio()){
			if(getConvenio().getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
				if(getConvenio().getEstadoConvenio().getId().longValue()!=EstadoConvenio.ID_RECOMPUESTO && getConvenio().getEstadoConvenio().getId().longValue()!=EstadoConvenio.ID_CANCELADO)
					return false;
			}
		}
		if(getEsIndeterminada())
			return false;
		if(getEstadoDeuda().getId().longValue()==EstadoDeuda.ID_CANCELADA)
			return false;
		if(getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_JUDICIAL && getEstadoDeuda().getId().longValue()!=EstadoDeuda.ID_JUDICIAL)
			return false;
		if(getProcurador().getId().longValue()!=idProcurador.longValue())
			return false;
		if(getCuenta().getId().longValue()!=idCuenta.longValue())
			return false;
		return true;
	}

	/**
	 * Verifica si cumple los requisitos para poder ser agregada a una gestion judicial:<br>
	 * 	- que no este en un Convenio en Via Judicial, que no este Recompuesto o Cancelado<br>
	 * 	- que no este indeterminada<br>
	 * 	 - que no este cancelada<br>
	 * @return
	 * @throws Exception
	 */
	public boolean esValidaParaGestionJudicial() throws Exception{
		if(!esIncluidaEnConvenioDePago() && !getEsIndeterminada() && !esCancelada())
			return true;
		return false;
	}
	
	/**
	 * Determina si se encuentra en un Convenio en Via Judicial, que no este Recompuesto o Cancelado
	 * @return boolean
	 * @throws Exception
	 */
	public boolean esIncluidaEnConvenioDePago() throws Exception{
		
		if (getConvenio() != null && 
			getConvenio().getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL &&
			getConvenio().getEstadoConvenio().getId().longValue() != EstadoConvenio.ID_RECOMPUESTO && 
			getConvenio().getEstadoConvenio().getId().longValue() != EstadoConvenio.ID_CANCELADO){
			return true;
		}
			
		return false;
	}
	
	/**
	 * Determina si el estado de la deuda esta cancelada
	 * @return
	 */
	public boolean esCancelada(){
		return (getEstadoDeuda().getId().longValue()==EstadoDeuda.ID_CANCELADA);
	}
	
	// Validaciones
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
	
	// Administracion de DeuJudRecCon
	public DeuJudRecCon createDeuJudRecCon(DeuJudRecCon deuJudRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuJudRecCon.validateCreate()) {
			return deuJudRecCon;
		}

		GdeDAOFactory.getDeuJudRecConDAO().update(deuJudRecCon);
		
		return deuJudRecCon;
	}	

	public DeuJudRecCon updateDeuJudRecCon(DeuJudRecCon deuJudRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuJudRecCon.validateUpdate()) {
			return deuJudRecCon;
		}

		GdeDAOFactory.getDeuJudRecConDAO().update(deuJudRecCon);
		
		return deuJudRecCon;
	}	

	public DeuJudRecCon deleteDeuJudRecCon(DeuJudRecCon deuJudRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuJudRecCon.validateDelete()) {
			return deuJudRecCon;
		}
				
		GdeDAOFactory.getDeuJudRecConDAO().delete(deuJudRecCon);
		
		return deuJudRecCon;
	}

}
