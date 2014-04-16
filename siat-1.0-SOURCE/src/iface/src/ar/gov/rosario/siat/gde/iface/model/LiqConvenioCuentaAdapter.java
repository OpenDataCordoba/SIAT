//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Adapter del LiqConvenioCuenta
 * 
 * @author tecso
 */
public class LiqConvenioCuentaAdapter extends SiatAdapterModel{
	
	//private Log log = LogFactory.getLog(LiqConvenioCuentaAdapter.class);
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqConvenioCuentaAdapterVO";
	
	private LiqCuentaVO cuenta = new LiqCuentaVO();
    
	private LiqCuentaVO cuentaFilter = new LiqCuentaVO();
	
	private ProcedimientoVO procedimiento = new ProcedimientoVO(); 
	
	private LiqConvenioVO convenio = new LiqConvenioVO();
   
    private String[] listIdCuotaSelected;
    
    private boolean tieneCuotaSaldo = false;
    
    private boolean anulaConvenio=false;
    

    
    private Boolean mostrarChkAllCuotasPagas = false;  

    // ---> Propiedades para la asignacion de permisos
    private boolean verDeudaContribEnabled = false; // Poder ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaEnabled = false;		// Poder ver desgloses y unificaciones o cuentas relacionadas
    private boolean verConvenioEnabled = false; 	// Poder ver detalles de convenios
    private boolean verDetalleObjImpEnabled = false;
    private boolean verHistoricoContribEnabled = false;
    private boolean verCuentaDesgUnifEnabled = false;
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    private boolean verHistoricoConvenio=false;
    
    // Acciones sobre el convenio.
    private boolean imprimirRecibosVisible = false; 
    private boolean imprimirFormularioFormalVisible = false; 
    private boolean generarCuotaSaldoVisible = false; 
    private boolean generarSaldoPorCaducidadVisible = false; 
    private boolean atrasSaldoPorCaducidadVisible = false; 
    private boolean aplicarPagoACuentaVisible = false; 
    private boolean generarRescateVisible = false;
    private boolean anularConvenioVisible= false;
    private boolean reclamarAsentamientoVisible= false;
	
    private boolean imprimirRecibosEnabled = false; 
    private boolean imprimirFormularioFormalEnabled = false; 
    private boolean generarCuotaSaldoEnabled = false; 
    private boolean generarSaldoPorCaducidadEnabled = false; 
    private boolean atrasSaldoPorCaducidadEnabled = false; 
    private boolean aplicarPagoACuentaEnabled = false; 
    private boolean generarRescateEnabled = false;
    private boolean anularConvenioEnabled = false;
    private boolean verificarConsistenciaVisible=false;
    
	
    // ---> Propiedades para la asignacion de permisos
   
    
    // Constructores
    public LiqConvenioCuentaAdapter(){
    	super(GdeSecurityConstants.LIQ_CONVENIOCUENTA);
    }
    
    //  Getters y Setters
	public LiqConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(LiqConvenioVO convenio) {
		this.convenio = convenio;
	}

	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	
	public String[] getListIdCuotaSelected() {
		return listIdCuotaSelected;
	}
	public void setListIdCuotaSelected(String[] listIdCuotaSelected) {
		this.listIdCuotaSelected = listIdCuotaSelected;
	}

	public boolean isTieneCuotaSaldo() {
		return tieneCuotaSaldo;
	}
	public void setTieneCuotaSaldo(boolean tieneCuotaSaldo) {
		this.tieneCuotaSaldo = tieneCuotaSaldo;
	}

