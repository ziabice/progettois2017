/* 
 * The MIT License
 *
 * Copyright 2017 Luca Gambetta.
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
/**
 * Author:  Luca Gambetta
 * Created: 27-dic-2017
 */
-- DB Schema per Unitirocinio

SET FOREIGN_KEY_CHECKS=0;

-- Tabella base per gli utenti
DROP TABLE IF EXISTS utenti;
CREATE TABLE utenti (
  id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  login VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  cognome VARCHAR(100) NOT NULL,
  tipo_utente ENUM('studente','operatore','tutor_accademico','tutor_aziendale','azienda') NOT NULL,
  data_iscrizione DATETIME NOT NULL
);

-- Tabella con le info aggiuntive per gli studenti
DROP TABLE IF EXISTS studenti;
CREATE TABLE studenti (
  studente_id INTEGER UNSIGNED PRIMARY KEY REFERENCES utenti(id) ON DELETE CASCADE ON UPDATE CASCADE,
  matricola VARCHAR(15) UNIQUE NOT NULL
);

-- Tabella contenente le info aggiuntive per gli operatori dell'ufficio tirocinio
DROP TABLE IF EXISTS operatori_ufficio_tirocinio;
CREATE TABLE operatori_ufficio_tirocinio (
  operatore_id INTEGER UNSIGNED PRIMARY KEY REFERENCES utenti(id) ON DELETE CASCADE ON UPDATE CASCADE,
  ruolo ENUM('operatore','presidente_cons_did','direttore_dip') NOT NULL,
  codice_fiscale CHAR(16) UNIQUE NOT NULL
);

-- Tabella contenente le info aggiuntive delle aziende
DROP TABLE IF EXISTS aziende;
CREATE TABLE aziende (
  azienda_id INTEGER UNSIGNED PRIMARY KEY REFERENCES utenti(id) ON DELETE CASCADE ON UPDATE CASCADE,
  partita_iva VARCHAR(30) NOT NULL UNIQUE,
  nome_rappresentante VARCHAR(100) NOT NULL,
  cognome_rappresentante VARCHAR(100) NOT NULL,
  stato_convenzione ENUM('attiva','non_attiva') NOT NULL,
  rif_convenzione VARCHAR(30) NOT NULL,
  data_convenzione DATE NOT NULL,
  indirizzo_sede_legale VARCHAR(100) NOT NULL,
  citta_sede_legale VARCHAR(60) NOT NULL
);

-- Tabella contenente le info aggiuntive per i tutor aziendali
DROP TABLE IF EXISTS tutor_aziendali;
CREATE TABLE tutor_aziendali (
  tutor_id INTEGER UNSIGNED PRIMARY KEY REFERENCES utenti(id) ON DELETE CASCADE ON UPDATE CASCADE,
  azienda_id INTEGER UNSIGNED REFERENCES aziende(azienda_id) ON DELETE CASCADE ON UPDATE CASCADE,
  codice_fiscale CHAR(16) UNIQUE NOT NULL,
  telefono VARCHAR(30) NOT NULL
);

-- Tabella contenente le info aggiuntive per i tutor accademici
DROP TABLE IF EXISTS tutor_accademici;
CREATE TABLE tutor_accademici (
  tutor_id INTEGER UNSIGNED PRIMARY KEY REFERENCES utenti(id) ON DELETE CASCADE ON UPDATE CASCADE,
  codice_fiscale CHAR(16) UNIQUE NOT NULL
);

SET FOREIGN_KEY_CHECKS=1;