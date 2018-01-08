/*
 * The MIT License
 *
 * Copyright 2018 Luca Gambetta.
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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Esegue il testing della classe UserManager.
 * 
 * @author Luca Gambetta
 */
public class UserManagerTest {
  
  // Configurazione del DB
  final static String DB_DRIVER = "com.mysql.jdbc.Driver";
  final static String DB_URL = "jdbc:mysql://localhost/unitirocinio?user=root&password=";
  
  static Connection conn;
  
  public UserManagerTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
    // Prima di eseguire i test si collega al database
    try{
      Class.forName(DB_DRIVER);
      conn = DriverManager.getConnection(DB_URL);
    }
    catch(Exception ex) {
      System.err.println(ex);
    }
  }
  
  @AfterClass
  public static void tearDownClass() {
    // Chiude la connessione al database
    try{
      if (conn != null) {
        conn.close();
      }
      
    }
    catch(Exception ex) {
      System.err.println(ex);
    }
  }
  
  @Before
  public void setUp() {
    // Ripulisce il database prima di ogni test
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("DELETE FROM tutor_accademici");
      stmt.executeUpdate("DELETE FROM tutor_aziendali");
      stmt.executeUpdate("DELETE FROM aziende");
      stmt.executeUpdate("DELETE FROM operatori_ufficio_tirocinio");
      stmt.executeUpdate("DELETE FROM studenti");
      stmt.executeUpdate("DELETE FROM utenti");
    }
    catch(SQLException ex) {
    }
  }
  
  @After
  public void tearDown() {
    // Ripulisce il database dopo ogni test
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("DELETE FROM tutor_accademici");
      stmt.executeUpdate("DELETE FROM tutor_aziendali");
      stmt.executeUpdate("DELETE FROM aziende");
      stmt.executeUpdate("DELETE FROM operatori_ufficio_tirocinio");
      stmt.executeUpdate("DELETE FROM studenti");
      stmt.executeUpdate("DELETE FROM utenti");
    }
    catch(SQLException ex) {
    }
  }

  /**
   * Test of getInstance method, of class UserManager.
   */
  @Test
  public void testGetInstance_0args() {
    System.out.println("getInstance");
    UserManager result = UserManager.getInstance();
    assertNotNull(result);
  }

  /**
   * Test of getInstance method, of class UserManager.
   */
  @Test
  public void testGetInstance_Connection() {
    System.out.println("getInstance");
    UserManager result = UserManager.getInstance(conn);
    assertNotNull(result);
  }
  
  /**
   * Verifica che un utente venga correttamente salvato nel database
   * 
   * @param user l'utente da salvare
   * @param tipo_utente tipo di utente 
   * @param tabelle_piene tabelle che devono contenere un solo valore
   * @param tabelle_vuote tabelle che devono essere vuote
   * @throws Exception 
   */
  public void eseguiSalvataggio(User user, String tipo_utente, String[] tabelle_piene, String[] tabelle_vuote) throws Exception {
    UserManager instance = UserManager.getInstance(conn);
    
    // Salva l'utente
    System.out.println("salvataggio");
    User utente = null;
    utente = instance.save(user);
    
    // Verifica che l'ID sia stato assegnato
    System.out.println("verifica ID");
    long id = utente.getId();
    assertNotEquals(0L, id);
    
    // Verifica che sia stato scritto il giusto tipo di utente
    System.out.println("verifica tipo utente");
    PreparedStatement st = conn.prepareStatement("SELECT tipo_utente FROM utenti WHERE id = ?");
    st.setLong(1, id);
    ResultSet rs1 = st.executeQuery();
    if (rs1.next()) {
      assertEquals(tipo_utente, rs1.getString(1));
    } else {
      fail("Utente non trovato");
    }
    rs1.close();
    
    // Controlla che non ci siano dati nelle altre tabelle
    System.out.println("controllo dati");
    Statement stmt = conn.createStatement();
    ResultSet rs = null;
    
    for (String t : tabelle_piene) {
      System.out.println("controllo dati su: " + t);
      rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM " + t);
      rs.next();
      assertEquals(1L, rs.getLong(1));
      rs.close();
    }
    
    for (String t : tabelle_vuote) {
      System.out.println("controllo dati su: " + t);
      rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM " + t);
      rs.next();
      assertEquals(0L, rs.getLong(1));
      rs.close();
    }
  }
  
  @Test
  public void testSaveGuest() {
    
    System.out.println("saveGuest");
    User usr = new Guest();
    try {
      UserManager.getInstance(conn).save(usr);
    }
    catch(Exception ex) {
      assertEquals("Invalid object", ex.getMessage());
    }
  }

  /**
   * Verifica che venga creato correttamente uno studente
   * @throws java.lang.Exception
   */
  @Test
  public void testSaveStudente() throws Exception {
    System.out.println("saveStudente");
    
    UserManager instance = UserManager.getInstance(conn);
    User user = new Studente("foo@bar.com", "foo@bar.com", "123456789", 0L);
    user.setPassword( instance.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    
    String[] tabelle_vuote = { "operatori_ufficio_tirocinio", "aziende", "tutor_aziendali", "tutor_accademici"};
    String[] tabelle_piene = {"studenti", "utenti"};
    
    eseguiSalvataggio(user, "studente", tabelle_piene, tabelle_vuote );
  }
  
  /**
   * Verifica che venga creata corrattamente un'azienda la cui convenzione è attiva
   * 
   * @throws Exception 
   */
  @Test
  public void testSaveAzienda1() throws Exception {
    System.out.println("saveAzienda1");
    
    UserManager instance = UserManager.getInstance(conn);
    User user = new Azienda("foo@bar.com", "foo@bar.com", "123456789", 0L);
    user.setPassword( instance.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    ((Azienda) user).setCittaSedeLegale("Città sede legale");
    ((Azienda) user).setCognomeRappresentanteLegale("Cognome rappresentante");
    ((Azienda) user).setNomeRappresentanteLegale("Nome rappresentante");
    ((Azienda) user).setDataConvenzione(0);
    ((Azienda) user).setIndirizzoSedeLegale("Indirizzo sede legale");
    ((Azienda) user).setRifConvenzione("1234");
    ((Azienda) user).setStatoConvenzione(Azienda.StatoConvenzione.ATTIVA);
    
    String[] tabelle_vuote = { "studenti", "operatori_ufficio_tirocinio",  "tutor_aziendali", "tutor_accademici"};
    String[] tabelle_piene = { "aziende","utenti"};
    
    eseguiSalvataggio(user, "azienda", tabelle_piene, tabelle_vuote );
  }
  
  /**
   * Verifica che venga creata corrattamente un'azienda la cui convenzione non è attiva
   * 
   * @throws Exception 
   */
  @Test
  public void testSaveAzienda2() throws Exception {
    System.out.println("saveAzienda2");
    
    UserManager instance = UserManager.getInstance(conn);
    User user = new Azienda("foo@bar.com", "foo@bar.com", "123456789", 0L);
    user.setPassword( instance.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    ((Azienda) user).setCittaSedeLegale("Città sede legale");
    ((Azienda) user).setCognomeRappresentanteLegale("Cognome rappresentante");
    ((Azienda) user).setNomeRappresentanteLegale("Nome rappresentante");
    ((Azienda) user).setDataConvenzione(0);
    ((Azienda) user).setIndirizzoSedeLegale("Indirizzo sede legale");
    ((Azienda) user).setRifConvenzione("1234");
    ((Azienda) user).setStatoConvenzione(Azienda.StatoConvenzione.NON_ATTIVA);
    
    String[] tabelle_vuote = { "studenti", "operatori_ufficio_tirocinio",  "tutor_aziendali", "tutor_accademici"};
    String[] tabelle_piene = { "aziende","utenti"};
    
    eseguiSalvataggio(user, "azienda", tabelle_piene, tabelle_vuote );
  }
  
  /**
   * Verifica che venga correttamente salvato un tutor accademico
   * 
   * @throws Exception 
   */
  @Test
  public void testSaveTutorAccademico() throws Exception {
    System.out.println("saveTutorAccademico");
    
    UserManager instance = UserManager.getInstance(conn);
    User user = new TutorAccademico("foo@bar.com", "foo@bar.com", "123456789", 0L);
    user.setPassword( instance.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    
    String[] tabelle_vuote = { "operatori_ufficio_tirocinio", "aziende", "tutor_aziendali", "studenti"};
    String[] tabelle_piene = {"tutor_accademici", "utenti"};
    
    eseguiSalvataggio(user, "tutor_accademico", tabelle_piene, tabelle_vuote );
  }
  
  /**
   * Verifica che venga correttamente salvato un tutor aziendale
   * 
   * @throws Exception 
   */
  @Test
  public void testSaveTutorAziendale() throws Exception {
    System.out.println("saveTutorAziendale");
    
    UserManager instance = UserManager.getInstance(conn);
    
    // Salva prima l'azienda
    System.out.println("crea azienda");
    Azienda azienda = new Azienda("foo@bar.com", "foo@bar.com", "123456789", 0L);
    azienda.setPassword( instance.encryptPassword("password") );
    azienda.setNome("John");
    azienda.setCognome("Doe");
    azienda.setCittaSedeLegale("Città sede legale");
    azienda.setCognomeRappresentanteLegale("Cognome rappresentante");
    azienda.setNomeRappresentanteLegale("Nome rappresentante");
    azienda.setDataConvenzione(0);
    azienda.setIndirizzoSedeLegale("Indirizzo sede legale");
    azienda.setRifConvenzione("1234");
    azienda.setStatoConvenzione(Azienda.StatoConvenzione.ATTIVA);
    
    User az = instance.save(azienda);
    
    // Salva il tutor aziendale
    User user = new TutorAziendale("tutor@bar.com", "tutor@bar.com", "123456789", az.getId(), 0L);
    user.setPassword( instance.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    ((TutorAziendale) user).setTelefono("12345678");
    
    String[] tabelle_vuote = { "operatori_ufficio_tirocinio", "studenti"};
    String[] tabelle_piene = { "aziende", "tutor_aziendali" };
    
    eseguiSalvataggio(user, "tutor_aziendale", tabelle_piene, tabelle_vuote );
    
    // Verifica che ci siano due utenti
    System.out.println("Conta utenti");
    Statement stmt = conn.createStatement();
    
    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM utenti");
    assertTrue( rs.next() );
    assertEquals(2L, rs.getLong(1));
    rs.close();
    
  }

  /**
   * Metodo di supporto per il testing del salvataggio di un operatore dell'ufficio tirocinio.
   * Esegue effettivamente il testing.
   * 
   * @param ruolo_str stringa col ruolo
   * @param ruolo ruolo dell'operatore
   * @throws Exception 
   */
  public void testaSalvataggioOperatoreUfficioTirocinio(String ruolo_str, OperatoreUfficioTirocinio.Ruolo ruolo) throws Exception {
    UserManager instance = UserManager.getInstance(conn);
    User user = new OperatoreUfficioTirocinio("foo@bar.com", "foo@bar.com", "123456789", ruolo, 0L);
    user.setPassword( instance.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    
    String[] tabelle_vuote = { "tutor_accademici", "aziende", "tutor_aziendali", "studenti"};
    String[] tabelle_piene = {"operatori_ufficio_tirocinio", "utenti"};
    
    eseguiSalvataggio(user, "operatore", tabelle_piene, tabelle_vuote );
    
    // Verifica che il ruolo salvato sia corretto
    System.out.println("verifica ruolo operatore");
    PreparedStatement st = conn.prepareStatement("SELECT ruolo FROM operatori_ufficio_tirocinio WHERE operatore_id = ?");
    st.setLong(1, user.getId());
    ResultSet rs = st.executeQuery();
    if (rs.next()) {
      assertEquals(rs.getString("ruolo"), ruolo_str);
    } else {
      fail("Operatore non trovato");
    }
  }
  
  /**
   * Verifica il salvataggio di un operatore dell'ufficio tirocinio
   * @throws Exception 
   */
  @Test
  public void testSaveOperatoreUfficioTirocinio() throws Exception {
    System.out.println("saveOperatoreUfficioTirocinio");
    testaSalvataggioOperatoreUfficioTirocinio("operatore", OperatoreUfficioTirocinio.Ruolo.OPERATORE);
  }
  
  /**
   * Verifica il salvataggio di un operatore dell'ufficio tirocinio di tipo direttore del dipartimento
   * @throws Exception 
   */
  @Test
  public void testSaveOperatoreUfficioTirocinio1() throws Exception {
    System.out.println("saveOperatoreUfficioTirocinio - Direttore Dipartimento");
    testaSalvataggioOperatoreUfficioTirocinio("direttore_dip", OperatoreUfficioTirocinio.Ruolo.DIRETTORE_DIPARTIMENTO);
  }

  /**
   * Verifica il salvataggio di un operatore dell'ufficio tirocinio di tipo presidente del consiglio didattico
   * @throws Exception 
   */
  @Test
  public void testSaveOperatoreUfficioTirocinio2() throws Exception {
    System.out.println("saveOperatoreUfficioTirocinio - Presidente consiglio didattico");
    testaSalvataggioOperatoreUfficioTirocinio("presidente_cons_did", OperatoreUfficioTirocinio.Ruolo.PRESIDENTE_CONSIGLIO_DIDATTICO);
  }
  
  /**
   * Testa il metodo find con valori null
   * @throws java.lang.Exception
   */
  @Test
  public void testFindNull() throws Exception {
    System.out.println("findNull");
    Long id = null;
    UserManager instance = UserManager.getInstance();
    boolean test_failed = true;
    
    try {
      instance.find(id);
    }
    catch(Exception ex) {
      test_failed = false;
    }
    assertFalse(test_failed);
  }
  
  /**
   * Testa il metodo find con valori di ID non validi
   * @throws java.lang.Exception
   */
  @Test
  public void testFindInvalidId() throws Exception {
    System.out.println("findInvalidId");
    Long id = -1L;
    UserManager instance = UserManager.getInstance();
    boolean test_failed = true;
    
    try {
      instance.find(id);
    }
    catch(Exception ex) {
      test_failed = false;
    }
    assertFalse(test_failed);
    
  } 
  
  /**
   * Testa il metodo find con valori di ID non validi
   * @throws java.lang.Exception
   */
  @Test
  public void testFindNothing() throws Exception {
    System.out.println("findNothing");
    Long id = 100L;
    UserManager instance = UserManager.getInstance();

    User user = instance.find(id);
    
    assertNull(user);
  }
  
  /**
   * Crea uno studente nel database e lo ritorna
   * 
   * @return lo studente creato
   * @throws Exception 
   * @throws java.sql.SQLException 
   */
  public User prepareFindStudente() throws Exception, SQLException {
    
    System.out.println("prapareFindStudente");
    UserManager um = UserManager.getInstance(conn);
    User user = new Studente("foo@bar.com", "foo@bar.com", "123456789", 0L);
    user.setPassword( um.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    
    return um.save(user);
  }
  
   /**
   * Crea un operatore nel database e lo ritorna
   * 
   * @param ruolo il ruolo dell'operatore
   * @return l'operatore creato
   * @throws Exception 
   * @throws java.sql.SQLException 
   */
  public User prepareFindOperatore(OperatoreUfficioTirocinio.Ruolo ruolo) throws Exception, SQLException {
    
    System.out.println("prapareFindOperatore");
    UserManager um = UserManager.getInstance(conn);
    
    User user = new OperatoreUfficioTirocinio("foo@bar.com", "foo@bar.com", "123456789", ruolo, 0L);
    user.setPassword( um.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    
    return um.save(user);
  } 
  
  /**
   * Crea un tutor accademico nel database e lo ritorna
   * 
   * @return il tutor accademico
   * @throws Exception 
   * @throws java.sql.SQLException 
   */
  public User prepareFindTutorAccademico() throws Exception, SQLException {
    
    System.out.println("prapareFindOperatore");
    UserManager um = UserManager.getInstance(conn);
    
    User user = new TutorAccademico("foo@bar.com", "foo@bar.com", "123456789", 0L);
    user.setPassword( um.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    
    return um.save(user);
  } 
  
  /**
   * Testa il metodo per la lettura di uno studente
   * 
   * @throws Exception 
   */
  @Test
  public void testFindStudente() throws Exception {
    
    System.out.println("findStudente");
    
    User usr_a = prepareFindStudente();
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof Studente);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((Studente) usr_a).getMatricola(), ((Studente) usr_b).getMatricola() );
  }

  /**
   * Testa il metodo per la lettura di un tutor accademico
   * 
   * @throws Exception 
   */
  @Test
  public void testFindTutorAccademico() throws Exception {
    
    System.out.println("findTutorAccademico");
    
    User usr_a = prepareFindTutorAccademico();
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof TutorAccademico);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((TutorAccademico) usr_a).getCodiceFiscale(), ((TutorAccademico) usr_b).getCodiceFiscale());
  }
  
  /**
   * Testa il metodo per la lettura di un operatore dell'ufficio tirocinio
   * 
   * @throws Exception 
   */
  @Test
  public void testFindOperatore() throws Exception {
    
    System.out.println("findOperatore");
    
    User usr_a = prepareFindOperatore(OperatoreUfficioTirocinio.Ruolo.OPERATORE);
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof OperatoreUfficioTirocinio);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((OperatoreUfficioTirocinio) usr_a).getCodiceFiscale(), ((OperatoreUfficioTirocinio) usr_b).getCodiceFiscale() );
    assertEquals( ((OperatoreUfficioTirocinio) usr_a).getRuolo(), ((OperatoreUfficioTirocinio) usr_b).getRuolo() );
  }
  
   /**
   * Testa il metodo per la lettura di un operatore dell'ufficio tirocinio col ruolo di direttore del dipartimento
   * 
   * @throws Exception 
   */
  @Test
  public void testFindOperatore2() throws Exception {
    
    System.out.println("findOperatore2 - direttore del dipartimento");
    
    User usr_a = prepareFindOperatore(OperatoreUfficioTirocinio.Ruolo.DIRETTORE_DIPARTIMENTO);
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof OperatoreUfficioTirocinio);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((OperatoreUfficioTirocinio) usr_a).getCodiceFiscale(), ((OperatoreUfficioTirocinio) usr_b).getCodiceFiscale() );
    assertEquals( ((OperatoreUfficioTirocinio) usr_a).getRuolo(), ((OperatoreUfficioTirocinio) usr_b).getRuolo() );
  }
  
  /**
   * Testa il metodo per la lettura di un operatore dell'ufficio tirocinio col ruolo di presidente del consiglio didattico
   * 
   * @throws Exception 
   */
  @Test
  public void testFindOperatore3() throws Exception {
    
    System.out.println("findOperatore3 - presidente del consiglio didattico");
    
    User usr_a = prepareFindOperatore(OperatoreUfficioTirocinio.Ruolo.PRESIDENTE_CONSIGLIO_DIDATTICO);
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof OperatoreUfficioTirocinio);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((OperatoreUfficioTirocinio) usr_a).getCodiceFiscale(), ((OperatoreUfficioTirocinio) usr_b).getCodiceFiscale() );
    assertEquals( ((OperatoreUfficioTirocinio) usr_a).getRuolo(), ((OperatoreUfficioTirocinio) usr_b).getRuolo() );
  }
  
  /**
   * Crea un'azienda nel DB e la ritorna
   * 
   * @param stato_convenzione
   * @return
   * @throws Exception
   * @throws SQLException 
   */
  public User prepareFindAzienda(Azienda.StatoConvenzione stato_convenzione) throws Exception, SQLException {
    
    System.out.println("prapareFindAzienda");
    UserManager um = UserManager.getInstance(conn);
    
    User user = new Azienda("foo@bar.com", "foo@bar.com", "123456789", 0L);
    user.setPassword( um.encryptPassword("password") );
    user.setNome("John");
    user.setCognome("Doe");
    ((Azienda) user).setCittaSedeLegale("Città sede legale");
    ((Azienda) user).setCognomeRappresentanteLegale("Cognome rappresentante");
    ((Azienda) user).setNomeRappresentanteLegale("Nome rappresentante");
    ((Azienda) user).setDataConvenzione(0);
    ((Azienda) user).setIndirizzoSedeLegale("Indirizzo sede legale");
    ((Azienda) user).setRifConvenzione("1234");
    ((Azienda) user).setStatoConvenzione(stato_convenzione);
    
    return um.save(user);
  } 

  /**
   * Testa il metodo per la lettura di una azienda con convenzione non attiva
   * 
   * @throws Exception 
   */
  @Test
  public void testFindAzienda1() throws Exception {
    
    System.out.println("findAzienda1 - convenzione non attiva");
    
    User usr_a = prepareFindAzienda(Azienda.StatoConvenzione.NON_ATTIVA);
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof Azienda);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((Azienda) usr_a).getCittaSedeLegale(), ((Azienda) usr_b).getCittaSedeLegale());
    assertEquals( ((Azienda) usr_a).getCognomeRappresentanteLegale(), ((Azienda) usr_b).getCognomeRappresentanteLegale());
    assertEquals( ((Azienda) usr_a).getNomeRappresentanteLegale(), ((Azienda) usr_b).getNomeRappresentanteLegale());
    assertEquals( ((Azienda) usr_a).getDataConvenzione(), ((Azienda) usr_b).getDataConvenzione());
    assertEquals( ((Azienda) usr_a).getIndirizzoSedeLegale(), ((Azienda) usr_b).getIndirizzoSedeLegale());
    assertEquals( ((Azienda) usr_a).getPartitaIva(), ((Azienda) usr_b).getPartitaIva());
    assertEquals( ((Azienda) usr_a).getRifConvenzione(), ((Azienda) usr_b).getRifConvenzione());
    assertEquals( ((Azienda) usr_a).getStatoConvenzione(), ((Azienda) usr_b).getStatoConvenzione());
    
  }
 
  /**
   * Testa il metodo per la lettura di una azienda con convenzione attiva
   * 
   * @throws Exception 
   */
  @Test
  public void testFindAzienda2() throws Exception {
    
    System.out.println("findAzienda2 - convenzione attiva");
    
    User usr_a = prepareFindAzienda(Azienda.StatoConvenzione.ATTIVA);
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof Azienda);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((Azienda) usr_a).getCittaSedeLegale(), ((Azienda) usr_b).getCittaSedeLegale());
    assertEquals( ((Azienda) usr_a).getCognomeRappresentanteLegale(), ((Azienda) usr_b).getCognomeRappresentanteLegale());
    assertEquals( ((Azienda) usr_a).getNomeRappresentanteLegale(), ((Azienda) usr_b).getNomeRappresentanteLegale());
    assertEquals( ((Azienda) usr_a).getDataConvenzione(), ((Azienda) usr_b).getDataConvenzione());
    assertEquals( ((Azienda) usr_a).getIndirizzoSedeLegale(), ((Azienda) usr_b).getIndirizzoSedeLegale());
    assertEquals( ((Azienda) usr_a).getPartitaIva(), ((Azienda) usr_b).getPartitaIva());
    assertEquals( ((Azienda) usr_a).getRifConvenzione(), ((Azienda) usr_b).getRifConvenzione());
    assertEquals( ((Azienda) usr_a).getStatoConvenzione(), ((Azienda) usr_b).getStatoConvenzione());
    
  }
  
  /**
   * Testa il metodo per la lettura di un tutor aziendale
   * @throws Exception
   * @throws SQLException 
   */
  @Test
  public void testTutorAziendale() throws Exception, SQLException {
    System.out.println("findTutorAziendale");
    
    
    User azienda = prepareFindAzienda(Azienda.StatoConvenzione.ATTIVA);
    assertNotNull(azienda);
    
    UserManager um = UserManager.getInstance(conn);
    User usr_a = new TutorAziendale("tutor@bar.com", "tutor@bar.com", "123456789", azienda.getId(), 0L);
    usr_a.setPassword( um.encryptPassword("password") );
    usr_a.setNome("John");
    usr_a.setCognome("Doe");
    ((TutorAziendale) usr_a).setTelefono("12345");
    usr_a = um.save(usr_a);
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).find( usr_a.getId() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof TutorAziendale);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((TutorAziendale) usr_a).getCodiceFiscale(), ((TutorAziendale) usr_b).getCodiceFiscale());
    assertEquals( ((TutorAziendale) usr_a).getIdAzienda(), ((TutorAziendale) usr_b).getIdAzienda());
    assertEquals( ((TutorAziendale) usr_a).getTelefono(), ((TutorAziendale) usr_b).getTelefono());
    assertEquals( azienda.getId(), ((TutorAziendale) usr_b).getIdAzienda() );
  }

  /**
   * Testa il metodo findByLogin con un oggetto esistente
   */
  @Test
  public void testFindByLogin() throws Exception {
    System.out.println("findByLogin");
    
    User usr_a = prepareFindStudente();
    assertNotNull(usr_a);
    
    System.out.println("finding");
    User usr_b = UserManager.getInstance(conn).findByLogin( usr_a.getLogin() );
    assertNotNull(usr_b);
    
    System.out.println("checking instance");
    assertTrue( usr_b instanceof Studente);
    
    System.out.println("checking ID");
    assertEquals(usr_a.getId(), usr_b.getId());
    
    System.out.println("checking values");
    assertEquals(usr_a.getLogin(), usr_b.getLogin());
    assertEquals(usr_a.getEmail(), usr_b.getEmail());
    assertEquals(usr_a.getNome(), usr_b.getNome());
    assertEquals(usr_a.getCognome(), usr_b.getCognome());
    assertEquals( ((Studente) usr_a).getMatricola(), ((Studente) usr_b).getMatricola() );
    
  }
  
  /**
   * Testa il metodo findByLogin con una login nulla
   */
  @Test
  public void testFindByLoginNull() throws Exception {
    System.out.println("findByLoginNull");
    
    boolean error = false;
    
    try {
      User usr_b = UserManager.getInstance(conn).findByLogin( null );
    }
    catch(NullPointerException ex) {
      error = true;
    }
    
    assertTrue(error);
    
  }
  
  /**
   * Testa il metodo findLogin con una login inesistente
   * 
   * @throws java.lang.Exception
   */
  @Test
  public void testFindByLoginNonEx() throws Exception {
    System.out.println("findByLoginNonEx");

    UserManager instance = UserManager.getInstance();

    User user = instance.findByLogin("dontexists@example.com");
    
    assertNull(user);
  }

  /**
   * Test of encryptPassword method, of class UserManager.
   */
  @Test
  public void testEncryptPassword() {
    System.out.println("encryptPassword");
    String plainPassword = "password";
    UserManager instance = UserManager.getInstance();
    String expResult = "password";
    String result = instance.encryptPassword(plainPassword);
    assertEquals(expResult, result);
  }

  
}
