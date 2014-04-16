//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.DeudaAdminVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;

/**
 * Deuda Administrativa
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_deudaAdmin")
public class DeudaAdmin extends Deuda {	
	
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idDeuda")
	private List<DeuAdmRecCon> listDeuRecCon;

	
	// Contructores 
	public DeudaAdmin() {
		super();
	}

	// Getters y Setters
	@Override
	public List<DeuAdmRecCon> getListDeuRecCon() {
		return listDeuRecCon;
	}

	public void setListDeuRecCon(List<DeuAdmRecCon> listDeuRecCon) {
		this.listDeuRecCon = listDeuRecCon;
	}	
	
	// Metodos de clase

	
	public static DeudaAdmin getById(Long id) {
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getById(id);
	}
	
	public static DeudaAdmin getByIdNull(Long id) {
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByIdNull(id);
	}
	
	public static DeudaAdmin getByCodRefPag(Long codRefPag){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByCodRefPag(codRefPag);
	}
	
	public static List<Deuda> getByCodRefPagBySearchPage(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		return (List<Deuda>) GdeDAOFactory.getDeudaAdminDAO().getByCodRefPagBySearchPage(liqCodRefPagSearchPage);
	}
	
	public static Deuda getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
	}
	
	public static Deuda getByCtaPerAnioSisResForRS(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema, resto);
	}

	public static Deuda getOriginalByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
	}

	public static List<Deuda> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		listDeuda.addAll(GdeDAOFactory.getDeudaAdminDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto));
		//return (ArrayList<Deuda>) GdeDAOFactory.getDeudaAdminDAO().getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema, resto);
		return listDeuda;
	}
	
	public static Deuda getByCtaPerAnioSisForIRS(Long idCuenta,Long periodo, Long anio, Long nroSistema){
		return GdeDAOFactory.getDeudaAdminDAO().getByCtaPerAnioSisForIRS(idCuenta, periodo, anio, nroSistema);
	}
	
	public static List<Deuda> getListDeudaIRSByCtaPerAnioSis(Long idCuenta,Long periodo, Long anio){
		return GdeDAOFactory.getDeudaAdminDAO().getListDeudaIRSByCtaPerAnioSis(idCuenta, periodo, anio);
	}
	
	public static List<DeudaAdmin> getList() {
		return (ArrayList<DeudaAdmin>) GdeDAOFactory.getDeudaAdminDAO().getList();
	}
	
	public static List<DeudaAdmin> getListActivos() {			
		return (ArrayList<DeudaAdmin>) GdeDAOFactory.getDeudaAdminDAO().getListActiva();
	}
	
	/**
	 * periodoDesde-anioDesde y periodoHasta-anioHasta se toman como pares para filtro
	 * @param listCuenta
	 * @param periodoDesde
	 * @param anioDesde
	 * @param periodoHasta
	 * @param anioHasta
	 * @param listRecClaDeu
	 * @param cuentasPerAniosExcluir Array de String donde c/u tiene el formato: idCuenta-periodo-anio, sin separacion
			 periodo -> 2 lugares; anio -> 4 lugares
			 Ej: 556071012008  donde 556071= idCuenta   01=periodo   2008=anio
	 * @return
	 */
	public static List<DeudaAdmin> getList(List<Cuenta> listCuenta,Integer periodoDesde, Integer anioDesde, Integer periodoHasta,Integer anioHasta, List<RecClaDeu> listRecClaDeu,String[] cuentasPerAniosExcluir) {
		return GdeDAOFactory.getDeudaAdminDAO().getList(listCuenta,periodoDesde, anioDesde, 
												periodoHasta,anioHasta, listRecClaDeu, cuentasPerAniosExcluir);
	}
	
	public static List<DeudaAdmin> getList(Cuenta cuenta,Integer periodo, Integer anio, List<RecClaDeu> listRecClaDeu) {
		return GdeDAOFactory.getDeudaAdminDAO().getList(cuenta, periodo, anio, listRecClaDeu);
	}

	public static List<Deuda> existeDeudaNotIn(Cuenta cuenta, Long[] listIdsAExcluir,  
			Long[] listIdsRecClaDeuExcluir, Date fechaVenDeuDes, Date fechaVenDeuHas, Long idViaDeudaSelected) {
		
		return GdeDAOFactory.getDeudaAdminDAO().existeDeudaNotIn(cuenta, listIdsAExcluir, 
				listIdsRecClaDeuExcluir, fechaVenDeuDes, fechaVenDeuHas, idViaDeudaSelected);		
	}
	
	public static Deuda getByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByCuentaPeriodoAnio(cuenta, periodo, anio);
	}
	
	public static List<Deuda> getListRectifForPagoByCtaPer(Long idCuenta, Long periodo, Long anio, Long idRecClaDeu){
		return GdeDAOFactory.getDeudaAdminDAO().getListRectifForPagoByCtaPer(idCuenta, periodo, anio, idRecClaDeu);
	}
	
	public static List<Deuda> getListByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		return GdeDAOFactory.getDeudaAdminDAO().getListByCuentaPeriodoAnio(cuenta, periodo, anio);
	}
	
	public static Deuda getByCuentaPeriodoAnioParaDJ(Cuenta cuenta, Long periodo, Integer anio){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByCuentaPeriodoAnioParaDJ(cuenta, periodo, anio);
	}
	
	public static List<DeudaAdmin> getByEmision(Emision emision) {
		return GdeDAOFactory.getDeudaAdminDAO().getByEmision(emision);
	}

	
	public static List<Deuda> getListByCuentaPeriodoAnioIdsRecClaDeu(Cuenta cuenta, Integer periodo, Integer anio, List<Long> listIdsRecClaDeu){
		return GdeDAOFactory.getDeudaAdminDAO().getListByCuentaPeriodoAnioListRecClaDeu(cuenta, periodo, anio, listIdsRecClaDeu);
	}
	
	/**
	 * Oobtiene una deuda de una emision extraordinaria
	 * @param emision
	 * @return
	 */
	public static DeudaAdmin getByEmisionExt(Emision emision) {
		List<DeudaAdmin> list = GdeDAOFactory.getDeudaAdminDAO().getByEmision(emision);
		if(list!=null && !list.isEmpty())
			return list.get(0);
		return null;	
	}
	
	/**
	 * Devuelve toda la deuda incluida en un procedimiento de Cyq, ordenada por cuenta
	 * 
	 * @param procedimiento
	 * @return
	 */
	public static List<DeudaAdmin> getByProcedimientoCyq(Long idProcedimiento){
		return GdeDAOFactory.getDeudaAdminDAO().getByProcedimientoCyq(idProcedimiento);
	}

	/**
	 * Obtiene la sumatoria de deuda.importe para las deudas con los valores pasados como parametro
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param recClaDeu
	 * @return
	 */
	public static Double getImporteDeclarado(Cuenta cuenta,Integer periodo, Integer anio, 
			List<RecClaDeu> listRecClaDeu) {
		return GdeDAOFactory.getDeudaAdminDAO().getImporteDeclarado(cuenta,periodo,anio,listRecClaDeu);
	}
	
	
	/**
	 * Devuelve el total asentado por pagos en SIAT para deuda original o rectificativa, para el periodo fiscal declarados.
	 * No tiene en cuenta los generados por DJ.
	 * @param idCuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public static Double getTotalPagosMismoPeriodo(Long idCuenta,Integer periodo, Integer anio) {
		return GdeDAOFactory.getDeudaAdminDAO().getTotalPagosMismoPeriodo(idCuenta, periodo, anio);
	}
	
	/**
	 * Devuelve la sumatoria de deuda.importe para deuda impaga y periodo fiscal de una cuenta.
	 * 
	 * @param idCuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public static Double getTotalDeudaImpagaMismoPeriodo(Long idCuenta,Integer periodo, Integer anio) {
		return GdeDAOFactory.getDeudaAdminDAO().getTotalDeudaMismoPeriodo(idCuenta, periodo, anio, false);
	}
	
	/**
	 * Calcula la fecha de Vto para el periodo pasado como parametro.
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param listRecClaDeu
	 * @return
	 */
	public static Date getFecVtoPeriodo(Cuenta cuenta,Integer periodo, Integer anio, 
			  															List<RecClaDeu> listRecClaDeu) {
		return GdeDAOFactory.getDeudaAdminDAO().getFecVtoPeriodo(cuenta,periodo,anio,listRecClaDeu);
	}

	// Metodos de Instancia
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
	
	// Administracion de DeuAdmRecCon
	public DeuAdmRecCon createDeuAdmRecCon(DeuAdmRecCon deuAdmRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuAdmRecCon.validateCreate()) {
			return deuAdmRecCon;
		}

		GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		
		return deuAdmRecCon;
	}	

	public DeuAdmRecCon updateDeuAdmRecCon(DeuAdmRecCon deuAdmRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuAdmRecCon.validateUpdate()) {
			return deuAdmRecCon;
		}

		GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		
		return deuAdmRecCon;
	}	

	public DeuAdmRecCon deleteDeuAdmRecCon(DeuAdmRecCon deuAdmRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!deuAdmRecCon.validateDelete()) {
			return deuAdmRecCon;
		}
				
		GdeDAOFactory.getDeuAdmRecConDAO().delete(deuAdmRecCon);
		
		return deuAdmRecCon;
	}

	/**
	 * Obtiene una DeudaAdminVO cargada adecuadamente para los procesos masivos de deuda judicial
	 * Si la fecha de envio es distinta de null la utiliza para actualizar el saldo de la deudaAdminVO retornado
	 * Si la fecha de envio es nula, no actualiza el saldo.
	 * @param fechaEnvio
	 * @return DeudaAdminVO
	 * @throws Exception
	 */
	public DeudaAdminVO toVOForProcesoMasivo(Date fechaEnvio) throws Exception{
		
		DeudaAdminVO deudaAdminVO = (DeudaAdminVO) this.toVO(1); 
		
		if(fechaEnvio != null){
			deudaAdminVO.setSaldoActualizado(this.actualizacionSaldo(fechaEnvio).getImporteAct());
		}
		// ahora el titular principal esta cargado en la cuenta en la propiedad nomTitPri
		
		return deudaAdminVO;
	}
	
}
