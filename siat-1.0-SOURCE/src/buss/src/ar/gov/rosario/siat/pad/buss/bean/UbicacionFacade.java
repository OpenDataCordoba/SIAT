//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.pad.iface.model.CalleSearchPage;
import ar.gov.rosario.siat.pad.iface.model.DomicilioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.LocalidadSearchPage;
import ar.gov.rosario.siat.per.buss.bean.PerCalle;
import ar.gov.rosario.siat.per.buss.bean.PerLocalidad;
import ar.gov.rosario.siat.per.buss.bean.PerProvincia;
import ar.gov.rosario.siat.per.buss.dao.PerDAOFactory;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Facade de Domicilio
 * 
 * @author tecso
 *
 */
public class UbicacionFacade {
		
	private static Logger log = Logger.getLogger(UbicacionFacade.class);
	
	private static final UbicacionFacade INSTANCE = new UbicacionFacade();
	
	/**
	 * Constructor privado
	 */
	private UbicacionFacade() {
		
	}

	/**
	 * Devuelve unica instancia
	 */
	public static UbicacionFacade getInstance() {
		return INSTANCE;
	}

	// ---> Domicilio
    /**
     * 
     * Valida los datos de un domicilio para Rosario
     * Devuelve un Domicilio validado o nulo si no esta validado
     * @param domicilio
     * @throws Exception 
     * 
     */
    public Domicilio validarDomicilio(Domicilio domicilio) throws Exception {
        domicilio.setEsValidado(SiNo.SI.getId());
        return domicilio;
    }

    /**
     * Obtiene una lista de direcciones que podrian matchear con lo ingresado
     * @param  nombreCalle
     * @param  altura
     * @return List<Domicilio>
     * @exception Exception
     */
    public List<Domicilio> getListDomicilios(String nombreCalle, Long altura, boolean bis, String letra) throws Exception {
            return new ArrayList<Domicilio>();
    }
    // <--- Domicilio
    
    // --> Calle
    /**
     * Obtiene la lista de Calles
     * @param  calleSearchPage
     * @return List<Calle>
     */
    public List<Calle> getListCalleByCalleSearchPage(CalleSearchPage calleSearchPage) {
		return getListCalleByNombre(calleSearchPage.getCalle().getNombreCalle());
	}
    
    /**
     * Obtiene la lista de Calles filtradas por el 
     * nombre mandado como parametro.
     * 
     * @param  nombre
     * @return List<Calle>
     */
    public List<Calle> getListCalleByNombre(String nombreCalle) {
    	List<Calle> ret = new ArrayList<Calle>();
    	ret.add(new Calle(nombreCalle));
    	
    	return ret;
	}

    /**
     * Obtiene la calle a partir de su codigo de calle de la db general
     * Si no la encuentra devuelve null
     * @param  idCalle
     * @return Calle
     * @throws Exception
     */
    public Calle getCalle(Long idCalle) throws Exception {
        return new Calle("San Juan");
    }
    

    /** Obtiene las coordenadas X, e Y de la
     *  interseccion entre las dos calles, esta la posibilidad
     *  de que encuente mas de una interseccion, o ninguna
     * 
     * @param calle1
     * @param calle2
     * @return
     * @throws Exception
     */
    public List<Double> getInterseccion(String calle1, String calle2) throws Exception {
        return new ArrayList<Double>();
    }
    
    /** Obtiene las cuatro manzanas que 
     *  rodean al punto (x,y) pasado como parametro
     * 
     * @param ejeX
     * @param ejeY
     * @return
     * @throws Exception
     */
    public List<String> getManzanasCircundantes(Double ejeX, Double ejeY) throws Exception {
    	return new ArrayList<String>();
    }
    // <-- Calle
    
    // ---> Localidad
    /**
     * Obtiene una localidad a partir de su id
     * @param idLocalidad
     * @return Localidad
     * @throws Exception
     */
    public Localidad getLocalidad(Long idLocalidad) throws Exception {
    	return toLocalidad(PerLocalidad.getById(idLocalidad));
    }

    
    /**
     * busca una localidad por codPostal y Sobpostal
     * @param  codPostal
     * @param  codSubPostal
     * @return Localidad
     * @throws Exception
     */
    public Localidad getLocalidad(Long codPostal, Long codSubPostal) throws Exception {        
        return toLocalidad(PerLocalidad.getByCodPostal(codPostal));
    }

