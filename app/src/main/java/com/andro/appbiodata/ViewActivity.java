package com.andro.appbiodata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewActivity extends AppCompatActivity {

    ListView listView;
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        listView = (ListView) findViewById(R.id.listView);
        getData();
    }

        void getData () {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

            AndroidNetworking.get("http://192.168.100.42:7000/view.php")
                    .setTag("Get Data")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jo = response.getJSONObject(i);
//                                arrNama.add(jo.getString("nama"));
                                    HashMap<String, String> teman = new HashMap<>();
                                    teman.put("id", jo.getString("id"));
                                    teman.put("nama", jo.getString("nama"));
                                    teman.put("email", jo.getString("email"));
                                    teman.put("phone", jo.getString("phone"));
                                    if (jo.getInt("jk") == 0) {
                                        sex = "Wanita";
                                    } else {
                                        sex = "Pria";
                                    }
                                    teman.put("jk", sex);
                                    teman.put("alamat", jo.getString("alamat"));
                                    teman.put("birthday", jo.getString("tgl_lahir"));
                                    list.add(teman);
                                }

                                ListAdapter adapter = new SimpleAdapter(
                                        ViewActivity.this, list, R.layout.list_item,
                                        new String[]{"nama", "email", "phone", "jk", "alamat", "birthday"},
                                        new int[]{R.id.tv_nama, R.id.tv_email, R.id.tv_phone, R.id.tv_jk, R.id.tv_alamat, R.id.tv_birthday});

                                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                        long deleteId = Long.parseLong(Objects.requireNonNull(list.get(position).get("id")));
                                        Log.d("ID: ", "[" + deleteId + "]");
                                        return true;
                                    };
                                });

                                listView.setAdapter(adapter);


                            } catch (Exception e) {
//
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        Log.d("Error ", anError.getMessage());
                        }
                    });
        }

    }