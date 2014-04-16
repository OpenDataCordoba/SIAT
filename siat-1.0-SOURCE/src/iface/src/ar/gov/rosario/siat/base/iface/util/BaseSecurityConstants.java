//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.util;

import coop.tecso.demoda.iface.model.CommonView;


/**
 * En esta clase se definen las constantes asociadas con la seguridad comunes a todos los modulos.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class BaseSecurityConstants {

	// Constantes utilizadas como parametros del canAccess de los AdministrarXXDAction
	public static final String BUSCAR                 = "buscar";
	public static final String VER                    = CommonView.METODO_VER;
	public static final String MODIFICAR              = CommonView.METODO_MODIFICAR;
	public static final String ELIMINAR               = CommonView.METODO_ELIMINAR;
	public static final String AGREGAR                = CommonView.METODO_AGREGAR;
	public static final String ACTIVAR                = CommonView.METODO_ACTIVAR;
	public static final String DESACTIVAR             = CommonView.METODO_DESACTIVAR;
	public static final String ANULAR          	      = "anular";
	public static final String ACEPTAR          	  = "aceptar";
	public static final String INCLUIR          	  = "incluir";
	public static final String EXCLUIR          	  = "excluir";
	public static final String ENVIAR          	 	  = "enviar";
	public static final String DEVOLVER          	  = "devolver";
	public static final String EMITIR				  = "emitir";
	public static final String GUARDAR          	  = "guardar";
	public static final String VUELTA_ATRAS        	  = "vueltaAtras";
	public static final String RELACIONAR        	  = "relacionar";
	public static final String CONCILIAR        	  = "conciliar";
	// usuario y passwros anonimo
	public static final String	USR_ANONIMO           = "internet";
	public static final String	PWD_ANONIMO           = "internet";
}