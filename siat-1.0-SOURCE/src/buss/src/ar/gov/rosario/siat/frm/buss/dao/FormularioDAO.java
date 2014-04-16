//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.buss.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.DesImp;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormularioSearchPage;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class FormularioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(FormularioDAO.class);	
	private GenericDAO genericDao = null;
	
	public FormularioDAO() {
		super(Formulario.class);
		genericDao = new GenericDAO(Formulario.class);
	}
	
	public List<Formulario> getBySearchPage(FormularioSearchPage formularioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Formulario t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del FormularioSearchPage: " + formularioSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (formularioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro formulario excluidos
 		List<FormularioVO> listFormularioExcluidos = (ArrayList<FormularioVO>) formularioSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listFormularioExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listFormularioExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(formularioSearchPage.getFormulario().getCodFormulario())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codFormulario)) like '%" + 
				StringUtil.escaparUpper(formularioSearchPage.getFormulario().getCodFormulario()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(formularioSearchPage.getFormulario().getDesFormulario())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desFormulario)) like '%" + 
				StringUtil.escaparUpper(formularioSearchPage.getFormulario().getDesFormulario()) + "%'";
			flagAnd = true;
		}

		// filtro por formato de salida
 		if (formularioSearchPage.getFormulario().getDesImp()!=null && formularioSearchPage.getFormulario().getDesImp().getId()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.desImp.id=" + 
				formularioSearchPage.getFormulario().getDesImp().getId();
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codFormulario ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Formulario> listFormulario = (ArrayList<Formulario>) genericDao.executeCountedSearch(queryString, formularioSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listFormulario;
	}

	/**
	 * Obtiene un Formulario por su codigo
	 */
	public Formulario getByCodigo(String codigo) throws Exception {
		Formulario formulario = null;
		
		if (!StringUtil.isNullOrEmpty(codigo)) {
			String queryString = "from Formulario t where UPPER(TRIM(t.codFormulario)) = '" + 
					StringUtil.escaparUpper(codigo) + "'";
			Session session = SiatHibernateUtil.currentSession();
			formulario = (Formulario)session.createQuery(queryString).uniqueResult();		 
		}

		return formulario; 
	}

	public Formulario getById(Long id) throws HibernateException {
		return (Formulario) genericDao.getById(id);
	}

    public Formulario getByIdNull(Long id) throws HibernateException {
		return (Formulario) genericDao.getByIdNull(id);
	}

    public List<Formulario> getList() throws HibernateException {
		return (List<Formulario>) genericDao.getList();
	}

    public List<Formulario> getListActiva() throws HibernateException {
		return (List<Formulario>) genericDao.getListActiva();
	}

    public Long update(Formulario f) throws Exception {
		Long ret = genericDao.update(f);
		writeXsl(f);
		writeXslTxt(f);
		writeXmlTest(f);
		return ret;
	}

    public void delete(Formulario f) throws HibernateException {
		genericDao.delete(f);
	}

	/**
	 * Escribe el archivo asociado al formulario.
	 * lo escribe en el dir /publico/general/reportes/db con nombre [codFormulario].xsl
	 */
	private void writeXsl(Formulario f) throws Exception {
		String data = f.getXsl();
		if (data == null) return;

		File file = new File(SiatParam.getFileSharePath() + "/publico/general/reportes/db");
		if (File.pathSeparator.equals("\\")) {
			file = new File("c:\\", file.getPath());
		}
		file = new File(file, f.getCodFormulario() + ".xsl");

		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
		osw.write(data);
		osw.close();
	}

	/**
	 * Escribe el archivo asociado al formulario.
	 * lo escribe en el dir /publico/general/reportes/db con nombre [codFormulario]_txt.xsl
	 */
	private void writeXslTxt(Formulario f) throws Exception {
		String data = f.getXslTxt();
		if (data == null) return;

		File file = new File(SiatParam.getFileSharePath() + "/publico/general/reportes/db");
		if (File.pathSeparator.equals("\\")) {
			file = new File("c:\\", file.getPath());
		}
		file = new File(file, f.getCodFormulario() + "_txt.xsl");

		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
		osw.write(data);
		osw.close();
	}

	/**
	 * Escribe el archivo asociado al formulario.
	 * lo escribe en el dir /publico/general/reportes/db con nombre [codFormulario]_xmlTest.xml
	 */
	private void writeXmlTest(Formulario f) throws Exception {
		String data = f.getXmlTest();
		if (data == null) return;

		File file = new File(SiatParam.getFileSharePath() , "/publico/general/reportes/db");
		if (File.pathSeparator.equals("\\")) {
			file = new File("c:\\", file.getPath());
		}
		file = new File(file, f.getCodFormulario() + "_xmltest.xml");

		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
		osw.write(data);
		osw.close();
	}

	
	/**
	 * Carga el contenindo del archivo xmlTest(ver writeXmlTest) xmlTest en la propiedad del formulario.
	 */
	public void loadXmlTest(Formulario f) throws Exception {
		File file = new File(SiatParam.getFileSharePath() + "/publico/general/reportes/db");
		if (File.pathSeparator.equals("\\")) {
			file = new File("c:\\", file.getPath());
		}
		file = new File(file, f.getCodFormulario() + "_xmltest.xml");

		if (!file.exists()) {
			log.warn("No se encontro archivo xml test: " + file.getPath());
			f.setXmlTest(null);
			return;
		}

		StringBuffer buffer = new StringBuffer();
		readFile(file, buffer);
		f.setXmlTest(buffer.toString()); 
	}

	/**
	 * Carga el contenindo del archivo XSL (ver writeXSL) en la propiedad xsl del formulario.
	 */
	public void loadXsl(Formulario f) throws Exception {
		File file = new File(SiatParam.getFileSharePath() + "/publico/general/reportes/db");
		if (File.pathSeparator.equals("\\")) {
			file = new File("c:\\", file.getPath());
		}
		file = new File(file, f.getCodFormulario() + ".xsl");

		if (!file.exists()) {
			log.warn("No se encontro archivo xsl: " + file.getPath());
			f.setXsl(null);
			return;
		}

		StringBuffer buffer = new StringBuffer();
		readFile(file, buffer);
		f.setXsl(buffer.toString()); 
	}

	/**
	 * Carga el contenindo del archivo XSL para salida por TXT (ver writeXSL) en la propiedad xsl del formulario.
	 */
	public void loadXslTxt(Formulario f) throws Exception {
		File file = new File(SiatParam.getFileSharePath() + "/publico/general/reportes/db");
		if (File.pathSeparator.equals("\\")) {
			file = new File("c:\\", file.getPath());
		}
		file = new File(file, f.getCodFormulario() + "_txt.xsl");

		if (!file.exists()) {
			log.warn("No se encontro archivo xsl: " + file.getPath());
			f.setXslTxt(null);
			return;
		}

		StringBuffer buffer = new StringBuffer();
		readFile(file, buffer);
		f.setXslTxt(buffer.toString()); 
	}

	private void readFile(File file, StringBuffer buffer) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
		Reader in = new BufferedReader(isr);
		int ch;
		while ((ch = in.read()) > -1) {
			buffer.append((char) ch);
		}
		in.close();
	} 

	/**
	 * Retorna la lista de Formularios activos filtrando por DesImp
	 */
	public List<Formulario> getListFormularioActivoByDesImp(DesImp desImp) throws Exception {
		
		// Armamos la query
		String queryString = "from Formulario frm where frm.desImp = :desImp" +
							 " and estado = " + Estado.ACTIVO.getId();
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
								.setEntity("desImp", desImp);
		 
		return (ArrayList<Formulario>) query.list(); 
	}

	/**
	 * Retorna la lista de Formularios activos ordenada por descripcion
	 */
	@SuppressWarnings("unchecked")
	public List<Formulario> getListActivosOrdenada() throws Exception {
		
		// Armamos la query
		String queryString = "";
		queryString += "from Formulario frm where frm.estado = " + Estado.ACTIVO.getId();
		queryString += " order by frm.desFormulario ";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		 
		return (ArrayList<Formulario>) query.list(); 
	}
	
}