	public Boolean getMostrarChkAllCuotasPagas() {
		return mostrarChkAllCuotasPagas;
	}
	public void setMostrarChkAllCuotasPagas(Boolean mostrarChkAllCuotasPagas) {
		this.mostrarChkAllCuotasPagas = mostrarChkAllCuotasPagas;
	}
	
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	// Permisos
	public boolean isVerConvenioEnabled() {
		return verConvenioEnabled;
	}
	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}

	public boolean isVerCuentaEnabled() {
		return verCuentaEnabled;
	}
	public void setVerCuentaEnabled(boolean verCuentaEnabled) {
		this.verCuentaEnabled = verCuentaEnabled;
	}

	public boolean isVerDeudaContribEnabled() {
		return verDeudaContribEnabled;
	}
	public void setVerDeudaContribEnabled(boolean verDeudaContribEnabled) {
		this.verDeudaContribEnabled = verDeudaContribEnabled;
	}

	
	public boolean isAplicarPagoACuentaEnabled() {
		return aplicarPagoACuentaEnabled;
	}

	public void setAplicarPagoACuentaEnabled(boolean aplicarPagoACuentaEnabled) {
		this.aplicarPagoACuentaEnabled = aplicarPagoACuentaEnabled;
	}

	public boolean isAplicarPagoACuentaVisible() {
		return aplicarPagoACuentaVisible;
	}

	public void setAplicarPagoACuentaVisible(boolean aplicarPagoACuentaVisible) {
		this.aplicarPagoACuentaVisible = aplicarPagoACuentaVisible;
	}

	public boolean isAtrasSaldoPorCaducidadEnabled() {
		return atrasSaldoPorCaducidadEnabled;
	}

	public void setAtrasSaldoPorCaducidadEnabled(boolean atrasSaldoPorCaducidadEnabled) {
		this.atrasSaldoPorCaducidadEnabled = atrasSaldoPorCaducidadEnabled;
	}

	public boolean isAtrasSaldoPorCaducidadVisible() {
		return atrasSaldoPorCaducidadVisible;
	}

	public void setAtrasSaldoPorCaducidadVisible(boolean atrasSaldoPorCaducidadVisible) {
		this.atrasSaldoPorCaducidadVisible = atrasSaldoPorCaducidadVisible;
	}

	public boolean isGenerarCuotaSaldoEnabled() {
		return generarCuotaSaldoEnabled;
	}

	public void setGenerarCuotaSaldoEnabled(boolean generarCuotaSaldoEnabled) {
		this.generarCuotaSaldoEnabled = generarCuotaSaldoEnabled;
	}

	public boolean isGenerarCuotaSaldoVisible() {
		return generarCuotaSaldoVisible;
	}

	public void setGenerarCuotaSaldoVisible(boolean generarCuotaSaldoVisible) {
		this.generarCuotaSaldoVisible = generarCuotaSaldoVisible;
	}

	public boolean isGenerarSaldoPorCaducidadEnabled() {
		return generarSaldoPorCaducidadEnabled;
	}

	public void setGenerarSaldoPorCaducidadEnabled(boolean generarSaldoPorCaducidadEnabled) {
		this.generarSaldoPorCaducidadEnabled = generarSaldoPorCaducidadEnabled;
	}

	public boolean isGenerarSaldoPorCaducidadVisible() {
		return generarSaldoPorCaducidadVisible;
	}

	public void setGenerarSaldoPorCaducidadVisible(boolean generarSaldoPorCaducidadVisible) {
		this.generarSaldoPorCaducidadVisible = generarSaldoPorCaducidadVisible;
	}

	public boolean isImprimirFormularioFormalEnabled() {
		return imprimirFormularioFormalEnabled;
	}

	public void setImprimirFormularioFormalEnabled(	boolean imprimirFormularioFormalEnabled) {
		this.imprimirFormularioFormalEnabled = imprimirFormularioFormalEnabled;
	}

	public boolean isImprimirFormularioFormalVisible() {
		return imprimirFormularioFormalVisible;
	}

	public void setImprimirFormularioFormalVisible(boolean imprimirFormularioFormalVisible) {
		this.imprimirFormularioFormalVisible = imprimirFormularioFormalVisible;
	}

	public boolean isImprimirRecibosEnabled() {
		return imprimirRecibosEnabled;
	}

	public void setImprimirRecibosEnabled(boolean imprimirRecibosEnabled) {
		this.imprimirRecibosEnabled = imprimirRecibosEnabled;
	}

	public boolean isImprimirRecibosVisible() {
		return imprimirRecibosVisible;
	}

	public void setImprimirRecibosVisible(boolean imprimirRecibosVisible) {
		this.imprimirRecibosVisible = imprimirRecibosVisible;
	}

	public boolean isBuzonCambiosEnabled() {
		return buzonCambiosEnabled;
	}
	public void setBuzonCambiosEnabled(boolean buzonCambiosEnabled) {
		this.buzonCambiosEnabled = buzonCambiosEnabled;
	}
	
	public boolean isVerDetalleObjImpEnabled() {
		return verDetalleObjImpEnabled;
	}
	public void setVerDetalleObjImpEnabled(boolean verDetalleObjImpEnabled) {
		this.verDetalleObjImpEnabled = verDetalleObjImpEnabled;
	}

	public boolean isVerHistoricoContribEnabled() {
		return verHistoricoContribEnabled;
	}
	public void setVerHistoricoContribEnabled(boolean verHistoricoContribEnabled) {
		this.verHistoricoContribEnabled = verHistoricoContribEnabled;
	}

	public boolean isVerCuentaDesgUnifEnabled() {
		return verCuentaDesgUnifEnabled;
	}
	public void setVerCuentaDesgUnifEnabled(boolean verCuentaDesgUnifEnabled) {
		this.verCuentaDesgUnifEnabled = verCuentaDesgUnifEnabled;
	}
	

	public boolean isGenerarRescateVisible() {
		return generarRescateVisible;
	}

	public void setGenerarRescateVisible(boolean generarRescateVisible) {
		this.generarRescateVisible = generarRescateVisible;
	}
	

	public boolean isGenerarRescateEnabled() {
		return generarRescateEnabled;
	}

	public void setGenerarRescateEnabled(boolean generarRescateEnabled) {
		this.generarRescateEnabled = generarRescateEnabled;
	}
	

	public boolean isAnularConvenioEnabled() {
		return anularConvenioEnabled;
	}

	public void setAnularConvenioEnabled(boolean anularConvenioEnabled) {
		this.anularConvenioEnabled = anularConvenioEnabled;
	}

	public boolean isAnularConvenioVisible() {
		return anularConvenioVisible;
	}

	public void setAnularConvenioVisible(boolean anularConvenioVisible) {
		this.anularConvenioVisible = anularConvenioVisible;
	}
	
	public boolean isReclamarAsentamientoVisible() {
		return reclamarAsentamientoVisible;
	}

	public void setReclamarAsentamientoVisible(boolean reclamarAsentamientoVisible) {
		this.reclamarAsentamientoVisible = reclamarAsentamientoVisible;
	}

	public String getName(){
		return NAME;
	}
	
	
	public boolean isVerHistoricoConvenio() {
		return verHistoricoConvenio;
	}

	public void setVerHistoricoConvenio(boolean verHistoricoConvenio) {
		this.verHistoricoConvenio = verHistoricoConvenio;
	}

	public boolean isAnulaConvenio() {
		return anulaConvenio;
	}

	public void setAnulaConvenio(boolean anulaConvenio) {
		this.anulaConvenio = anulaConvenio;
	}

	public boolean isVerificarConsistenciaVisible() {
		return verificarConsistenciaVisible;
	}

	public void setVerificarConsistenciaVisible(boolean verificarConsistenciaVisible) {
		this.verificarConsistenciaVisible = verificarConsistenciaVisible;
	}
	
	public LiqCuentaVO getCuentaFilter() {
		return cuentaFilter;
	}
	public void setCuentaFilter(LiqCuentaVO cuentaFilter) {
		this.cuentaFilter = cuentaFilter;
	}

	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Convenio");
		 report.setReportBeanName("Convenio");
		 report.setReportFileName(this.getClass().getName());
		 report.setPageHeight(29.7);
		 report.setPageWidth(21.0);
	}

	public ContenedorVO getContenedorForReport(){
		
		ContenedorVO contPrinc = new ContenedorVO("ContenedorVO");
		
		LiqCuentaVO liqCuentaVO = this.getCuenta();

		// Datos de la Cuenta
		ContenedorVO contCuenta = new ContenedorVO("Cuenta");
		contCuenta.getTablaCabecera().setTitulo("Datos de la Cuenta");
		
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO(liqCuentaVO.getNroCuenta(),"","Nro. Cuenta")); //String valor, String nombreColumna, String etiqueta
		filaCabecera.add(new CeldaVO(liqCuentaVO.getDesRecurso(),"","Recurso")); 
		filaCabecera.add(new CeldaVO(liqCuentaVO.getCodGestionPersonal(),"","Cód. Gestión Personal"));
		// Atributos
		
		filaCabecera.add(new CeldaVO(this.getCuenta().getDesDomEnv(), "", "Domicilio Envío"));
		// el domicilio se saca del atributo Ubicacion, que se llena en LiqDeudaBeanHelper.getListAtrObjImp()
		
		List<LiqAtrValorVO> listLiqAtrValorVO = this.getCuenta().getListAtributoObjImp();
		for (LiqAtrValorVO liqAtrValorVO : listLiqAtrValorVO) {
			if (!liqAtrValorVO.getKey().equals("DomicilioEnvio") && !liqAtrValorVO.getKey().equals("DomicilioFinca"))
				filaCabecera.add(new CeldaVO(liqAtrValorVO.getValue(),"",liqAtrValorVO.getLabel())); 
		}
		
		// Titulares
		List<LiqTitularVO> listTitularVO = liqCuentaVO.getListTitular();
		for (LiqTitularVO liqTitularVO : listTitularVO) {
			filaCabecera.add(new CeldaVO(liqTitularVO.getDesTitular(),"","Titular/es"));
		}
		// Atributos del Contribuyente
		List<LiqAtrValorVO> listAtributoContr = liqCuentaVO.getListAtributoContr();
		for (LiqAtrValorVO liqAtrValorVO : listAtributoContr) {
			// TODO ubicar horizontalmente
			filaCabecera.add(new CeldaVO(liqAtrValorVO.getValue(),"",liqAtrValorVO.getLabel()));
		}
		// Cuentas Unificadas
		List<LiqCuentaVO> listLiqCuentaVO = liqCuentaVO.getListCuentaUnifDes();
		for (LiqCuentaVO liqCuentaUnif : listLiqCuentaVO) {
			
			filaCabecera.add(new CeldaVO(liqCuentaUnif.getNroCuenta(),"","Cuenta/s Unificadas"));
		}
		
		contCuenta.getTablaCabecera().setFilaCabecera(filaCabecera);
		contPrinc.getListBloque().add(contCuenta);
		
		// Datos del Convenio
		ContenedorVO contConvenio = new ContenedorVO("Convenio");
		contConvenio.getTablaCabecera().setTitulo("Datos del Convenio");
		
		LiqConvenioVO liqConvenio = this.getConvenio();

		filaCabecera = new FilaVO();
		
		// Nro convenio
		filaCabecera.add(new CeldaVO(liqConvenio.getNroConvenio(),"","Nro. Convenio"));
		// Plan pago
		filaCabecera.add(new CeldaVO(liqConvenio.getDesPlan(),"","Plan de Pago"));
		// caso
		if(liqConvenio.isPoseeCaso()){
			String caso = liqConvenio.getCaso().getSistemaOrigen().getDesSistemaOrigen() + " " + 
			liqConvenio.getCaso().getNumero();
			filaCabecera.add(new CeldaVO(caso,"","Caso"));
		}
		// viaDeuda
		filaCabecera.add(new CeldaVO(liqConvenio.getDesViaDeuda(),"","Vía Deuda"));
		// procurador
		if(liqConvenio.getPoseeProcurador()){
			filaCabecera.add(new CeldaVO(liqConvenio.getProcurador().getIdView()+" - "+
										liqConvenio.getProcurador().getDescripcion(),"","Procurador"));
		}
		// Estado Convenio
		filaCabecera.add(new CeldaVO(liqConvenio.getDesEstadoConvenio(),"","Estado Convenio"));
		// Cantidad Cuotas
		filaCabecera.add(new CeldaVO(liqConvenio.getCanCuotasPlan(),"","Cant. de Cuotas"));
		// Total Convenio
		filaCabecera.add(new CeldaVO(liqConvenio.getTotImporteConvenioView(),"","Importe Total"));
		
		contConvenio.getTablaCabecera().setFilaCabecera(filaCabecera);
		contPrinc.getListBloque().add(contConvenio);
		
		// Datos de la Formalizacion:
		ContenedorVO contFormalizacion = new ContenedorVO("Formalización");
		contFormalizacion.getTablaCabecera().setTitulo("Datos de la Formalización");
		
		filaCabecera = new FilaVO();
		
		// Fecha de Formalizacion
		filaCabecera.add(new CeldaVO(liqConvenio.getFechaFor(),"","Fecha de Formalización"));
		
		if(liqConvenio.getPoseeDatosPersona()){
			//<!-- Apellido -->
			filaCabecera.add(new CeldaVO(liqConvenio.getPersona().getApellido(),"","Apellido"));
			//<!-- Nombres -->
			filaCabecera.add(new CeldaVO(liqConvenio.getPersona().getNombres(),"","Nombres"));
			//<!-- Apellido Materno -->
			filaCabecera.add(new CeldaVO(liqConvenio.getPersona().getApellidoMaterno(),"","Apellido Materno"));
			//<!-- Tipo y Nro Doc -->
			filaCabecera.add(new CeldaVO(liqConvenio.getPersona().getDocumento().getNumeroView(),"","Tpo. y Nro. Doc."));
		}
		/*else{
			filaCabecera.add(new CeldaVO("","","No existen datos sobre la persona que formalizó el convenio"));
		}*/
		
		//<!-- Tipo Per For --> 
		filaCabecera.add(new CeldaVO(liqConvenio.getTipoPerFor().getDesTipoPerFor(),"","En Caracter de"));
		// <!-- Domicilio -->
		filaCabecera.add(new CeldaVO(liqConvenio.getPersona().getDomicilio().getView(),"","Domicilio particular"));
		//<!-- Telefono -->
		filaCabecera.add(new CeldaVO(liqConvenio.getPersona().getTelefono(),"","Teléfono"));
		// <!-- Tipo Doc Aportada --> Documentaciï¿½n aportada:
		filaCabecera.add(new CeldaVO(liqConvenio.getTipoDocApo().getDesTipoDocApo(),"","Documentación aportada"));
		// <!-- Obs -->
		filaCabecera.add(new CeldaVO(liqConvenio.getObservacionFor(),"","Observación"));
		// <!-- Agente Interviniente -->
		filaCabecera.add(new CeldaVO(liqConvenio.getUsusarioFor(),"","Agente Interviniente"));

		contFormalizacion.getTablaCabecera().setFilaCabecera(filaCabecera);
		contPrinc.getListBloque().add(contFormalizacion);
		
		TablaVO tablaPeriodosIncluidos = new TablaVO("Períodos Incluidos");
		tablaPeriodosIncluidos.setTitulo("PERIODOS INCLUIDOS");
		FilaVO filaCabeceraT = new FilaVO();
		// Deuda  	Fecha Vto.  	Importe  	Actualizaciï¿½n  	Total
		filaCabeceraT.add(new CeldaVO("Deuda"));
		filaCabeceraT.add(new CeldaVO("Fecha Vto."));
		filaCabeceraT.add(new CeldaVO("Importe"));
		filaCabeceraT.add(new CeldaVO("Actualización"));
		filaCabeceraT.add(new CeldaVO("Total"));
		tablaPeriodosIncluidos.setFilaCabecera(filaCabeceraT);
		

		//<!-- Periodos Incluidos -->
		List<LiqDeudaVO> listLiqPeriodo = liqConvenio.getListPeriodoIncluido();
		if(listLiqPeriodo.size() > 0){
			for (LiqDeudaVO liqDeudaVO : listLiqPeriodo) {
				FilaVO fila = new FilaVO();
				fila.add(new CeldaVO(liqDeudaVO.getPeriodoDeuda()));
				fila.add(new CeldaVO(liqDeudaVO.getFechaVto()));
				fila.add(new CeldaVO(liqDeudaVO.getSaldoView()));
				fila.add(new CeldaVO(liqDeudaVO.getActualizacionView()));
				fila.add(new CeldaVO(liqDeudaVO.getTotalView()));
				tablaPeriodosIncluidos.add(fila);
			}
			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO("" + liqConvenio.getTotalPeriodos()));
			tablaPeriodosIncluidos.addFilaPie(filaPie);
		}else{
			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO("No se Registran períodos incluidos"));
			tablaPeriodosIncluidos.add(fila);
		}
		contPrinc.getListTabla().add(tablaPeriodosIncluidos);
		
		// <!-- Cuotas Pagas-->
		TablaVO tablaCuotasPagas = new TablaVO("Cuotas Pagas");
		tablaCuotasPagas.setTitulo("CUOTAS PAGAS");
		filaCabeceraT = new FilaVO();
		// Cuota  	Fecha Vto.  	Capital  	Interes  	Actualizaciï¿½n  	Total  	Fec. Pago  	Estado
		filaCabeceraT.add(new CeldaVO("Cuota"));
		filaCabeceraT.add(new CeldaVO("Fecha Vto."));
		filaCabeceraT.add(new CeldaVO("Capital"));
		filaCabeceraT.add(new CeldaVO("Interés"));
		filaCabeceraT.add(new CeldaVO("Actualización"));
		filaCabeceraT.add(new CeldaVO("Total"));
		filaCabeceraT.add(new CeldaVO("Fec. Pago"));
		filaCabeceraT.add(new CeldaVO("Estado"));
		tablaCuotasPagas.setFilaCabecera(filaCabeceraT);

		List<LiqCuotaVO> listLiqCuotaPagas = liqConvenio.getListCuotaPaga();
		if(listLiqCuotaPagas.size() > 0){
			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO("" + liqConvenio.getTotalCuotasPagas()));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			tablaCuotasPagas.addFilaPie(filaPie);
			
			for (LiqCuotaVO liqCuotaVO : listLiqCuotaPagas) {
				FilaVO fila = new FilaVO();
				fila.add(new CeldaVO(liqCuotaVO.getNroCuota()));
				fila.add(new CeldaVO(liqCuotaVO.getFechaVto()));
				fila.add(new CeldaVO(liqCuotaVO.getCapitalView()));
				fila.add(new CeldaVO(liqCuotaVO.getInteresView()));
				fila.add(new CeldaVO(liqCuotaVO.getActualizacionView()));
				fila.add(new CeldaVO(liqCuotaVO.getTotalView()));
				fila.add(new CeldaVO(liqCuotaVO.getFechaPago()));
				fila.add(new CeldaVO(liqCuotaVO.getDesEstado()));
				tablaCuotasPagas.add(fila);
			}
		}else{
			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO("No se Registran cuotas pagas"));
			tablaCuotasPagas.add(fila);
		}
		
		contPrinc.getListTabla().add(tablaCuotasPagas);

		// <!-- Cuotas Impagas-->
		TablaVO tablaCuotasImPagas = new TablaVO("Cuotas Impagas");
		tablaCuotasImPagas.setTitulo("CUOTAS IMPAGAS");
		filaCabeceraT = new FilaVO();
		// Cuota  	Fecha Vto.  	Capital  	Interes  	Actualizaciï¿½n  	Total
		filaCabeceraT.add(new CeldaVO("Cuota"));
		filaCabeceraT.add(new CeldaVO("Fecha Vto."));
		filaCabeceraT.add(new CeldaVO("Capital"));
		filaCabeceraT.add(new CeldaVO("Interés"));
		filaCabeceraT.add(new CeldaVO("Actualización"));
		filaCabeceraT.add(new CeldaVO("Total"));
		filaCabeceraT.add(new CeldaVO("Observación"));
		tablaCuotasImPagas.setFilaCabecera(filaCabeceraT);

		List<LiqCuotaVO> listCuotasImpagas = liqConvenio.getListCuotaInpaga();
		if(listCuotasImpagas.size() > 0){
			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO("" + liqConvenio.getTotalCuotasImpagas())); 
			filaPie.add(new CeldaVO(""));
			tablaCuotasImPagas.addFilaPie(filaPie);
			for (LiqCuotaVO liqCuotaVO : listCuotasImpagas) {
				FilaVO fila = new FilaVO();
				fila.add(new CeldaVO(liqCuotaVO.getNroCuota()));
				fila.add(new CeldaVO(liqCuotaVO.getFechaVto()));
				fila.add(new CeldaVO(liqCuotaVO.getCapitalView()));
				fila.add(new CeldaVO(liqCuotaVO.getInteresView()));
				fila.add(new CeldaVO(liqCuotaVO.getActualizacionView()));
				fila.add(new CeldaVO(liqCuotaVO.getTotalView()));
				fila.add(new CeldaVO(liqCuotaVO.getObservacion()));
				tablaCuotasImPagas.add(fila);
			}
		}else{
			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO("No se Registran cuotas impagas"));
			tablaCuotasImPagas.add(fila);
		}
		contPrinc.getListTabla().add(tablaCuotasImPagas);

		TablaVO tablaCuotasImPutadas = new TablaVO("Cuotas Imputadas");
		tablaCuotasImPutadas.setTitulo("DETALLE DE IMPUTACIONES DE CUOTAS");
		filaCabeceraT = new FilaVO();
		// Cuota  	Cuota imputada  	Fec. Pago  	Capital Cuota / Importe Pago  	Saldo Cubierto  	Deuda  	Total  	Saldo / Imputaciï¿½n
		filaCabeceraT.add(new CeldaVO("Cuota"));
		filaCabeceraT.add(new CeldaVO("Cuota Imputada"));
		filaCabeceraT.add(new CeldaVO("Fec. Pago"));
		filaCabeceraT.add(new CeldaVO("Capital Cuota / Importe Pago"));
		filaCabeceraT.add(new CeldaVO("Saldo Cubierto"));
		filaCabeceraT.add(new CeldaVO("Deuda"));
		filaCabeceraT.add(new CeldaVO("Total"));
		filaCabeceraT.add(new CeldaVO("Saldo / Imputación"));
		tablaCuotasImPutadas.setFilaCabecera(filaCabeceraT);

		List<CuotaDeudaVO> listCuotaDeudaImputada = liqConvenio.getListCuotaDeudaImputadas();
		if(listCuotaDeudaImputada.size() > 0){
			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO("")); 
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			tablaCuotasImPutadas.addFilaPie(filaPie);
			for (CuotaDeudaVO cuotaDeudaVO : listCuotaDeudaImputada) {
				FilaVO fila = new FilaVO();
				fila.add(new CeldaVO(cuotaDeudaVO.getNroCuota()));
				fila.add(new CeldaVO(cuotaDeudaVO.getNroCuotaImputada()));
				fila.add(new CeldaVO(cuotaDeudaVO.getFechaPago()));
				fila.add(new CeldaVO(cuotaDeudaVO.getCapitalView()));
				fila.add(new CeldaVO(cuotaDeudaVO.getSaldoEnPlanCubView()));
				fila.add(new CeldaVO(cuotaDeudaVO.getPeriodoDeudaView()));
				fila.add(new CeldaVO(cuotaDeudaVO.getTotalView()));
				fila.add(new CeldaVO(cuotaDeudaVO.getSaldoView()));
				tablaCuotasImPutadas.add(fila);
			}
		}else{
			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO("No se Registran cuotas imputadas"));
			tablaCuotasImPutadas.add(fila);
		}
		contPrinc.getListTabla().add(tablaCuotasImPutadas);
		
		// Filtros
		/*
		TablaVO tablaFiltros  = new TablaVO("filtros");
		tablaFiltros.setTitulo("F");
		FilaVO  filaDeFiltros = new FilaVO();
		for (String claveFiltro : report.getReportFiltros().keySet()) {
			String valor = report.getReportFiltros().get(claveFiltro);
			filaDeFiltros.add(new CeldaVO(valor, claveFiltro, claveFiltro));
		}
		tablaFiltros.add(filaDeFiltros);
		
		contPrinc.setTablaFiltros(tablaFiltros);
		*/
		
		return contPrinc;
	}
	
}