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
 * Modella un operatore dell'ufficio tirocinio e stage
 * 
 * @author Luca Gambetta
 */
public class OperatoreUfficioTirocinio extends User {

  /**
   * Codice fiscale dell'operatore
   */
  protected String codiceFiscale;

  /**
   * Specifica il ruolo dell'operatore
   */
  public enum Ruolo {
    /**
     * L'utente è un operatore semplice
     */
    OPERATORE,
    /**
     * L'utente è presidente del consiglio didattico
     */
    PRESIDENTE_CONSIGLIO_DIDATTICO,
    /**
     * L'utente è il direttore del dipartimento
     */
    DIRETTORE_DIPARTIMENTO
  };

  /**
   * Ruolo dell'operatore in seno all'ufficio stage e tirocinio
   */
  protected Ruolo ruolo;

  /**
   * Crea una nuova istanza della classe.
   * 
   * Il codice fiscale viene usato per indicare univocamente l'utente.
   * 
   * @param login login dell'utente
   * @param email email dell'utente
   * @param codiceFiscale codice fiscale
   * @param ruolo ruolo dell'operatore in seno all'ufficio
   * @param id ID dell'utente nel DB
   */
  public OperatoreUfficioTirocinio(String login, String email, String codiceFiscale, Ruolo ruolo, Long id) {
    super(login, email, id);
    this.ruolo = ruolo;
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * Ritorna il codice fiscale dell'operatore
   *
   * @return il codice fiscale dell'operatore
   */
  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  /**
   * Imposta il codice fiscale dell'utente
   *
   * @param codiceFiscale il codice fiscale dell'operatore
   */
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * Ritorna il ruolo dell'operatore
   *
   * @return il ruolo dell'operatore
   */
  public Ruolo getRuolo() {
    return ruolo;
  }

  /**
   * Imposta il ruolo dell'operatore
   *
   * @param ruolo il ruolo dell'operatore
   */
  public void setRuolo(Ruolo ruolo) {
    this.ruolo = ruolo;
  }

}
