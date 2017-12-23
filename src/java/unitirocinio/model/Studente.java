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
 * Modella uno studente
 *
 * @author Luca Gambetta
 */
public class Studente extends User {

  /**
   * Matricola dello studente
   */
  private String matricola;

  /**
   * Crea una istanza di uno studente
   *
   * @param login login dell'utente
   * @param email email dell'utente
   * @param matricola matricola dello studente
   * @param id ID univoco dello studente
   */
  public Studente(String login, String email, String matricola, Long id) {
    super(login, email, id);
    this.matricola = matricola;
  }
  
  /**
   * Ritorna la matricola dello studende
   *
   * @return la matricola dello studente
   */
  public String getMatricola() {
    return matricola;
  }

  /**
   * Imposta la matricola dello studente
   *
   * @param matricola la matricola dello studente
   */
  public void setMatricola(String matricola) {
    this.matricola = matricola;
  }

}
