/*******************************************************************************
 * 
 * RENCI Open Source Software License � The University of North Carolina at
 * Chapel Hill
 * 
 * The University of North Carolina at Chapel Hill (the "Licensor") through its
 * Renaissance Computing Institute (RENCI) is making an original work of
 * authorship (the "Software") available through RENCI upon the terms set forth
 * in this Open Source Software License (this "License"). This License applies
 * to any Software that has placed the following notice immediately following
 * the copyright notice for the Software: Licensed under the RENCI Open Source
 * Software License v. 1.0.
 * 
 * Licensor grants You, free of charge, a world-wide, royalty-free,
 * non-exclusive, perpetual, sublicenseable license to do the following to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  . Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimers.
 *  . Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimers in the documentation
 * and/or other materials provided with the distribution.
 *  . Neither You nor any sublicensor of the Software may use the names of
 * Licensor (or any derivative thereof), of RENCI, or of contributors to the
 * Software without explicit prior written permission. Nothing in this License
 * shall be deemed to grant any rights to trademarks, copyrights, patents, trade
 * secrets or any other intellectual property of Licensor except as expressly
 * stated herein.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * CONTIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * You may use the Software in all ways not otherwise restricted or conditioned
 * by this License or by law, and Licensor promises not to interfere with or be
 * responsible for such uses by You. This Software may be subject to U.S. law
 * dealing with export controls. If you are in the U.S., please do not mirror
 * this Software unless you fully understand the U.S. export regulations.
 * Licensees in other countries may face similar restrictions. In all cases, it
 * is licensee's responsibility to comply with any export regulations applicable
 * in licensee's jurisdiction.
 * 
 ******************************************************************************/
package org.renci.common.logging;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;

/**
 * 
 * @author jdr0887
 */
public class LoggingServiceTest extends TestCase {

	public LoggingServiceTest() {
		super();
	}

	public LoggingServiceTest(String arg0) {
		super(arg0);
	}

	public void testLogger() {
		Log logger = LoggingService.getLog(this.getClass().getName());
		assertNotNull(logger);
		logger.debug("test");
	}
	
	public void testDifferentPackage() {
        Log logger = LoggingService.getLog("org.renci.bioportal.menu");
		assertNotNull(logger);
		logger.debug("test");
	}
	
}
