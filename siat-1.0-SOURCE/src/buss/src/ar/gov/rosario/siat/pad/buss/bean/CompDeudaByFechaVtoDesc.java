//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Comparator;

import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import coop.tecso.demoda.iface.helper.DateUtil;


/**
 * Comparator para ordenar deuda por fecha de vencimieto en forma descendente.
 * 
 * 
 * @author burano
 *
 */
public class CompDeudaByFechaVtoDesc implements Comparator<Deuda> {
	
	public int compare(Deuda d1, Deuda d2) {
		
		if (DateUtil.isDateBefore(d1.getFechaVencimiento(), d2.getFechaVencimiento()))
			return 0;
		else 
			return 1;
	}
}