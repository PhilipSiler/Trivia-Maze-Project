package database;

import java.sql.*;
import java.util.*;


/* DatabaseConnection.java
 * Author: Damien Rodriguez
 * Revision: 4
 * Rev. Author: Damien Rodriguez
 * Description: Database connection class. Singleton pattern implemented.
 * Has functionality to execute SQL queries within the SQLite database
 */
public class DatabaseConnection {

    private static DatabaseConnection SINGLE_INSTANCE = null;
    private final String DB_CONNECTION = "jdbc:sqlite:trivia.db";
    private final String MULTIPLECHOICE_COUNT_QUERY = "SELECT COUNT(*) AS rowCount FROM questions WHERE wrongAnswerOne IS NOT NULL";
    private final String TRUEFALSE_COUNT_QUERY = "SELECT COUNT(*) AS rowCount FROM questions WHERE length(answer) == 1 AND wrongAnswerOne IS NULL";
    private final String SHORTANSWER_COUNT_QUERY = "SELECT COUNT(*) AS rowCount FROM questions WHERE length(answer) != 1 AND wrongAnswerOne IS NULL";
    private final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) AS rowCount FROM questions";
    private Connection c;

    private int trueFalseRecordCount;
    private int multipleChoiceRecordCount;
    private int shortAnswerChoiceRecordCount;
    private int totalRecordCount;

    private Hashtable<String, Question> questionLookUp = new Hashtable<>();
    private Set<String> keyset;
    private int hashTableCursor;



    ///Returns: nothing, it sets up the connection to our global Connection variable
    ///Throws:
    ///ClassNotFoundException: Throws the exception when the driver .jar file isn't present
    ///SQLException: Throws exception when SQL database connection fails
    private DatabaseConnection() throws Exception {
        connectionSetUp();
        setMultipleChoiceRecordCount(getRecordCount(MULTIPLECHOICE_COUNT_QUERY));
        setTrueFalseRecordCount(getRecordCount(TRUEFALSE_COUNT_QUERY));
        setShortAnswerChoiceRecordCount(getRecordCount(SHORTANSWER_COUNT_QUERY));
        setTotalRecordCount(getRecordCount(TOTAL_COUNT_QUERY));
        setQuestionLookUp();
        setKeyset();
        setHashTableCursor(this.questionLookUp.size()-1); //when questions are loaded into hashtable, all of the hard questions are placed at the top, so we just start from the bottom

        if(this.totalRecordCount != (this.multipleChoiceRecordCount + this.trueFalseRecordCount + this.shortAnswerChoiceRecordCount))
            throw new SQLException("SQL statements aren't getting the seperate categories that you are expecting.");

    }

    public Set<String> getKeyset() {
        return keyset;
    }

    //Keyset was created as a list to be able to handle random pulls from the given collection.
    public void setKeyset() {
        this.keyset = this.questionLookUp.keySet();
    }

    //gets num of records for question types based on predefined
    //sql queries above
    private int getRecordCount(final String sql)  {

        int count = 0;
        try {
            Statement statement = this.c.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            count = rs.getInt("rowCount");
            rs.close();

        } catch(Exception e) {
            System.out.println("ERROR in getRecordCount");
        }

        return count;
    }


    public int getHashTableCursor() {
        return hashTableCursor;
    }


    public void setHashTableCursor(int hashTableCursor) {
        this.hashTableCursor = hashTableCursor;
    }


    public static DatabaseConnection getInstance() {
        try {
            if(SINGLE_INSTANCE == null) {
                synchronized (DatabaseConnection.class) {
                    if(SINGLE_INSTANCE == null)
                        SINGLE_INSTANCE = new DatabaseConnection();
                }
            }
            return SINGLE_INSTANCE;
        } catch(Exception e) {
            System.out.println(e);
        }
        return null;

    }


    private void connectionSetUp() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(DB_CONNECTION);

        } catch (Exception e) {
            System.out.println("Problem in connectionSetUp");
            System.out.println(e);
        }
    }


    public void addQuestion(final Question q) {
        String sql = "insert into questions values(";

        sql = sql + "'"+ q.getId() + "', '" + q.getQuestion() + "', '" + q.getAnswer() + "', '" + q.getHint() + "', '" + q.getWrongAnswerOne() + "', '" + q.getWrongAnswerTwo() + "', '" + q.getWrongAnswerThree() + "')";


        insertQuery(sql);
    }


    public void updateRecordCount(final char token) {
        if(token == 't')
            this.trueFalseRecordCount++;
        else if(token == 'm')
            this.multipleChoiceRecordCount++;
        else if(token == 's')
            this.shortAnswerChoiceRecordCount++;
        else
            throw new IllegalArgumentException("token sent to update the record count is invalid.");

        this.totalRecordCount++; //there might be a problem here if we get the invalid token exception.
    }


    private void insertQuery(final String query) {
        try {
            Statement myStm = this.c.createStatement();
            myStm.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("ERROR HAPPENED AT insertQuery");
            System.out.println(e);
            System.out.println(query);
        }

    }


    //to be deleted later
    public void clearQuestionData() {
        try {
            Statement temp = this.c.createStatement();
            temp.execute("DELETE FROM questions");

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void closeConnection() {
        try {
            this.c.close();
        } catch(Exception e) {
            System.out.println("ERROR HAPPENED in closeConnection");
            System.out.println(e);
        }

    }


    public int getTrueFalseRecordCount() {
        return trueFalseRecordCount;
    }


    public void setTrueFalseRecordCount(int trueFalseRecordCount) {
        this.trueFalseRecordCount = trueFalseRecordCount;
    }


    public int getMultipleChoiceRecordCount() {
        return multipleChoiceRecordCount;
    }


    public void setMultipleChoiceRecordCount(int multipleChoiceRecordCount) {
        this.multipleChoiceRecordCount = multipleChoiceRecordCount;
    }


    public int getShortAnswerChoiceRecordCount() {
        return shortAnswerChoiceRecordCount;
    }


    public void setShortAnswerChoiceRecordCount(int shortAnswerChoiceRecordCount) {
        this.shortAnswerChoiceRecordCount = shortAnswerChoiceRecordCount;
    }


    public int getTotalRecordCount() {
        return totalRecordCount;
    }


    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }


    public boolean recordExists(final String questionID) {

        try {
            Statement myStm = this.c.createStatement();

            String sql = "SELECT * FROM questions WHERE questionID = ";
            String addedQuotes = "\'" + questionID + "\'";
            sql = sql + addedQuotes;



            ResultSet rs = myStm.executeQuery(sql);
            //need to do some check with the result set here

            return true;

        }
        catch(Exception e) {
            System.out.println("question does not exist.");
            return false;
        }


    }


    public Hashtable<String, Question> getQuestionLookUp() {
        return questionLookUp;
    }


    public void setQuestionLookUp() {
        Statement myStm = null;
        ResultSet set = null;
        try {
            myStm = this.c.createStatement();
            set = myStm.executeQuery("SELECT * FROM questions");

            while(set.next()) {
                this.questionLookUp.put(set.getString("questionID"), new Question(set));
            }
            set.close();

        } catch(Exception e) {
            System.out.println("Error happened while filling Hashtable");
            System.out.println(e);
        }
    }


    public Question pullQuestion() {
        Object[] myArray = this.keyset.toArray();
        Question q = this.questionLookUp.get(myArray[this.hashTableCursor]);
        this.hashTableCursor--;

        if(this.hashTableCursor == -1)
            this.hashTableCursor = this.questionLookUp.size() - 1;

        return q;
    }


    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        //KEY SET LIST IS THE PROBLEM
        db.setKeyset();

    }
}
