//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.Vigencia;

/**
 * Adapter del Cambio de Domicilio de Envio
 * 
 * @author tecso
 */
public class CambiarDomEnvioAdapter extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cambiarDomEnvioAdapterVO";

    private CuentaVO cuenta = new CuentaVO();
    private Boolean codGesPerRequerido = true;

    /////// Cuentas seleccionadas /////////
    private List<CuentaVO> listCuenta = new ArrayList<CuentaVO>(); //lista de cuentas a seleccionar
    private String[] listIdCuentasSeleccionadas; // solo para mantener lo valores

    ////// Nuevo Domicilio /////////
    private String localidadAuxiliar = "";  // nombre de la localidad a buscar
    private List<LocalidadVO> listLocalidad = new ArrayList<LocalidadVO>(); //lista de localidades a seleccionar
    private LocalidadVO localidadRosario = new LocalidadVO(); 
    private List<CalleVO> listCalle = new ArrayList<CalleVO>();
    private boolean rosarioSelec = true; // solo para mantener valores

    ///// Solicitante /////////
    private List<TipoDocumentoVO> listTipoDocumento = new ArrayList<TipoDocumentoVO>();
    private CamDomWebVO camDomWeb = new CamDomWebVO();
    
    // esta pila mantiene la informacion de los forwards a donde volver
    private Stack<String> stackFwdVolver = new Stack<String>();

    // Constructores
    public CambiarDomEnvioAdapter(){
    	super(PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO);
    }
    
    //  Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuentaVO) {
		this.cuenta = cuentaVO;
	}
	public List<CuentaVO> getListCuenta() {
		return listCuenta;
	}
	public void setListCuenta(List<CuentaVO> listCuenta) {
		this.listCuenta = listCuenta;
	}
	public String getLocalidadAuxiliar() {
		return localidadAuxiliar;
	}
	public void setLocalidadAuxiliar(String localidadAuxiliar) {
		this.localidadAuxiliar = localidadAuxiliar;
	}
	
	public void setRosarioSelec(boolean rosarioSelec) {
		this.rosarioSelec = rosarioSelec;
	}

	public boolean getRosarioSelec() {
		boolean rosarioSelec = false;
		
		if (this.getCamDomWeb().getDomNue().getLocalidad().getEsRosario()) {
			rosarioSelec = true;
		}
		
		return rosarioSelec;
	}

	public boolean getViewResultCalles() {
		boolean viewCalles = false;

		if (this.getListCalle().size() > 0) {
			viewCalles = true;
		}

		return viewCalles;
	}

	public boolean getViewResultLocalidades() {
		boolean viewLocalidades = false;

		if (this.getListLocalidad().size() > 0) {
			viewLocalidades = true;
		}

		return viewLocalidades;

	}

	public List<TipoDocumentoVO> getListTipoDocumento() {
		return listTipoDocumento;
	}
	public void setListTipoDocumento(List<TipoDocumentoVO> listTipoDocumento) {
		this.listTipoDocumento = listTipoDocumento;
	}

	public List<LocalidadVO> getListLocalidad() {
		return listLocalidad;
	}

	public void setListLocalidad(List<LocalidadVO> listLocalidad) {
		this.listLocalidad = listLocalidad;
	}

	public LocalidadVO getLocalidadRosario() {
		return localidadRosario;
	}

	public void setLocalidadRosario(LocalidadVO localidadRosario) {
		this.localidadRosario = localidadRosario;
	}

	public List<CalleVO> getListCalle() {
		return listCalle;
	}

	public void setListCalle(List<CalleVO> listCalle) {
		this.listCalle = listCalle;
	}
	
	public String[] getListIdCuentasSeleccionadas() {
		return listIdCuentasSeleccionadas;
	}

	public void setListIdCuentasSeleccionadas(String[] listIdCuentasSeleccionadas) {
		this.listIdCuentasSeleccionadas = listIdCuentasSeleccionadas;
	}

	public String getOtraLocalidadView(){
		LocalidadVO loc = this.getCamDomWeb().getDomNue().getLocalidad();
		if (loc.getEsRosario()){
			return "";
		}else{
			return this.getCamDomWeb().getDomNue().getLocalidad().getLocalidadPciaView();
		}
	}
	
	public CamDomWebVO getCamDomWeb() {
		return camDomWeb;
	}

	public void setCamDomWeb(CamDomWebVO camDomWeb) {
		this.camDomWeb = camDomWeb;
	}

	public void marcarCuentasSeleccionadas(String[] listIdCuentasSeleccionadas) {

		this.limpiarMarcasCuentas();
		this.setListIdCuentasSeleccionadas(listIdCuentasSeleccionadas);

		for (String idCtasSeleccionada : listIdCuentasSeleccionadas) {
			this.marcarCuenta(new Long(idCtasSeleccionada));
		}
	}

	private void marcarCuenta(Long id){
		for (CuentaVO c : this.getListCuenta()) {
			if (id != null && id.equals(c.getId())){ // id != null por las dudas
				c.setMarcada(true);
				return;
			}
		}
	}

	public List<CuentaVO> getListCuentasMarcadas() {
		List<CuentaVO> listCuentasMarcadas = new ArrayList<CuentaVO>();
		for (CuentaVO c : this.getListCuenta()) {
			if (c.getMarcada()){
				listCuentasMarcadas.add(c);
			}
		}
		return listCuentasMarcadas;
	}

	public void limpiarMarcasCuentas() {
		this.setListIdCuentasSeleccionadas(null);
		for (CuentaVO c : this.getListCuenta()) {
			if (c.getMarcada()){
				c.setMarcada(false);
			}
		}
	}
	
	//////// localidad ///////////
	/** Recupera la localidad, de la lista de localidades cargada
	 *  que posea le id pasado como parametro.
	 * 
	 * @return
	 */
	public LocalidadVO getLocalidadById(Long idLocalidad) {
		LocalidadVO localidad = null;

		for (LocalidadVO localidadIt : this.getListLocalidad()) {
			if (localidadIt.getId().equals(idLocalidad)) {
				localidad = localidadIt;
				break;
			}
		}
		return localidad;
	}
	
	/** Setea los valores necesarios cuando se
	 *  selecciona la opcion rosario
	 * 
	 */
	public void seleccionarRosario() {

		this.getCamDomWeb().getDomNue().setLocalidad(this.getLocalidadRosario());

		// limpio la lista de localidades
		this.getListLocalidad().clear();
		// limpio el filtro de localidad
		this.setLocalidadAuxiliar("");

		// limpio el campo de cod postal para fuera de rosario
		this.getCamDomWeb().getDomNue().setCodPostalFueraRosario("");
	}
	
	/** Setea la localidad con el id pasado como parametro
	 * 
	 */
	public void setLocalidadById(Long idLocalidad) {
		
		LocalidadVO localidadSeleccionada = this.getLocalidadById(idLocalidad);
		
		this.getCamDomWeb().getDomNue().setLocalidad(localidadSeleccionada);

		// limpio la lista de localidades
		this.getListLocalidad().clear();
		// limpio el filtro de localidad
		this.setLocalidadAuxiliar("");
	}

	/** Setea los valores necesarios cuando se
	 *  selecciona la opcion otra localidad
	 * 
	 */
	public void seleccionarOtraLocalidad() {

		// si la localidad seleccionada es rosario la limpio
		if (this.getCamDomWeb().getDomNue().getLocalidad().getEsRosario()) {
			this.getCamDomWeb().getDomNue().setLocalidad(new LocalidadVO());				
		}

		// limpio la lista de localidades
		this.getListLocalidad().clear();
		
	}
	
	////// validaciones ///////
	
	public boolean validateSeleccionarCuentas() throws Exception {

		this.clearError();		
		// valido que alla una localidad seleccionada
		if (this.getListCuentasMarcadas().size() == 0) {
			this.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_SIN_CUENTAS_SELEC);			
		}

		// valido que se hallas seleccionado cuentas activas
		if (this.hasCuentasInactivas()) {
			this.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CUENTAS_SELECCIONADAS_INACTIVAS);			
		}

		if (hasError()) {
			return false;
		}

		return true;
		
	}
	
	private boolean hasCuentasInactivas() {
		boolean hasCuentasInactivas = false;

		for(CuentaVO cuentaVO:this.getListCuentasMarcadas()) {
			if (!cuentaVO.getVigencia().equals(Vigencia.VIGENTE) ) {
				hasCuentasInactivas = true;
			}
			
		}

		return hasCuentasInactivas;
	}
	
	
	/** Valida el ingreso del nuevo domicilio
	 * 
	 */
	public boolean validateNuevoDom() throws Exception {
		
		// valido que alla una localidad seleccionada
		if (ModelUtil.isNullOrEmpty(this.getCamDomWeb().getDomNue().getLocalidad()) ) {
			addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_LOCALIDAD_REQUERIDA);			
		}
		
		// valido que alla un nombre de calle
		if (StringUtil.isNullOrEmpty(this.getCamDomWeb().getDomNue().getCalle().getNombreCalle()) ) {
			addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CALLE_REQUERIDA);			
		}

		// valido que alla una Altura
		Long numero = this.getCamDomWeb().getDomNue().getNumero();
		if (numero == null || numero < 1  ) {
			addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_ALTURA_REQUERIDA);			
		}

		// valido el bis
		SiNo bis = this.getCamDomWeb().getDomNue().getBis(); 
		if ( bis == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.DOMICILIO_BIS);			
		}

		if (hasError()) {
			return false;
		}

		return true;

	}
	
	/** Valida el ingreso de los datos del solicitante
	 * 
	 */
	public boolean validateSolicitante() throws Exception {

		this.clearError();
		// valido que alla ingresado el nombre
		if (StringUtil.isNullOrEmpty(this.getCamDomWeb().getNomSolicitante()) ) {
			addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_NOMBRE_REQUERIDO);			
		}

		// valido que alla ingresado el apellido
		if (StringUtil.isNullOrEmpty(this.getCamDomWeb().getApeSolicitante()) ) {
			addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_APELLIDO_REQUERIDO);			
		}

		// valido el tipo de documento
		if (ModelUtil.isNullOrEmpty(this.getCamDomWeb().getDocumento().getTipoDocumento()) ) {
			addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_TIPODOC_REQUERIDO);			
		}

		// valido que el nro de documento
		if (StringUtil.isNullOrEmpty(this.getCamDomWeb().getDocumento().getNumeroView()) ) {
			addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_NUMERODOC_REQUERIDO);			
		}

		if (hasError()) {
			return false;
		}

		return true;

	}
	
	///////// Volver ////////////
	public void setForwardVolverWizard(String forwardVolverWizard) {
		this.stackFwdVolver.push(forwardVolverWizard);		
	}

	/** Devuelve el forward a donde volver 
	 *  y saca el ultimo elemento del stack
	 * 
	 * @return
	 */
	public String obtainForwardVolverWizard() {
		return this.stackFwdVolver.pop();		
	}

	public Boolean getCodGesPerRequerido() {
		return codGesPerRequerido;
	}

	public void setCodGesPerRequerido(Boolean codGesPerRequerido) {
		this.codGesPerRequerido = codGesPerRequerido;
	}

}
