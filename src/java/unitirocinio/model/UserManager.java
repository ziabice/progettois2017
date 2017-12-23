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

import java.sql.ResultSet;

/**
 * Gestisce la persistenza degli oggetti di tipo User.
 * 
 * É un singleton.
 *
 * @author Luca Gambetta
 */
public class UserManager {

  /**
   * Il singleton per l'user manager
   */
  private static UserManager singleton;
  
  // Usato per sincronizzare i thread
  private static final Object CLASSLOCK = UserManager.class;

  /**
   * Istanzia l'user manager.
   * 
   * Prima dell'istanziazione occorre impostare una connessione al database
   * 
   * @throws Exception in caso di errore nella creazione
   */
  private UserManager() {
  }

  /**
   * Ritorna il singleton per il gestore degli utenti
   *
   * @return il singleton del gestore degli utenti
   */
  public static UserManager getInstance() {
    synchronized (CLASSLOCK) {
      if (singleton == null) {
        singleton = new UserManager();
      }

      return singleton;
    }
  }

  /**
   * Salva un utente nel database, aggiornandone l'ID univoco.
   *
   * @param user l'utente da salvare
   * @return l'utente salvato
   */
  public User save(User user) {
    return null;
  }

  /**
   * Estrae un utente dal database in base al suo ID univoco
   *
   * @param id l'ID dell'utente
   * @return l'eventuale utente trovato o null se non ha trovato niente
   */
  public User find(Long id) {
    return null;
  }

  /**
   * Estra un utente dal database usando la sua login
   *
   * @param login la stringa di login da cercare
   * @return l'eventuale utente trovato o null se non ha trovato niente
   */
  public User findByLogin(String login) {
    return null;
  }

  /**
   * Data una password in chiaro, ritorna la sua versione crittografata.
   *
   * @param plainPassword la password in chiaro
   * @return la versione crittografata della password in chiaro
   * 
   */
  public String encryptPassword(String plainPassword) {
    return plainPassword;
  }
  
  /**
   * Metodo factory che, dato un Resultset, ritorna l'istanza di User approriata
   * 
   * @param res il resultset da cui creare l'istanza
   * @return una sottoclasse di User o null se non è stato possibile costruire l'oggetto
   */
  protected User userFactory(ResultSet res) {
    return null;
  }
}
