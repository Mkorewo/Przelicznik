package com.example.przelicznik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private TextView time;
    private RequestQueue http;
    private EditText input;
    private String currency1,currency2;
    public String[] signs = {"₣","Kč","€","£","Ƶ","$"};
    public int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        http= Volley.newRequestQueue(this);
        input=findViewById(R.id.input);
        input.setFilters(new InputFilter[] { filter });
        result=findViewById(R.id.result);
        time = findViewById(R.id.time);
        setTime();
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.currancies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(4);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currency1 =adapterView.getItemAtPosition(i).toString();
                if(input.getText().toString().equals("")||input.getText().toString().equals(".")){
                    fetch(0,signs[x]);
                }
                else{
                    fetch(Double.parseDouble(input.getText().toString()),signs[x]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currency2 =adapterView.getItemAtPosition(i).toString();
                x=i;
                if(input.getText().toString().equals("")||input.getText().toString().equals(".")){
                    fetch(0,signs[x]);
                }
                else{
                    fetch(Double.parseDouble(input.getText().toString()),signs[x]);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(input.getText().toString().equals("")||input.getText().toString().equals(".")){
                    fetch(0,signs[x]);
                }
                else{
                    fetch(Double.parseDouble(input.getText().toString()),signs[x]);
                }

            }
        });
        Button swap =findViewById(R.id.swap);
        swap.setOnClickListener(view -> {
            int i=spinner1.getSelectedItemPosition();
            spinner1.setSelection(spinner2.getSelectedItemPosition());
            spinner2.setSelection(i);
        });
    }
    private void fetch(double value,String sign){
        String url="https://free.currconv.com/api/v7/convert?q="+currency1+"_"+currency2+"&compact=ultra&apiKey=a934c8614acb8f431665";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        double rate=response.getDouble(currency1+"_"+currency2);
                        result.setText(String.format("%.2f "+sign+"%n", value*rate));
                        setTime();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        http.add(request);

    }
    private void setTime(){
        String url="https://free.currconv.com/others/usage?apiKey=a934c8614acb8f431665";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        time.setText("kurs z dnia: "+response.getString("timestamp").substring(0,10));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        http.add(request);
    }
    private final InputFilter filter = (source, start, end, dest, dstart, dend) -> {

        String blockCharacterSet = ",- @#$&_()=%*':/!?+£€¥¢©®™~¿[]{}<>^¡`;÷|¦¬×§¶°";
        if (source != null && blockCharacterSet.contains(("" + source))) {
            return "";
        }
        return null;
    };
}
//2303f2e5f5387569064d
//a934c8614acb8f431665
//https://free.currconv.com/others/usage?apiKey=a934c8614acb8f431665