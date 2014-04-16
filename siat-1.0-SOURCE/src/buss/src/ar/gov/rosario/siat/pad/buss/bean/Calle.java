//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;

import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Esta clase representa una Calle tanto para el modelo como para el mapeo de
 * hibernate. Es copia de la clase Calle de la framework de la muni
 *
 * 
 */
@Embeddable
public class Calle extends BaseBO {

	private static final long serialVersionUID = 1L;

	/** Nombre de la calle */
	private String 	nombreCalle;
    
    private Long codCalle; 
    
    /**
     * Constructor requerido por hibernate.
     */
    public Calle() {
    }
    
    public Calle(String nombreCalle) {
        this.nombreCalle = nombreCalle;
    }

	// Getters y Setters
    public String getNombreCalle() {
		return nombreCalle;
	}
	public void setNombreCalle(String nombreCalle) {
		this.nombreCalle = nombreCalle;
	}
	public Long getCodCalle() {
		return codCalle;
	}
	public void setCodCalle(Long codCalle) {
		// seteo del id en la propiedad de la super
		this.setId(new Long(codCalle));
		this.codCalle = codCalle;
	}

	/**
	 * Obtiene una Calle a partir de su id
	 * @param id
	 * @return Calle
	 * @throws Exception
	 */
	public static Calle getByIdNull(Long id) throws Exception{
		
		return UbicacionFacade.getInstance().getCalle(id);
	}
	
	/** Recupera una lista de calles validas para
	 * un determinado nombre de calle y altura
	 * 
	 */
	public static List<Calle> getListCalle(String nombreCalle, Long altura, boolean bis, String letra) throws Exception{

		List<Domicilio> listDomicilio = 
			UbicacionFacade.getInstance().getListDomicilios(nombreCalle, altura, bis, letra);

		List<Calle> listCalle =	new ArrayList<Calle>(); 

		//agrego las no repetidas a una lista 
		for(Domicilio domicilio:listDomicilio) {
			Calle calle = domicilio.getCalle();
			if (!ListUtil.isInList(listCalle, calle)) {
				listCalle.add(calle);
			}
		}

		return listCalle;

	}

	/** Recupera una lista de calles validas para
	 * un determinado nombre de calle
	 * 
	 */
	public static List<Calle> getListCalle(String nombreCalle) throws Exception {

		List<Calle> listDomicilio = 
			UbicacionFacade.getInstance().getListCalleByNombre(nombreCalle);

		return listDomicilio;

	}

	/**
	 * Obtiene una calle desde MCR framework por codigo de calle
	 * @param codCalle codigo de la calle
	 * @return
	 * @throws Exception
	 */
	public static Calle getByCodCalle(Long codCalle) throws Exception{
		return UbicacionFacade.getInstance().getCalle(codCalle);
	}

}
