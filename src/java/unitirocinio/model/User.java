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
 * Modella un utente generico del sistema.
 *
 * @author Luca Gambetta
 */
public abstract class User {

  /**
   * ID univoco dell'utente quando salvato nel database
   */
  protected Long id = null;

  /**
   * Data dell'iscrizione dell'utente: mantiene un timestamp della data
   */
  protected long dataIscrizione;

  /**
   * Nome dell'utente
   */
  protected String nome;
  
  /**
   * Cognome dell'utente
   */
  protected String cognome;
  
  /**
   * Login dell'utente
   */
  protected String login;
  
  /**
   * Indirizzo e-mail dell'utente
   */
  protected String email;
  
  /**
   * Password crittata 
   */
  protected String password;

  /**
   * Crea un nuovo utente del tipo specificato
   *
   * @param login login dell'utente
   * @param email email dell'utente
   * @param id ID dell'utente nel database
   */
  public User(String login, String email, Long id) {
    this.id = id;
    this.login = login;
    this.email = email;
  }

  /**
   * Ritorna l'ID dell'oggetto
   *
   * @return l'ID dell'oggetto
   */
  public Long getId() {
    return id;
  }

  /**
   * Ritorna il nome dell'utente
   *
   * @return una stringa contenente il nome dell'utente
   *
   */
  public String getNome() {
    return nome;
  }

  /**
   * Imposta il nome dell'utente
   *
   * @param nome il nome dell'utente
   */
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Ritorna il cognome dell'utente
   *
   * @return ritorna il cognome dell'utente
   */
  public String getCognome() {
    return cognome;
  }

  /**
   * Imposta il cognome dell'utente
   *
   * @param cognome il cognome dell'utente
   */
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   * Ritorna la login dell'utente
   *
   * @return la login dell'utente
   */
  public final String getLogin() {
    return login;
  }

  /**
   * Imposta la login dell'utente
   *
   * @param login la stringa con la login
   */
  public final void setLogin(String login) {
    this.login = login;
  }

  /**
   * Ritorna l'email associata all'utente
   *
   * @return una stringa contenente l'email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Imposta l'email dell'utente
   *
   * @param email l'email dell'utente
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Ritorna la password crittata
   *
   * @return la password crittata
   */
  public String getPassword() {
    return password;
  }

  /**
   * Imposta l'ID univoco dell'utente
   *
   * @param id l'ID dell'utente
   *
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Imposta la password crittata dell'utente
   *
   * @param password la password crittata dell'utente
   *
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Ritorna la data di iscrizione dell'utente nella piattaforma
   *
   * @return data di iscrizione
   */
  public long getDataIscrizione() {
    return dataIscrizione;
  }

  /**
   * Imposta la data di iscrizione dell'utente nella piattaforma.
   *
   * @param dataIscrizione il timestamp della data di iscrizione dell'utente
   */
  public void setDataIscrizione(long dataIscrizione) {
    this.dataIscrizione = dataIscrizione;
  }

}
