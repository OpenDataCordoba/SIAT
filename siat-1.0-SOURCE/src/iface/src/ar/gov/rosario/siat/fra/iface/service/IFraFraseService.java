//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.service;

import java.util.Properties;

import ar.gov.rosario.siat.fra.iface.model.FraProperties;
import ar.gov.rosario.siat.fra.iface.model.FraseAdapter;
import ar.gov.rosario.siat.fra.iface.model.FraseSearchPage;
import ar.gov.rosario.siat.fra.iface.model.FraseVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IFraFraseService {
	
	// ---> ABM Frase
	public FraseSearchPage getFraseSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public FraseSearchPage getFraseSearchPageResult(UserContext usercontext, FraseSearchPage fraseSearchPage) throws DemodaServiceException;

	public FraseAdapter getFraseAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FraseAdapter getFraseAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public FraseVO updateFrase(UserContext userContext, FraseVO fraseVO ) throws DemodaServiceException;
	public FraseVO publicarFrase(UserContext userContext, FraseVO fraseVO ) throws DemodaServiceException;	
	public FraseAdapter imprimirFrase(UserContext userContext, FraseAdapter fraseAdapterVO)throws DemodaServiceException;
	public void checkNew (Properties propiedades) throws DemodaServiceException;
	public void loadFraProperties(FraProperties fraProperties) throws DemodaServiceException;
	// <--- ABM Frase
	
}