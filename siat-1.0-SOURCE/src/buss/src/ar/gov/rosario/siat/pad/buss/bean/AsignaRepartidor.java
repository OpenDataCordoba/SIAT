//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;

/**
 * Asignador de repartidores.
 * 
 * @author tecso
 */
public class AsignaRepartidor {

	private static Logger log = Logger.getLogger(AsignaRepartidor.class);
	private Long idRecurso = 0L; 
	private Recurso recurso = null;
	private Date fechaVig = null; 
	private List<CriRepCat> listCriCat = null;
	private HashMap<Long, String> cache = new HashMap<Long, String>(); //cache que relaciona un idCriRepCat con idRepartidor y con idBroche 

	public AsignaRepartidor(Long idRecurso, Date fechaVigencia){
		super();
		this.idRecurso = idRecurso;
		this.recurso = Recurso.getById(idRecurso);
		this.fechaVig = fechaVigencia;
	}
		
	/**
	 * Busca un id de broche asignado a la catastral que se pasa como parametro.
	 * La busqueda se realiza utilizando los criterios de repartidor por catastral
	 * en la represntados por CriRepCat.
	 * @param catastral: string separada por '/' cada parte de la catastral.
	 * @return el id de broche o null si no encontro el broche.
	 *         Si encuentra mas de uno, retorna el primero que encuentra
	 */
	public Long buscarIdBrochePorCatastral(String catastral) throws Exception {
		String find = buscarPorCatastral0(catastral);
		if (find == null)
			return null;
		String arr[] = find.split(",");
		return Long.valueOf(arr[1]);
	}

	/**
	 * Busca un id de Repartidor asignado a la catastral que se pasa como parametro.
	 * La busqueda se realiza utilizando los criterios de repartidor por catastral
	 * en la represntados por CriRepCat.
	 * @param catastral: string separada por '/' cada parte de la catastral.
	 * @return el id de repartidor encontrado. o null si no encontro el repartidor.
	 *         Si encuentra mas de uno, retorna el primero que encuentra
	 */
	public Long buscarIdRepartidorPorCatastral(String catastral) throws Exception {
		String find = buscarPorCatastral0(catastral);
		if (find == null)
			return null;
		String arr[] = find.split(",");
		return Long.valueOf(arr[0]);
	}
	
	/**
	 * Busca un Nro de Repartidor asignado a la catastral que se pasa como parametro.
	 * La busqueda se realiza utilizando los criterios de repartidor por catastral
	 * en la represntados por CriRepCat.
	 * @param catastral: string separada por '/' cada parte de la catastral.
	 * @return el id de repartidor encontrado. o null si no encontro el repartidor.
	 *         Si encuentra mas de uno, retorna el primero que encuentra
	 */
	public Long buscarNroRepartidorPorCatastral(String catastral) throws Exception {
		String find = buscarPorCatastral0(catastral);
		if (find == null)
			return null;
		String arr[] = find.split(",");
		return Long.valueOf(arr[2]);
	}


	private String buscarPorCatastral0(String catastral) throws Exception {
		// cargamos lista de criterios, y cada repartidor y broche asignado al criterio
		if (listCriCat == null) {
			this.listCriCat = PadDAOFactory.getCriRepCatDAO().getListActivosByRecursoFecha(this.idRecurso, this.fechaVig);
			cache.clear();
			for(CriRepCat item: listCriCat) {
				Repartidor rep = item.getRepartidor();
				Broche broche = rep.getBroche();
				cache.put(item.getId(), rep.getId() + "," + broche.getId() + "," + rep.getNroRepartidor());
			}
		}

		String buscarSecMan = catastral;
		Long find = 0L;
		for (CriRepCat criRepCat: listCriCat) {
			String sec = leftPad(criRepCat.getSeccion().getId(), 2);
			String manzDesde = criRepCat.getCatastralDesde();
			String manzHasta = criRepCat.getCatastralHasta();
			String secManDesde = sec + "/" + manzDesde;
			String secManHasta = sec + "/" + manzHasta;
			if (buscarSecMan.compareTo(secManDesde) >= 0 && buscarSecMan.compareTo(secManHasta) <= 0) {
				if (find == 0L) {
					find = criRepCat.getId();
					log.debug("buscarPorCatastral(): " + catastral + " asigna: idCriRepCat:" + criRepCat.getId() + " " + secManDesde + "-" + secManHasta);
					break; //si se desa que loguee cuadno encuenta mas de uno, comentar el break.
				} else {
					log.info("buscarPorCatastral(): " + catastral + " Cuidado! mas de un CriRepCat!: idCriRepCat:" + criRepCat.getId() + " " + secManDesde + "-" + secManHasta);					
				}
			}
		}
		return cache.get(find);
	}

	private String leftPad(long valor, int pad) {
		String s = "000000000000000000000000000000000";
		String ret = valor + "";
		return s.substring(0, pad - ret.length()) + ret;
	}
 
	
	/**
	 * Metodo que se deberá especializar para cada 
	 * criterio de asignacion.
	 * 
	 * Nota: Por ahora soporta busqueda por CATASTRAL.
  	 */
	public Long obtenerNroBroche(String key)  {
		try { 
			log.debug("Obteniendo broche para la clave " + key);

			return buscarIdBrochePorCatastral(key);			
		
		} catch (Exception e) {
			log.error("Ocurrio una excepcion durante la asignacion del broche");
			return null;
		}
	} 
	
}
