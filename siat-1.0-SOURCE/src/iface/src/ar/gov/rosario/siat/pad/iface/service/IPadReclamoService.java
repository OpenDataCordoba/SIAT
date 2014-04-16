//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import ar.gov.rosario.siat.pad.iface.model.BuzonCambiosAdapter;
import ar.gov.rosario.siat.pad.iface.model.BuzonCambiosVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IPadReclamoService {
	
	// ---> ABM BuzonCambios
	public BuzonCambiosAdapter getBuzonCambiosAdapterForCreate(UserContext userContext, CommonKey commonKeyTitular) throws DemodaServiceException;
	public BuzonCambiosVO createBuzonCambios(UserContext userContext, BuzonCambiosVO buzonCambiosVO ) throws DemodaServiceException;
	// <--- ABM BuzonCambios
	
}