//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPDet;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class PlaEnvDeuPDetDAO extends GenericDAO {

	private static long migId;

	//private Log log = LogFactory.getLog(HistEstPlaEnvDPDAO.class);	
	
	public PlaEnvDeuPDetDAO() {
		super(PlaEnvDeuPDet.class);
	}
	

	//////////////
	public static long getMigId() {
		return migId;
	}
	public static void setMigId(long migId) {
		PlaEnvDeuPDetDAO.migId = migId;
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
	
	public Long createForLoad(PlaEnvDeuPDet o, LogFile output) throws Exception {
		long id = getNextId(output.getPath(), output.getNameFile());
		/*
		Column name          Type                                    Nulls
		id                   serial                                  no
		idplaenvdeupro       integer                                 no
		iddeuda              integer                                 no
		idestplaenvdeupd     integer                                 no
		usuario              varchar(60,0)                           no
		fechaultmdf          datetime year to second                 no
		estado               smallint                                no
		*/
		
		StringBuffer line = new StringBuffer();
		line.append(id);
		line.append("|");
		line.append(o.getPlaEnvDeuPro().getId());
		line.append("|");
		line.append(o.getIdDeuda());
		line.append("|");
		line.append("2");
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
