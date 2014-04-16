//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms 
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Atributo
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_atributo")
public class Atributo extends BaseBO {
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	public static final String COD_NROCUENTA = "NroCuenta";

	public static final String COD_CATASTRAL = "Catastral";

	public static final String COD_NROCOMERCIO = "NroComercio";

	public static final String COD_FECHA_INICIO = "FechaInicio";

	public static final String COD_RUBRO = "Rubro";

	public static final String COD_LOCALES_EN_OTRAS_PROV = "LocalesEnOtrasProv";

	public static final String COD_LOCFUEROSENSFE = "LocFueRosEnSFe";

	public static final String COD_UBICACIONES = "Ubicaciones";
	
	public static final String COD_UBICACION = "Ubicacion";

	public static final String COD_CER = "CER";

	public static final String COD_RAD_RED_TRIB = "RadRefTrib";
	
	public static final String COD_CANT_PERSONAL_SIAT = "CantPersonalSiat";

	public static final String COD_NROPERMISO = "NroPermiso";

	public static final String COD_ACTDESARROLLADA = "ActDesarrollada";

	public static final String COD_LOCALESCIUDAD = "LocalesCiudad";

	public static final String COD_PUBLICIDADROD = "PublicidadRod";

	public static final String COD_CARTELES = "Carteles";

	public static final String COD_COPIACONTRATO = "CopiaContrato";

	public static final String COD_TERCEROS = "Terceros";

	public static final Object COD_DOMICILIOENVIO = "DomicilioEnvio";
	
	public static final String COD_REGIMEN = "Regimen";
		
	public static final String COD_VALOREMISION = "ValorEmision";
	
	public static final String COD_TABLAATR = "TablaAtr";
	
	public static final String COD_HAB_EMI_PORC = "HabEmiPorc";
	
	public static final String COD_HAB_DESCRIPCION = "HabDesc";
	
	public static final String COD_HAB_NRO_ANIO = "HabNroAnio";
	
	public static final String COD_ATR_ASE_VAL_DREI="AseValDrei";
	
	public static final String COD_CUMUR = "CUMUR";
	
	public static final String COD_PERINIRS = "PerIniRS";
	
	@Column(name = "codAtributo")
	private String codAtributo;
	
