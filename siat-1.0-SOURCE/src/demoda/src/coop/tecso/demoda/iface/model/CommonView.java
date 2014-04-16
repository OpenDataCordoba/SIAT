//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;
                                                                         
/**
 * Aloja la propiedades del objeto base del model
 *
 * @author tecso
 * @version  2.0
 */
public class CommonView extends Common {
	
	static final long serialVersionUID = 0;

    private Long id;
    private String idView;

	// flags for enabling or desabling controls on the jsps
	public static String ENABLED = "enabled";
	public static String DISABLED = "disabled";
	
	/***** propiedades para el manejo de seguridad de banderas ***/
	// Acciones y Metodos para validar la seguridad. Se setean el el constructor de los SearchPage.
	protected String ACCION_AGREGAR 				= "";
	protected String ACCION_MODIFICAR				= "";
	protected String ACCION_ELIMINAR 				= "";
	protected String ACCION_VER     				= "";
	protected String ACCION_ACTIVAR    				= "";
	protected String ACCION_DESACTIVAR  			= "";
	protected String ACCION_ADMINISTRAR_PROCESO  	= "";
	protected String ACCION_MODIFICAR_ENCABEZADO  	= "";	

	@Deprecated
	protected String ACCION_MODIFICAR_ESTADO	= "";
	@Deprecated
	protected String ACCION_ALTA 				= "";
	@Deprecated
	protected String ACCION_BAJA 				= "";
	
	public static String METODO_AGREGAR 		    = "agregar";
	public static String METODO_MODIFICAR 		    = "modificar";
	public static String METODO_ELIMINAR 		    = "eliminar";
	public static String METODO_VER 			    = "ver";
	public static String METODO_ACTIVAR 		    = "activar";
	public static String METODO_DESACTIVAR		    = "desactivar";
	public static String METODO_ADMINISTRAR_PROCESO	= "administrarProceso";
	
	@Deprecated
	public static String METODO_ALTA 			  = "alta";
	@Deprecated
	public static String METODO_BAJA 			  = "baja";
	@Deprecated
	public static String METODO_MODIFICAR_ESTADO  = "modificarEstado";

    // business flags
    private Boolean agregarBussEnabled   		     = true; 
    private Boolean modificarBussEnabled 		     = true; 
    private Boolean eliminarBussEnabled  		     = true;
    private Boolean verBussEnabled                   = true;     
    private Boolean activarBussEnabled               = true;
    private Boolean desactivarBussEnabled            = true;
    private Boolean modificarEncabezadoBussEnabled   = true;
    private Boolean administrarProcesoBussEnabled    = true;
    
    @Deprecated
    private Boolean modificarEstadoBussEnabled 	= true;
    @Deprecated
    private Boolean bajaBussEnabled      		= true;
    @Deprecated
    private Boolean altaBussEnabled      		= true;
    
    private boolean modoSeleccionar = false; // Bandera para habilitar o deshabilitar controles en la vista.
    private boolean agregarEnSeleccion = false; // Bandera para habilitar o deshabilitar el agregar en modo seleccion    
    
	public CommonView()  {
        super();
    }
	
	public CommonView(String sweActionName)  {
        super();
    	ACCION_AGREGAR 			= sweActionName;
    	ACCION_MODIFICAR		= sweActionName;
    	ACCION_ELIMINAR 		= sweActionName;
    	ACCION_VER     			= sweActionName;
    	
    	ACCION_ACTIVAR     		= sweActionName;
    	ACCION_DESACTIVAR     	= sweActionName;
    	
    	ACCION_MODIFICAR_ESTADO	= sweActionName;
    	ACCION_ALTA 			= sweActionName;
    	ACCION_BAJA 			= sweActionName;
    }
	
	public Long getId() {
		return id;
	}

	/**
	 * @param id Fija el atributo id.
	 */
	public void setId(Long id) {
		this.id = id;
		this.idView = StringUtil.formatLong(id);
	}

	public String getIdView() {
		return idView;
	}

	public void setIdView(String idView) {
		this.idView = idView;
	}

	public Boolean getAgregarBussEnabled() {
		return agregarBussEnabled;
	}

	public void setAgregarBussEnabled(Boolean agregarBussEnabled) {
		this.agregarBussEnabled = agregarBussEnabled;
	}

	public Boolean getEliminarBussEnabled() {
		return eliminarBussEnabled;
	}

	public void setEliminarBussEnabled(Boolean eliminarBussEnabled) {
		this.eliminarBussEnabled = eliminarBussEnabled;
	}

	public Boolean getModificarBussEnabled() {
		return modificarBussEnabled;
	}

	public void setModificarBussEnabled(Boolean modificarBussEnabled) {
		this.modificarBussEnabled = modificarBussEnabled;
	}
	
	public Boolean getVerBussEnabled() {
		return verBussEnabled;
	}

	public void setVerBussEnabled(Boolean verBussEnabled) {
		this.verBussEnabled = verBussEnabled;
	}
	
	public boolean getModoSeleccionar() {
		return modoSeleccionar;
	}
	public void setModoSeleccionar(boolean modoSeleccionar) {
		this.modoSeleccionar = modoSeleccionar;
	}
	
	public Boolean getActivarBussEnabled() {
		return activarBussEnabled;
	}

	public void setActivarBussEnabled(Boolean activarBussEnabled) {
		this.activarBussEnabled = activarBussEnabled;
	}
	
	public Boolean getDesactivarBussEnabled() {
		return desactivarBussEnabled;
	}

	public void setDesactivarBussEnabled(Boolean desactivarBussEnabled) {
		this.desactivarBussEnabled = desactivarBussEnabled;
	}
	
	public Boolean getModificarEncabezadoBussEnabled() {
		return modificarEncabezadoBussEnabled;
	}

	public void setModificarEncabezadoBussEnabled(Boolean modificarEncabezadoBussEnabled) {
		this.modificarEncabezadoBussEnabled = modificarEncabezadoBussEnabled;
	}
	
	public boolean getAgregarEnSeleccion() {
		return agregarEnSeleccion;
	}

	public void setAgregarEnSeleccion(boolean agregarEnSeleccion) {
		this.agregarEnSeleccion = agregarEnSeleccion;
	}

	public Boolean getModificarEstadoBussEnabled() {
		return modificarEstadoBussEnabled;
	}
	
	public void setModificarEstadoBussEnabled(Boolean modificarEstadoBussEnabled) {
		this.modificarEstadoBussEnabled = modificarEstadoBussEnabled;
	}
	
	public Boolean getAltaBussEnabled() {
		return altaBussEnabled;
	}

	public void setAltaBussEnabled(Boolean altaBussEnabled) {
		this.altaBussEnabled = altaBussEnabled;
	}

	public Boolean getBajaBussEnabled() {
		return bajaBussEnabled;
	}

	public void setBajaBussEnabled(Boolean bajaBussEnabled) {
		this.bajaBussEnabled = bajaBussEnabled;
	}
	
}