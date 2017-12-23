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
 * Modella un'azienda
 *
 * @author Luca Gambetta
 */
public class Azienda extends User {

  /**
   * Partita IVA dell'azienda
   */
  protected String partitaIva;

  /**
   * Nome del rappresentante legale
   */
  protected String nomeRappresentanteLegale;

  /**
   * Cognome del rappresentante aziendale
   */
  protected String cognomeRappresentanteLegale;

  /**
   * Elenca gli stati possibili per una convenzione con l'università
   */
  public enum StatoConvenzione {
    /**
     * La convenzione è attiva
     */
    ATTIVA,
    /**
     * La convenzione non è più attiva
     */
    NON_ATTIVA
  };

  /**
   * Stato corrente della convenzione con l'università
   */
  protected StatoConvenzione statoConvenzione;

  /**
   * Riferimento alla convenzione stipulata con l'Università
   */
  protected String rifConvenzione;

  /**
   * Data della stipula della convenzione, come timestamp
   */
  protected long dataConvenzione;

  /**
   * Indirizzo della sede legale
   */
  protected String indirizzoSedeLegale;

  /**
   * Città della sede legale
   */
  protected String cittaSedeLegale;

  /**
   * Crea una nuova istanza dell'azienda
   *
   * @param login login dell'utente
   * @param email email dell'utente
   * @param partitaIva partita IVA dell'azienda
   * @param id ID univoco dell'azienda
   * 
   */
  public Azienda(String login, String email, String partitaIva, Long id) {
    super(login, email, id);
    this.partitaIva = partitaIva;
  }

  /**
   * Ritorna la partita IVA
   *
   * @return la partita IVA dell'azienda
   */
  public String getPartitaIva() {
    return partitaIva;
  }

  /**
   * Imposta la partita IVA dell'azienda
   *
   * @param partitaIva la partita IVA dell'azienda
   */
  public void setPartitaIva(String partitaIva) {
    this.partitaIva = partitaIva;
  }

  /**
   * Ritorna il nome del rappresentante legale
   *
   * @return il nome del rappresentante
   */
  public String getNomeRappresentanteLegale() {
    return nomeRappresentanteLegale;
  }

  /**
   * Imposta il nome del rappresentante legale
   *
   * @param nomeRappresentante il nome del rappresentante legale
   */
  public void setNomeRappresentanteLegale(String nomeRappresentante) {
    this.nomeRappresentanteLegale = nomeRappresentante;
  }

  /**
   * Ritorna il cognome del rappresentante legale
   *
   * @return il cognome del rappresentante legale
   */
  public String getCognomeRappresentanteLegale() {
    return cognomeRappresentanteLegale;
  }

  /**
   * Imposta il cognome del rappresentante legale
   *
   * @param cognomeRappresentante il cognome del rappresentante legale
   */
  public void setCognomeRappresentanteLegale(String cognomeRappresentante) {
    this.cognomeRappresentanteLegale = cognomeRappresentante;
  }

  /**
   * Ritorna lo stato della convenzione con l'università
   *
   * @return lo stato della convenzione
   */
  public StatoConvenzione getStatoConvenzione() {
    return statoConvenzione;
  }

  /**
   * Imposta lo stato della convenzione con l'università
   *
   * @param statoConvenzione lo stato della convenzione con l'università
   */
  public void setStatoConvenzione(StatoConvenzione statoConvenzione) {
    this.statoConvenzione = statoConvenzione;
  }

  /**
   * Ritorna il riferimento usato internamente dall'università per le convenzione tra azienda e
   * università
   *
   * @return il riferimento alla convenzione
   */
  public String getRifConvenzione() {
    return rifConvenzione;
  }

  /**
   * Imposta il riferimento usato internamente dall'università per le convenzione tra azienda e
   * università
   *
   * @param rifConvenzione il riferimento alla convenzione
   */
  public void setRifConvenzione(String rifConvenzione) {
    this.rifConvenzione = rifConvenzione;
  }

  /**
   * Ritorna il timestamp con la data della convenzione tra azienda e università
   *
   * @return un timestamp
   */
  public long getDataConvenzione() {
    return dataConvenzione;
  }

  /**
   * Imposta il timestamp con la data della convenzione tra azienda e università
   *
   * @param dataConvenzione un timestamp
   */
  public void setDataConvenzione(long dataConvenzione) {
    this.dataConvenzione = dataConvenzione;
  }

  /**
   * Ritorna l'indirizzo della sede legale dell'azienda
   *
   * @return l'indirizzo della sede legale
   */
  public String getIndirizzoSedeLegale() {
    return indirizzoSedeLegale;
  }

  /**
   * Imposta l'indirizzo della sede legale dell'azienda
   *
   * @param indirizzoSedeLegale l'indirizzo della sede legale
   */
  public void setIndirizzoSedeLegale(String indirizzoSedeLegale) {
    this.indirizzoSedeLegale = indirizzoSedeLegale;
  }

  /**
   * Ritorna la città in cui è ubicata la sede legale
   *
   * @return il nome della città
   */
  public String getCittaSedeLegale() {
    return cittaSedeLegale;
  }

  /**
   * Imposta il nome della città in cui è ubicata la sede legale dell'azienda
   *
   * @param cittaSedeLegale il nome della città
   */
  public void setCittaSedeLegale(String cittaSedeLegale) {
    this.cittaSedeLegale = cittaSedeLegale;
  }

}
