//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Bean correspondiente a la Extension de la Transaccion Autodeclarativa para el Servicio Banco 84 
 * correspondiente a ETUR
 * 
 * @author tecso
 */
@Entity
@DiscriminatorValue("84")
public class TransaccionETUR extends TransaccionAutoLiq {

	private static final long serialVersionUID = 1L;

}
