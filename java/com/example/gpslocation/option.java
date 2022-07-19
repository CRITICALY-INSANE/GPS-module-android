package com.example.gpslocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class option extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout feild;
    private Button bt;
    private static final String FILE_NAME="GPSData.txt";
    private ArrayList<String> Button_info;
    final int TOP_ID = 3;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        bt=findViewById(R.id.test);
        
        //String data=load();
        ArrayList<String> details=load();
        Button_info=details;
        make_button(details);
        
        
    }
    
    private void make_button(ArrayList<String> details) {
        int val=0;
        feild=(LinearLayout)findViewById(R.id.for_button);
    
        while (details.size() > val) {
            System.out.println("bando "+(details.get(val)));
            val++ ;
        }
        int i;
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(0,30,0,30);
        String name;
        for( i=0;i<val;i++){
            System.out.println("output "+i+" = "+(details.get(i)));
            Button button = new Button(this);
            name=extract(details.get(i));
            button.setText(name);
            button.setId(i);
            button.setTextColor(getResources().getColor(R.color.black));
            button.setTextSize(16);
            button.setOnClickListener(this);
    
            button.setBackgroundResource(R.drawable.circle_but);
            button.setLayoutParams(marginLayoutParams);
            feild.addView(button);
            //top.addView(button);
        }
        
    }
    @Override
    public void onClick(View v) {
    
        int numer= (v.getId());
       String name= Button_info.get(numer);
        String currentString = name;
        String[] separated = currentString. split("=");
        // this will contain "Fruit"
        String nme=separated[0];
         Toast.makeText(option.this, "Locating  " +nme, Toast.LENGTH_SHORT).show();
    
        Intent k = new Intent(getApplicationContext(),MainActivity.class);
        String Loca=separated[1];
        
       String[] location= Loca.split(",");
        k.putExtra("Lat",location[0] );
        k.putExtra("Long",location[1]);
        startActivity(k);
        
    }
    private String extract(String s) {
        String currentString = s;
        String[] separated = currentString. split("=");
        String gg= separated[0];
        return gg;
    }
    
    private ArrayList<String> load() {
        File fp = new File(getFilesDir()+FILE_NAME);
        FileReader fr = null;
        try {
            fr = new FileReader(fp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);
        int val = 0;
    
        ArrayList<String> lines = new ArrayList<>();
        String line = null;
        while(true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            lines.add(line); }
    
        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (lines.size() > val) {
        
            
            System.out.println("bando "+(lines.get(val)));
        
            // Step 3: Terminating condition by incrementing
            // our counter in each iteration
            val++ ;
        }
        return lines;
        /*
        BufferedReader br = null;
        try {
            File file = new File(getFilesDir()+FILE_NAME); // java.io.File
            FileReader fr = new FileReader(file); // java.io.FileReader
            br = new BufferedReader(fr); // java.io.BufferedReader
            String line;
            while ((line = br.readLine()) != null) {
                Log.d(TAG, "onClick: "+line);
                bt.setText(line);
               
            }
        }
        catch(IOException e) { e.printStackTrace();}
        finally
        {
            try { if (br != null) br.close(); }
            catch(IOException e) { e.printStackTrace(); }
        }
        
        /*
        FileInputStream fis=null;
        String text = null;
        String passer = null;
        try {
            InputStream inStream = getApplicationContext().getAssets().open(FILE_NAME);
            Scanner s = new Scanner(inStream);
            String string = s.hasNext() ? s.next() : "";
            inStream.close();
            Log.d(TAG, "onClick: "+string);
        
        } catch (IOException e) {
            Log.d(TAG, "onClick: ");
            e.printStackTrace();
        }
    
        try {
            fis=openFileInput(FILE_NAME);
            BufferedReader br =new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb=new StringBuilder();
            
            
            while((text=br.readLine()) !=null){
                Log.d(TAG, "onClick: "+text);
    
                sb.append(text).append("\n");
                passer=sb.toString();
            }
            
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
        
        return passer;*/
        
    }
    
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    
    }
}