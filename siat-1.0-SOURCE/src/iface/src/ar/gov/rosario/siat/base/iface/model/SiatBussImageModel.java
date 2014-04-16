//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.model;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.util.SiatCache;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.model.SistemaOrigenVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.UserContext;

public class SiatBussImageModel extends BussImageModel implements ISiatModel {
	
	static private Log log = LogFactory.getLog(SiatBussImageModel.class);

	/**
	 * Constructor
	 *
	 */
	public SiatBussImageModel() {    
		super();
	}

    /**
     * Constructor parametrizado
     * @param commonKey
     */
    public SiatBussImageModel(CommonKey commonKey) {
        super();
        super.setId(commonKey.getId());
    }
    
	/**
	 * Constructor parametrizado
	 * @param id
	 */
	public SiatBussImageModel(Long id) {
		super();
		super.setId(id);
	}

	/**
	 * Constructor parametrizado
	 * @param id
	 */
	public SiatBussImageModel(long id) {
		super();
		super.setId(Long.valueOf(id));
	}
	
	/**
	 * Constructor parametrizado
	 * @param id
	 */
	public SiatBussImageModel(String id) {
		super.setId(Long.valueOf(id));
	}

	/**
	 * Verifica si una bandera esta habilidata o no chequeando al mismo tiempo los 
	 * permisos segun la accion y metodo que dependen de esa bandera.
	 * Este metodo es un helper para verficar si una bandera esta habilitada o no segun
	 * la manera mas comunmete hecha en Demoda.
	 * Si necesita una verificacion mas compleja puede programar el checkeo uds. mismo en cada VO.
	 * Llamando a SweContext.hasAccess() de SWE.
	 * <p>Requiere:
	 * <br> - Que el UserContext este en el TLS. Ver: DemodaUtil.setCurrentUserContext()
	 * <br> - Que el UserContext tenga seteado los IdsAccionesModuloUsuario, obtenidos durante del login en SWE.
	*/
	static public String hasEnabledFlag(boolean flag, String accion, String metodo) {
		long hasAccess= Common.HASACCESS_SINACCESO;

		try {
			if (!flag) {
				if (log.isDebugEnabled()) log.debug("hasEnabledFlag(): " + accion + " " + metodo + " DISABLED by bussiness flag");
				return DISABLED;
			}

			UserContext uc = DemodaUtil.currentUserContext();
			hasAccess = SiatCache.getInstance().getSweContext().hasAccess(uc.getIdsAccionesModuloUsuario(), accion, metodo);

			if (hasAccess == Common.HASACCESS_OK || hasAccess == Common.HASACCESS_OK_NOEXISTE ) { 
				if (log.isDebugEnabled()) {
					log.debug("hasEnabledFlag(): " + accion + " " + metodo + (hasAccess == Common.HASACCESS_OK ? " OK_HASACCESS": " OK_NOEXISTE"));
				}
				return ENABLED;
			} else {  
				if (log.isDebugEnabled()) log.debug("hasEnabledFlag(): " + accion + " " + metodo + " DISABLED by swe");
				return DISABLED;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DISABLED;
		}	
	}

	/**
	 * Verifica si una bandera esta habilidata o no chequeando los 
	 * permisos segun la accion y metodo que dependen de esa bandera.
	 * Este metodo es un helper para verficar si una bandera esta habilitada o no segun
	 * la manera mas comunmete hecha en Demoda.
	 * Si necesita una verificacion mas compleja puede programar el checkeo uds. mismo en cada VO.
	 * Llamando a SweContext.hasAccess() de SWE.
	 * <p>Requiere:
	 * <br> - Que el UserContext este en el TLS. Ver: DemodaUtil.setCurrentUserContext()
	 * <br> - Que el UserContext tenga seteado los IdsAccionesModuloUsuario, obtenidos durante del login en SWE.
	*/
	static public String hasEnabledFlag(String accion, String metodo) {
		long hasAccess= Common.HASACCESS_SINACCESO;

		try {

			UserContext uc = DemodaUtil.currentUserContext();
			hasAccess = SiatCache.getInstance().getSweContext().hasAccess
				(uc.getIdsAccionesModuloUsuario(), accion, metodo);

			if (hasAccess == Common.HASACCESS_OK || hasAccess == Common.HASACCESS_OK_NOEXISTE ) { 
				if (log.isDebugEnabled()) {
					log.debug("hasEnabledFlag(): " + accion + " " + metodo + (hasAccess == Common.HASACCESS_OK ? " OK_HASACCESS": " OK_NOEXISTE"));
				}
				return ENABLED;
			} else {  
				if (log.isDebugEnabled()) log.debug("hasEnabledFlag(): " + accion + " " + metodo + " DISABLED by swe");
				return DISABLED;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DISABLED;
		}	
	}
	
	// View flags NO! chequeamos acceso por SWE
	public String getModificarEnabled() {
		return this.getModificarBussEnabled() ? ENABLED : DISABLED;
	}
	
	public String getEliminarEnabled() {

		return this.getEliminarBussEnabled() ? ENABLED : DISABLED;
	}

	public String getAltaEnabled() {
		return this.getAltaBussEnabled() ? ENABLED : DISABLED;
	}

	public String getBajaEnabled() {
		return this.getBajaBussEnabled() ? ENABLED : DISABLED;
	}
	
	public String getVerEnabled() {
		return this.getVerBussEnabled() ? ENABLED : DISABLED;
	}
	
	public String getActivarEnabled() {
		return this.getActivarBussEnabled() ? ENABLED : DISABLED;
	}
	
	public String getDesactivarEnabled() {
		return this.getDesactivarBussEnabled() ? ENABLED : DISABLED;
	}
	
	/**
	 * Metodo utilizado para que la intefaz de Sercivio de Caso "ICasCasoService"
	 * pueda recibir un SiatBussImageModel que implemente el getCaso().
	 * 
	 * @author Cristian
	 * @return
	 */
	public CasoVO getCaso(){
		return null;
	}
	
	
	/**
	 *  Devuelve el idCaso formateado para guardar en DB
	 *  
	 *  NOTA: si se esta utilizando CasoVO directamentete, solicitarle getIdFormateado()
	 * 
	 * @author Cristian
	 * @return
	 */
	public String getIdCaso() {
		if (getCaso() != null)
			return CasServiceLocator.getCasCasoService().getIdFormateado(getCaso());
		else
			return null;
	}

	public void setIdCaso(String idCaso) {
		// idCaso = idSistemaOrigen + "-" + numero + "-"+ anio (para MEGE o NOTAS)
		// idCaso = idSistemaOrigen + "-" + numero (para OTROS)
		
		if (getCaso() != null && !StringUtil.isNullOrEmpty(idCaso)) {
			
			log.debug("setIdCaso: "+ idCaso + " en clase: " + this.getClass().getName());

			CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(idCaso);
			
			getCaso().setNumero(caso.getNumero());
			getCaso().setSistemaOrigen(caso.getSistemaOrigen());
					
		
		} else if (getCaso() != null && StringUtil.isNullOrEmpty(idCaso)) {			
			getCaso().setNumero("");
			getCaso().setSistemaOrigen(new SistemaOrigenVO());
		}
	}

	
	/**
	 * Devuelve una lista de String con los contenidos de los errores
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getListErrorValues(){
		
		ArrayList<String> listErrorValues = new ArrayList<String>();
		
		for (DemodaStringMsg dsm:this.getListError()){			
			if (dsm.key() !=null ){
				if( dsm.key().startsWith("&") ){
					listErrorValues.add( dsm.key().substring(1) );
				} else {
					listErrorValues.add(SiatUtil.getValueFromBundle(dsm.key()));
				}
			}
		}
		
		return listErrorValues;
	}
}
