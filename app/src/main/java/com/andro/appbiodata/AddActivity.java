package com.andro.appbiodata;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Response;

public class AddActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText nama, birthday, email, phone, alamat;
    TextView viewBirtday;
    RadioGroup jk;
    RadioButton jkl;
    Button btnSimpan;
    String getNama;
    String getBirthday;
    String getEmail;
    String getPhone;
    String getAlamat;
    int getJk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nama = (EditText) findViewById(R.id.editName);
        birthday = (EditText) findViewById(R.id.editBirthDay);
        viewBirtday = (TextView) findViewById(R.id.editBirthDay);
        email = (EditText) findViewById(R.id.editEmail);
        phone = (EditText) findViewById(R.id.editPhone);
        alamat = (EditText) findViewById(R.id.editAlamat);
        jk = (RadioGroup) findViewById(R.id.editJk);

        btnSimpan = (Button) findViewById(R.id.btnSimpan);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDateDialog();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNama = nama.getText().toString();
                getBirthday = birthday.getText().toString();
                getEmail = email.getText().toString();
                getPhone = phone.getText().toString();
                getAlamat = alamat.getText().toString();

                jk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        jkl = (RadioButton) findViewById(checkedId);
                        if(jkl.getText() == "Wanita") {
                            getJk = 0;
                        } else {
                            getJk = 1;
                        }
                    }
                });
                validate();
            }
        });

    }

    void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                viewBirtday.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    void validate() {
        if(getNama.equals("") || getEmail.equals("") || getBirthday.equals("") || getPhone.equals("") || getAlamat.equals("")) {
            Toast.makeText(AddActivity.this, "Data kurang lengkap bos, benerin dong!", Toast.LENGTH_SHORT).show();
        } else {
            sendData();
        }
    }

    void sendData() {
        AndroidNetworking.post("http://192.168.1.8:7000/add.php")
                .addBodyParameter("nama", getNama)
                .addBodyParameter("email", getEmail)
                .addBodyParameter("phone", getPhone)
                .addBodyParameter("alamat", getAlamat)
                .addBodyParameter("jk", String.valueOf(getJk))
                .addBodyParameter("tgl_lahir", getBirthday).setPriority(Priority.MEDIUM).build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            try {
                            Intent intent = new Intent(AddActivity.this, ViewActivity.class);
                            startActivity(intent);
                            Toast.makeText(AddActivity.this, "Sukses menambahkan data", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddActivity.this, ""+ "Gagal menambah data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AddActivity.this, ""+ "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
//                        Log.d("Error ", anError.getMessage());
                    }
                });
    }
}