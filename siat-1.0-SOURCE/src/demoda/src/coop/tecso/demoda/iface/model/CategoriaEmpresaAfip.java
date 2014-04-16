//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para las categorias de empresas. Datos de Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum CategoriaEmpresaAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    DIRECTO(1, "Contribuyente Directo"), 
    MULTILATERAL(2, "Convenio Multilateral");
    
    private Integer id;
    private String value;

    private CategoriaEmpresaAfip(final Integer id, final String value) {
        this.id = id;
        this.value = value;
    }
    
    public Integer getId() {
		return id;
	}

    public String getValue() {
		return value;
	}

    public String toString() {
    	return "CategoriaEmpresaAfip: [" + id + ", " + value + "]";
	}
	
    public static CategoriaEmpresaAfip getById(Integer _id) {
       CategoriaEmpresaAfip[] categoriaEmpresaAfip = CategoriaEmpresaAfip.values();
	   for (int i = 0; i < categoriaEmpresaAfip.length; i++) {
		   CategoriaEmpresaAfip tipo = categoriaEmpresaAfip[i];
  		   if (_id == null){
  			   if (tipo.id == null){
  				   return tipo;
  			   }  				   
  		   } else {
			   if (tipo.id.intValue() == _id.intValue()){
				   return tipo;
			   }
  		   }
		}
		   return null;
	}
    
    /**
     * Devuelve un Id valido para persistir, en un create o update.
     *
     * @return el numero del SiNo.
     */
    public Integer getBussId(){
 	    if (id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
    public static List<CategoriaEmpresaAfip> getList(){
    	
    	List<CategoriaEmpresaAfip> listCategoriaEmpresaAfip = new ArrayList<CategoriaEmpresaAfip>();
 	   
 	   //Obtengo la lista de CategoriaEmpresaAfip
 	   CategoriaEmpresaAfip[] categoriaEmpresaAfip = CategoriaEmpresaAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < categoriaEmpresaAfip.length; i++) {
 	       	if (categoriaEmpresaAfip[i].getId() != null &&
 	       		categoriaEmpresaAfip[i].getId() >= 0)
 	       	listCategoriaEmpresaAfip.add(categoriaEmpresaAfip[i]);
 	   }
        return listCategoriaEmpresaAfip;
    }
    
  	

}
