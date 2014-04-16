//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

/**
 * Nombre de los estados de una corrida
 * @author Tecso Coop. Ltda.
 *
 */
public enum AdpRunState {
	CANCEL(-4, "Cancelar"),
	MSG(-3L, "Nuevo Mensaje"),
	STEP(-2L, "Siguiente Paso"),
	VOLATIL(-1L, "Volatil"),
	PREPARACION(1, "En Preparacion"),
	ESPERA_COMENZAR(2L, "Espera Comenzar"), 
	ESPERA_CONTINUAR(3L, "Espera Continuar"),
	PROCESANDO(4L, "Procesando"),
	FIN_OK(5L, "Procesado con Exito"),
	FIN_ERROR(6L, "Procesado con Error"),	
	VAL_ERROR(7L, "Validacion con Error"),
	FIN_ADVERT(8L, "Cancelado"),
	ABORT_EXCEP(9L, "Abortado por Exeception"),
	//-Se agrega estado "Sin Procesar" por issue 7805: AFIP - Acomodar deuda. 
	SIN_PROCESAR(10L, "Sin Procesar");
		
	private long id;
	private String description;
	
	AdpRunState(long id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public long id() { return this.id; }
	public String description() { return this.description; } //TODO: hacer que salga de los valores de la DB 

    public static AdpRunState getById(long findId){
 	   AdpRunState[] list = AdpRunState.values();
 	   for (AdpRunState state : list) {
		   if (state.id == findId) {
			   return state;
		   }
	   }
 	   return null;
    }
}
