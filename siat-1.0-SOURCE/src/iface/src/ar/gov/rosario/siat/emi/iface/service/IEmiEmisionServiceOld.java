//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import ar.gov.rosario.siat.emi.iface.model.EmisionAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEmiEmisionServiceOld {

	// A deprecar
	public EmisionAdapter getEmisionAdapterForView(UserContext userSession,
			CommonKey commonKey) throws DemodaServiceException;
	public EmisionAdapter getEmisionAdapterForUpdate(UserContext userSession,
			CommonKey commonKey) throws DemodaServiceException;
	public EmisionAdapter getEmisionAdapterForCreate(UserContext userSession) throws DemodaServiceException;
	public EmisionVO createEmision(UserContext userSession,
			EmisionAdapter emisionAdapterVO) throws DemodaServiceException;
	public EmisionVO updateEmision(UserContext userSession,
			EmisionAdapter emisionAdapterVO) throws DemodaServiceException;;
	public EmisionVO deleteEmision(UserContext userSession, EmisionVO emision) throws DemodaServiceException;;
	public EmisionSearchPage getEmisionSearchPageInit(UserContext userSession) throws DemodaServiceException;
	public EmisionSearchPage getEmisionSearchPageResult(
			UserContext userSession, EmisionSearchPage emisionSearchPageVO) throws DemodaServiceException;
	public EmisionAdapter paramRecurso(UserContext userSession,
			EmisionAdapter emisionAdapterVO) throws DemodaServiceException;

}
