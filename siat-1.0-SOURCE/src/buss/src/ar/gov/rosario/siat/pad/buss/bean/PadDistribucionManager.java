//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Manejador del m&oacute;dulo Pad y submodulo Distribuci&oacute;n
 * 
 * @author tecso
 *
 */
public class PadDistribucionManager {

	private static final PadDistribucionManager INSTANCE = new PadDistribucionManager();
	
	/**
	 * Constructor privado
	 */
	private PadDistribucionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static PadDistribucionManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Repartidor	
	public Repartidor createRepartidor(Repartidor repartidor) throws Exception {

		// Validaciones de negocio
		if (!repartidor.validateCreate()) {
			return repartidor;
		}

		PadDAOFactory.getRepartidorDAO().update(repartidor);

		return repartidor;
	}
	
	public Repartidor updateRepartidor(Repartidor repartidor) throws Exception {
		
		// Validaciones de negocio
		if (!repartidor.validateUpdate()) {
			return repartidor;
		}
		
		PadDAOFactory.getRepartidorDAO().update(repartidor);
		
	    return repartidor;
	}
	
	public Repartidor deleteRepartidor(Repartidor repartidor) throws Exception {

		// Validaciones de negocio
		if (!repartidor.validateDelete()) {
			return repartidor;
		}
		
		PadDAOFactory.getRepartidorDAO().delete(repartidor);
		
		return repartidor;
	}
	// <--- ABM Repartidor	
	

	// ---> ABM Broche	
	public Broche createBroche(Broche broche) throws Exception {

		// Validaciones de negocio
		if (!broche.validateCreate()) {
			return broche;
		}

		PadDAOFactory.getBrocheDAO().update(broche);

		return broche;
	}
	
	public Broche updateBroche(Broche broche) throws Exception {
		
		// Validaciones de negocio
		if (!broche.validateUpdate()) {
			return broche;
		}
		
		PadDAOFactory.getBrocheDAO().update(broche);
		
	    return broche;
	}
	
	public Broche deleteBroche(Broche broche) throws Exception {

		// Validaciones de negocio
		if (!broche.validateDelete()) {
			return broche;
		}
		
		PadDAOFactory.getBrocheDAO().delete(broche);
		
		return broche;
	}
	// <--- ABM Broche	

	// Obtener Broche asignado a un Repartidor de Tipo Fuera de Zona

	/**
	 * Devuelve una lista de Repartidores de Tipo Fuera de Zona, que incluyan un Criterio de Reparto
	 * por Calle donde Zona Ubicacion Origen coincida con el parametro zona, la calle sea la del domicilio pasado como parametro
	 * y cuyo rango de numero incluya al numero del domicilio pasado. 
	 * <p><i>TODO (por ahora solo funciona para el recurso TGI, ser mas adelante si se debe cambiar)</i></p>
	 * @param domicilio
	 * @param zona
	 * @return listRepartidor
	 */
	public List<Repartidor> getListRepartidorActivosFueraDeZonaByDomicilioYZonaOrigen(Domicilio domicilio, Zona zona){
		List<Repartidor> listRepartidorFueraDeZona = Repartidor.getListActivosByIdTipoRepartidor(TipoRepartidor.ID_TGI_FUERA_DE_ZONA_); // Fuera de Zona
		List<Repartidor> listRepartidorFinal = new ArrayList<Repartidor>(); 
		
		//si es bis, -1 sino  1
		int bis = domicilio.getBis()!=null && domicilio.getBis().intValue()==1 ? -1 : 1; 
		long numero = domicilio.getNumero().longValue() * bis;
		
		// Recorremos los Repartidores de Tipo Fuera de Zona
		for(Repartidor item: listRepartidorFueraDeZona){
			// Por cada Criterio de Reparto por Calle del Repartidor
			for(CriRepCalle criRepCalle: item.getListCriRepCalle()){
				// Comparamos si coindice la Zona Ubicacion Origen, la Calle y si el numero esta en el rango del criterio.
				if(criRepCalle.getZona().getId().equals(zona.getId()) 
						&& criRepCalle.getCalle().getCodCalle().longValue() == domicilio.getCalle().getCodCalle().longValue()
						&& criRepCalle.getNroDesde().longValue() <= numero
						&& numero <= criRepCalle.getNroHasta()){
					// Si el repartidor cumple las condiciones, lo agregamos a la lista de repartidores a retornar. 		
					listRepartidorFinal.add(item);
				}
			}
		}
		return listRepartidorFinal;
	}
	
	/**
	 * Devuelve una lista de Broches asignados a Repartidores de Tipo Fuera de Zona, que incluyan un Criterio de Reparto
	 * por Calle donde Zona Ubicacion Origen coincida con el parametro zona, la calle sea la del domicilio pasado como parametro
	 * y cuyo rango de numero incluya al numero del domicilio pasado. 
	 * 
	 * @param domicilio
	 * @param zona
	 * @return listBroche
	 */
	public List<Broche> getListBrocheActivosFueraDeZonaByDomicilioYZonaOrigen(Domicilio domicilio, Zona zona){
		// Buscar los repartidores fuera de zona que entre sus criterios de reparto incluyan al domicilio pasado con la zona ubicacion origen indicada.
		List<Repartidor> listRepartidor = getListRepartidorActivosFueraDeZonaByDomicilioYZonaOrigen(domicilio, zona);
		List<Broche> listBroche = new ArrayList<Broche>();
		// Una vez que tenemos la lista de repartidores, buscamos los broches que tienen asignados.
		for(Repartidor repartidor: listRepartidor){
			if(repartidor.getBroche()!=null){
				listBroche.add(repartidor.getBroche());
			}
		}

		return listBroche;		
	}


	/**
	 * Devuelve un broche asignado a un Repartidor de Tipo Fuera de Zona, que en sus Criterio de Reparto por Calle
	 * incluya uno donde Zona Ubicacion Origen coincida con el parametro zona, la calle sea la del domicilio pasado
	 * como parametro y cuyo rango de numero incluya al numero del domicilio pasado.
	 * (Si no encuentra ninguno devuelve null, si encuentra mas de uno devuelve el primero)
	 * <p><i>TODO (por ahora solo funciona para el recurso TGI, ser mas adelante si se debe cambiar)</i></p>
	 * 
	 * @param domicilio
	 * @param zona
	 * @return broche
	 */
	public Broche obtenerRepartidorFueraDeZona(Domicilio domicilio, Zona zona){
		List<Broche> listBroche = getListBrocheActivosFueraDeZonaByDomicilioYZonaOrigen(domicilio, zona);
		if(ListUtil.isNullOrEmpty(listBroche)){
			return null;
		}
		return listBroche.get(0);
	}
	
	
}
