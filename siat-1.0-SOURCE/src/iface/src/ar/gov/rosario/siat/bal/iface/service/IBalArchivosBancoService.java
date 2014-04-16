//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.ArchivoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ArchivoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ArchivoVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalArchivosBancoService {
	
	// ---> ABM Archivo
	public ArchivoSearchPage getArchivoSearchPageInit(UserContext userContext, ArchivoSearchPage archivoSPFiltro) throws DemodaServiceException;
	public ArchivoSearchPage getArchivoSearchPageResult(UserContext userContext, ArchivoSearchPage archivoSearchPage) throws DemodaServiceException;
	
	public ArchivoAdapter getArchivoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
		
	public ArchivoVO anularArchivo(UserContext userContext, ArchivoVO archivoVO) throws DemodaServiceException;
	public ArchivoVO aceptarArchivo(UserContext userContext, ArchivoVO archivoVO) throws DemodaServiceException;
	public ArchivoVO deleteArchivo(UserContext userContext, ArchivoVO archivoVO) throws DemodaServiceException;
	
	public ArchivoSearchPage imprimirArchivos(UserContext userContext, ArchivoSearchPage archivoSearchPage) throws  DemodaServiceException;
	// <--- ABM Archivo

}
