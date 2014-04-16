//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqComVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a LiqCom.<br>
 * Liquidacion de Comision a Procuradores
 * @author tecso
 */
@Entity
@Table(name = "gde_liqcom")
public class LiqCom extends BaseBO {
	
	private static Logger log = Logger.getLogger(LiqCom.class);
	
	private static final long serialVersionUID = 1L;

	public static final String ID_LIQCOM = "ID_LIQCOM";

	@Column(name = "fechaLiquidacion")
	private Date fechaLiquidacion;

	@ManyToOne(optional=true,fetch = FetchType.LAZY)
	@JoinColumn(name = "idrecurso")
	private Recurso recurso;
	
	@ManyToOne(optional=true, fetch = FetchType.LAZY) 
	@JoinColumn(name = "idServicioBanco")
	private ServicioBanco servicioBanco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProcurador")
	private Procurador procurador;

	@Column(name = "fechaPagoDesde")
	private Date fechaPagoDesde;

	@Column(name = "fechaPagoHasta")
	private Date fechaPagoHasta;

	@Column(name = "observacion")
	private String observacion;

	@Column(name = "idCaso")
	private String idCaso;

	@Column(name = "esVueltaAtras")
	private Integer esVueltaAtras;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idLiqCom")
	private LiqCom liqComVueltaAtras;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCorrida")
	private Corrida corrida;

	// <#Propiedades#>

	// Constructores
	public LiqCom() {
		super();
		// Seteo de valores default
	}

	public LiqCom(Long id) {
		super();
		setId(id);
	}

	// Getters y setters
	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Date getFechaPagoDesde() {
		return fechaPagoDesde;
	}

	public void setFechaPagoDesde(Date fechaPagoDesde) {
		this.fechaPagoDesde = fechaPagoDesde;
	}

	public Date getFechaPagoHasta() {
		return fechaPagoHasta;
	}

	public void setFechaPagoHasta(Date fechaPagoHasta) {
		this.fechaPagoHasta = fechaPagoHasta;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Integer getEsVueltaAtras() {
		return esVueltaAtras;
	}

	public void setEsVueltaAtras(Integer esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
	}

	public LiqCom getLiqComVueltaAtras() {
		return liqComVueltaAtras;
	}

	public void setLiqComVueltaAtras(LiqCom liqComVueltaAtras) {
		this.liqComVueltaAtras = liqComVueltaAtras;
	}

	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	// Metodos de Clase
	public static LiqCom getById(Long id) {
		return (LiqCom) GdeDAOFactory.getLiqComDAO().getById(id);
	}

	public static LiqCom getByIdNull(Long id) {
		return (LiqCom) GdeDAOFactory.getLiqComDAO().getByIdNull(id);
	}

	public static List<LiqCom> getList() {
		return (List<LiqCom>) GdeDAOFactory.getLiqComDAO().getList();
	}

	public static List<LiqCom> getListActivos() {
		return (List<LiqCom>) GdeDAOFactory.getLiqComDAO().getListActiva();
	}


	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		return true;
	}

