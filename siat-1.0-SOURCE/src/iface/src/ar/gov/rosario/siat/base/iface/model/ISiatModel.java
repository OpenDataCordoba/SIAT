//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.model;

import ar.gov.rosario.siat.cas.iface.model.CasoVO;

public interface ISiatModel {

	/**
	 * Metodo utilizado para que la intefaz de Sercivio de Caso "ICasCasoService"
	 * pueda recibir un SiatBussImageModel que implemente el getCaso().
	 * 
	 * @author Cristian
	 * @return
	 */
	public CasoVO getCaso();

	public void clearErrorMessages();

	public void addRecoverableError(String msgCampoRequerido,
			String casoNumeroLabel);

	public void addRecoverableError(String casoNoValido);
	
	public boolean hasError();

	public void addRecoverableValueError(String string);

	public void addMessage(String casoValido);

}
