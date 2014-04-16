//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.List;

import coop.tecso.demoda.buss.bean.BaseBO;


public class Provincia extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	// Propiedades
    private String descripcion;

    // Constructores
    public Provincia() {
        super();
    }
    
    public Provincia(Long id, String descripcion) {
		super(id);
		this.descripcion = descripcion;
	}



	// Geters y Setters
    public String getDescripcion() {

        return descripcion;
    }
    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public static List<Provincia> getList() {
    	
    	return UbicacionFacade.getInstance().getListProvincias();
	}

    public static Provincia getByIdNull(Long id) throws Exception {
		return UbicacionFacade.getInstance().getProvincia(id);
	}

    public static Provincia getSantaFe() throws Exception {
    	
    	return UbicacionFacade.getInstance().getSantaFe();
	}
}