	@Column(name = "desAtributo") 
	private String desAtributo;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="idTipoAtributo") 
	private TipoAtributo tipoAtributo;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idDomAtr")
	private DomAtr domAtr;
	
	@Column(name = "mascaraVisual")
	private String mascaraVisual;

	// Constructores
	public Atributo(){
		super();
	}
	
	// Getters y setters
	public String getCodAtributo() {
		return codAtributo;
	}
	public void setCodAtributo(String codAtributo) {
		this.codAtributo = codAtributo;
	}
	public String getDesAtributo() {
		return desAtributo;
	}
	public void setDesAtributo(String desAtributo) {
		this.desAtributo = desAtributo;
	}
	public DomAtr getDomAtr() {
		return domAtr;
	}
	public void setDomAtr(DomAtr domAtr) {
		this.domAtr = domAtr;
	}
	public TipoAtributo getTipoAtributo() {
		return tipoAtributo;
	}
	public void setTipoAtributo(TipoAtributo tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}
	public String getMascaraVisual() {
		return mascaraVisual;
	}
	public void setMascaraVisual(String mascaraVisual) {
		this.mascaraVisual = mascaraVisual;
	}

	// Metodos de clase
	public static Atributo getById(Long id) {
		return (Atributo) DefDAOFactory.getAtributoDAO().getById(id);
	}
	
	public static Atributo getByCodigo(String codigo) throws Exception {
		return (Atributo) DefDAOFactory.getAtributoDAO().getByCodigo(codigo);
	}
	
	public static Atributo getByIdNull(Long id) {
		return (Atributo) DefDAOFactory.getAtributoDAO().getByIdNull(id);
	}
	
	public static List<Atributo> getListActivos() {
		return (ArrayList<Atributo>) DefDAOFactory.getAtributoDAO().getListActiva();
	}
	
	public static List<Atributo> getList(List<Long> listId) throws Exception {
		return (ArrayList<Atributo>) DefDAOFactory.getAtributoDAO().getList(listId);
	}

	/**
	 * Obtiene la lista de Atributos de Contribuyente marcados para la busqueda
	 * @return List<Atributo>
	 * @throws Exception
	 */
	public static List<Atributo> getListAtributoDeContribuyenteForBusquedaMasiva() throws Exception {
		return DefDAOFactory.getAtributoDAO().getListAtributoDeContribuyenteForBusquedaMasiva();
	}

	
	// Metodos de Instancia
	// Validaciones
	
	/**
	 * Valida la creacion
	 * @author Ivan
	 * @throws Exception 
	 */
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	/**
	 * Valida la actualizacion
	 * @author Ivan
	 * @throws Exception 
	 */
	public boolean validateUpdate() throws Exception {
		//	limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	/**
	 * Valida la eliminacion
	 * @author tecso
	 */
	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();
				
		if (GenericDAO.hasReference(this, RecAtrCue.class, "atributo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					DefError.ATRIBUTO_LABEL , DefError.RECATRCUE_LABEL);
		}
		if (GenericDAO.hasReference(this, ConAtr.class, "atributo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.CONATR_LABEL);
		}
		if (GenericDAO.hasReference(this, RecAtrCueEmi.class, "atributo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.RECATRCUEEMI_LABEL);
		}
		if (GenericDAO.hasReference(this, RecAtrVal.class, "atributo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.RECATRVAL_LABEL);
		}
		if (GenericDAO.hasReference(this, RecEmi.class, "atributoEmision")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.RECEMI_LABEL);
		}
		if (GenericDAO.hasReference(this, RecGenCueAtrVa.class, "atributo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.RECGENCUEATRVA_LABEL);
		}
		if (GenericDAO.hasReference(this, TipObjImpAtr.class, "atributo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.TIPOBJIMPATR_LABEL);
		}
		if (GenericDAO.hasReference(this, Recurso.class, "atributoAse")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.RECURSO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}

	// Metodos de negocio
	
	/**
	 * Activa el Atributo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getAtributoDAO().update(this);
	}

	/**
	 * Desactiva el Atributo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getAtributoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Atributo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Atributo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	
	private boolean validate() throws Exception {
		//	Validaciones de Requeridos
		if (StringUtil.isNullOrEmpty(getCodAtributo())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.ATRIBUTO_CODATRIBUTO);
		}
		if (StringUtil.isNullOrEmpty(getDesAtributo())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.ATRIBUTO_DESATRIBUTO);
		}
		if (getTipoAtributo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOATRIBUTO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codAtributo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.ATRIBUTO_CODATRIBUTO);			
		}

		if (hasError()) {
			return false;
		}

		return true;
		
	}
	
	/**
	 * Obtiene la definicion de un unico atributo sin valorizar.
	 * <p>Este metodo sirve para obtener un unico atributo a valorizar.
	 * @return el genericAtrDefinition cargado con este atributo (sin valor).
	 * @throws Exception 
	 */
	public GenericAtrDefinition getDefinition() throws Exception {
		AtributoVO atributo = (AtributoVO) this.toVO(2);		
		// Creo la definicion
		GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
		
		genericAtrDefinition.setAtributo(atributo);
		genericAtrDefinition.setPoseeVigencia(false);
		
		return genericAtrDefinition;
	}
	
	/**
	 * Recibe un valor para el atributo y lo formatea para el view. Para esto, si el atributo posee dominio, busca
	 * la descripcion que corresponda. Si no posee dominio o no encuentra el valor en la lista de valores del dominio
	 * devuelve el valor recibido.
	 * 
	 * @param valor
	 * @return string
	 */
	public String getValorForView(String valor){
		String valorView = "";
		boolean valorValido = false;
		if(this.getDomAtr() != null){
			DomAtr domAtr = this.getDomAtr();
			for (DomAtrVal item: domAtr.getListDomAtrVal()){
				if(valor.equals(item.getValor())){
					valorView = item.getDesValor(); 
					valorValido = true;
					break;
				}
			}
		}
		if(valorValido)
			return valorView;
		else
			return valor;
	}
	
}