	/**
	 * Obtiene una LiqCom para el recurso y procurador pasado como parametros y que esten en estado  
	 * "En Preparación", "En espera comenzar", "Procesando" o "En espera continuar".<br>
	 * Los parametros pueden ser null
	 * 
	 * @param idRecurso
	 * @param idProcurador
	 */
	public static List<LiqCom> getByRecursoProcuradorSinTerminar(Long idRecurso, Long idProcurador, Long idLiqComNotIn) {
		return GdeDAOFactory.getLiqComDAO().getByRecursoProcuradorSinTerminar(idRecurso, idProcurador, idLiqComNotIn);		
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
		// limpiamos la lista de errores
		clearError();		

		// <#ValidateDelete#>
	    if(getCorrida().getPasoActual().equals(PasoCorrida.PASO_UNO)){
			if(getCorrida().getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESANDO)){
				// Esta procesando, no se puede eliminar
				return false;
			}else{
				// Esta en preparacion, se puede eliminar
				return true;
			}
		}else if(getCorrida().getPasoActual().equals(PasoCorrida.PASO_DOS)){
			if(getCorrida().getEstadoCorrida().getId().equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) ||
				getCorrida().getEstadoCorrida().getId().equals(EstadoCorrida.ID_EN_PREPARACION) ||
				getCorrida().getEstadoCorrida().getId().equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)){
				// Si esta en espera continuar se puede eliminar
				return true;
			}else{
				// Esta procesando el paso 2, no se puede eliminar
				return false;
			}
		}else if(getCorrida().getPasoActual().equals(PasoCorrida.PASO_TRES)){
			// Finalizo con exito, no se puede eliminar
			return false;
		}
		

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (fechaLiquidacion==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.LIQCOM_FECHALIQ_LABEL);
		}

		if((servicioBanco==null || servicioBanco.getId().longValue() < 0) && (recurso==null || recurso.getId().longValue() < 0))
			addRecoverableError(GdeError.LIQCOM_YA_EXISTE_LIQCOM);//BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		
		if(fechaPagoHasta== null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.LIQCOM_FECHAPAGOHASTA_LABEL);
					
		if (hasError()) {
			return false;
		}

		return true;
	}

	/**
	 * Hace el toVO(0, false) y setea con el mismo metodo recurso y procurador. Setea corrida con toVOWithDesEstado()
	 * @return
	 * @throws Exception
	 */
	public LiqComVO toVoForSearch() throws Exception{
		LiqComVO liqComVO = (LiqComVO) this.toVO(0, false);
		
		if(this.recurso != null)
			liqComVO.setRecurso((RecursoVO) recurso.toVO(0, false));
		if(this.servicioBanco != null)
			liqComVO.setServicioBanco((ServicioBancoVO) servicioBanco.toVO(0, false));
		liqComVO.setProcurador((ProcuradorVO) (procurador!=null?procurador.toVO(0, false):new ProcuradorVO(0L, StringUtil.SELECT_OPCION_TODOS)));
		liqComVO.setCorrida(corrida.toVOWithDesEstado());
//liqComVO.setCaso( (this.idCaso!=null && this.idCaso>0?new CasoVO(idCaso):new CasoVO()) );
		liqComVO.setIdCaso(this.getIdCaso());
		return liqComVO;
	}

	// Metodos de instancia
	/**
	 * Obtiene la lista de LiqComPro asociadas (una por cada procurador que tenga la liqCom), por idLiqCom
	 */
	public List<LiqComPro> getListLiqComPro() {
		return LiqComPro.getByIdLiqCom(this.getId());		
	}
	
	// Metodos de negocio
	
	// metodos para ABM liqComPro
	public LiqComPro createLiqComPro(LiqComPro liqComPro) throws Exception {
		// Validaciones de negocio
		if (!liqComPro.validateCreate()) {
			return liqComPro;
		}
		
		// graba liqCom
		GdeDAOFactory.getLiqComProDAO().update(liqComPro);

		return liqComPro;
	}
	
	public LiqComPro updateLiqComPro(LiqComPro liqComPro) throws Exception {
		// Validaciones de negocio
		if (!liqComPro.validateUpdate()) {
			return liqComPro;
		}

		GdeDAOFactory.getLiqComProDAO().update(liqComPro);
		
		return liqComPro;
	}

	/**
	 * * Elimina liqComPro, junto con la lista correspondiente en AuxLiqComProDeu
	 * @param liqComPro
	 * @return
	 * @throws Exception
	 */
	public LiqComPro deleteLiqComPro(LiqComPro liqComPro) throws Exception {
		// Validaciones de negocio
		if (!liqComPro.validateDelete()) {
			return liqComPro;
		}
		liqComPro.deleteListAuxLiqComProDeu();
		log.debug("Va a eliminar el liqComPro");
		GdeDAOFactory.getLiqComProDAO().delete(liqComPro);
		
		return liqComPro;
	}

	/**
	 * Elimina la lista de liqComPro asociada, junto con la lista correspondiente en AuxLiqComProDeu
	 * @throws Exception
	 */
	public void deleteListLiqComPro() throws Exception{
		for(LiqComPro liqComPro: getListLiqComPro()){			
			deleteLiqComPro(liqComPro);
		}		
	}
	
	// FIN metodos para ABM liqComPro	

	/**
	 * Emision Liquidacion de Comisiones para Procuradores: 
	 * Generacion de formularios para control del paso 1: 
	public void generarReportesLiqComPaso1(String outputDir, LiqComPro liqComPro) throws Exception{
		
		//-> Detalle de Transacciones (Planilla de calculo)
		List<String> listFileName = GdeDAOFactory.getLiqComDAO().exportReportesLiqComPasoUno(this, outputDir, liqComPro);

		int numHoja = 1;
		String nombre = "Detalle de Comisiones - Procurador: "+liqComPro.getProcurador().getDescripcion();
		
		for(String fileName: listFileName) {
			String descripcion = "Planilla con Liquidación de Comisiones a Procuradores en Aux. Hoja "+ numHoja;			
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			numHoja++;
		}
		
	}
*/
	
	
	/**
	 * Obtiene los mismos reportes del paso 1, porque el paso 2 lo unico que hace es pasar los datos generados en el paso 1 a la tabla definitiva
	 * @param outputDir
	 * @param listLiqComPro
	 * @throws Exception
	 */
	public void generarReportesLiqComPaso2(String outputDir, List<LiqComPro> listLiqComPro) throws Exception {
		
		for(LiqComPro liqComPro:listLiqComPro){
			String nomProcurador = StringUtil.replace(liqComPro.getProcurador().getDescripcion().trim(), " ", "_");
			String fileName = "liqCom_"+ liqComPro.getLiqCom().getId().toString()+"_"+nomProcurador+".pdf";
			String descripcion = "Planilla con Liquidación de Comisiones a Procuradores";			
			this.getCorrida().addOutputFile(nomProcurador, descripcion, outputDir+fileName);
		}
		
		/*List<String> listFileName = GdeDAOFactory.getLiqComDAO().exportReportesLiqComPasoDos(this, outputDir);

		int numHoja = 1;
		String nombre = "Liquidacion de Comisiones";
		
		for(String fileName: listFileName) {
			String descripcion = "Planilla con Liquidación de Comisiones a Procuradores en Aux. Hoja "+ numHoja;			
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			numHoja++;
		}*/
		
	}

	@Override
	public String infoString() {
		String ret=" Liquidacion de Comision a Procuradores";
		
		if(fechaLiquidacion!=null){
			ret+=" - Fecha Liquidacion: "+DateUtil.formatDate(fechaLiquidacion, DateUtil.ddSMMSYYYY_MASK);
		}

		if(fechaPagoDesde!=null){
			ret+=" - Fecha Pago Desde: "+DateUtil.formatDate(fechaPagoDesde, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fechaPagoHasta!=null){
			ret+=" - Fecha Pago Hasta: "+DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(recurso!=null){
			ret+=" - Recurso: "+recurso.getDesRecurso();
		}

		if(procurador!=null){
			ret+=" - Procurador: "+procurador.getDescripcion();
		}
		
		if(corrida!=null){
			ret+=" - Corrida: "+corrida.getDesCorrida();
		}
		
		if(observacion!=null){
			ret += " - Observacion: " + observacion;
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		
		return ret;
	}
	
}