    /**
     * Obtiene la lista de localidades buscadas  
     * @param  localidadSearchPage
     * @return List<Localidad>
     * @throws Exception
     */
    public List<Localidad> getListLocalidadByLocalidadSearchPage(LocalidadSearchPage localidadSearchPage) throws Exception {
    	List<Localidad> ret = new ArrayList<Localidad>();
    	for(PerLocalidad e : PerDAOFactory.getPerLocalidadDAO().getBySearchPage(localidadSearchPage)) {
    		ret.add(toLocalidad(e));
    	}
    	return ret;
    }
    
    /**
     * Obtiene la localidad Rosario con los siguientes datos:
     * <ul>
     * <li>Id</li>
     * <li>Descripcion</li>
     * <li>Codigo postal</li>
     * <li>Codigo subpostal</li>
     * 
     * @return Localidad  
     */
    public Localidad getRosario() throws Exception {
        return toLocalidad(PerLocalidad.getByCodPostal(2000L));
    }
	//  <--- Localidad
	
    //  ---> Provincia
    /**
     * Obtiene una provincia a partir de su id
     * @param  idProvincia
     * @return Provincia
     * @throws Exception
     */
    public Provincia getProvincia(Long idProvincia) throws Exception {
    	return toProvincia(PerProvincia.getById(idProvincia));
    }

    /**
     * Obtiene la provincia de Sta Fe.
     * @return Provincia
     * @throws Exception
     */
    public Provincia getSantaFe() throws Exception {
    	return toProvincia(PerProvincia.getById(20L));
    }

    /**
     * Obtiene la lista de todas las provincias en la base de datos
     * @return List<Provincia>
     */
    public List<Provincia> getListProvincias() {
    	List<Provincia> ret = new ArrayList<Provincia>();
    	for(PerProvincia p : PerProvincia.getList()) {
    		ret.add(toProvincia(p));
    	}
    	return ret;
    }  	
	
    /**
     * Obtiene la lista de domicilios a partir de los filtros del domicilioSearchPage
     * @param  domicilioSearchPage
     * @return List<Domicilio>
     * @throws Exception
     */
	public List<Domicilio> getListDomicilioByDomicilioSearchPage(DomicilioSearchPage domicilioSearchPage ) throws Exception{

		List<Domicilio> listDomicilioSIAT = new ArrayList<Domicilio>();
		
		String nombreCalle = domicilioSearchPage.getDomicilio().getCalle().getNombreCalle();
		Long altura = domicilioSearchPage.getDomicilio().getNumero();
		boolean bis = domicilioSearchPage.getDomicilio().getBis().getEsSI() ? true : false;
		String letra = domicilioSearchPage.getDomicilio().getLetraCalle();
		listDomicilioSIAT = this.getListDomicilios(nombreCalle, altura, bis, letra); 

		return listDomicilioSIAT;
	}

	public String getNombreAbrevCalle(Long codCalle) throws Exception {
		return "";
	}


	private Calle toCalle(PerCalle perCalle) {
		Calle calle = new Calle();
		
		calle.setId(perCalle.getId());
		calle.setCodCalle(perCalle.getId());
		calle.setNombreCalle(perCalle.getNombreCalle());
		
		return calle;
	}

	private Localidad toLocalidad(PerLocalidad perLocalidad) {
		Localidad localidad = new Localidad();
		
		localidad.setId(perLocalidad.getId());
		localidad.setCodPostal(NumberUtil.getLong(perLocalidad.getCodPostal()));
		localidad.setCodSubPostal(null);
		localidad.setDescripcionPostal(perLocalidad.getDescripcion());
		localidad.setProvincia(toProvincia(perLocalidad.getProvincia()));
		
		return localidad;
	}

	private Provincia toProvincia(PerProvincia perProvincia) {
		Provincia provincia = new Provincia();

		provincia.setId(perProvincia.getId());
		provincia.setDescripcion(perProvincia.getDescripcion());
		
		return provincia;
	}

}