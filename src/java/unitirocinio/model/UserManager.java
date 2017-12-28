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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
   * Connessione al database
   */
  private Connection conn;

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
  
  public static UserManager getInstance(Connection conn) {
    synchronized (CLASSLOCK) {
      if (singleton == null) {
        singleton = new UserManager();
      }
      
      if (conn != null) {
        singleton.setConnection(conn);
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
  public User save(User user) throws Exception, SQLException {
    if (conn == null) {
      throw new Exception("No DB connection!");
    }
    
    String tipo_utente;
    if (user instanceof Studente) {
      tipo_utente = "studente";
    } else if(user instanceof OperatoreUfficioTirocinio) {
      tipo_utente = "operatore";
    } else if(user instanceof Azienda) {
      tipo_utente = "azienda";
    } else if(user instanceof TutorAziendale) {
      tipo_utente = "tutor_aziendale";
    } else if(user instanceof TutorAccademico) {
      tipo_utente = "tutor_accademico";
    } else {
      throw new Exception("Invalid object");
    }
    
    conn.setAutoCommit(false);
    try {
      
      // Salva prima le informazioni cumuni
      PreparedStatement stmt = conn.prepareStatement(
              "INSERT INTO utenti (data_iscrizione, login, password, email, nome, cognome, tipo_utente) VALUES ( NOW(), ?, ?, ?, ?, ?, ? )",
              Statement.RETURN_GENERATED_KEYS
              );
      stmt.setString(1, user.getLogin());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getNome());
      stmt.setString(5, user.getCognome());
      stmt.setString(6, tipo_utente);
              
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        // ROLLBACK
        conn.rollback();
      }
      
      // Recupera l'ID e lo inserisce nell'istanza
      ResultSet genkeys = stmt.getGeneratedKeys();
      if (genkeys.next()) {
        user.setId(genkeys.getLong(1));
      } else {
        // ROLLBACK
        conn.rollback();
      }
      
      // Aggiunge le informazioni per i sottotipi
      PreparedStatement stmt2;
      
      if (user instanceof Studente) {
        stmt2 = conn.prepareStatement("INSERT INTO studenti (studente_id, matricola) VALUES (?,?)");
        stmt2.setLong(1, user.getId() );
        stmt2.setString(2, ((Studente) user).getMatricola());
      } else if(user instanceof OperatoreUfficioTirocinio) {
        stmt2 = conn.prepareStatement("INSERT INTO studenti (operatore_id, codice_fiscale, ruolo) VALUES (?,?,?)");
        stmt2.setLong(1, user.getId() );
        stmt2.setString(2, ((OperatoreUfficioTirocinio) user).getCodiceFiscale());
        switch( ((OperatoreUfficioTirocinio) user).getRuolo() ) {
          case OPERATORE:
            stmt2.setString(3, "operatore");
            break;
          case DIRETTORE_DIPARTIMENTO:
            stmt2.setString(3, "direttore_dip");
            break;
          case PRESIDENTE_CONSIGLIO_DIDATTICO:
            stmt2.setString(3, "presidente_cons_did");
            break;
        } 
      } else if(user instanceof Azienda) {
        stmt2 = conn.prepareStatement(
                "INSERT INTO aziende (azienda_id, partita_iva, nome_rappresentante, cognome_rappresentante, stato_convenzione," +
                        "rif_convenzione, data_convenzione, indirizzo_sede_legale, citta_sede_legale" +
                ") VALUES (?,?,?,?,?,?,CAST(FROM_UNIXTIME(?) AS DATE),?,?)"
        );
        stmt2.setLong(1, user.getId());
        stmt2.setString(2, ((Azienda) user).getPartitaIva());
        stmt2.setString(3, ((Azienda) user).getNomeRappresentanteLegale());
        stmt2.setString(4, ((Azienda) user).getCognomeRappresentanteLegale());
        
        switch(((Azienda) user).getStatoConvenzione()) {
          case ATTIVA: 
            stmt2.setString(5, "attiva");
            break;
          case NON_ATTIVA: 
            stmt2.setString(5, "non_attiva");
            break;
        }
        stmt2.setString(6, ((Azienda) user).getRifConvenzione());
        stmt2.setLong(7, ((Azienda) user).getDataConvenzione());
        
        stmt2.setString(8, ((Azienda) user).getIndirizzoSedeLegale());
        stmt2.setString(9, ((Azienda) user).getCittaSedeLegale());
        
      } else if(user instanceof TutorAziendale) {
        stmt2 = conn.prepareStatement("INSERT INTO tutor_aziendali (tutor_id, azienda_id, codice_fiscale, telefono) VALUES (?,?,?,?) ");
        stmt2.setLong(1, user.getId());
        stmt2.setLong(2, ((TutorAziendale) user).getIdAzienda());
        stmt2.setString(3, ((TutorAziendale) user).getCodiceFiscale());
        stmt2.setString(4, ((TutorAziendale) user).getTelefono());
      } else if(user instanceof TutorAccademico) {
        stmt2 = conn.prepareStatement("INSERT INTO tutor_accademici (tutor_id, codice_fiscale) VALUES (?,?)");
        stmt2.setLong(1, user.getId());
        stmt2.setString(2, ((TutorAccademico) user).getCodiceFiscale());
      } else {
        // Messo solo per evitare che il compilatore si lamenti di 
        // variabile non inizializzata
        throw new Exception("Invalid object");
      }
      
      stmt2.execute();
      conn.commit();
      
      
    }
    catch(SQLException e) {
    }
    
    return user;
  }

  /**
   * Estrae un utente dal database in base al suo ID univoco
   *
   * @param id l'ID dell'utente
   * @return l'eventuale utente trovato o null se non ha trovato niente
   */
  public User find(Long id) throws SQLException,Exception {
    if (id == null) {
      throw new Exception("Invalid ID");
    }
    
    if (id <= 0) {
      throw new Exception("Invalid ID");
    }
    
    PreparedStatement st = conn.prepareStatement("SELECT * FROM utenti WHERE id = ?");
    st.setLong(1, id);
    ResultSet res = st.executeQuery();
    return userFactory(res);
  }

  /**
   * Estra un utente dal database usando la sua login
   *
   * @param login la stringa di login da cercare
   * @return l'eventuale utente trovato o null se non ha trovato niente
   */
  public User findByLogin(String login) throws SQLException, Exception {
    PreparedStatement st = conn.prepareStatement("SELECT * FROM utenti WHERE login = ? LIMIT 1");
    st.setString(1, login);
    ResultSet res = st.executeQuery();
    return userFactory(res);
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
   * @throws java.sql.SQLException
   */
  protected User userFactory(ResultSet res) throws SQLException, Exception {
    User user = null;
    if(res.next()) {
      String tipo_utente = res.getString("tipo_utente");
      long id = res.getLong("id");
      PreparedStatement st;
      ResultSet res2;
      
      switch(tipo_utente) {
        case "studente": 
          st = conn.prepareStatement("SELECT * from studenti where studente_id = ?");
          st.setLong(1, id);
          res2 = st.executeQuery();
          
          user = new Studente(res.getString("login"), res.getString("email"), res2.getString("matricola"), id );
        
          break;
        case "operatore":
          st = conn.prepareStatement("SELECT * from operatori_ufficio_tirocinio where operatore_id = ?");
          st.setLong(1, id);
          res2 = st.executeQuery();
          OperatoreUfficioTirocinio.Ruolo r;
          switch(res.getString("ruolo")) {
            case "presidente_cons_did": r = OperatoreUfficioTirocinio.Ruolo.PRESIDENTE_CONSIGLIO_DIDATTICO;break;
            case "direttore_dip": r = OperatoreUfficioTirocinio.Ruolo.DIRETTORE_DIPARTIMENTO; break;
            default:
              r = OperatoreUfficioTirocinio.Ruolo.OPERATORE;
          }
          
          user = new OperatoreUfficioTirocinio(res.getString("login"), res.getString("email"), res2.getString("codice_fiscale"), r, id );
          break;
        case "tutor_accademico":
          st = conn.prepareStatement("SELECT * from tutor_accademici where tutor_id = ?");
          st.setLong(1, id);
          res2 = st.executeQuery();
          
          user = new TutorAccademico(res.getString("login"), res.getString("email"), res2.getString("codice_fiscale"), id );
          
          break;
        case "tutor_aziendale":
          st = conn.prepareStatement("SELECT * from tutor_aziendali where tutor_id = ?");
          st.setLong(1, id);
          res2 = st.executeQuery();
          
          user = new TutorAziendale(res.getString("login"), res.getString("email"), res2.getString("codice_fiscale"), res2.getLong("azienda_id"), id );
          
          break;
        case "azienda":
          st = conn.prepareStatement("SELECT * from aziende where azienda_id = ?");
          st.setLong(1, id);
          res2 = st.executeQuery();
          
          user = new Azienda(res.getString("login"), res.getString("email"), res2.getString("partita_iva"), id );
          /*
  data_convezione DATE NOT NULL,
          */
          ((Azienda) user).setCittaSedeLegale(res2.getString("citta_sede_legale"));
          ((Azienda) user).setCognomeRappresentanteLegale(res2.getString("cognome_reppresentante"));
          ((Azienda) user).setNomeRappresentanteLegale(res2.getString("nome_reppresentante"));
          ((Azienda) user).setRifConvenzione(res2.getString("rif_convenzione"));
          ((Azienda) user).setIndirizzoSedeLegale(res2.getString("indirizzo_sede_legale"));
          String stato_convenzione = res2.getString("stato_convenzione");
          ((Azienda) user).setStatoConvenzione(  stato_convenzione.compareTo("attiva") == 0 ?  Azienda.StatoConvenzione.ATTIVA : Azienda.StatoConvenzione.NON_ATTIVA );
          
          break;
      }
      
      user.setNome(res.getString("nome"));
      user.setCognome(res.getString("cognome"));
      user.setPassword(res.getString("password"));
    
    }
    return user;
  }
  
  /**
   * Imposta una connessione al database
   * @param conn la connessione al database
   */
  public void setConnection(Connection conn) {
    this.conn = conn;
  }
  
  /**
   * Ritorna la connessione correnta al database
   * 
   * @return Connection la connessione al database
   */
  public Connection getConnection() {
    return conn;
  }
  
}
