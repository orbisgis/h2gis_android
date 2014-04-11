package com.ogis.mobileh2gis.app; // SHOULD Be changed to something like org.ogis.* 

// Android imports, for typical application behaviour
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

// JDBC imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

// H2GIS imports
import org.h2gis.h2spatialext.CreateSpatialExtension;


// Actual code follows

public class MainActivity extends ActionBarActivity {
    
    // The jdbc connection to the database which needs to persist is a class variable
    private Connection connection;

    // INITIALIZING METHODS

    // onCreate() 
    // Is called when the application is launched in android
    // as it calls the MainActivity
    // 
    // So it is here that we make the first connection, and the initialization of the 
    // database 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // For typical android app behaviour
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Loading the jdbc driver for H2
            Class.forName("org.h2.Driver");

            // Opening the connection
            // getConnection(location,user,password)
            //
            // Here the connection is made in-memory
            // This should be changed to a already created and spatialy initialized
            // db file in a folder accessible to the android app
            this.connection = DriverManager.getConnection("jdbc:h2:mem:syntax", "sa", "sa");
            CreateSpatialExtension.initSpatialExtension(connection);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // MENU CONTEXT METHODS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId(); //gets the id of the selected action in the menu
        
        // RESET action block 
        // might be a good idea to change action_settings to something explicit
        if (id == R.id.action_settings) {
            try {

                // Closing connection if exists
                if (this.connection != null){
                    this.connection.close();
                }

                // Re-establishing connection
                // see comments in onCreate()
                // think about making an establishConnection() to be called here and in onCreate()
                Class.forName("org.h2.Driver");
                this.connection = DriverManager.getConnection("jdbc:h2:mem:syntax", "sa", "sa");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    
    }

    // SEND METHOD

    public void send(View view) {

        final TextView textViewToChange = (TextView) findViewById(R.id.textView);   // This is the display field
        final EditText textToSend = (EditText) findViewById(R.id.editText);         // This is the entry field
        
        try {

            // The query in the entry field is put in a string
            // Possible whitespace at the extremeties are trimmed
            String query = textToSend.getText().toString().trim();

            // The query is put in the display field, for backfeed
            textViewToChange.setText(query+"\n");

            // Test to see if the query expects a result set or not
            // Here naively, it is only the case if the first word is select
            String firstWord = query.split(" ", 2)[0].toUpperCase();
            if (firstWord.equals("SELECT")) { 
                // RS IS EXPECTED

                // Getting the result from our query
                // And initializating the output string
                ResultSet rs = this.connection.createStatement().executeQuery(query);
                String result="";

                // ResultSet metadata, used to see how many columns there are
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                // Loop putting and formatting the result set in a string
                while (rs.next()) {
                    for(int i=0; i<columnsNumber;i++){
                        result += rs.getString(i+1)+" ";
                    }
                    result+="\n";
                }

                // Appending the result String to the display field    
                textViewToChange.setText(textViewToChange.getText().toString()+result);

            } else { 
                // RS IS NOT EXPECTED

                // Simple query
                this.connection.createStatement().execute(query);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}