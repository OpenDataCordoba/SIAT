//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.DetallePagoVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.ImpuestoAfip;

/**
 * Bean correspondiente a Envios de Osiris
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_envioOsiris")
public class EnvioOsiris extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger log = Logger.getLogger(EnvioOsiris.class); 
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstadoEnvio")
	private EstadoEnvio estadoEnvio;
	
	@Column(name="idEnvioAfip")
	private Long idEnvioAfip;
	
	@Column(name="idOrgRecAfip")
	private Long idOrgRecAfip;
	
	@Column(name="fechaRecepcion")
	private Date fechaRecepcion;
	
	@Column(name="fechaProceso")
	private Date fechaProceso;
	
	@Column(name="fechaRegistroMulat")
	private Date fechaRegistroMulat;
	
	@Column(name="observacion")
	private String observacion;
	
	@Column(name="cantidadPagos")
	private Integer cantidadPagos;
	
	@Column(name="importePagos")
	private Double importePagos;
	
	@Column(name="canDecJur")
	private Integer canDecJur;
	
	@OneToMany(mappedBy="envioOsiris",fetch=FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris")
	@OrderBy(clause="cantTransaccion DESC,importeTotal DESC")
	private List<CierreBanco> listCierreBanco;
	
	@OneToMany(mappedBy="envioOsiris",fetch=FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris")
	private List<TranAfip> listTranAfip;

	@OneToMany(mappedBy="envioOsiris",fetch=FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris")
	private List<Conciliacion> listConciliacion;
	
	@OneToMany(mappedBy="envioOsiris",fetch=FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris")
	private List<EnvNotObl> listEnvNotObl;
	
	/*
	 * Mapa de Contadores usados en Log de Procesar EnvioOsiris:
	 * 		1-ForDecJur Generadas Correctamente
	 * 		2-ForDecJur Pendientes de Procesar
	 * 		3-TranArc 	Generadas (Transacciones de Pago)
	 */
	@Transient 
	private Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
	
	
	// Constructores
	public EnvioOsiris(){
		super();
	}
	
	
	//Metodos de clase
	public static EnvioOsiris getById(Long id) {
		return (EnvioOsiris) BalDAOFactory.getEnvioOsirisDAO().getById(id);  
	}
	
	public static EnvioOsiris getByIdNull(Long id) {
		return (EnvioOsiris) BalDAOFactory.getEnvioOsirisDAO().getByIdNull(id);
	}
	
	public static List<EnvioOsiris> getList() {
		return (ArrayList<EnvioOsiris>) BalDAOFactory.getEnvioOsirisDAO().getList();
	}
	
	public static List<EnvioOsiris> getListActivos() {			
		return (ArrayList<EnvioOsiris>) BalDAOFactory.getEnvioOsirisDAO().getListActiva();
	}
	
	public static EnvioOsiris getByIdEnvioAfip(Long idEnvioAfip){
		return BalDAOFactory.getEnvioOsirisDAO().getByIdEnvioAfip(idEnvioAfip);
	}
	
	public static List<EnvioOsiris>getListInconsistentes(){
		return BalDAOFactory.getEnvioOsirisDAO().getListInconsistentes();
	}
	
	public static List<EnvioOsiris> getListPendientes(){			
		return (ArrayList<EnvioOsiris>) BalDAOFactory.getEnvioOsirisDAO().getListPendientes();
	}

	public static List<EnvioOsiris> getListConciliado(){			
		return (ArrayList<EnvioOsiris>) BalDAOFactory.getEnvioOsirisDAO().getListConciliado();
	}
	
	public void setListEnvNotObl(List<EnvNotObl> listEnvNotObl) {
		this.listEnvNotObl = listEnvNotObl;
	}


	public List<EnvNotObl> getListEnvNotObl() {
		return listEnvNotObl;
	}


	public static Boolean existenEnvioOsirisConciliados(){			
		return BalDAOFactory.getEnvioOsirisDAO().existenEnvioOsirisConciliados();
	}
	
	public List<TranAfip> getListTranDecJur() {			
		return (ArrayList<TranAfip>) BalDAOFactory.getTranAfipDAO().getListDecJur(this);
	}
	
	public List<TranAfip> getListTranAfipDJHasError() {			
		return (ArrayList<TranAfip>) BalDAOFactory.getTranAfipDAO().getListTranAfipDJHasError(this);
	}
		
	public List<TranAfip> getListTranPago() {			
		return (ArrayList<TranAfip>) BalDAOFactory.getTranAfipDAO().getListPago(this);
	}
	
	/**
	 * Retorna la cantidad de transacciones asociadas del tipo  "DJ" y "DJ y Pago"
	 */
	public Long getCantTranDecJur() {			
		return BalDAOFactory.getTranAfipDAO().getCantDecJur(this);
	}
	
	/**
	 * Retorna la cantidad de transacciones asociadas del tipo "Pago"
	 */
	public Long getCantTranPago() {			
		return BalDAOFactory.getTranAfipDAO().getCantPago(this);
	}
	
	/**
	 * Retorna la sumatoria de totMonto de las TranAfip asociadas al EnvioOsiris
	 */
	public Double getSumTotalImportePagos() {			
		return BalDAOFactory.getTranAfipDAO().getTotalImportePagos(this);
	}
	
	public Boolean existenCierreBancoSinConciliar() throws Exception{			
		return BalDAOFactory.getCierreBancoDAO().existenCierreBancoSinConciliarForEnvio(this);
	}
	
	public Boolean existenForDecJurForEnvio() throws Exception{			
		return AfiDAOFactory.getForDecJurDAO().existenForDecJurForEnvio(this);
	}
	
	public EstadoEnvio getEstadoEnvio() {
		return estadoEnvio;
	}


	public void setEstadoEnvio(EstadoEnvio estadoEnvio) {
		this.estadoEnvio = estadoEnvio;
	}


	public Long getIdEnvioAfip() {
		return idEnvioAfip;
	}


	public void setIdEnvioAfip(Long idEnvioAfip) {
		this.idEnvioAfip = idEnvioAfip;
	}


	public Long getIdOrgRecAfip() {
		return idOrgRecAfip;
	}


	public void setIdOrgRecAfip(Long idOrgRecAfip) {
		this.idOrgRecAfip = idOrgRecAfip;
	}


	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}


	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}


	public Date getFechaProceso() {
		return fechaProceso;
	}


	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}


	public Date getFechaRegistroMulat() {
		return fechaRegistroMulat;
	}


	public void setFechaRegistroMulat(Date fechaRegistroMulat) {
		this.fechaRegistroMulat = fechaRegistroMulat;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public Integer getCantidadPagos() {
		return cantidadPagos;
	}


	public void setCantidadPagos(Integer cantidadPagos) {
		this.cantidadPagos = cantidadPagos;
	}


	public Double getImportePagos() {
		return importePagos;
	}


	public void setImportePagos(Double importePagos) {
		this.importePagos = importePagos;
	}


	public Integer getCanDecJur() {
		return canDecJur;
	}


	public void setCanDecJur(Integer canDecJur) {
		this.canDecJur = canDecJur;
	}


	public List<CierreBanco> getListCierreBanco() {
		return listCierreBanco;
	}


	public void setListCierreBanco(List<CierreBanco> listCierreBanco) {
		this.listCierreBanco = listCierreBanco;
	}


	public List<TranAfip> getListTranAfip() {
		return listTranAfip;
	}


	public void setListTranAfip(List<TranAfip> listTranAfip) {
		this.listTranAfip = listTranAfip;
	}

	public List<Conciliacion> getListConciliacion() {
		return listConciliacion;
	}

	public void setListConciliacion(List<Conciliacion> listConciliacion) {
		this.listConciliacion = listConciliacion;
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
		
		// TODO: validate create -----> se refiere a validar si el envio ya se creo antes?????
		
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
				
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

	// Administracion de TranAfip
	public TranAfip createTranAfip(TranAfip tranAfip) throws Exception {
		
		// Validaciones de negocio
		if (!tranAfip.validateCreate()) {
			return tranAfip;
		}

		BalDAOFactory.getTranAfipDAO().update(tranAfip);
		
		return tranAfip;
	}	

	public TranAfip updateTranAfip(TranAfip tranAfip) throws Exception {
		
		// Validaciones de negocio
		if (!tranAfip.validateUpdate()) {
			return tranAfip;
		}

		BalDAOFactory.getTranAfipDAO().update(tranAfip);
		
		return tranAfip;
	}	

	public TranAfip deleteTranAfip(TranAfip tranAfip) throws Exception {
		
		// Validaciones de negocio
		if (!tranAfip.validateDelete()) {
			return tranAfip;
		}
				
		BalDAOFactory.getTranAfipDAO().delete(tranAfip);
		
		return tranAfip;
	}	

	// Administracion de CierreBanco
	public CierreBanco createCierreBanco(CierreBanco cierreBanco) throws Exception {
		
		// Validaciones de negocio
		if (!cierreBanco.validateCreate()) {
			return cierreBanco;
		}

		BalDAOFactory.getCierreBancoDAO().update(cierreBanco);
		
		return cierreBanco;
	}	

	public CierreBanco updateCierreBanco(CierreBanco cierreBanco) throws Exception {
		
		// Validaciones de negocio
		if (!cierreBanco.validateUpdate()) {
			return cierreBanco;
		}

		BalDAOFactory.getCierreBancoDAO().update(cierreBanco);
		
		return cierreBanco;
	}	

	public CierreBanco deleteCierreBanco(CierreBanco cierreBanco) throws Exception {
		
		// Validaciones de negocio
		if (!cierreBanco.validateDelete()) {
			return cierreBanco;
		}
				
		BalDAOFactory.getCierreBancoDAO().delete(cierreBanco);
		
		return cierreBanco;
	}	

	//	---> ABM EnvNovObl
	public EnvNotObl createEnvNovObl(EnvNotObl envNotObl) throws Exception {

		// Validaciones de negocio
		if (!envNotObl.validateCreate()) {
			return envNotObl;
		}

		BalDAOFactory.getEnvNovOblDAO().update(envNotObl);

		return envNotObl;
	}
	
	public EnvNotObl updateEnvNovObl(EnvNotObl envNotObl) throws Exception {
		
		// Validaciones de negocio
		if (!envNotObl.validateUpdate()) {
			return envNotObl;
		}

		BalDAOFactory.getEnvNovOblDAO().update(envNotObl);
		
		return envNotObl;
	}
	
	public EnvNotObl deleteEnvNovObl(EnvNotObl envNotObl) throws Exception {
	
		// Validaciones de negocio
		if (!envNotObl.validateDelete()) {
			return envNotObl;
		}
		
		BalDAOFactory.getEnvNovOblDAO().delete(envNotObl);
		
		return envNotObl;
	}
	
	/**
	 * Genera archivos de transacciones de pagos obtenidas del envio de osiris.  
	 *
	 */
	public void generarTransacciones() throws Exception{
				
		// Crear archivo de transacciones
		Archivo archivo = new Archivo();
		String prefix = "os";
		String fileName = prefix+DateUtil.formatDate(this.getFechaRegistroMulat(), DateUtil.yyMMdd_MASK)+"-"+StringUtil.formatLong(this.getIdEnvioAfip());
		archivo.setNombre(fileName);
		archivo.setPrefix(prefix);
		archivo.setFechaBanco(this.getFechaRegistroMulat());
		archivo.setNroBanco(0);
		TipoArc tipoArc = TipoArc.getById(TipoArc.ID_OSIRIS);
		archivo.setTipoArc(tipoArc);
		EstadoArc estadoArc = EstadoArc.getById(EstadoArc.ID_ACEPTADO);
		archivo.setEstadoArc(estadoArc);
		archivo.setCantTrans(0L);

		archivo = BalArchivosBancoManager.getInstance().createArchivo(archivo);

		// Crear transacciones del archivo
		Double total = 0D;
		long cantLineas = 0;
		for(TranAfip tranAfip: this.getListTranPago()){
			
			// Identificar si el pago corresponde a Drei o a Etur
			boolean esDrei = Recurso.COD_RECURSO_DReI.equals(tranAfip.getCodRecursoSegunFormulario());
			boolean esEtur = Recurso.COD_RECURSO_ETuR.equals(tranAfip.getCodRecursoSegunFormulario());
			
			// Obtengo la lista con detalles de pago totalizados (importe + impuesto)
			List<DetallePagoVO> listDetallePago = getListDetallePagoTotalizado(tranAfip);
			
			// Recorremos los detalles de pago de la transaccion afip. Puede tener mas de uno, correspondientes al pago de Drei y Etur
			for(DetallePagoVO detallePagoVO: listDetallePago){

				cantLineas++;
				if(cantLineas%1000==0){
					SiatHibernateUtil.currentSession().flush(); //  TODO Ver si lo dejamos
				}
				
				//si es regimen simplificado nos fijamos en detalle pago.
				if(tranAfip.getFormulario().intValue() == FormularioAfip.RS_6057.getId()) {		
					DetallePago detallePago = DetallePago.getById(detallePagoVO.getId());
					
					esDrei = Recurso.COD_RECURSO_DReI.equals(detallePago.getCodRecursoSegunImpuesto());
					esEtur = Recurso.COD_RECURSO_ETuR.equals(detallePago.getCodRecursoSegunImpuesto());
				}
				
				// Armamos linea de transaccion ficticia con los datos obtenidos
				//    SS     NNNNNNNNNN    CCCCCC  YYYYMMDD  IIIIIII.II  CCC    FFFF  IIIIIIIIIIII
				//  sistema+nroComprobante+clave+fechaPago+importePago+caja+formulario+idTranAfip
				String line = "";
				// Sistema
				if(esDrei)
					line += StringUtil.completarCerosIzq(ServicioBanco.COD_DREI, 2).substring(0, 2);
				else if(esEtur)
					line += StringUtil.completarCerosIzq(ServicioBanco.COD_ETUR, 2).substring(0, 2);
				else
					line +=00;
				
				// NroComprobante
				String numeroCuenta = detallePagoVO.getNumeroCuenta();
				//si numeroCuenta es null la seteo en vacio asi se completa su correspondiente posicion con 0.
				if(StringUtil.isNullOrEmpty(numeroCuenta)) numeroCuenta = "";
				line += StringUtil.completarCerosIzq(numeroCuenta, 10);
				
				// Clave
				if(tranAfip.getFormulario().intValue() == FormularioAfip.DREI_MULTAS_6053.getId()) {
					line += "000"+Transaccion.TIPO_BOLETA_DEUDA+"00";
				}else{
					line += StringUtil.completarCerosIzq(StringUtil.formatInteger(detallePagoVO.getAnio()), 4).substring(0, 4);
					line += StringUtil.completarCerosIzq(StringUtil.formatInteger(detallePagoVO.getPeriodo()), 2).substring(0, 2);					
				}
				// Fecha Pago
				line += DateUtil.formatDate(detallePagoVO.getFechaPago(), DateUtil.YYYYMMDD_MASK);
				// Importe Pago
				line += StringUtil.completarCerosIzq(StringUtil.formatDouble(detallePagoVO.getImportePago(), "#########0.00"), 10).substring(0, 10);
				// Caja
				line += StringUtil.completarCerosIzq(StringUtil.formatInteger(detallePagoVO.getCaja()), 3).substring(0, 3);
				// Cod. Formulario
				line += StringUtil.completarCerosIzq(StringUtil.formatInteger(tranAfip.getFormulario()), 4).substring(0, 4);
				// Id. TranAfip
				line += StringUtil.completarCerosIzq(StringUtil.formatLong(tranAfip.getId()), 12).substring(0, 12);
				
				// Crear transaccion
				TranArc tranArc = new TranArc();
				
				tranArc.setArchivo(archivo);
				tranArc.setLinea(line);
				tranArc.setNroLinea(cantLineas);
				tranArc.setImporte(detallePagoVO.getImportePago());
				
				System.out.println("TranArc:" + tranArc.infoString());
				
				// total que se muestra en el archivo
				total += detallePagoVO.getImportePago(); 
				
				archivo.createTranArc(tranArc);
			}	
				
		}
		
		// Seteo la cantidad de Transacciones de Pago generadas, asi las puedo loguear
		this.getCountMap().put(3, (int) cantLineas);
			
		archivo.setCantTrans(cantLineas);
		archivo.setTotal(total);
		
		//Si la cantidad de lineas generadas es cero
		if (cantLineas == 0L) {
			//Elimino el archivo creado
			archivo = BalArchivosBancoManager.getInstance().deleteArchivo(archivo);
		}else {
			//Actualizo el archivo de pago
			archivo = BalArchivosBancoManager.getInstance().updateArchivo(archivo);
		}
		
	}
	
	/**
	 * Obtiene una lista con DetallePago del tipo Impuesto con Interes aplicado
	 * OBS: si detallePago tiene numeroCuenta = null o 0, lo trata como una cuenta 0
	 * 
	 */
	private List<DetallePagoVO> getListDetallePagoTotalizado(TranAfip tranAfip) throws Exception {
		
		List<DetallePagoVO> listDetallePagoVO = new ArrayList<DetallePagoVO>();
		
		// Identificar si el pago corresponde a Drei o a Etur
		boolean esDrei = Recurso.COD_RECURSO_DReI.equals(tranAfip.getCodRecursoSegunFormulario());
		boolean esEtur = Recurso.COD_RECURSO_ETuR.equals(tranAfip.getCodRecursoSegunFormulario());
		
		if (esDrei || esEtur) {
			
			for (DetallePago detallePago: tranAfip.getListDetallePago()) {
				
				int idImpuesto = 0;
				int idInteres = 0;
				
				//Si el detallepago esta anulado, no lo tengo en cuenta para el calculo
				if (detallePago.getEstDetPago().getId().longValue() == EstDetPago.ID_ANULADO) {
					continue;
				}
				
				//Si es regimen simplificado nos fijamos en detalle pago.
				if(tranAfip.getFormulario().intValue() == FormularioAfip.RS_6057.getId()) {		
					esDrei = Recurso.COD_RECURSO_DReI.equals(detallePago.getCodRecursoSegunImpuesto());
					esEtur = Recurso.COD_RECURSO_ETuR.equals(detallePago.getCodRecursoSegunImpuesto());
				}
				
				//El Recurso es DReI
				if (esDrei) {
					//Pago corresponde a una Multa	
					if (detallePago.getImpuesto().equals(ImpuestoAfip.DREI_MULTAS.getId())) {
						idImpuesto = ImpuestoAfip.DREI_MULTAS.getId();
						idInteres = ImpuestoAfip.DREI_INT_MULTAS.getId();
						/*
						 * "Solo Pago" y "DJ y Pago Web" comparten los mismos id's de 
						 * impuesto e interes, por lo tanto se los evalua en una misma condicion
						 */
					} else if (detallePago.getImpuesto().equals(ImpuestoAfip.DREI.getId())) {
						idImpuesto = ImpuestoAfip.DREI.getId();
						idInteres = ImpuestoAfip.DREI_INTERESES.getId();
					} else if (detallePago.getImpuesto().equals(ImpuestoAfip.RS_DREI.getId())) {
						idImpuesto = ImpuestoAfip.RS_DREI.getId();
						//Regimen Simplificado no posee Interes, idInteres queda en 0 
					}
				
				//El Recurso es ETuR
				} else {
					//Pago corresponde a una Multa	
					if (detallePago.getImpuesto().equals(ImpuestoAfip.ETUR_MULTAS.getId())) {
						idImpuesto = ImpuestoAfip.ETUR_MULTAS.getId();
						idInteres = ImpuestoAfip.ETUR_INT_MULTAS.getId();
						/*
						 * "Solo Pago" y "DJ y Pago Web" comparten los mismos id's de 
						 * impuesto e interes, por lo tanto se los evalua en una misma condicion
						 */
					} else if (detallePago.getImpuesto().equals(ImpuestoAfip.ETUR.getId())) {
						idImpuesto = ImpuestoAfip.ETUR.getId();
						idInteres = ImpuestoAfip.ETUR_INTERESES.getId();
					} else if (detallePago.getImpuesto().equals(ImpuestoAfip.RS_ETUR.getId())) {
						idImpuesto = ImpuestoAfip.RS_ETUR.getId();
						//Regimen Simplificado no posee Interes, idInteres queda en 0 
					}
				}

				if (idImpuesto != 0) {
					//El detallePago corresponde a un Impuesto
					Double importeTotalPago = detallePago.getImportePago();
					
					if (idInteres != 0) {
						//Debo recorrer la lista nuevamente para obtener impuesto+interes	
						for (DetallePago detallePagoAux : tranAfip.getListDetallePago()) {
							//Sumar Interes al Impuesto sii su numeroCuenta, anio y periodo (agrupadores) son iguales
							if (detallePagoAux.getImpuesto().equals(idInteres) &&
								detallePago.getAnio().equals(detallePagoAux.getAnio()) &&
								detallePago.getPeriodo().equals(detallePagoAux.getPeriodo()) &&
								detallePago.getNumeroCuenta().equals(detallePagoAux.getNumeroCuenta())) {  //TODO: que hacemos si cuenta = null??
														
								importeTotalPago += detallePagoAux.getImportePago();
							}
						}
					}
					
					// Si el importe de pago (impuesto capital + impuesto interes) es cero
					// . No agrego el detalle pago a la lista (no genero transacciones de pago en cero)
					if (importeTotalPago.doubleValue() == 0D) continue;
									
					//Creo un VO con los datos de detallePago
					DetallePagoVO detallePagoVO = (DetallePagoVO) detallePago.toVO(0,false);

					//Actualizo el importe
					detallePagoVO.setImportePago(importeTotalPago);
					listDetallePagoVO.add(detallePagoVO);
				}
			}
		}
		
		return listDetallePagoVO;
	}
	
	public void setCountMap(Map<Integer, Integer> countMap) {
		this.countMap = countMap;
	}


	public Map<Integer, Integer> getCountMap() {
		return countMap;
	}


	/**
	 * Procesa EnvioOsiris, generando Formulario DDJJ AFIP,Formulario DDJJ SIAT y determinando Deuda.
	 * HAY QUE SER CUIDADOSO AL USARLO: fuerza el manejo de sesiones y transacciones de Hibernate.
	 *	
	 * Se pasa como parametro una bandera que cambia dependiendo de donde se llame este metodo
	 *	  - TRUE: reproceso de envio con error, se lo llama desde el servicio.
	 *	  - FALSE: proceso de envio masivo, se lo llama desde AdP 
	 * 
	 * @param esReproceso
	 * @throws Exception
	 */
	public void procesarEnvio(boolean esReproceso) throws Exception {
		if (log.isDebugEnabled()) log.debug("procesarEnvio: enter");
		
		//Mapa de contadores del Envio
		//ForDecJur Generadas Correctamente
		this.getCountMap().put(1, 0);
		//ForDecJur Pendientes de Procesar
		this.getCountMap().put(2, 0);
		
		//Por cada envio
		int countTranAfipFinErr = 0;
		//TranAfip procesadas (usada para manejar el Session de Hibernate) 
		int countTranAfip = 0;
		
		List<TranAfip> listTranAfip;
		if (esReproceso) {
			// Se llamo procesarEnvio desde el servicio
			// obtengo lista de TranAfip procesadas con error
			listTranAfip  = this.getListTranAfipDJHasError();
			 
			if (listTranAfip.isEmpty()) return;
		}else {
			// Se llamo procesarEnviod desde un proceso masivo de AdP
			// obtengo lista de TranAfip pendientes de procesar
			listTranAfip  = this.getListTranDecJur();
		}
		
		Boolean envioOsirisHasError = false;
		
		int totalTranAfip = listTranAfip.size();
		// Por cada transaccion 
		for (TranAfip tranAfip : listTranAfip) {
			SiatHibernateUtil.currentSession().refresh(tranAfip);
			SiatHibernateUtil.currentSession().refresh(this);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			//Resultado del procesar transaccion
			Boolean procTranAfip = false; 
			//Identifica si ocurrio una excepcion
			Boolean	hasException = false;
			String logErr = "";
			
			try {
				//Proceso la TranAfip
				procTranAfip = BalEnvioOsirisManager.getInstance().procesarTranAfip(tranAfip,this);
				
				// Obtengo Advertencias/Errores
				if (procTranAfip && tranAfip.getListAdvertencias().size() > 0) {
					logErr += "\nAdvertencias detectadas al procesar: ";
					for (String adv : tranAfip.getListAdvertencias()) logErr += "\n - " + adv;
				}else if (tranAfip.hasError()) {
					logErr += "\nErrores detectados al procesar: ";
					for (DemodaStringMsg error : tranAfip.getListError()) logErr += "\n - "+error.key().substring(1);
					tranAfip.clearErrorMessages();
				}
			} catch (Exception e) {
				hasException = true;
				envioOsirisHasError = true;
				logErr += "\nError inesperado al intentar procesar: " + StringUtil.stackTrace(e);
				log.error("\nError inesperado al intentar procesar: ", e);
			}
			
			//Determino el resultado del Proceso
			if (procTranAfip) {
				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_PROCESADO_OK));
			}else{
				SiatHibernateUtil.currentSession().getTransaction().rollback();
				SiatHibernateUtil.currentSession().beginTransaction();
				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_PROCESADO_ERROR));
				countTranAfipFinErr++; //TranAfip que no generaron ForDecjur
			}
			
			tranAfip.setObservacion(logErr);
			this.updateTranAfip(tranAfip);
			
			
			SiatHibernateUtil.currentSession().getTransaction().commit();
			
			//Cada 20 TranAfip cierro la sesion para acelerar el proceso
			if ((countTranAfip++ % 20) == 0 || hasException) SiatHibernateUtil.closeSession();
			
		}
		
		SiatHibernateUtil.currentSession().refresh(this);
		SiatHibernateUtil.currentSession().beginTransaction();
		
		String observaciones = this.getObservacion();
			   observaciones +="\n"+DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
			   observaciones += "\n- Se procesaron "+ totalTranAfip + " transacciones AFIP. ";
		if (countTranAfipFinErr > 0) {
			envioOsirisHasError = true;
			observaciones+= countTranAfipFinErr +" no generaron ForDecJur."; 
		}
		observaciones += "\n- Se generaron "+ this.getCountMap().get(1)  + " Formularios DDJJ Afip. " ;
		if (this.getCountMap().get(2) > 0) {
			observaciones += this.getCountMap().get(1) - this.getCountMap().get(2) +  " se procesaron satisfactoriamente y ";
			observaciones += this.getCountMap().get(2) +  " quedaron sin procesar.";
		}else if(this.getCountMap().get(1) > 0) {
			observaciones += "Todos se procesaron satisfactoriamente.";
		}
		this.setObservacion(observaciones);
		this.setFechaProceso(new Date());
		//Si ocurrió algun tipo de Excepcion no manejada o no se pudo procesar alguna tranAfip
		if (envioOsirisHasError){ 
			//marco el envioOsiris con error
			this.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PROCESADO_ERROR));
		}
		
		BalEnvioOsirisManager.getInstance().updateEnvioOsiris(this);
		SiatHibernateUtil.currentSession().getTransaction().commit();
		
		SiatHibernateUtil.closeSession();
		
		if (log.isDebugEnabled()) log.debug("procesarEnvio: exit");
	}
	
	/**
	 * Controla la concistencia de un EnvioOsiris comparando el importe total de  
	 * transacciones sobre Mulator contra el importe total de detallepago generados.
	 * 
	 * @return Boolean
	 * @throws Exception 
	 */
	public Boolean getEsConcistente() throws Exception{

		Double totalMulator;
		Double totalDetalle;
		
		// importe total de transacciones sobre Mulator asociadas al EnvioOsiris
		totalMulator = BalDAOFactory.getMulatorJDBCDAO().getMontoTotalEnvio(this);

		// importe total de detallepago generados al obtener EnvioOsiris
		totalDetalle = BalDAOFactory.getDetallePagoDAO().getMontoTotalDetalleForEnvio(this);
		
		if (NumberUtil.isDoubleEqualToDouble(totalMulator, totalDetalle, 0.05)) {
			// si la diferencia se encuentra dentro de 0.05 de tolerancia
			// determino que el envío se obtuvo en forma consistente.
			return true;
		}else {
			// Mantis #6229 (nota:0015124):
			// . Si existe una diferencia mayor a 0.05 entre el total_monto_ingresado de la transaccion Mulator
			// y la sumatoria de importePago de los detallePago asociados a la TranAfip, para alguna transaccionAfip
			// determino que el envío se obtuvo de forma inconsistente.
			// . Si ninguna supera esta diferencia, determino que el envío se obtuvo consistente.
			
			Double tolerancia = 0.05;
			//Obtengo un mapa<idTransaccionAfip,montoTotal> desde Mulator con las transacciones asociadas al envio
			Map<Long, Double> mTransaccion = BalDAOFactory.getMulatorJDBCDAO().getMapTransaccionMonto(this);
			for (TranAfip tranAfip : getListTranAfip()) {
				
				// total_monto_ingresado de Transaccion Mulator
				Double totMontoMulator = mTransaccion.get(tranAfip.getIdTransaccionAfip());
				
				// Sumatoria de importePago de los DetallePago asociados a la TranAfip
				Double totMontoTranAfip =  BalDAOFactory.getDetallePagoDAO().getMontoTotalDetalleForTranAfip(tranAfip);
				
				// Determino si hay una diferencia significativa (0,05) entre los totales 
				boolean hasDiference = !NumberUtil.isDoubleEqualToDouble(totMontoTranAfip, totMontoMulator,tolerancia);
				
				// Si existe diferencia
				// . determino que el envío se obtuvo de forma inconcistente 
				if (hasDiference){
					CierreBanco cierreBanco = tranAfip.getCierreBanco();
					
					String observaciones = this.getObservacion();
						   observaciones +="\n"+DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
						   observaciones +="\n- Existe una diferencia mayor a "+tolerancia+" entre el importe total de Transacción";
						   observaciones +=" Mulator e importe total sobre detalles de Pago generados.";
						   observaciones +="\n\t- Transacción Mulator: "+tranAfip.getIdTransaccionAfip()+",  - Total: "+totMontoMulator;
						   observaciones +="\n\t- Transacción AFIP: "+tranAfip.getId()+",  - Total: "+totMontoTranAfip;
						   observaciones +="\n\t- Banco: "+cierreBanco.getBanco()+" - Número de Cierre : "+cierreBanco.getNroCierreBanco();
	    			
	    			this.setObservacion(observaciones);
					
					return false;
				}
			}
			
			//No existe diferencia significativa entre las transacciones
			return true;
		}
		
	}
}
