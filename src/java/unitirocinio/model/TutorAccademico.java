/*
 * The MIT License
 *
 * Copyright 2017 Luca Gambetta <l.gambetta@studenti.unisa.it>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package unitirocinio.model;

/**
 * Modella un tutor accademico
 *
 * @author Luca Gambetta
 */
public class TutorAccademico extends User {

  /**
   * Il codice fiscale del tutor accademico
   */
  protected String codiceFiscale;

  /**
   * Istanzia un nuovo tutor accademico
   * 
   * @param login login dell'utente
   * @param email email dell'utente
   * @param codiceFiscale codice fiscale dell'utente
   * @param id ID del tuto accademico
   */
  public TutorAccademico(String login, String email, String codiceFiscale, Long id) {
    super(login, email, id);
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * Ritorna il codice fiscale del tutor accademico
   *
   * @return il codice fiscale
   */
  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  /**
   * Imposta il codice fiscale del tutor accademico
   *
   * @param codiceFiscale il codice fiscale
   */
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

}
