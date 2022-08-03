package kr.hs.emirim.s2102.mirim_project_0803_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText editName, editCount, editResultName, getEditResultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_people);
        editResultName = findViewById(R.id.edit_result_name);
        getEditResultCount = findViewById(R.id.edit_result_people);
        Button btnReset = findViewById(R.id.btn_reset);
        Button btnInput = findViewById(R.id.btn_input);
        Button btnResearch = findViewById(R.id.btn_research);
        btnReset.setOnClickListener(btnListener);
        btnInput.setOnClickListener(btnListener);
        btnResearch.setOnClickListener(btnListener);

        dbHelper = new DBHelper(this);
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        SQLiteDatabase db;
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_reset:
                    db = dbHelper.getWritableDatabase();
                    dbHelper.onUpgrade(db, 1, 2);
                    db.close();
                    break;
                case R.id.btn_input:
                    db = dbHelper.getWritableDatabase();
                    db.execSQL("insert into idolTBl values('"+editName.getText().toString()+"', "
                            +editCount.getText().toString()+");");
                    db.close();
                    Toast.makeText(getApplicationContext(), "새로운 idol정보가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editCount.setText("");
                    break;
                case R.id.btn_research:
                    db = dbHelper.getReadableDatabase();
                    Cursor c = db.rawQuery("select * from idolTBl;", null);
                    
                    String strName = "아이돌명\r\n____________\r\n";
                    String strCnt = "인원수\r\n____________\r\n";

                    while(c.moveToNext()) {
                        strName += c.getString(0) + "\r\n";
                        strCnt += c.getInt(1) + "\r\n";
                    }

                    editResultName.setText(strName);
                    getEditResultCount.setText(strCnt);

                    c.close();
                    db.close();

                    break;
            }
        }
    };

    public class DBHelper extends SQLiteOpenHelper {

        // db 생성
        public DBHelper(Context context) {
            super(context, "idolDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table idolTBl(name char(30) primary key, " + "cnt integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists idolTBl");
            onCreate(db);
        }
    }

}