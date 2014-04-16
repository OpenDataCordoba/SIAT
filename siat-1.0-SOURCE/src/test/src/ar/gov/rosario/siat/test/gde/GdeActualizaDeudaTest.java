//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.test.gde;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.test.TestUtil;

public class GdeActualizaDeudaTest {

	@BeforeClass
	public static void oneTimeBefore() {
		TestUtil.initSiat();
	}
	
	@AfterClass
	public static void oneTimeAfter() {
	}

	@Before
	public void before() {
	}

	@After
	public void after() {
	}

	@Test
	public void testActualizaDeuda001() throws Exception {
		//genera el test en el archivo /tmp/actuadeu.log
		String sampleFilename = "/tmp/actuadeu.log";
		String originFilename = "data/test001-actuadeu";

		ActualizaDeuda.getInstance().test001("/tmp/actuadeu.log");
		assertTrue("Actualiza Deuda 001", TestUtil.compareFiles(originFilename, sampleFilename));
	}
}
