//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.HistEstPlaEnvDP;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPro;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class HistEstPlaEnvDPDAO extends GenericDAO {

	private static long migId;

	//private Log log = LogFactory.getLog(HistEstPlaEnvDPDAO.class);	
	
	public HistEstPlaEnvDPDAO() {
		super(HistEstPlaEnvDP.class);
	}
	
	/**
	 * Elimina los Historicos la planilla de envio de deuda a procuradores
	 * @param plaEnvDeuPro
	 * @return int
	 */
	public int deleteByPlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) {
		
		String queryString = "DELETE FROM HistEstPlaEnvDP hepeDP " +
				"WHERE hepeDP.plaEnvDeuPro = :plaEnvDeuPro)";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("plaEnvDeuPro", plaEnvDeuPro);
		return query.executeUpdate();	
	}

	//////////////
	public static long getMigId() {
		return migId;
	}
	public static void setMigId(long migId) {
		HistEstPlaEnvDPDAO.migId = migId;
	}

	/**
	 *  Devuelve el proximo valor de id a asignar. 
	 *  Para se inicializa obteniendo el ultimo id asignado el archivo de migracion con datos pasados como parametro
	 *  y luego en cada llamada incrementa el valor.
	 * 
	 * @return long - el proximo id a asignar.
	 * @throws Exception
	 */
	public long getNextId(String path, String nameFile) throws Exception{
		// Si migId==-1 buscar MaxId en el archivo de load. Si migId!=-1, migId++ y retornar migId;
		if(getMigId()==-1){
			setMigId(this.getLastId(path, nameFile)+1);
		}else{
			setMigId(getMigId() + 1);
		}

		return getMigId();
	}
	
	public Long createForLoad(HistEstPlaEnvDP o, LogFile output) throws Exception {
		long id = getNextId(output.getPath(), output.getNameFile());
		/*
		Column name          Type                                    Nulls		
		id                   serial                                  no
		idestplaenvdeupr     integer                                 no
		idplaenvdeupro       integer                                 no
		fechadesde           datetime year to second                 no
		logestado            varchar(255)                            yes
		usuario              char(10)                                no
		fechaultmdf          datetime year to second                 no
		estado               smallint                                no
		*/
		
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getEstPlaEnvDeuPr().getId());
		line.append("|");
		line.append(o.getPlaEnvDeuPro().getId());
		line.append("|");
		if (o.getFechaDesde() != null) {
			line.append(DateUtil.formatDate(o.getFechaDesde(), "yyyy-MM-dd"));
		}
		line.append("|");
		if (o.getLogEstado() != null) { line.append(o.getLogEstado()); }
		line.append("|"); 				
		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");
		line.append("2010-01-01 00:00:00");
		line.append("|");
		line.append("1");
		line.append("|");
		
		output.addline(line.toString());
		// Seteamos el id generado en el bean.
		o.setId(id);
	
		return id;
	}

}
