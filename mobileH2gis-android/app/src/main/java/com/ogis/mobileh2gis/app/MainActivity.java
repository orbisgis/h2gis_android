package com.ogis.mobileh2gis.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.h2gis.h2spatialext.CreateSpatialExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class MainActivity extends ActionBarActivity {
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection("jdbc:h2:mem:syntax", "sa", "sa");
            //this.st = connection.createStatement();
            // Import spatial functions, domains and drivers
            // If you are using a file database, you have to do only that once.
            CreateSpatialExtension.initSpatialExtension(connection);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            try {
                if (this.connection != null){
                    this.connection.close();
                }
                Class.forName("org.h2.Driver");
                this.connection = DriverManager.getConnection("jdbc:h2:mem:syntax", "sa", "sa");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void test1 (View v) {
        // Open memory H2 table
        try {
            if (this.connection != null) {
                this.connection.close();
            }
            //this.st = connection.createStatement();
            // Import spatial functions, domains and drivers
            // If you are using a file database, you have to do only that once.
            CreateSpatialExtension.initSpatialExtension(connection);
            // Create a table
            this.connection.createStatement().execute("CREATE TABLE ROADS (the_geom MULTILINESTRING, speed_limit INT)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void test2 (View v) {
        // Open memory H2 table
        try {
            // Add some roads
            this.connection.createStatement().execute("INSERT INTO ROADS VALUES ('MULTILINESTRING((15 5, 20 6, 25 7))', 80)");
            this.connection.createStatement().execute("INSERT INTO ROADS VALUES ('MULTILINESTRING((20 6, 21 15, 21 25))', 50)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void test3 (View v) {
        final TextView textViewToChange = (TextView) findViewById(R.id.textView);
            // Open memory H2 table
            try {
                // Compute the sum of roads length
                try {
                    ResultSet rs = this.connection.createStatement().executeQuery("SELECT SUM(ST_LENGTH(the_geom)) total_length from ROADS");
                    if(rs.next()) {
                        textViewToChange.setText("Total length of roads: "+rs.getDouble("total_length")+" m");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    public void send(View view) {
        final TextView textViewToChange = (TextView) findViewById(R.id.textView);
        final EditText textToSend = (EditText) findViewById(R.id.editText);
        try {
            String query = textToSend.getText().toString().trim();
            textViewToChange.setText(query+"\n");
            String firstWord = query.split(" ", 2)[0].toUpperCase();
            if (firstWord.equals("SELECT")) {
                ResultSet rs = this.connection.createStatement().executeQuery(query);

                // traitement;
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                String result="";
                while (rs.next()) {
                    for(int i=0; i<columnsNumber;i++){
                        result += rs.getString(i+1)+" ";
                    }
                    result+="\n";
                }
                textViewToChange.setText(textViewToChange.getText().toString()+result);
            } else {
                this.connection.createStatement().execute(query);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
