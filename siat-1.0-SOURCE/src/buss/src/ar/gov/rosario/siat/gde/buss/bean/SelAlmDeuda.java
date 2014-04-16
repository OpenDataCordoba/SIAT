//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.SelAlmAgregarParametrosSearchPage;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Bean correspondiente a la Seleccion Almacenada de Deuda
 * 
 * @author tecso
 */

@Entity
@DiscriminatorValue("1")
public class SelAlmDeuda extends SelAlm {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmDeuda";

	// Constructores
	public SelAlmDeuda(){
		super();
	}
	
	
	
	// Getters y Setters

	// Metodos de Clase
	public static SelAlmDeuda getById(Long id) {
		return (SelAlmDeuda) GdeDAOFactory.getSelAlmDeudaDAO().getById(id);  
	}
	
	public static SelAlmDeuda getByIdNull(Long id) {
		return (SelAlmDeuda) GdeDAOFactory.getSelAlmDAO().getByIdNull(id);
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
	
	/**
	 * Obtiene la cantidad total de Detalles de la Seleccion Almacenada.
	 * return Long
	 */
	//public Long obtenerCantidadRegistros(){
	//	return GdeDAOFactory.getSelAlmDetDAO().getTotalBySelAlmDeuda(this);
	//}

	/**
	 * Obtiene la cantidad total de Detalles de la Seleccion Almacenada para el tipo de SelAlmDet.
	 * @param  tipoSelAlmDet
	 * @return Long
	 */
	public Long obtenerCantidadRegistros(TipoSelAlm tipoSelAlmDet){
		return GdeDAOFactory.getSelAlmDetDAO().getTotalBySelAlmDeudaTipoSelAlmDet(this, tipoSelAlmDet);
	}
	
	/**
	 * Obtiene la cantidad total de Cuentas de los Detalles de la Seleccion Almacenada para el tipo de SelAlmDet.
	 * @param  tipoSelAlmDet
	 * @return Long
	 */
	public Long obtenerCantidadCuentas(TipoSelAlm tipoSelAlmDet){ 
		return PadDAOFactory.getCuentaDAO().getTotalDistintas(this, tipoSelAlmDet);
	}
	
	/**
	 * Sumatoria de saldos historicos de todos los registros de deuda existentes en la SelAlm
	 * @return Double
	 */

	/*
	public Double obtenerImporteHistoricoTotal(){
		Double sumaSaldo = GdeDAOFactory.getDeudaDAO().getSumaSaldoBySelAlmDeuda(this);
		if (sumaSaldo == null){
			return 0D;
		}else{
			return sumaSaldo;
		}
	}
	*/
	
	/**
	 * Sumatoria de saldos historicos de los registros de deuda existentes en la SelAlm
	 */
	public Double obtenerImporteHistorico(TipoSelAlm tipoSelAlmDet){

		Double sumaSaldo = 0D;
		if(tipoSelAlmDet.getEsTipoSelAlmDetDeuda()){
			sumaSaldo = GdeDAOFactory.getDeudaDAO().getSumaSaldoDeuda(this, tipoSelAlmDet);
		}else
		if(tipoSelAlmDet.getEsTipoSelAlmDetConvCuot()){
			sumaSaldo = GdeDAOFactory.getConvenioCuotaDAO().getSumaSaldo(this, tipoSelAlmDet);
		}
		if(sumaSaldo == null){
			sumaSaldo = 0D;
		}
		
		return sumaSaldo;
	}


	/**
	 * Genera las planillas de deuda de la Seleccion Almacenada
	 * @param tipoSelAlmDet tipo del detalle de la seleccion almacenada
	 * @param processDir path base donde generar las planillas
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportPlanillasDeuda(TipoSelAlm tipoSelAlmDet, boolean deudaSigueTitular, String processDir) throws Exception{
		return GdeDAOFactory.getSelAlmDetDAO().exportPlanillasDeudaBySelAlm(this, tipoSelAlmDet, deudaSigueTitular, processDir);
	}

	/**
	 * Genera las planillas de cuenta agrupando deuda y convenios de la Seleccion Almacenada
	 * @param tipoSelAlmDet tipo del detalle de la seleccion almacenada
	 * @param processDir path base donde generar las planillas
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportPlanillasCuenta(TipoSelAlm tipoSelAlmDet, boolean deudaSigueTitular, String processDir) throws Exception{
		return GdeDAOFactory.getSelAlmDetDAO().exportPlanillasCuentaBySelAlm(this, tipoSelAlmDet, deudaSigueTitular, processDir);
	}

	/**
	 * Genera las planillas de convenio cuota de la Seleccion Almacenada
	 * @param  tipoSelAlmDet
	 * @param  processDir
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportPlanillasConvenioCuota(TipoSelAlm tipoSelAlmDet, String processDir) throws Exception{
		return GdeDAOFactory.getSelAlmDetDAO().exportPlanillasConvenioCuotaBySelAlm(this, tipoSelAlmDet, processDir);
	} 
	
	/**
	 * Obtiene la lista de DeudaAdmin de manera paginada para la Seleccion Almacenada.
	 * Aplica el filtro Tipo de Detalle de Seleccion Almanacenada de Deuda Administrativa a los SelAlmDet
	 * @param firstResult
	 * @param maxResults
	 * @return List<DeudaAdmin>
	 */
	public List<DeudaAdmin> getListDeudaAdmin(Integer firstResult, Integer maxResults){
		return GdeDAOFactory.getDeudaAdminDAO().getListBySelAlm(this, firstResult, maxResults);
	}

	/**
	 * Obtiene la lista de DeudaJudicial de manera paginada para la Seleccion Almacenada.
	 * Aplica el filtro Tipo de Detalle de Seleccion Almanacenada de Deuda Judicial a los SelAlmDet
	 * @param firstResult
	 * @param maxResults
	 * @return List<DeudaAdmin>
	 */
	public List<DeudaJudicial> getListDeudaJudicial(Integer firstResult, Integer maxResults){
		return GdeDAOFactory.getDeudaJudicialDAO().getListBySelAlm(this, firstResult, maxResults);
	}
	
	/**
	 * Obtiene la lista de DeudaJudicial de manera paginada para la Seleccion Almacenada y 
	 * el Tipo de Detalle de Seleccion Almanacenada.
	 * @param firstResult
	 * @param maxResults
	 * @return List<DeudaAdmin>
	 */
	public List<ConvenioCuota> getListConvenioCuota(TipoSelAlm tipoSelAlm, Integer firstResult, Integer maxResults){
		return GdeDAOFactory.getConvenioCuotaDAO().getListBySelAlm(this, tipoSelAlm, firstResult, maxResults);
	}


	/**
	 * Carga los detalles de la seleccion almacenada a incluir usando como filtro el mapa de filtros.
	 * Crea el log correspondiente.
	 * @param mapaFiltros
	 * @throws Exception
	 */
	public void cargarSelAlmDetIncluidos(Map<String, String> mapaFiltros, Date fechaEnvio, ProcesoMasivo pm) throws Exception{
		
		SelAlmLog selAlmLog = new SelAlmLog();
		selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_AGREGAR_MASIVO));
		selAlmLog.setDetalleLog(mapaFiltros.get(SelAlmAgregarParametrosSearchPage.DETALLE_LOG));
		selAlmLog = this.createSelAlmLog(selAlmLog);
		if (selAlmLog.hasError()){
			selAlmLog.passErrorMessages(this);
			return;
		}
		
