package website.jace.filereader;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        StringBuffer somethingToWrite = new StringBuffer();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "test/file.txt"));
            while(reader.ready()) {
                somethingToWrite.append(reader.readLine());
                somethingToWrite.append('\n');
            }
            reader.close();
        } catch (FileNotFoundException e) {
            somethingToWrite = new StringBuffer("Cannot find the file");
            e.printStackTrace();
        } catch (IOException e) {
            somethingToWrite = new StringBuffer("IOException");
            e.printStackTrace();
        }

        textView.setText(somethingToWrite.toString());
    }
}
