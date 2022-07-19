package com.example.gpslocation;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static android.content.ContentValues.TAG;


public class save_page extends AppCompatActivity {
    
    private Button save,edit;
    private EditText name,lati,longi;
    static String k, la,lo;
    private static final String FILE_NAME="GPSData.txt";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_page);
        String passedArg = getIntent().getExtras().getString("loca_loca");
       // Toast.makeText(this, passedArg, Toast.LENGTH_SHORT).show();
        lati=findViewById(R.id.lat);
        longi=findViewById(R.id.longi);
        save=findViewById(R.id.save);
        name=findViewById(R.id.nme);
        edit=findViewById(R.id.edit);
       
        String[] separated = passedArg.split(",");
         la = separated[0];
         lo = separated[1];
        lati.setText(la);
        longi.setText(lo);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            lati.setFocusableInTouchMode(true);
                longi.setFocusableInTouchMode(true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String w=name.getText().toString() ;
                String law,low;
                law=lati.getText().toString();
                low=longi.getText().toString();
                if(TextUtils.isEmpty(w)) {
                    alert();
                    return;
                }
                else{
                    save_function(w,law,low);
                
                }
            }
        });
        
    
    
    }
    
    public void alert() {
        k="Please Enter a Location Name";
        Toast.makeText(this,k,Toast.LENGTH_SHORT).show();
    
    }
    
    public void save_function(String lable,String la, String lo) {
        //shared pref
        //FileOutputStream fos=null;
        String s = ((lable + "=" + la + "," + lo)) + ("\n");
        String sk= s;
    
        File file = new File("");
        String currentPath = file.getAbsolutePath();
        System.out.println("Current path is:: " + currentPath);
        Log.d(TAG, "bando : "+currentPath);
    
        try(PrintWriter output = new PrintWriter(new FileWriter(getFilesDir() + FILE_NAME,true)))
        {
            output.printf(sk);
            Toast.makeText(this,"Location Saved Successfully",Toast.LENGTH_SHORT).show();
    
        }
        catch (Exception e) {
            System.out.println("bando " + e);
    
        }
        
        
        
        
        finish();
        /*
        try {
            fos=openFileOutput(FILE_NAME,MODE_PRIVATE);
            String saver=((lable+"="+la+","+lo))+("\n");
            fos.write(saver.getBytes());
            name.getText().clear();
            Toast.makeText(this,"Location Saved Successfully",Toast.LENGTH_SHORT).show();
    
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fos !=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        
    }
    
    private void load() {
        FileInputStream fis=null;
        try {
            fis=openFileInput(FILE_NAME);
            BufferedReader br =new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb=new StringBuilder();
            String text;
            
            while((text=br.readLine()) !=null){
                sb.append(text).append("\n");
            }
            Toast.makeText(this,sb.toString(),Toast.LENGTH_SHORT).show();
    
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(fis !=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    
    }
    
    
}