		String     idTipoSelAlmDet = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_TIPO_SEL_ALM_DET);
		TipoSelAlm tipoSelAlmDet   = TipoSelAlm.getById(Long.valueOf(idTipoSelAlmDet)); 
		
		if(tipoSelAlmDet.getEsTipoSelAlmDetConvCuot()){
			// realiza los inserts de las convenioCuotas en los selAlmDet de la selAlm
			GdeDAOFactory.getConvenioCuotaDAO().cargarSelAlmConvenioCuotaIncluida(mapaFiltros);
		}else{
			// realiza los inserts de las deudas en los selAlmDet de la selAlm
			GdeDAOFactory.getDeudaAdminDAO().cargarSelAlmDeudaIncluida(mapaFiltros, fechaEnvio, pm);
		}
	}
	
	/**
	 * Determina si la SelAlm contiene SelAlmDet
	 * @return boolean
	 */
	public boolean contieneSelAlmDet(){
		return GdeDAOFactory.getSelAlmDetDAO().contieneSelAlmDetBySelAlm(this);
	}

	/**
	 * Carga una lista de deuda en la seleccion 
	 * almacenada de deuda
	 *   
	 * @return void
	 */
	public void cargarSelAlmDeudaFromList(List<Deuda> listDeuda){
		
		for (Deuda deuda : listDeuda) {
			SelAlmDet selAlmDet = new SelAlmDet();
			selAlmDet.setSelAlm(this);
			selAlmDet.setIdElemento(deuda.getId());
			GdeDAOFactory.getSelAlmDetDAO().update(selAlmDet);
		}
	}
	
	/**
	 * Obtiene la lista de deuda administrativa de la seleccion
	 * almacenada de deuda
	 * 
	 * @return List<DeudaAdmin>
	 */
	public List<DeudaAdmin> getListDeudaAdmin(){
		return GdeDAOFactory.getDeudaAdminDAO().getListBySelAlm(this);
	}

	/**
	 * Obtiene la lista de deuda  de la seleccion
	 * almacenada de deuda
	 * 
	 * @return List<Deuda>
	 */
	public List<Deuda> getListDeuda(){
		return GdeDAOFactory.getDeudaDAO().getListDeudaBySelAlm(this);
	}

}
