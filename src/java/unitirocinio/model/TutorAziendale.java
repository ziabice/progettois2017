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
 * Modella un Tutor aziendale.
 * 
 * @author Luca Gambetta
 */
public class TutorAziendale extends User {

  /**
   * ID dell'azienda a cui afferisce il tutor aziendale
   */
  protected Long idAzienda;

  /**
   * Codice fiscale del tutor aziendale
   */
  protected String codiceFiscale;

  /**
   * Numero di telefono del tutor aziendale
   */
  protected String telefono;

  /**
   * Crea una nuova istanza di un tutor aziendale.
   * 
   * @param login login dell'utente
   * @param email email dell'utente
   * @param codiceFiscale codice fiscale dell'utente
   * @param idAzienda ID dell'azienda a cui afferisce
   * @param id ID univoco del tutor aziendale
   */
  public TutorAziendale(String login, String email, String codiceFiscale, Long idAzienda, Long id) {
    super(login, email, id);
    this.idAzienda = idAzienda;
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * Ritorna il codice fiscale del tutor aziendale
   *
   * @return il codice fiscale del tutor aziendale
   */
  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  /**
   * Imposta il codice fiscale del tutor aziendale
   *
   * @param codiceFiscale il codice fiscale del tutor aziendale
   */
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * Ritorna l'ID dell'azienda a cui appartiene il tutor
   *
   * @return l'ID dell'azienda
   */
  public Long getIdAzienda() {
    return idAzienda;
  }

  /**
   * Imposta l'ID dell'azienda a cui appartiene il tutor aziendale
   *
   * @param idAzienda l'ID dell'azienda
   */
  public void setIdAzienda(Long idAzienda) {
    this.idAzienda = idAzienda;
  }

  /**
   * Ritorna il numero di telefono del tutor aziendale
   *
   * @return il numero di telefono
   */
  public String getTelefono() {
    return telefono;
  }

  /**
   * Impostail numero di telefono del tutor aziendale
   *
   * @param telefono il numero di telefono
   */
  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

}
